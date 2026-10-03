import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Trace inherited renderer declarations only during the exact target invocation. */
public final class GraphicsEntryTrace {
    private static String quote(String value) { return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\""; }
    private static Thread drain(InputStream stream, boolean error) {
        Thread thread = new Thread(() -> {
            try {
                String text = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                if (error) System.err.print(text); else System.out.print(text);
            } catch (Exception failure) { throw new RuntimeException(failure); }
        }); thread.start(); return thread;
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 2) throw new IllegalArgumentException("Expected classpath and fresh pixel directory");
        LaunchingConnector connector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String,Connector.Argument> config = connector.defaultArguments();
        config.get("main").setValue("SqaGraphicsSuite " + quote(args[1]));
        config.get("options").setValue("-Djava.awt.headless=true -Duser.timezone=UTC -cp " + quote(args[0]));
        VirtualMachine vm = connector.launch(config);
        Thread out = drain(vm.process().getInputStream(), false), err = drain(vm.process().getErrorStream(), true);
        MethodEntryRequest request = vm.eventRequestManager().createMethodEntryRequest();
        request.addClassFilter("org.jfree.chart.renderer.category.AbstractCategoryItemRenderer");
        request.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD); request.enable(); vm.resume();
        Set<String> names = new HashSet<String>(Arrays.asList("drawAnnotations", "drawBackground", "drawDomainLine", "drawDomainMarker", "drawOutline", "drawRangeMarker", "initialise"));
        boolean done = false; int count = 0;
        while (!done) {
            EventSet events = vm.eventQueue().remove(30000);
            if (events == null) throw new IllegalStateException("JDI event timeout");
            for (Event event : events) {
                if (event instanceof MethodEntryEvent) {
                    MethodEntryEvent entry = (MethodEntryEvent) event;
                    if (names.contains(entry.method().name())) {
                        ReferenceType probe = vm.classesByName("SqaProbe$GraphicsRecipe").get(0);
                        String active = ((StringReference) probe.getValue(probe.fieldByName("activeCase"))).value();
                        if (!active.isEmpty()) {
                            System.out.println("{\"method_entry\":true,\"case\":" + quote(active)
                                    + ",\"class\":" + quote(entry.method().declaringType().name())
                                    + ",\"method\":" + quote(entry.method().name()) + ",\"descriptor\":" + quote(entry.method().signature())
                                    + ",\"source_line\":" + entry.location().lineNumber() + "}"); count++;
                        }
                    }
                } else if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) done = true;
            } events.resume();
        }
        out.join(); err.join(); int exit = vm.process().waitFor();
        System.out.println("{\"trace_summary\":true,\"method_entries\":" + count + ",\"debuggee_exit_code\":" + exit + "}");
        if (exit != 0) System.exit(exit);
    }
}
