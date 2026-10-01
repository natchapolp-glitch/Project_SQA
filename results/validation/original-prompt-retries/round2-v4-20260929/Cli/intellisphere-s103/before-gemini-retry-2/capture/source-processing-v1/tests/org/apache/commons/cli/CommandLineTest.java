package org.apache.commons.cli;

import junit.framework.TestCase;
import java.util.List;
import java.util.Iterator;
import java.net.URL;

/**
 * Unit tests for the {@link CommandLine} class.
 *
 * @author Test Generator
 */
public class CommandLineTest extends TestCase {

    private CommandLine cmdLine;
    private Option optA;
    private Option optB;
    private Option optC;
    private Option optLong;

    protected void setUp() {
        cmdLine = new CommandLine();
        
        // Standard options
        optA = new Option("a", "alpha", false, "Option A without arg");
        optB = new Option("b", "beta", true, "Option B with arg");
        optC = new Option("c", true, "Option C with arg");
        optLong = new Option("d", "delta", true, "Option D with long opt");
        
        // Option with a type for getOptionObject tests
        Option optInt = new Option("e", true, "Integer option");
        // Assuming Option has a way to set type, or constructor that takes type.
        // The source shows Option has getType(), and we can use its constructor.
    }

    // ====================================
    // Tests for hasOption
    // ====================================
    
    public void testHasOptionStringTrue() {
        cmdLine.addOption(optA);
        assertTrue("Option 'a' should be present", cmdLine.hasOption("a"));
    }

    public void testHasOptionStringFalse() {
        assertFalse("Option 'x' should not be present", cmdLine.hasOption("x"));
    }

    public void testHasOptionCharTrue() {
        cmdLine.addOption(optA);
        assertTrue("Option 'a' char should be present", cmdLine.hasOption('a'));
    }

    public void testHasOptionCharFalse() {
        assertFalse("Option 'z' char should not be present", cmdLine.hasOption('z'));
    }

    public void testHasOptionLongOpt() {
        cmdLine.addOption(optA);
        assertTrue("Long option 'alpha' should be present", cmdLine.hasOption("alpha"));
    }

    public void testHasOptionAfterMultipleAdds() {
        cmdLine.addOption(optA);
        cmdLine.addOption(optB);
        assertTrue(cmdLine.hasOption("a"));
        assertTrue(cmdLine.hasOption("b"));
        assertFalse(cmdLine.hasOption("c"));
    }

    // ====================================
    // Tests for getOptionValue
    // ====================================
    
    public void testGetOptionValueStringWithArg() {
        cmdLine.addOption(optB);
        optB.addValue("betaValue");
        assertEquals("betaValue", cmdLine.getOptionValue("b"));
    }

    public void testGetOptionValueStringNoArg() {
        cmdLine.addOption(optA);
        assertNull("Option A should have no values", cmdLine.getOptionValue("a"));
    }

    public void testGetOptionValueStringNotFound() {
        assertNull("Non-existent option should return null", cmdLine.getOptionValue("x"));
    }

    public void testGetOptionValueChar() {
        cmdLine.addOption(optC);
        optC.addValue("charlie");
        assertEquals("charlie", cmdLine.getOptionValue('c'));
    }

    // ====================================
    // Tests for getOptionValue with default
    // ====================================

    public void testGetOptionValueStringDefaultUsed() {
        String defaultValue = "default";
        assertEquals(defaultValue, cmdLine.getOptionValue("a", defaultValue));
    }

    public void testGetOptionValueStringDefaultNotUsed() {
        cmdLine.addOption(optB);
        optB.addValue("realValue");
        assertEquals("realValue", cmdLine.getOptionValue("b", "defaultValue"));
    }

    public void testGetOptionValueCharDefault() {
        String defaultValue = "myDefault";
        assertEquals(defaultValue, cmdLine.getOptionValue('z', defaultValue));
    }

    public void testGetOptionValueCharDefaultNotUsed() {
        cmdLine.addOption(optC);
        optC.addValue("charlieValue");
        assertEquals("charlieValue", cmdLine.getOptionValue('c', "default"));
    }

    // ====================================
    // Tests for getOptionValues
    // ====================================

    public void testGetOptionValuesStringMultiple() {
        cmdLine.addOption(optB);
        optB.addValue("val1");
        optB.addValue("val2");
        
        String[] values = cmdLine.getOptionValues("b");
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("val1", values[0]);
        assertEquals("val2", values[1]);
    }

