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
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "ensureNotShared", "", new double[]{-0.086937624036138444, -0.19015235631540686, -0.95901584820932984}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsDouble", "", new double[]{0.063263881649785525, -0.11841903632738525, 0.044382125342745417}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "inLongRange", "[C,int,int,boolean", new double[]{-0.494475084655083, 0.0040459293084874875, 0.57588908140818373, -1, -0.70081578666644284, -0.50113769869357083, -0.46085807148078128, -0.0029363649767365388, 0.48769792318058458, 0.37512556639819078, 1, 0.96299361550790896}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Double:MC4w", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseDouble", "java.lang.String", new double[]{-0.92681512075952299, -0.16817690795811496, -0.1980481460954189}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "append", "[C,int,int", new double[]{0.37065590652482211, 1, -0.045888503638938767, -0.95092217036816717, -0.3995003944947873, -0.12795993751545498, -0.48168387187935235, -0.76936315969075586, -0.24964434344920541, -0.87251372910350555, 0.68511466207888938, -0.93762671828781829}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "size", "", new double[]{0.59795106071021442, 0.25735876399775598, 0.83215100037503487}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseLong", "[C,int,int", new double[]{0.4677090632944122, -0.37078033349470269, 0.66660148570462618, -0.23985059497177683, -0.47793331026552927, -0.46334439603861455, -1, -1, -0.38180558725340735}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "append", "char", new double[]{0.062979045034559852, -0.14100915120929239, -0.91397048212208643, -0.80059105517979157, 0.85280232423584723, -0.31312610907568278}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "clearSegments", "", new double[]{-0.25597727799187614, -0.44678064741143148, 0.21457686671457607}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("sha256:21b16aa34b01292aec53240d825fbf0e8e31f5af9b520f1503236766c28f25b7:bytes:195435", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "findBuffer", "int", new double[]{0.17292909312057625, -0.60222459698836828, 0.67926583082859771, 0.78172321722889715, 0.71693376794823249, -0.55589727153761115}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "expand", "int", new double[]{0.38638519770635621, -0.87597360166355032, 0.21681961560453919, 0.29925894523433499, -0.35783594604369945, 1}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{0.42246589907377896, -0.82401764314725734, -0.34109802727334854}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "toString", "", new double[]{0.57214960891186639, -0.47098395832963946, -0.88601188757129812}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{-0.15654517125577214, -0.28043973975238135, -0.36800324472631979}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "resetWithCopy", "[C,int,int", new double[]{0.3150947465057865, -0.33615318999170984, 0.18582230586138321, -0.13688212215514239, -0.26012522577197672, -0.45119895729227716, 0.070640227818371745, -0.6250505106365607, 1, 0.31199577040783649, 0.18610791754061098, 0.99766944501580312}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "ensureNotShared", "", new double[]{-0.58035251362902462, 1, -1}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "resetWithString", "java.lang.String", new double[]{-0.28501438891133613, 0.28317351200358198, -1, 0.73857216476230669, -0.0099952763800632283, 0.50964306996303665}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "expandCurrentSegment", "", new double[]{0.41137619227599875, -0.0026320259246256222, -0.83582720668133692}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("sha256:07214cb941481ce8103cb8f1cdbd0e244c0bf3720785d614e9bef14fd6d15177:bytes:46185", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "findBuffer", "int", new double[]{1, -0.0028959085634240045, 0.095971413697742647, 0.18468869753416078, 1, -0.27806103448815411}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "append", "char", new double[]{-0.73544267803651076, -0.24308748860350274, -0.16412043667465193, -0.78235912352842207, 0.11665981327538018, -0.74115487460297347}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "append", "[C,int,int", new double[]{0.22239449425553831, -0.82200428602083053, -0.84020660638733591, -0.68543348040503194, 0.26688551130064503, 0.12684324833011273, -0.017697538688869058, 0.056769335243467611, 0.78670591046837834, -0.1253098525030632, 0.15419142309939157, 0.12579156085834561}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.ArrayIndexOutOfBoundsException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "append", "[C,int,int", new double[]{0.39055276086162771, -1, 0.51959883892436642, -0.063088240513151678, 0.54559470932450949, 0.020273940370441113, 0.72353987538491127, -0.26035362902907855, 0.66429436493865368, -0.42137662439478318, -0.13257014147474894, 0.61631870584482262}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "clearSegments", "", new double[]{0.30296721623154549, -0.81049595377998651, -0.41307278802845349}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "unshare", "int", new double[]{0.51531914358970143, -0.93198240451998204, -0.47724162334444048, -0.55028696900317964, 0.32308577946208078, 0.9485548792580516}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "setCurrentLength", "int", new double[]{0.34387479496101153, -0.72492216859344927, 0.10542662787658474, -0.95245170768228116, 0.25786524257847215, -0.2248942902755561}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:null", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getTextBuffer", "", new double[]{0.91540296580464831, -1, -0.0068335543098244633}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "expandCurrentSegment", "", new double[]{-0.31610281231827275, 0.25883294431348991, -1}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.Long:LTQyMDg=", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseAsLong", "java.lang.String,long", new double[]{0.75384891265288889, 0.045557642125619968, -1, -0.42076664555193316, 0.56195448448521224, 0.35385138461525789}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsString", "", new double[]{-0.25613254209760183, -0.292505643422175, -0.68044677506171325}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "toString", "", new double[]{0.075404372305877154, -0.88942187801768735, 0.13838686651751209}));
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
