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
    assertEquals("value:[C[]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "buildResultArray", "", new double[]{-0.19275191598468638, 1, -0.29502854980607007}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:null", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getTextBuffer", "", new double[]{0.87231085730312385, 0.54488738352560939, -0.45305075975128561}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "_charArray", "int", new double[]{-0.80302803370382236, 0.4445014790358171, -0.40473810276689071, -0.45671139620883039, 0.43279433032780984, -0.73646665111255394}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getCurrentSegment", "", new double[]{0.77217563711311499, 0.045872834140195586, 0.090821818057883724}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "inLongRange", "[C,int,int,boolean", new double[]{0.011923870537382713, -0.4553092635391221, 1, -0.32926499126108122, -0.84840837512783807, 0.25126079012048497, 0.0093941525821895529, 0.54158053821501517, 0.11035764021659317, 1, -0.40707862171624226, 0.15945578831224816}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "clearSegments", "", new double[]{-0.28437430425406618, -0.99571928534083087, 0.38693633886282797}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("constructed:com.fasterxml.jackson.core.util.TextBuffer", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "<init>", "", new double[]{-0.3317815852655342, -0.11732062694621649, 0.19129711544027342}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsString", "", new double[]{1, 1, 0.42765683273523314}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:null", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getTextBuffer", "", new double[]{-0.41743967712960767, -0.12834228878667803, 0.87243104909043756}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:null", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getTextBuffer", "", new double[]{0.31069901760123675, 0.13745628932118678, 0.39948256790261466}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "append", "char", new double[]{0.5414362171196252, -0.37501790087675724, 0.81889869415101868, -0.04253261418341437, -0.4432496125896625, -0.60681950885111802}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.Integer:MTAwMA==", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseAsInt", "java.lang.String,int", new double[]{-0.13988999713580069, 0.28578780358062233, -0.62252330222629659, -1, 0.13275854955117239, -0.47442684617118092}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseLong", "java.lang.String", new double[]{-0.12495358660486183, 0.65014306271313915, -1}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseLong", "[C,int,int", new double[]{-0.048940561402034066, 0.098241569854101041, -0.32074568266068559, 0.089394277649833376, 0.23612566209194302, 1, 0.54857370078290668, 0.37764234315813267, 0.3883082131140933}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:java.lang.Integer:LTg5Nw==", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseAsInt", "java.lang.String,int", new double[]{-0.95192424951615706, 0.52361612961449011, -0.23837086294758156, -0.089685006738993128, 0.66118901841066613, -0.13322543255645222}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "setCurrentLength", "int", new double[]{0.6926533467311442, 0.23605887326142705, -0.57196053785512979, -0.27264094546422601, 0.14975475692745588, 0.2609168569385808}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:[C[]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsArray", "", new double[]{0.23087078873946848, 0.50145088488132328, 0.37974322001576599}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "[C", new double[]{0.54493339541046104, 0.47338495784783141, 0.17736761986204835}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "unshare", "int", new double[]{-0.85479259227582705, 1, 0.60795737673346495, -0.2282764541501065, 0.70858430425966157, 1}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "[C", new double[]{0.42887161968716964, -0.61622756076210594, -1}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "emptyAndGetCurrentSegment", "", new double[]{0.029674823015329248, 0.70474635186838575, -0.27571187604510738}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("constructed:com.fasterxml.jackson.core.util.TextBuffer", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "<init>", "", new double[]{-0.025396170665659579, -0.19836801964477557, -0.39289915690327964}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.StringIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "[C,int,int", new double[]{-0.24377554991224953, 0.22977365196256691, -1, -0.85210501193046162, -0.25222765240689943, 0.77379694931668086, 1, -0.059123640087039006, 0.53889960010500504}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:[C[]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsArray", "", new double[]{0.37128444446821052, 0.74861785057885488, -0.20132348232823905}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "inLongRange", "[C,int,int,boolean", new double[]{-0.27003316901333374, 0.32699074747735846, -1, -0.21533541188601718, 0.18685586997799225, -0.091049206101543156, 0.083008548690640893, 0.31927257468355064, 0.27258943527312207, 0.079106403081274812, -0.39574244618684806, -0.061063280355815208}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseInt", "[C,int,int", new double[]{-1, -0.070374490535561329, 0.015351895876205224, -0.41513871900056815, 0.23426782262870446, -0.27854684062204693, 1, 0.43653329845757771, 0.36515237005925572}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseInt", "java.lang.String", new double[]{0.099499998515165361, 0.6758344881640177, -0.18532608405913992}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "findBuffer", "int", new double[]{0.068208607571784152, 0.168129980297619, -0.84613469278288578, -1, 0.28141823798254018, 0.99009097428766246}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Double:LTIuMTQ3NDgzNjQ4RTk=", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseAsDouble", "java.lang.String,double", new double[]{0.4440045182722302, 0.25789246980083136, -0.22961389847290214, 1, -0.29294800874437815, 0.62339042064329953}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "inLongRange", "[C,int,int,boolean", new double[]{-0.18228870725346316, 0.95144911068771165, -0.34542760086817459, -0.51689930126657857, 0.68685630198513792, 0.28563113126129791, 0.28904166569443351, -1, 0.30550062976121861, 0.15991538920778975, 0.38019737087448618, 0.59132392859024208}));
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
