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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
public class GeneratedStudyTest {
  @Test(timeout=10000)
  public void generated1() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseItemLabelGenerator", "org.jfree.chart.labels.CategoryItemLabelGenerator,boolean", new double[]{0.66590947354708863, 0.45803966823341336, 0.82928909475281765, -0.46043473069732355, -0.37045971698347407, -0.57359663273266814}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "removeAnnotation", "org.jfree.chart.annotations.CategoryAnnotation", new double[]{-0.66061872754831097, 0.25327733207262509, 0.12900675175914036}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.RuntimeException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawAnnotations", "java.awt.Graphics2D,java.awt.geom.Rectangle2D,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.axis.ValueAxis,org.jfree.chart.util.Layer,org.jfree.chart.plot.PlotRenderingInfo", new double[]{0.77138117155032226, -0.43331548838040113, -0.46963283239284093, -0.60414120575010744, -0.23072361993500579, -0.61497722558604473, -1, 0.38923445281966829, -0.76325446625780369, -1, 0.27178640073623811, -0.79520130025219837, 0.27430036163934651, 0.61656543028135391, -0.66927083313095648, -0.76499691609893472, 0.62695280847735346, -1}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "removeAnnotation", "org.jfree.chart.annotations.CategoryAnnotation", new double[]{-0.53032789279071368, -0.51770017297130644, 0.72508669030682793}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "addAnnotation", "org.jfree.chart.annotations.CategoryAnnotation", new double[]{0.61074896830476733, 0.42739463154248336, 0.53642830541779363}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseURLGenerator", "org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{0.39363532710196519, -1, -1, 0.27045988225061912, -0.26937884170222592, -0.33567425927367506}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getItemLabelGenerator", "int,int,boolean", new double[]{-1, -0.42278628988583694, -1, -0.44031718489737826, -0.51941031617868361, 0.24903600263667564, -0.41056811449385672, 0.17220455960984593, -0.95206336371672895}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getDomainAxis", "org.jfree.chart.plot.CategoryPlot,org.jfree.data.category.CategoryDataset", new double[]{-0.17494384295644252, 0.27070669836112876, 0.56675650468942673, -0.33756113392432174, 0.0026923999637000853, 0.32730893608386846}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "removeAnnotation", "org.jfree.chart.annotations.CategoryAnnotation", new double[]{-0.032004781635167207, -0.55221958987826603, -0.39211338418737302}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{0.17199211974411074, 1, 0.36220000515638701, 0.408020040495361, -0.55665678162934529, 0.098027987629407579, 0.43356865309848763, -0.098757274068388454, -0.40442545172093824}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "addAnnotation", "org.jfree.chart.annotations.CategoryAnnotation", new double[]{-0.0045456745531381529, -1, 0.57384091823682759}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{-0.041970271052441797, -1, -0.55317534642915633, -1, 0.47753448270440435, -0.70357453080802224, -0.053104057572047558, 0.2474642471610021, 0.75583007031572302}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:object-type:org.jfree.chart.labels.StandardCategorySeriesLabelGenerator", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItemLabelGenerator", "", new double[]{-0.6408747286193327, 0.51082000076003142, -0.50518832595354868}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawOutline", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{0.17346501443819917, 0.37449652068580186, -0.41077997737386751, 0.3962554589865625, 0.79888389911730506, 0.26924243379836477, 0.054000271730293925, 0.522034928647922, -0.75959866500987583}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawDomainLine", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D,double,java.awt.Paint,java.awt.Stroke", new double[]{0.81144534793867218, 0.63630762594993551, 0.13638595262412057, 0.090716648500515029, -0.32081675912161195, 0.43981753879553909, -0.4406405656125244, 0.072346730757600619, 0.70142318873417264, -0.36906484475712298, -0.35618948539690598, -0.23396595511374302, -0.48609344360850798, 0.8389268809839644, -0.64914405925123753, 0.88226405891972726, -0.0089383806316689629, -0.69058807482364293}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setLegendItemURLGenerator", "org.jfree.chart.labels.CategorySeriesLabelGenerator", new double[]{-0.21006550495822751, 0.011187967586430397, 0.068560835491815963}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getRangeAxis", "org.jfree.chart.plot.CategoryPlot,int", new double[]{1, -0.24770886616540602, 0.55378901108628331, 0.50112230601934749, 0.80094208322139848, -0.72763931133665405}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:object-type:org.jfree.chart.LegendItemCollection", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItems", "", new double[]{1, 0.060690053388783693, -0.051037567552782381}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getDrawingSupplier", "", new double[]{-0.29153357615190545, -0.49654919753425264, 0.91917765067078705}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getItemLabelGenerator", "int,int,boolean", new double[]{0.27344281390636083, -0.2456604441134955, -0.54946053026129615, -0.040133531351932088, 0.070450101924745004, 0.24289372175098148, -0.19230713031192342, -0.25982197186785339, -0.47852729173301356}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getDrawingSupplier", "", new double[]{-0.2249768798911691, 1, 1}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getURLGenerator", "int,int,boolean", new double[]{0.95837701809683318, 0.80615339248938789, 1, -0.55590419675229141, -0.65185993198018222, -0.083494207865070696, 0.25970160959530414, 1, -0.061314763252758017}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawDomainMarker", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.plot.CategoryMarker,java.awt.geom.Rectangle2D", new double[]{0.89241776833114073, 0.6303721654522747, -0.85819661668945357, 0.5191662579627756, 0.35485660065797109, 0.38969243373186402, -0.98727131956953873, 0.32133910975307717, -0.48653902810754579, -0.48307703886163122, -0.19185271635212414, 0.50868895693003169, -1, 0.50818639461536885, 1}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getPlot", "", new double[]{0.13192079361284806, -0.095306513948696631, 1}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{0.021668241550293572, 0.55553237260416233, 0.4223545514665174, -0.16180131218543736, -0.019457797558283113, 0.34550173708084853, -0.43636704750426841, 0.5058500725997499, 0.18954866771057566}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getColumnCount", "", new double[]{1, 0.66238336765948114, -0.85222526872662985}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawOutline", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{-0.37959657106621081, 0.45074450480587142, -0.61386629587971586, -0.61375229251726171, 1, -0.39725627429126681, -0.65447762419942679, -0.29276288535364609, -0.46611743188039428}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:object-type:org.jfree.chart.LegendItemCollection", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItems", "", new double[]{-0.62309837814951363, -0.036100787491841868, -0.056902084369734068}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "removeAnnotations", "", new double[]{-0.041453349681882003, 1, 0.1397731794558566}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getBaseItemLabelGenerator", "", new double[]{-0.51248579012567164, 0.032617633239724775, -0.11728771085862461}));
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
            return method.getReturnType() == void.class ? "void" : "value:" + value(result);
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
