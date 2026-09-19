import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;

/** Generated from fixed revision behaviour for 0x0000000000000000000010000000000000. */
public class GeneratedLang1RegressionTest {
    @Test
    public void preservesLeadingZeroHexClassification() {
        Number actual = NumberUtils.createNumber("0x0000000000000000000010000000000000");
        assertEquals("java.lang.Long", actual.getClass().getName());
        assertEquals("4503599627370496", actual.toString());
    }
}
