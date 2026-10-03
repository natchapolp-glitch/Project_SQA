import java.io.StringReader;
import java.util.Arrays;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser;

/** Standalone diagnostic candidate; it never changes the shared SqaProbe policy. */
public final class EnumBoundaryProbe {
    public static volatile String activeCase = "setup";

    private static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static String quote(String value) {
        if (value == null) return "null";
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r") + "\"";
    }

    private static String state(FromXmlParser parser) throws Exception {
        return "{\"format_features\":" + parser.getFormatFeatures()
                + ",\"closed\":" + parser.isClosed()
                + ",\"token\":" + quote(String.valueOf(parser.getCurrentToken()))
                + ",\"text\":" + quote(parser.getText()) + "}";
    }

    private static void exercise(String name) throws Exception {
        activeCase = "setup";
        String xml = "<root><item>45</item><other>beta</other></root>";
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(xml));
        try {
            while (reader.hasNext() && reader.getEventType() != XMLStreamConstants.START_ELEMENT) reader.next();
            require(reader.getEventType() == XMLStreamConstants.START_ELEMENT, "StAX setup failed");
            IOContext context = new IOContext(new BufferRecycler(), xml, false);
            FromXmlParser parser = new FromXmlParser(context, 0, 0, null, reader);
            try {
                int steps = 0;
                while (parser.nextToken() != JsonToken.VALUE_STRING && ++steps < 10) { }
                require(parser.getCurrentToken() == JsonToken.VALUE_STRING, "Parser token setup failed");
                require("45".equals(parser.getText()), "Parser text setup failed");
                require(!parser.isClosed(), "Parser unexpectedly closed during setup");
                String before = state(parser);
                NullPointerException caught = null;
                activeCase = name;
                try {
                    if (name.equals("configure_true")) parser.configure(null, true);
                    else if (name.equals("configure_false")) parser.configure(null, false);
                    else if (name.equals("enable")) parser.enable(null);
                    else if (name.equals("disable")) parser.disable(null);
                    else if (name.equals("isEnabled")) parser.isEnabled(null);
                    else throw new AssertionError("Unknown case");
                } catch (NullPointerException error) {
                    caught = error;
                } finally {
                    activeCase = "state_check";
                }
                require(caught != null, "Target did not throw candidate NPE: " + name);
                String method = name.startsWith("configure") ? (name.endsWith("true") ? "enable" : "disable") : name;
                StackTraceElement origin = caught.getStackTrace()[0];
                require(origin.getClassName().equals(FromXmlParser.class.getName())
                        && origin.getMethodName().equals(method), "Exception did not originate in target: " + name);
                if (name.startsWith("configure")) {
                    boolean delegated = false;
                    for (StackTraceElement frame : caught.getStackTrace()) {
                        if (frame.getClassName().equals(FromXmlParser.class.getName())
                                && frame.getMethodName().equals("configure")) delegated = true;
                    }
                    require(delegated, "configure frame missing from exception stack");
                }
                String after = state(parser);
                require(before.equals(after), "Parser state changed: " + name + " before=" + before + " after=" + after);
                JsonToken next = parser.nextToken();
                require(next == JsonToken.FIELD_NAME && "other".equals(parser.getCurrentName()),
                        "Parser could not continue after exception: " + name);
                require(parser.nextToken() == JsonToken.VALUE_STRING && "beta".equals(parser.getText()),
                        "Remaining XML value differs after exception: " + name);
                System.out.println("{\"case\":" + quote(name) + ",\"setup_succeeded\":true,"
                        + "\"exception\":\"java.lang.NullPointerException\",\"exception_origin_method\":" + quote(method)
                        + ",\"before\":" + before + ",\"after\":" + after
                        + ",\"continued_value\":\"beta\",\"target_check_passed\":true}");
            } finally {
                activeCase = "cleanup";
                parser.close();
            }
        } finally {
            reader.close();
        }
    }

    public static void main(String[] args) throws Exception {
        require(FromXmlParser.Feature.values().length == 0, "Production enum domain is not empty");
        for (String name : Arrays.asList("configure_true", "configure_false", "enable", "disable", "isEnabled")) exercise(name);
        System.out.println("{\"summary\":true,\"executed\":5,\"skipped\":0,\"fixture_errors\":0,\"target_checks\":5,\"enum_constants\":0}");
    }
}
