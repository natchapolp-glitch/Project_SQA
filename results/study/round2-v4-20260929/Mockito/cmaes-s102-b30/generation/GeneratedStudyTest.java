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
    assertEquals("value:object-type:java.util.LinkedList", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "", "createFrom", "java.util.List", new double[]{0.66590947354708863, 0.45803966823341336, 0.82928909475281765}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "hasSameMethod", "org.mockito.invocation.Invocation", new double[]{-0.49621752434238758, 0.71086565841001481, -0.67208203741790584, -0.30835671335960285, 0.30024953046113839, 0.7585033920445543}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "safelyArgumentsMatch", "[Ljava.lang.Object;", new double[]{0.25327733207262509, 0.12900675175914036, 0.72636126217288544, 0.51087477187740371, 0.66163265129573756, -0.61116365894584956}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "hasSimilarMethod", "org.mockito.invocation.Invocation", new double[]{0.79367696683281352, -0.99369976831238582, -0.061978994107967769, -0.54903435929521083, 0.25393093846270959, 0.30325781717188682}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "hasSimilarMethod", "org.mockito.invocation.Invocation", new double[]{-0.46963283239284093, -0.60414120575010744, -0.23072361993500579, -0.61497722558604473, -1, 0.38923445281966829}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "isVariableArgument", "org.mockito.invocation.Invocation,int", new double[]{0.27430036163934651, 0.61656543028135391, -0.66927083313095648, -0.76499691609893472, 0.62695280847735346, -1, -0.7850396798941025, -0.53032789279071368, -0.51770017297130644}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "captureArgumentsFrom", "org.mockito.invocation.Invocation", new double[]{-0.11945373369540913, -1, 0.84216154557724043, 0.34612982744433296, -0.12689827943879683, 1}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getMethod", "", new double[]{0.91094667782099037, 0.38631901537418206, -0.15264315247666979}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getMethod", "", new double[]{0.24409916344278193, -0.26259864722400267, 0.21960046346932621}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getMethod", "", new double[]{0.38691521573118937, -0.28868213886604432, 0.94646078169459547}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "matches", "org.mockito.invocation.Invocation", new double[]{-0.013438361056442583, 0.62265619941478878, 0.46368873145022238, -0.14645008860682665, -0.4457054704993304, 0.57026775315135136}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getLocation", "", new double[]{0.30876686923876318, 0.50226849645402605, 1}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getMatchers", "", new double[]{-0.076309111079138403, 0.50344114010348762, -0.5602564091989175}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "hasSameMethod", "org.mockito.invocation.Invocation", new double[]{-0.081589038976355444, 1, -0.62873027838265294, -0.43094927799180516, 0.30808504267532127, 0.37566153673572722}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "captureArgumentsFrom", "org.mockito.invocation.Invocation", new double[]{0.33019395750088926, 0.22165268425359561, 0.96663312587707839, 0.25822363379045771, -0.23147675593915251, 0.011264109447592763}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getMethod", "", new double[]{0.25211354280774595, -0.32387748075596468, 0.25723022163523751}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:object-type:java.util.LinkedList", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "", "createFrom", "java.util.List", new double[]{0.11915919860343802, 1, 0.69475948093796103}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:object-type:java.util.LinkedList", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "", "createFrom", "java.util.List", new double[]{1, 0.51297058999341505, 1}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "hasSimilarMethod", "org.mockito.invocation.Invocation", new double[]{0.31957458311956932, 0.98404202280471853, 0.81250090349374027, -0.61274182007048572, 0.57348891442555194, -0.24885360800461676}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getMethod", "", new double[]{0.28195117111308787, -1, 0.78685761405918342}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getInvocation", "", new double[]{-0.33671124142984094, 0.077731084767339254, 0.54803258188328252}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "toString", "", new double[]{-0.84987678757292695, 0.12750687811647921, -0.70068923355268464}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getInvocation", "", new double[]{-0.17728795069094738, 0.50667693026953209, -0.8126497271599622}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getInvocation", "", new double[]{-0.62361652915791188, 1, 0.18755046855668048}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "isVariableArgument", "org.mockito.invocation.Invocation,int", new double[]{0.59751801573172103, 1, 0.41710747016056543, -0.68425122870596888, -0.31287476611993281, 0.76531378186778998, -0.38514885753774086, 0.35667589198995764, -0.015272899135596291}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "safelyArgumentsMatch", "[Ljava.lang.Object;", new double[]{0.49761738352858015, 1, 0.80748127895776678, 0.16739068412908992, -0.19571545686548797, -0.47212868698237048}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "toString", "", new double[]{0.49086536127234809, 0.86637580496794764, -0.16329269366237287}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getMatchers", "", new double[]{-0.16775497691206026, 1, 0.42240066846297347}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "captureArgumentsFrom", "org.mockito.invocation.Invocation", new double[]{-0.4620929206737005, 1, -0.18367381862146692, -0.69099930272569754, 0.093190479684698735, -0.24921002986322743}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.mockito.internal.invocation.InvocationMatcher", "org.mockito.invocation.Invocation", "getInvocation", "", new double[]{-0.15590850287729052, 0.40820397761353677, 0.59213240636860309}));
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