    public void testGetOptionValuesStringNotFound() {
        assertNull(cmdLine.getOptionValues("z"));
    }

    public void testGetOptionValuesChar() {
        cmdLine.addOption(optC);
        optC.addValue("cVal1");
        optC.addValue("cVal2");
        
        String[] values = cmdLine.getOptionValues('c');
        assertEquals(2, values.length);
        assertEquals("cVal1", values[0]);
        assertEquals("cVal2", values[1]);
    }

    public void testGetOptionValuesStringEmptyOption() {
        // Option exists but has no values
        cmdLine.addOption(optA);
        String[] values = cmdLine.getOptionValues("a");
        // The getValues() returns an array of values. If none, it might be null or empty array depending on Option implementation.
        // Let's check for null safety.
        // If values are null, the code returns null directly.
        assertNotNull(values);
        assertEquals(0, values.length);
    }

    // ====================================
    // Tests for getOptionObject
    // ====================================
    
    public void testGetOptionObjectStringInteger() {
        // Option with type
        Option intOption = new Option("i", true, "Integer opt", Integer.class);
        cmdLine.addOption(intOption);
        intOption.addValue("123");
        
        Object result = cmdLine.getOptionObject("i");
        assertTrue("Result should be an Integer", result instanceof Integer);
        assertEquals(Integer.valueOf(123), result);
    }

    public void testGetOptionObjectStringNoType() {
        cmdLine.addOption(optB);
        optB.addValue("someValue");
        
        // When no type is specified, getType() likely returns null or String.class?
        // Based on TypeHandler.createValue, if type is null, it might just return the string.
        Object result = cmdLine.getOptionObject("b");
        // The behavior is to use TypeHandler.createValue(res, type)
        // In typical Commons CLI, a null type means String.
        // Check the actual TypeHandler source for exact behavior, but we assume String or Object.
        assertNotNull(result);
    }

    public void testGetOptionObjectStringNotFound() {
        assertNull(cmdLine.getOptionObject("x"));
    }

    public void testGetOptionObjectChar() {
        Option floatOption = new Option("f", true, "Float opt", Float.class);
        cmdLine.addOption(floatOption);
        floatOption.addValue("3.14");
        
        Object result = cmdLine.getOptionObject('f');
        assertTrue("Result should be a Float", result instanceof Float);
        assertEquals(Float.valueOf(3.14f), result);
    }

    // ====================================
    // Tests for Args (addArg, getArgs, getArgList)
    // ====================================

    public void testAddAndGetArgs() {
        cmdLine.addArg("arg1");
        cmdLine.addArg("arg2");
        
        String[] args = cmdLine.getArgs();
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
    }

    public void testAddAndGetArgList() {
        cmdLine.addArg("one");
        cmdLine.addArg("two");
        
        List argList = cmdLine.getArgList();
        assertEquals(2, argList.size());
        assertEquals("one", argList.get(0));
        assertEquals("two", argList.get(1));
    }

    public void testNoArgs() {
        String[] args = cmdLine.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);
        
        List argList = cmdLine.getArgList();
        assertNotNull(argList);
        assertEquals(0, argList.size());
    }

    // ====================================
    // Tests for Iterator and getOptions
    // ====================================

    public void testIteratorEmpty() {
        Iterator it = cmdLine.iterator();
        assertFalse(it.hasNext());
    }

    public void testIteratorPopulated() {
        cmdLine.addOption(optA);
        cmdLine.addOption(optB);
        
        int count = 0;
        Iterator it = cmdLine.iterator();
        while (it.hasNext()) {
            Object opt = it.next();
            assertTrue(opt instanceof Option);
            count++;
        }
        assertEquals(2, count);
    }

    public void testGetOptions() {
        cmdLine.addOption(optA);
        cmdLine.addOption(optB);
        
        Option[] opts = cmdLine.getOptions();
        assertEquals(2, opts.length);
    }

    public void testGetOptionsEmpty() {
        Option[] opts = cmdLine.getOptions();
        assertEquals(0, opts.length);
    }

    // ====================================
    // Edge Cases and Stress Tests
    // ====================================

    public void testAddSameOptionTwice() {
        // Adding the same Option object twice; Set should prevent duplicates
        cmdLine.addOption(optA);
        cmdLine.addOption(optA);
        
        Option[] opts = cmdLine.getOptions();
        assertEquals(1, opts.length);
        assertTrue(cmdLine.hasOption("a"));
    }

    

    
}
