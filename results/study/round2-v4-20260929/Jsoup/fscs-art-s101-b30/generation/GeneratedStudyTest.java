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
    assertEquals("value:object-type:org.jsoup.nodes.Element", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "createElement", "java.lang.String", new double[]{-0.61049100893172659, 0.93050221412222234, 0.84795280335358858, -0.065722643606052067, 0.3269412890601211, -0.57095406052406394}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "text", "java.lang.String", new double[]{0.36691307832350173, -0.98810455272076059, -0.83187732002215831, -0.93601413703448788, -0.30846918323353778, 0.89219684455009607}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("void", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "normalise", "org.jsoup.nodes.Element", new double[]{0.98563798282468573, 0.47986361203875871, -0.12247712256732957, 0.33332936542666958, -0.46202788423382879, 0.42076212472413577}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("constructed:org.jsoup.nodes.Document", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "<init>", "", new double[]{0.54643037766004943, -0.069681495804074123, 0.96712138680300752}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "title", "java.lang.String", new double[]{-0.88893280247898177, 0.34329033347695903, -0.81704516445929531, 0.32252994211084451, -0.25737973328817132, 0.70893785313594382}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "outerHtml", "", new double[]{-0.7271074388895844, -0.11460872377816744, -0.87206661122150786}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:null", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "body", "", new double[]{0.40271440023471206, -0.92232175624706847, 0.27658601623459678}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "title", "", new double[]{0.094441540085618447, 0.54472726175783026, -0.85199491055693621}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("constructed:org.jsoup.nodes.Document", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "<init>", "", new double[]{-0.073436437908427132, 0.77902634680737881, 0.72362024014974979}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:object-type:org.jsoup.nodes.Document", SqaProbe.observe("org.jsoup.nodes.Document", "", "createShell", "java.lang.String", new double[]{0.69110682663567591, -0.7585478021062293, -0.86328557024709207}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("constructed:org.jsoup.nodes.Document", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "<init>", "", new double[]{0.73196404244994051, 0.017020411271082558, 0.61766063961286277}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "title", "java.lang.String", new double[]{0.24640617566158651, -0.90716706590137108, -0.58786880977873035, 0.22120297208654516, -0.76913764025865761, -0.68201901704743073}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.String:I2RvY3VtZW50", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "nodeName", "", new double[]{0.27560753499374746, 0.781631948480773, -0.56044664216281936}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:object-type:org.jsoup.nodes.Document", SqaProbe.observe("org.jsoup.nodes.Document", "", "createShell", "java.lang.String", new double[]{0.52314451927353045, 0.86173976552067377, -0.86959109861480055}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:null", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "head", "", new double[]{-0.54277628171702053, -0.3579365757338413, 0.75088100588311724}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("void", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "normalise", "org.jsoup.nodes.Element", new double[]{-0.89359722826003773, -0.43532870851728633, 0.5969027960228479, -0.32631590357417939, -0.90077106845470478, -0.063774945762343993}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "title", "", new double[]{0.47078276976705014, 0.35185862502550536, -0.80094381672866932}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:object-type:org.jsoup.nodes.Document", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "normalise", "", new double[]{-0.060609282908926643, -0.66932745406121796, -0.42105139796449342}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("constructed:org.jsoup.nodes.Document", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "<init>", "", new double[]{0.28884721544337544, -0.90554600453063872, 0.51461208747364329}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "head", "", new double[]{-0.9955040773222692, -0.73503398217727312, -0.25348735241023812}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:null", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "body", "", new double[]{-0.42033691060444989, 0.89010952911846242, 0.31049757014401669}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("void", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "normalise", "org.jsoup.nodes.Element", new double[]{-0.87668155963824002, 0.11129078999973951, 0.91590166619267044, 0.9251499460943966, 0.12621426287842796, -0.69601345272315784}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:object-type:org.jsoup.nodes.Document", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "normalise", "", new double[]{0.10375045184312093, 0.40595509381727668, 0.82591438072872525}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "title", "", new double[]{0.28619570014315765, 0.63706365305959833, 0.42922867378839125}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("void", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "normalise", "org.jsoup.nodes.Element", new double[]{-0.53878195233106152, -0.049897763263856643, -0.18410250597385636, 0.26976382228707729, 0.62164302747684896, -0.10697675246688965}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("constructed:org.jsoup.nodes.Document", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "<init>", "", new double[]{-0.67246918955994794, -0.48516532358778974, 0.88660141125453684}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:null", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "body", "", new double[]{-0.93083678866422348, 0.93777338039779545, -0.85184469434577337}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("void", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "normalise", "org.jsoup.nodes.Element", new double[]{-0.62766923526819673, 0.72955351145814085, -0.91989587545136242, 0.3984078592352549, -0.68750887997675658, 0.76932570695046709}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "text", "java.lang.String", new double[]{0.31364112845801695, 0.40780747708514964, -0.6741476628796137, -0.16638659524546839, -0.66311086585757417, 0.90523656208351611}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("org.jsoup.nodes.Document", "java.lang.String", "title", "java.lang.String", new double[]{-0.8893122586235469, 0.86495389309993786, 0.59756858688973602, 0.14146588119764103, -0.99202670264631143, -0.26394585707598806}));
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
