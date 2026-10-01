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
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseNode", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.Scope", new double[]{0.66590947354708863, 0.45803966823341336, 0.82928909475281765, -0.46043473069732355, -0.37045971698347407, -0.57359663273266814, -0.52191718761276218, 0.71039957628821593, 0.19781276765747477, -0.53069739928378745, -0.49621752434238758, 0.71086565841001481, -0.67208203741790584, -0.30835671335960285, 0.30024953046113839, 0.7585033920445543, 0.78813956625265424, -0.30385214520275933, -0.7644431060446405, -0.66061872754831097, 0.25327733207262509}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "removeUnreferencedVars", "", new double[]{0.72636126217288544, 0.51087477187740371, 0.66163265129573756, -0.61116365894584956, -0.6521928944867218, 0.0058964401248932582, 0.26354434127739718, -0.45589316586317874, 0.79367696683281352, -0.99369976831238582, -0.061978994107967769, -0.54903435929521083}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-1, 0.38923445281966829, -0.76325446625780369}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "<init>", "", new double[]{0.52106353457184451, 0.4377737687138975, -0.39717786622146456, -0.027040768233070524, 0.91094667782099037, 0.38631901537418206, -0.15264315247666979, -0.83746341027498661, 0.28477708035222149, 0.11327712920975604, 0.61074896830476733, 0.42739463154248336}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "removeUnreferencedFunctionArgs", "com.google.javascript.jscomp.Scope", new double[]{-0.61492945583721614, 0.016451544182311924, 0.38691521573118937, -0.28868213886604432, 0.94646078169459547, 0.20131234958452696, 0.64044855444759596, 0.39363532710196519, -1, -1, 0.27045988225061912, -0.26937884170222592, -0.33567425927367506, 0.30570041004511367, 0.20184972276381927}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseNode", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.Scope", new double[]{0.023077724888272519, 0.16852466938758731, 0.85178601545546928, -0.4028636675486596, -1, -0.42278628988583694, -1, -0.44031718489737826, -0.51941031617868361, 0.24903600263667564, -0.41056811449385672, 0.17220455960984593, -0.95206336371672895, 0.1419253215691843, -0.15720361529355009, 0.55313622569714627, 1, 0.41229488969394223, 0.51191509409529701, -0.55236638719610232, -0.41064250520665463}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "<init>", "", new double[]{-1, -0.17494384295644252, 0.27070669836112876, 0.56675650468942673, -0.33756113392432174, 0.0026923999637000853, 0.32730893608386846, 0.79196200442305997, 0.045022838105293263, -0.14805838790895745, 0.75048227767732145, 0.61399432939529197}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "collectMaybeUnreferencedVars", "com.google.javascript.jscomp.Scope", new double[]{-0.39211338418737302, -0.18590236012338116, 1, 0.67168422706608044, -0.64708300884038361, 0.73698768774142087, -0.19316466200026236, 0.81071383902249716, 0.44204507282359445, 0.79087190855236822, 0.61251238644515194, -0.24295561146200687, -0.32843363031835071, 0.12906200712853214, 0.18343469008746752}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "collectMaybeUnreferencedVars", "com.google.javascript.jscomp.Scope", new double[]{0.098027987629407579, 0.43356865309848763, -0.098757274068388454, -0.40442545172093824, 0.041817221465168017, 0.71125947920886989, 0.58175418966666026, -0.36823715281021518, 0.58740240625207663, -0.091069927165182737, -0.24227206222262337, 0.86205014156420334, 0.30724180123949268, 0.11578398620455005, -0.0045456745531381529}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.23351654859173449, 0.34443122209773869, -0.48785981153949515, -0.61010764145368057, 0.23871166726464293, -0.79970646440096582, 0.75330299588945582, 0.42827745797742672, -0.073882431720565744, -0.47625154306153283, 0.97258144903526911, -0.041970271052441797, -1, -0.55317534642915633, -1, 0.47753448270440435, -0.70357453080802224, -0.053104057572047558}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseFunction", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.Scope", new double[]{-0.29660724130663391, -0.10031824597580928, -1, 0.035170746971268256, 0.12624134422157623, 0.19375447155677999, 0.3745519002863163, -0.47411583832544346, -1, 0.38409662095745506, -0.83284251657844355, 1, -0.21745051842328189, -1, 0.50310931919235269, -0.087019123561774533, -0.44138727659673321, -0.3154425643828212}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{0.046260367438965028, -0.44999400812803575, 0.19243325913023804, 0.67095388853800675, 0.28869627137605913, -0.13613597381319056, 0.23501645859539808, -0.73689091033728449, 0.52526017317843454, 0.92343752266104939, 0.50467432105157795, 0.58615790934481027, 0.33811507504785504, -0.79193074139988573, 0.88031875235082813, 0.76694149038239823, -0.61160417887919216, -0.14578942549962295, 0.51109252301125407, 0.32459062404766753, -0.63880805908507687}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.72290694574733039, 0.21171041281706099, 0.55219075635975456}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "interpretAssigns", "", new double[]{0.14740071617382303, 0.21744007035160728, 1, -0.18152297962164404, -0.3879016505573486, -0.50009312906903169, 0.17768357780200533, -0.14594175897110703, 0.57027969278535817, 0.052079504406040034, 0.42473433571935348, -0.56197577010215682}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{1, -0.10253987017473054, 1, -0.849624084879953, -0.26887819087255116, 0.90457602670109138, -0.14112331242546614, 0.21227258029981527, -0.59764970409188489, -0.5946188170197404, -1, 0.15830666199054955, 0.089878756639827639, -0.063227141620435523, -0.5486495837518357, 1, 0.42095635897763639, -0.65155079335887112, -0.36930273622443055, -0.97013425026453382, -0.44986353433832721}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.18020234049597222, -0.75863731851635208, 0.52565224950357015}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.19970927663482574, 0.9691776051691029, 0.26843058314296508, -0.11228819113190591, 0.1738495094609962, -0.64318832140152149, -0.11323858216194035, 0.28534685510610636, -0.35313789010697771, -0.6402964606379552, -0.42576041258597663, -0.82024017792334902, -0.26137152338215119, 0.010712700679971418, 0.46682677558032559}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "markReferencedVar", "com.google.javascript.jscomp.Scope$Var", new double[]{-0.14709436268466586, 1, -0.4355077353962627, 0.24391636917333359, -0.54399385639791564, -0.47961624093966249, -0.6011566328382747, 1, 0.9292562291303732, -1, 0.24027915521562868, -0.90035669888099212, -0.59740035668919267, 0.015872323224501175, -0.22434238099388593}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.29899374900904607, 0.95983842413726517, 0.32947710914529188}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseNode", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.Scope", new double[]{0.90066526077341058, 0.90367914469931354, -0.77022613147410457, 0.10799528146281734, 0.28945100119445971, 0.30965128576977557, -0.57744691245700819, 0.31675291493455249, -0.31212694524441348, -0.70385971103379619, -0.013063043874116465, 0.22494919081475473, -0.9893323805626355, 0.44729420593916502, 1, 0.50261069654121104, 0.6375979013585763, -0.35190320636925326, -0.52806962565890603, -0.53512008028653857, -0.21376490973339279}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "<init>", "", new double[]{0.43570585964028635, -0.19909916837700836, -0.16595696558401757, -0.73822539348413097, -0.99274695023375292, -0.032694755323321445, 0.44529243616972175, -0.72708666599134431, -0.46152736673891048, -1, -0.53415751234634667, 0.46414518913609787}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "<init>", "", new double[]{-0.0075507414317912969, 0.70323528257562862, 0.90241472297167769, -0.29975249505462292, 0.37265615893915988, -0.34692454926832339, -0.8972679509115784, -0.55203454158894982, -0.65450192492951742, -0.19075097918495071, -0.026953167535913616, -0.22942437312030758}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseNode", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.Scope", new double[]{0.35711955384113081, 0.31507508504350434, -0.35756981190846776, -1, 1, -0.48986137205587788, -0.65931046350202849, -0.093623645320504628, -1, -0.24311504792654898, -0.39642315862900035, 0.065260291641458093, -0.69734015428311236, -1, 1, 0.42026287805823909, -0.07977714857507856, -0.89735003770131594, -0.45252669152175445, -0.54011643652246522, -0.61644220928386584}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "interpretAssigns", "", new double[]{0.46595035067307872, 1, -0.33062670951906808, -0.48887381667177027, 0.43596991369843358, 0.44033915530798223, -0.60823714665152984, -0.42119402263534178, -0.32213010365520461, -0.39360502158959682, 0.67983413784865843, -0.01560929530810444}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.72736201782529819, 0.38297353590975136, 0.708495776603004, -0.061681209171587592, -0.15496896234888025, 0.41521668746888613, 0.33344862913781903, 0.67368980153645019, 0.083185975520318048, -0.53236262095931497, 0.71521385272075233, -0.2803774294962319, 0.25417464154250541, -1, -0.85569507925720312}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "removeAllAssigns", "com.google.javascript.jscomp.Scope$Var", new double[]{-0.21132274961957676, -0.10010974308276233, -0.033511655982948529, -0.35504662598299397, 0.12148048235866515, -1, 1, -0.4356726273166005, 0.10050988294031535, -1, 0.29707668319867908, -0.35269420327298873, 0.13556489143981459, -0.48750519988214086, 0.64207607159562019}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "removeAllAssigns", "com.google.javascript.jscomp.Scope$Var", new double[]{-1, 0.63272044831988727, 0.10602791685500818, -0.47614744506518802, -0.55584349818287548, -0.66636388416369585, 0.25262197586640806, 0.37936953113399829, -0.92743534365547953, -0.080842487832094356, -0.34172837129259181, 0.22713877308867503, -0.42636290641857288, -0.8081823862289742, -0.15749250389585479}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "removeAllAssigns", "com.google.javascript.jscomp.Scope$Var", new double[]{-0.20704242181708601, 0.44521981082616657, 0.4891395039203501, -0.33591883654934074, -0.013137870154698, -0.040154292007138603, 0.03536699242674142, 0.067654841472983993, -0.062896011663355011, -0.10889331215352899, 0.025160606471029065, -0.40386975577712914, -0.31113121075769212, 0.36443575341583945, 0.042907872650218115}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.5587663272491169, 0.5255974396522457, 0.050725771390380647, -1, -0.70746206515098931, 0.41737726902453454, 0.052310654810091617, 0.21878601403512193, 0.36130409509779615, -0.99209607284112, 0.10303489587853513, -1, 0.5182563571308676, 0.63850327469514367, 0.5573059227117243}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.42802511186233061, -0.42995321404242537, 0.88840380837621469, -0.10673584034618416, -1, -0.055462612971025838, -0.1059480139135337, -0.50524001788264905, -0.0030041714119858898, -1, -0.51764150741690684, -0.51994596960415684, -0.71320716239361004, -0.61541002826187874, 0.08254686551403001, 0.94102967414296446, 0.50242824901208716, 0.065199492466549047}));
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
