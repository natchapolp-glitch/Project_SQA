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
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createDouble", "java.lang.String", new double[]{-0.61049100893172659, 0.93050221412222234, 0.84795280335358858}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isDigits", "java.lang.String", new double[]{0.12882092325956251, -0.69326658568349031, -0.93083543432074656}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Short:LTEw", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{0.84419360518388054, -0.88350808600720598, -0.17636507857656336, -0.095855047296162565, -0.45173220679099213, 0.23290704122795813}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[S", new double[]{-0.91305235990915179, -0.8866022194656431, -0.99482334584165555}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.math.BigInteger:MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createBigInteger", "java.lang.String", new double[]{-0.8253894866562046, -0.71927621884485715, 0.11886588018702704}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Float:LTI0LjYyNDY0NQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toFloat", "java.lang.String,float", new double[]{0.40271440023471206, -0.92232175624706847, 0.27658601623459678, -0.024624645123015876, 0.60163273024969532, -0.26830017048695076}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[D", new double[]{0.12865366861196126, -0.088637430838607312, -0.75397748974073631}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.Float:LTIuMTQ3NDgzNjVFOQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[F", new double[]{0.90481255926255488, -0.64837197347826536, 0.91835189592311695}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Double:MjYyLjYzMDYzMDY1MjcyNTUz", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "double,double,double", new double[]{-0.98528479423193494, 0.57445842359216792, -0.88392679032320443, 0.26263063065272552, 0.93189660453542089, -0.49573131088154332, 0.17794965586031175, 0.76774328747836562, -0.12325744222066581}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Byte:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "byte,byte,byte", new double[]{-0.22590167408965112, 0.61568159754379903, 0.60151205531523599, 0.64369762828296739, -0.79029140335902959, 0.46407865056052855, -0.93994570194276972, 0.966099665948146, 0.48641624629789249}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createBigDecimal", "java.lang.String", new double[]{-0.4880470136791526, -0.5192224365250413, 0.74449624476667586}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isNumber", "java.lang.String", new double[]{0.72204330955528429, 0.39475478200114722, 0.54126099773910519}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.Float:LTY1NC4zMjI0NQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[F", new double[]{-0.9943224360261036, 0.44716267462658399, 0.39901195982456672}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.Short:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String", new double[]{-0.21637258553952177, 0.38348709814971693, -0.50239564332240327}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:java.lang.Byte:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String", new double[]{0.5484581348038764, -0.84087073209154894, 0.64312248324027799}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String", new double[]{0.6522350714986751, 0.43344716451425391, 0.62961437200645221}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:java.lang.Short:OTU1Nw==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "short,short,short", new double[]{0.95571409580348754, 0.60851211527398585, 0.85998926260855346, -0.37569062893746019, 0.58431216042043532, -0.11931584342079171, -0.95864665286489115, 0.11207992996941663, -0.77185690443305943}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:java.lang.Double:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String", new double[]{0.54849445028440735, 0.46908791122445126, -0.38930686713373652}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createInteger", "java.lang.String", new double[]{-0.16455097276160524, 0.52221115497255655, -0.77317283340073972}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.Short:ODY0MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[S", new double[]{0.86414162428013386, 0.74174779558000337, -0.54173292633315606}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:java.lang.Byte:LTEwMg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "byte,byte,byte", new double[]{0.86039528991892378, 0.80899889829188831, -0.77385625022184912, -0.41978828208141805, 0.16704066224974112, -0.43559950780154799, 0.058439618329498177, 0.31892834822919047, -0.82698835748817046}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createNumber", "java.lang.String", new double[]{0.35548500477808398, -0.21269090703081761, 0.65044343116203818}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.Double:MzI3NjcuMA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "double,double,double", new double[]{0.48099083187059044, -0.75819391548459047, -0.20945945649577746, -0.24530040102021911, 0.26275039525103772, 0.076956123747116356, 0.97662911050975842, 0.96429011809984178, 0.7520925190258918}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.Short:MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{-0.87887382800624692, 0.36444709652624563, 0.42251576683796177, 0.44227712207794045, 0.20800125155249338, 0.74040318189706822}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Byte:LTE=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String", new double[]{-0.80008469068668653, 0.34211581485089559, -0.37462727272186847}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Integer:LTkxMTE=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{0.94744456139654409, 0.50112266930991911, 0.16850873462727289, -0.91111736742881622, 0.98716674690377637, 0.35692485816226105, 0.91701976075838476, 0.58100324314379459, 0.511719627177464}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.math.BigInteger:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createBigInteger", "java.lang.String", new double[]{-0.39918694722377857, 0.44437528679787697, -0.88984882469621818}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.460393480118934, 0.73193305743482839, -0.60162212613242194}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createBigInteger", "java.lang.String", new double[]{-0.69761130692227247, -0.73853457388104271, -0.31416198525230987}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Float:Mi4xNDc0ODM2NUU5", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "float,float,float", new double[]{0.38991587447352494, 0.53567618980245379, 0.52880566391416473, -0.69706696336752327, 0.020024811355388916, 0.3597516382235777, 0.82397576645854032, -0.99214992942018121, -0.81787490307038802}));
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
