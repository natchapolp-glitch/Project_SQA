import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/** Exact first production entry during the one target invocation per declared case. */
public final class CollectionsEntryTrace {
    static String q(String s){return "\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\"";}
    static Thread drain(InputStream stream,boolean error) {
        Thread t=new Thread(()->{try{String s=new String(stream.readAllBytes(),StandardCharsets.UTF_8);
            if(error)System.err.print(s);else System.out.print(s);}catch(Exception e){throw new RuntimeException(e);}});
        t.start();return t;
    }
    public static void main(String[] args) throws Exception {
        LaunchingConnector connector=Bootstrap.virtualMachineManager().defaultConnector();
        Map<String,Connector.Argument> config=connector.defaultArguments();
        config.get("main").setValue("sqa.development.CollectionsCandidateProbe "+q(args[1]));
        config.get("options").setValue("-Duser.timezone=UTC -Duser.language=en -Duser.country=US -cp "+q(args[0]));
        VirtualMachine vm=connector.launch(config);Thread out=drain(vm.process().getInputStream(),false),err=drain(vm.process().getErrorStream(),true);
        for(String owner:Arrays.asList("org.apache.commons.collections.map.Flat3Map")) {
            MethodEntryRequest r=vm.eventRequestManager().createMethodEntryRequest();r.addClassFilter(owner);
            r.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);r.enable();
        }
        Set<String> names=new HashSet<String>(Arrays.asList("<init>","convertToMap","createDelegateMap","entrySet","hashCode","keySet","mapIterator","readObject","values","writeObject"));
        vm.resume();boolean done=false;int count=0;
        while(!done) {
            EventSet events=vm.eventQueue().remove(30000);if(events==null)throw new IllegalStateException("JDI event timeout");
            for(Event event:events) {
                if(event instanceof MethodEntryEvent) {
                    MethodEntryEvent e=(MethodEntryEvent)event;
                    if(names.contains(e.method().name())) {
                        ReferenceType probe=vm.classesByName("sqa.development.CollectionsCandidateProbe").get(0);
                        String active=((StringReference)probe.getValue(probe.fieldByName("activeCase"))).value();
                        if(!active.isEmpty()) {
                            System.out.println("{\"method_entry\":true,\"case\":"+q(active)+",\"class\":"+q(e.method().declaringType().name())+
                                ",\"method\":"+q(e.method().name())+",\"descriptor\":"+q(e.method().signature())+
                                ",\"source_line\":"+e.location().lineNumber()+"}");count++;
                        }
                    }
                } else if(event instanceof VMDeathEvent||event instanceof VMDisconnectEvent)done=true;
            }events.resume();
        }
        out.join();err.join();int exit=vm.process().waitFor();
        System.out.println("{\"trace_summary\":true,\"method_entries\":"+count+",\"debuggee_exit_code\":"+exit+"}");
        if(exit!=0)System.exit(exit);
    }
}
