package org.apache.commons.cli;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * Deterministic regression tests for {@link CommandLine} (JUnit 3.8.1 style).
 */
public class CommandLineGeneratedTest extends TestCase {

    private CommandLine cl;

    protected void setUp() throws Exception {
        super.setUp();
        cl = new CommandLine();
    }

    // ------------------------------------------------------------------
    // helpers
    // ------------------------------------------------------------------

    /**
     * Stores a value in an Option. The method used to do this is not part of
     * the supplied source, so it is located reflectively.
     */
    private static void addValue(Option option, String value) throws Exception {
        Method m;
        try {
            m = Option.class.getDeclaredMethod("addValueForProcessing",
                    new Class[] { String.class });
        } catch (NoSuchMethodException e) {
            m = Option.class.getDeclaredMethod("addValue",
                    new Class[] { String.class });
        }
        m.setAccessible(true);
        m.invoke(option, new Object[] { value });
    }

    /** Flag option without argument: -a */
    private static Option flag() {
        return new Option("a", "flag a");
    }

    /** Option with one argument and a long name: -f / --file, value "data.txt" */
    private static Option fileOption() throws Exception {
        Option o = new Option("f", "file", true, "file option");
        addValue(o, "data.txt");
        return o;
    }

    /** Option with argument but no value stored: -e */
    private static Option emptyArgOption() {
        return new Option("e", "empty", true, "option without stored value");
    }

    /** Option with two values: -m / --multi */
    private static Option multiOption() throws Exception {
        Option o = new Option("m", "multi", true, "multi-valued option");
        o.setArgs(2);
        addValue(o, "one");
        addValue(o, "two");
        return o;
    }

    // ------------------------------------------------------------------
    // hasOption(String) / hasOption(char)
    // ------------------------------------------------------------------

    public void testHasOptionOnEmptyCommandLineIsFalse() {
        assertFalse(cl.hasOption("a"));
        assertFalse(cl.hasOption('a'));
    }

    public void testHasOptionByShortName() {
        cl.addOption(flag());
        assertTrue(cl.hasOption("a"));
        assertFalse(cl.hasOption("b"));
    }

    public void testHasOptionByCharName() {
        cl.addOption(flag());
        assertTrue(cl.hasOption('a'));
        assertFalse(cl.hasOption('b'));
    }

    public void testHasOptionByLongName() throws Exception {
        cl.addOption(fileOption());
        assertTrue(cl.hasOption("file"));
        assertTrue(cl.hasOption("f"));
    }

    public void testHasOptionStripsLeadingHyphens() throws Exception {
        cl.addOption(fileOption());
        assertTrue(cl.hasOption("-f"));
        assertTrue(cl.hasOption("--file"));
        assertFalse(cl.hasOption("--missing"));
    }

    public void testHasOptionWithEmptyStringIsFalse() {
        cl.addOption(flag());
        assertFalse(cl.hasOption(""));
    }

    // ------------------------------------------------------------------
    // getOptionValue / getOptionValues
    // ------------------------------------------------------------------

    public void testGetOptionValueByShortAndLongName() throws Exception {
        cl.addOption(fileOption());
        assertEquals("data.txt", cl.getOptionValue("f"));
        assertEquals("data.txt", cl.getOptionValue("file"));
        assertEquals("data.txt", cl.getOptionValue("-f"));
        assertEquals("data.txt", cl.getOptionValue("--file"));
    }

    public void testGetOptionValueByChar() throws Exception {
        cl.addOption(fileOption());
        assertEquals("data.txt", cl.getOptionValue('f'));
        assertNull(cl.getOptionValue('x'));
    }

    public void testGetOptionValueForUnknownOptionIsNull() throws Exception {
        cl.addOption(fileOption());
        assertNull(cl.getOptionValue("unknown"));
    }

    public void testGetOptionValueForFlagWithoutArgumentIsNull() {
        cl.addOption(flag());
        assertNull(cl.getOptionValue("a"));
    }

    public void testGetOptionValueReturnsFirstOfMultipleValues() throws Exception {
        cl.addOption(multiOption());
        assertEquals("one", cl.getOptionValue("m"));
        assertEquals("one", cl.getOptionValue('m'));
    }

    public void testGetOptionValueWithDefaultStringName() throws Exception {
        cl.addOption(fileOption());
        cl.addOption(emptyArgOption());
        assertEquals("data.txt", cl.getOptionValue("f", "fallback"));
        assertEquals("fallback", cl.getOptionValue("unknown", "fallback"));
        assertEquals("fallback", cl.getOptionValue("e", "fallback"));
        assertNull(cl.getOptionValue("unknown", null));
    }

