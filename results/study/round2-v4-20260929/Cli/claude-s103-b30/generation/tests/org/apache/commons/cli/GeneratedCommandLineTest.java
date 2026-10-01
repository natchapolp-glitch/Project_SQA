package org.apache.commons.cli;

import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

public class GeneratedCommandLineTest extends TestCase
{
    private Options buildOptions()
    {
        Options options = new Options();
        options.addOption(new Option("a", true, "option a with arg"));
        options.addOption(new Option("b", false, "flag b"));
        options.addOption(new Option("f", "file", true, "file option"));

        Option typedString = new Option("s", true, "string typed");
        typedString.setType(String.class);
        options.addOption(typedString);

        Option number = new Option("n", true, "number typed");
        number.setType(Number.class);
        options.addOption(number);

        Option multi = new Option("m", true, "two values");
        multi.setArgs(2);
        options.addOption(multi);

        return options;
    }

    private CommandLine parse(String[] args) throws Exception
    {
        return new BasicParser().parse(buildOptions(), args);
    }

    // ---------- hasOption ----------

    public void testHasOptionOnEmptyCommandLineIsFalse()
    {
        CommandLine cl = new CommandLine();
        assertFalse(cl.hasOption("a"));
        assertFalse(cl.hasOption('a'));
    }

    public void testHasOptionStringAfterAddOption()
    {
        CommandLine cl = new CommandLine();
        cl.addOption(new Option("x", "desc"));
        assertTrue(cl.hasOption("x"));
        assertFalse(cl.hasOption("y"));
    }

    public void testHasOptionCharAfterAddOption()
    {
        CommandLine cl = new CommandLine();
        cl.addOption(new Option("x", "desc"));
        assertTrue(cl.hasOption('x'));
        assertFalse(cl.hasOption('y'));
    }

    public void testHasOptionByLongNameAndLeadingHyphens()
    {
        CommandLine cl = new CommandLine();
        cl.addOption(new Option("f", "file", true, "file option"));
        assertTrue(cl.hasOption("f"));
        assertTrue(cl.hasOption("file"));
        assertTrue(cl.hasOption("-f"));
        assertTrue(cl.hasOption("--file"));
        assertFalse(cl.hasOption("other"));
    }

    public void testHasOptionFromParsedArguments() throws Exception
    {
        CommandLine cl = parse(new String[] { "-b", "-a", "val" });
        assertTrue(cl.hasOption('b'));
        assertTrue(cl.hasOption("a"));
        assertFalse(cl.hasOption('f'));
    }

    // ---------- getOptionValue / getOptionValues ----------

    public void testGetOptionValueStringReturnsFirstValue() throws Exception
    {
        CommandLine cl = parse(new String[] { "-a", "alpha" });
        assertEquals("alpha", cl.getOptionValue("a"));
    }

    public void testGetOptionValueCharReturnsValue() throws Exception
    {
        CommandLine cl = parse(new String[] { "-a", "alpha" });
        assertEquals("alpha", cl.getOptionValue('a'));
    }

    public void testGetOptionValueByLongName() throws Exception
    {
        CommandLine cl = parse(new String[] { "--file", "data.txt" });
        assertEquals("data.txt", cl.getOptionValue("file"));
        assertEquals("data.txt", cl.getOptionValue("f"));
        assertEquals("data.txt", cl.getOptionValue("--file"));
    }

    public void testGetOptionValueForUnsetOptionIsNull() throws Exception
    {
        CommandLine cl = parse(new String[] { "-b" });
        assertNull(cl.getOptionValue("a"));
        assertNull(cl.getOptionValue('a'));
    }

    public void testGetOptionValueOnEmptyCommandLineIsNull()
    {
        CommandLine cl = new CommandLine();
        assertNull(cl.getOptionValue("a"));
        assertNull(cl.getOptionValue('a'));
    }

    public void testGetOptionValueWithDefaultStringWhenSet() throws Exception
    {
        CommandLine cl = parse(new String[] { "-a", "alpha" });
        assertEquals("alpha", cl.getOptionValue("a", "dflt"));
    }

    public void testGetOptionValueWithDefaultStringWhenUnset() throws Exception
    {
        CommandLine cl = parse(new String[] { "-b" });
        assertEquals("dflt", cl.getOptionValue("a", "dflt"));
    }

    public void testGetOptionValueWithDefaultCharWhenSet() throws Exception
    {
        CommandLine cl = parse(new String[] { "-a", "alpha" });
        assertEquals("alpha", cl.getOptionValue('a', "dflt"));
    }

    public void testGetOptionValueWithDefaultCharWhenUnset() throws Exception
    {
        CommandLine cl = parse(new String[] { "-b" });
        assertEquals("dflt", cl.getOptionValue('a', "dflt"));
    }

