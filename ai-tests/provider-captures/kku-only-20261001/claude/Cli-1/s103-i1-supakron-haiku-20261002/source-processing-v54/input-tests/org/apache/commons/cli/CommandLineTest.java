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

    public void testGetOptionsSingleOption() {
        optionA.setValue(null);
        cmdLine.addOption(optionA);
        Option[] options = cmdLine.getOptions();
        assertEquals("getOptions should return array with single option",
                     1, options.length);
        assertSame("option should match", optionA, options[0]);
    }

    public void testGetOptionsMultipleOptions() {
        optionA.setValue(null);
        optionB.addValue("value");
        cmdLine.addOption(optionA);
        cmdLine.addOption(optionB);
        Option[] options = cmdLine.getOptions();
        assertEquals("getOptions should return array with all options",
                     2, options.length);
    }

    // ============ iterator Tests ============

    public void testIteratorEmpty() {
        Iterator it = cmdLine.iterator();
        assertNotNull("iterator should return non-null iterator",
                      it);
        assertFalse("iterator should be empty initially",
                    it.hasNext());
    }

    public void testIteratorWithOptions() {
        optionA.setValue(null);
        optionB.addValue("value");
        cmdLine.addOption(optionA);
        cmdLine.addOption(optionB);
        Iterator it = cmdLine.iterator();
        assertTrue("iterator should have next element",
                   it.hasNext());
        Object first = it.next();
        assertNotNull("iterator should return option",
                      first);
        assertTrue("iterator should be Option instance",
                   first instanceof Option);
    }

    public void testIteratorIteratesAllOptions() {
        optionA.setValue(null);
        optionB.addValue("value");
        cmdLine.addOption(optionA);
        cmdLine.addOption(optionB);
        Iterator it = cmdLine.iterator();
        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertEquals("iterator should iterate all options",
                     2, count);
    }

    // ============ addOption Tests ============

    public void testAddOptionSingleOption() {
        optionA.setValue(null);
        cmdLine.addOption(optionA);
        assertTrue("option should be retrievable after add",
                   cmdLine.hasOption("a"));
    }

    public void testAddOptionMultipleOptions() {
        optionA.setValue(null);
        optionB.addValue("value");
        cmdLine.addOption(optionA);
        cmdLine.addOption(optionB);
        assertTrue("first option should be present",
                   cmdLine.hasOption("a"));
        assertTrue("second option should be present",
                   cmdLine.hasOption("b"));
    }

    // ============ addArg Tests ============

    public void testAddArgSingleArg() {
        cmdLine.addArg("argument");
        String[] args = cmdLine.getArgs();
        assertEquals("arg should be added",
                     1, args.length);
        assertEquals("arg value should match",
                     "argument", args[0]);
    }

    public void testAddArgMultipleArgs() {
        cmdLine.addArg("first");
        cmdLine.addArg("second");
        String[] args = cmdLine.getArgs();
        assertEquals("both args should be added",
                     2, args.length);
        assertEquals("first arg should match", "first", args[0]);
        assertEquals("second arg should match", "second", args[1]);
    }

    public void testAddArgMaintainsOrder() {
        cmdLine.addArg("alpha");
        cmdLine.addArg("beta");
        cmdLine.addArg("gamma");
        String[] args = cmdLine.getArgs();
        assertEquals("args should maintain insertion order",
                     3, args.length);
        assertEquals("order should be preserved", "alpha", args[0]);
        assertEquals("order should be preserved", "beta", args[1]);
        assertEquals("order should be preserved", "gamma", args[2]);
    }

    // ============ Integration Tests ============

    public void testMixedOptionsAndArgs() {
        optionA.setValue(null);
        optionB.addValue("option-value");
        cmdLine.addOption(optionA);
        cmdLine.addOption(optionB);
        cmdLine.addArg("file.txt");
        cmdLine.addArg("output.txt");
        
        assertTrue("option a should be present", cmdLine.hasOption('a'));
        assertEquals("option b value should be retrievable",
                     "option-value", cmdLine.getOptionValue('b'));
        String[] args = cmdLine.getArgs();
        assertEquals("remaining args should be retrievable",
                     2, args.length);
    }

    public void testEmptyCommandLine() {
        assertEquals("empty cmdline should have no options",
                     0, cmdLine.getOptions().length);
        assertEquals("empty cmdline should have no args",
                     0, cmdLine.getArgs().length);
        assertFalse("empty cmdline should not have arbitrary option",
                    cmdLine.hasOption("x"));
    }
}
