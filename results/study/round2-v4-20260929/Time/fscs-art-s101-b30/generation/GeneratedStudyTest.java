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
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "", "toString", "java.lang.String,java.util.Locale", new double[]{-0.61049100893172659, 0.93050221412222234, 0.84795280335358858, -0.065722643606052067, 0.3269412890601211, -0.57095406052406394}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:object-type:org.joda.time.field.UnsupportedDurationField", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "readResolve", "", new double[]{0.12882092325956251, -0.69326658568349031, -0.93083543432074656}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology", "<init>", "", new double[]{0.84419360518388054, -0.88350808600720598, -0.17636507857656336, -0.095855047296162565, -0.45173220679099213, 0.23290704122795813, -0.71310127545302926, 0.49428924628021642, 0.71537835353299362}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "[Lorg.joda.time.DateTimeFieldType;,[I", "<init>", "", new double[]{-0.91305235990915179, -0.8866022194656431, -0.99482334584165555, -0.74906491504783745, 0.97229743587178574, 0.67043683088199102}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:object-type:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "", "withChronologyRetainFields", "org.joda.time.Chronology", new double[]{-0.8253894866562046, -0.71927621884485715, 0.11886588018702704}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("constructed:org.joda.time.field.UnsupportedDurationField", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "<init>", "", new double[]{0.40271440023471206, -0.92232175624706847, 0.27658601623459678}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "add", "long,long", new double[]{0.12865366861196126, -0.088637430838607312, -0.75397748974073631, -0.78893223896963605, -0.098240924134365137, 0.88179545402043402, 0.40594847834353853, -0.88823931633134268, 0.83332353531440195}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getMillis", "long,long", new double[]{0.90481255926255488, -0.64837197347826536, 0.91835189592311695, 0.34717975726305772, -0.51069729199640657, 0.97186767110912298, 0.73196404244994051, 0.017020411271082558, 0.61766063961286277}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getName", "", new double[]{-0.98528479423193494, 0.57445842359216792, -0.88392679032320443}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:null", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getType", "", new double[]{-0.22590167408965112, 0.61568159754379903, 0.60151205531523599}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:object-type:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "", "plus", "org.joda.time.ReadablePeriod", new double[]{-0.4880470136791526, -0.5192224365250413, 0.74449624476667586}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:object-type:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "unsupported", "", new double[]{0.72204330955528429, 0.39475478200114722, 0.54126099773910519}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getUnitMillis", "", new double[]{-0.9943224360261036, 0.44716267462658399, 0.39901195982456672}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology", "<init>", "", new double[]{-0.21637258553952177, 0.38348709814971693, -0.50239564332240327, -0.65318093168237468, 0.56467045734745458, 0.86885995551400219, -0.51392860860074174, -0.38872789764324533, 0.25556089064273158}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getValue", "long", new double[]{0.5484581348038764, -0.84087073209154894, 0.64312248324027799, -0.55174249108511475, -0.45389411979647543, -0.93302519398416428}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.String:W10=", SqaProbe.observe("org.joda.time.Partial", "", "toString", "", new double[]{0.6522350714986751, 0.43344716451425391, 0.62961437200645221}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "org.joda.time.ReadablePartial", "<init>", "", new double[]{0.95571409580348754, 0.60851211527398585, 0.85998926260855346}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getDifferenceAsLong", "long,long", new double[]{0.54849445028440735, 0.46908791122445126, -0.38930686713373652, 0.96608944732431956, -0.54812864250903237, -0.12387756615697909, 0.40165077960110596, -0.91507067544140397, -0.071306499157099967}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("org.joda.time.Partial", "", "getFieldType", "int", new double[]{-0.16455097276160524, 0.52221115497255655, -0.77317283340073972}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "[Lorg.joda.time.DateTimeFieldType;,[I", "<init>", "", new double[]{0.86414162428013386, 0.74174779558000337, -0.54173292633315606, 0.39251579759709321, -0.86754323563953317, -0.54161929323622982}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:null", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getType", "", new double[]{0.86039528991892378, 0.80899889829188831, -0.77385625022184912}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:object-type:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "", "minus", "org.joda.time.ReadablePeriod", new double[]{0.35548500477808398, -0.21269090703081761, 0.65044343116203818}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getName", "", new double[]{0.48099083187059044, -0.75819391548459047, -0.20945945649577746}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology", "<init>", "", new double[]{-0.87887382800624692, 0.36444709652624563, 0.42251576683796177, 0.44227712207794045, 0.20800125155249338, 0.74040318189706822, -0.85633276356474708, 0.2135030736282828, -0.22806223478996901}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getDifference", "long,long", new double[]{-0.80008469068668653, 0.34211581485089559, -0.37462727272186847, -0.21602953933400526, -0.47577300834227598, 0.17071403936162066, -0.95967732083885005, -0.08088815261727178, 0.36340465108376696}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("org.joda.time.Partial", "", "getValue", "int", new double[]{0.94744456139654409, 0.50112266930991911, 0.16850873462727289}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:object-type:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "", "withChronologyRetainFields", "org.joda.time.Chronology", new double[]{-0.39918694722377857, 0.44437528679787697, -0.88984882469621818}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.String:W10=", SqaProbe.observe("org.joda.time.Partial", "", "toStringList", "", new double[]{-0.460393480118934, 0.73193305743482839, -0.60162212613242194}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "", "with", "org.joda.time.DateTimeFieldType,int", new double[]{-0.69761130692227247, -0.73853457388104271, -0.31416198525230987, -0.68736025453727079, -0.75894968997886036, 0.24244475727410197}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getMillis", "long", new double[]{0.38991587447352494, 0.53567618980245379, 0.52880566391416473, -0.69706696336752327, 0.020024811355388916, 0.3597516382235777}));
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
