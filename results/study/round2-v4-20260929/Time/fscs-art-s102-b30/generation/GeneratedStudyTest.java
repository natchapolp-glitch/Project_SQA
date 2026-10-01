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
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getValue", "long", new double[]{0.23193057415244711, -0.66062835522866559, 0.42501389475041518, 0.21566498551494262, -0.23132505576605888, 0.23499526722893549}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology", "<init>", "", new double[]{0.055293009405522842, -0.20312547505984835, 0.87541208377393009, 0.88814461044913595, -0.65596618790424022, -0.85516073139501447, 0.71612203900659988, 0.76546425986492195, -0.23105900397517787}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology", "<init>", "", new double[]{-0.96304950195694539, -0.09554478487794138, 0.98930809070094106, 0.66606765917338406, 0.97875982970856268, 0.058378662565180761, 0.94353905352808032, -0.88808901720270694, -0.056066811595451238}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:object-type:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "", "without", "org.joda.time.DateTimeFieldType", new double[]{0.37819345299037721, 0.95678803630640608, 0.94455460946833392}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getMillis", "long", new double[]{0.75880069910296233, 0.11602159400727974, -0.64032158202602241, 0.98546903014776022, 0.80104242154634453, 0.49747174959193163}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:object-type:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "", "withPeriodAdded", "org.joda.time.ReadablePeriod,int", new double[]{0.12624821663825592, -0.63808562999606644, 0.88241364422984003, -0.5772607881380647, -0.93269065382769445, 0.44917871793146502}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getDifferenceAsLong", "long,long", new double[]{-0.67594568242204889, 0.98584609537283541, -0.44278481970304262, -0.83153167405903705, 0.31622462449272493, 0.95153350084816912, -0.42436619131513686, -0.53739828000988599, 0.16927305396397574}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "isSupported", "", new double[]{-0.92385666634700714, -0.060896351091694445, -0.47103070305443895}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "org.joda.time.DateTimeFieldType,int", "<init>", "", new double[]{-0.87967828237632606, -0.6537835696641443, -0.74242963070718271, -0.15274239221694708, -0.32085340773255888, -0.45464792044170133}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("constructed:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "org.joda.time.Partial,[I", "<init>", "", new double[]{0.91920247368690444, -0.59377799022618394, -0.85567911262472607, 0.737617583673845, 0.16637851994594777, 0.97047704737641527}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "hashCode", "", new double[]{-0.95328320387746479, 0.91772377012025497, 0.79457209146469276}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:object-type:org.joda.time.chrono.ISOChronology", SqaProbe.observe("org.joda.time.Partial", "", "getChronology", "", new double[]{-0.98162668565096611, -0.74584636951008521, 0.95789076468411172}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getDifferenceAsLong", "long,long", new double[]{0.60599365696981056, 0.94311505312892607, 0.8612050076012161, -0.77648208997596768, -0.028347360530222243, 0.38129743265757599, -0.92892591831869376, -0.96327366464740094, 0.55321641337359639}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getValue", "long,long", new double[]{-0.3949385839045767, -0.42699939646705709, -0.94904161481753535, -0.034732872242312585, 0.59934650266831069, 0.23609890888957374, 0.8666883797073921, 0.82888908089725089, 0.2745462953689406}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:object-type:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "", "without", "org.joda.time.DateTimeFieldType", new double[]{0.92368297316534731, 0.24726538162849243, -0.5469630893428854}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getUnitMillis", "", new double[]{-0.04342362691763757, 0.96733804106028676, -0.021863054249898095}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:object-type:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "", "minus", "org.joda.time.ReadablePeriod", new double[]{0.49354999593045989, -0.75353270600974254, -0.60947225581860431}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "", "withFieldAdded", "org.joda.time.DurationFieldType,int", new double[]{0.49580167076397097, -0.42048951354839414, -0.97641035723244429, 0.22905229379415504, -0.50505401363652069, -0.69476487324885738}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "", "withFieldAddWrapped", "org.joda.time.DurationFieldType,int", new double[]{0.95457050767459739, 0.84237596169574913, 0.051622202062840961, -0.082825329163203065, 0.46922742993342248, -0.8409392613259683}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology", "<init>", "", new double[]{-0.99299811614653999, -0.67853273401723047, 0.1820182136335724, 0.86501585299772676, 0.95766313413813298, 0.81834684363712862, -0.72894532295010328, -0.76219130721540629, -0.46265467058930332}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.UnsupportedOperationException", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "getValue", "long,long", new double[]{-0.23162125117156673, -0.73931195429845298, -0.35342156475437214, -0.25104900708801647, -0.11854167812094341, 0.192155791528694, -0.97880626047964969, 0.38604349804900617, -0.97949421168594886}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "", "with", "org.joda.time.DateTimeFieldType,int", new double[]{-0.52584739797930191, -0.25136945783419495, -0.94700282496530108, -0.076099915266512053, 0.88540648177852099, -0.20275045933235392}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "org.joda.time.DateTimeFieldType,int", "<init>", "", new double[]{0.92226478769584141, -0.65860549423755477, -0.056440584061172139, -0.95658303725301175, 0.51080409482081635, 0.98138737648828811}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:object-type:org.joda.time.Partial", SqaProbe.observe("org.joda.time.Partial", "", "without", "org.joda.time.DateTimeFieldType", new double[]{-0.85031746965309352, -0.735071530041147, -0.18892372491188647}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology", "<init>", "", new double[]{-0.9346867988827261, 0.97042906907132309, 0.078797641498781879, -0.41260064013612863, 0.92336865309131788, -0.2808601860148725, -0.63375567288659784, -0.8484736565543991, -0.97560765322192977}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "", "withField", "org.joda.time.DateTimeFieldType,int", new double[]{-0.8674311172652136, 0.89245068457546339, -0.37533434482710759, 0.69262817603482452, 0.10025095956581365, 0.11353549642854799}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "", "with", "org.joda.time.DateTimeFieldType,int", new double[]{0.20449375576437245, -0.99858838901774605, 0.0027262440051427639, -0.62391208006741516, -0.80612039289814019, -0.87828309609939126}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology", "<init>", "", new double[]{0.51660411431363884, -0.17681893780098945, 0.2688611034508126, -0.4423144793495557, -0.37016898299449386, 0.91886697062896894, 0.14939283050317798, 0.65147961392957643, 0.82039070514350443}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("org.joda.time.field.UnsupportedDurationField", "org.joda.time.DurationFieldType", "isPrecise", "", new double[]{0.17091109910962943, 0.41279724741728741, 0.7353128567528171}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.joda.time.Partial", "", "withFieldAddWrapped", "org.joda.time.DurationFieldType,int", new double[]{-0.82253337692121842, 0.7515534960290513, 0.40782860627179818, -0.72120889274289457, -0.86314448831498436, -0.42787561819528053}));
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
