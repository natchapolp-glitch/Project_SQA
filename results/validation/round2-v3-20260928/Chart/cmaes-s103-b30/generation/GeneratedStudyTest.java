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
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setLegendItemLabelGenerator", "org.jfree.chart.labels.CategorySeriesLabelGenerator", new double[]{-0.086937624036138444, -0.19015235631540686, -0.95901584820932984}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseToolTipGenerator", "org.jfree.chart.labels.CategoryToolTipGenerator", new double[]{0.25012284707244503, 0.56272119404747301, -0.10137957821232831}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator", new double[]{-0.83209298245225327, -0.92681512075952299, -0.16817690795811496, -0.1980481460954189, 0.65862114415714479, 0.38223914674301152}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getItemLabelGenerator", "int,int,boolean", new double[]{-0.12795993751545498, -0.48168387187935235, -0.76936315969075586, -0.24964434344920541, -0.87251372910350555, 0.68511466207888938, -0.93762671828781829, 0.3428094044683011, 0.59795106071021442}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "removeAnnotation", "org.jfree.chart.annotations.CategoryAnnotation", new double[]{-0.40965437410895439, -0.49845129324760695, 0.4677090632944122}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "equals", "java.lang.Object", new double[]{0.85280232423584723, -0.31312610907568278, -0.30468207493397836}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Integer:ODk0Mjk0MTI0", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "hashCode", "", new double[]{-0.080177229273994899, -0.44451393946724854, -0.29794854924127456}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawBackground", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{0.29925894523433499, -0.35783594604369945, 1, 0.16485626464931935, 0.15206171104654095, -0.74320253936103509, 0.14974438981123375, 0.12771801940096719, 0.67443908661948493}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawDomainLine", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D,double,java.awt.Paint,java.awt.Stroke", new double[]{-0.97114008285089093, -0.59937527762854859, -0.32723396633914326, 0.30519603837958426, 0.8353490226347372, -0.41054155326767255, -0.59301160500835093, -0.24291033756519892, -1, 0.26789523004211813, -0.099146375465534689, -0.19278762538973429, 0.80393788959155721, -0.1815178919580728, -0.45616377706507272, 0.60483559273273857, -0.28103971312865006, 0.018781253869971575}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getSeriesToolTipGenerator", "int", new double[]{-0.023263648018767388, 0.12580297689757522, 0.1859257483193788}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getItemLabelGenerator", "int,int,boolean", new double[]{0.79429559257717897, 0.49361797443022087, 0.35163925568831372, 0.6890707700116796, 0.56858088749469449, -0.46070811981794441, 1, -0.87838726544356116, 0.095729003137862437}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getPlot", "", new double[]{0.44099173679774661, -0.88880139339016806, 0.855178284020783}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{-0.75595862289210647, 0.0062615537353961359, -0.90718436292703064, -0.47971802139743347, -0.40093726882475111, -0.28142377481314196, 0.95835914497718511, -0.38587402911152313, 0.84665348627219505}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesItemLabelGenerator", "int,org.jfree.chart.labels.CategoryItemLabelGenerator,boolean", new double[]{-0.71850862558350204, -0.31575542341872731, -0.304365290880585, -1, -0.11760184673538762, -0.59068646281856252, -0.43502567060796166, 0.42053994892225649, 1}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseURLGenerator", "org.jfree.chart.urls.CategoryURLGenerator", new double[]{-0.040655637180304557, 0.17890235573559538, -0.16113343686915482}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setLegendItemToolTipGenerator", "org.jfree.chart.labels.CategorySeriesLabelGenerator", new double[]{-0.65452801367305669, -0.25747456298132398, -0.80153692959559197}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{0.65245972452001522, 0.11549693891237567, -0.87930558799813929, 0.15224302237113047, 1, -0.051546486155089305, -0.47465257433265484, 0.22133476342846076, 0.34131754157032873}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:object-type:org.jfree.chart.labels.StandardCategorySeriesLabelGenerator", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getLegendItemLabelGenerator", "", new double[]{0.17162957241540247, -0.27921715139619563, 0.075524492129434517}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawOutline", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{-1, -1, -0.21210659567515294, -1, 0.30734299354441275, 0.1620907427124661, -0.91433667386378648, -0.11652643846761879, -0.40868993975301782}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseURLGenerator", "org.jfree.chart.urls.CategoryURLGenerator", new double[]{-0.90397546855333888, -0.0060649107370580602, -0.37693568105568048}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{-0.23255207708450976, -0.38324091126488102, -0.21604750920414778, -0.94997301243434329, -0.19229927771195898, 0.1046968942422835, 1, -1, 0.34655107001319302}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseURLGenerator", "org.jfree.chart.urls.CategoryURLGenerator", new double[]{0.4455606704678986, 0.01270996918360337, 0.0002000113975217932}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "drawOutline", "java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D", new double[]{-0.82272226155576966, 0.2445538767741392, -1, 0.49968868319079129, 0.42804593864790885, 0.23849516691288158, 0.3740241257484227, 0.33138655141786505, 0.020526153411544679}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "getSeriesToolTipGenerator", "int", new double[]{-0.95251246918402832, 0.42333832221431977, 0.05114479462815652}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:null", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "findRangeBounds", "org.jfree.data.category.CategoryDataset", new double[]{-0.89501078052279026, 0.26401045316538085, -0.022592381361496661}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setLegendItemURLGenerator", "org.jfree.chart.labels.CategorySeriesLabelGenerator", new double[]{-0.015212014854885325, 0.51979850450302967, -0.88374727617899929}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator,boolean", new double[]{0.81435504863757346, 0.58480686568530138, -0.24575712402792094, -0.8581374904649699, -0.21395227237260861, -0.1634480370053692, -0.060471755536144367, -0.023862275305602643, 0.5837171742562407}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("void", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setBaseItemLabelGenerator", "org.jfree.chart.labels.CategoryItemLabelGenerator", new double[]{-0.36808993728399408, -0.87227806962411147, -0.5674595782886519}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "addAnnotation", "org.jfree.chart.annotations.CategoryAnnotation", new double[]{-0.78140821366092617, 0.55559199636912315, -0.96720410150436176}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jfree.chart.renderer.category.AreaRenderer", "", "setSeriesURLGenerator", "int,org.jfree.chart.urls.CategoryURLGenerator", new double[]{-0.80450881963440213, 0.17579718805052622, -0.73396438879156178, -0.37622596399345337, 0.49489450829472664, -0.37073431043947486}));
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
