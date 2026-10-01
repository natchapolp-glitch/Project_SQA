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
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "isLanguage", "java.lang.String", new double[]{0.23193057415244711, -0.66062835522866559, 0.42501389475041518, 0.21566498551494262, -0.23132505576605888, 0.23499526722893549, 0.13250487418939949, -0.91355549209864462, 0.060236521705815838}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("constructed:org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale,java.lang.String", "<init>", "", new double[]{-0.96304950195694539, -0.09554478487794138, 0.98930809070094106, 0.66606765917338406, 0.97875982970856268, 0.058378662565180761, 0.94353905352808032, -0.88808901720270694, -0.056066811595451238}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "", "getLocalName", "org.w3c.dom.Node", new double[]{0.97016354587519271, 0.6678635649289244, 0.042988119834957539}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "equals", "java.lang.Object", new double[]{-0.94590893554895072, 0.98370986004079009, -0.20195272950542376, 0.34610948219834503, -0.54110870040928116, -0.15312394714290001, -0.76443763454046865, 0.93607179533806151, -0.91623173290158633}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.ClassCastException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "addContent", "java.util.List", new double[]{-0.16003128794545685, 0.38439771957165303, 0.94995393674000739, -0.81180106929639151, -0.1328238114672271, -0.723267248446674, -0.78891953989614239, 0.14350856935522893, -0.62617562371459345}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "equals", "java.lang.Object", new double[]{-0.075829788876811133, 0.74908856208439834, 0.18457825768436043, -0.93897535037075719, -0.56608668573655496, 0.66904735559406037, -0.68866515620717705, -0.43197920860697114, 0.64387951409565058}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "isLeaf", "", new double[]{-0.54635768019861586, 0.93928096685646256, -0.68172411687395318, -0.043730687302581783, 0.17463910078418987, -0.70918208192753296}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getRelativePositionOfTextNode", "", new double[]{0.62525199206421789, -0.53999635403103463, -0.96637423033087577, -0.57414521659369666, -0.59138529801703421, 0.14498552376020823}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "namespaceIterator", "", new double[]{-0.5640677737365285, 0.43003766522163822, 0.087081457470402412, 0.79890836133249388, 0.79133763233371024, 0.68750994969846912}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getRelativePositionOfTextNode", "", new double[]{0.95734655159440152, 0.35209137832038051, 0.49354999593045989, -0.75353270600974254, -0.60947225581860431, 0.10015543622789314}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getRelativePositionOfTextNode", "", new double[]{-0.68262039806887609, -0.53242537535929579, -0.31054536257194454, 0.61175128298268966, -0.13534052395300655, 0.94745716666631186}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:null", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getBaseValue", "", new double[]{-0.72298791879065027, 0.58142199578510256, -0.3007930989691725, -0.85915624995603235, -0.37015588644799857, 0.65107549576484103}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:object-type:org.apache.commons.jxpath.ri.model.jdom.JDOMNamespaceIterator", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "namespaceIterator", "", new double[]{0.78658817449004159, 0.82678616060288346, 0.92226478769584141, -0.65860549423755477, -0.056440584061172139, -0.95658303725301175}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:null", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "getNamespaceURI", "java.lang.String", new double[]{-0.5133217174811735, -0.63061226764162348, -0.34289659230078895, 0.4821825223716687, 0.036198321491276664, 0.98094613536449837, -0.9346867988827261, 0.97042906907132309, 0.078797641498781879}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("constructed:org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.w3c.dom.Node,java.util.Locale", "<init>", "", new double[]{-0.56281809768617408, -0.27147292901490028, -0.37707046494792329, -0.29340221319163717, -0.19734522518083342, -0.30363460328233915}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:org.apache.commons.jxpath.JXPathException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "remove", "", new double[]{0.20159627074957398, -0.8824399412148578, 0.10124822717217263, 0.62982816612544035, -0.58110052145698132, 0.2727595613134326}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:object-type:org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "attributeIterator", "org.apache.commons.jxpath.ri.QName", new double[]{0.35595154873962631, 0.40877678086225333, -0.34187990663230794, -0.64772250514343233, 0.22626804824229185, 0.27932506954216807, -0.69766609271788216, -0.98747465917557409, -0.83035680374425458}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "createChild", "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName,int", new double[]{-0.18883600680748636, 0.32732721779127338, 0.70980731489958249, -0.88249019517565341, -0.91324572932248471, 0.58143447605677201, 0.01391455309585643, 0.53221291506796065, 0.53318614156111321, 0.56662562024270979, 0.74591201595436218, -0.29983594059229968, -0.084065014980448272, 0.56775680881804558, -0.71813438707724941}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:org.apache.commons.jxpath.JXPathException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "remove", "", new double[]{0.91429366901761111, -0.2358037632573875, -0.9192943758727119, 0.76715540590738351, -0.26803569733735477, -0.91160393556423247}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.String:YQli", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "escape", "java.lang.String", new double[]{-0.33542883236255716, 0.95867008417436939, -0.38352814771351595, -0.024234694873279006, -0.73009178867571833, 0.49562145244906874, 0.68000403335230963, -0.59052886155926432, -0.33996635649187512}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "", "testNode", "org.apache.commons.jxpath.ri.model.NodePointer,java.lang.Object,org.apache.commons.jxpath.ri.compiler.NodeTest", new double[]{-0.50604924606010115, 0.39610955528002867, 0.6223297132863439, 0.71923866828913741, 0.46479642563532075, 0.35053130782259578, -0.91565831465027636, 0.91123360661780572, -0.28403539840550462}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("constructed:org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale,java.lang.String", "<init>", "", new double[]{0.83455216960293921, 0.86493342558719677, 0.10723358581708364, -0.30919188445116341, 0.56806397290580901, -0.053280811168985975, 0.47792512473337334, -0.34733325470302345, 0.54265622330742502}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "", "testNode", "org.w3c.dom.Node,org.apache.commons.jxpath.ri.compiler.NodeTest", new double[]{-0.11310915166193158, -0.50177518836599821, -0.89892224535444676, -0.51358249106831133, 0.86225650045529489, -0.3219244003398305}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "attributeIterator", "org.apache.commons.jxpath.ri.QName", new double[]{-0.16583066807986535, 0.75297532244454635, 0.41500082302271535, 0.88710766560871024, -0.64970297376262987, -0.97858305455334227, -0.47987582093788528, 0.74596446039852249, -0.38704214983516572}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "createAttribute", "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName", new double[]{0.59485905336959655, -0.06997026633490866, 0.60546458311478557, 0.92729264772752229, 0.82052970158028571, -0.857737777934793, -0.58960528260985168, 0.1101098633652966, -0.74123027109281536, -0.17784066073046856, -0.88046953382517534, 0.10434148731764958}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("constructed:org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.w3c.dom.Node,java.util.Locale", "<init>", "", new double[]{0.80873709652887515, -0.030149623895821209, -0.034904355772599871, -0.80195493355095704, -0.97479345468689238, -0.93954822068480892}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.ClassCastException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "addContent", "java.util.List", new double[]{-0.16517282242511899, -0.70374520156088782, -0.066703131215466005, 0.26051121678058897, 0.64743224073917238, -0.37363616421094692, -0.94640755861057446, -0.89152499703635635, -0.48926616014255031}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:null", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "getNamespaceURI", "java.lang.String", new double[]{-0.97544062551276745, -0.65250270540392186, -0.8297926794270909, 0.34373473887299699, -0.30772804075003268, 0.119076294278462, -0.55522229308972326, -0.69597476263657576, 0.45857544175916787}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:null", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "", "getLocalName", "java.lang.Object", new double[]{0.095701579826987571, -0.96226514530344365, 0.64722518292712583}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "createChild", "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName,int,java.lang.Object", new double[]{-0.72275415414570854, -0.49957504009358966, -0.98244739286666483, -0.82600607888876398, -0.96821379975298627, -0.89202781340742643, 0.91337843580297307, 0.38309490162799542, -0.31188900053375357, -0.60427945432302876, 0.63618298594587186, -0.029683894488929274, -0.3275112471426127, -0.53718827805682379, -0.39817132790996435, 0.72065806194862114, -0.94570047351364162, -0.68506170781772457}));
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
