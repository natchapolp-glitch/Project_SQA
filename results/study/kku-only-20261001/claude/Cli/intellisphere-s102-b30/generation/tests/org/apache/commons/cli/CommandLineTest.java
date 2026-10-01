// org/apache/commons/cli/CommandLineTest.java
package org.apache.commons.cli;

import junit.framework.TestCase;
import java.util.Iterator;
import java.util.List;

/**
 * Regression tests for CommandLine class.
 * Tests cover normal cases, boundaries, empty states, and option resolution.
 */
public class CommandLineTest extends TestCase {

    private CommandLine commandLine;

    protected void setUp() {
        commandLine = new CommandLine();
    }

    // ============ hasOption(String) Tests ============

    public void testHasOptionStringWhenEmpty() {
        assertFalse("Should return false when no options added", commandLine.hasOption("verbose"));
    }

    public void testHasOptionStringWhenNotPresent() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        commandLine.addOption(option);
        assertFalse("Should return false for different option", commandLine.hasOption("quiet"));
    }

    public void testHasOptionStringWhenPresent() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        commandLine.addOption(option);
        assertTrue("Should return true when option present by short name", commandLine.hasOption("v"));
    }

    public void testHasOptionStringByLongName() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        commandLine.addOption(option);
        assertTrue("Should return true when option present by long name", commandLine.hasOption("verbose"));
    }

    public void testHasOptionStringWithLeadingHyphens() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        commandLine.addOption(option);
        assertTrue("Should handle single leading hyphen", commandLine.hasOption("-v"));
        assertTrue("Should handle double leading hyphens", commandLine.hasOption("--verbose"));
    }

    // ============ hasOption(char) Tests ============

    public void testHasOptionCharWhenEmpty() {
        assertFalse("Should return false for char when no options added", commandLine.hasOption('v'));
    }

    public void testHasOptionCharWhenPresent() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        commandLine.addOption(option);
        assertTrue("Should return true for char option when present", commandLine.hasOption('v'));
    }

    public void testHasOptionCharWhenNotPresent() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        commandLine.addOption(option);
        assertFalse("Should return false for char option not present", commandLine.hasOption('q'));
    }

    // ============ getOptionValue(String) Tests ============

    public void testGetOptionValueStringWhenNotPresent() {
        assertNull("Should return null when option not present", commandLine.getOptionValue("verbose"));
    }

    public void testGetOptionValueStringWhenPresentNoValue() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        commandLine.addOption(option);
        assertNull("Should return null when option has no values", commandLine.getOptionValue("verbose"));
    }

    

    

    // ============ getOptionValue(char) Tests ============

    public void testGetOptionValueCharWhenNotPresent() {
        assertNull("Should return null for char when option not present", commandLine.getOptionValue('f'));
    }

    

    // ============ getOptionValue with default value Tests ============

    public void testGetOptionValueStringWithDefaultWhenNotPresent() {
        String result = commandLine.getOptionValue("verbose", "false");
        assertEquals("Should return default when option not present", "false", result);
    }

    

    public void testGetOptionValueCharWithDefaultWhenNotPresent() {
        String result = commandLine.getOptionValue('v', "off");
        assertEquals("Should return default for char option when not present", "off", result);
    }

    

    // ============ getOptionValues Tests ============

    public void testGetOptionValuesStringWhenNotPresent() {
        assertNull("Should return null when option not present", commandLine.getOptionValues("files"));
    }

    public void testGetOptionValuesStringWhenPresentNoValues() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        commandLine.addOption(option);
        assertNull("Should return null for option without values", commandLine.getOptionValues("verbose"));
    }

    

    

    // ============ getOptionValues(char) Tests ============

    public void testGetOptionValuesCharWhenNotPresent() {
        assertNull("Should return null for char when option not present", commandLine.getOptionValues('x'));
    }

    

    // ============ getOptionObject Tests ============

    public void testGetOptionObjectStringWhenNotPresent() {
        assertNull("Should return null when option not present", commandLine.getOptionObject("count"));
    }

    public void testGetOptionObjectStringWhenPresentNoValue() {
        Option option = new Option("v", "verbose", false, "Verbose");
        commandLine.addOption(option);
        assertNull("Should return null when option has no value", commandLine.getOptionObject("verbose"));
    }

    public void testGetOptionObjectCharWhenNotPresent() {
        assertNull("Should return null for char when option not present", commandLine.getOptionObject('c'));
    }

    // ============ addArg and getArgs Tests ============

    public void testGetArgsWhenEmpty() {
        String[] args = commandLine.getArgs();
        assertNotNull("Should return non-null array", args);
        assertEquals("Should return empty array when no args added", 0, args.length);
    }

    public void testAddArgSingleArgument() {
        commandLine.addArg("file.txt");
        String[] args = commandLine.getArgs();
        assertEquals("Should contain one argument", 1, args.length);
        assertEquals("Should contain correct argument", "file.txt", args[0]);
    }

    public void testAddArgMultipleArguments() {
        commandLine.addArg("first.txt");
        commandLine.addArg("second.txt");
        commandLine.addArg("third.txt");
        String[] args = commandLine.getArgs();
        assertEquals("Should contain three arguments", 3, args.length);
        assertEquals("Arguments should be in order", "first.txt", args[0]);
        assertEquals("Arguments should be in order", "second.txt", args[1]);
        assertEquals("Arguments should be in order", "third.txt", args[2]);
    }

    public void testAddArgPreservesOrder() {
        commandLine.addArg("alpha");
        commandLine.addArg("beta");
        commandLine.addArg("gamma");
        String[] args = commandLine.getArgs();
        assertEquals("First arg", "alpha", args[0]);
        assertEquals("Second arg", "beta", args[1]);
        assertEquals("Third arg", "gamma", args[2]);
    }

    // ============ getArgList Tests ============

    public void testGetArgListWhenEmpty() {
        List argList = commandLine.getArgList();
        assertNotNull("Should return non-null list", argList);
        assertEquals("Should be empty", 0, argList.size());
    }

    public void testGetArgListReturnsInternalList() {
        commandLine.addArg("item1");
        commandLine.addArg("item2");
        List argList = commandLine.getArgList();
        assertEquals("Should return list with added items", 2, argList.size());
        assertEquals("Should contain first item", "item1", argList.get(0));
        assertEquals("Should contain second item", "item2", argList.get(1));
    }

    public void testGetArgListMutabilityReflectsAddArg() {
        List argList1 = commandLine.getArgList();
        assertEquals("Initially empty", 0, argList1.size());
        commandLine.addArg("new");
        List argList2 = commandLine.getArgList();
        assertEquals("Should reflect new additions", 1, argList2.size());
    }

    // ============ addOption and getOptions Tests ============

    public void testGetOptionsWhenEmpty() {
        Option[] options = commandLine.getOptions();
        assertNotNull("Should return non-null array", options);
        assertEquals("Should be empty array", 0, options.length);
    }

    public void testAddOptionSingleOption() {
        Option option = new Option("v", "verbose", false, "Verbose");
        commandLine.addOption(option);
        Option[] options = commandLine.getOptions();
        assertEquals("Should contain one option", 1, options.length);
        assertEquals("Should contain same option", option, options[0]);
    }

    public void testAddOptionMultipleOptions() {
        Option opt1 = new Option("v", "verbose", false, "Verbose");
        Option opt2 = new Option("q", "quiet", false, "Quiet");
        Option opt3 = new Option("f", "file", true, "File");
        commandLine.addOption(opt1);
        commandLine.addOption(opt2);
        commandLine.addOption(opt3);
        Option[] options = commandLine.getOptions();
        assertEquals("Should contain three options", 3, options.length);
    }

    public void testAddOptionDoesNotAddDuplicates() {
        Option option = new Option("v", "verbose", false, "Verbose");
        commandLine.addOption(option);
        commandLine.addOption(option);
        Option[] options = commandLine.getOptions();
        assertEquals("Should not add duplicate (Set behavior)", 1, options.length);
    }

    // ============ iterator Tests ============

    

    

    

    // ============ Integration Tests ============

    

    

    
}
