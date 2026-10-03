import org.junit.Test;
public class FixtureFailureStudyTest {
    @Test public void setupFailureCannotBeFault() {
        GeneratedStudyTest.SqaProbe.observeWithPolicy("org.joda.time.Partial","org.joda.time.Chronology","<init>","",
            new double[]{Double.NaN,0,0},"aom-beam-champ-chronology-fixtures-v11-development");
    }
}
