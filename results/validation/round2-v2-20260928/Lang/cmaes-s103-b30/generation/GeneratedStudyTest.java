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
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String", new double[]{-0.086937624036138444, -0.19015235631540686, -0.95901584820932984}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:java.lang.Float:MS4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.21705431303519587, 0.37009804347218705, 0.21028105214878293}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createInteger", "java.lang.String", new double[]{0.56272119404747301, -0.10137957821232831, -0.95603439494467901}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isDigits", "java.lang.String", new double[]{-0.70081578666644284, -0.50113769869357083, -0.46085807148078128}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isNumber", "java.lang.String", new double[]{-0.16817690795811496, -0.1980481460954189, 0.65862114415714479}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Integer:MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createNumber", "java.lang.String", new double[]{-0.8587121499689726, 0.58361920202428197, 0.37065590652482211}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Byte:LTEyOA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[B", new double[]{-0.24964434344920541, -0.87251372910350555, 0.68511466207888938}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.Long:Mg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[J", new double[]{-0.53904954727556542, -0.76779168134462372, -0.33450071728107311}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Float:LTIuMA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "float,float,float", new double[]{0.66660148570462618, -0.23985059497177683, -0.47793331026552927, -0.46334439603861455, -1, -1, -0.38180558725340735, -0.49195262232187797, 1}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{0.51885379858549985, 0.062979045034559852, -0.14100915120929239}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:java.lang.Integer:MTA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String,int", new double[]{-0.21958624056942289, 1, -0.34137568365581789, -0.31841184644121279, -0.023709317166059035, -0.62683324864026213}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.Integer:LTYzMjc=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String,int", new double[]{-0.9568017819952368, -0.22457270191658013, -0.17116371374309419, -0.63272746847045958, 0.13974771640569819, -0.62432835787009733}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[S", new double[]{0.63781651785499238, 0.5981472731777433, -1}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String", new double[]{0.27858609442608717, -0.84314198865756385, -0.35897084370713139}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createBigInteger", "java.lang.String", new double[]{0.044713278620551758, 0.55957544311144258, -0.83078793020462083}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[D", new double[]{0.30338149605136083, 0.20077406163148226, -1}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[I", new double[]{-1, 0.19078831420779668, -0.64275160587238189}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:java.lang.Long:NDI5NDk2NzI5NQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createLong", "java.lang.String", new double[]{-0.24851612514434879, -0.071951536412682843, -0.44227775596221286}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.Integer:MTgzOA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[I", new double[]{0.18382697301614437, 0.56209781590139629, -0.039151610331270681}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.Integer:NjQ3OQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "int,int,int", new double[]{0.64789793496252046, 0.39663981888463951, -0.21702005323499718, 0.53019945459273354, 0.71854578884934872, -0.63820659396849611, 1, -0.97783918330956154, 0.63123292937481601}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:java.lang.Double:LTE3Ny4wMzI2NjExOTkwMzYy", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "double,double,double", new double[]{-0.37221633818104916, 0.21026541585294489, -1, -0.17703266119903621, 0.057209818524350843, -0.39390265056778351, -0.49653270692280821, 0.67197864396754281, 0.066889992838071483}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Long:LTk1OTM=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[J", new double[]{-0.95931404335376325, 1, -0.59131723890975862}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createDouble", "java.lang.String", new double[]{-0.35445744141306357, 0.98373031526665944, -0.3139630659765793}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createBigInteger", "java.lang.String", new double[]{-0.58525000163328833, 0.27381763199761877, -0.70928251550164778}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Integer:LTg0MTM=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{-0.84127533952346223, 0.7682748237584911, -0.85307685520429222, 0.27315569543636786, -0.43332928829040601, -0.51687509893925654, 0.35068303176744436, -0.52882858530939802, 0.077949151265141944}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.math.BigInteger:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createBigInteger", "java.lang.String", new double[]{-0.92435734880665243, 0.40829896211270439, -1}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String", new double[]{0.13450639102436629, -0.23675953142785372, -0.88272658662773829}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createInteger", "java.lang.String", new double[]{0.042799757604360111, 0.19364817823540351, -0.032881866027385409}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Short:LTQ1NzQ=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{-0.23132185367255964, 0.65045480200902195, -0.7033521315966017, -0.45738347622375602, 0.21689921338050344, -0.89696819651056048}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Short:MTI4", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "short,short,short", new double[]{-0.14968431426260742, 1, -0.75652107361283472, -0.64035154379353743, -0.089028158720171752, -0.87535893676612286, 0.18591709285708641, -0.36599905606447797, -0.13692927800529514}));
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
