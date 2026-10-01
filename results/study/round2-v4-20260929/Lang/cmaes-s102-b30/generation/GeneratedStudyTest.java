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
    assertEquals("value:java.lang.Long:NjY1OQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "long,long,long", new double[]{0.66590947354708863, 0.45803966823341336, 0.82928909475281765, -0.46043473069732355, -0.37045971698347407, -0.57359663273266814, -0.52191718761276218, 0.71039957628821593, 0.19781276765747477}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[D", new double[]{-0.49621752434238758, 0.71086565841001481, -0.67208203741790584}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Byte:LTExOQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[B", new double[]{0.25327733207262509, 0.12900675175914036, 0.72636126217288544}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Double:LTU0OS4wMzQzNTkyOTUyMTA4", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "double,double,double", new double[]{0.79367696683281352, -0.99369976831238582, -0.061978994107967769, -0.54903435929521083, 0.25393093846270959, 0.30325781717188682, -0.19900075226792249, 0.21174665565325093, 0.77138117155032226}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Double:Mi4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[D", new double[]{-0.46963283239284093, -0.60414120575010744, -0.23072361993500579}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Byte:MzA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "byte,byte,byte", new double[]{0.27430036163934651, 0.61656543028135391, -0.66927083313095648, -0.76499691609893472, 0.62695280847735346, -1, -0.7850396798941025, -0.53032789279071368, -0.51770017297130644}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Long:MjU2", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String,long", new double[]{-0.11945373369540913, -1, 0.84216154557724043, 0.34612982744433296, -0.12689827943879683, 1}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.Integer:OTEwOQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "int,int,int", new double[]{0.91094667782099037, 0.38631901537418206, -0.15264315247666979, -0.83746341027498661, 0.28477708035222149, 0.11327712920975604, 0.61074896830476733, 0.42739463154248336, 0.53642830541779363}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Integer:MjU1", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{0.24409916344278193, -0.26259864722400267, 0.21960046346932621, 0.25158306241119055, 0.23287827229615449, -0.45617548513274025, 0.25944148662558958, -0.084362368790579637, -0.61492945583721614}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Integer:MzI3Njc=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[I", new double[]{0.38691521573118937, -0.28868213886604432, 0.94646078169459547}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:java.lang.Float:LTEwLjA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "float,float,float", new double[]{-0.031008832025471111, 0.58983944627637797, 0.47413904367153931, -0.14312091810317107, -0.48490487855504877, 0.60103925188245055, -1, 0.75823834570183346, -0.2802732339681126}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createLong", "java.lang.String", new double[]{0.29004108036645809, 0.47086052022383551, 1}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String", new double[]{-0.093130840309010032, 0.47205457046653188, -0.54474993604671385}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.Double:LTk5LjAxMjc2NTY2MjIxNzY3", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "double,double,double", new double[]{-0.099012765662217672, 1, -0.61216748341280125, -0.4239583858244742, 0.26359424024129713, 0.41089911686343933, -0.41888612397971969, 0.21107649163374201, 0.25430086770317245}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:java.lang.Long:MzExNg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[J", new double[]{0.31159591612864168, 0.19228020838146384, 0.97390404322298862}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.Integer:MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{0.2334300262406197, -0.35081577268369679, 0.26823405233568337, -0.74951511828902129, -0.33065122674192393, -0.28264234651776143, 0.95378577157634792, 0.81203291810735423, -0.56961274343119772}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:java.lang.Long:NjExNw==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[J", new double[]{0.10166185750978143, 1, 0.70378954974781394}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:java.lang.Long:MTAwMDA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[J", new double[]{1, 0.48071384511094889, 1}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.Double:MzAwLjA5MjY3NjY3MzE5MDk=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[D", new double[]{0.30009267667319089, 0.94863719411451763, 0.82142584964730347}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.Integer:LTQ2OTM=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{0.26308255994397639, -1, 0.79469763276355132, 0.53032861369127315, -1, -1, -0.46932773829607677, 0.81404262901800373, 0.25364154624502239}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createDouble", "java.lang.String", new double[]{-0.35224818321476054, 0.0524729515321396, 0.55589515852352145}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Float:MS4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toFloat", "java.lang.String,float", new double[]{-0.86318333993849339, 0.10211998687700213, -0.68673664062056483, 0.14010440203259861, -1, 0.24611206422106013}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.Float:MS4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.19359195915322969, 0.4786974895813525, -0.79832380793075841}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.63771480191733487, 1, 0.1972411971757127}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Byte:LTE=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "byte,byte,byte", new double[]{0.57646925565543328, 1, 0.42606763774609357, -0.6756251951701413, -0.34501530093781346, 0.79907873737966884, -0.38411247419210942, 0.34224338178522201, 0.0069222661513080874}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Byte:LTExMg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[B", new double[]{0.47717261587885429, 1, 0.81455012238441526}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.lang.Float:NDcwLjg5Njg4", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[F", new double[]{0.47089686672144893, 0.8346660154299681, -0.15186899963465655}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String", new double[]{-0.18422863370685041, 1, 0.43097392284502678}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Long:LTY4NDY=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String,long", new double[]{-0.47655204822774566, 1, -0.17248772640146065, -0.68458836052597194, 0.06149000796634263, -0.21363050781255119}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Float:MTAwMC4w", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.17232539177492756, 0.38076299732015872, 0.59979434978153101}));
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
