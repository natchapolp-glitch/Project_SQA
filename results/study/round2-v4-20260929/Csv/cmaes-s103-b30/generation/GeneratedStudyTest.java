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
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readLine", "", new double[]{-0.086937624036138444, -0.19015235631540686, -0.95901584820932984}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "[C,int,int", new double[]{0.063263881649785525, -0.11841903632738525, 0.044382125342745417, 0.28231650688195964, 0.50790055152760694, 0.52502450441589199, 0.25012284707244503, 0.56272119404747301, -0.10137957821232831, -0.95603439494467901, -0.90719309326957998, 0.53622340027329785}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "getLineNumber", "", new double[]{-0.494475084655083, 0.0040459293084874875, 0.57588908140818373}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "getLineNumber", "", new double[]{-0.92681512075952299, -0.16817690795811496, -0.1980481460954189}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readLine", "", new double[]{0.37065590652482211, 1, -0.045888503638938767}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "[C,int,int", new double[]{0.59795106071021442, 0.25735876399775598, 0.83215100037503487, 0.23857085487326285, 0.52341535362575675, -0.53904954727556542, -0.76779168134462372, -0.33450071728107311, 0.077811003374410548, 0.587736522224383, -0.76187097188586372, -0.40965437410895439}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "lookAhead", "", new double[]{0.4677090632944122, -0.37078033349470269, 0.66660148570462618}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readLine", "", new double[]{0.062979045034559852, -0.14100915120929239, -0.91397048212208643}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readAgain", "", new double[]{-0.25597727799187614, -0.44678064741143148, 0.21457686671457607}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readAgain", "", new double[]{0.17292909312057625, -0.60222459698836828, 0.67926583082859771}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readAgain", "", new double[]{0.38638519770635621, -0.87597360166355032, 0.21681961560453919}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "", new double[]{0.52842194295121203, -0.88290308789926641, -0.26058120957820918}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "[C,int,int", new double[]{0.6772109360939067, -0.52848615851579572, -0.80942106592046004, -0.28511092317939951, -1, 0.13834607478265398, -0.29541728886632956, -0.54829230920295347, 1, -0.31460508602804249, -0.50478441262213425, 1}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "", new double[]{-0.05291354057152188, -0.33544111518968356, -0.28733389368739531}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "<init>", "", new double[]{0.41822258106240306, -0.39271760033638181, 0.26775509490029004}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readLine", "", new double[]{-0.48049378481267074, 1, -1}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "<init>", "", new double[]{-0.1846665084550799, 0.2308606508005844, -1}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "", new double[]{0.51692650387201755, -0.05705559148984235, -0.75860946868812529}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readAgain", "", new double[]{1, -0.058195744960197096, 0.17755726796710464}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readLine", "", new double[]{-0.63582742515870072, -0.29736897385060812, -0.082508539014557541}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readLine", "", new double[]{0.32563640596856047, -0.88099497607519317, -0.76290345946180405}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readLine", "", new double[]{0.49450951840392193, -1, 0.60404238123575804}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readAgain", "", new double[]{0.27024012481945237, -0.82675958115376691, -0.14223225787744986}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "readAgain", "", new double[]{0.47507134638269233, -0.94426280794360518, -0.20088124048908826}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "<init>", "", new double[]{0.30446043338962969, -0.74251471858038776, 0.36429528690471236}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "", new double[]{0.8605909291455105, -1, 0.26190517018353771}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "", new double[]{-0.32942962911914647, 0.21132295338672225, -1}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "lookAhead", "", new double[]{0.70735684305170154, 0.0060352226339583925, -0.86519739329071621}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "[C,int,int", new double[]{-0.27309859948543996, -0.32407971794309393, -0.40871123470917653, -0.058684050237360952, 0.31416579247676296, -0.55566347467625288, -1, 0.28004057413338412, 0.95114371461154357, -0.71733175199562682, 0.28634836305041195, 0.78274122118332368}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.apache.commons.csv.ExtendedBufferedReader", "java.io.Reader", "read", "[C,int,int", new double[]{0.047152807759255455, -0.90341546753990043, 0.39309575575137812, -0.54127264456746316, -0.24684886996733924, 0.17747361758859947, -1, -0.43181176101250851, 0.64845210427517996, 0.97267174111973409, -1, 0.49378240640950338}));
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
