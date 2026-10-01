package org.apache.commons.cli;

import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;

/**
 * Regression tests for {@link CommandLine}.
 */
public class CommandLineRegressionTest extends TestCase {

    public CommandLineRegressionTest(String name) {
        super(name);
    }

    public void testEmptyCommandLine() {
        CommandLine cl = new CommandLine();

        assertFalse(cl.hasOption("opt"));
        assertFalse(cl.hasOption('o'));
        assertNull(cl.getOptionValue("opt"));
        assertNull(cl.getOptionValue('o'));
        assertNull(cl.getOptionValues("opt"));
        assertNull(cl.getOptionValues('o'));
        assertNull(cl.getOptionObject("opt"));
        assertNull(cl.getOptionObject('o'));

        assertEquals("default", cl.getOptionValue("opt", "default"));
        assertEquals("default", cl.getOptionValue('o', "default"));

        assertNotNull(cl.getArgs());
        assertEquals(0, cl.getArgs().length);
        assertNotNull(cl.getArgList());
        assertTrue(cl.getArgList().isEmpty());

        assertNotNull(cl.getOptions());
        assertEquals(0, cl.getOptions().length);

        Iterator it = cl.iterator();
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    public void testAddAndGetArgs() {
        CommandLine cl = new CommandLine();
        cl.addArg("arg1");
        cl.addArg("arg2");

        String[] args = cl.getArgs();
        assertNotNull(args);
        assertEquals(2, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);

        List argList = cl.getArgList();
        assertNotNull(argList);
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

    public void testAddArgWithNull() {
        CommandLine cl = new CommandLine();
        cl.addArg(null);

        String[] args = cl.getArgs();
        assertEquals(1, args.length);
        assertNull(args[0]);

        List argList = cl.getArgList();
        assertEquals(1, argList.size());
        assertNull(argList.get(0));
    }

    public void testHasOptionShort() {
        CommandLine cl = new CommandLine();
        Option optA = new Option("a", "option a");
        cl.addOption(optA);

        assertTrue(cl.hasOption("a"));
        assertTrue(cl.hasOption('a'));
        assertTrue(cl.hasOption("-a"));
        assertFalse(cl.hasOption("b"));
        assertFalse(cl.hasOption('b'));
    }

    public void testHasOptionLong() {
        CommandLine cl = new CommandLine();
        Option optLong = new Option("a", "alpha", false, "option alpha");
        cl.addOption(optLong);

        assertTrue(cl.hasOption("a"));
        assertTrue(cl.hasOption('a'));
        assertTrue(cl.hasOption("alpha"));
        assertTrue(cl.hasOption("--alpha"));
        assertTrue(cl.hasOption("-alpha"));
        assertFalse(cl.hasOption("beta"));
        assertFalse(cl.hasOption("--beta"));
    }

    public void testHasOptionLongOnly() {
        CommandLine cl = new CommandLine();
        Option optLongOnly = new Option(null, "config", false, "configuration");
        cl.addOption(optLongOnly);

        assertTrue(cl.hasOption("config"));
        assertTrue(cl.hasOption("--config"));
        assertFalse(cl.hasOption("c"));
        assertFalse(cl.hasOption('c'));
    }

    public void testGetOptionsAndIterator() {
        CommandLine cl = new CommandLine();
        Option opt1 = new Option("a", "first");
        Option opt2 = new Option("b", "second");

        cl.addOption(opt1);
        cl.addOption(opt2);

        Option[] options = cl.getOptions();
        assertNotNull(options);
        assertEquals(2, options.length);

        int count = 0;
        for (Iterator it = cl.iterator(); it.hasNext(); ) {
            Option opt = (Option) it.next();
            assertNotNull(opt);
            count++;
        }
        assertEquals(2, count);
    }

    public void testGetOptionValueSingle() {
        CommandLine cl = new CommandLine();
        Option opt = new Option("f", "file", true, "input file") {
            public String[] getValues() {
                return new String[] { "data.txt" };
            }
        };
        cl.addOption(opt);

        assertEquals("data.txt", cl.getOptionValue("f"));
        assertEquals("data.txt", cl.getOptionValue('f'));
        assertEquals("data.txt", cl.getOptionValue("-f"));
        assertEquals("data.txt", cl.getOptionValue("file"));
        assertEquals("data.txt", cl.getOptionValue("--file"));

        assertEquals("data.txt", cl.getOptionValue("f", "fallback.txt"));
        assertEquals("data.txt", cl.getOptionValue('f', "fallback.txt"));
    }

    public void testGetOptionValueDefaultWhenPresentWithoutValue() {
        CommandLine cl = new CommandLine();
        Option opt = new Option("n", "none", false, "flag with no value") {
            public String[] getValues() {
                return null;
            }
        };
        cl.addOption(opt);

        assertTrue(cl.hasOption("n"));
        assertNull(cl.getOptionValue("n"));
        assertEquals("fallback", cl.getOptionValue("n", "fallback"));
        assertEquals("fallback", cl.getOptionValue('n', "fallback"));
    }

    public void testGetOptionValueDefaultWhenOptionMissing() {
        CommandLine cl = new CommandLine();

        assertEquals("defaultVal", cl.getOptionValue("missing", "defaultVal"));
        assertEquals("defaultVal", cl.getOptionValue('m', "defaultVal"));
        assertNull(cl.getOptionValue("missing", null));
        assertNull(cl.getOptionValue('m', null));
    }

    public void testGetOptionValuesMultiple() {
        CommandLine cl = new CommandLine();
        Option opt = new Option("p", "property", true, "properties") {
            public String[] getValues() {
                return new String[] { "key1=val1", "key2=val2" };
            }
        };
        cl.addOption(opt);

        String[] valuesByShortStr = cl.getOptionValues("p");
        assertNotNull(valuesByShortStr);
        assertEquals(2, valuesByShortStr.length);
        assertEquals("key1=val1", valuesByShortStr[0]);
        assertEquals("key2=val2", valuesByShortStr[1]);

        String[] valuesByChar = cl.getOptionValues('p');
        assertNotNull(valuesByChar);
        assertEquals(2, valuesByChar.length);

        String[] valuesByLongStr = cl.getOptionValues("property");
        assertNotNull(valuesByLongStr);
        assertEquals(2, valuesByLongStr.length);

        assertEquals("key1=val1", cl.getOptionValue("p"));
        assertEquals("key1=val1", cl.getOptionValue('p'));
    }

    public void testGetOptionValuesForUnsetOption() {
        CommandLine cl = new CommandLine();
        assertNull(cl.getOptionValues("notSet"));
        assertNull(cl.getOptionValues('x'));
    }

    public void testGetOptionObjectMissingOption() {
        CommandLine cl = new CommandLine();
        assertNull(cl.getOptionObject("nonexistent"));
        assertNull(cl.getOptionObject('z'));
    }

    public void testGetOptionObjectOptionWithoutValue() {
        CommandLine cl = new CommandLine();
        Option opt = new Option("a", "alpha", false, "no argument") {
            public String[] getValues() {
                return null;
            }
        };
        cl.addOption(opt);

        assertNull(cl.getOptionObject("a"));
        assertNull(cl.getOptionObject('a'));
    }

    public void testGetOptionObjectWithStringValue() {
        CommandLine cl = new CommandLine();
        Option opt = new Option("s", "stringOpt", true, "string option") {
            public String[] getValues() {
                return new String[] { "hello" };
            }

            public Object getType() {
                return PatternOptionBuilder.STRING_VALUE;
            }
        };
        cl.addOption(opt);

        Object objByString = cl.getOptionObject("s");
        assertEquals("hello", objByString);

        Object objByChar = cl.getOptionObject('s');
        assertEquals("hello", objByChar);

        Object objByLong = cl.getOptionObject("stringOpt");
        assertEquals("hello", objByLong);
    }

    
}
