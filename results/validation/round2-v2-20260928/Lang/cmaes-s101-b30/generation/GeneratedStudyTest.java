import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
public class GeneratedStudyTest {
  @Test(timeout=10000)
  public void generated1() {
    assertEquals("value:java.lang.Float:MjU2LjA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "float,float,float", new double[]{-0.19275191598468638, 1, -0.29502854980607007, -0.86670299434726183, 0.18153854892674576, 0.09391060736277862, 0.41429512464883717, -0.22083770669199668, 0.89450074379313704}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createNumber", "java.lang.String", new double[]{1, 0.35544948241648838, 0.16823257361162219}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Float:ODU3LjMwNDQ0", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "float,float,float", new double[]{0.23283417482554164, -0.46267521452595367, 0.21717172739210969, 0.85730445817292322, 0.71650494943003107, -0.37697080129172161, -0.80302803370382236, 0.4445014790358171, -0.40473810276689071}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Double:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String", new double[]{0.43279433032780984, -0.73646665111255394, 0.5035220270807621}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Short:OTMy", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "short,short,short", new double[]{0.045872834140195586, 0.090821818057883724, 0.1307339591918435, 0.093150983379294144, 0.20070505829727012, 0.53243585678343874, 0.74023053518358728, -0.077508167005142722, -0.56432122761603942}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Double:LTQ1NS4zMDkyNjM1MzkxMjIx", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String,double", new double[]{-0.55710208988686183, -1, 0.011923870537382713, -0.4553092635391221, 1, -0.32926499126108122}));
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
    assertEquals("value:java.lang.Float:LTEyMS43NTcxMQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toFloat", "java.lang.String,float", new double[]{0.19129711544027342, -0.87495743092009015, -0.27542383336715137, -0.12175711119727206, 0.16675745809446776, -0.20054893332519608}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Integer:MjU2", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{0.40579399477970773, 1, 1, 0.42765683273523314, -0.30641914407703352, 0.72038469793523263, 0.49371923411137969, 0.071252638175748256, -0.060797673131111001}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:java.lang.Byte:Mg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[B", new double[]{-0.53883765574689102, -0.27139179896509541, 0.17202368374611182}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.Byte:MTY=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[B", new double[]{1, 0.23111193808090513, 0.78755474266275127}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{0.60165835979717297, 0.0066584386224012859, 0.42665666234984828}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createNumber", "java.lang.String", new double[]{0.75079399787769074, 0.072648348380701167, 0.73438344938221056}));
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
    assertEquals("value:java.lang.Float:LTI5MS44NzkzMw==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "float,float,float", new double[]{-0.29187934606806032, 0.37637598622060708, 0.38531252741686772, 0.23453939161297782, 1, 0.14321155153699544, 0.42455114565172036, 0.054828149705231678, 0.85303181736865863}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[F", new double[]{-0.45913074726457392, -0.18802364964653623, -1}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.Double:LTU4MC41MzQwMzg3Nzk1MTI3", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "double,double,double", new double[]{0.22984779166603189, -0.65244630607372334, -0.69036905371028745, -0.58053403877951271, 1, 0.4105890336834454, -0.21208886834982571, -0.32842700924986556, 0.55649042772528579}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:java.lang.Float:MjU1LjA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[F", new double[]{0.26959758730338401, -0.49065952802851071, -0.23954236877623375}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Double:NzUwLjA1NzU3OTkzMDcxNjk=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[D", new double[]{0.75005757993071687, 0.49795917023259528, 0.20157046739576195}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.Float:MS4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createNumber", "java.lang.String", new double[]{-0.19983940037169831, 0.48769226026624474, 0.040248018948360248}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.Integer:NjI2Mw==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[I", new double[]{0.62628572674841687, 0.99025680683474115, -0.22343918079859815}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String", new double[]{0.40704452614923003, 0.55097238442765961, 0.66428937920166753}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isDigits", "java.lang.String", new double[]{-0.73607453512176857, 0.151663275846391, 0.11437000223304122}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.lang.Float:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toFloat", "java.lang.String", new double[]{0.13413070932333213, 0.12064459493974579, 0.24113402725624764}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.Float:MTI3LjA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toFloat", "java.lang.String,float", new double[]{-0.55941862499326511, -0.080857794503350386, 1, 0.057239886042356872, -0.73315795245328297, -0.28564239243783962}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isDigits", "java.lang.String", new double[]{0.24299536334960087, -0.76229285824033066, 0.71917992642218276}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Byte:LTcy", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "byte,byte,byte", new double[]{-0.30475137270932473, 0.82671632635218884, 1, 0.1464203765762406, 0.26186653188749709, -0.35282196501581931, 0.46600702494328416, 0.37409063643047497, 0.78195141426940096}));
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
        for (Class<?> type : types) if (!supported(type) || type == void.class) return false;
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
        if (type.isArray()) {
            int length = bucket(c, 5);
            Object array = Array.newInstance(type.getComponentType(), length);
            for (int i = 0; i < length; i++) {
                Array.set(array, i, argument(type.getComponentType(),
                    Math.max(-1, Math.min(1, a + i * 0.17)), b, c));
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
        throw new IllegalArgumentException("SQA_HARNESS unsupported argument " + type.getName());
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
        if (!scalar(type) && !(value instanceof Number)) throw new IllegalStateException("SQA_HARNESS unsupported return " + type.getName());
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
            Method method = methodName.equals("<init>") ? null : target.getDeclaredMethod(methodName, parameterTypes);
            if (method == null || !Modifier.isStatic(method.getModifiers())) {
                Constructor<?> ctor = target.getConstructor(ctorTypes);
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

    private static void discover(String[] classes) {
        List<String> targets = new ArrayList<String>();
        List<String> errors = new ArrayList<String>();
        for (String className : classes) {
            try {
                Class<?> target = Class.forName(className, false, SqaProbe.class.getClassLoader());
                if (!Modifier.isPublic(target.getModifiers())) continue;
                List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();
                if (!Modifier.isAbstract(target.getModifiers()) && !target.isEnum()) {
                    Constructor<?>[] all = target.getConstructors();
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
                    if (!Modifier.isPublic(method.getModifiers()) || method.isSynthetic()
                        || method.isBridge() || !supportedParameters(method.getParameterTypes())
                        || !(supported(method.getReturnType()) || Number.class.isAssignableFrom(method.getReturnType()))) continue;
                    if (Modifier.isStatic(method.getModifiers())) {
                        targets.add(descriptor(className, "", method.getName(),
                            typeNames(method.getParameterTypes()), method.getParameterCount()));
                    } else if (!constructors.isEmpty()) {
                        Constructor<?> ctor = constructors.get(0);
                        targets.add(descriptor(className, typeNames(ctor.getParameterTypes()), method.getName(),
                            typeNames(method.getParameterTypes()), ctor.getParameterCount() + method.getParameterCount()));
                    }
                }
                for (Constructor<?> ctor : constructors) {
                    if (ctor.getParameterCount() > 0)
                        targets.add(descriptor(className, typeNames(ctor.getParameterTypes()), "<init>", "", ctor.getParameterCount()));
                }
            } catch (Throwable error) {
                if (error instanceof VirtualMachineError || error instanceof ThreadDeath) throw (Error)error;
                errors.add(quote(className + ":" + error.getClass().getName()));
            }
        }
        System.out.println("{\"targets\":[" + String.join(",", targets) + "],\"errors\":[" + String.join(",", errors) + "]}");
    }

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("discover")) {
            discover(Arrays.copyOfRange(args, 1, args.length));
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
