import java.lang.reflect.*;
import java.util.*;

/** Development controls, distinct from the generated scientific suites. */
public final class CliOracleRegression {
    static final String CLI = "org.apache.commons.cli.CommandLine";
    static final String OLD = "aom-beam-champ-graphics-fixtures-v12-development";
    static final String NEW = "aom-cli-unordered-options-v1-development";
    static int checks;
    static Object session(String method, String policy) throws Exception {
        Constructor<?> c = Class.forName("SqaProbe$FixtureSession").getDeclaredConstructor(String.class, String.class, String.class);
        c.setAccessible(true);
        return c.newInstance(CLI, method, policy);
    }
    static String project(Object s, Object value) throws Exception {
        Method m = s.getClass().getDeclaredMethod("projection", Object.class, int.class);
        m.setAccessible(true);
        return (String)m.invoke(s, value, 0);
    }
    static Object option(String key, String... values) throws Exception {
        Class<?> c = Class.forName("org.apache.commons.cli.Option");
        Object o = c.getConstructor(String.class, boolean.class, String.class).newInstance(key, true, "control");
        c.getMethod("setArgs", int.class).invoke(o, -2);
        Method add = c.getDeclaredMethod("addValue", String.class);
        add.setAccessible(true);
        for (String value : values) add.invoke(o, value);
        return o;
    }
    static Object array(Object... options) throws Exception {
        Object a = Array.newInstance(Class.forName("org.apache.commons.cli.Option"), options.length);
        for (int i = 0; i < options.length; i++) Array.set(a, i, options[i]);
        return a;
    }
    static void check(String name, boolean pass) {
        if (!pass) throw new AssertionError(name);
        checks++;
        System.out.println("PASS " + name);
    }
    static String argsState(String policy, String... args) throws Exception {
        Object s = session("getArgs", policy);
        Constructor<?> ctor = Class.forName(CLI).getDeclaredConstructor();
        ctor.setAccessible(true);
        Object receiver = ctor.newInstance();
        Method add = receiver.getClass().getDeclaredMethod("addArg", String.class);
        add.setAccessible(true);
        for (String arg : args) add.invoke(receiver, arg);
        Field f = s.getClass().getDeclaredField("receiver");
        f.setAccessible(true); f.set(s, receiver);
        Method state = s.getClass().getDeclaredMethod("state");
        state.setAccessible(true);
        return (String)state.invoke(s);
    }
    public static void main(String[] args) throws Exception {
        Object a = option("x", "alpha", "beta"), b = option("extra", "left");
        Object old = session("getOptions", OLD);
        check("legacy option array order retained", !project(old, array(a,b)).equals(project(old, array(b,a))));
        if (args[0].equals("base")) { System.out.println("SQA_CONTROLS=" + checks); return; }
        Object s = session("getOptions", NEW);
        check("option array permutation equivalent", project(s,array(a,b)).equals(project(s,array(b,a))));
        check("different option identity detected", !project(s,array(a)).equals(project(s,array(option("y","alpha","beta")))));
        check("different option value detected", !project(s,array(a)).equals(project(s,array(option("x","other","beta")))));
        check("per-option value order retained", !project(s,array(a)).equals(project(s,array(option("x","beta","alpha")))));
        check("duplicate multiplicity retained", !project(s,array(a,a)).equals(project(s,array(a))));
        check("empty and nonempty distinct", !project(s,array()).equals(project(s,array(a))));
        check("String array order retained", !project(s,new String[]{"a","b"}).equals(project(s,new String[]{"b","a"})));
        check("char array order retained", !project(s,new char[]{'a','b'}).equals(project(s,new char[]{'b','a'})));
        check("positional argument state order retained", !argsState(NEW,"a","b").equals(argsState(NEW,"b","a")));
        Object iterator = session("iterator", NEW);
        check("iterator permutation equivalent", project(iterator,Arrays.asList(a,b).iterator()).equals(project(iterator,Arrays.asList(b,a).iterator())));
        check("iterator value difference detected", !project(iterator,Arrays.asList(a).iterator()).equals(project(iterator,Arrays.asList(option("x","bad")).iterator())));
        try { project(iterator, Arrays.asList("not-an-option").iterator()); throw new AssertionError("non-Option accepted"); }
        catch (InvocationTargetException e) { check("non-Option iterator rejected", e.getCause().getClass().getName().contains("FixtureFailure")); }
        Object huge = Array.newInstance(Class.forName("org.apache.commons.cli.Option"),257);
        try { project(s,huge); throw new AssertionError("oversize accepted"); }
        catch (InvocationTargetException e) { check("oversized options rejected", e.getCause().getClass().getName().contains("FixtureFailure")); }
        try { SqaProbe.observeWithPolicy("java.lang.String", "", "length", "", new double[]{0.1,0.2,0.3}, NEW); throw new AssertionError("non-Cli accepted"); }
        catch (IllegalArgumentException e) { check("new policy rejects non-Cli class", true); }
        System.out.println("SQA_CONTROLS=" + checks);
    }
}
