import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import java.util.LinkedHashMap;
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
  private static final java.util.concurrent.atomic.AtomicInteger EXECUTED = new java.util.concurrent.atomic.AtomicInteger();
  private static final java.util.concurrent.atomic.AtomicInteger TARGET_CHECKS = new java.util.concurrent.atomic.AtomicInteger();
  @org.junit.AfterClass public static void retainStageCounts() throws Exception {
    String report = "{\"schema_version\":1,\"executed\":" + EXECUTED.get() + ",\"skipped\":0,\"target_checks\":" + TARGET_CHECKS.get() + "}\n";
    Files.write(Paths.get("sqa-stage-counts.json"), report.getBytes(StandardCharsets.UTF_8));
  }
  @Test(timeout=10000)
  public void generated3() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observeWithPolicy("org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator", "int,double,double,[D,[D", "<init>", "", new double[]{-0.40707862171624226, 0.15945578831224816, -0.25630375578480008, -0.28437430425406618, -0.99571928534083087, 0.38693633886282797, -0.99078136129558703, 0.23950509416686216, 0.29068450657000516, 0.25956817639741148, -0.94741527720343555, 0.50915387608824758, -0.4081749462454165, 0.25276695804981925, -0.21025390616344158}, "aom-beam-champ-graphics-fixtures-v12-development"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated17() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("exception:org.apache.commons.math3.exception.NoDataException", SqaProbe.observeWithPolicy("org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator", "int,double,double,[D,[D", "<init>", "", new double[]{-1, -0.8240141409145908, 0.24567644488134391, 0.0059585591610412658, -0.80242252903242739, 1, -0.35269222987786464, 0.46859858216362127, 1, -0.11665007681943301, -0.83331125234080849, 0.069002649198488017, -0.53125885094091596, 0.86530708229110553, -0.70468982105409772}, "aom-beam-champ-graphics-fixtures-v12-development"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated24() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observeWithPolicy("org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator", "int,double,double,[D,[D", "<init>", "", new double[]{-0.20133164654229807, 0.42783763077777071, 0.4789181506416203, -0.11829167009981714, -1, -0.57234642507076372, -0.17423426606236889, -1, 0.37231203172710947, 1, -0.3418744205539721, 0.66454159978734162, -1, -0.08554548225037284, -0.12381559302296495}, "aom-beam-champ-graphics-fixtures-v12-development"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated29() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observeWithPolicy("org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator", "int,double,double,[D,[D", "<init>", "", new double[]{-0.20685215091387826, 0.33271771900996877, 0.75747462722039005, 0.10879009015905747, -1, 0.33281647942182857, -0.68235508367267939, 0.050824944980345545, 0.41966586706207631, 1, -0.40678920473376284, 0.47978967571622116, -1, 0.96471253667860413, -1}, "aom-beam-champ-graphics-fixtures-v12-development"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated30() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.String:QWRhbXMtQmFzaGZvcnRo|state=stateless-scalars", SqaProbe.observeWithPolicy("org.apache.commons.math3.ode.nonstiff.AdamsBashforthIntegrator", "int,double,double,double,double", "getName", "", new double[]{-0.49232046773795907, -0.088200198314144523, 0.25149415307173728, -1, -1, -0.92886703103526957, -1, -0.66502630610702218, 0.54907639133402386, 0.75163545540647714, 1, 0.070687696281527684, 0.10434115703638913, 1, -0.53247791646757059}, "aom-beam-champ-graphics-fixtures-v12-development"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
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

    /** Schema scaffolding carried in the suite; no benchmark test classes. */
    public static class GenericFixture<T> { public T value; public T[] array; public List<T> items; }
    public static class StringBinding extends GenericFixture<String> { }
    public static class IntegerBinding extends GenericFixture<Integer> { }
    public static class FixtureBean { public String value = "fixture-value"; }
    public interface FixtureMock { String accept(String value); }

    public static final String EXPLICIT_FIXTURES = "beam-explicit-fixtures-v3-proposal";
    public static final String SCALAR_FIXTURES = "beam-explicit-fixtures-v4-proposal";
    public static final String PILOT_FIXTURES = "beam-explicit-fixtures-v5-proposal";
    public static final String BUFFER_FIXTURES = "beam-explicit-fixtures-v6-buffer-proposal";
    public static final String FRACTION_FIELD_FIXTURES = "aom-beam-fraction-field-v6-development";
    public static final String LANG_HELPER_FIXTURES = "beam-explicit-fixtures-v9-buffer-lang-development";
    public static final String JOINT_FIXTURES = "aom-beam-champ-joint-fixtures-v10-development";
    public static final String CODEC_FIXTURES = "aom-beam-champ-codec-fixtures-v13-development";
    public static final String GRAPHICS_FIXTURES = "aom-beam-champ-graphics-fixtures-v12-development";
    public static final String CHRONOLOGY_FIXTURES = "aom-beam-champ-chronology-fixtures-v11-development";
    // Diagnostic scope only, serialized by observeChronology. Empty during all
    // receiver setup/projection calls, so JDI cannot count setup as target entry.
    public static String chronologyActiveCase = "";
    private static final ThreadLocal<FixtureSession> FIXTURES = new ThreadLocal<FixtureSession>();
    private static final ThreadLocal<Boolean> INVOKED = new ThreadLocal<Boolean>();

    /** A setup failure is never an observation of an uncalled target method. */
    private static final class FixtureFailure extends RuntimeException {
        FixtureFailure(String message, Throwable cause) { super(message, cause); }
    }

    // Production factories only: no dataset test classes, patches or buggy results.
    // Reflection keeps the helper compilable without project-specific dependencies.
    private static Object call(Object receiver, String name, Class<?>[] parameterTypes, Object... values)
            throws ReflectiveOperationException {
        Class<?> declaring = receiver instanceof Class ? (Class<?>)receiver : receiver.getClass();
        while (declaring != null) {
            try {
                Method method = declaring.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return method.invoke(receiver instanceof Class ? null : receiver, values);
            } catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }
        }
        throw new NoSuchMethodException(name);
    }

    private static Object construct(String name, Class<?>[] parameterTypes, Object... values)
            throws ReflectiveOperationException {
        Constructor<?> ctor = Class.forName(name).getDeclaredConstructor(parameterTypes);
        ctor.setAccessible(true);
        return ctor.newInstance(values);
    }

    private static final class FixtureSession {
        final String targetClass;
        final String method;
        final boolean pilot;
        final boolean bufferSlices;
        char[] outputBuffer;
        final boolean fractionField;
        final boolean langHelpers;
        final boolean reviewed;
        Object validationInput;
        boolean constructing;
        Object compiler, registry, scope, cfg, reverse, flow, closureNode, receiver;
        org.w3c.dom.Element domRoot;
        org.w3c.dom.Node domChild;
        Object jdomRoot, jdomChild;
        java.io.ByteArrayOutputStream archiveBytes;
        Object mapper, parser, context, collectionType, collectionDeserializer;
        Object mock, baseInvocation, actualInvocation;
        Object chartDataset, chartPlot, chartAxis, cleanupScript, cleanupExterns;
        int cleanupNodeIndex;

        @SuppressWarnings({"unchecked", "rawtypes"})
        void unusedClosure(double a) throws ReflectiveOperationException {
            if (compiler != null) return;
            Class<?> node = Class.forName("com.google.javascript.rhino.Node");
            Class<?> ac = Class.forName("com.google.javascript.jscomp.AbstractCompiler");
            compiler = construct("com.google.javascript.jscomp.Compiler", new Class<?>[]{});
            Object options = construct("com.google.javascript.jscomp.CompilerOptions", new Class<?>[]{});
            call(compiler, "initOptions", new Class<?>[]{options.getClass()}, options);
            cleanupExterns = call(compiler, "parseTestCode", new Class<?>[]{String.class}, "");
            cleanupScript = call(compiler, "parseTestCode", new Class<?>[]{String.class},
                "var unused = 1; function fixture(x) { var local = " + (a < 0 ? "2" : "3") + "; return x; } fixture(1);");
            // Normalize traverses sibling roots and requires their common parent.
            int block = Class.forName("com.google.javascript.rhino.Token").getField("BLOCK").getInt(null);
            Object roots = construct(node.getName(), new Class<?>[]{int.class}, block);
            call(roots, "addChildToBack", new Class<?>[]{node}, cleanupExterns);
            call(roots, "addChildToBack", new Class<?>[]{node}, cleanupScript);
            Object normalize = construct("com.google.javascript.jscomp.Normalize", new Class<?>[]{ac, boolean.class}, compiler, false);
            call(normalize, "process", new Class<?>[]{node, node}, cleanupExterns, cleanupScript);
            Class<?> lifecycle = Class.forName("com.google.javascript.jscomp.AbstractCompiler$LifeCycleStage");
            call(compiler, "setLifeCycleStage", new Class<?>[]{lifecycle}, Enum.valueOf((Class)lifecycle, "NORMALIZED"));
            closureNode = cleanupScript;
        }

        void chart(double a) throws ReflectiveOperationException {
            if (chartDataset != null) return;
            Class<?> dataset = Class.forName("org.jfree.data.category.CategoryDataset");
            Class<?> axis = Class.forName("org.jfree.chart.axis.CategoryAxis");
            Class<?> valueAxis = Class.forName("org.jfree.chart.axis.ValueAxis");
            Class<?> renderer = Class.forName("org.jfree.chart.renderer.category.CategoryItemRenderer");
            chartDataset = construct("org.jfree.data.category.DefaultCategoryDataset", new Class<?>[]{});
            call(chartDataset, "addValue", new Class<?>[]{double.class, Comparable.class, Comparable.class}, a < 0 ? -2.0 : 2.0, "row-a", "column-a");
            call(chartDataset, "addValue", new Class<?>[]{double.class, Comparable.class, Comparable.class}, 5.0, "row-b", "column-a");
            chartAxis = construct(axis.getName(), new Class<?>[]{String.class}, "Domain");
            Object rangeAxis = construct("org.jfree.chart.axis.NumberAxis", new Class<?>[]{String.class}, "Range");
            chartPlot = construct("org.jfree.chart.plot.CategoryPlot", new Class<?>[]{dataset, axis, valueAxis, renderer},
                chartDataset, chartAxis, rangeAxis, receiver);
            java.awt.Graphics2D graphics = new java.awt.image.BufferedImage(16,16,java.awt.image.BufferedImage.TYPE_INT_RGB).createGraphics();
            try {
                call(receiver, "initialise", new Class<?>[]{java.awt.Graphics2D.class, java.awt.geom.Rectangle2D.class,
                    chartPlot.getClass(), dataset, Class.forName("org.jfree.chart.plot.PlotRenderingInfo")},
                    graphics, new java.awt.geom.Rectangle2D.Double(0,0,16,16), chartPlot, chartDataset, null);
            } finally { graphics.dispose(); }
        }

        Object beanWriter() throws ReflectiveOperationException {
            Object objectMapper = construct("com.fasterxml.jackson.databind.ObjectMapper", new Class<?>[]{});
            Object provider = call(objectMapper, "getSerializerProvider", new Class<?>[]{});
            provider = call(provider, "createInstance", new Class<?>[]{Class.forName("com.fasterxml.jackson.databind.SerializationConfig"),
                Class.forName("com.fasterxml.jackson.databind.ser.SerializerFactory")},
                call(objectMapper, "getSerializationConfig", new Class<?>[]{}), call(objectMapper, "getSerializerFactory", new Class<?>[]{}));
            Object serializer = call(provider, "findValueSerializer", new Class<?>[]{Class.class, Class.forName("com.fasterxml.jackson.databind.BeanProperty")}, FixtureBean.class, null);
            return Array.get(field(serializer, "_props"), 0);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        void jacksonCollection(double a) throws ReflectiveOperationException {
            if (mapper != null) return;
            mapper = construct("com.fasterxml.jackson.databind.ObjectMapper", new Class<?>[]{});
            Class<?> feature = Class.forName("com.fasterxml.jackson.databind.DeserializationFeature");
            call(mapper, "configure", new Class<?>[]{feature, boolean.class}, Enum.valueOf((Class)feature, "ACCEPT_SINGLE_VALUE_AS_ARRAY"), true);
            Object typeFactory = call(mapper, "getTypeFactory", new Class<?>[]{});
            collectionType = call(typeFactory, "constructCollectionType", new Class<?>[]{Class.class, Class.class}, java.util.ArrayList.class, String.class);
            Object factory = call(mapper, "getFactory", new Class<?>[]{});
            String input = method.equals("handleNonArray") ? a < 0 ? "\"alpha\"" : "\"beta\""
                : a < 0 ? "[\"alpha\",\"beta\"]" : "[\"left\",\"right\"]";
            parser = call(factory, "createParser", new Class<?>[]{String.class}, input);
            call(parser, "nextToken", new Class<?>[]{});
            Object blueprint = call(mapper, "getDeserializationContext", new Class<?>[]{});
            context = call(blueprint, "createInstance", new Class<?>[]{Class.forName("com.fasterxml.jackson.databind.DeserializationConfig"),
                Class.forName("com.fasterxml.jackson.core.JsonParser"), Class.forName("com.fasterxml.jackson.databind.InjectableValues")},
                call(mapper, "getDeserializationConfig", new Class<?>[]{}), parser, null);
            collectionDeserializer = call(context, "findRootValueDeserializer", new Class<?>[]{Class.forName("com.fasterxml.jackson.databind.JavaType")}, collectionType);
        }

        void mockito(double a) throws ReflectiveOperationException {
            if (mock != null) return;
            mock = call(Class.forName("org.mockito.Mockito"), "mock", new Class<?>[]{Class.class}, FixtureMock.class);
            call(mock, "accept", new Class<?>[]{String.class}, "alpha");
            call(mock, "accept", new Class<?>[]{String.class}, a < 0 ? "alpha" : "beta");
            Object util = construct("org.mockito.internal.util.MockUtil", new Class<?>[]{});
            Object handler = call(util, "getMockHandler", new Class<?>[]{Object.class}, mock);
            Object container = call(handler, "getInvocationContainer", new Class<?>[]{});
            List<?> invocations = (List<?>)call(container, "getInvocations", new Class<?>[]{});
            baseInvocation = invocations.get(0);
            actualInvocation = invocations.get(1);
        }

        FixtureSession(String targetClass, String method, String policy) {
            this.targetClass = targetClass;
            this.method = method;
            this.reviewed = JOINT_FIXTURES.equals(policy) || CHRONOLOGY_FIXTURES.equals(policy) || GRAPHICS_FIXTURES.equals(policy) || CODEC_FIXTURES.equals(policy);
            this.langHelpers = LANG_HELPER_FIXTURES.equals(policy) || reviewed;
            this.bufferSlices = BUFFER_FIXTURES.equals(policy) || langHelpers;
            this.fractionField = FRACTION_FIELD_FIXTURES.equals(policy) || langHelpers;
            this.pilot = PILOT_FIXTURES.equals(policy) || bufferSlices || fractionField;
        }

        Object[] langHelperArguments(Class<?>[] types, double[] vector) {
            if (!langHelpers || constructing || !targetClass.equals("org.apache.commons.lang3.math.NumberUtils")
                    || types.length != 1) return null;
            double a = vector[0];
            if (method.equals("isAllZeros") && types[0] == String.class)
                return new Object[]{new String[]{null, "", "0", "000", "001", "12", "00 0", "-0"}[bucket(a, 8)]};
            if (method.equals("validateArray") && types[0] == Object.class) {
                Object[] arrays = {null, new int[0], new int[]{0}, new int[]{-1, 0, 7}};
                validationInput = arrays[bucket(a, arrays.length)];
                return new Object[]{validationInput};
            }
            return null;
        }

        Object[] boundedBufferArguments(Class<?>[] types, double[] vector) {
            if (!bufferSlices || constructing || types.length == 0) return null;
            double a = vector[0], b = vector[1 % vector.length];
            if (targetClass.equals("com.fasterxml.jackson.core.io.NumberInput") && types[0] == char[].class) {
                String text;
                if (method.equals("parseLong"))
                    text = new String[]{"1000000000", "1234567890123", "123456789012345678"}[bucket(a, 3)];
                else if (method.equals("parseInt"))
                    text = new String[]{"0", "7", "12345", "999999999"}[bucket(a, 4)];
                else if (method.equals("inLongRange"))
                    text = new String[]{"0", "9223372036854775807", "9223372036854775808", "9223372036854775809"}[bucket(a, 4)];
                else if (method.equals("parseBigDecimal"))
                    text = new String[]{"0", "12.50", "-0.125"}[bucket(a, 3)];
                else return null;
                if (types.length == 1) return new Object[]{text.toCharArray()};
                char[] chars = ("##" + text + "?").toCharArray();
                if (types.length == 4) return new Object[]{chars, 2, text.length(), b < 0};
                return new Object[]{chars, 2, text.length()};
            }
            if (targetClass.equals("com.fasterxml.jackson.core.util.TextBuffer") && method.equals("append")
                    && types.length == 3 && (types[0] == char[].class || types[0] == String.class)) {
                String text = a < 0 ? "xABCDy" : "p12345q";
                int offset = a < 0 ? 1 : 2;
                int length = 1 + bucket(b, text.length() - offset - 1);
                return new Object[]{types[0] == char[].class ? text.toCharArray() : text, offset, length};
            }
            if (targetClass.equals("org.apache.commons.csv.ExtendedBufferedReader") && method.equals("read")
                    && types.length == 3 && types[0] == char[].class) {
                outputBuffer = new char[8];
                Arrays.fill(outputBuffer, '~');
                int offset = a < 0 ? 1 : 2;
                int length = 1 + bucket(b, outputBuffer.length - offset - 1);
                return new Object[]{outputBuffer, offset, length};
            }
            return null;
        }

        Object option(String name, String text) throws ReflectiveOperationException {
            Object option = construct("org.apache.commons.cli.Option",
                    new Class<?>[]{String.class, boolean.class, String.class}, name, true, "fixture");
            call(option, "setType", new Class<?>[]{Object.class}, String.class);
            call(option, "addValue", new Class<?>[]{String.class}, text);
            return option;
        }

        Object archiveEntry(String name, long size) throws ReflectiveOperationException {
            Object entry = construct("org.apache.commons.compress.archivers.cpio.CpioArchiveEntry",
                    new Class<?>[]{String.class}, name);
            call(entry, "setSize", new Class<?>[]{long.class}, size);
            call(entry, "setTime", new Class<?>[]{long.class}, 0L);
            call(entry, "setMode", new Class<?>[]{long.class}, 0100644L);
            return entry;
        }

        Object prepareReceiver(Object value, double a) throws ReflectiveOperationException {
            if (!pilot) return value;
            if (targetClass.equals("org.apache.commons.cli.CommandLine")) {
                call(value, "addOption", new Class<?>[]{Class.forName("org.apache.commons.cli.Option")}, option("x", a < 0 ? "alpha" : "beta"));
                call(value, "addArg", new Class<?>[]{String.class}, "positional");
            } else if (targetClass.equals("com.fasterxml.jackson.core.util.TextBuffer")) {
                char[] content = (a < 0 ? "123" : "45.5").toCharArray();
                call(value, "resetWithCopy", new Class<?>[]{char[].class, int.class, int.class}, content, 0, content.length);
            } else if (targetClass.equals("org.jsoup.nodes.Document")) {
                Object html = call(value, "appendElement", new Class<?>[]{String.class}, "html");
                call(html, "appendElement", new Class<?>[]{String.class}, "head");
                Object body = call(html, "appendElement", new Class<?>[]{String.class}, "body");
                call(body, "text", new Class<?>[]{String.class}, a < 0 ? "alpha" : "beta");
                call(value, "title", new Class<?>[]{String.class}, "Fixture");
            } else if (targetClass.endsWith("CpioArchiveOutputStream")) {
                call(value, "putNextEntry", new Class<?>[]{Class.forName("org.apache.commons.compress.archivers.cpio.CpioArchiveEntry")},
                        archiveEntry("fixture.txt", method.equals("write") ? 1 : 0));
            } else if (targetClass.equals("org.joda.time.Partial")) {
                return call(value, "with", new Class<?>[]{Class.forName("org.joda.time.DateTimeFieldType"), int.class},
                        call(Class.forName("org.joda.time.DateTimeFieldType"), "hourOfDay", new Class<?>[]{}), 10);
            } else if (targetClass.equals("org.jfree.chart.renderer.category.AreaRenderer")) {
                receiver = value;
                chart(a);
            } else if (targetClass.equals("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser")) {
                // Real StAX input; getters start on a named leaf VALUE_STRING.
                for (int i = 0; i < 8; i++) {
                    Object token = call(value, "nextToken", new Class<?>[]{});
                    if (token != null && token.toString().equals("VALUE_STRING")) break;
                }
            }
            return value;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        Object nativeType(String name, boolean object) throws ReflectiveOperationException {
            Class<?> nativeClass = Class.forName("com.google.javascript.rhino.jstype.JSTypeNative");
            Object key = Enum.valueOf((Class)nativeClass, name);
            return call(registry, object ? "getNativeObjectType" : "getNativeType", new Class<?>[]{nativeClass}, key);
        }

        void closure(double a) throws ReflectiveOperationException {
            if (compiler != null) return;
            Class<?> node = Class.forName("com.google.javascript.rhino.Node");
            Class<?> scopeClass = Class.forName("com.google.javascript.jscomp.Scope");
            Class<?> abstractCompiler = Class.forName("com.google.javascript.jscomp.AbstractCompiler");
            compiler = construct("com.google.javascript.jscomp.Compiler", new Class<?>[]{});
            Object options = construct("com.google.javascript.jscomp.CompilerOptions", new Class<?>[]{});
            call(compiler, "initOptions", new Class<?>[]{options.getClass()}, options);
            registry = call(compiler, "getTypeRegistry", new Class<?>[]{});
            String expression = a < 0 ? "x + 1" : "x + 's'";
            if (method.contains("And") || method.contains("ShortCircuit")) expression = "x && true";
            if (method.contains("Or")) expression = "x || false";
            if (method.equals("traverseArrayLiteral")) expression = "[x, 1]";
            if (method.equals("traverseObjectLiteral")) expression = "({p:x})";
            if (method.equals("traverseHook")) expression = "x ? 1 : 2";
            if (method.equals("traverseAssign")) expression = "x = 2";
            if (method.equals("traverseGetElem")) expression = "x['p']";
            if (method.equals("traverseGetProp") || method.contains("Property")) expression = "x.p";
            if (method.equals("traverseName") || method.equals("redeclareSimpleVar")
                    || method.equals("narrowScope") || method.equals("updateScopeForTypeChange")) expression = "x";
            Object script = call(compiler, "parseTestCode", new Class<?>[]{String.class},
                    "function fixture(x) { return " + expression + "; }");
            Object function = call(script, "getFirstChild", new Class<?>[]{});
            Object global = call(scopeClass, "createGlobalScope", new Class<?>[]{node}, script);
            scope = construct(scopeClass.getName(), new Class<?>[]{scopeClass, node}, global, function);
            Object astParameters = call(call(function, "getFirstChild", new Class<?>[]{}), "getNext", new Class<?>[]{});
            Object name = call(astParameters, "getFirstChild", new Class<?>[]{});
            call(scope, "declare", new Class<?>[]{String.class, node,
                    Class.forName("com.google.javascript.rhino.jstype.JSType"),
                    Class.forName("com.google.javascript.jscomp.CompilerInput")}, "x", name, nativeType("UNKNOWN_TYPE", false), null);
            Object body = call(function, "getLastChild", new Class<?>[]{});
            Object returnNode = call(body, "getFirstChild", new Class<?>[]{});
            closureNode = method.equals("traverseReturn") || method.equals("branchedFlowThrough")
                    ? returnNode : call(returnNode, "getFirstChild", new Class<?>[]{});
            if (method.equals("traverseObjectLiteral"))
                call(closureNode, "setJSType", new Class<?>[]{Class.forName("com.google.javascript.rhino.jstype.JSType")}, nativeType("OBJECT_TYPE", true));
            Object analysis = construct("com.google.javascript.jscomp.ControlFlowAnalysis",
                    new Class<?>[]{abstractCompiler, boolean.class, boolean.class}, compiler, false, true);
            call(analysis, "process", new Class<?>[]{node, node}, null, function);
            cfg = call(analysis, "getCfg", new Class<?>[]{});
            Object convention = call(compiler, "getCodingConvention", new Class<?>[]{});
            reverse = construct("com.google.javascript.jscomp.type.SemanticReverseAbstractInterpreter",
                    new Class<?>[]{Class.forName("com.google.javascript.jscomp.CodingConvention"), registry.getClass()}, convention, registry);
            flow = call(Class.forName("com.google.javascript.jscomp.LinkedFlowScope"), "createEntryLattice",
                    new Class<?>[]{scopeClass}, scope);
            call(flow, "inferSlotType", new Class<?>[]{String.class, Class.forName("com.google.javascript.rhino.jstype.JSType")},
                    "x", nativeType(a < 0 ? "NUMBER_TYPE" : "STRING_TYPE", false));
        }

        void dom(double a) throws Exception {
            if (domRoot != null) return;
            javax.xml.parsers.DocumentBuilderFactory factory = pilot
                ? javax.xml.parsers.DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", SqaProbe.class.getClassLoader())
                : javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            org.w3c.dom.Document document = factory.newDocumentBuilder().newDocument();
            domRoot = document.createElementNS("urn:sqa:root", "r:root");
            document.appendChild(domRoot);
            domRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:r", "urn:sqa:root");
            domRoot.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "en");
            org.w3c.dom.Element element = document.createElementNS("urn:sqa:item", "i:item");
            domChild = element;
            element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:i", "urn:sqa:item");
            element.setAttribute("id", a < 0 ? "left" : "right");
            domChild.appendChild(document.createTextNode(a < 0 ? "alpha" : "beta"));
            org.w3c.dom.Element grandchild = document.createElementNS("urn:sqa:item", "i:item");
            grandchild.appendChild(document.createTextNode("nested"));
            domChild.appendChild(grandchild);
            org.w3c.dom.Element last = document.createElementNS("urn:sqa:item", "i:item");
            last.appendChild(document.createTextNode("nested-last"));
            domChild.appendChild(last);
            if (method.equals("getRelativePositionOfPI")) {
                domRoot.appendChild(document.createProcessingInstruction("fixture", "before"));
                domChild = document.createProcessingInstruction("fixture", a < 0 ? "alpha" : "beta");
            } else if (method.equals("getRelativePositionOfTextNode")) {
                domRoot.appendChild(document.createCDATASection("before"));
                domChild = document.createTextNode(a < 0 ? "alpha" : "beta");
            }
            domRoot.appendChild(domChild);
        }

        void jdom(double a) throws ReflectiveOperationException {
            if (jdomRoot != null) return;
            Class<?> element = Class.forName("org.jdom.Element");
            jdomRoot = construct(element.getName(), new Class<?>[]{String.class}, "root");
            jdomChild = construct(element.getName(), new Class<?>[]{String.class}, "item");
            call(jdomChild, "setText", new Class<?>[]{String.class}, a < 0 ? "alpha" : "beta");
            call(jdomChild, "setAttribute", new Class<?>[]{String.class, String.class}, "id", a < 0 ? "left" : "right");
            Object grandchild = construct(element.getName(), new Class<?>[]{String.class}, "item");
            call(grandchild, "setText", new Class<?>[]{String.class}, "nested");
            call(jdomChild, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, grandchild);
            Object last = construct(element.getName(), new Class<?>[]{String.class}, "item");
            call(last, "setText", new Class<?>[]{String.class}, "nested-last");
            call(jdomChild, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, last);
            if (method.equals("getRelativePositionOfPI")) {
                Object before = construct("org.jdom.ProcessingInstruction", new Class<?>[]{String.class, String.class}, "fixture", "before");
                call(jdomRoot, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, before);
                jdomChild = construct("org.jdom.ProcessingInstruction", new Class<?>[]{String.class, String.class}, "fixture", a < 0 ? "alpha" : "beta");
            } else if (method.equals("getRelativePositionOfTextNode")) {
                Object before = construct("org.jdom.CDATA", new Class<?>[]{String.class}, "before");
                call(jdomRoot, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, before);
                jdomChild = construct("org.jdom.Text", new Class<?>[]{String.class}, a < 0 ? "alpha" : "beta");
            }
            call(jdomRoot, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, jdomChild);
        }

        void configurePointer(Object pointer) throws ReflectiveOperationException {
            Class<?> resolverClass = Class.forName("org.apache.commons.jxpath.ri.NamespaceResolver");
            Object resolver = construct(resolverClass.getName(), new Class<?>[]{resolverClass}, new Object[]{null});
            call(resolver, "registerNamespace", new Class<?>[]{String.class, String.class}, "i", "urn:sqa:item");
            call(resolver, "registerNamespace", new Class<?>[]{String.class, String.class}, "r", "urn:sqa:root");
            call(resolver, "setNamespaceContextPointer", new Class<?>[]{Class.forName("org.apache.commons.jxpath.ri.model.NodePointer")}, pointer);
            call(pointer, "setNamespaceResolver", new Class<?>[]{resolverClass}, resolver);
        }

        Object argument(Class<?> type, double a, double b, double c, int depth) {
            try {
                if (depth > 2) throw new FixtureFailure("Fixture recursion limit: " + type.getName(), null);
                String name = type.getName();
                if (reviewed && !constructing && targetClass.equals("org.apache.commons.codec.language.Metaphone")
                        && method.equals("setMaxCodeLen") && type == int.class)
                    return a < -8 ? 0 : a < 0 ? 1 : a < 8 ? 4 : 8;
                if (pilot) {
                    if (targetClass.equals("com.google.gson.TypeInfoFactory")) {
                        java.lang.reflect.Field value = GenericFixture.class.getField(a < 0 ? "value" : "items");
                        if (type == java.lang.reflect.TypeVariable.class) return GenericFixture.class.getTypeParameters()[0];
                        if (type == java.lang.reflect.Field.class) return value;
                        if (type == Class.class) return GenericFixture.class;
                        if (type == java.lang.reflect.Type.class) {
                            if (method.equals("getTypeInfoForArray")) return a < 0 ? String[].class : Integer[].class;
                            return a < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();
                        }
                    }
                    if (targetClass.equals("com.google.javascript.jscomp.RemoveUnusedVars")) {
                        unusedClosure(a);
                        if (type == boolean.class) return false; // No call-site optimizer prerequisite.
                        if (name.equals("com.google.javascript.jscomp.AbstractCompiler")) return compiler;
                        if (name.equals("com.google.javascript.rhino.Node")) {
                            if (method.equals("process")) return cleanupNodeIndex++ == 0 ? cleanupExterns : cleanupScript;
                            if (method.equals("getFunctionArgList")) {
                                Object child = call(cleanupScript, "getFirstChild", new Class<?>[]{});
                                while (child != null && !(Boolean)call(child, "isFunction", new Class<?>[]{}))
                                    child = call(child, "getNext", new Class<?>[]{});
                                if (child == null) throw new FixtureFailure("Missing parsed function", null);
                                return child;
                            }
                            return cleanupScript;
                        }
                    }
                    if (targetClass.equals("org.jfree.chart.renderer.category.AreaRenderer")) {
                        chart(a);
                        if (name.equals("org.jfree.data.category.CategoryDataset")) return chartDataset;
                        if (name.equals("org.jfree.chart.axis.CategoryAxis")) return chartAxis;
                        if (type == Comparable.class) return a < 0 ? "row-a" : "column-a";
                        if (type == java.awt.geom.Rectangle2D.class) return new java.awt.geom.Rectangle2D.Double(0,0,16,16);
                        if (name.equals("org.jfree.chart.util.RectangleEdge")) return type.getField("BOTTOM").get(null);
                        if (type == int.class) return 0;
                    }
                    if (targetClass.equals("com.fasterxml.jackson.databind.ser.BeanPropertyWriter")) {
                        if (name.equals(targetClass)) return beanWriter();
                        if (name.equals("com.fasterxml.jackson.databind.util.NameTransformer"))
                            return call(type, "simpleTransformer", new Class<?>[]{String.class, String.class}, a < 0 ? "left_" : "right_", "_suffix");
                        if (type == Object.class) return method.equals("get") ? new FixtureBean() : a < 0 ? "fixture-key" : "fixture-value";
                    }
                    if (targetClass.equals("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer")) {
                        jacksonCollection(a);
                        if (name.equals("com.fasterxml.jackson.databind.JavaType")) return collectionType;
                        if (name.equals("com.fasterxml.jackson.core.JsonParser")) return parser;
                        if (name.equals("com.fasterxml.jackson.databind.DeserializationContext")) return context;
                        if (name.equals("com.fasterxml.jackson.databind.deser.ValueInstantiator"))
                            return call(collectionDeserializer, "getValueInstantiator", new Class<?>[]{});
                        if (name.equals("com.fasterxml.jackson.databind.JsonDeserializer"))
                            return Class.forName("com.fasterxml.jackson.databind.deser.std.StringDeserializer").getField("instance").get(null);
                    }
                    if (targetClass.equals("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser")) {
                        String xml = a < 0 ? "<root><item>123</item><other>alpha</other></root>" : "<root><item>45</item><other>beta</other></root>";
                        if (type == int.class && constructing) return 0;
                        if (name.equals("com.fasterxml.jackson.core.io.IOContext"))
                            return construct(name, new Class<?>[]{Class.forName("com.fasterxml.jackson.core.util.BufferRecycler"), Object.class, boolean.class},
                                construct("com.fasterxml.jackson.core.util.BufferRecycler", new Class<?>[]{}), xml, false);
                        if (name.equals("com.fasterxml.jackson.core.ObjectCodec")) return construct("com.fasterxml.jackson.dataformat.xml.XmlMapper", new Class<?>[]{});
                        if (type == javax.xml.stream.XMLStreamReader.class) {
                            javax.xml.stream.XMLStreamReader reader = javax.xml.stream.XMLInputFactory.newInstance().createXMLStreamReader(new java.io.StringReader(xml));
                            while (reader.hasNext() && reader.getEventType() != javax.xml.stream.XMLStreamConstants.START_ELEMENT) reader.next();
                            return reader;
                        }
                    }
                    if (targetClass.equals("org.mockito.internal.invocation.InvocationMatcher")) {
                        mockito(a);
                        if (name.equals("org.mockito.invocation.Invocation")) return constructing ? baseInvocation : actualInvocation;
                    }
                    if (targetClass.startsWith("org.apache.commons.math3.fraction.")) {
                        int number = 1 + bucket(a, 8);
                        if (type == double.class) return (a < 0 ? -1 : 1) * number / 4.0;
                        if (type == int.class) return number;
                        if (type == long.class) return (long)number;
                        if (type == java.math.BigInteger.class) return java.math.BigInteger.valueOf(number);
                        if (name.equals("org.apache.commons.math3.fraction.BigFraction") || name.equals("org.apache.commons.math3.fraction.Fraction"))
                            return construct(name, new Class<?>[]{int.class, int.class}, number, 3);
                    }
                    if (targetClass.equals("org.apache.commons.cli.CommandLine")) {
                        if (type == String.class) return constructing ? "fixture" : a < -0.33 ? "x" : a < 0.33 ? "missing" : "extra";
                        if (type == char.class) return a < 0 ? 'x' : 'z';
                        if (name.equals("org.apache.commons.cli.Option")) return option("extra", a < 0 ? "left" : "right");
                    }
                    if (targetClass.equals("org.jsoup.nodes.Document") && type == String.class)
                        return constructing ? "https://fixture.invalid/" : method.equals("createElement") ? a < 0 ? "span" : "section"
                            : STRINGS[bucket(a, STRINGS.length)];
                    if (targetClass.equals("org.joda.time.Partial")) {
                        if (type == int.class) return bucket(a, 24);
                        if (name.equals("org.joda.time.DateTimeFieldType"))
                            return call(type, "hourOfDay", new Class<?>[]{});
                    }
                    if (name.equals("org.joda.time.DurationFieldType")) return call(type, a < 0 ? "hours" : "days", new Class<?>[]{});
                    if (name.equals("org.joda.time.DurationField")) return call(Class.forName("org.joda.time.field.UnsupportedDurationField"),
                        "getInstance", new Class<?>[]{Class.forName("org.joda.time.DurationFieldType")},
                        call(Class.forName("org.joda.time.DurationFieldType"), "hours", new Class<?>[]{}));
                    if (name.equals("com.fasterxml.jackson.core.util.BufferRecycler")) return construct(name, new Class<?>[]{});
                    if (type == java.io.OutputStream.class && targetClass.endsWith("CpioArchiveOutputStream")) {
                        archiveBytes = new java.io.ByteArrayOutputStream();
                        return archiveBytes;
                    }
                    if (name.equals("org.apache.commons.compress.archivers.ArchiveEntry") || name.equals("org.apache.commons.compress.archivers.cpio.CpioArchiveEntry"))
                        return archiveEntry(a < 0 ? "next-left.txt" : "next-right.txt", 0);
                    if (targetClass.equals("com.fasterxml.jackson.core.io.NumberInput") && type == String.class)
                        return new String[]{"0", "1", "12", "2147483647"}[bucket(a, 4)];
                }
                if (scalar(type)) {
                    if (type == String.class && method.equals("getRelativePositionOfPI")) return a < 0 ? "fixture" : "other";
                    if (type == String.class && (method.equals("namespacePointer") || method.equals("getNamespaceURI")))
                        return a < 0 ? "r" : "i";
                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);
                }
                if (type.isArray()) {
                    Object array = Array.newInstance(type.getComponentType(), pilot && (targetClass.endsWith("NumberUtils") || targetClass.endsWith("TypeInfoFactory")) ? 1 + bucket(c, 4) : bucket(c, 5));
                    for (int i = 0; i < Array.getLength(array); i++)
                        Array.set(array, i, argument(type.getComponentType(), a, b, c, depth + 1));
                    return array;
                }
                if (type == java.io.Reader.class && targetClass.equals("org.apache.commons.csv.ExtendedBufferedReader"))
                    return new java.io.StringReader(bufferSlices ? (a < 0 ? "A\nBC\nDE" : "12\n345\n") : STRINGS[bucket(a, STRINGS.length)]);
                if (name.startsWith("com.google.javascript.")) {
                    closure(a);
                    if (name.endsWith(".AbstractCompiler")) return compiler;
                    if (name.endsWith(".ControlFlowGraph")) return cfg;
                    if (name.endsWith(".ReverseAbstractInterpreter")) return reverse;
                    if (name.endsWith(".Scope")) return scope;
                    if (name.endsWith(".Scope$Var")) return call(scope, "getVar", new Class<?>[]{String.class}, "x");
                    if (name.endsWith(".FlowScope")) return flow;
                    if (name.endsWith(".Node")) return closureNode;
                    if (name.endsWith(".JSType")) return nativeType(a < 0 ? "NUMBER_TYPE" : "STRING_TYPE", false);
                    if (name.endsWith(".ObjectType")) return nativeType("OBJECT_TYPE", true);
                }
                if (name.startsWith("org.w3c.dom.")) {
                    dom(a);
                    if (type.isInstance(domChild)) return domChild;
                    if (type.isInstance(domChild.getOwnerDocument())) return domChild.getOwnerDocument();
                }
                if (type == java.util.Locale.class) return java.util.Locale.ROOT;
                if (name.equals("org.apache.commons.jxpath.ri.QName"))
                    return construct(name, new Class<?>[]{String.class}, method.equals("attributeIterator") ? "id" : "item");
                if (name.equals("org.apache.commons.jxpath.ri.compiler.NodeTest"))
                    return construct("org.apache.commons.jxpath.ri.compiler.NodeNameTest",
                            new Class<?>[]{Class.forName("org.apache.commons.jxpath.ri.QName"), String.class},
                            targetClass.contains(".jdom.")
                                ? construct("org.apache.commons.jxpath.ri.QName", new Class<?>[]{String.class}, "item")
                                : construct("org.apache.commons.jxpath.ri.QName", new Class<?>[]{String.class, String.class}, "i", "item"),
                            targetClass.contains(".jdom.") ? null : "urn:sqa:item");
                if (name.equals("org.apache.commons.jxpath.ri.model.NodePointer")) {
                    if (targetClass.contains(".jdom.")) {
                        jdom(a);
                        if (!constructing && (method.equals("childIterator") || method.equals("compareChildNodePointers"))) {
                            List<?> children = (List<?>)call(jdomChild, "getContent", new Class<?>[]{});
                            Object anchor = children.get(a < 0 ? 0 : children.size() - 1);
                            Object pointer = construct(targetClass, new Class<?>[]{type, Object.class}, receiver, anchor);
                            configurePointer(pointer);
                            return pointer;
                        }
                        Object pointer = construct("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
                                new Class<?>[]{Object.class, java.util.Locale.class}, jdomRoot, java.util.Locale.ROOT);
                        configurePointer(pointer);
                        return pointer;
                    }
                    dom(a);
                    if (!constructing && (method.equals("childIterator") || method.equals("compareChildNodePointers"))) {
                        org.w3c.dom.Node anchor = a < 0 ? domChild.getFirstChild() : domChild.getLastChild();
                        Object pointer = construct(targetClass, new Class<?>[]{type, org.w3c.dom.Node.class}, receiver, anchor);
                        configurePointer(pointer);
                        return pointer;
                    }
                    Object pointer = construct("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
                            new Class<?>[]{org.w3c.dom.Node.class, java.util.Locale.class}, domRoot, java.util.Locale.ROOT);
                    configurePointer(pointer);
                    return pointer;
                }
                if (type == Object.class && targetClass.contains(".jdom.")
                        && (constructing || !method.equals("setValue"))) { jdom(a); return jdomChild; }
                if (type == java.util.Iterator.class) return new ArrayList<Object>().iterator();
                if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)
                    return new ArrayList<Object>();
                if (type == java.util.Set.class) return new java.util.HashSet<Object>();
                if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();
                if (type == Object.class || type == Number.class || type == java.util.Date.class)
                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);
                throw new FixtureFailure("No explicit recipe: " + name, null);
            } catch (FixtureFailure failure) { throw failure; }
            catch (Exception failure) { throw new FixtureFailure("Fixture recipe failed: " + type.getName()
                    + ":" + failure.getClass().getName() + ":" + failure.getMessage(), failure); }
        }

        String nodeSnapshot(org.w3c.dom.Node node, int depth) {
            if (depth > 8) return "depth-limit";
            StringBuilder out = new StringBuilder("node:").append(node.getNodeType()).append(':')
                    .append(quote(node.getNodeName())).append(':').append(quote(String.valueOf(node.getNodeValue())));
            org.w3c.dom.NamedNodeMap attributes = node.getAttributes();
            List<String> attrs = new ArrayList<String>();
            if (attributes != null) for (int i = 0; i < attributes.getLength(); i++)
                attrs.add(nodeSnapshot(attributes.item(i), depth + 1));
            java.util.Collections.sort(attrs);
            out.append(attrs.toString()).append('[');
            org.w3c.dom.NodeList children = node.getChildNodes();
            for (int i = 0; i < Math.min(256, children.getLength()); i++) out.append(nodeSnapshot(children.item(i), depth + 1));
            return out.append("]children:").append(children.getLength()).toString();
        }

        Object field(Object value, String name) throws ReflectiveOperationException {
            for (Class<?> type = value.getClass(); type != null; type = type.getSuperclass()) {
                try {
                    java.lang.reflect.Field field = type.getDeclaredField(name);
                    field.setAccessible(true);
                    return field.get(value);
                } catch (NoSuchFieldException missing) { }
            }
            throw new NoSuchFieldException(name);
        }

        String projection(Object result, int depth) throws ReflectiveOperationException {
            if (depth > 8) throw new FixtureFailure("Oracle projection depth exceeded", null);
            if (result == null) return "null";
            String name = result.getClass().getName();
            if (fractionField && (name.equals("org.apache.commons.math3.fraction.BigFractionField")
                    || name.equals("org.apache.commons.math3.fraction.FractionField")))
                return "fraction-field:runtime=" + projection(call(result, "getRuntimeClass", new Class<?>[]{}), depth + 1)
                    + ":zero=" + projection(call(result, "getZero", new Class<?>[]{}), depth + 1)
                    + ":one=" + projection(call(result, "getOne", new Class<?>[]{}), depth + 1);
            if (pilot && result instanceof java.lang.reflect.Type) return "type:" + nestedTestName(((java.lang.reflect.Type)result).getTypeName());
            if (pilot && result instanceof Method) return "method:" + nestedTestName(((Method)result).toGenericString());
            if (pilot && name.startsWith("com.google.gson.TypeInfo"))
                return "type-info:" + projection(call(result, "getActualType", new Class<?>[]{}), depth + 1);
            if (pilot && name.equals("com.google.javascript.rhino.Node")) return "ast:" + call(result, "toStringTree", new Class<?>[]{});
            if (pilot && name.equals("org.apache.commons.jxpath.ri.NamespaceResolver"))
                return "namespaces:r=" + call(result, "getNamespaceURI", new Class<?>[]{String.class}, "r")
                    + ":i=" + call(result, "getNamespaceURI", new Class<?>[]{String.class}, "i");
            if (pilot && name.equals("org.jfree.data.Range"))
                return "range:" + call(result, "getLowerBound", new Class<?>[]{}) + ':' + call(result, "getUpperBound", new Class<?>[]{});
            if (pilot && name.equals("org.jfree.chart.LegendItem")) return "legend:" + call(result, "getLabel", new Class<?>[]{});
            if (pilot && name.equals("org.jfree.chart.LegendItemCollection")) {
                StringBuilder out = new StringBuilder("legends[");
                int count = ((Number)call(result, "getItemCount", new Class<?>[]{})).intValue();
                if (count > 256) throw new FixtureFailure("Legend limit exceeded", null);
                for (int i = 0; i < count; i++) out.append(projection(call(result, "get", new Class<?>[]{int.class}, i), depth + 1)).append(';');
                return out.append(']').toString();
            }
            if (pilot && name.startsWith("com.fasterxml.jackson.databind.type.")) return "java-type:" + call(result, "toCanonical", new Class<?>[]{});
            if (pilot && name.equals("com.fasterxml.jackson.core.io.SerializedString")) return "serialized-name:" + call(result, "getValue", new Class<?>[]{});
            if (pilot && name.equals("com.fasterxml.jackson.databind.ser.BeanPropertyWriter"))
                return "property:" + call(result, "getName", new Class<?>[]{}) + ':' + projection(call(result, "getType", new Class<?>[]{}), depth + 1);
            if (pilot && targetClass.equals("org.mockito.internal.invocation.InvocationMatcher")
                    && Class.forName("org.mockito.invocation.Invocation").isInstance(result))
                return "invocation:" + projection(call(result, "getMethod", new Class<?>[]{}), depth + 1)
                    + ':' + projection(call(result, "getArguments", new Class<?>[]{}), depth + 1)
                    + ":verified=" + call(result, "isVerified", new Class<?>[]{});
            if (pilot && result.getClass().isArray()) {
                int length = Array.getLength(result);
                if (length > 100000) throw new FixtureFailure("Oracle array limit exceeded", null);
                StringBuilder out = new StringBuilder("array[");
                for (int i = 0; i < length; i++) out.append(projection(Array.get(result, i), depth + 1)).append(';');
                return out.append(']').toString();
            }
            if (pilot && (name.equals("org.jsoup.nodes.Document") || name.equals("org.jsoup.nodes.Element")))
                return "html:" + call(result, "outerHtml", new Class<?>[]{});
            if (pilot && name.equals("org.apache.commons.cli.Option"))
                return "option:" + call(result, "getOpt", new Class<?>[]{}) + ':' + projection(call(result, "getValues", new Class<?>[]{}), depth + 1);
            if (pilot && result instanceof java.util.Iterator) {
                StringBuilder out = new StringBuilder("iterator[");
                java.util.Iterator<?> iterator = (java.util.Iterator<?>)result;
                int count = 0;
                while (iterator.hasNext()) {
                    if (++count > 256) throw new FixtureFailure("Oracle iterator limit exceeded", null);
                    out.append(projection(iterator.next(), depth + 1)).append(';');
                }
                return out.append(']').toString();
            }
            if (pilot && (name.equals("org.apache.commons.math3.fraction.BigFraction") || name.equals("org.apache.commons.math3.fraction.Fraction")))
                return "fraction:" + call(result, "getNumerator", new Class<?>[]{}) + '/' + call(result, "getDenominator", new Class<?>[]{});
            if (pilot && name.startsWith("org.joda.time.")) {
                if (name.equals("org.joda.time.Partial")) return "partial:" + call(result, "toStringList", new Class<?>[]{});
                if (Class.forName("org.joda.time.DurationFieldType").isInstance(result)) return "duration-type:" + call(result, "getName", new Class<?>[]{});
                if (Class.forName("org.joda.time.DurationField").isInstance(result))
                    return "duration:" + call(result, "getName", new Class<?>[]{}) + ':' + call(result, "isSupported", new Class<?>[]{});
            }
            if (result instanceof org.w3c.dom.Node) return nodeSnapshot((org.w3c.dom.Node)result, 0);
            if (reviewed && name.equals("org.jdom.Attribute"))
                return "jdom-attribute:name=" + projection(call(result, "getName", new Class<?>[]{}), depth + 1)
                    + ":namespace=" + projection(call(result, "getNamespaceURI", new Class<?>[]{}), depth + 1)
                    + ":value=" + projection(call(result, "getValue", new Class<?>[]{}), depth + 1);
            if (name.equals("org.jdom.Element") || name.equals("org.jdom.ProcessingInstruction")
                    || name.equals("org.jdom.Text") || name.equals("org.jdom.CDATA")) {
                Object writer = construct("org.jdom.output.XMLOutputter", new Class<?>[]{});
                return "xml:" + call(writer, "outputString", new Class<?>[]{result.getClass()}, result);
            }
            if (name.equals("org.apache.commons.jxpath.ri.QName")) return "qname:" + result.toString();
            if (name.startsWith("com.google.javascript.rhino.jstype.")) return "js-type:" + result.toString();
            if (name.equals("com.google.javascript.jscomp.LinkedFlowScope")) {
                Object slot = call(result, "getSlot", new Class<?>[]{String.class}, "x");
                return "flow:x=" + (slot == null ? "absent" : projection(call(slot, "getType", new Class<?>[]{}), depth + 1));
            }
            if (name.endsWith("TypeInference$BooleanOutcomePair"))
                return "boolean-pair:" + field(result, "toBooleanOutcomes") + ':' + field(result, "booleanValues")
                    + ":left=" + projection(field(result, "leftScope"), depth + 1)
                    + ":right=" + projection(field(result, "rightScope"), depth + 1);
            if (result instanceof List) {
                StringBuilder out = new StringBuilder("list[");
                if (((List<?>)result).size() > 256) throw new FixtureFailure("Oracle collection limit exceeded", null);
                for (Object item : (List<?>)result) out.append(projection(item, depth + 1)).append(';');
                return out.append(']').toString();
            }
            if (result instanceof java.util.Map) {
                java.util.Map<?,?> map = (java.util.Map<?,?>)result;
                if (map.size() > 256) throw new FixtureFailure("Oracle map limit exceeded", null);
                List<String> entries = new ArrayList<String>();
                for (java.util.Map.Entry<?,?> entry : map.entrySet())
                    entries.add(projection(entry.getKey(), depth + 1) + "=" + projection(entry.getValue(), depth + 1));
                java.util.Collections.sort(entries);
                return "map:" + entries.toString();
            }
            if (name.startsWith("org.apache.commons.jxpath.ri.model.")) {
                Class<?> pointer = Class.forName("org.apache.commons.jxpath.ri.model.NodePointer");
                if (pointer.isInstance(result))
                    return "pointer:" + projection(call(result, "getImmediateNode", new Class<?>[]{}), depth + 1);
                if (Class.forName("org.apache.commons.jxpath.ri.model.NodeIterator").isInstance(result)) {
                    StringBuilder out = new StringBuilder("iterator[");
                    for (int i = 1; i <= 9; i++) {
                        boolean present = (Boolean)call(result, "setPosition", new Class<?>[]{int.class}, i);
                        if (!present) return out.append(']').toString();
                        if (i == 9) throw new FixtureFailure("Oracle iterator limit exceeded", null);
                        out.append(projection(call(result, "getNodePointer", new Class<?>[]{}), depth + 1)).append(';');
                    }
                }
            }
            String simple = value(result);
            if (simple.startsWith("object-type:")) throw new FixtureFailure("No structural oracle: " + name, null);
            return simple;
        }

        String state() throws ReflectiveOperationException {
            if (reviewed && targetClass.equals("org.apache.commons.codec.language.Metaphone")
                    && method.equals("setMaxCodeLen")) {
                int limit = ((Number)call(receiver, "getMaxCodeLen", new Class<?>[]{})).intValue();
                String encoded = (String)call(receiver, "metaphone", new Class<?>[]{String.class}, "architecture");
                return "metaphone:maxCodeLen=" + limit + ":encoded=" + encoded
                    + ":maxCodeLenAfterEncoding=" + call(receiver, "getMaxCodeLen", new Class<?>[]{});
            }
            if (langHelpers && targetClass.equals("org.apache.commons.lang3.math.NumberUtils")
                    && method.equals("validateArray")) return "validation-input:" + value(validationInput);
            if (pilot && targetClass.equals("com.google.javascript.jscomp.RemoveUnusedVars"))
                return "cleanup:" + call(cleanupScript, "toStringTree", new Class<?>[]{});
            if (pilot && targetClass.equals("org.jfree.chart.renderer.category.AreaRenderer"))
                return "chart:rows=" + call(chartDataset, "getRowCount", new Class<?>[]{}) + ":columns=" + call(chartDataset, "getColumnCount", new Class<?>[]{});
            if (pilot && targetClass.equals("com.fasterxml.jackson.databind.ser.BeanPropertyWriter"))
                return projection(receiver, 0) + ":setting=" + projection(call(receiver, "getInternalSetting", new Class<?>[]{Object.class}, "fixture-key"), 0);
            if (pilot && targetClass.equals("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer"))
                return "json-token:" + call(parser, "getCurrentToken", new Class<?>[]{});
            if (pilot && targetClass.equals("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser"))
                return "xml:closed=" + call(receiver, "isClosed", new Class<?>[]{}) + ":token=" + call(receiver, "getCurrentToken", new Class<?>[]{})
                    + ":text=" + projection(field(receiver, "_currText"), 0);
            if (pilot && targetClass.equals("org.mockito.internal.invocation.InvocationMatcher"))
                return projection(baseInvocation, 0) + ":candidate=" + projection(actualInvocation, 0);
            if (pilot && targetClass.equals("org.apache.commons.cli.CommandLine"))
                return "cli:" + projection(call(receiver, "getOptions", new Class<?>[]{}), 0) + ':' + projection(call(receiver, "getArgs", new Class<?>[]{}), 0);
            if (pilot && targetClass.equals("com.fasterxml.jackson.core.util.TextBuffer"))
                return "text:" + call(receiver, "contentsAsString", new Class<?>[]{}) + ":size=" + call(receiver, "size", new Class<?>[]{});
            if (pilot && targetClass.equals("org.jsoup.nodes.Document") && receiver != null) return projection(receiver, 0);
            if (pilot && targetClass.endsWith("CpioArchiveOutputStream")) return "archive:" + value(archiveBytes.toByteArray());
            if (pilot && targetClass.startsWith("org.apache.commons.math3.fraction.")) return projection(receiver, 0);
            if (pilot && targetClass.equals("org.joda.time.Partial")) return projection(receiver, 0);
            if (pilot && targetClass.equals("org.joda.time.field.UnsupportedDurationField") && receiver != null) return projection(receiver, 0);
            if (targetClass.equals("org.apache.commons.collections.map.Flat3Map")) return projection(receiver, 0);
            if (targetClass.equals("org.apache.commons.csv.ExtendedBufferedReader"))
                return "reader:line=" + call(receiver, "getLineNumber", new Class<?>[]{})
                    + ":last=" + call(receiver, "readAgain", new Class<?>[]{})
                    + (bufferSlices && outputBuffer != null ? ":buffer=" + value(outputBuffer) : "");
            if (compiler != null) {
                Object jsType = call(closureNode, "getJSType", new Class<?>[]{});
                return "ast:" + call(closureNode, "toStringTree", new Class<?>[]{})
                    + ":ast-type=" + projection(jsType, 0) + ':' + projection(flow, 0);
            }
            if (domRoot != null) return nodeSnapshot(domRoot, 0) + ":child=" + nodeSnapshot(domChild, 0)
                    + ":attached=" + (domChild.getParentNode() != null);
            if (jdomRoot != null) return projection(jdomRoot, 0) + ":child=" + projection(jdomChild, 0)
                    + ":attached=" + (call(jdomChild, "getParent", new Class<?>[]{}) != null);
            return "stateless-scalars";
        }
    }

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
        FixtureSession session = FIXTURES.get();
        return session == null ? legacyArgument(type, a, b, c, depth) : session.argument(type, a, b, c, depth);
    }

    private static Object legacyArgument(Class<?> type, double a, double b, double c, int depth) {
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
        FixtureSession explicitSession = FIXTURES.get();
        if (explicitSession != null) {
            Object[] helpers = explicitSession.langHelperArguments(types, vector);
            if (helpers != null) return helpers;
            Object[] bounded = explicitSession.boundedBufferArguments(types, vector);
            if (bounded != null) return bounded;
        }
        Object[] values = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            int start = offset + 3 * i;
            values[i] = argument(types[i], vector[start % vector.length],
                vector[(start + 1) % vector.length], vector[(start + 2) % vector.length]);
        }
        FixtureSession session = FIXTURES.get();
        if (session != null && session.pilot && !session.constructing) {
            if (session.targetClass.equals("com.google.gson.TypeInfoFactory")) {
                java.lang.reflect.Type parent = vector[0] < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();
                try {
                    if (session.method.equals("getActualType")) {
                        values[0] = GenericFixture.class.getField("items").getGenericType();
                        values[1] = parent;
                        values[2] = GenericFixture.class;
                    } else if (session.method.equals("extractRealTypes")) {
                        values[0] = new java.lang.reflect.Type[]{GenericFixture.class.getField("value").getGenericType()};
                        values[1] = parent;
                        values[2] = GenericFixture.class;
                    }
                } catch (NoSuchFieldException failure) { throw new FixtureFailure("Generic schema field missing", failure); }
            }
            if (session.targetClass.equals("org.jfree.chart.renderer.category.AreaRenderer") && session.method.equals("getItemMiddle")) {
                values[0] = "row-a";
                values[1] = "column-a";
            }
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
        if (value instanceof Class) return "class:" + nestedTestName(((Class<?>)value).getName());
        if (!scalar(type) && !(value instanceof Number)) return "object-type:" + type.getName();
        String text = value instanceof Enum ? ((Enum<?>) value).name() : String.valueOf(value);
        return type.getName() + ":" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    private static String nestedTestName(String text) {
        // GeneratedStudyTest nests a copy of this helper, so probe-time
        // "SqaProbe$FixtureMock" renders at test runtime as
        // "GeneratedStudyTest$SqaProbe$FixtureMock". Oracles must compare
        // the probe-time spelling in both phases; never edit old suites.
        return text.replace("GeneratedStudyTest$SqaProbe$", "SqaProbe$");
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
        INVOKED.set(false);
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
                FixtureSession session = FIXTURES.get();
                if (session != null) session.constructing = true;
                try {
                    Object[] values = arguments(ctorTypes, vector, 0);
                    if (method == null) INVOKED.set(true);
                    receiver = ctor.newInstance(values);
                    if (session != null) receiver = session.prepareReceiver(receiver, vector[0]);
                    if (session != null) session.receiver = receiver;
                    if (session != null && className.equals("org.apache.commons.collections.map.Flat3Map")) {
                        call(receiver, "put", new Class<?>[]{Object.class, Object.class}, "fixture-a", "value-a");
                        call(receiver, "put", new Class<?>[]{Object.class, Object.class}, "fixture-b", "value-b");
                    }
                    if (session != null && className.startsWith("org.apache.commons.jxpath.ri.model.")) session.configurePointer(receiver);
                } catch (InvocationTargetException error) {
                    if (session != null && method != null)
                        throw new FixtureFailure("Receiver constructor failed before method invocation", error.getCause());
                    throw error;
                } finally { if (session != null) session.constructing = false; }
            }
            if (method == null) {
                if (FIXTURES.get() == null) return "constructed:" + target.getName();
                try { return snapshot("constructed:" + target.getName() + ":state=" + FIXTURES.get().state()); }
                catch (ReflectiveOperationException failure) { throw new FixtureFailure("Constructor state oracle failed", failure); }
            }
            Object[] values = arguments(parameterTypes, vector, ctorTypes.length * 3);
            INVOKED.set(true);
            Object result = method.invoke(receiver, values);
            if (FIXTURES.get() != null) {
                FixtureSession session = FIXTURES.get();
                try {
                    return snapshot((method.getReturnType() == void.class ? "void" : "value:" + session.projection(result, 0))
                            + "|state=" + session.state());
                } catch (ReflectiveOperationException failure) { throw new FixtureFailure("Structural oracle failed", failure); }
            }
            return method.getReturnType() == void.class ? "void" : snapshot("value:" + value(result));
        } catch (InvocationTargetException error) {
            Throwable cause = error.getCause();
            if (cause instanceof VirtualMachineError || cause instanceof LinkageError || cause instanceof ThreadDeath)
                throw new IllegalStateException("SQA_HARNESS JVM failure", cause);
            FixtureSession session = FIXTURES.get();
            if (session != null && session.langHelpers && session.targetClass.equals("org.apache.commons.lang3.math.NumberUtils")
                    && session.method.equals("validateArray")) {
                try { return "exception:" + cause.getClass().getName() + "|message=" + value(cause.getMessage())
                        + "|state=" + session.state(); }
                catch (ReflectiveOperationException failure) { throw new FixtureFailure("Validation boundary oracle failed", failure); }
            }
            return "exception:" + cause.getClass().getName();
        } catch (ReflectiveOperationException error) {
            throw new IllegalStateException("SQA_HARNESS reflection failure", error);
        } catch (LinkageError error) {
            throw new IllegalStateException("SQA_HARNESS linkage failure", error);
        }
    }

    public static String observeWithPolicy(String className, String constructorTypes, String methodName,
            String methodTypes, double[] vector, String policy) {
        if (!EXPLICIT_FIXTURES.equals(policy) && !SCALAR_FIXTURES.equals(policy)
                && !PILOT_FIXTURES.equals(policy) && !BUFFER_FIXTURES.equals(policy)
                && !FRACTION_FIELD_FIXTURES.equals(policy) && !LANG_HELPER_FIXTURES.equals(policy) && !JOINT_FIXTURES.equals(policy)
                && !CHRONOLOGY_FIXTURES.equals(policy) && !GRAPHICS_FIXTURES.equals(policy) && !CODEC_FIXTURES.equals(policy))
            throw new IllegalArgumentException("Unknown explicit fixture policy");
        FIXTURES.set(new FixtureSession(className, methodName, policy));
        try {
            if (CODEC_FIXTURES.equals(policy) && codecIdentity(className,constructorTypes,methodName,methodTypes))
                return CodecRecipe.observe(methodName,vector);
            if ((GRAPHICS_FIXTURES.equals(policy) || CODEC_FIXTURES.equals(policy)) && graphicsIdentity(className, constructorTypes, methodName, methodTypes))
                return observeGraphics(methodName, vector);
            if ((CHRONOLOGY_FIXTURES.equals(policy) || GRAPHICS_FIXTURES.equals(policy) || CODEC_FIXTURES.equals(policy)) && className.equals("org.joda.time.Partial")
                    && chronologyIdentity(constructorTypes, methodName, methodTypes))
                return observeChronology(constructorTypes, methodName, methodTypes, vector);
            return observe(className, constructorTypes, methodName, methodTypes, vector);
        }
        finally { FIXTURES.remove(); }
    }

    private static boolean codecIdentity(String owner,String ctor,String method,String params){
        if(!ctor.isEmpty())return false;
        if(owner.equals("org.apache.commons.codec.language.Metaphone") && method.equals("isNextChar"))return params.equals("java.lang.StringBuffer,int,char");
        if(owner.equals("org.apache.commons.codec.language.Metaphone") && method.equals("isPreviousChar"))return params.equals("java.lang.StringBuffer,int,char");
        if(owner.equals("org.apache.commons.codec.language.Metaphone") && method.equals("isVowel"))return params.equals("java.lang.StringBuffer,int");
        if(owner.equals("org.apache.commons.codec.language.Metaphone") && method.equals("regionMatch"))return params.equals("java.lang.StringBuffer,int,java.lang.String");
        if(owner.equals("org.apache.commons.codec.language.SoundexUtils") && method.equals("difference"))return params.equals("org.apache.commons.codec.StringEncoder,java.lang.String,java.lang.String");
        return false;
    }
    /** Jointly scoped Codec5 only. Data/oracles predeclared before production execution. */
    public static final class CodecRecipe {
    public static String activeCase = "";
    static String q(String s) { return "\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\""; }
    static String json(Object o) {
        if(o==null)return "null";
        if(o instanceof String)return q((String)o);
        if(o instanceof Map) {
            StringBuilder b=new StringBuilder("{");
            for(Object entry:((Map)o).entrySet()) {
                Map.Entry e=(Map.Entry)entry;if(b.length()>1)b.append(',');
                b.append(q((String)e.getKey())).append(':').append(json(e.getValue()));
            }return b.append('}').toString();
        }
        if(o instanceof List) {
            StringBuilder b=new StringBuilder("[");
            for(Object item:(List)o){if(b.length()>1)b.append(',');b.append(json(item));}
            return b.append(']').toString();
        }return String.valueOf(o);
    }
    static Map<String,Object> map(Object... kv) {
        Map<String,Object> r=new LinkedHashMap<String,Object>();
        for(int i=0;i<kv.length;i+=2)r.put((String)kv[i],kv[i+1]);return r;
    }
    static String string(String encoded) {
        return encoded.equals("-")?null:new String(Base64.getDecoder().decode(encoded),StandardCharsets.UTF_8);
    }
    static String type(Class<?> c) {
        if(c==boolean.class)return "Z";if(c==int.class)return "I";if(c==char.class)return "C";
        return "L"+c.getName().replace('.','/')+";";
    }
    static String descriptor(Method m) {
        StringBuilder b=new StringBuilder("(");for(Class<?> c:m.getParameterTypes())b.append(type(c));
        return b.append(')').append(type(m.getReturnType())).toString();
    }
    static void check(boolean ok,String reason){if(!ok)throw new AssertionError(reason);}
    static Map<String,Object> run(String[] c) throws Exception {
        String name=c[0],method=c[1],text=string(c[2]),needle=string(c[5]),s1=string(c[6]),s2=string(c[7]);
        int index=Integer.parseInt(c[3]);char ch=(char)Integer.parseInt(c[4]);
        Object expectedValue=c[8].equals("null")?null:(c[8].equals("true")?Boolean.TRUE:
              (c[8].equals("false")?Boolean.FALSE:Integer.valueOf(c[8])));
        String expectedException=c[9].equals("-")?null:c[9];
        StringBuffer buffer=text==null?null:new StringBuffer(text);
        Object receiver;Object encoder=null;Class<?> owner;Class<?>[] types;Object[] args;
        Object encoded=null;
        if(method.equals("difference")) {
            owner=Class.forName("org.apache.commons.codec.language.SoundexUtils");
            Constructor<?> ctor=owner.getDeclaredConstructor();ctor.setAccessible(true);receiver=ctor.newInstance();
            encoder=construct("org.apache.commons.codec.language.Metaphone", new Class<?>[]{});check(Integer.valueOf(4).equals(call(encoder,"getMaxCodeLen",new Class<?>[]{})),"Exact encoder initial state");
            // Independent literal encoded references are fixture preconditions, never the result oracle.
            List<String> pre=Arrays.asList((String)call(encoder,"encode",new Class<?>[]{String.class},s1),(String)call(encoder,"encode",new Class<?>[]{String.class},s2));
            List<String> refs=Arrays.asList(string(c[10]),string(c[11]));
            check(pre.equals(refs),"Real production encoder differs from declared literal references");encoded=pre;
            types=new Class<?>[]{Class.forName("org.apache.commons.codec.StringEncoder"),String.class,String.class};args=new Object[]{encoder,s1,s2};
        } else {
            owner=Class.forName("org.apache.commons.codec.language.Metaphone");receiver=owner.getDeclaredConstructor().newInstance();
            check(Integer.valueOf(4).equals(call(receiver,"getMaxCodeLen",new Class<?>[]{})),
                "Exact receiver initial state");
            check(buffer!=null,"Bounded helpers require real non-null StringBuffer");
            if(method.equals("isVowel")){types=new Class<?>[]{StringBuffer.class,int.class};args=new Object[]{buffer,index};}
            else if(method.equals("regionMatch")){types=new Class<?>[]{StringBuffer.class,int.class,String.class};args=new Object[]{buffer,index,needle};}
            else {types=new Class<?>[]{StringBuffer.class,int.class,char.class};args=new Object[]{buffer,index,ch};}
        }
        check(receiver.getClass()==owner,"Exact production receiver identity");
        Method target=owner.getDeclaredMethod(method,types);target.setAccessible(true);
        check(target.getDeclaringClass()==owner,"Exact declaring class");
        check(Modifier.isStatic(target.getModifiers())==method.equals("difference"),"Static/instance identity");
        String before=buffer==null?null:buffer.toString();int capacity=buffer==null?0:buffer.capacity();
        Object value=null;Throwable thrown=null;
        activeCase=name; INVOKED.set(true);
        try{value=target.invoke(receiver,args);}catch(InvocationTargetException error){thrown=error.getCause();}
        finally{activeCase="";}
        if(thrown instanceof VirtualMachineError || thrown instanceof LinkageError || thrown instanceof ThreadDeath)
            throw new FixtureFailure("SQA_HARNESS Codec target environment failure",thrown);
        boolean unchanged=buffer==null||(buffer.toString().equals(before)&&buffer.length()==before.length()&&buffer.capacity()==capacity);
        Object receiverMax=receiver.getClass().getName().endsWith("Metaphone")?call(receiver,"getMaxCodeLen",new Class<?>[]{}):null;
        Object encoderMax=encoder==null?null:call(encoder,"getMaxCodeLen",new Class<?>[]{});
        Map<String,Object> actual=map("value",value,"exception_class",thrown==null?null:thrown.getClass().getName(),
            "buffer_contents",buffer==null?null:buffer.toString(),"buffer_length",buffer==null?null:buffer.length(),
            "buffer_unchanged",unchanged,"receiver_max_code_len",receiverMax,"encoder_max_code_len",encoderMax,
            "encoder_encoded_inputs",encoded);
        Map<String,Object> expected=map("value",expectedValue,"exception_class",expectedException,
            "buffer_contents",text,"buffer_length",text==null?null:text.length(),"buffer_unchanged",true,
            "receiver_max_code_len",method.equals("difference")?null:4,"encoder_max_code_len",method.equals("difference")?4:null,
            "encoder_encoded_inputs",method.equals("difference")?Arrays.asList(string(c[10]),string(c[11])):null);
        boolean passed=json(actual).equals(json(expected));
        return map("case",name,"method",method,"receiver_class",receiver.getClass().getName(),
            "declaring_class",target.getDeclaringClass().getName(),"descriptor",descriptor(target),
            "setup_succeeded",true,"target_invoked",true,"target_check_passed",passed,
            "failure_class",passed?null:"java.lang.AssertionError","failure_reason",passed?null:"Declared value/state oracle differs",
            "observation",actual,"expected_observation",expected,
            "pre_state",map("buffer_contents",before,"buffer_length",before==null?null:before.length(),"buffer_capacity",buffer==null?null:capacity),
            "post_state",map("buffer_contents",buffer==null?null:buffer.toString(),"buffer_length",buffer==null?null:buffer.length(),"buffer_capacity",buffer==null?null:buffer.capacity()));
    }
    public static Map<String,Object> lastEvidence=null;
    private static final String[][] CASES={
        {"isNextChar_match_first", "isNextChar", "QUJDQQ==", "0", "66", "", "-", "-", "true", "-", "-", "-"},
        {"isNextChar_match_middle", "isNextChar", "QUJDQQ==", "1", "67", "", "-", "-", "true", "-", "-", "-"},
        {"isNextChar_mismatch", "isNextChar", "QUJDQQ==", "0", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isNextChar_negative", "isNextChar", "QUJDQQ==", "-1", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isNextChar_last", "isNextChar", "QUJDQQ==", "3", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isNextChar_at_length", "isNextChar", "QUJDQQ==", "4", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isNextChar_empty", "isNextChar", "", "0", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isNextChar_single", "isNextChar", "QQ==", "0", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isPreviousChar_match_first", "isPreviousChar", "QUJDQQ==", "1", "65", "", "-", "-", "true", "-", "-", "-"},
        {"isPreviousChar_match_last", "isPreviousChar", "QUJDQQ==", "3", "67", "", "-", "-", "true", "-", "-", "-"},
        {"isPreviousChar_mismatch", "isPreviousChar", "QUJDQQ==", "1", "67", "", "-", "-", "false", "-", "-", "-"},
        {"isPreviousChar_negative", "isPreviousChar", "QUJDQQ==", "-1", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isPreviousChar_first", "isPreviousChar", "QUJDQQ==", "0", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isPreviousChar_at_length", "isPreviousChar", "QUJDQQ==", "4", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isPreviousChar_empty", "isPreviousChar", "", "0", "65", "", "-", "-", "false", "-", "-", "-"},
        {"isPreviousChar_single", "isPreviousChar", "QQ==", "0", "65", "", "-", "-", "false", "-", "-", "-"},
        {"vowel_A", "isVowel", "QUVJT1VC", "0", "65", "", "-", "-", "true", "-", "-", "-"},
        {"vowel_E", "isVowel", "QUVJT1VC", "1", "65", "", "-", "-", "true", "-", "-", "-"},
        {"vowel_I", "isVowel", "QUVJT1VC", "2", "65", "", "-", "-", "true", "-", "-", "-"},
        {"vowel_O", "isVowel", "QUVJT1VC", "3", "65", "", "-", "-", "true", "-", "-", "-"},
        {"vowel_U", "isVowel", "QUVJT1VC", "4", "65", "", "-", "-", "true", "-", "-", "-"},
        {"vowel_B", "isVowel", "QUVJT1VC", "5", "65", "", "-", "-", "false", "-", "-", "-"},
        {"vowel_empty", "isVowel", "", "0", "65", "", "-", "-", "null", "java.lang.StringIndexOutOfBoundsException", "-", "-"},
        {"vowel_negative", "isVowel", "QQ==", "-1", "65", "", "-", "-", "null", "java.lang.StringIndexOutOfBoundsException", "-", "-"},
        {"vowel_at_length", "isVowel", "QQ==", "1", "65", "", "-", "-", "null", "java.lang.StringIndexOutOfBoundsException", "-", "-"},
        {"region_prefix", "regionMatch", "QUJDQQ==", "0", "65", "QUI=", "-", "-", "true", "-", "-", "-"},
        {"region_middle", "regionMatch", "QUJDQQ==", "1", "65", "QkM=", "-", "-", "true", "-", "-", "-"},
        {"region_last", "regionMatch", "QUJDQQ==", "3", "65", "QQ==", "-", "-", "true", "-", "-", "-"},
        {"region_mismatch", "regionMatch", "QUJDQQ==", "1", "65", "QkE=", "-", "-", "false", "-", "-", "-"},
        {"region_too_long", "regionMatch", "QUJDQQ==", "3", "65", "QUI=", "-", "-", "false", "-", "-", "-"},
        {"region_negative", "regionMatch", "QUJDQQ==", "-1", "65", "QQ==", "-", "-", "false", "-", "-", "-"},
        {"region_empty_end", "regionMatch", "QUJDQQ==", "4", "65", "", "-", "-", "true", "-", "-", "-"},
        {"region_empty_buffer", "regionMatch", "", "0", "65", "", "-", "-", "true", "-", "-", "-"},
        {"region_empty_beyond_end", "regionMatch", "QUJDQQ==", "5", "65", "", "-", "-", "false", "-", "-", "-"},
        {"difference_equal", "difference", "-", "0", "65", "", "QQ==", "QQ==", "1", "-", "QQ==", "QQ=="},
        {"difference_case_fold", "difference", "-", "0", "65", "", "YQ==", "QQ==", "1", "-", "QQ==", "QQ=="},
        {"difference_different", "difference", "-", "0", "65", "", "QQ==", "RQ==", "0", "-", "QQ==", "RQ=="},
        {"difference_null_left", "difference", "-", "0", "65", "", "-", "QQ==", "0", "-", "", "QQ=="},
        {"difference_empty_both", "difference", "-", "0", "65", "", "", "", "0", "-", "", ""},
        {"difference_null_both", "difference", "-", "0", "65", "", "-", "-", "0", "-", "", ""},
        {"difference_two_equal", "difference", "-", "0", "65", "", "QUI=", "QUI=", "2", "-", "QUI=", "QUI="},
        {"difference_shorter_right", "difference", "-", "0", "65", "", "QUI=", "QQ==", "1", "-", "QUI=", "QQ=="},
        {"difference_two_mismatch", "difference", "-", "0", "65", "", "QUI=", "Qg==", "0", "-", "QUI=", "Qg=="}
    };
    public static synchronized String observe(String method, double[] vector) {
        INVOKED.set(false); lastEvidence=null; activeCase="";
        try {
            if(vector.length==0 || !Double.isFinite(vector[0]))throw new IllegalArgumentException("Codec needs finite vector");
            List<String[]> options=new ArrayList<String[]>();
            for(String[] row:CASES)if(row[1].equals(method))options.add(row);
            int slot=Math.min(options.size()-1,(int)Math.floor((Math.max(-1.0,Math.min(1.0,vector[0]))+1)*options.size()/2));
            lastEvidence=run(options.get(slot));
            return json(lastEvidence.get("observation"));
        } catch(FixtureFailure failure){throw failure;}
        catch(Throwable failure){throw new FixtureFailure("SQA_HARNESS Codec setup/projection failed",failure);}
        finally{activeCase="";}
    }
    }

    private static boolean chronologyIdentity(String ctor, String method, String params) {
        if (method.equals("<init>") && params.isEmpty()) return ctor.equals("org.joda.time.Chronology")
            || ctor.equals("org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology")
            || ctor.equals("[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology")
            || ctor.equals("org.joda.time.Chronology,[Lorg.joda.time.DateTimeFieldType;,[I");
        return ctor.isEmpty() && ((method.equals("getField") && params.equals("int,org.joda.time.Chronology"))
            || (method.equals("withChronologyRetainFields") && params.equals("org.joda.time.Chronology")));
    }

    private static String partialState(Object partial) throws ReflectiveOperationException {
        Object chrono = call(partial,"getChronology",new Class<?>[]{});
        Object zone = call(chrono,"getZone",new Class<?>[]{});
        int size = (Integer)call(partial,"size",new Class<?>[]{});
        List<String> names = new ArrayList<String>();
        List<Integer> values = new ArrayList<Integer>();
        boolean named = true;
        for (int i=0; i<size; i++) {
            Object type = call(partial,"getFieldType",new Class<?>[]{int.class},i);
            names.add((String)call(type,"getName",new Class<?>[]{}));
            Integer indexed = (Integer)call(partial,"getValue",new Class<?>[]{int.class},i);
            values.add(indexed);
            named &= indexed.equals(call(partial,"get",types("org.joda.time.DateTimeFieldType"),type));
        }
        return "partial:"+chrono.getClass().getName()+":"+call(zone,"getID",new Class<?>[]{})
            +":types="+names+":values="+values+":named="+named;
    }

    /** Six bounded production identities only, separate from all historical policies.
     * Vector[0] chooses a declared case, not arbitrary legal-domain approval.
     * Reflection enters the exact protected/internal target on real final Partial.
     */
    private static synchronized String observeChronology(String ctor, String method, String params, double[] vector) {
        INVOKED.set(false);
        chronologyActiveCase = "";
        try {
            if (vector.length==0 || !Double.isFinite(vector[0]))
                throw new IllegalArgumentException("Chronology needs a finite vector");
            System.setProperty("org.joda.time.DateTimeZone.Provider","org.joda.time.tz.UTCProvider");
            java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("UTC"));
            Class<?> partial = Class.forName("org.joda.time.Partial");
            Class<?> chronoType = Class.forName("org.joda.time.Chronology");
            Class<?> fieldType = Class.forName("org.joda.time.DateTimeFieldType");
            Class<?> zoneType = Class.forName("org.joda.time.DateTimeZone");
            Object provider = Class.forName("org.joda.time.tz.UTCProvider").getDeclaredConstructor().newInstance();
            call(zoneType,"setProvider",types("org.joda.time.tz.Provider"),provider);
            Object utc = zoneType.getField("UTC").get(null);
            call(zoneType,"setDefault",new Class<?>[]{zoneType},utc);
            Object offset = call(zoneType,"forOffsetHours",new Class<?>[]{int.class},7);
            Object iso = call(Class.forName("org.joda.time.chrono.ISOChronology"),"getInstance",new Class<?>[]{zoneType},utc);
            Object isoOffset = call(Class.forName("org.joda.time.chrono.ISOChronology"),"getInstance",new Class<?>[]{zoneType},offset);
            Object buddhist = call(Class.forName("org.joda.time.chrono.BuddhistChronology"),"getInstance",new Class<?>[]{zoneType},offset);
            Object year = call(fieldType,"year",new Class<?>[]{});
            Object month = call(fieldType,"monthOfYear",new Class<?>[]{});
            Object day = call(fieldType,"dayOfMonth",new Class<?>[]{});
            Object hour = call(fieldType,"hourOfDay",new Class<?>[]{});
            Object era = call(fieldType,"era",new Class<?>[]{});
            // All three bounded cases occupy intervals within the generators'
            // shared [-1,1] domain (including CMA-ES/FSCS-ART proposals).
            int bucket = Math.min(2, (int)Math.floor((Math.max(-1.0,Math.min(1.0,vector[0]))+1.0)*1.5));
            String caseName;
            Object receiver = null, result = null;
            Object[] args;
            Object inputTypes = null; int[] inputValues = null;
            String before = null;
            Constructor<?> constructor = null;
            Method targetMethod = null;
            if (method.equals("<init>")) {
                constructor = partial.getDeclaredConstructor(types(ctor));
                constructor.setAccessible(true);
                if (ctor.equals("org.joda.time.Chronology")) {
                    caseName = bucket==0 ? "empty_iso_offset" : "empty_null";
                    args = new Object[]{bucket==0 ? isoOffset : null};
                } else if (ctor.equals("org.joda.time.DateTimeFieldType,int,org.joda.time.Chronology")) {
                    caseName = bucket==0 ? "single_hour_iso" : "single_invalid_hour";
                    args = new Object[]{hour,bucket==0 ? 10 : 24,isoOffset};
                } else {
                    boolean internal = ctor.startsWith("org.joda.time.Chronology,");
                    caseName = internal ? "internal_iso" : bucket==0 ? "arrays_leap_iso"
                        : bucket==1 ? "arrays_invalid_date" : "arrays_bad_order";
                    inputTypes = Array.newInstance(fieldType,3);
                    Object[] chosen = !internal && bucket==2 ? new Object[]{year,day,era} : new Object[]{year,month,day};
                    for (int i=0;i<3;i++) Array.set(inputTypes,i,chosen[i]);
                    inputValues = !internal && bucket==2 ? new int[]{1,1,1} : new int[]{2024,2,!internal && bucket==1 ? 30 : 29};
                    if (internal) {
                        Object validated = partial.getDeclaredConstructor(types("[Lorg.joda.time.DateTimeFieldType;,[I,org.joda.time.Chronology"))
                            .newInstance(inputTypes,inputValues,iso);
                        inputTypes = call(validated,"getFieldTypes",new Class<?>[]{});
                        inputValues = (int[])call(validated,"getValues",new Class<?>[]{});
                        args = new Object[]{iso,inputTypes,inputValues};
                    } else args = new Object[]{inputTypes,inputValues,isoOffset};
                }
            } else {
                receiver = partial.getDeclaredConstructor().newInstance();
                boolean field = method.equals("getField");
                receiver = call(receiver,"with",new Class<?>[]{fieldType,int.class},field ? year : hour,field ? 2024 : 10);
                if (!field && bucket==2) receiver = call(receiver,"withChronologyRetainFields",new Class<?>[]{chronoType},buddhist);
                before = partialState(receiver);
                String expected = "partial:org.joda.time.chrono."+(!field && bucket==2 ? "BuddhistChronology" : "ISOChronology")
                    +":UTC:types=["+(field ? "year" : "hourOfDay")+"]:values=["+(field ? 2024 : 10)+"]:named=true";
                if (!before.equals(expected)) throw new IllegalStateException("Default receiver seed differs");
                targetMethod = partial.getDeclaredMethod(method,types(params));
                targetMethod.setAccessible(true);
                if (field) {
                    caseName = bucket==0 ? "getfield_buddhist" : "getfield_bad_index";
                    args = new Object[]{bucket==0 ? 0 : 1,buddhist};
                } else {
                    caseName = bucket==0 ? "withchrono_buddhist" : bucket==1 ? "withchrono_same" : "withchrono_null";
                    args = new Object[]{bucket==0 ? buddhist : bucket==1 ? isoOffset : null};
                }
            }
            Throwable targetException = null;
            chronologyActiveCase = caseName;
            INVOKED.set(true);
            try { result = constructor!=null ? constructor.newInstance(args) : targetMethod.invoke(receiver,args); }
            catch (InvocationTargetException failure) { targetException = failure.getCause(); }
            finally { chronologyActiveCase = ""; }
            if (targetException!=null) {
                if (targetException instanceof VirtualMachineError || targetException instanceof LinkageError || targetException instanceof ThreadDeath)
                    throw new IllegalStateException("SQA_HARNESS JVM failure",targetException);
                String out = "exception:"+targetException.getClass().getName();
                if (receiver!=null) out += "|receiver="+partialState(receiver)+":unchanged="+before.equals(partialState(receiver));
                return out;
            }
            if (method.equals("getField")) return "field:"+call(result,"getName",new Class<?>[]{})
                +":epoch="+call(result,"get",new Class<?>[]{long.class},0L)
                +":supplied-identity="+(result==call(buddhist,"year",new Class<?>[]{}))
                +":type-year="+(call(result,"getType",new Class<?>[]{})==year)
                +"|receiver="+partialState(receiver)+":unchanged="+before.equals(partialState(receiver));
            String out = partialState(result);
            if (method.equals("withChronologyRetainFields"))
                return out+":same="+(result==receiver)+"|receiver="+partialState(receiver)+":unchanged="+before.equals(partialState(receiver));
            if (caseName.equals("arrays_leap_iso")) {
                Array.set(inputTypes,0,hour); inputValues[0]=1900;
                boolean inputCopy = out.equals(partialState(result));
                Object getterTypes = call(result,"getFieldTypes",new Class<?>[]{});
                int[] getterValues = (int[])call(result,"getValues",new Class<?>[]{});
                Array.set(getterTypes,0,hour); getterValues[0]=1900;
                out += ":input-copy="+inputCopy+":output-copy="+out.equals(partialState(result));
            }
            return out;
        } catch (ReflectiveOperationException | RuntimeException failure) {
            throw new FixtureFailure("SQA_HARNESS Chronology setup/projection failed",failure);
        } finally { chronologyActiveCase = ""; }
    }

    public static boolean targetInvoked() { return Boolean.TRUE.equals(INVOKED.get()); }

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
        if ((args.length != 6 && args.length != 7) || !args[0].equals("observe"))
            throw new IllegalArgumentException("SQA_HARNESS expected discover classes or observe class ctor method types vector");
        String[] pieces = args[5].split(",");
        double[] vector = new double[pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            vector[i] = Double.parseDouble(pieces[i]);
            if (!Double.isFinite(vector[i]))
                throw new IllegalArgumentException("SQA_HARNESS nonfinite vector");
        }
        String outcome;
        try {
            outcome = args.length == 7 ? observeWithPolicy(args[1], args[2], args[3], args[4], vector, args[6])
                : observe(args[1], args[2], args[3], args[4], vector);
        } catch (FixtureFailure failure) {
            System.out.println("SQA_FIXTURE_FAILURE:" + Base64.getEncoder().encodeToString(failure.getMessage().getBytes(StandardCharsets.UTF_8)));
            return;
        }
        System.out.println("SQA_TRACE:{\"target_invoked\":" + Boolean.TRUE.equals(INVOKED.get()) + "}");
        System.out.println("SQA_RESULT:" + Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)));
    }
    private static boolean graphicsIdentity(String cls, String ctor, String method, String params) {
        if (!cls.equals("org.jfree.chart.renderer.category.AreaRenderer") || !ctor.isEmpty()) return false;
        if (method.equals("drawAnnotations")) return params.equals("java.awt.Graphics2D,java.awt.geom.Rectangle2D,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.axis.ValueAxis,org.jfree.chart.util.Layer,org.jfree.chart.plot.PlotRenderingInfo");
        if (method.equals("drawBackground")) return params.equals("java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D");
        if (method.equals("drawDomainLine")) return params.equals("java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D,double,java.awt.Paint,java.awt.Stroke");
        if (method.equals("drawDomainMarker")) return params.equals("java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.plot.CategoryMarker,java.awt.geom.Rectangle2D");
        if (method.equals("drawOutline")) return params.equals("java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D");
        if (method.equals("drawRangeMarker")) return params.equals("java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,org.jfree.chart.axis.ValueAxis,org.jfree.chart.plot.Marker,java.awt.geom.Rectangle2D");
        if (method.equals("initialise")) return params.equals("java.awt.Graphics2D,java.awt.geom.Rectangle2D,org.jfree.chart.plot.CategoryPlot,org.jfree.data.category.CategoryDataset,org.jfree.chart.plot.PlotRenderingInfo");
        return false;
    }
    private static synchronized String observeGraphics(String method, double[] vector) {
        INVOKED.set(Boolean.FALSE); GraphicsRecipe.lastEvidence = null;
        try {
            if (vector.length == 0 || !Double.isFinite(vector[0])) throw new IllegalArgumentException("Finite fixture selector required");
            java.util.List<String> cases = new ArrayList<String>();
            for (String name : GraphicsRecipe.CASES) {
                String group = name.startsWith("annotations_") ? "drawAnnotations" : name.startsWith("background_") ? "drawBackground" :
                    name.startsWith("domain_line_") ? "drawDomainLine" : name.startsWith("domain_marker_") ? "drawDomainMarker" :
                    name.startsWith("outline_") ? "drawOutline" : name.startsWith("range_") ? "drawRangeMarker" : "initialise";
                if (group.equals(method)) cases.add(name);
            }
            String name = cases.get(bucket(vector[0], cases.size()));
            return GraphicsRecipe.json(GraphicsRecipe.run(name, null).get("observation"));
        } catch (Throwable failure) {
            // Target exceptions are already observations; every escaping error belongs to setup/projection.
            throw new FixtureFailure("SQA_HARNESS Graphics setup/projection failed", failure);
        } finally { GraphicsRecipe.activeCase = ""; }
    }
