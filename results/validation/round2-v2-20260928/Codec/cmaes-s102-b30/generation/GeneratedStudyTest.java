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
    assertEquals("value:java.lang.String:QUI=", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "encode", "java.lang.String", new double[]{0.66590947354708863, 0.45803966823341336, 0.82928909475281765}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:java.lang.String:MTExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "caverphone", "java.lang.String", new double[]{0.71039957628821593, 0.19781276765747477, -0.53069739928378745}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.String:QVAxMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "encode", "java.lang.String", new double[]{0.30024953046113839, 0.7585033920445543, 0.78813956625265424}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "encode", "java.lang.String", new double[]{0.12900675175914036, 0.72636126217288544, 0.51087477187740371}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Integer:NA==", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "getMaxCodeLen", "", new double[]{0.26354434127739718, -0.45589316586317874, 0.79367696683281352}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.String:SExQ", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "encode", "java.lang.String", new double[]{0.30325781717188682, -0.19900075226792249, 0.21174665565325093}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.String:MTExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "caverphone", "java.lang.String", new double[]{-0.23072361993500579, -0.61497722558604473, -1}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "encode", "java.lang.String", new double[]{-0.79520130025219837, 0.27430036163934651, 0.61656543028135391}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "isCaverphoneEqual", "java.lang.String,java.lang.String", new double[]{-0.7850396798941025, -0.53032789279071368, -0.51770017297130644, 0.72508669030682793, -0.11945373369540913, -1}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{0.95111700649363717, 0.33862326925780956, 1}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:java.lang.Integer:NA==", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "getMaxCodeLen", "", new double[]{1, 0.85135988168192411, 0.32700110124209952}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "metaphone", "java.lang.String", new double[]{1, 0.9789967363332932, 0.50342419627857171}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.String:Qg==", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "encode", "java.lang.String", new double[]{0.83457145464404103, 0.011428982831934942, 0.67217098475129955}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.String:SExQ", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "encode", "java.lang.String", new double[]{0.325073639638707, 1, 0.64804479166822826}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "isCaverphoneEqual", "java.lang.String,java.lang.String", new double[]{0.85827624615203901, 0.18139414216947547, 0.073417860142445335, -0.097899207233660523, 0.091693829415543326, -0.44122511100738526}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.String:MTExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "encode", "java.lang.String", new double[]{1, -0.81861218560021132, 0.9686882127489842}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:java.lang.Integer:NA==", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "getMaxCodeLen", "", new double[]{1, 0.09053924060604851, -0.80802236673819983}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:java.lang.String:SzExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "encode", "java.lang.String", new double[]{0.84304168746807551, 0.049570795483911134, 0.57336205909425875}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{1, 0.97377683927181757, 1}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.String:QVAxMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "encode", "java.lang.String", new double[]{0.68861209339806106, 0.67817490085794352, 1}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{1, 0.35799971871171576, 1}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "metaphone", "java.lang.String", new double[]{0.98991959760371562, -0.48660646073047059, 0.86385984004182426}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.Integer:NA==", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "getMaxCodeLen", "", new double[]{0.51108090157654951, 0.2451314586188936, 1}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "encode", "java.lang.String", new double[]{1, 0.93832062145742856, 1}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{1, 1, 1}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{0.42440557502576226, 0.53738817536855488, 1}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{1, 0.18265349835099204, 1}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{1, 0.67504498568190241, -0.70576048424505022}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "isCaverphoneEqual", "java.lang.String,java.lang.String", new double[]{0.48566538088495709, 1, 1, -0.19981136436964936, -0.83520396216021409, -0.73446944440923001}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{0.17787227165518094, 1, 1}));
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
