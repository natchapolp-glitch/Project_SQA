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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
public class GeneratedStudyTest {
  @Test(timeout=10000)
  public void generated1() {
    assertEquals("value:java.lang.String:QTExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "caverphone", "java.lang.String", new double[]{0.23193057415244711, -0.66062835522866559, 0.42501389475041518}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{0.88814461044913595, -0.65596618790424022, -0.85516073139501447}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "isMetaphoneEqual", "java.lang.String,java.lang.String", new double[]{-0.7141432374702672, -0.04058597189539892, -0.54017290457839962, -0.83869997796973594, -0.221690859265957, -0.69960152930389508}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.String:MTExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "caverphone", "java.lang.String", new double[]{-0.86503296044329869, -0.64627641637182376, 0.95607758160122835}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{0.68854248473627711, 0.76921808762751032, 0.48305714841374203}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{-0.78783167817176603, 0.49199560214962013, -0.35235253453300897}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "isCaverphoneEqual", "java.lang.String,java.lang.String", new double[]{0.70336891600686702, -0.45640722785777976, -0.75611810073626806, -0.79662989894852632, 0.86174145214727416, -0.16860095485665338}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{-0.91623173290158633, -0.85478111155718528, 0.45325581333020315}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.String:MTExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "caverphone", "java.lang.String", new double[]{-0.227942344627865, 0.98598953518564336, 0.47015101955580296}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Integer:NA==", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "getMaxCodeLen", "", new double[]{0.68173534036063987, -0.1095163882023682, -0.44036633229521849}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:java.lang.String:QUI=", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "metaphone", "java.lang.String", new double[]{0.46996776855570532, -0.11693151468753649, 0.52898654427194547}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.String:SExQ", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "encode", "java.lang.String", new double[]{0.34067005480877111, 0.84669691888551979, -0.87967828237632606}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.String:QUI=", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "metaphone", "java.lang.String", new double[]{0.28266279608802569, 0.91462740605526416, -0.95638101228044303}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "metaphone", "java.lang.String", new double[]{-0.16056745240653592, 0.12616159163943497, 0.59587659334154042}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "metaphone", "java.lang.String", new double[]{0.0092172294212968797, -0.50311612643705073, -0.81886371326008356}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.String:SzExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "encode", "java.lang.String", new double[]{-0.33043767265812551, -0.20004019479939705, -0.47464798133811437}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:java.lang.String:Tkw=", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "metaphone", "java.lang.String", new double[]{-0.75117867380225323, 0.23932492920947523, -0.98162668565096611}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "isCaverphoneEqual", "java.lang.String,java.lang.String", new double[]{0.43068645714242781, 0.92797919315693744, 0.96471417321610153, 0.98418388304230175, 0.6226997481261709, 0.87587779951719358}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.String:SExQ", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "encode", "java.lang.String", new double[]{0.32127327102820469, 0.14634352145338814, 0.47159508252222571}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("void", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "setMaxCodeLen", "int", new double[]{0.82888908089725089, 0.2745462953689406, -0.63592177126837024}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "isCaverphoneEqual", "java.lang.String,java.lang.String", new double[]{0.82195856222636232, 0.18737527939245946, 0.84347297124207743, -0.47878041440810803, -0.9568089914418163, -0.9016146555885618}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "isMetaphoneEqual", "java.lang.String,java.lang.String", new double[]{-0.61536392606666657, -0.90992490673392434, -0.94146017177705099, -0.78320536472359104, 0.86912259616629162, -0.79208051162687076}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.String:VFI=", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "metaphone", "java.lang.String", new double[]{-0.69402411927237773, -0.88325858049011718, 0.41464721791581161}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.String:MTExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "caverphone", "java.lang.String", new double[]{0.98339574368981175, 0.89439381858189471, -0.10877341436485}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Integer:NA==", SqaProbe.observe("org.apache.commons.codec.language.Metaphone", "", "getMaxCodeLen", "", new double[]{-0.40148957695470111, 0.44544117752994405, -0.052826050154470838}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "isCaverphoneEqual", "java.lang.String,java.lang.String", new double[]{-0.35931703016113037, -0.86665409161031315, -0.29468425327664005, 0.69195583443969522, 0.61265593851186217, 0.7037885390261005}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "isCaverphoneEqual", "java.lang.String,java.lang.String", new double[]{-0.55232274615247379, -0.61150220663624633, -0.12354201357029937, -0.9479693912634457, 0.7212439680076983, 0.77639204646554094}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.String:MTExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "caverphone", "java.lang.String", new double[]{0.82357446967267567, 0.9835833034315411, -0.53798595271913219}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.String:MTExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "encode", "java.lang.String", new double[]{0.81629654407276986, 0.91676847065119227, 0.96235485793252673}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.String:TkExMTExMTExMQ==", SqaProbe.observe("org.apache.commons.codec.language.Caverphone", "", "caverphone", "java.lang.String", new double[]{-0.72298791879065027, 0.58142199578510256, -0.3007930989691725}));
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
