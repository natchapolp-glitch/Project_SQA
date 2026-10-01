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
    assertEquals("constructed:org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,java.lang.Object", "<init>", "", new double[]{-0.087403447416303459, 0.43769406820047285, 0.46977248517067904, -0.57019666300587324, -0.84392406176975188, 0.20548647253427887}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "attributeIterator", "org.apache.commons.jxpath.ri.QName", new double[]{0.33261841945060056, -0.80297233818223113, -0.20080957011256739, -0.78040302842538622, 0.33987684539258733, -0.40419205789090773, 0.93011581499929696, -0.034553895371156162, 0.32375347232801888}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "", "equalStrings", "java.lang.String,java.lang.String", new double[]{-0.90408353948070186, -0.7567250725309933, -0.36253660119363973, -0.51353696884213251, -0.71424191121575542, -0.32309315832829388}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:null", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "getLanguage", "", new double[]{-0.19949530143788818, 0.62934225385141151, -0.65351445745277537, 0.7795420162805009, 0.71802543657444984, -0.050305146010493074}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:null", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "", "getNamespaceURI", "java.lang.Object", new double[]{0.26132227026920418, -0.99533773728131925, -0.3153938101026188}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:object-type:org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "attributeIterator", "org.apache.commons.jxpath.ri.QName", new double[]{-0.023921819542193967, -0.34180833116452725, 0.17028559009400035, 0.56083745110340266, 0.45678531069355643, -0.51918012324786877, 0.81064935262165205, -0.19409395490926107, -0.3332392460699547}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getRelativePositionOfElement", "", new double[]{0.8463624281111346, 0.018986449647713011, 0.64289070600639975, 0.71911627103552345, 0.83868181670073905, -0.39511633031493254}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "asPath", "", new double[]{0.18420365173924913, 0.76895494029786593, -0.15251512325212135, -0.58374912622828257, -0.91275569878141027, 0.17081254455503836}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "", "getPrefix", "org.w3c.dom.Node", new double[]{0.98300736681172718, -0.88899977030569222, -0.099427091662782408}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:org.apache.commons.jxpath.JXPathException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "remove", "", new double[]{-0.47362890661156132, 0.2703692228929524, -0.23106310989456591, -0.84793484170788203, -0.52368222785054686, 0.74847847578165383}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "createChild", "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName,int", new double[]{0.73630408661290203, -0.78074564655750889, 0.90637066206064243, 0.27495469177462528, -0.17747020586486539, -0.039206232357909876, 0.19458013914998373, 0.92662511382430646, -0.10606903955779523, -0.46245260188117143, -0.29151393919364743, 0.7158604475972794, -0.44870359303936258, -0.4313400044338136, -0.31781003505401517}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:object-type:org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "attributeIterator", "org.apache.commons.jxpath.ri.QName", new double[]{-0.71331704350628078, 0.14811377095097344, 0.0094238225735900905, 0.18625098127516004, 0.52264179341442962, 0.9396193116520235, 0.98150000592511488, -0.54746451492091119, 0.84994623066224961}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "isLeaf", "", new double[]{-0.42331559803561936, 0.60655975148874086, 0.82341613518936829, -0.3890167783582732, 0.82548717243477876, -0.44955739165929276}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "", "equalStrings", "java.lang.String,java.lang.String", new double[]{0.59706055988511819, 0.80088610101072777, 0.61600315899472302, -0.095165659147714798, -0.90800556541881527, 0.479131847807581}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "setValue", "java.lang.Object", new double[]{-0.98267504110718762, -0.098640184566833211, -0.98542039009049498, -0.56510034601995129, -0.3917355565784495, 0.73632298522002015, 0.005230535880403675, 0.081689198651239137, 0.68747027596133825}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:null", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "", "getNamespaceURI", "java.lang.Object", new double[]{0.61612024143498645, 0.8014946276591659, 0.95204061765157033}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("constructed:org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.w3c.dom.Node,java.util.Locale,java.lang.String", "<init>", "", new double[]{0.032302886171538647, -0.86988609020710594, 0.9728200317225173, -0.076654133380858802, -0.59167874373972529, -0.80885307683195062, -0.46382029299104532, 0.52453628907629546, -0.83314195626861398}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "createChild", "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName,int,java.lang.Object", new double[]{0.92035777307631506, -0.036621116336100545, -0.35915452355349586, -0.37020095844194789, -0.73113019253444866, 0.075831155129356898, -0.47195541105714267, 0.90277321969449553, 0.58563061928639648, -0.96016468940448219, -0.52935357113207249, 0.075138494393926614, 0.88287729882047872, 0.88319061797959653, -0.57759726286710933, -0.024503500679825541, 0.096769139279799354, 0.94080091139416933}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:null", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "", "getLocalName", "java.lang.Object", new double[]{0.70444256072075806, -0.28214696857449728, -0.39212625364063913}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.ClassCastException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "setValue", "java.lang.Object", new double[]{0.3523090986924251, 0.14827823585950783, 0.79343326967946637, 0.54406508150619626, 0.54522136975402091, -0.20503996693996851, -0.20675611557199547, 0.6483616033730859, 0.29382047904182418}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "", "getPrefix", "org.w3c.dom.Node", new double[]{0.98009099012545287, 0.32003783747643055, -0.76707235845395183}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "", "equalStrings", "java.lang.String,java.lang.String", new double[]{-0.82845298249141686, -0.90412620345396055, 0.015625071413315483, 0.46389453299158778, 0.15035170808007603, 0.77960329238471848}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:object-type:org.apache.commons.jxpath.ri.model.jdom.JDOMNodeIterator", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "childIterator", "org.apache.commons.jxpath.ri.compiler.NodeTest,boolean,org.apache.commons.jxpath.ri.model.NodePointer", new double[]{0.85034229353550672, -0.33878776440079417, 0.11841400534778534, -0.43238100128429924, 0.089033998341692788, -0.64736173006535358, 0.8640549567754483, -0.70300565211321597, 0.66095133731963096, -0.81721336390484756, -0.99071244672004988, -0.41361398122847048, 0.8231140382812161, 0.29835821651219896, 0.87826294498043467}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.String:YT1i", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "escape", "java.lang.String", new double[]{-0.84067970982638096, 0.68641716465944036, 0.59504208780005108, -0.46960840952666949, -0.11742966044549097, 0.47095573900473275, 0.2460685220736325, 0.91293453351289311, -0.23707068327711678}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "isLeaf", "", new double[]{-0.5918703379615089, -0.73860651394907118, 0.12347188130486808, -0.83525763868213176, 0.59538890872898986, 0.845969954111645}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getRelativePositionOfElement", "", new double[]{-0.97247863078409247, -0.99543057617551001, -0.98049361988108208, 0.51826632159625441, -0.84163686250006498, -0.70877534720115598}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "createAttribute", "org.apache.commons.jxpath.JXPathContext,org.apache.commons.jxpath.ri.QName", new double[]{-0.74683450522862982, -0.2429972793080224, -0.78706598229483737, -0.76870024232563128, -0.89218784627692127, -0.0036851741306871411, -0.7935359995287441, -0.66086015909580476, 0.73201312443033784, 0.07029635244880339, -0.97621838644698311, -0.85502266103247537}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "setValue", "java.lang.Object", new double[]{-0.96139937646506679, -0.29211975195640427, -0.55972862252131605, 0.99493827750021802, 0.74692709318309425, -0.49037017368607749, 0.27352873241435427, 0.99614179082447052, 0.95290131548335766}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "compareChildNodePointers", "org.apache.commons.jxpath.ri.model.NodePointer,org.apache.commons.jxpath.ri.model.NodePointer", new double[]{-0.92426129241985833, 0.38816028572603178, 0.37978631247620021, 0.75219418337262534, -0.34589277972741472, 0.90803783577570329, 0.85440075128439652, 0.04146908731651644, 0.90882945946499061, -0.73616403498503113, -0.81270313897899826, -0.74838200319626802}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getRelativePositionOfPI", "java.lang.String", new double[]{0.16659551829246433, 0.76908033565517164, -0.19262606967064588, -0.46345067718591082, 0.24503158731196772, -0.90085760799321646, -0.36053661897535827, 0.67409915105877527, -0.8919524038956177}));
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
