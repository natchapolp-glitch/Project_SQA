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
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putNextEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.66590947354708863, 0.45803966823341336, 0.82928909475281765, -0.46043473069732355, -0.37045971698347407, -0.57359663273266814}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.StringIndexOutOfBoundsException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeAsciiLong", "long,int,int", new double[]{-0.30835671335960285, 0.30024953046113839, 0.7585033920445543, 0.78813956625265424, -0.30385214520275933, -0.7644431060446405, -0.66061872754831097, 0.25327733207262509, 0.12900675175914036, 0.72636126217288544, 0.51087477187740371, 0.66163265129573756}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeAsciiLong", "long,int,int", new double[]{-0.6521928944867218, 0.0058964401248932582, 0.26354434127739718, -0.45589316586317874, 0.79367696683281352, -0.99369976831238582, -0.061978994107967769, -0.54903435929521083, 0.25393093846270959, 0.30325781717188682, -0.19900075226792249, 0.21174665565325093}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "int", new double[]{-0.43331548838040113, -0.46963283239284093, -0.60414120575010744, -0.23072361993500579, -0.61497722558604473, -1}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.IndexOutOfBoundsException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "[B,int,int", new double[]{-0.66927083313095648, -0.76499691609893472, 0.62695280847735346, -1, -0.7850396798941025, -0.53032789279071368, -0.51770017297130644, 0.72508669030682793, -0.11945373369540913, -1, 0.84216154557724043, 0.34612982744433296}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldAsciiEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{1, 0.52106353457184451, 0.4377737687138975, -0.39717786622146456, -0.027040768233070524, 0.91094667782099037}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putArchiveEntry", "org.apache.commons.compress.archivers.ArchiveEntry", new double[]{0.53642830541779363, 0.030971614560335842, 0.24409916344278193, -0.26259864722400267, 0.21960046346932621, 0.25158306241119055}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putArchiveEntry", "org.apache.commons.compress.archivers.ArchiveEntry", new double[]{-0.28868213886604432, 0.94646078169459547, 0.20131234958452696, 0.64044855444759596, 0.39363532710196519, -1}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "close", "", new double[]{-0.57736673033200481, 0.82045958929981899, -1}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "ensureOpen", "", new double[]{-0.44031718489737826, -0.51941031617868361, 0.24903600263667564}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeBinaryLong", "long,int,boolean", new double[]{-0.41064250520665463, 1, -1, -0.17494384295644252, 0.27070669836112876, 0.56675650468942673, -0.33756113392432174, 0.0026923999637000853, 0.32730893608386846, 0.79196200442305997, 0.045022838105293263, -0.14805838790895745}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "int", new double[]{0.64116659307859347, -0.059838879476317025, 0.79333737486528644, -0.18545103522741657, -0.15549917151911363, -1}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldAsciiEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{1, 0.86579219622962678, -0.0026547404974517352, 0.57115040580232934, -0.37745284302529486, 0.052707834615168925}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "closeArchiveEntry", "", new double[]{0.75028731030878171, 1, 0.77832039567253686}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "int", new double[]{0.61067981196149701, -0.10876335356960162, 1, -0.21120896627667288, -0.43229462500935495, 0.074744737273203077}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "ensureOpen", "", new double[]{-1, -0.050370310952322833, 1}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeCString", "java.lang.String", new double[]{0.96696732617051673, 0.19755950450114437, -0.44253905900235024, -0.63049405806523928, -1, -0.25811548431812281}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeNewEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{-0.035721083954870628, -1, 0.60531306685523956, 0.0064993668573948976, -0.0069443133755522912, -0.35811384985346101}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "ensureOpen", "", new double[]{0.52135605892896308, 0.15265030007460609, 0.17591367536619268}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "finish", "", new double[]{-0.064567048836236984, 0.45054887920636671, -0.10318888760779998}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldAsciiEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.5377011843296855, 0.53751597107831439, -0.0048557526451966648, -0.71013916823301648, -0.86822102138108259, -0.52287052291365343}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "close", "", new double[]{-0.095193281902823335, 0.96871071546466836, 0.62012928876637274}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeBinaryLong", "long,int,boolean", new double[]{0.46624403025992445, 0.18783813735636584, 1, 0.63096980161648086, -0.39202968623412648, -0.55011143381947258, -0.60060298429792192, -0.17713105219274417, 0.69705257151417388, 0.72240353647754385, 0.10884558387028953, 0.061339654966168555}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "int", new double[]{0.6534562629953925, 1, 0.68792954611969037, 0.28964014976735264, -1, 0.67421624021721849}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeHeader", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.48746894345102232, 1, 0.31123387876151259, 0.32872542745628891, -0.87354325405502586, -0.31880145641807511}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldAsciiEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.29654551641955001, 0.33452184479357006, -0.21054588721254602, -0.16273159423357603, -0.96211104505086475, -0.77615197574280237}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream,short", "<init>", "", new double[]{-0.067839346596827887, 0.49328777583113198, 0.68723827774055646, 0.13362375104568733, -0.71028656823943437, -0.98664050363259403}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldAsciiEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.59695041632116808, -0.08563257229232879, 0.858740398603274, 0.2125326032658319, 0.21863781006624564, -0.056646141489603286}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "pad", "long,int", new double[]{0.64805348048571854, 0.49961574271368453, 0.768476485557679, 0.60709032732097135, -0.60287984866843791, -0.77941880418744391, -0.25706217938710674, -0.21007605273146973, -0.67673808612418673}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "setFormat", "short", new double[]{1, 0.00094706996860238757, 0.15981166261896226, -0.183655195920845, 0.76497142168356236, 0.43390373598291182}));
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
