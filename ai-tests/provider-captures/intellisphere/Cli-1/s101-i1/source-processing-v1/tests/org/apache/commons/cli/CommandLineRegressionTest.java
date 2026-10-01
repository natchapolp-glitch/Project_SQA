package org.apache.commons.cli;

import junit.framework.TestCase;
import java.util.Iterator;
import java.util.List;

/**
 * Regression tests for {@link CommandLine} based on fixed-reference behavior.
 * Uses JUnit 3.8.1, following the project’s build configuration.
 */
public class CommandLineRegressionTest extends TestCase {

    private CommandLine cmd;
    private Option optFoo;
    private Option optBarLong;
    private Option optBazInt;

    protected void setUp() throws Exception {
        super.setUp();
        cmd = new CommandLine();

        // short option only, no argument
        optFoo = new Option("f", false, "Foo option");
        // short and long option, requires argument
        optBarLong = new Option("b", "bar", true, "Bar option with value");
        // short option with type conversion, requires argument
        optBazInt = new Option("z", true, "Baz integer option");
        optBazInt.setType(Integer.class);
    }

    // ---- hasOption(String) ----

    public void testHasOptionShort() {
        cmd.addOption(optFoo);
        assertTrue(cmd.hasOption("f"));
    }

    public void testHasOptionLong() {
        cmd.addOption(optBarLong);
        assertTrue(cmd.hasOption("bar"));
    }

    public void testHasOptionShortViaLong() {
        cmd.addOption(optBarLong);
        assertTrue(cmd.hasOption("b"));
    }

    public void testHasOptionNotPresent() {
        assertFalse(cmd.hasOption("x"));
    }

    public void testHasOptionWithLeadingHyphens() {
        cmd.addOption(optFoo);
        assertTrue(cmd.hasOption("--f"));
    }

    public void testHasOptionEmptyString() {
        assertFalse(cmd.hasOption(""));
    }

    // ---- hasOption(char) ----

    public void testHasOptionCharMatch() {
        cmd.addOption(optFoo);
        assertTrue(cmd.hasOption('f'));
    }

    public void testHasOptionCharNoMatch() {
        assertFalse(cmd.hasOption('x'));
    }

    public void testHasOptionCharLongNameOptionViaShort() {
        cmd.addOption(optBarLong);
        assertTrue(cmd.hasOption('b'));
        assertFalse(cmd.hasOption('a')); // 'b' only, not any char
    }

    // ---- getOptionValue(String) ----

    public void testGetOptionValuePresentNoArg() {
        cmd.addOption(optFoo); // no argument
        assertNull(cmd.getOptionValue("f"));
    }

    public void testGetOptionValuePresentWithArg() {
        optBarLong.addValue("hello");
        cmd.addOption(optBarLong);
        assertEquals("hello", cmd.getOptionValue("b"));
    }

    public void testGetOptionValueLongWithArg() {
        optBarLong.addValue("world");
        cmd.addOption(optBarLong);
        assertEquals("world", cmd.getOptionValue("bar"));
    }

    public void testGetOptionValueNotPresent() {
        assertNull(cmd.getOptionValue("x"));
    }

    

    // ---- getOptionValue(String, String) ----

    public void testGetOptionValueWithDefaultNotPresent() {
        assertEquals("default", cmd.getOptionValue("x", "default"));
    }

    public void testGetOptionValueWithDefaultPresentNoArg() {
        cmd.addOption(optFoo);
        assertEquals("default", cmd.getOptionValue("f", "default"));
    }

    public void testGetOptionValueWithDefaultPresentWithArg() {
        optBarLong.addValue("explicit");
        cmd.addOption(optBarLong);
        assertEquals("explicit", cmd.getOptionValue("b", "default"));
    }

    // ---- getOptionValue(char) ----

    public void testGetOptionValueCharPresentWithArg() {
        optBarLong.addValue("data");
        cmd.addOption(optBarLong);
        assertEquals("data", cmd.getOptionValue('b'));
    }

    public void testGetOptionValueCharNotPresent() {
        assertNull(cmd.getOptionValue('x'));
    }

    // ---- getOptionValue(char, String) ----

    public void testGetOptionValueCharWithDefaultNotPresent() {
        assertEquals("fallback", cmd.getOptionValue('x', "fallback"));
    }

    public void testGetOptionValueCharWithDefaultPresentNoArg() {
        cmd.addOption(optFoo);
        assertEquals("fallback", cmd.getOptionValue('f', "fallback"));
    }

    // ---- getOptionValues(String) ----

    

    public void testGetOptionValuesPresentSingleArg() {
        optBarLong.addValue("only");
        cmd.addOption(optBarLong);
        String[] values = cmd.getOptionValues("b");
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("only", values[0]);
    }

    public void testGetOptionValuesNotPresent() {
        assertNull(cmd.getOptionValues("x"));
    }

    public void testGetOptionValuesPresentNoArgReturnsNull() {
        cmd.addOption(optFoo);
        assertNull(cmd.getOptionValues("f"));
    }

    // ---- getOptionValues(char) ----

    public void testGetOptionValuesCharPresent() {
        optBarLong.addValue("a1");
        cmd.addOption(optBarLong);
        String[] values = cmd.getOptionValues('b');
        assertNotNull(values);
        assertEquals("a1", values[0]);
    }

    public void testGetOptionValuesCharNotPresent() {
        assertNull(cmd.getOptionValues('x'));
    }

    // ---- getOptionObject(String) ----

    

    public void testGetOptionObjectNotPresent() {
        assertNull(cmd.getOptionObject("z"));
    }

    public void testGetOptionObjectPresentNoValue() {
        // Option has type but no value, getOptionValue returns null => result null
        cmd.addOption(optBazInt);
        assertNull(cmd.getOptionObject("z"));
    }

    // ---- getOptionObject(char) ----

    

    // ---- getArgs / getArgList ----

    

    

    

    // ---- iterator / getOptions ----

    

    

    

    

    // ---- behavior on duplicate options ----

    
}
