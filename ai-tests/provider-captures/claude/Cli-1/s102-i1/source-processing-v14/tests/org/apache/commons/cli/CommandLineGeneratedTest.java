package org.apache.commons.cli;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class CommandLineGeneratedTest extends TestCase
{
    private CommandLine cl;

    protected void setUp() throws Exception
    {
        super.setUp();
        cl = new CommandLine();
    }

    // ---------- helpers ----------

    private static Option flag(String opt, String longOpt)
    {
        Option o = new Option(opt, "flag option");
        if (longOpt != null)
        {
            o.setLongOpt(longOpt);
        }
        return o;
    }

    private static Option withValue(String opt, String longOpt, String value)
    {
        Option o = new Option(opt, longOpt, true, "option with argument");
        if (value != null)
        {
            o.addValue(value);
        }
        return o;
    }

    // ---------- hasOption(String) / hasOption(char) ----------

    public void testHasOptionStringShortName()
    {
        cl.addOption(flag("a", null));
        assertTrue(cl.hasOption("a"));
    }

    public void testHasOptionStringLongName()
    {
        cl.addOption(flag("a", "alpha"));
        assertTrue(cl.hasOption("alpha"));
        assertTrue(cl.hasOption("a"));
    }

    public void testHasOptionStringStripsLeadingHyphens()
    {
        cl.addOption(flag("a", "alpha"));
        assertTrue(cl.hasOption("-a"));
        assertTrue(cl.hasOption("--alpha"));
    }

    public void testHasOptionStringNotSet()
    {
        cl.addOption(flag("a", "alpha"));
        assertFalse(cl.hasOption("b"));
        assertFalse(cl.hasOption("beta"));
    }

    public void testHasOptionOnEmptyCommandLine()
    {
        assertFalse(cl.hasOption("a"));
        assertFalse(cl.hasOption('a'));
    }

    public void testHasOptionCharSet()
    {
        cl.addOption(flag("x", null));
        assertTrue(cl.hasOption('x'));
    }

    public void testHasOptionCharNotSet()
    {
        cl.addOption(flag("x", null));
        assertFalse(cl.hasOption('y'));
    }

    // ---------- getOptionValue(String) / getOptionValue(char) ----------

    public void testGetOptionValueStringShortName()
    {
        cl.addOption(withValue("b", "beta", "bv"));
        assertEquals("bv", cl.getOptionValue("b"));
    }

    public void testGetOptionValueStringLongName()
    {
        cl.addOption(withValue("b", "beta", "bv"));
        assertEquals("bv", cl.getOptionValue("beta"));
        assertEquals("bv", cl.getOptionValue("--beta"));
    }

    public void testGetOptionValueStringAbsentOptionIsNull()
    {
        cl.addOption(withValue("b", "beta", "bv"));
        assertNull(cl.getOptionValue("z"));
    }

    public void testGetOptionValueStringFlagWithoutArgumentIsNull()
    {
        cl.addOption(flag("a", null));
        assertTrue(cl.hasOption("a"));
        assertNull(cl.getOptionValue("a"));
    }

    public void testGetOptionValueStringReturnsFirstOfMultipleValues()
    {
        Option o = new Option("c", "gamma", true, "two args");
        o.setArgs(2);
        o.addValueForProcessing("first");
        o.addValueForProcessing("second");
        cl.addOption(o);
        assertEquals("first", cl.getOptionValue("c"));
    }

    public void testGetOptionValueChar()
    {
        cl.addOption(withValue("b", null, "bv"));
        assertEquals("bv", cl.getOptionValue('b'));
        assertNull(cl.getOptionValue('q'));
    }

    // ---------- getOptionValue with default ----------

    public void testGetOptionValueStringDefaultNotUsedWhenPresent()
    {
        cl.addOption(withValue("b", "beta", "bv"));
        assertEquals("bv", cl.getOptionValue("b", "default"));
    }

    public void testGetOptionValueStringDefaultUsedWhenAbsent()
    {
        assertEquals("default", cl.getOptionValue("b", "default"));
    }

    public void testGetOptionValueStringDefaultUsedWhenOptionHasNoValue()
    {
        cl.addOption(flag("a", null));
        assertEquals("default", cl.getOptionValue("a", "default"));
    }

    public void testGetOptionValueStringNullDefaultWhenAbsent()
    {
        assertNull(cl.getOptionValue("b", null));
    }

    public void testGetOptionValueCharDefault()
    {
        cl.addOption(withValue("b", null, "bv"));
        assertEquals("bv", cl.getOptionValue('b', "default"));
        assertEquals("default", cl.getOptionValue('z', "default"));
    }

    // ---------- getOptionValues ----------

    public void testGetOptionValuesMultiple()
    {
        Option o = new Option("c", "gamma", true, "two args");
        o.setArgs(2);
        o.addValueForProcessing("one");
        o.addValueForProcessing("two");
        cl.addOption(o);

        String[] values = cl.getOptionValues("c");
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("one", values[0]);
        assertEquals("two", values[1]);
        assertEquals(2, cl.getOptionValues("gamma").length);
        assertEquals(2, cl.getOptionValues('c').length);
    }

    public void testGetOptionValuesAbsentOptionIsNull()
    {
        assertNull(cl.getOptionValues("c"));
        assertNull(cl.getOptionValues('c'));
    }

    public void testGetOptionValuesFlagWithoutValuesIsNull()
    {
        cl.addOption(flag("a", null));
        assertNull(cl.getOptionValues("a"));
    }

    // ---------- getOptionObject ----------

    public void testGetOptionObjectNumberType()
    {
        Option o = new Option("n", "number", true, "numeric");
        o.setType(PatternOptionBuilder.NUMBER_VALUE);
        o.addValueForProcessing("42");
        cl.addOption(o);

        assertEquals(Long.valueOf(42L), cl.getOptionObject("n"));
        assertEquals(Long.valueOf(42L), cl.getOptionObject("number"));
        assertEquals(Long.valueOf(42L), cl.getOptionObject('n'));
    }

    public void testGetOptionObjectStringType()
    {
        Option o = new Option("s", "str", true, "string");
        o.setType(PatternOptionBuilder.STRING_VALUE);
        o.addValueForProcessing("hello");
        cl.addOption(o);

        assertEquals("hello", cl.getOptionObject("s"));
    }

    public void testGetOptionObjectAbsentOptionIsNull()
    {
        assertNull(cl.getOptionObject("n"));
        assertNull(cl.getOptionObject('n'));
    }

    public void testGetOptionObjectOptionWithoutValueIsNull()
    {
        Option o = new Option("n", "number", true, "numeric");
        o.setType(PatternOptionBuilder.NUMBER_VALUE);
        cl.addOption(o);
        assertNull(cl.getOptionObject("n"));
    }

    // ---------- args ----------

    public void testGetArgsEmpty()
    {
        assertEquals(0, cl.getArgs().length);
        assertTrue(cl.getArgList().isEmpty());
    }

    public void testAddArgPreservesOrderInGetArgsAndGetArgList()
    {
        cl.addArg("one");
        cl.addArg("two");
        cl.addArg("three");

        String[] args = cl.getArgs();
        assertEquals(3, args.length);
        assertEquals("one", args[0]);
        assertEquals("two", args[1]);
        assertEquals("three", args[2]);

        List list = cl.getArgList();
        assertEquals(3, list.size());
        assertEquals("one", list.get(0));
        assertEquals("three", list.get(2));
    }

    public void testGetArgsReturnsIndependentArrayAndGetArgListIsLive()
    {
        cl.addArg("one");
        String[] snapshot = cl.getArgs();
        cl.addArg("two");
        assertEquals(1, snapshot.length);
        assertEquals(2, cl.getArgs().length);
        assertSame(cl.getArgList(), cl.getArgList());
        assertEquals(2, cl.getArgList().size());
    }

    // ---------- iterator / getOptions / addOption ----------

    public void testIteratorAndGetOptionsEmpty()
    {
        assertFalse(cl.iterator().hasNext());
        assertEquals(0, cl.getOptions().length);
    }

    public void testIteratorAndGetOptionsContainAddedOptions()
    {
        Option a = flag("a", "alpha");
        Option b = withValue("b", "beta", "bv");
        cl.addOption(a);
        cl.addOption(b);

        int count = 0;
        boolean sawA = false;
        boolean sawB = false;
        for (Iterator it = cl.iterator(); it.hasNext();)
        {
            Object o = it.next();
            count++;
            if (o == a)
            {
                sawA = true;
            }
            if (o == b)
            {
                sawB = true;
            }
        }
        assertEquals(2, count);
        assertTrue(sawA);
        assertTrue(sawB);

        Option[] options = cl.getOptions();
        assertEquals(2, options.length);
    }

    
}
