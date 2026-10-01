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
    assertEquals("value:java.lang.Short:Mg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{-0.087403447416303459, 0.43769406820047285, 0.46977248517067904, -0.57019666300587324, -0.84392406176975188, 0.20548647253427887}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:java.lang.Long:MjU2", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[J", new double[]{0.3991581025858999, -0.81989119491306939, -0.029694562140674119}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Byte:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String", new double[]{0.54906261628654995, 0.79005920855128231, 0.36232742019832376}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createLong", "java.lang.String", new double[]{-0.91322315225944317, -0.94430192055912854, -0.078911036287813596}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createNumber", "java.lang.String", new double[]{0.66456021434610557, -0.79365698603202306, 0.92566268414465314}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Byte:MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String,byte", new double[]{0.7795420162805009, 0.71802543657444984, -0.050305146010493074, -0.77453482313032063, -0.10327046525623929, -0.78848555867908776}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createInteger", "java.lang.String", new double[]{-0.95396870967885894, -0.81603581326430064, -0.45514184166646965}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createLong", "java.lang.String", new double[]{-0.0049573389800461332, 0.98600663293162372, -0.72649427462933791}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Byte:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String", new double[]{0.15208346534056916, -0.35013308531831933, 0.644645743035269}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Byte:Nzc=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[B", new double[]{-0.70908361368319217, 0.7640173635055747, -0.16215041361885962}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createLong", "java.lang.String", new double[]{0.58253025082355769, 0.085323574360981036, -0.8605518449132652}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isDigits", "java.lang.String", new double[]{-0.053648369401303242, -0.54684406795589857, -0.60477505846991519}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.Short:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String", new double[]{0.23263625124756193, 0.83911466013894764, -0.99074711104687263}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.Integer:NzE2MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[I", new double[]{0.71609082808711588, 0.84638221043485995, -0.38443070291338266}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:java.lang.Double:LTIuMTQ3NDgzNjQ4RTk=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[D", new double[]{0.98851228265107083, -0.3183464060754313, 0.63523691288757789}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.Short:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String", new double[]{-0.052992970951414753, -0.67964301923406212, 0.74422101781393879}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:java.lang.Float:LTEwMy43MjQ1Ng==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toFloat", "java.lang.String,float", new double[]{-0.48200370691156591, -0.29140884927017119, 0.79376001310341304, -0.10372456432011146, 0.46527511825177359, -0.91779691443732037}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createBigInteger", "java.lang.String", new double[]{-0.70696984809375363, 0.071940726498731067, 0.88749199046492788}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isDigits", "java.lang.String", new double[]{-0.31125295771059314, 0.85488146097811679, -0.037376280681244323}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[B", new double[]{-0.41552522620604693, -0.33142408064916418, -0.69469548945832593}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createLong", "java.lang.String", new double[]{0.55874032985481725, 0.22435847625999483, 0.81728548245574717}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Double:MS4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "double,double,double", new double[]{-0.72168802197350335, 0.23367125700071822, 0.55039725508596726, -0.83360237664787173, -0.72167433952408788, 0.70411988179184082, 0.65658184878764003, -0.56579813685033487, -0.7183212876316003}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.Long:LTIxNDc0ODM2NDg=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[J", new double[]{0.82341613518936829, -0.3890167783582732, 0.82548717243477876}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.Integer:NDA0NQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[I", new double[]{0.40446394256206308, 0.96832868860531729, -0.45757030260969489}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Long:LTg4MDY=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[J", new double[]{-0.88064440943658306, 0.46990348920892178, -0.29429943988880281}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Integer:MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String,int", new double[]{-0.84696138333991833, 0.9841988394770067, -0.097015479860223852, -0.80713131253936266, 0.76860515499125115, 0.66474512838611033}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.lang.Integer:NzM2Mw==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "int,int,int", new double[]{-0.98542039009049498, -0.56510034601995129, -0.3917355565784495, 0.73632298522002015, 0.005230535880403675, 0.081689198651239137, 0.68747027596133825, -0.31489926552201331, 0.96723181051634755}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.Double:Mi4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[D", new double[]{-0.9335259085516181, -0.43841976210751032, 0.5491798944385653}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createNumber", "java.lang.String", new double[]{0.98217528952537703, -0.95352532155032965, -0.40187572217917555}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Short:NzYxNA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[S", new double[]{0.42144445008435505, 0.79056597951111107, 0.45145673445658319}));
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