    public void testGetOptionValueWithNullDefaultWhenUnset()
    {
        CommandLine cl = new CommandLine();
        assertNull(cl.getOptionValue("a", null));
        assertNull(cl.getOptionValue('a', null));
    }

    public void testGetOptionValuesStringSingleValue() throws Exception
    {
        CommandLine cl = parse(new String[] { "-a", "alpha" });
        String[] values = cl.getOptionValues("a");
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("alpha", values[0]);
    }

    public void testGetOptionValuesCharMultipleValues() throws Exception
    {
        CommandLine cl = parse(new String[] { "-m", "one", "two" });
        String[] values = cl.getOptionValues('m');
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("one", values[0]);
        assertEquals("two", values[1]);
        assertEquals("one", cl.getOptionValue('m'));
    }

    public void testGetOptionValuesForUnsetOptionIsNull()
    {
        CommandLine cl = new CommandLine();
        assertNull(cl.getOptionValues("a"));
        assertNull(cl.getOptionValues('a'));
    }

    // ---------- getOptionObject ----------

    public void testGetOptionObjectStringTypeReturnsString() throws Exception
    {
        CommandLine cl = parse(new String[] { "-s", "text" });
        assertEquals("text", cl.getOptionObject("s"));
    }

    public void testGetOptionObjectCharNumberTypeReturnsNumber() throws Exception
    {
        CommandLine cl = parse(new String[] { "-n", "42" });
        Object value = cl.getOptionObject('n');
        assertNotNull(value);
        assertTrue(value instanceof Number);
        assertEquals(42, ((Number) value).intValue());
    }

    public void testGetOptionObjectForUnknownOptionIsNull()
    {
        CommandLine cl = new CommandLine();
        assertNull(cl.getOptionObject("zzz"));
        assertNull(cl.getOptionObject('z'));
    }

    public void testGetOptionObjectForOptionWithoutValueIsNull()
    {
        CommandLine cl = new CommandLine();
        cl.addOption(new Option("x", "desc"));
        assertNull(cl.getOptionObject("x"));
    }

    // ---------- args ----------

    public void testGetArgsEmptyByDefault()
    {
        CommandLine cl = new CommandLine();
        assertNotNull(cl.getArgs());
        assertEquals(0, cl.getArgs().length);
    }

    public void testAddArgAndGetArgsPreserveOrder()
    {
        CommandLine cl = new CommandLine();
        cl.addArg("one");
        cl.addArg("two");
        String[] args = cl.getArgs();
        assertEquals(2, args.length);
        assertEquals("one", args[0]);
        assertEquals("two", args[1]);
    }

    public void testGetArgsReturnsCopyOfInternalList()
    {
        CommandLine cl = new CommandLine();
        cl.addArg("one");
        String[] args = cl.getArgs();
        args[0] = "changed";
        assertEquals("one", cl.getArgs()[0]);
        assertEquals("one", cl.getArgList().get(0));
    }

    public void testGetArgListReflectsAddedArgs()
    {
        CommandLine cl = new CommandLine();
        List list = cl.getArgList();
        assertNotNull(list);
        assertEquals(0, list.size());
        cl.addArg("first");
        assertEquals(1, cl.getArgList().size());
        assertEquals("first", cl.getArgList().get(0));
    }

    public void testUnrecognizedTokensAreCollectedAsArgs() throws Exception
    {
        CommandLine cl = parse(new String[] { "-b", "leftover1", "leftover2" });
        String[] args = cl.getArgs();
        assertEquals(2, args.length);
        assertEquals("leftover1", args[0]);
        assertEquals("leftover2", args[1]);
    }

    // ---------- options / iterator ----------

    public void testGetOptionsEmptyByDefault()
    {
        CommandLine cl = new CommandLine();
        Option[] opts = cl.getOptions();
        assertNotNull(opts);
        assertEquals(0, opts.length);
    }

    public void testGetOptionsContainsAddedOptions()
    {
        CommandLine cl = new CommandLine();
        Option a = new Option("a", "first");
        Option b = new Option("b", "second");
        cl.addOption(a);
        cl.addOption(b);
        Option[] opts = cl.getOptions();
        assertEquals(2, opts.length);
        boolean foundA = false;
        boolean foundB = false;
        for (int i = 0; i < opts.length; i++)
        {
            if (opts[i] == a) { foundA = true; }
            if (opts[i] == b) { foundB = true; }
        }
        assertTrue(foundA);
        assertTrue(foundB);
    }

    public void testAddSameOptionTwiceIsStoredOnce()
    {
        CommandLine cl = new CommandLine();
        Option a = new Option("a", "first");
        cl.addOption(a);
        cl.addOption(a);
        assertEquals(1, cl.getOptions().length);
    }

    
}
