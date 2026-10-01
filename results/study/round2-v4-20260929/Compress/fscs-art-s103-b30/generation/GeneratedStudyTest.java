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
    assertEquals("constructed:org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream,short", "<init>", "", new double[]{-0.087403447416303459, 0.43769406820047285, 0.46977248517067904, -0.57019666300587324, -0.84392406176975188, 0.20548647253427887}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "close", "", new double[]{0.65392030205680829, -0.50223626068128557, -0.26175284083828654}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "ensureOpen", "", new double[]{-0.94430192055912854, -0.078911036287813596, 0.088920834552111527}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "setFormat", "short", new double[]{0.72214639752983056, 0.42664492860135828, -0.63507355810075228, 0.82274224645698912, 0.0048426031435626626, -0.64419375222971942}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putNextEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{0.7298936000733407, 0.96697471134380075, -0.8498448512642891, 0.8891942660174259, -0.43235878858049448, -0.72927741276912306}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream,short", "<init>", "", new double[]{0.83488738527884498, -0.74662721650144603, -0.51688955611541298, -0.6480409853133513, -0.93365473196872251, -0.71590188587790715}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putNextEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{-0.79824238218361887, -0.66449360394208923, -0.66565503894547606, 0.42029956433115001, 0.30494570380521435, -0.25145904746039438}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeCString", "java.lang.String", new double[]{0.27225330131712844, 0.30489264164819851, 0.87063015986659953, -0.42936349096005655, 0.31006362558908962, 0.58253025082355769}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeBinaryLong", "long,int,boolean", new double[]{-0.9613262935206135, -0.56659813101402756, -0.97402400443264092, 0.84791769335985423, 0.50527582920234471, 0.98249581404261344, 0.30903983092749177, -0.52919955559244269, -0.55962143737855308, 0.65769659666799796, 0.71159547324631389, -0.76351762694761094}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeNewEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{-0.98216367707594721, 0.91074108052347591, 0.24417477804193699, -0.28579410506696568, -0.04680522606834403, -0.74532244025452066}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldBinaryEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry,boolean", new double[]{-0.90576473059185458, 0.96814188159233483, 0.1859154090179802, -0.98876557942778498, 0.9114580677620896, -0.50208116836148253, -0.21452423179411007, 0.82126475145106093, 0.60425123028745276}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "pad", "long,int", new double[]{0.29560659486268515, 0.61541694487803733, -0.51691634058894276, 0.80013778084775988, -0.60080928667881017, -0.98305601506974383, -0.67128254418841027, -0.91628711654042072, 0.61145121597536889}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream,short", "<init>", "", new double[]{0.92210293572989221, -0.55004328254492418, -0.45236907520791081, -0.67555747366653462, 0.30675454864920626, 0.68136599496561834}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldBinaryEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry,boolean", new double[]{-0.050101771432413456, 0.7338790613853976, 0.016100771643241751, 0.92501541325075531, 0.83540382104868782, 0.20576972214026301, 0.52871746886508553, -0.011689099872943087, 0.79398712162935547}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.IndexOutOfBoundsException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "[B,int,int", new double[]{-0.46278963990619126, -0.75936414300352806, -0.60984874012078416, 0.43850048893410798, -0.33085424631191973, 0.92254319307038735, 0.92118413047986203, -0.58325349207991506, -0.88359462137180067, -0.66993114026318601, -0.2638027606119191, -0.59790099685964737}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "ensureOpen", "", new double[]{0.12646255573070886, 0.69278067986654013, 0.337448817632239}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "ensureOpen", "", new double[]{0.98900077379787277, 0.34046990907892916, -0.27382471233320449}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream,short", "<init>", "", new double[]{-0.61609473870448861, 0.6057344002532592, -0.51902625632591781, 0.11434873497446674, -0.97645398586500209, 0.77993688360867841}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "pad", "long,int", new double[]{0.76860515499125115, 0.66474512838611033, 0.63150151191305737, -0.54576245838757131, -0.49350355627296083, -0.75107148188144257, -0.81028776940325309, 0.59706055988511819, 0.80088610101072777}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "ensureOpen", "", new double[]{-0.098640184566833211, -0.98542039009049498, -0.56510034601995129}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeCString", "java.lang.String", new double[]{0.5491798944385653, 0.97209110469127591, -0.88312110831662571, -0.080335628999817166, 0.99074255624698671, 0.71998825224613561}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeCString", "java.lang.String", new double[]{0.997557734773727, -0.36751199442787907, 0.15239676149500792, -0.43769441737558457, -0.46593796097599682, 0.99877756063058132}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "putArchiveEntry", "org.apache.commons.compress.archivers.ArchiveEntry", new double[]{-0.651421259495925, 0.14797063018545953, 0.56488685309393505, 0.76384758753649784, 0.72934472229847702, -0.14322710970266317}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "close", "", new double[]{-0.83231448429984378, -0.76379056077047758, 0.59130443101398567}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldBinaryEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry,boolean", new double[]{-0.9723215505457683, -0.51902798496713443, 0.64722496315249112, 0.85374420013853691, -0.72271248199184224, -0.70597505796495397, -0.45219518156120064, -0.77575322975605498, 0.7556673669981171}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "writeOldAsciiEntry", "org.apache.commons.compress.archivers.cpio.CpioArchiveEntry", new double[]{-0.51971938728674694, -0.82919575096589848, -0.4249361403703702, 0.16104834960504943, -0.46742793204574884, 0.90367625980364696}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "pad", "long,int", new double[]{-0.21622292075058458, -0.66036120983169644, 0.90521874551671422, 0.4277569497081366, -0.40623694218191964, 0.24197788825407529, 0.78931814052424043, 0.031418481973215284, -0.996671987889586}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "write", "int", new double[]{0.82393866411708316, 0.87992973241979144, 0.97195561553387178, -0.41575326501522625, 0.72155206835970964, 0.96478727024118704}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream", "java.io.OutputStream", "closeArchiveEntry", "", new double[]{0.66746809544121732, -0.69108461959239587, -0.37530566134150711}));
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
