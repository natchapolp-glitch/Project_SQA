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

    

    public void testHasOptionStringWhenAbsent() {
        assertFalse("hasOption should return false for non-existent option",
                    cmdLine.hasOption("x"));
    }

    

    

    

    // ============ hasOption(char) Tests ============

    

    public void testHasOptionCharWhenAbsent() {
        assertFalse("hasOption(char) should return false for non-existent option",
                    cmdLine.hasOption('z'));
    }

    

    // ============ getOptionValue(String) Tests ============

    public void testGetOptionValueStringWithValue() {
        optionB.addValue("test-value");
        cmdLine.addOption(optionB);
        assertEquals("getOptionValue should return the option value",
                     "test-value", cmdLine.getOptionValue("b"));
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

    public void testGetArgsSingleArg() {
        cmdLine.addArg("arg1");
        String[] args = cmdLine.getArgs();
        assertEquals("getArgs should return array with single arg",
                     1, args.length);
        assertEquals("arg should match", "arg1", args[0]);
    }

    public void testGetArgsMultipleArgs() {
        cmdLine.addArg("first");
        cmdLine.addArg("second");
        cmdLine.addArg("third");
        String[] args = cmdLine.getArgs();
        assertEquals("getArgs should return array with all args",
                     3, args.length);
        assertEquals("first arg should match", "first", args[0]);
        assertEquals("second arg should match", "second", args[1]);
        assertEquals("third arg should match", "third", args[2]);
    }

    // ============ getArgList Tests ============

    public void testGetArgListEmpty() {
        List argList = cmdLine.getArgList();
        assertNotNull("getArgList should return non-null list",
                      argList);
        assertEquals("getArgList should return empty list initially",
                     0, argList.size());
    }

    public void testGetArgListAfterAddArg() {
        cmdLine.addArg("test");
        List argList = cmdLine.getArgList();
        assertEquals("getArgList should contain added arg",
                     1, argList.size());
        assertEquals("arg should be in list", "test", argList.get(0));
    }

    public void testGetArgListModification() {
        cmdLine.addArg("original");
        List argList = cmdLine.getArgList();
        cmdLine.addArg("added");
        assertEquals("modifications to CommandLine should be visible in list",
                     2, argList.size());
    }

    // ============ getOptions Tests ============

    public void testGetOptionsEmpty() {
        Option[] options = cmdLine.getOptions();
        assertNotNull("getOptions should return non-null array",
                      options);
        assertEquals("getOptions should return empty array when no options",
                     0, options.length);
    }

    

    

    // ============ iterator Tests ============

    public void testIteratorEmpty() {
        Iterator it = cmdLine.iterator();
        assertNotNull("iterator should return non-null iterator",
                      it);
        assertFalse("iterator should be empty initially",
                    it.hasNext());
    }

    

    

    // ============ addOption Tests ============

    

    

    // ============ addArg Tests ============

    public void testAddArgSingleArg() {
        cmdLine.addArg("argument");
        String[] args = cmdLine.getArgs();
        assertEquals("arg should be added",
                     1, args.length);
        assertEquals("arg value should match",
                     "argument", args[0]);
    }

    

    

    // ============ Integration Tests ============

    

    
}
