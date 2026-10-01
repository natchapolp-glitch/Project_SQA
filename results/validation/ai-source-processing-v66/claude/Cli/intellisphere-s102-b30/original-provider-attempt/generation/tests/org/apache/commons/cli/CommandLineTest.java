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

    public void testGetOptionValueStringWhenPresentWithValue() {
        Option option = new Option("f", "file", true, "Input file");
        option.setValue("input.txt");
        commandLine.addOption(option);
        assertEquals("Should return first value when multiple values exist", "input.txt", commandLine.getOptionValue("file"));
    }

    public void testGetOptionValueStringByShortName() {
        Option option = new Option("f", "file", true, "Input file");
        option.setValue("data.csv");
        commandLine.addOption(option);
        assertEquals("Should find option by short name", "data.csv", commandLine.getOptionValue("f"));
    }

    // ============ getOptionValue(char) Tests ============

    public void testGetOptionValueCharWhenNotPresent() {
        assertNull("Should return null for char when option not present", commandLine.getOptionValue('f'));
    }

    public void testGetOptionValueCharWhenPresent() {
        Option option = new Option("f", "file", true, "Input file");
        option.setValue("test.txt");
        commandLine.addOption(option);
        assertEquals("Should return value for char option", "test.txt", commandLine.getOptionValue('f'));
    }

    // ============ getOptionValue with default value Tests ============

    public void testGetOptionValueStringWithDefaultWhenNotPresent() {
        String result = commandLine.getOptionValue("verbose", "false");
        assertEquals("Should return default when option not present", "false", result);
    }

    public void testGetOptionValueStringWithDefaultWhenPresent() {
        Option option = new Option("v", "verbose", true, "Verbosity level");
        option.setValue("2");
        commandLine.addOption(option);
        String result = commandLine.getOptionValue("verbose", "1");
        assertEquals("Should return actual value, not default", "2", result);
    }

    public void testGetOptionValueCharWithDefaultWhenNotPresent() {
        String result = commandLine.getOptionValue('v', "off");
        assertEquals("Should return default for char option when not present", "off", result);
    }

    public void testGetOptionValueCharWithDefaultWhenPresent() {
        Option option = new Option("l", "level", true, "Level");
        option.setValue("high");
        commandLine.addOption(option);
        String result = commandLine.getOptionValue('l', "low");
        assertEquals("Should return actual value for char option", "high", result);
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

    public void testGetOptionValuesStringWhenPresentSingleValue() {
        Option option = new Option("f", "file", true, "File");
        option.setValue("single.txt");
        commandLine.addOption(option);
        String[] values = commandLine.getOptionValues("file");
        assertNotNull("Should return array of values", values);
        assertEquals("Should have one value", 1, values.length);
        assertEquals("Should contain correct value", "single.txt", values[0]);
    }

    public void testGetOptionValuesStringByShortName() {
        Option option = new Option("o", "output", true, "Output");
        option.setValue("out.log");
        commandLine.addOption(option);
        String[] values = commandLine.getOptionValues("o");
        assertNotNull("Should find option by short name", values);
        assertEquals("Should return values for short name", "out.log", values[0]);
    }

    // ============ getOptionValues(char) Tests ============

    public void testGetOptionValuesCharWhenNotPresent() {
        assertNull("Should return null for char when option not present", commandLine.getOptionValues('x'));
    }

    public void testGetOptionValuesCharWhenPresent() {
        Option option = new Option("d", "dir", true, "Directory");
        option.setValue("path/to/dir");
        commandLine.addOption(option);
        String[] values = commandLine.getOptionValues('d');
        assertNotNull("Should return values for char option", values);
        assertEquals("Should contain value", "path/to/dir", values[0]);
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

    

    // ============ getArgList Tests ============

    

    

    

    // ============ addOption and getOptions Tests ============

    

    

    

    

    // ============ iterator Tests ============

    

    

    

    // ============ Integration Tests ============

    

    

    
}
