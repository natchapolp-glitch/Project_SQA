import com.sun.jdi.Bootstrap;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.VirtualMachine;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.event.MethodEntryEvent;
import com.sun.jdi.event.VMDeathEvent;
import com.sun.jdi.event.VMDisconnectEvent;
import com.sun.jdi.request.EventRequest;
import com.sun.jdi.request.MethodEntryRequest;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/** Trace exact Partial descriptors only while a target case is active. */
public final class ChronologyEntryTrace {
    private static String quote(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
    private static Thread drain(InputStream stream, boolean error) {
        Thread thread = new Thread(() -> {
            try {
                String text = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
                if (error) System.err.print(text); else System.out.print(text);
            } catch (Exception failure) { throw new RuntimeException(failure); }
        });
        thread.start();
        return thread;
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Expected classpath");
        LaunchingConnector connector = Bootstrap.virtualMachineManager().defaultConnector();
        Map<String, Connector.Argument> config = connector.defaultArguments();
        config.get("main").setValue("org.joda.time.ChronologyProbe");
        config.get("options").setValue("-Duser.timezone=UTC -Dorg.joda.time.DateTimeZone.Provider=org.joda.time.tz.UTCProvider -cp \"" + args[0] + "\"");
        VirtualMachine vm = connector.launch(config);
        Thread out = drain(vm.process().getInputStream(), false);
        Thread err = drain(vm.process().getErrorStream(), true);
        MethodEntryRequest request = vm.eventRequestManager().createMethodEntryRequest();
        request.addClassFilter("org.joda.time.Partial");
        request.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
        request.enable();
        vm.resume();
        boolean done = false;
        int entries = 0;
        while (!done) {
            EventSet events = vm.eventQueue().remove(30000);
            if (events == null) throw new IllegalStateException("JDI event timeout");
            for (Event event : events) {
                if (event instanceof MethodEntryEvent) {
                    MethodEntryEvent entry = (MethodEntryEvent) event;
                    String name = entry.method().name();
                    if (name.equals("<init>") || name.equals("getField") || name.equals("withChronologyRetainFields")) {
                        ReferenceType probe = vm.classesByName("org.joda.time.ChronologyProbe").get(0);
                        String active = ((com.sun.jdi.StringReference) probe.getValue(probe.fieldByName("activeCase"))).value();
                        if (!active.isEmpty()) {
                            System.out.println("{\"method_entry\":true,\"case\":" + quote(active)
                                    + ",\"class\":" + quote(entry.method().declaringType().name())
                                    + ",\"method\":" + quote(name) + ",\"descriptor\":" + quote(entry.method().signature())
                                    + ",\"source_line\":" + entry.location().lineNumber() + "}");
                            entries++;
                        }
                    }
                } else if (event instanceof VMDeathEvent || event instanceof VMDisconnectEvent) done = true;
            }
            events.resume();
        }
        out.join(); err.join();
        int exit = vm.process().waitFor();
        System.out.println("{\"trace_summary\":true,\"method_entries\":" + entries + ",\"debuggee_exit_code\":" + exit + "}");
        if (exit != 0) System.exit(exit);
    }
}
