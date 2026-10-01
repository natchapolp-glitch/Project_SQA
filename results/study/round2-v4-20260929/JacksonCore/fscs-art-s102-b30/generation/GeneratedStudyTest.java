import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
public class GeneratedStudyTest {
  @Test(timeout=10000)
  public void generated1() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseInt", "java.lang.String", new double[]{0.23193057415244711, -0.66062835522866559, 0.42501389475041518}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.StringIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "[C,int,int", new double[]{-0.40576473126726964, 0.48662524938195695, -0.59694172408584723, -0.069481072015856959, 0.6133595374527856, 0.65947980870952883, -0.075297406738162209, 0.68546882580308699, 0.28473850697423009}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseLong", "[C,int,int", new double[]{0.47693362107852155, 0.86099927414502408, -0.58648776888262777, -0.95285928655245411, -0.65195765280463114, -0.56230325642957801, 0.3600032120306611, 0.64265813338426425, 0.63754758096888264}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "append", "[C,int,int", new double[]{0.88204116813066369, 0.95642981263937954, 0.88412610511170509, 0.14380416532443552, 0.77827753797217336, 0.18157988167243744, 0.69739545664340863, 0.97953360023835523, -0.25003040184721881, -0.75271930234102169, 0.50036493657084335, 0.87596223623071778}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "resetWithEmpty", "", new double[]{-0.074972587894792664, -0.10194735556238665, -0.75057856672896506}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{-0.44036633229521849, 0.77135693704992669, -0.95932509832423141}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "size", "", new double[]{-0.37425962348681252, -0.0049951400656771394, -0.80733833481649353}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{0.63808846865499547, -0.80114695712030759, 0.806329571196023}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "setCurrentLength", "int", new double[]{0.79457209146469276, -0.44314045313352945, 0.054614624296899228, -0.76603082339389905, 0.64810029788412882, 0.80379726303059207}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseInt", "[C,int,int", new double[]{0.23932492920947523, -0.98162668565096611, -0.74584636951008521, 0.95789076468411172, -0.95399137139437129, 0.61591139787927007, 0.98759485881324105, 0.39642808840977461, 0.95473305155383503}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:null", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getTextBuffer", "", new double[]{-0.80022701363926307, 0.78302878610324966, 0.42438595826055403}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "unshare", "int", new double[]{0.84158456890070288, 0.96219027301275051, -0.29005570078285325, -0.27517414564865428, -0.021185279234019028, -0.34638146961298566}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "setCurrentLength", "int", new double[]{-0.30320513755882694, -0.5640677737365285, 0.43003766522163822, 0.087081457470402412, 0.79890836133249388, 0.79133763233371024}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "[C", new double[]{-0.67159074272056052, 0.75892110301842397, 0.57967127547153674}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "java.lang.String", new double[]{-0.61150220663624633, -0.12354201357029937, -0.9479693912634457}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getTextOffset", "", new double[]{0.56034870853187368, -0.95854158913808396, -0.40360351610096767}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("constructed:com.fasterxml.jackson.core.util.TextBuffer", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "<init>", "", new double[]{0.66786459781545271, 0.80834387557285181, 0.94540986889651735}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "releaseBuffers", "", new double[]{-0.86126662476722982, -0.23052362687611283, 0.80342156700385514}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "toString", "", new double[]{0.63677334600622704, -0.85031746965309352, -0.735071530041147}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "expand", "int", new double[]{0.47640665164050322, -0.39195891295566221, 0.4756265949185654, -0.8674311172652136, 0.89245068457546339, -0.37533434482710759}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "emptyAndGetCurrentSegment", "", new double[]{-0.60167226644507954, 0.33188980634439047, -0.7596507358389315}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getCurrentSegmentSize", "", new double[]{-0.50513434260984158, 0.95205330673409838, 0.4947000059591049}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseLong", "[C,int,int", new double[]{-0.3895136468727638, 0.65538978180205332, 0.84394043575671329, -0.035521380836618821, -0.92187110479667678, -0.46225850466167828, 0.56100965410551251, 0.56377802548068567, -0.79306860160670722}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{0.20526319244449143, 0.54127773648914634, 0.70458123028951336}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getCurrentSegmentSize", "", new double[]{0.88150772576103975, 0.23671639094795105, -0.75410667981275536}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{-0.38770403526052721, -0.47068629885597413, 0.67551154727731477}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "setCurrentLength", "int", new double[]{-0.60576717647923317, -0.76586101090708913, -0.18363546805484132, 0.15856375647304866, -0.49749482400045975, 0.058446961497955474}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.Integer:MjIyMjI=", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseInt", "java.lang.String", new double[]{0.80946595282476475, -0.77835769951041556, -0.64459896762389124}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "emptyAndGetCurrentSegment", "", new double[]{-0.82299749525035115, 0.59582383628603575, -0.57497421147930727}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "unshare", "int", new double[]{0.76434477089621278, 0.96670456639301072, -0.88494564828976396, -0.23584260430542869, -0.97253322982189983, 0.39876910083625505}));
  }

/** Fixed-revision observations for explicitly supported, deterministic Java APIs.
 * No buggy source, patch, or triggering test is used during input generation.
 * The same source is packaged with the generated JUnit suite.
 */
public static final class SqaProbe {
    private static final String[] STRINGS = {
        "", "0", "1", "-1", "null", "true", "false", "abc", "ABC", " ",
        "0x0", "0x1", "0xFFFFFFFF", "1.0", "1e3", "NaN", "Infinity",
        "{}", "[]", "[1]", "{\"a\":1}", "a=b", "--help", "-x", "a,b",
        "1970-01-01", "a\\nb", "a\nb", "a\tb", "\u0e17\u0e14\u0e2a\u0e2d\u0e1a"
    };
    private static final long[] NUMBERS = {0, 1, -1, 2, -2, 10, -10, 127, 128,
        255, 256, 32767, -32768, Integer.MAX_VALUE, Integer.MIN_VALUE};

    private SqaProbe() { }

    private static String quote(String value) {
        StringBuilder out = new StringBuilder("\"");
        for (char c : value.toCharArray()) {
            if (c == '"' || c == '\\') out.append('\\').append(c);
            else if (c < 32) out.append(String.format("\\u%04x", (int)c));
            else out.append(c);
        }
        return out.append('"').toString();
    }

    private static String typeNames(Class<?>[] types) {
        List<String> names = new ArrayList<String>();
        for (Class<?> type : types) names.add(type.getName());
        return String.join(",", names);
    }

    private static boolean scalar(Class<?> type) {
        return type.isPrimitive() || type == String.class || type == Boolean.class
            || type == Character.class || type == Byte.class || type == Short.class
            || type == Integer.class || type == Long.class || type == Float.class
            || type == Double.class || type.isEnum();
    }

    private static boolean supported(Class<?> type) {
        return scalar(type) || (type.isArray() && scalar(type.getComponentType()));
    }

    private static boolean supportedParameters(Class<?>[] types) {
        if (types.length > 6) return false;
        for (Class<?> type : types) if (type == void.class) return false;
        return true;
    }

    private static Class<?> type(String name) throws ClassNotFoundException {
        if (name.equals("boolean")) return boolean.class;
        if (name.equals("byte")) return byte.class;
        if (name.equals("short")) return short.class;
        if (name.equals("int")) return int.class;
        if (name.equals("long")) return long.class;
        if (name.equals("float")) return float.class;
        if (name.equals("double")) return double.class;
        if (name.equals("char")) return char.class;
        return Class.forName(name);
    }

    private static Class<?>[] types(String names) throws ClassNotFoundException {
        if (names.length() == 0) return new Class<?>[0];
        String[] split = names.split(",", -1);
        Class<?>[] result = new Class<?>[split.length];
        for (int i = 0; i < split.length; i++) result[i] = type(split[i]);
        return result;
    }

    private static int bucket(double coordinate, int size) {
        double unit = Math.max(0, Math.min(1, (coordinate + 1) / 2));
        return Math.min(size - 1, (int)(unit * size));
    }

    private static Object argument(Class<?> type, double a, double b, double c) {
        return argument(type, a, b, c, 0);
    }

    private static Object argument(Class<?> type, double a, double b, double c, int depth) {
        if (depth > 2) return null;
        if (type.isArray()) {
            int length = bucket(c, 5);
            Object array = Array.newInstance(type.getComponentType(), length);
            for (int i = 0; i < length; i++) {
                Array.set(array, i, argument(type.getComponentType(),
                    Math.max(-1, Math.min(1, a + i * 0.17)), b, c, depth + 1));
            }
            return array;
        }
        if (!type.isPrimitive() && a < -0.96) return null;
        if (type == String.class) {
            int selection = bucket(a, STRINGS.length + 4);
            if (selection < STRINGS.length) return STRINGS[selection];
            int length = bucket(c, 33);
            char character = "0123456789abcdefXYZ +-_.".charAt(bucket(b, 23));
            char[] value = new char[length];
            Arrays.fill(value, character);
            return new String(value);
        }
        if (type == boolean.class || type == Boolean.class) return a >= 0;
        if (type == char.class || type == Character.class) return (char)bucket(a, 128);
        if (type.isEnum()) {
            Object[] values = type.getEnumConstants();
            return values.length == 0 ? null : values[bucket(a, values.length)];
        }
        long integer = b < 0 ? NUMBERS[bucket(a, NUMBERS.length)] : Math.round(a * 10000);
        if (type == byte.class || type == Byte.class) return (byte)integer;
        if (type == short.class || type == Short.class) return (short)integer;
        if (type == int.class || type == Integer.class) return (int)integer;
        if (type == long.class || type == Long.class) return integer;
        double real = b < 0 ? integer : a * 1000;
        if (type == float.class || type == Float.class) return (float)real;
        if (type == double.class || type == Double.class) return real;
        if (type == Number.class) return Double.valueOf(real);
        if (type == Object.class) return b < 0 ? STRINGS[bucket(a, STRINGS.length)] : Long.valueOf(integer);
        if (type == java.util.Date.class) return new java.util.Date(integer);
        if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)
            return new java.util.ArrayList<Object>();
        if (type == java.util.Set.class) return new java.util.HashSet<Object>();
        if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();
        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers()) && !type.getName().startsWith("java.")) {
            Constructor<?>[] constructors = type.getDeclaredConstructors();
            Arrays.sort(constructors, new Comparator<Constructor<?>>() {
                public int compare(Constructor<?> left, Constructor<?> right) {
                    int count = left.getParameterCount() - right.getParameterCount();
                    return count != 0 ? count : left.toString().compareTo(right.toString());
                }
            });
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() > 3) continue;
                try {
                    constructor.setAccessible(true);
                    Class<?>[] parameters = constructor.getParameterTypes();
                    Object[] values = new Object[parameters.length];
                    for (int i = 0; i < values.length; i++) values[i] = argument(parameters[i], a, b, c, depth + 1);
                    return constructor.newInstance(values);
                } catch (ReflectiveOperationException error) {
                    // Failed fixture construction yields an explicit null boundary input.
                } catch (RuntimeException error) {
                    // Encapsulated/unconstructible fixture yields the same null boundary.
                }
            }
        }
        return null;
    }

    private static Object[] arguments(Class<?>[] types, double[] vector, int offset) {
        Object[] values = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            int start = offset + 3 * i;
            values[i] = argument(types[i], vector[start % vector.length],
                vector[(start + 1) % vector.length], vector[(start + 2) % vector.length]);
        }
        return values;
    }

    private static String value(Object value) {
        if (value == null) return "null";
        Class<?> type = value.getClass();
        if (type.isArray()) {
            StringBuilder out = new StringBuilder(type.getName()).append('[');
            int length = Array.getLength(value);
            if (length > 100000) throw new IllegalStateException("SQA_HARNESS oversized outcome");
            for (int i = 0; i < length; i++) out.append(value(Array.get(value, i))).append(';');
            return out.append(']').toString();
        }
        if (value instanceof Class) return "class:" + ((Class<?>)value).getName();
        if (!scalar(type) && !(value instanceof Number)) return "object-type:" + type.getName();
        String text = value instanceof Enum ? ((Enum<?>) value).name() : String.valueOf(value);
        return type.getName() + ":" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    private static String snapshot(String observed) {
        // JVM string constants are limited to 65,535 encoded bytes. Long exact
        // observations use a deterministic digest rather than enormous literals.
        if (observed.length() <= 16000) return observed;
        byte[] bytes = observed.getBytes(StandardCharsets.UTF_8);
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(bytes);
            StringBuilder hex = new StringBuilder();
            for (byte item : digest) hex.append(String.format("%02x", item & 255));
            return "sha256:" + hex + ":bytes:" + bytes.length;
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException("SQA_HARNESS SHA-256 unavailable", error);
        }
    }

    public static String observe(String className, String constructorTypes, String methodName,
                                 String methodTypes, double[] vector) {
        if (vector.length == 0) throw new IllegalArgumentException("SQA_HARNESS empty vector");
        try {
            Class<?> target = Class.forName(className);
            Class<?>[] ctorTypes = types(constructorTypes);
            Class<?>[] parameterTypes = types(methodTypes);
            Object receiver = null;
            Method method = null;
            if (!methodName.equals("<init>")) {
                Class<?> declaring = target;
                while (declaring != null) {
                    try { method = declaring.getDeclaredMethod(methodName, parameterTypes); break; }
                    catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }
                }
                if (method == null) throw new NoSuchMethodException(methodName);
                method.setAccessible(true);
            }
            if (method == null || !Modifier.isStatic(method.getModifiers())) {
                Constructor<?> ctor = target.getDeclaredConstructor(ctorTypes);
                ctor.setAccessible(true);
                receiver = ctor.newInstance(arguments(ctorTypes, vector, 0));
            }
            if (method == null) return "constructed:" + target.getName();
            Object result = method.invoke(receiver, arguments(parameterTypes, vector, ctorTypes.length * 3));
            return method.getReturnType() == void.class ? "void" : snapshot("value:" + value(result));
        } catch (InvocationTargetException error) {
            Throwable cause = error.getCause();
            if (cause instanceof VirtualMachineError || cause instanceof LinkageError || cause instanceof ThreadDeath)
                throw new IllegalStateException("SQA_HARNESS JVM failure", cause);
            return "exception:" + cause.getClass().getName();
        } catch (ReflectiveOperationException error) {
            throw new IllegalStateException("SQA_HARNESS reflection failure", error);
        } catch (LinkageError error) {
            throw new IllegalStateException("SQA_HARNESS linkage failure", error);
        }
    }

    private static String descriptor(String className, String ctor, String method, String params, int count) {
        return "{\"class\":" + quote(className) + ",\"constructor_types\":" + quote(ctor)
            + ",\"method\":" + quote(method) + ",\"parameter_types\":" + quote(params)
            + ",\"dimensions\":" + Math.max(3, count * 3) + "}";
    }

    private static void discover(String[] classes, List<String> fixtureClasses) {
        List<String> targets = new ArrayList<String>();
        List<String> errors = new ArrayList<String>();
        for (String className : classes) {
            try {
                Class<?> target = Class.forName(className, false, SqaProbe.class.getClassLoader());
                Class<?> receiverType = target;
                if (Modifier.isAbstract(target.getModifiers())) {
                    for (String name : fixtureClasses) {
                        try {
                            Class<?> candidate = Class.forName(name, false, SqaProbe.class.getClassLoader());
                            if (!Modifier.isAbstract(candidate.getModifiers()) && target.isAssignableFrom(candidate)
                                    && candidate.getDeclaredConstructors().length > 0) {
                                receiverType = candidate;
                                break;
                            }
                        } catch (ClassNotFoundException ignored) { } catch (LinkageError ignored) { }
                    }
                }
                List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();
                if (!Modifier.isAbstract(receiverType.getModifiers()) && !receiverType.isEnum()) {
                    Constructor<?>[] all = receiverType.getDeclaredConstructors();
                    Arrays.sort(all, new Comparator<Constructor<?>>() {
                        public int compare(Constructor<?> a, Constructor<?> b) { return a.toString().compareTo(b.toString()); }
                    });
                    for (Constructor<?> ctor : all) {
                        if (supportedParameters(ctor.getParameterTypes())) constructors.add(ctor);
                    }
                    // Select a constructor before generating inputs; prefer the simplest fixture.
                    java.util.Collections.sort(constructors, new Comparator<Constructor<?>>() {
                        public int compare(Constructor<?> a, Constructor<?> b) { return a.getParameterCount() - b.getParameterCount(); }
                    });
                }
                Method[] methods = target.getDeclaredMethods();
                Arrays.sort(methods, new Comparator<Method>() {
                    public int compare(Method a, Method b) { return a.toString().compareTo(b.toString()); }
                });
                for (Method method : methods) {
                    if (method.isSynthetic() || method.getName().equals("main")
                        || method.isBridge() || !supportedParameters(method.getParameterTypes())
                        ) continue;
                    if (Modifier.isStatic(method.getModifiers())) {
                        targets.add(descriptor(className, "", method.getName(),
                            typeNames(method.getParameterTypes()), method.getParameterCount()));
                    } else if (!constructors.isEmpty()) {
                        Constructor<?> ctor = constructors.get(0);
                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), method.getName(),
                            typeNames(method.getParameterTypes()), ctor.getParameterCount() + method.getParameterCount()));
                    }
                }
                for (Constructor<?> ctor : constructors) {
                    if (ctor.getParameterCount() > 0)
                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), "<init>", "", ctor.getParameterCount()));
                }
            } catch (Throwable error) {
                if (error instanceof VirtualMachineError || error instanceof ThreadDeath) throw (Error)error;
                errors.add(quote(className + ":" + error.getClass().getName()));
            }
        }
        System.out.println("{\"targets\":[" + String.join(",", targets) + "],\"errors\":[" + String.join(",", errors) + "]}");
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("discover")) {
            int start = 1;
            List<String> fixtures = new ArrayList<String>();
            if (args.length > 2 && args[1].equals("--fixtures")) {
                fixtures = Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8);
                start = 3;
            }
            discover(Arrays.copyOfRange(args, start, args.length), fixtures);
            return;
        }
        if (args.length != 6 || !args[0].equals("observe"))
            throw new IllegalArgumentException("SQA_HARNESS expected discover classes or observe class ctor method types vector");
        String[] pieces = args[5].split(",");
        double[] vector = new double[pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            vector[i] = Double.parseDouble(pieces[i]);
            if (!Double.isFinite(vector[i]))
                throw new IllegalArgumentException("SQA_HARNESS nonfinite vector");
        }
        String outcome = observe(args[1], args[2], args[3], args[4], vector);
        System.out.println("SQA_RESULT:" + Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)));
    }
}
}
