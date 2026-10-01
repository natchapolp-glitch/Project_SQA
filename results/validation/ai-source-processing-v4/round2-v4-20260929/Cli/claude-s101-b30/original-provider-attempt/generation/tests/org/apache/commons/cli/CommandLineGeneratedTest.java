package org.apache.commons.cli;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class CommandLineGeneratedTest extends TestCase {

    private CommandLine cl;

    protected void setUp() throws Exception {
        super.setUp();
        cl = new CommandLine();
    }

    private static Option optWithValue(String opt, String longOpt, String value) {
        Option o = new Option(opt, longOpt, true, "desc");
        o.addValueForProcessing(value);
        return o;
    }

    // ---------------- hasOption ----------------

    public void testHasOptionStringOnEmptyCommandLine() {
        assertFalse(cl.hasOption("a"));
    }

    public void testHasOptionStringShortName() {
        cl.addOption(new Option("a", "alpha", false, "desc"));
        assertTrue(cl.hasOption("a"));
        assertFalse(cl.hasOption("b"));
    }

    public void testHasOptionStringLongName() {
        cl.addOption(new Option("a", "alpha", false, "desc"));
        assertTrue(cl.hasOption("alpha"));
        assertFalse(cl.hasOption("beta"));
    }

    public void testHasOptionStringWithLeadingHyphens() {
        cl.addOption(new Option("a", "alpha", false, "desc"));
        assertTrue(cl.hasOption("-a"));
        assertTrue(cl.hasOption("--alpha"));
        assertFalse(cl.hasOption("-b"));
    }

    public void testHasOptionChar() {
        cl.addOption(new Option("a", "alpha", false, "desc"));
        assertTrue(cl.hasOption('a'));
        assertFalse(cl.hasOption('b'));
    }

    public void testHasOptionCharOnEmptyCommandLine() {
        assertFalse(cl.hasOption('x'));
    }

    // ---------------- getOptionValue ----------------

    public void testGetOptionValueStringUnknownOption() {
        assertNull(cl.getOptionValue("a"));
    }

    public void testGetOptionValueStringWithArgument() {
        cl.addOption(optWithValue("f", "file", "foo.txt"));
        assertEquals("foo.txt", cl.getOptionValue("f"));
        assertEquals("foo.txt", cl.getOptionValue("file"));
        assertEquals("foo.txt", cl.getOptionValue("-f"));
        assertEquals("foo.txt", cl.getOptionValue("--file"));
    }

    public void testGetOptionValueStringOptionWithoutArgument() {
        cl.addOption(new Option("v", "verbose", false, "desc"));
        assertTrue(cl.hasOption("v"));
        assertNull(cl.getOptionValue("v"));
    }

    public void testGetOptionValueStringArgumentOptionWithNoValuesAdded() {
        cl.addOption(new Option("f", "file", true, "desc"));
        assertTrue(cl.hasOption("f"));
        assertNull(cl.getOptionValue("f"));
    }

    public void testGetOptionValueStringReturnsFirstOfMultipleValues() {
        Option o = new Option("d", "define", true, "desc");
        o.setArgs(2);
        o.addValueForProcessing("first");
        o.addValueForProcessing("second");
        cl.addOption(o);
        assertEquals("first", cl.getOptionValue("d"));
    }

    public void testGetOptionValueStringOptionNotAddedToCommandLine() {
        Option o = optWithValue("f", "file", "foo.txt");
        assertNotNull(o);
        assertNull(cl.getOptionValue("f"));
    }

    public void testGetOptionValueChar() {
        cl.addOption(optWithValue("f", "file", "foo.txt"));
        assertEquals("foo.txt", cl.getOptionValue('f'));
        assertNull(cl.getOptionValue('x'));
    }

    public void testGetOptionValueStringWithDefaultOptionPresent() {
        cl.addOption(optWithValue("f", "file", "foo.txt"));
        assertEquals("foo.txt", cl.getOptionValue("f", "default"));
    }

    public void testGetOptionValueStringWithDefaultOptionAbsent() {
        assertEquals("default", cl.getOptionValue("f", "default"));
    }

    public void testGetOptionValueStringWithNullDefaultOptionAbsent() {
        assertNull(cl.getOptionValue("f", null));
    }

    public void testGetOptionValueStringWithDefaultOptionWithoutArgument() {
        cl.addOption(new Option("v", "verbose", false, "desc"));
        assertEquals("default", cl.getOptionValue("v", "default"));
    }

    public void testGetOptionValueCharWithDefault() {
        cl.addOption(optWithValue("f", "file", "foo.txt"));
        assertEquals("foo.txt", cl.getOptionValue('f', "default"));
        assertEquals("default", cl.getOptionValue('x', "default"));
    }

    // ---------------- getOptionValues ----------------

    public void testGetOptionValuesStringUnknownOption() {
        assertNull(cl.getOptionValues("a"));
    }

    public void testGetOptionValuesStringMultipleValues() {
        Option o = new Option("d", "define", true, "desc");
        o.setArgs(2);
        o.addValueForProcessing("one");
        o.addValueForProcessing("two");
        cl.addOption(o);

        String[] values = cl.getOptionValues("d");
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("one", values[0]);
        assertEquals("two", values[1]);

        String[] byLong = cl.getOptionValues("--define");
        assertNotNull(byLong);
        assertEquals(2, byLong.length);
    }

    public void testGetOptionValuesStringOptionWithoutValues() {
        cl.addOption(new Option("v", "verbose", false, "desc"));
        assertNull(cl.getOptionValues("v"));
    }

    public void testGetOptionValuesChar() {
        cl.addOption(optWithValue("f", "file", "foo.txt"));
        String[] values = cl.getOptionValues('f');
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("foo.txt", values[0]);
        assertNull(cl.getOptionValues('z'));
    }

    // ---------------- getOptionObject ----------------

    public void testGetOptionObjectStringUnknownOption() {
        assertNull(cl.getOptionObject("n"));
    }

    public void testGetOptionObjectStringDefaultTypeReturnsString() {
        cl.addOption(optWithValue("f", "file", "foo"));
        assertEquals("foo", cl.getOptionObject("f"));
    }

    public void testGetOptionObjectStringNumberType() {
        Option o = new Option("n", "number", true, "desc");
        o.setType(Number.class);
        o.addValueForProcessing("42");
        cl.addOption(o);

        Object result = cl.getOptionObject("n");
        assertTrue(result instanceof Number);
        assertEquals(42, ((Number) result).intValue());
    }

    public void testGetOptionObjectStringOptionWithoutValue() {
        cl.addOption(new Option("f", "file", true, "desc"));
        assertNull(cl.getOptionObject("f"));
    }

    public void testGetOptionObjectChar() {
        cl.addOption(optWithValue("f", "file", "foo"));
        assertEquals("foo", cl.getOptionObject('f'));
        assertNull(cl.getOptionObject('q'));
    }

    // ---------------- args ----------------

    public void testGetArgsEmpty() {
        String[] args = cl.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
    }

    public void testAddArgAndGetArgsPreserveOrder() {
        cl.addArg("one");
        cl.addArg("two");
        cl.addArg("three");
        String[] args = cl.getArgs();
        assertEquals(3, args.length);
        assertEquals("one", args[0]);
        assertEquals("two", args[1]);
        assertEquals("three", args[2]);
    }

    public void testGetArgsReturnsCopyOfInternalList() {
        cl.addArg("one");
        String[] args = cl.getArgs();
        args[0] = "changed";
        assertEquals("one", cl.getArgs()[0]);
    }

    

    // ---------------- options / iterator ----------------

    

    

    

    

    
}
