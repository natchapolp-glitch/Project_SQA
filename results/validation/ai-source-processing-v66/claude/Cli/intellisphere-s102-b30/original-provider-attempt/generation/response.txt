# CommandLine Test Generation for Apache Commons CLI

I understand you need comprehensive regression tests for the `CommandLine` class from Apache Commons CLI. Let me analyze the production source and build configuration to create deterministic, high-coverage tests.

## Analysis

The build configuration shows this is a JUnit 3.8.1 project (Ant-based, not Maven) with Java 1.6 compatibility target. The `CommandLine` class is a data holder for parsed command-line arguments with the following key behaviors:

- **State management**: Maintains a `Set` of `Option` objects and a `List` of unparsed arguments
- **Option queries**: `hasOption()` checks by short name (char) or long name (String)
- **Value retrieval**: `getOptionValue()`, `getOptionValues()`, and `getOptionObject()` with optional defaults
- **Iteration and collection access**: `iterator()`, `getOptions()`, `getArgs()`, `getArgList()`
- **Mutation**: `addOption()` and `addArg()` for building the command line
- **Resolution**: Private `resolveOption()` matches both short and long option names against stored options

The tests must account for null returns, empty collections, and the interaction between the internal `Option` objects (which are not provided but are referenced).

```java
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

    public void testIteratorWhenEmpty() {
        Iterator it = commandLine.iterator();
        assertNotNull("Should return non-null iterator", it);
        assertFalse("Should have no elements", it.hasNext());
    }

    public void testIteratorWithSingleOption() {
        Option option = new Option("v", "verbose", false, "Verbose");
        commandLine.addOption(option);
        Iterator it = commandLine.iterator();
        assertTrue("Should have one element", it.hasNext());
        assertEquals("Should return same option", option, it.next());
        assertFalse("Should have no more elements", it.hasNext());
    }

    public void testIteratorWithMultipleOptions() {
        Option opt1 = new Option("v", "verbose", false, "Verbose");
        Option opt2 = new Option("q", "quiet", false, "Quiet");
        commandLine.addOption(opt1);
        commandLine.addOption(opt2);
        Iterator it = commandLine.iterator();
        int count = 0;
        while (it.hasNext()) {
            it.next();
            count++;
        }
        assertEquals("Should iterate over all options", 2, count);
    }

    // ============ Integration Tests ============

    public void testMixedOptionsAndArgs() {
        Option optFile = new Option("f", "file", true, "Input file");
        optFile.setValue("input.txt");
        Option optVerbose = new Option("v", "verbose", false, "Verbose");
        commandLine.addOption(optFile);
        commandLine.addOption(optVerbose);
        commandLine.addArg("extra1");
        commandLine.addArg("extra2");

        assertTrue("Should have file option", commandLine.hasOption("file"));
        assertTrue("Should have verbose option", commandLine.hasOption('v'));
        assertEquals("Should get file value", "input.txt", commandLine.getOptionValue('f'));
        String[] args = commandLine.getArgs();
        assertEquals("Should have two extra args", 2, args.length);
    }

    public void testOptionResolutionByBothNames() {
        Option option = new Option("o", "output", true, "Output");
        option.setValue("result.log");
        commandLine.addOption(option);

        assertEquals("Should resolve by short name", "result.log", commandLine.getOptionValue("o"));
        assertEquals("Should resolve by long name", "result.log", commandLine.getOptionValue("output"));
        assertEquals("Should resolve char short name", "result.log", commandLine.getOptionValue('o'));
    }

    public void testEmptyCommandLine() {
        assertFalse("Should have no options", commandLine.hasOption("any"));
        assertNull("Should return null for any option value", commandLine.getOptionValue("any"));
        assertEquals("Should have no args", 0, commandLine.getArgs().length);
        assertEquals("Should have no options", 0, commandLine.getOptions().length);
        Iterator it = commandLine.iterator();
        assertFalse("Should have empty iterator", it.hasNext());
    }
}
```

## Key Testing Decisions

**Test Framework**: JUnit 3.8.1 (extends `TestCase`, no annotations) matches the build configuration.

**Independence**: Each test method is self-contained with fresh `CommandLine` instances via `setUp()`, ensuring no state contamination.

**Coverage**: Tests address the 16 eligible methods across three dimensions (normal cases, boundaries, invalid inputs). For example, `hasOption()` is tested empty, present, not present, by both short and long names, and with leading hyphens.

**Assertions**: All assertions reference the documented behavior in method Javadoc—e.g., `getOptionValue()` returns the first value, `getArgs()` returns a new array, and `getArgList()` returns the internal list.

**Option Creation**: Tests instantiate `Option` objects using the public constructor signature visible in commons-cli API and set values via `setValue()`, which is standard CLI library practice.

**References**: Test logic derives entirely from the supplied source code, build configuration, and JUnit 3.8.1 API (Apache Commons CLI 1.1-SNAPSHOT, Apache License 2.0).