public static final class GraphicsRecipe {
    public static String activeCase = "";
    public static Map<String,Object> lastEvidence = null;
    private static final String OWNER = "org.jfree.chart.renderer.category.AbstractCategoryItemRenderer";
    private static final String[] CASES = {
        "annotations_fg_v", "annotations_fg_h", "annotations_bg_v", "annotations_bg_h",
        "background_v", "background_h", "domain_line_v", "domain_line_h",
        "domain_line_null_paint", "domain_line_null_stroke",
        "domain_marker_line_v", "domain_marker_line_h", "domain_marker_band_v", "domain_marker_band_h",
        "domain_marker_missing", "outline_enabled", "outline_disabled",
        "range_value_v", "range_value_h", "range_interval_v", "range_interval_h", "range_outside",
        "initialise_dataset", "initialise_null_dataset"
    };
    private static String quote(String text) {
        return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
    public static String json(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return quote((String) value);
        if (value instanceof Map) {
            StringBuilder out = new StringBuilder("{");
            for (Object object : ((Map) value).entrySet()) {
                Map.Entry item = (Map.Entry) object;
                if (out.length() > 1) out.append(',');
                out.append(quote((String) item.getKey())).append(':').append(json(item.getValue()));
            }
            return out.append('}').toString();
        }
        if (value instanceof java.util.List) {
            StringBuilder out = new StringBuilder("[");
            for (Object item : (java.util.List) value) {
                if (out.length() > 1) out.append(',');
                out.append(json(item));
            }
            return out.append(']').toString();
        }
        return String.valueOf(value);
    }
    private static Map<String,Object> map(Object... pairs) {
        Map<String,Object> result = new LinkedHashMap<String,Object>();
        for (int i = 0; i < pairs.length; i += 2) result.put((String) pairs[i], pairs[i+1]);
        return result;
    }
    private static void check(boolean state, String reason) {
        if (!state) throw new AssertionError(reason);
    }
    private static BufferedImage canvas() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try { g.setColor(Color.WHITE); g.fillRect(0, 0, 64, 64); }
        finally { g.dispose(); }
        return image;
    }
    private static Graphics2D graphics(BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setPaint(Color.BLACK); g.setStroke(new BasicStroke(1.0f));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        return g;
    }
    private static Map<String,Object> state(Graphics2D g) {
        return map("paint_rgb", ((Color) g.getPaint()).getRGB(), "stroke_width", ((BasicStroke) g.getStroke()).getLineWidth(),
                "composite_rule", ((AlphaComposite) g.getComposite()).getRule(),
                "composite_alpha", ((AlphaComposite) g.getComposite()).getAlpha(),
                "identity_transform", g.getTransform().isIdentity(), "clip_null", g.getClip() == null);
    }
    private static byte[] pixels(BufferedImage image) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        for (int y = 0; y < 64; y++) for (int x = 0; x < 64; x++) out.writeInt(image.getRGB(x, y));
        out.close(); return bytes.toByteArray();
    }
    private static String sha(byte[] raw) throws Exception {
        StringBuilder text = new StringBuilder();
        for (byte b : MessageDigest.getInstance("SHA-256").digest(raw)) text.append(String.format("%02x", b & 255));
        return text.toString();
    }
    private static void save(File root, String name, byte[] bytes) throws IOException {
        File file = new File(root, name);
        if (!file.createNewFile()) throw new IOException("Refuse to overwrite pixel evidence");
        try (FileOutputStream out = new FileOutputStream(file)) { out.write(bytes); }
    }
    private static final class Fixture implements AutoCloseable {
        final Object renderer = make("org.jfree.chart.renderer.category.AreaRenderer", "");
        final Object dataset = make("org.jfree.data.category.DefaultCategoryDataset", "");
        final Object domain = make("org.jfree.chart.axis.CategoryAxis", "java.lang.String", "Domain");
        final Object range = make("org.jfree.chart.axis.NumberAxis", "java.lang.String", "Range");
        final Rectangle2D area = new Rectangle2D.Double(10, 10, 40, 40);
        final BufferedImage actual = canvas(), reference = canvas();
        final Graphics2D g = graphics(actual), ref = graphics(reference);
        final Object plot;
        Fixture(boolean horizontal) throws Exception {
            invoke(dataset, "addValue", "double,java.lang.Comparable,java.lang.Comparable", 2.0, "r1", "A"); invoke(dataset, "addValue", "double,java.lang.Comparable,java.lang.Comparable", 8.0, "r1", "B");
            invoke(dataset, "addValue", "double,java.lang.Comparable,java.lang.Comparable", 4.0, "r2", "A"); invoke(dataset, "addValue", "double,java.lang.Comparable,java.lang.Comparable", 6.0, "r2", "B");
            invoke(domain, "setLowerMargin", "double", 0); invoke(domain, "setUpperMargin", "double", 0); invoke(domain, "setCategoryMargin", "double", 0);
            invoke(range, "setAutoRange", "boolean", false); invoke(range, "setRange", "double,double", 0, 10); invoke(range, "setInverted", "boolean", false);
            plot = make("org.jfree.chart.plot.CategoryPlot", "org.jfree.data.category.CategoryDataset,org.jfree.chart.axis.CategoryAxis,org.jfree.chart.axis.ValueAxis,org.jfree.chart.renderer.category.CategoryItemRenderer", dataset, domain, range, renderer);
            invoke(plot, "setOrientation", "org.jfree.chart.plot.PlotOrientation", horizontal ? constant("org.jfree.chart.plot.PlotOrientation", "HORIZONTAL") : constant("org.jfree.chart.plot.PlotOrientation", "VERTICAL"));
            invoke(plot, "setBackgroundPaint", "java.awt.Paint", Color.YELLOW); invoke(plot, "setBackgroundAlpha", "float", 1.0f); invoke(plot, "setBackgroundImage", "java.awt.Image", (Object)null);
            invoke(plot, "setOutlinePaint", "java.awt.Paint", Color.BLUE); invoke(plot, "setOutlineStroke", "java.awt.Stroke", new BasicStroke(2.0f));
            invoke(plot, "setOutlineVisible", "boolean", true);
            check(invoke(plot, "getRenderer", "") == renderer && invoke(renderer, "getPlot", "") == plot, "Production receiver binding");
        }
        public void close() { g.dispose(); ref.dispose(); }
    }
    private static void line(Graphics2D g, boolean horizontal, double value, Color color, float width) {
        g.setPaint(color); g.setStroke(new BasicStroke(width));
        g.draw(horizontal ? new Line2D.Double(10, value, 50, value) : new Line2D.Double(value, 10, value, 50));
    }
    public static Map<String,Object> run(String name, File images) throws Exception {
        boolean horizontal = name.endsWith("_h");
        try (Fixture f = new Fixture(horizontal)) {
            String method; Class<?>[] types; Object[] args;
            String expectedException = null, expectedMessage = null;
            Object expectedReturn = null;
            int rows = 0, columns = 0;
            boolean plotBound = true;
            if (name.startsWith("annotations_")) {
                method = "drawAnnotations";
                types = new Class<?>[]{Graphics2D.class, Rectangle2D.class, Class.forName("org.jfree.chart.axis.CategoryAxis"), Class.forName("org.jfree.chart.axis.ValueAxis"), Class.forName("org.jfree.chart.util.Layer"), Class.forName("org.jfree.chart.plot.PlotRenderingInfo")};
                boolean foreground = name.contains("_fg_");
                invoke(f.renderer, "addAnnotation", "org.jfree.chart.annotations.CategoryAnnotation,org.jfree.chart.util.Layer", make("org.jfree.chart.annotations.CategoryLineAnnotation", "java.lang.Comparable,double,java.lang.Comparable,double,java.awt.Paint,java.awt.Stroke", "A", 2, "B", 8, Color.RED, new BasicStroke(1.0f)), constant("org.jfree.chart.util.Layer", "FOREGROUND"));
                invoke(f.renderer, "addAnnotation", "org.jfree.chart.annotations.CategoryAnnotation,org.jfree.chart.util.Layer", make("org.jfree.chart.annotations.CategoryLineAnnotation", "java.lang.Comparable,double,java.lang.Comparable,double,java.awt.Paint,java.awt.Stroke", "A", 8, "B", 2, Color.GREEN, new BasicStroke(1.0f)), constant("org.jfree.chart.util.Layer", "BACKGROUND"));
                args = new Object[]{f.g, f.area, f.domain, f.range, foreground ? constant("org.jfree.chart.util.Layer", "FOREGROUND") : constant("org.jfree.chart.util.Layer", "BACKGROUND"), null};
                f.ref.setPaint(foreground ? Color.RED : Color.GREEN); f.ref.setStroke(new BasicStroke(1.0f));
                // Two categories with zero margins have middle coordinates 20 and 40.
                // Range [0,10] maps value v to 50-4v vertically, or 10+4v horizontally.
                int a = foreground ? 2 : 8, b = foreground ? 8 : 2;
                if (horizontal) f.ref.drawLine(10+4*a, 20, 10+4*b, 40);
                else f.ref.drawLine(20, 50-4*a, 40, 50-4*b);
            } else if (name.startsWith("background_")) {
                method = "drawBackground"; types = new Class<?>[]{Graphics2D.class, Class.forName("org.jfree.chart.plot.CategoryPlot"), Rectangle2D.class};
                args = new Object[]{f.g, f.plot, f.area};
                f.ref.setComposite(AlphaComposite.SrcOver); f.ref.setPaint(Color.YELLOW); f.ref.fillRect(10, 10, 40, 40);
                f.ref.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            } else if (name.startsWith("domain_line_")) {
                method = "drawDomainLine";
                types = new Class<?>[]{Graphics2D.class, Class.forName("org.jfree.chart.plot.CategoryPlot"), Rectangle2D.class, double.class, Paint.class, Stroke.class};
                Paint paint = name.endsWith("null_paint") ? null : Color.RED;
                Stroke stroke = name.endsWith("null_stroke") ? null : new BasicStroke(2.0f);
                args = new Object[]{f.g, f.plot, f.area, 24.0, paint, stroke};
                if (paint == null || stroke == null) {
                    expectedException = "java.lang.IllegalArgumentException";
                    expectedMessage = paint == null ? "Null 'paint' argument." : "Null 'stroke' argument.";
                } else line(f.ref, horizontal, 24, Color.RED, 2);
            } else if (name.startsWith("domain_marker_")) {
                method = "drawDomainMarker";
                types = new Class<?>[]{Graphics2D.class, Class.forName("org.jfree.chart.plot.CategoryPlot"), Class.forName("org.jfree.chart.axis.CategoryAxis"), Class.forName("org.jfree.chart.plot.CategoryMarker"), Rectangle2D.class};
                Object marker = make("org.jfree.chart.plot.CategoryMarker", "java.lang.Comparable,java.awt.Paint,java.awt.Stroke", name.endsWith("missing") ? "missing" : "A", Color.RED, new BasicStroke(2.0f));
                invoke(marker, "setAlpha", "float", 1.0f); invoke(marker, "setLabel", "java.lang.String", (Object)null); invoke(marker, "setDrawAsLine", "boolean", name.contains("_line_"));
                args = new Object[]{f.g, f.plot, f.domain, marker, f.area};
                if (!name.endsWith("missing")) {
                    f.ref.setComposite(AlphaComposite.SrcOver);
                    if (name.contains("_line_")) line(f.ref, horizontal, 20, Color.RED, 2);
                    else { f.ref.setPaint(Color.RED); f.ref.fillRect(10, 10, horizontal ? 40 : 20, horizontal ? 20 : 40); }
                    f.ref.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
            } else if (name.startsWith("outline_")) {
                method = "drawOutline"; types = new Class<?>[]{Graphics2D.class, Class.forName("org.jfree.chart.plot.CategoryPlot"), Rectangle2D.class};
                boolean visible = name.endsWith("enabled"); invoke(f.plot, "setOutlineVisible", "boolean", visible);
                args = new Object[]{f.g, f.plot, f.area};
                if (visible) { f.ref.setPaint(Color.BLUE); f.ref.setStroke(new BasicStroke(2.0f)); f.ref.drawRect(10, 10, 40, 40); }
            } else if (name.startsWith("range_")) {
                method = "drawRangeMarker";
                types = new Class<?>[]{Graphics2D.class, Class.forName("org.jfree.chart.plot.CategoryPlot"), Class.forName("org.jfree.chart.axis.ValueAxis"), Class.forName("org.jfree.chart.plot.Marker"), Rectangle2D.class};
                Object marker;
                if (name.contains("_interval_")) {
                    Object interval = make("org.jfree.chart.plot.IntervalMarker", "double,double,java.awt.Paint", 2, 8, Color.RED);
                    invoke(interval, "setOutlinePaint", "java.awt.Paint", (Object)null); invoke(interval, "setOutlineStroke", "java.awt.Stroke", (Object)null); marker = interval;
                } else marker = make("org.jfree.chart.plot.ValueMarker", "double,java.awt.Paint,java.awt.Stroke", name.endsWith("outside") ? 20 : 2, Color.RED, new BasicStroke(2.0f));
                invoke(marker, "setAlpha", "float", 1.0f); invoke(marker, "setLabel", "java.lang.String", (Object)null);
                args = new Object[]{f.g, f.plot, f.range, marker, f.area};
                if (!name.endsWith("outside")) {
                    f.ref.setComposite(AlphaComposite.SrcOver);
                    if (name.contains("_interval_")) {
                        f.ref.setPaint(Color.RED);
                        f.ref.fillRect(horizontal ? 18 : 10, horizontal ? 10 : 18, horizontal ? 24 : 40, horizontal ? 40 : 24);
                    } else line(f.ref, !horizontal, horizontal ? 18 : 42, Color.RED, 2);
                    f.ref.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
            } else {
                method = "initialise";
                types = new Class<?>[]{Graphics2D.class, Rectangle2D.class, Class.forName("org.jfree.chart.plot.CategoryPlot"), Class.forName("org.jfree.data.category.CategoryDataset"), Class.forName("org.jfree.chart.plot.PlotRenderingInfo")};
                boolean withDataset = name.equals("initialise_dataset");
                invoke(f.renderer, "setPlot", "org.jfree.chart.plot.CategoryPlot", make("org.jfree.chart.plot.CategoryPlot", ""));
                check(invoke(f.renderer, "getPlot", "") != f.plot, "Distinct legal production plot before initialise");
                args = new Object[]{f.g, f.area, f.plot, withDataset ? f.dataset : null, null};
                rows = columns = withDataset ? 2 : 0;
                expectedReturn = map("class", "org.jfree.chart.renderer.category.CategoryItemRendererState", "info_null", true,
                        "bar_width", 0.0, "selection_matches_dataset", withDataset, "selection_null", !withDataset);
            }
            Method target = f.renderer.getClass().getMethod(method, types);
            check(target.getDeclaringClass().getName().equals(OWNER), "Exact inherited declaration binding");
            check(f.renderer.getClass() == Class.forName("org.jfree.chart.renderer.category.AreaRenderer"), "Default AreaRenderer receiver identity");
            byte[] expectedPixels = pixels(f.reference);
            Map<String,Object> expected = map("pixel_sha256", sha(expectedPixels), "graphics", state(f.ref),
                    "rows", rows, "columns", columns, "plot_bound", plotBound,
                    "dataset", Arrays.asList(2.0, 8.0, 4.0, 6.0), "return_state", expectedReturn,
                    "exception", expectedException, "message", expectedMessage);
            Throwable thrown = null; Object result = null;
            activeCase = name; INVOKED.set(Boolean.TRUE);
            try { result = target.invoke(f.renderer, args); }
            catch (InvocationTargetException failure) { thrown = failure.getCause(); }
            finally { activeCase = ""; }
            if (thrown instanceof VirtualMachineError || thrown instanceof LinkageError || thrown instanceof ThreadDeath)
                throw new FixtureFailure("SQA_HARNESS Graphics target environment failure", thrown);
            Object returned = null;
            if (result != null && result.getClass().getName().equals("org.jfree.chart.renderer.category.CategoryItemRendererState")) {
                Object r = result;
                returned = map("class", r.getClass().getName(), "info_null", invoke(r, "getInfo", "") == null,
                        "bar_width", invoke(r, "getBarWidth", ""), "selection_matches_dataset", invoke(r, "getSelectionState", "") == f.dataset,
                        "selection_null", invoke(r, "getSelectionState", "") == null);
            }
            byte[] actualPixels = pixels(f.actual);
            Map<String,Object> actual = map("pixel_sha256", sha(actualPixels), "graphics", state(f.g),
                    "rows", invoke(f.renderer, "getRowCount", ""), "columns", invoke(f.renderer, "getColumnCount", ""), "plot_bound", invoke(f.renderer, "getPlot", "") == f.plot,
                    "dataset", Arrays.asList(invoke(f.dataset, "getValue", "int,int", 0,0), invoke(f.dataset, "getValue", "int,int", 0,1), invoke(f.dataset, "getValue", "int,int", 1,0), invoke(f.dataset, "getValue", "int,int", 1,1)),
                    "return_state", returned, "exception", thrown == null ? null : thrown.getClass().getName(),
                    "message", thrown == null ? null : thrown.getMessage());
            if (images != null) { save(images, name + ".actual.argb", actualPixels); save(images, name + ".reference.argb", expectedPixels); }
            AssertionError assertion = null;
            try { check(json(actual).equals(json(expected)) && Arrays.equals(actualPixels, expectedPixels), "Declared image/value/state oracle differs"); }
            catch (AssertionError failure) { assertion = failure; }
            boolean passed = assertion == null;
            lastEvidence = map("actual_argb_b64", Base64.getEncoder().encodeToString(actualPixels), "reference_argb_b64", Base64.getEncoder().encodeToString(expectedPixels), "case", name, "method", method, "receiver_class", f.renderer.getClass().getName(),
                    "declaring_class", target.getDeclaringClass().getName(), "setup_succeeded", true,
                    "target_invoked", true, "target_check_passed", passed,
                    "failure_class", passed ? null : assertion.getClass().getName(), "failure_reason", passed ? null : assertion.getMessage(),
                    "observation", actual, "expected_observation", expected);
            return lastEvidence;
        }
    }
    private static Object make(String name, String params, Object... args) throws Exception {
        return Class.forName(name).getConstructor(types(params)).newInstance(args);
    }
    private static Object invoke(Object receiver, String name, String params, Object... args) throws Exception {
        return call(receiver, name, types(params), args);
    }
    private static Object constant(String name, String field) throws Exception {
        return Class.forName(name).getField(field).get(null);
    }
    }
}
}
