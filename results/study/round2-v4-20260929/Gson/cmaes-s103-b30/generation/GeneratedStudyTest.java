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
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForArray", "java.lang.reflect.Type", new double[]{-0.086937624036138444, -0.19015235631540686, -0.95901584820932984}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForField", "java.lang.reflect.Field,java.lang.reflect.Type", new double[]{-0.21705431303519587, 0.37009804347218705, 0.21028105214878293, 0.063263881649785525, -0.11841903632738525, 0.044382125342745417}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForField", "java.lang.reflect.Field,java.lang.reflect.Type", new double[]{0.56272119404747301, -0.10137957821232831, -0.95603439494467901, -0.90719309326957998, 0.53622340027329785, -1}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getIndex", "[Ljava.lang.reflect.TypeVariable;,java.lang.reflect.TypeVariable", new double[]{-0.70081578666644284, -0.50113769869357083, -0.46085807148078128, -0.0029363649767365388, 0.48769792318058458, 0.37512556639819078}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getIndex", "[Ljava.lang.reflect.TypeVariable;,java.lang.reflect.TypeVariable", new double[]{-0.16817690795811496, -0.1980481460954189, 0.65862114415714479, 0.38223914674301152, 0.49625672576755975, -0.0046774982393349472}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForField", "java.lang.reflect.Field,java.lang.reflect.Type", new double[]{-0.8587121499689726, 0.58361920202428197, 0.37065590652482211, 1, -0.045888503638938767, -0.95092217036816717}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getIndex", "[Ljava.lang.reflect.TypeVariable;,java.lang.reflect.TypeVariable", new double[]{-0.24964434344920541, -0.87251372910350555, 0.68511466207888938, -0.93762671828781829, 0.3428094044683011, 0.59795106071021442}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForField", "java.lang.reflect.Field,java.lang.reflect.Type", new double[]{-0.53904954727556542, -0.76779168134462372, -0.33450071728107311, 0.077811003374410548, 0.587736522224383, -0.76187097188586372}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getActualType", "java.lang.reflect.Type,java.lang.reflect.Type,java.lang.Class", new double[]{0.66660148570462618, -0.23985059497177683, -0.47793331026552927, -0.46334439603861455, -1, -1, -0.38180558725340735, -0.49195262232187797, 1}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForField", "java.lang.reflect.Field,java.lang.reflect.Type", new double[]{0.51885379858549985, 0.062979045034559852, -0.14100915120929239, -0.91397048212208643, -0.80059105517979157, 0.85280232423584723}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{-0.28698842653674139, 0.99406904396573881, -0.30816027615848662, -0.24218249491043098, -0.15025773503170517, -0.54271771561167226, 0.29722006395372985, 0.14283255197762415, 0.15186720913762292}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{-1, -0.26366535356479637, -0.14949139639265763, -0.55287803681421688, 0.013721650765014498, -0.53799448653295112, -0.15012928259591923, -0.0081221974842461131, -0.063977751847055175}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForArray", "java.lang.reflect.Type", new double[]{0.55660576958764152, 0.54979386046261058, -1}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{0.20300121418864564, -0.8696878554836629, -0.32730119067662833, 0.24243367096927484, -0.2371075309630791, 1, 0.26791783343456488, -0.0019287567457652866, -0.23267803042331187}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForField", "java.lang.reflect.Field,java.lang.reflect.Type", new double[]{-0.026932500254747957, 0.50963843034888912, -0.78360074600202478, 0.57250450761393379, -0.63663306889088134, -0.14698144478797709}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.IllegalStateException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getIndex", "[Ljava.lang.reflect.TypeVariable;,java.lang.reflect.TypeVariable", new double[]{0.22718512058128698, 0.15495207554468721, -1, -0.55756998372402555, -0.20623696717146564, 0.11348062242919371}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:[Ljava.lang.reflect.Type;[]", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{-1, 0.14336916356299578, -0.6146500580493105, -0.18978506016669414, 0.80906180445879727, -0.30146593267208421, -0.27929946789772347, 0.37737550483051885, 0.22445087095686972}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{-0.31539537313601973, -0.11198137358255567, -0.40812180013993449, 0.14388163895806083, -0.54430377928369789, 0.55010887150982657, -0.1013569646400419, -0.39354766373848687, 0.94348259220346553}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{0.10977787288998492, 0.51139814940782591, -0.012249761150407545, -0.24992788880181196, 0.6149336315488686, -0.24838673137171421, -0.30542882352074113, -0.60505648397091161, 0.66536687655813997}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{0.56621189181961729, 0.3493097486300018, -0.18475328173914207, 0.58884851201824029, 0.58060151372192714, -0.55160807451617788, 1, -0.93631151927296274, 0.57871778677969488}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.IllegalStateException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getIndex", "[Ljava.lang.reflect.TypeVariable;,java.lang.reflect.TypeVariable", new double[]{-0.44332624054821862, 0.30698165551572787, -1, -0.11943532527516862, 0.06276022172521098, -0.31024929088776748}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForField", "java.lang.reflect.Field,java.lang.reflect.Type", new double[]{-1, 1, -0.72718093763088865, 0.095635620976056046, -0.1871875577502175, -0.36135805253901243}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getActualType", "java.lang.reflect.Type,java.lang.reflect.Type,java.lang.Class", new double[]{-0.42661485703437291, 1, -0.45328971556793896, -0.82793353174882767, -0.13533785416662419, 0.049272931330040515, 0.37920248766082371, 0.56418290281353944, -0.50427663668051514}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:[Ljava.lang.reflect.Type;[]", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{-0.65858646637963103, 0.3470042891155587, -0.84302126738586036, -0.75813762474354229, 1, -0.22514994627412993, 0.7124324871751484, 0.17526669272807752, 1}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getActualType", "java.lang.reflect.Type,java.lang.reflect.Type,java.lang.Class", new double[]{-0.91499198875850396, 0.85674452442919868, -1, 0.34877449375266811, -0.41582154246973885, -0.43558178801371911, 0.67247499247372555, -0.54524803106685449, -0.0087476711461986489}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:[Ljava.lang.reflect.Type;[]", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{-0.99896262196827879, 0.48467237184470224, -1, -0.44678049062531666, 0.82723102614689081, 0.36904005364450015, 0.23911453163582003, -0.12245885021425605, -0.22927226260772066}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForField", "java.lang.reflect.Field,java.lang.reflect.Type", new double[]{0.063505041032821119, -0.17819270361420603, -0.99698825823276882, -0.95463429887642015, 0.10738068635823815, -0.18077582929419941}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "extractRealTypes", "[Ljava.lang.reflect.Type;,java.lang.reflect.Type,java.lang.Class", new double[]{-0.028018572742099401, 0.26504446736625642, -0.15927209658067565, 0.16725177485781062, -1, 0.5062922984382523, 0.268559517514664, 0.34359021244521087, 0.10661810164909925}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForArray", "java.lang.reflect.Type", new double[]{-0.30416366511305304, 0.70901817171622372, -0.81413130132702116}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("com.google.gson.TypeInfoFactory", "", "getTypeInfoForField", "java.lang.reflect.Field,java.lang.reflect.Type", new double[]{-0.22184658082028125, 1, -0.87693830760132063, -0.5383411978595517, -0.079809586115275361, -0.76965064709532727}));
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
