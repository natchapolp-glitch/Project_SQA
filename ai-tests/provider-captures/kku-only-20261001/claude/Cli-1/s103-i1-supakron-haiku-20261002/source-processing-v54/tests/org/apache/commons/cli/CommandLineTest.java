// org/apache/commons/cli/CommandLineTest.java
package org.apache.commons.cli;

import junit.framework.TestCase;
import java.util.List;
import java.util.Iterator;

/**
 * Regression tests for CommandLine class.
 * Tests the fixed reference behavior of query and state management methods.
 */
public class CommandLineTest extends TestCase {

    private CommandLine cmdLine;
    private Option optionA;
    private Option optionB;
    private Option optionLongOpt;

    protected void setUp() throws Exception {
        super.setUp();
        cmdLine = new CommandLine();
        
        // Create mock options for testing
        optionA = new Option("a", "option-a", false, "Option A");
        optionB = new Option("b", "option-b", true, "Option B");
        optionLongOpt = new Option("l", "long-option", true, "Long option");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        cmdLine = null;
        optionA = null;
        optionB = null;
        optionLongOpt = null;
    }

    // ============ hasOption(String) Tests ============

    public void testHasOptionStringWhenPresent() {
        optionA.setValue(null);
        cmdLine.addOption(optionA);
        assertTrue("hasOption should return true for added option", 
                   cmdLine.hasOption("a"));
    }

    public void testHasOptionStringWhenAbsent() {
        assertFalse("hasOption should return false for non-existent option",
                    cmdLine.hasOption("x"));
    }

    public void testHasOptionStringWithLongName() {
        optionA.setValue(null);
        cmdLine.addOption(optionA);
        assertTrue("hasOption should match long option name",
                   cmdLine.hasOption("option-a"));
    }

    public void testHasOptionStringWithLeadingHyphens() {
        optionA.setValue(null);
        cmdLine.addOption(optionA);
        assertTrue("hasOption should handle leading hyphens",
                   cmdLine.hasOption("-a"));
    }

    public void testHasOptionStringWithDoubleLeadingHyphens() {
        optionA.setValue(null);
        cmdLine.addOption(optionA);
        assertTrue("hasOption should handle double leading hyphens",
                   cmdLine.hasOption("--option-a"));
    }

    // ============ hasOption(char) Tests ============

    public void testHasOptionCharWhenPresent() {
        optionA.setValue(null);
        cmdLine.addOption(optionA);
        assertTrue("hasOption(char) should return true for added option",
                   cmdLine.hasOption('a'));
    }

    public void testHasOptionCharWhenAbsent() {
        assertFalse("hasOption(char) should return false for non-existent option",
                    cmdLine.hasOption('z'));
    }

    public void testHasOptionCharMultipleOptions() {
        optionA.setValue(null);
        optionB.setValue("value");
        cmdLine.addOption(optionA);
        cmdLine.addOption(optionB);
        assertTrue("hasOption(char) should find first option", 
                   cmdLine.hasOption('a'));
        assertTrue("hasOption(char) should find second option",
                   cmdLine.hasOption('b'));
        assertFalse("hasOption(char) should not find non-existent option",
                    cmdLine.hasOption('c'));
    }

    // ============ getOptionValue(String) Tests ============

    public void testGetOptionValueStringWithValue() {
        optionB.addValue("test-value");
        cmdLine.addOption(optionB);
        assertEquals("getOptionValue should return the option value",
                     "test-value", cmdLine.getOptionValue("b"));
    }

    public void testGetOptionValueStringWithoutValue() {
        optionA.setValue(null);
        cmdLine.addOption(optionA);
        assertNull("getOptionValue should return null when option has no value",
                   cmdLine.getOptionValue("a"));
    }

    public void testGetOptionValueStringNotPresent() {
        assertNull("getOptionValue should return null for non-existent option",
                   cmdLine.getOptionValue("nonexistent"));
    }

    public void testGetOptionValueStringWithLongName() {
        optionB.addValue("long-value");
        cmdLine.addOption(optionB);
        assertEquals("getOptionValue should work with long option name",
                     "long-value", cmdLine.getOptionValue("option-b"));
    }

    // ============ getOptionValue(char) Tests ============

    public void testGetOptionValueCharWithValue() {
        optionB.addValue("char-value");
        cmdLine.addOption(optionB);
        assertEquals("getOptionValue(char) should return the option value",
                     "char-value", cmdLine.getOptionValue('b'));
    }

    public void testGetOptionValueCharNotPresent() {
        assertNull("getOptionValue(char) should return null for non-existent option",
                   cmdLine.getOptionValue('x'));
    }

    // ============ getOptionValue(String, String) Tests ============

