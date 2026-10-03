
/** Diagnostics of the unchanged shared helper; fixture failures are not bug detections. */
public final class SharedChronologyDiagnosticV2 {
    private static String quote(String s) {
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r") + "\"";
    }
    public static void main(String[] args) {
        String[][] targets = {
            {"", "getField", "int,org.joda.time.Chronology"},
            {"", "withChronologyRetainFields", "org.joda.time.Chronology"},
            {"[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology", "<init>", ""},
            {"org.joda.time.Chronology", "<init>", ""},
            {"org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I", "<init>", ""},
            {"org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology", "<init>", ""}
        };
        for (int index=0; index<targets.length; index++) {
            String[] target=targets[index]; String outcome=null, errorClass=null, message=null;
            try { outcome=SqaProbe.observeWithPolicy("org.joda.time.Partial",target[0],target[1],target[2],new double[]{0.5,0.5,0.5},args[0]); }
            catch (RuntimeException error) { errorClass=error.getClass().getName(); message=String.valueOf(error.getMessage()); }
            System.out.println("{\"case\":"+index+",\"constructor_types\":"+quote(target[0])+",\"method\":"+quote(target[1])
                +",\"parameter_types\":"+quote(target[2])+",\"target_invoked\":"+SqaProbe.targetInvoked()
                +",\"outcome\":"+(outcome==null?"null":quote(outcome))+",\"error_class\":"+(errorClass==null?"null":quote(errorClass))
                +",\"error_message\":"+(message==null?"null":quote(message))+"}");
        }
    }
}
