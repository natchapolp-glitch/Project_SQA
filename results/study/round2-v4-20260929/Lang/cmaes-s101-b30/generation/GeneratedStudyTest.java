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
    assertEquals("value:java.lang.Double:MS4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String,double", new double[]{-0.19275191598468638, 1, -0.29502854980607007, -0.86670299434726183, 0.18153854892674576, 0.09391060736277862}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createNumber", "java.lang.String", new double[]{1, 0.35544948241648838, 0.16823257361162219}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Double:ODU3LjMwNDQ1ODE3MjkyMzI=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String,double", new double[]{0.23283417482554164, -0.46267521452595367, 0.21717172739210969, 0.85730445817292322, 0.71650494943003107, -0.37697080129172161}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Double:LTEyMC43OTg0NjQ4NTc2OTExMg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "double,double,double", new double[]{0.43279433032780984, -0.73646665111255394, 0.5035220270807621, 0.6696236056587499, 0.027441509136262351, -0.53909145252019786, -0.12079846485769112, 0.92817365087904291, 0.12832943474678932}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Short:OTMy", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "short,short,short", new double[]{0.045872834140195586, 0.090821818057883724, 0.1307339591918435, 0.093150983379294144, 0.20070505829727012, 0.53243585678343874, 0.74023053518358728, -0.077508167005142722, -0.56432122761603942}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Double:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String", new double[]{-0.55710208988686183, -1, 0.011923870537382713}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Long:MTEwNA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[J", new double[]{0.11035764021659317, 1, -0.40707862171624226}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{0.29068450657000516, 0.25956817639741148, -0.94741527720343555}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Float:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toFloat", "java.lang.String", new double[]{0.19129711544027342, -0.87495743092009015, -0.27542383336715137}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Integer:OTE1OA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[I", new double[]{0.40579399477970773, 1, 1}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isNumber", "java.lang.String", new double[]{-0.53883765574689102, -0.27139179896509541, 0.17202368374611182}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.Byte:MTY=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[B", new double[]{1, 0.23111193808090513, 0.78755474266275127}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createDouble", "java.lang.String", new double[]{0.60165835979717297, 0.0066584386224012859, 0.42665666234984828}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createLong", "java.lang.String", new double[]{0.75079399787769074, 0.072648348380701167, 0.73438344938221056}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:java.lang.Short:LTEw", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{0.78821249456848208, 0.37031465417209736, -0.5787820156444633, -0.15880339020286932, -0.019305878829441347, -0.4958518323430724}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.Short:LTM4MTU=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{0.1363698659363996, 0.93577464981765535, 0.18779846752594648, -0.38146193679570983, 0.48006318119081526, -0.39122444266883794}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:java.lang.Long:NTc2Mg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[J", new double[]{0.57619252051591552, 0.33436859982142975, 0.76065369647023595}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:java.lang.Float:NDguMTIwNjU1", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[F", new double[]{-0.29187934606806032, 0.37637598622060708, 0.38531252741686772}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.Float:LTEyMC4wNjQxOA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "float,float,float", new double[]{-0.45913074726457392, -0.18802364964653623, -1, -0.12006417720213754, 0.37960962265786102, 0.083646434076255383, 0.79619139847903364, -0.31917937833891297, -0.67237688794091222}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[D", new double[]{0.22984779166603189, -0.65244630607372334, -0.69036905371028745}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:java.lang.Double:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String", new double[]{0.30146705991641948, -0.65477082251665308, -0.085595643522959508}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Byte:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String", new double[]{0.80908102185149566, 0.38718051716727397, 0.37665933200732504}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.Float:MS4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.19626383800479327, 0.38006015155232609, 0.19519664483961113}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.Float:LTIuMTQ3NDgzNjVFOQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "float,float,float", new double[]{0.67662165087183324, 0.90756826837707949, -0.080457025499786061, 0.1705333052953199, -0.86283512411736207, 0.10449669693099872, 1, -0.23745794858322997, 1}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Long:OTUyMA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[J", new double[]{0.44195050543650083, 0.44493861398585893, 0.86013182989782089}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isAllZeros", "java.lang.String", new double[]{-0.75303180825880101, 0.026222674355349126, 0.2765532712817842}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.lang.Double:MTAwMC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String,double", new double[]{0.15854829599109152, -0.0089051942313808641, 0.41604634027338178, 1, 0.97632238002160177, 0.33299269935211934}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.Float:MTI3LjA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[F", new double[]{-0.57283555577786505, -0.21921509458871266, 1}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isAllZeros", "java.lang.String", new double[]{0.27928199308324858, -0.9420946603683733, 0.93514132778328096}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isAllZeros", "java.lang.String", new double[]{-0.30101112425714749, 0.73771031846249191, 1}));
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
