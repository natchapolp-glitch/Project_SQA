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
    assertEquals("exception:java.lang.IndexOutOfBoundsException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "[B,int,int", new double[]{-0.086937624036138444, -0.19015235631540686, -0.95901584820932984, 0.052908039485623448, 0.23601192846300303, -0.24439142138918518, -0.18407899986821655, -0.93038801527718762, 0.84951370501017076, 0.20941741692076871, -0.21705431303519587, 0.37009804347218705}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "closeArchiveEntry", "", new double[]{0.063263881649785525, -0.11841903632738525, 0.044382125342745417}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "ensureOpen", "", new double[]{-0.494475084655083, 0.0040459293084874875, 0.57588908140818373}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "pad", "long,int", new double[]{-0.92681512075952299, -0.16817690795811496, -0.1980481460954189, 0.65862114415714479, 0.38223914674301152, 0.49625672576755975, -0.0046774982393349472, 0.27535261788000204, 0.23665043871965327}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.IndexOutOfBoundsException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "[B,int,int", new double[]{0.37065590652482211, 1, -0.045888503638938767, -0.95092217036816717, -0.3995003944947873, -0.12795993751545498, -0.48168387187935235, -0.76936315969075586, -0.24964434344920541, -0.87251372910350555, 0.68511466207888938, -0.93762671828781829}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "finish", "", new double[]{0.59795106071021442, 0.25735876399775598, 0.83215100037503487}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeBinaryLong", "long,int,boolean", new double[]{0.4677090632944122, -0.37078033349470269, 0.66660148570462618, -0.23985059497177683, -0.47793331026552927, -0.46334439603861455, -1, -1, -0.38180558725340735, -0.49195262232187797, 1, 0.23662650335317686}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putNextEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.062979045034559852, -0.14100915120929239, -0.91397048212208643, -0.80059105517979157, 0.85280232423584723, -0.31312610907568278}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeNewEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{-0.25597727799187614, -0.44678064741143148, 0.21457686671457607, 0.33569111365351845, -0.36069732074790872, -0.15843177912403181}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeHeader", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.17292909312057625, -0.60222459698836828, 0.67926583082859771, 0.78172321722889715, 0.71693376794823249, -0.55589727153761115}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeNewEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.38638519770635621, -0.87597360166355032, 0.21681961560453919, 0.29925894523433499, -0.35783594604369945, 1}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldAsciiEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.42864047214739764, -0.84305259310699121, -0.31893200680186307, -0.96727482159602252, 0.16205612260600449, 0.37504226735770829}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putArchiveEntry", "org.apache.commons.compress.archivers.ArchiveEntry", new double[]{0.57845285564276516, -0.49023681366252181, -0.86376476546487235, -0.21105378176299089, -1, 0.20899504842060063}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldAsciiEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{-0.1507507180987489, -0.29929356343664082, -0.34579703772508735, 0.11162214123187739, 0.34255278348432655, -0.64867062004218679}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "int", new double[]{0.32124756616928879, -0.35552250108943684, 0.20889421539561642, -0.10629615213154056, -0.23640479653442784, -0.46218130541109925}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.IndexOutOfBoundsException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "[B,int,int", new double[]{-0.57479417389599519, 1, -1, 0.090456836215874845, -0.79424628788630458, -0.174896541819337, -0.34695240706267622, -1, 0.77793251429496002, -0.02374673804708477, -0.16483433100156591, -0.15543472878546022}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("constructed:org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "<init>", "", new double[]{-0.27924800059506394, 0.263934091702971, -1}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldBinaryEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry,boolean", new double[]{0.41755082859843506, -0.021544317971347579, -0.81360616192223234, -0.13702358024719608, 0.59258940245792346, 0.029707305037472424, 0.50323621681521602, -0.86477505429650514, 0.65088124805991909}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeHeader", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{1, -0.021725842659361783, 0.11883103712466597, 0.2150710274657015, 1, -0.28952030876223284}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putNextEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{-0.72999386257675758, -0.26220083244781123, -0.14162839328348187, -0.75217779759806291, 0.14022743287854689, -0.7519873487177462}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putNextEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.22847657130979832, -0.84138467305501019, -0.81808239350750855, -0.65525899986321723, 0.29056970503901658, 0.1165498488198629}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putNextEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.39674442478010374, -1, 0.5424771128716217, -0.033431509153082468, 0.56970148080317207, 0.0091713969119300923}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeCString", "java.lang.String", new double[]{0.26858161881862952, -0.86467791361215296, -0.38692536423994173, 0.027359903018262022, -0.073616653793779852, 0.76381144355160036}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeHeader", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.47820699682197576, -0.9847405478006046, -0.45021251623258113, -0.53646841614227392, 0.39176413701305379, 0.83014554778298799}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "int", new double[]{0.30502215637162511, -0.77714227429272742, 0.1291240142911132, -0.93171515116497927, 0.33044035879156419, -0.33006155228603173}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldBinaryEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry,boolean", new double[]{0.87285677817423124, -1, 0.015371572140285006, 0.54567964140472702, -0.48264719842594184, 0.094260923163433361, 0.097241915311899574, 1, 0.80161899163122774}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldAsciiEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{-0.34443957229387667, 0.19519288893648368, -1, -0.55420415526820299, -1, -1}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.StringIndexOutOfBoundsException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeAsciiLong", "long,int,int", new double[]{0.71533220742662129, -0.017024279475576498, -1, -0.40844147466044778, 0.62646059106644292, 0.24085258885318167, -1, 0.30747187798707176, 0.73576658093907688, -0.33850491953496747, -1, 0.6497295360066514}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "closeArchiveEntry", "", new double[]{-0.28637953879087885, -0.35018856566798784, -0.64885063624869699}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "finish", "", new double[]{0.04124385739003178, -0.94135488546700952, 0.15990726175084125}));
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
