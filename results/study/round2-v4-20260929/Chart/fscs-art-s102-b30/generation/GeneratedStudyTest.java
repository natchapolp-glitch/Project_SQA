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
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getColumnCount", "", new double[]{0.23193057415244711, -0.66062835522866559, 0.42501389475041518}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{-0.96304950195694539, -0.09554478487794138, 0.98930809070094106, 0.66606765917338406, 0.97875982970856268, 0.058378662565180761, 0.94353905352808032, -0.88808901720270694, -0.056066811595451238}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getToolTipGenerator", "int,int,boolean", new double[]{0.97016354587519271, 0.6678635649289244, 0.042988119834957539, -0.68074868458019577, 0.22390113094601483, 0.20307961323644586, 0.16053577493428817, 0.55771537678416849, -0.045078828668711379}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawDomainMarker", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.plot.CategoryMarker,java.awt.geom.Rectangle2D", new double[]{-0.94590893554895072, 0.98370986004079009, -0.20195272950542376, 0.34610948219834503, -0.54110870040928116, -0.15312394714290001, -0.76443763454046865, 0.93607179533806151, -0.91623173290158633, -0.85478111155718528, 0.45325581333020315, 0.15256293502322937, -0.35438036718577326, 0.881777855581656, 0.39767926868173498}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawDomainLine", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D,double,java.awt.Paint,java.awt.Stroke", new double[]{-0.16003128794545685, 0.38439771957165303, 0.94995393674000739, -0.81180106929639151, -0.1328238114672271, -0.723267248446674, -0.78891953989614239, 0.14350856935522893, -0.62617562371459345, -0.79341539074497591, 0.16073552650584322, 0.10902029336505015, -0.0088161530306096747, -0.94969575933633155, -0.73161148309047541, 0.078405446341503593, -0.87292039358941476, -0.78115852919218853}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "removeAnnotation", "org.jfree.chart.annotations.CategoryAnnotation", new double[]{-0.075829788876811133, 0.74908856208439834, 0.18457825768436043}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawRangeMarker", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.ValueAxis,org.jfree.chart.plot.Marker,java.awt.geom.Rectangle2D", new double[]{-0.54635768019861586, 0.93928096685646256, -0.68172411687395318, -0.043730687302581783, 0.17463910078418987, -0.70918208192753296, 0.38078902311600293, -0.46743479464450011, 0.60599365696981056, 0.94311505312892607, 0.8612050076012161, -0.77648208997596768, -0.028347360530222243, 0.38129743265757599, -0.92892591831869376}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:object-type:org.jfree.chart.renderer.category.CategoryItemRendererState", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "createState", "org.jfree.chart.plot.PlotRenderingInfo", new double[]{0.62525199206421789, -0.53999635403103463, -0.96637423033087577}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItemToolTipGenerator", "", new double[]{-0.5640677737365285, 0.43003766522163822, 0.087081457470402412}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:object-type:org.jfree.chart.renderer.category.CategoryItemRendererState", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "createState", "org.jfree.chart.plot.PlotRenderingInfo", new double[]{0.95734655159440152, 0.35209137832038051, 0.49354999593045989}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:object-type:org.jfree.chart.renderer.category.CategoryItemRendererState", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "createState", "org.jfree.chart.plot.PlotRenderingInfo", new double[]{-0.68262039806887609, -0.53242537535929579, -0.31054536257194454}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:object-type:org.jfree.chart.renderer.category.AreaRenderer", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "clone", "", new double[]{-0.72298791879065027, 0.58142199578510256, -0.3007930989691725}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setLegendItemToolTipGenerator", "org.jfree.chart.labels.CategorySeriesLabelGenerator", new double[]{0.78658817449004159, 0.82678616060288346, 0.92226478769584141}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseURLGenerator", "org.jfree.chart.urls.CategoryURLGenerator", new double[]{-0.5133217174811735, -0.63061226764162348, -0.34289659230078895}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getBaseURLGenerator", "", new double[]{-0.56281809768617408, -0.27147292901490028, -0.37707046494792329}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesToolTipGenerator", "int,org.jfree.chart.labels.CategoryToolTipGenerator", new double[]{0.20159627074957398, -0.8824399412148578, 0.10124822717217263, 0.62982816612544035, -0.58110052145698132, 0.2727595613134326}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setLegendItemLabelGenerator", "org.jfree.chart.labels.CategorySeriesLabelGenerator", new double[]{0.35595154873962631, 0.40877678086225333, -0.34187990663230794}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setLegendItemURLGenerator", "org.jfree.chart.labels.CategorySeriesLabelGenerator", new double[]{-0.18883600680748636, 0.32732721779127338, 0.70980731489958249}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesToolTipGenerator", "int,org.jfree.chart.labels.CategoryToolTipGenerator,boolean", new double[]{0.91429366901761111, -0.2358037632573875, -0.9192943758727119, 0.76715540590738351, -0.26803569733735477, -0.91160393556423247, -0.66333414922953748, 0.48249270358108087, -0.85201481378840227}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:object-type:org.jfree.chart.renderer.category.CategoryItemRendererState", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "createState", "org.jfree.chart.plot.PlotRenderingInfo", new double[]{-0.33542883236255716, 0.95867008417436939, -0.38352814771351595}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesItemLabelGenerator", "int,org.jfree.chart.labels.CategoryItemLabelGenerator,boolean", new double[]{-0.50604924606010115, 0.39610955528002867, 0.6223297132863439, 0.71923866828913741, 0.46479642563532075, 0.35053130782259578, -0.91565831465027636, 0.91123360661780572, -0.28403539840550462}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{0.83455216960293921, 0.86493342558719677, 0.10723358581708364, -0.30919188445116341, 0.56806397290580901, -0.053280811168985975, 0.47792512473337334, -0.34733325470302345, 0.54265622330742502}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getSeriesToolTipGenerator", "int", new double[]{-0.11310915166193158, -0.50177518836599821, -0.89892224535444676}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:object-type:org.jfree.chart.labels.StandardCategorySeriesLabelGenerator", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItemLabelGenerator", "", new double[]{-0.16583066807986535, 0.75297532244454635, 0.41500082302271535}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItemURLGenerator", "", new double[]{0.59485905336959655, -0.06997026633490866, 0.60546458311478557}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getBaseURLGenerator", "", new double[]{0.80873709652887515, -0.030149623895821209, -0.034904355772599871}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawBackground", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{-0.16517282242511899, -0.70374520156088782, -0.066703131215466005, 0.26051121678058897, 0.64743224073917238, -0.37363616421094692, -0.94640755861057446, -0.89152499703635635, -0.48926616014255031}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseURLGenerator", "org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{-0.97544062551276745, -0.65250270540392186, -0.8297926794270909, 0.34373473887299699, -0.30772804075003268, 0.119076294278462}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesItemLabelGenerator", "int,org.jfree.chart.labels.CategoryItemLabelGenerator,boolean", new double[]{0.095701579826987571, -0.96226514530344365, 0.64722518292712583, 0.92597049018525346, 0.70789052995330448, -0.61881713496085933, 0.42112841648038324, 0.85940599267780171, 0.17205157992535991}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getBaseToolTipGenerator", "", new double[]{-0.72275415414570854, -0.49957504009358966, -0.98244739286666483}));
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
