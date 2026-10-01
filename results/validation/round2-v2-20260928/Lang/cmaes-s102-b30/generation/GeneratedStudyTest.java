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
    assertEquals("value:java.lang.Long:NjY1OQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "long,long,long", new double[]{0.66590947354708863, 0.45803966823341336, 0.82928909475281765, -0.46043473069732355, -0.37045971698347407, -0.57359663273266814, -0.52191718761276218, 0.71039957628821593, 0.19781276765747477}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:java.lang.Double:LTQ5Ni4yMTc1MjQzNDIzODc2", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "double,double,double", new double[]{-0.49621752434238758, 0.71086565841001481, -0.67208203741790584, -0.30835671335960285, 0.30024953046113839, 0.7585033920445543, 0.78813956625265424, -0.30385214520275933, -0.7644431060446405}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Byte:LTEx", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String,byte", new double[]{0.25327733207262509, 0.12900675175914036, 0.72636126217288544, 0.51087477187740371, 0.66163265129573756, -0.61116365894584956}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Double:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String", new double[]{0.79367696683281352, -0.99369976831238582, -0.061978994107967769}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Double:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String", new double[]{-0.46963283239284093, -0.60414120575010744, -0.23072361993500579}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Byte:LTcz", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "byte,byte,byte", new double[]{0.27430036163934651, 0.61656543028135391, -0.66927083313095648, -0.76499691609893472, 0.62695280847735346, -1, -0.7850396798941025, -0.53032789279071368, -0.51770017297130644}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Long:MjU2", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String,long", new double[]{-0.11945373369540913, -1, 0.84216154557724043, 0.34612982744433296, -0.12689827943879683, 1}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.Integer:MTAwMDA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[I", new double[]{0.91094667782099037, 0.38631901537418206, -0.15264315247666979}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Integer:MjU1", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[I", new double[]{0.24409916344278193, -0.26259864722400267, 0.21960046346932621}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{0.38691521573118937, -0.28868213886604432, 0.94646078169459547, 0.20131234958452696, 0.64044855444759596, 0.39363532710196519, -1, -1, 0.27045988225061912}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:java.lang.Float:MzI2LjU2MTY1", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[F", new double[]{-0.013438361056442583, 0.62265619941478878, 0.46368873145022238}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createNumber", "java.lang.String", new double[]{0.30876686923876318, 0.50226849645402605, 1}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.Integer:LTEw", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String,int", new double[]{-0.076309111079138403, 0.50344114010348762, -0.5602564091989175, -0.14390821099504617, -0.076473615992285679, 0.35952844469779877}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[D", new double[]{-0.081589038976355444, 1, -0.62873027838265294}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:java.lang.Long:MzMwMg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[J", new double[]{0.33019395750088926, 0.22165268425359561, 0.96663312587707839}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.Integer:MjU1", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[I", new double[]{0.25211354280774595, -0.32387748075596468, 0.25723022163523751}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:java.lang.Long:LTQ3MDg=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "long,long,long", new double[]{0.11915919860343802, 1, 0.69475948093796103, 0.40785940515574176, 0.58790470044020771, -0.38167408713193574, -0.47077080504590424, 0.37581420918941272, 0.14572918792877826}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:java.lang.Long:LTE0OTU=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "long,long,long", new double[]{1, 0.51297058999341505, 1, 0.014225004317886702, 0.39574727572499346, -0.70439613441261517, -0.14950565283545325, 0.68858695697468653, -0.069081447521525341}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.Double:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String", new double[]{0.31957458311956932, 0.98404202280471853, 0.81250090349374027}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.Integer:LTQ2NzY=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{0.28195117111308787, -1, 0.78685761405918342, 0.53432207501917994, -1, -1, -0.46761073916330342, 0.83035300832137426, 0.23621258328463662}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.33671124142984094, 0.077731084767339254, 0.54803258188328252}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Integer:MTI4", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "int,int,int", new double[]{-0.84987678757292695, 0.12750687811647921, -0.70068923355268464, 0.13960827190825575, -1, 0.21540744777491572, -0.32547009374122721, 1, -0.9568792465138638}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.Float:MS4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.17728795069094738, 0.50667693026953209, -0.8126497271599622}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createInteger", "java.lang.String", new double[]{-0.62361652915791188, 1, 0.18755046855668048}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Byte:LTEx", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "byte,byte,byte", new double[]{0.59751801573172103, 1, 0.41710747016056543, -0.68425122870596888, -0.31287476611993281, 0.76531378186778998, -0.38514885753774086, 0.35667589198995764, -0.015272899135596291}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Byte:LTEyOA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String,byte", new double[]{0.49761738352858015, 1, 0.80748127895776678, 0.16739068412908992, -0.19571545686548797, -0.47212868698237048}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.lang.Float:MC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toFloat", "java.lang.String", new double[]{0.49086536127234809, 0.86637580496794764, -0.16329269366237287}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String", new double[]{-0.16775497691206026, 1, 0.42240066846297347}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Long:LTY5MTA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String,long", new double[]{-0.4620929206737005, 1, -0.18367381862146692, -0.69099930272569754, 0.093190479684698735, -0.24921002986322743}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Float:MTAwMC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.15590850287729052, 0.40820397761353677, 0.59213240636860309}));
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