    public void testGetOptionValueWithDefaultCharName() throws Exception {
        cl.addOption(fileOption());
        assertEquals("data.txt", cl.getOptionValue('f', "fallback"));
        assertEquals("fallback", cl.getOptionValue('z', "fallback"));
        assertNull(cl.getOptionValue('z', null));
    }

    public void testGetOptionValuesReturnsAllValuesInOrder() throws Exception {
        cl.addOption(multiOption());
        String[] values = cl.getOptionValues("m");
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("one", values[0]);
        assertEquals("two", values[1]);

        String[] byLong = cl.getOptionValues("--multi");
        assertNotNull(byLong);
        assertEquals(2, byLong.length);
    }

    public void testGetOptionValuesByChar() throws Exception {
        cl.addOption(multiOption());
        String[] values = cl.getOptionValues('m');
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("two", values[1]);
    }

    public void testGetOptionValuesForUnknownOptionIsNull() {
        assertNull(cl.getOptionValues("nope"));
        assertNull(cl.getOptionValues('n'));
    }

    public void testGetOptionValuesForOptionWithoutValuesIsNull() {
        cl.addOption(flag());
        assertNull(cl.getOptionValues("a"));
    }

    // ------------------------------------------------------------------
    // getOptionObject
    // ------------------------------------------------------------------

    public void testGetOptionObjectStringType() throws Exception {
        Option o = new Option("s", "str", true, "string option");
        o.setType(String.class);
        addValue(o, "hello");
        cl.addOption(o);

        assertEquals("hello", cl.getOptionObject("s"));
        assertEquals("hello", cl.getOptionObject("str"));
        assertEquals("hello", cl.getOptionObject('s'));
    }

    public void testGetOptionObjectNumberType() throws Exception {
        Option o = new Option("n", "number", true, "number option");
        o.setType(Number.class);
        addValue(o, "42");
        cl.addOption(o);

        Object result = cl.getOptionObject("n");
        assertTrue(result instanceof Number);
        assertEquals(42, ((Number) result).intValue());

        Object byChar = cl.getOptionObject('n');
        assertTrue(byChar instanceof Number);
        assertEquals(42, ((Number) byChar).intValue());
    }

    public void testGetOptionObjectForUnknownOptionIsNull() {
        assertNull(cl.getOptionObject("unknown"));
        assertNull(cl.getOptionObject('u'));
    }

    public void testGetOptionObjectForOptionWithoutValueIsNull() {
        Option o = new Option("s", "str", true, "string option");
        o.setType(String.class);
        cl.addOption(o);
        assertNull(cl.getOptionObject("s"));
    }

    // ------------------------------------------------------------------
    // getArgs / getArgList / addArg
    // ------------------------------------------------------------------

    public void testGetArgsEmptyByDefault() {
        String[] args = cl.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
    }

    public void testAddArgPreservesOrderAndAllowsDuplicates() {
        cl.addArg("first");
        cl.addArg("second");
        cl.addArg("first");

        String[] args = cl.getArgs();
        assertEquals(3, args.length);
        assertEquals("first", args[0]);
        assertEquals("second", args[1]);
        assertEquals("first", args[2]);
    }

    public void testGetArgsReturnsIndependentCopy() {
        cl.addArg("x");
        String[] args = cl.getArgs();
        args[0] = "changed";
        assertEquals("x", cl.getArgs()[0]);
    }

    public void testGetArgListReflectsAddedArgs() {
        assertNotNull(cl.getArgList());
        assertTrue(cl.getArgList().isEmpty());

        cl.addArg("a1");
        cl.addArg("a2");

        List list = cl.getArgList();
        assertEquals(2, list.size());
        assertEquals("a1", list.get(0));
        assertEquals("a2", list.get(1));
        assertSame(list, cl.getArgList());
    }

    // ------------------------------------------------------------------
    // getOptions / iterator / addOption
    // ------------------------------------------------------------------

    public void testGetOptionsAndIteratorEmptyByDefault() {
        assertEquals(0, cl.getOptions().length);
        assertFalse(cl.iterator().hasNext());
    }

    public void testAddOptionAndGetOptions() throws Exception {
        Option a = flag();
        Option f = fileOption();
        cl.addOption(a);
        cl.addOption(f);

        List options = Arrays.asList(cl.getOptions());
        assertEquals(2, options.size());
        assertTrue(options.contains(a));
        assertTrue(options.contains(f));
    }

    public void testAddEqualOptionTwiceIsStoredOnce() {
        Option a1 =
