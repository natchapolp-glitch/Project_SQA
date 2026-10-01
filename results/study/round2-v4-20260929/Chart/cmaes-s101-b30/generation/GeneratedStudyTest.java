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
    assertEquals("value:object-type:org.jfree.chart.labels.StandardCategorySeriesLabelGenerator", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItemLabelGenerator", "", new double[]{-0.19275191598468638, 1, -0.29502854980607007}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setPlot", "org.jfree.chart.plot.CategoryPlot", new double[]{-0.33753704139927643, 0.23283417482554164, -0.46267521452595367}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesToolTipGenerator", "int,org.jfree.chart.labels.CategoryToolTipGenerator,boolean", new double[]{0.12832943474678932, 0.77217563711311499, 0.045872834140195586, 0.090821818057883724, 0.1307339591918435, 0.093150983379294144, 0.20070505829727012, 0.53243585678343874, 0.74023053518358728}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "addEntity", "org.jfree.chart.entity.EntityCollection,java.awt.Shape,org.jfree.data.category.CategoryDataset,int,int,boolean", new double[]{0.25126079012048497, 0.0093941525821895529, 0.54158053821501517, 0.11035764021659317, 1, -0.40707862171624226, 0.15945578831224816, -0.25630375578480008, -0.28437430425406618, -0.99571928534083087, 0.38693633886282797, -0.99078136129558703, 0.23950509416686216, 0.29068450657000516, 0.25956817639741148, -0.94741527720343555, 0.50915387608824758, -0.4081749462454165}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawDomainLine", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D,double,java.awt.Paint,java.awt.Stroke", new double[]{-0.21025390616344158, 1, -0.3317815852655342, -0.11732062694621649, 0.19129711544027342, -0.87495743092009015, -0.27542383336715137, -0.12175711119727206, 0.16675745809446776, -0.20054893332519608, 1, 1, 0.76124648573485165, -4.6455512270862728e-05, 0.40579399477970773, 1, 1, 0.42765683273523314}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItemToolTipGenerator", "", new double[]{0.72038469793523263, 0.49371923411137969, 0.071252638175748256}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseItemLabelGenerator", "org.jfree.chart.labels.CategoryItemLabelGenerator,boolean", new double[]{-0.37471399254636167, 0.39895865985604273, 0.14174545310793471, 0.31069901760123675, 0.13745628932118678, 0.39948256790261466}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesItemLabelGenerator", "int,org.jfree.chart.labels.CategoryItemLabelGenerator", new double[]{-0.04253261418341437, -0.4432496125896625, -0.60681950885111802, 0.26202710574545679, -0.050106187479729405, 0.16510812825181748}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseURLGenerator", "org.jfree.chart.urls.CategoryURLGenerator", new double[]{0.22121045864752714, -0.45867890837502273, 0.14245789368506659}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getRowCount", "", new double[]{-0.042180809789216935, 0.44091243239650135, 0.22183176037519303}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getBaseItemLabelGenerator", "", new double[]{-0.030253853371241708, -1, -0.77087579699335562}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "addAnnotation", "org.jfree.chart.annotations.CategoryAnnotation,org.jfree.chart.util.Layer", new double[]{-0.17561465519680672, 0.71689414941506702, 0.50205195796701707, -0.043620017509802475, 0.089341850625662592, 0.95756262429139793}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawBackground", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{0.18931791128616896, 0.96609541209571592, 0.32544198883611541, 0.76340119294711384, -0.11187012190136103, 0.17021093547360819, -0.84878461007882744, 0.12425568416450583, 1}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getSeriesItemLabelGenerator", "int", new double[]{0.23189433201290313, -0.63222310125573866, -1}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setPlot", "org.jfree.chart.plot.CategoryPlot", new double[]{-1, 0.9959166306092837, 0.59014898250024506}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawOutline", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{-0.13416754655353505, 0.57560717266677586, -0.34127883297874106, -0.7080109036247445, -0.13739953507359737, -0.42825344625821959, 0.97206448053448979, 0.78727948322791064, 0.58878343334950245}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseToolTipGenerator", "org.jfree.chart.labels.CategoryToolTipGenerator,boolean", new double[]{-1, 0.77500528718063255, -0.0017601382467099524, -0.035880417263683601, -0.68287701746037144, 0.66108171420657258}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:object-type:org.jfree.chart.renderer.category.AreaRenderer", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "clone", "", new double[]{-0.53894951752510778, 0.47812721602010544, -1}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawBackground", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{-0.44539131972242685, 0.73343122703639341, -0.39763314145370504, -0.28080024611446947, 1, 0.62610347969211566, 0.11452983421571181, -0.014666191275760798, 0.92557599941037205}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getPlot", "", new double[]{0.53587048164359607, 0.081820754721882327, -0.49108131590407711}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseItemLabelGenerator", "org.jfree.chart.labels.CategoryItemLabelGenerator,boolean", new double[]{-0.28885924283126652, 1, 0.16039910717662023, -0.75835009005460274, -0.61462243089790103, 0.18704279346235397}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Integer:MQ==", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getPassCount", "", new double[]{-0.044193113423541278, 0.31359670994620092, 0.31843724163173048}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getPlot", "", new double[]{0.41225803617226864, -0.15910689184212312, 0.14529690971879963}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{-0.57226025704084116, 0.61232817927811745, -0.041379057453259677, -0.99362806382942881, 0.65318025587288897, -0.071775222840301245, 0.044795616049213713, -0.36262361490817263, 0.82713231406428089}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawDomainMarker", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.plot.CategoryMarker,java.awt.geom.Rectangle2D", new double[]{-0.11312538631173424, -0.17177334453121551, 0.62674600243900258, 0.016186600551158281, 0.43298877860103463, 0.71574304852047166, 1, 0.047820828548139349, 0.36550136578277803, -0.43181931769222393, 0.51194348822891, 0.82009504473438266, -0.0096354683178931713, 0.18430846851610208, 0.059068735403770001}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "removeAnnotations", "", new double[]{0.67935340874172678, 1, -0.68758206936715083}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getSeriesURLGenerator", "int", new double[]{0.73800646425322591, 1, -1}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{-0.13652714669783453, 0.34009753876776516, 0.23741815840262295, 0.040432286389681937, -0.470960676530217, -0.66034600136856292, 0.87100812614214085, -0.54534596618375442, 0.6208160872875389}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawBackground", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{-0.73334422438284397, -0.039059341589921193, 0.27046821451999026, -0.79032057091558683, 0.1705675930188571, 0.51155196719812501, -0.73091583156046835, -0.13102246038021167, 1}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItem", "int,int", new double[]{0.34115230804175323, 0.51419971041541268, -0.38052609174157948, 0.4733120761522524, 0.50524212794816692, 0.55611444388726272}));
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