    public void testGetOptionValueStringWithDefaultWhenPresent() {
        optionB.addValue("actual-value");
        cmdLine.addOption(optionB);
        assertEquals("getOptionValue with default should return actual value when present",
                     "actual-value", cmdLine.getOptionValue("b", "default"));
    }

    public void testGetOptionValueStringWithDefaultWhenAbsent() {
        assertEquals("getOptionValue with default should return default when option absent",
                     "default", cmdLine.getOptionValue("nonexistent", "default"));
    }

    public void testGetOptionValueStringWithDefaultWhenNoValue() {
        optionA.setValue(null);
        cmdLine.addOption(optionA);
        assertEquals("getOptionValue with default should return default when option has no value",
                     "fallback", cmdLine.getOptionValue("a", "fallback"));
    }

    public void testGetOptionValueStringWithDefaultNullDefault() {
        assertEquals("getOptionValue with null default should return null for non-existent",
                     null, cmdLine.getOptionValue("absent", null));
    }

    // ============ getOptionValue(char, String) Tests ============

    public void testGetOptionValueCharWithDefaultWhenPresent() {
        optionB.addValue("value");
        cmdLine.addOption(optionB);
        assertEquals("getOptionValue(char, default) should return actual when present",
                     "value", cmdLine.getOptionValue('b', "default"));
    }

    public void testGetOptionValueCharWithDefaultWhenAbsent() {
        assertEquals("getOptionValue(char, default) should return default when absent",
                     "default", cmdLine.getOptionValue('z', "default"));
    }

    // ============ getOptionValues(String) Tests ============

    public void testGetOptionValuesStringSingleValue() {
        optionB.addValue("single");
        cmdLine.addOption(optionB);
        String[] values = cmdLine.getOptionValues("b");
        assertNotNull("getOptionValues should return array for present option",
                      values);
        assertEquals("getOptionValues should return array with value",
                     "single", values[0]);
    }

    public void testGetOptionValuesStringMultipleValues() {
        optionB.addValue("first");
        optionB.addValue("second");
        optionB.addValue("third");
        cmdLine.addOption(optionB);
        String[] values = cmdLine.getOptionValues("b");
        assertNotNull("getOptionValues should return array",
                      values);
        assertEquals("getOptionValues should have correct size",
                     3, values.length);
        assertEquals("first value should match", "first", values[0]);
        assertEquals("second value should match", "second", values[1]);
        assertEquals("third value should match", "third", values[2]);
    }

    public void testGetOptionValuesStringNotPresent() {
        assertNull("getOptionValues should return null for non-existent option",
                   cmdLine.getOptionValues("nonexistent"));
    }

    // ============ getOptionValues(char) Tests ============

    public void testGetOptionValuesCharSingleValue() {
        optionB.addValue("char-value");
        cmdLine.addOption(optionB);
        String[] values = cmdLine.getOptionValues('b');
        assertNotNull("getOptionValues(char) should return array",
                      values);
        assertEquals("getOptionValues(char) should contain value",
                     "char-value", values[0]);
    }

    public void testGetOptionValuesCharNotPresent() {
        assertNull("getOptionValues(char) should return null for non-existent",
                   cmdLine.getOptionValues('z'));
    }

    // ============ getOptionObject Tests ============

    public void testGetOptionObjectStringWhenPresent() {
        optionB.addValue("123");
        optionB.setType(Integer.class);
        cmdLine.addOption(optionB);
        Object obj = cmdLine.getOptionObject("b");
        assertNotNull("getOptionObject should return object for present option",
                      obj);
    }

    public void testGetOptionObjectStringWhenAbsent() {
        assertNull("getOptionObject should return null for non-existent option",
                   cmdLine.getOptionObject("nonexistent"));
    }

    public void testGetOptionObjectCharWhenPresent() {
        optionB.addValue("42");
        optionB.setType(Integer.class);
        cmdLine.addOption(optionB);
        Object obj = cmdLine.getOptionObject('b');
        assertNotNull("getOptionObject(char) should return object",
                      obj);
    }

    public void testGetOptionObjectCharWhenAbsent() {
        assertNull("getOptionObject(char) should return null for non-existent",
                   cmdLine.getOptionObject('x'));
    }

    // ============ getArgs Tests ============

    public void testGetArgsEmpty() {
        String[] args = cmdLine.getArgs();
        assertNotNull("getArgs should return non-null array",
                      args);
        assertEquals("getArgs should return empty array when no args",
                     0, args.length);
    }

    

    

    // ============ getArgList Tests ============

    

    

    

    // ============ getOptions Tests ============

    

    

    

    // ============ iterator Tests ============

    

    

    

    // ============ addOption Tests ============

    

    

    // ============ addArg Tests ============

    

    

    

    // ============ Integration Tests ============

    

    
}
