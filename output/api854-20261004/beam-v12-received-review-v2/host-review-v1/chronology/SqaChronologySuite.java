import java.util.Base64;
import java.nio.charset.StandardCharsets;
public final class SqaChronologySuite {
    private static int failed=0;
    private static String quote(String s) { return "\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\""; }
    private static void check(String name,String cls,String ctor,String method,String params,String expected,double bucket) {
        String outcome="",error=null; boolean passed=false,invoked=false;
        try {
            outcome=SqaProbe.observeWithPolicy(cls,ctor,method,params,new double[]{bucket,0,0},"aom-beam-champ-graphics-fixtures-v12-development");
            invoked=SqaProbe.targetInvoked();
            if (!invoked || !outcome.equals(expected)) throw new AssertionError("Independent shared oracle differs");
            passed=true;
        } catch (AssertionError assertion) { failed++; }
          catch (Throwable fixture) { error=fixture.getClass().getName()+":"+fixture.getMessage(); failed++; }
        System.out.println("{\"case\":"+quote(name)+",\"target_invoked\":"+invoked+",\"passed\":"+passed
            +",\"fixture_error\":"+(error==null?"null":quote(error))+",\"outcome_b64\":"
            +quote(Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)))+"}");
    }
    public static void main(String[] args) {
        check("empty_iso_offset", "org.joda.time.Partial", "org.joda.time.Chronology", "<init>", "", "partial:org.joda.time.chrono.ISOChronology:UTC:types=[]:values=[]:named=true", -0.75);
        check("empty_null", "org.joda.time.Partial", "org.joda.time.Chronology", "<init>", "", "partial:org.joda.time.chrono.ISOChronology:UTC:types=[]:values=[]:named=true", 0.0);
        check("single_hour_iso", "org.joda.time.Partial", "org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology", "<init>", "", "partial:org.joda.time.chrono.ISOChronology:UTC:types=[hourOfDay]:values=[10]:named=true", -0.75);
        check("single_invalid_hour", "org.joda.time.Partial", "org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology", "<init>", "", "exception:org.joda.time.IllegalFieldValueException", 0.0);
        check("arrays_leap_iso", "org.joda.time.Partial", "[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology", "<init>", "", "partial:org.joda.time.chrono.ISOChronology:UTC:types=[year, monthOfYear, dayOfMonth]:values=[2024, 2, 29]:named=true:input-copy=true:output-copy=true", -0.75);
        check("arrays_invalid_date", "org.joda.time.Partial", "[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology", "<init>", "", "exception:org.joda.time.IllegalFieldValueException", 0.0);
        check("arrays_bad_order", "org.joda.time.Partial", "[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology", "<init>", "", "exception:java.lang.IllegalArgumentException", 0.75);
        check("internal_iso", "org.joda.time.Partial", "org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I", "<init>", "", "partial:org.joda.time.chrono.ISOChronology:UTC:types=[year, monthOfYear, dayOfMonth]:values=[2024, 2, 29]:named=true", -0.75);
        check("getfield_buddhist", "org.joda.time.Partial", "", "getField", "int,org.joda.time.Chronology", "field:year:epoch=2513:supplied-identity=true:type-year=true|receiver=partial:org.joda.time.chrono.ISOChronology:UTC:types=[year]:values=[2024]:named=true:unchanged=true", -0.75);
        check("getfield_bad_index", "org.joda.time.Partial", "", "getField", "int,org.joda.time.Chronology", "exception:java.lang.ArrayIndexOutOfBoundsException|receiver=partial:org.joda.time.chrono.ISOChronology:UTC:types=[year]:values=[2024]:named=true:unchanged=true", 0.0);
        check("withchrono_buddhist", "org.joda.time.Partial", "", "withChronologyRetainFields", "org.joda.time.Chronology", "partial:org.joda.time.chrono.BuddhistChronology:UTC:types=[hourOfDay]:values=[10]:named=true:same=false|receiver=partial:org.joda.time.chrono.ISOChronology:UTC:types=[hourOfDay]:values=[10]:named=true:unchanged=true", -0.75);
        check("withchrono_same", "org.joda.time.Partial", "", "withChronologyRetainFields", "org.joda.time.Chronology", "partial:org.joda.time.chrono.ISOChronology:UTC:types=[hourOfDay]:values=[10]:named=true:same=true|receiver=partial:org.joda.time.chrono.ISOChronology:UTC:types=[hourOfDay]:values=[10]:named=true:unchanged=true", 0.0);
        check("withchrono_null", "org.joda.time.Partial", "", "withChronologyRetainFields", "org.joda.time.Chronology", "partial:org.joda.time.chrono.ISOChronology:UTC:types=[hourOfDay]:values=[10]:named=true:same=false|receiver=partial:org.joda.time.chrono.BuddhistChronology:UTC:types=[hourOfDay]:values=[10]:named=true:unchanged=true", 0.75);
        if (failed>0) System.exit(1);
    }
}
