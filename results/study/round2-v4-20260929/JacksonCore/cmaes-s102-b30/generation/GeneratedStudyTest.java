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
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsDecimal", "", new double[]{0.66590947354708863, 0.45803966823341336, 0.82928909475281765}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "[C", new double[]{-0.30835671335960285, 0.30024953046113839, 0.7585033920445543}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.StringIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "[C,int,int", new double[]{-0.6521928944867218, 0.0058964401248932582, 0.26354434127739718, -0.45589316586317874, 0.79367696683281352, -0.99369976831238582, -0.061978994107967769, -0.54903435929521083, 0.25393093846270959}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "resetWithCopy", "[C,int,int", new double[]{-0.43331548838040113, -0.46963283239284093, -0.60414120575010744, -0.23072361993500579, -0.61497722558604473, -1, 0.38923445281966829, -0.76325446625780369, -1, 0.27178640073623811, -0.79520130025219837, 0.27430036163934651}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "append", "java.lang.String,int,int", new double[]{-0.66927083313095648, -0.76499691609893472, 0.62695280847735346, -1, -0.7850396798941025, -0.53032789279071368, -0.51770017297130644, 0.72508669030682793, -0.11945373369540913, -1, 0.84216154557724043, 0.34612982744433296}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{1, 0.52106353457184451, 0.4377737687138975}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "toString", "", new double[]{0.53642830541779363, 0.030971614560335842, 0.24409916344278193}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsString", "", new double[]{-0.28868213886604432, 0.94646078169459547, 0.20131234958452696}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:null", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getTextBuffer", "", new double[]{-0.57736673033200481, 0.82045958929981899, -1}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "inLongRange", "[C,int,int,boolean", new double[]{-0.44031718489737826, -0.51941031617868361, 0.24903600263667564, -0.41056811449385672, 0.17220455960984593, -0.95206336371672895, 0.1419253215691843, -0.15720361529355009, 0.55313622569714627, 1, 0.41229488969394223, 0.51191509409529701}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "java.lang.String", new double[]{-0.41064250520665463, 1, -1}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "resetWithCopy", "[C,int,int", new double[]{0.64116659307859347, -0.059838879476317025, 0.79333737486528644, -0.18545103522741657, -0.15549917151911363, -1, -0.29228048522102018, 0.23846121736646814, -0.63254847646638135, -0.03775230486766868, -0.70089681104358859, 0.23128497096204964}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "unshare", "int", new double[]{1, 0.86579219622962678, -0.0026547404974517352, 0.57115040580232934, -0.37745284302529486, 0.052707834615168925}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsDouble", "", new double[]{0.75028731030878171, 1, 0.77832039567253686}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "releaseBuffers", "", new double[]{0.61067981196149701, -0.10876335356960162, 1}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "inLongRange", "[C,int,int,boolean", new double[]{-1, -0.050370310952322833, 1, 0.12087540819824627, 0.13917539211329844, -1, -0.98937876983089434, 0.47424598522654415, -0.65881138590206278, 0.73653614766612641, 0.25399663147144702, 0.51742426045417778}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseLong", "java.lang.String", new double[]{0.96696732617051673, 0.19755950450114437, -0.44253905900235024}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "clearSegments", "", new double[]{-0.035721083954870628, -1, 0.60531306685523956}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "inLongRange", "[C,int,int,boolean", new double[]{0.52135605892896308, 0.15265030007460609, 0.17591367536619268, -0.40170546499840754, -0.36142453146986508, -1, 0.188545111221464, 0.94696606032583186, 0.12477752977575357, -0.41601954832970217, 0.0094046212635355286, 1}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getTextOffset", "", new double[]{-0.064567048836236984, 0.45054887920636671, -0.10318888760779998}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{0.5377011843296855, 0.53751597107831439, -0.0048557526451966648}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "finishCurrentSegment", "", new double[]{-0.095193281902823335, 0.96871071546466836, 0.62012928876637274}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseLong", "[C,int,int", new double[]{0.54673750388880815, 0.44744261194740509, 1, 0.80912365388884799, -0.46354927074700647, -0.37720294481651506, -0.14885800946127969, 0.30534763376781365, 0.78726191679278146}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "resetWithShared", "[C,int,int", new double[]{0.74900098239363955, 1, 0.79176901132088862, 0.46587790165076642, -1, 0.90630677438519702, -0.77130392724061458, 0.99302883568110023, 1, 0.57440995582769516, -0.84479589783852527, 1}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("sha256:0763d7f9c0f4a169e0c25d170a8d259da9f2fd94dd9be0a7b5ab19b2129e2346:bytes:819185", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "findBuffer", "int", new double[]{0.56987119410952825, 1, 0.40586402643734587, 0.5181724322827379, -0.95629221951176657, -0.12383766162211556}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{0.37371666071145637, 0.59335266261766706, -0.12429689066433514}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("constructed:com.fasterxml.jackson.core.util.TextBuffer", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "<init>", "", new double[]{0.0038672680756355327, 0.74441400388543066, 0.79366733095960595}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:[C[]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsArray", "", new double[]{0.68482519810621778, 0.16549340211670388, 0.97545760053685582}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Integer:MjE0NzQ4MzY0Nw==", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseAsInt", "java.lang.String,int", new double[]{0.73299191117527318, 0.77275436570062495, 0.88135521831415098, 0.79597922092316953, -0.67709114019109962, -0.60806545129326095}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseInt", "[C,int,int", new double[]{1, 0.26992643192214827, 0.25992933584888994, -0.0047788605936062711, 0.73802454661035277, 0.63696868250315219, -0.76456698651295274, 0.90908405010001947, -0.46056475762487231}));
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
