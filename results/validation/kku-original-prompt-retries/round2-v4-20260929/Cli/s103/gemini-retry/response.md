```java
package org.apache.commons.cli;

import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;

/**
 * Deterministic regression tests for CommandLine covering normal paths,
 * boundary conditions, and option resolution.
 */
public class CommandLineRegressionTest extends TestCase {

    private CommandLine cmd;

    protected void setUp() throws Exception {
        super.setUp();
        cmd = new CommandLine();
    }

    public void testEmptyCommandLineState() {
        assertFalse(cmd.hasOption('a'));
        assertFalse(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue('a'));
        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValues('a'));
        assertNull(cmd.getOptionValues("a"));
        assertNull(cmd.getOptionObject('a'));
        assertNull(cmd.getOptionObject("a"));

        String[] args = cmd.getArgs();
        assertNotNull(args);
        assertEquals(0, args.length);

        List argList = cmd.getArgList();
        assertNotNull(argList);
        assertEquals(0, argList.size());

        Option[] options = cmd.getOptions();
        assertNotNull(options);
        assertEquals(0, options.length);

        Iterator it = cmd.iterator();
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    public void testAddArgAndGetArgs() {
        cmd.addArg("first");
        cmd.addArg("second");

        String[] args = cmd.getArgs();
        assertEquals(2, args.length);
        assertEquals("first", args[0]);
        assertEquals("second", args[1]);

        List list = cmd.getArgList();
        assertEquals(2, list.size());
        assertEquals("first", list.get(0));
        assertEquals("second", list.get(1));
    }

    public void testAddArgWithNull() {
        cmd.addArg(null);
        String[] args = cmd.getArgs();
        assertEquals(1, args.length);
        assertNull(args[0]);

        List list = cmd.getArgList();
        assertEquals(1, list.size());
        assertNull(list.get(0));
    }

    public void testHasOptionShortAndLongOpt() {
        Option opt = new Option("a", "alpha", false, "desc");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption('a'));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("alpha"));

        assertFalse(cmd.hasOption('b'));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("beta"));
    }

    public void testHasOptionWithLeadingHyphens() {
        Option opt = new Option("a", "alpha", false, "desc");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("-a"));
        assertTrue(cmd.hasOption("--a"));
        assertTrue(cmd.hasOption("-alpha"));
        assertTrue(cmd.hasOption("--alpha"));
    }

    public void testGetOptionValueString() {
        Option opt = new Option("f", "file", true, "filename");
        opt.addValue("output.txt");
        cmd.addOption(opt);

        assertEquals("output.txt", cmd.getOptionValue("f"));
        assertEquals("output.txt", cmd.getOptionValue("file"));
        assertEquals("output.txt", cmd.getOptionValue("-f"));
        assertEquals("output.txt", cmd.getOptionValue("--file"));
        assertNull(cmd.getOptionValue("nonexistent"));
    }

    public void testGetOptionValueChar() {
        Option opt = new Option("f", "file", true, "filename");
        opt.addValue("output.txt");
        cmd.addOption(opt);

        assertEquals("output.txt", cmd.getOptionValue('f'));
        assertNull(cmd.getOptionValue('z'));
    }

    public void testGetOptionValueStringWithDefaultValue() {
        Option opt = new Option("f", "file", true, "filename");
        cmd.addOption(opt);

        // Option present but no value added
        assertEquals("default.txt", cmd.getOptionValue("f", "default.txt"));
        assertEquals("default.txt", cmd.getOptionValue("file", "default.txt"));

        // Option absent
        assertEquals("default.txt", cmd.getOptionValue("unknown", "default.txt"));
        assertNull(cmd.getOptionValue("unknown", null));

        // Option present with value
        opt.addValue("actual.txt");
        assertEquals("actual.txt", cmd.getOptionValue("f", "default.txt"));
        assertEquals("actual.txt", cmd.getOptionValue("file", "default.txt"));
    }

    public void testGetOptionValueCharWithDefaultValue() {
        Option opt = new Option("f", "file", true, "filename");
        cmd.addOption(opt);

        assertEquals("default.txt", cmd.getOptionValue('f', "default.txt"));
        assertEquals("default.txt", cmd.getOptionValue('z', "default.txt"));
        assertNull(cmd.getOptionValue('z', null));

        opt.addValue("actual.txt");
        assertEquals("actual.txt", cmd.getOptionValue('f', "default.txt"));
    }

    public void testGetOptionValuesString() {
        Option opt = new Option("m", "multi", true, "multiple values");
        opt.setArgs(3);
        opt.addValue("v1");
        opt.addValue("v2");
        opt.addValue("v3");
        cmd.addOption(opt);

        String[] vals = cmd.getOptionValues("m");
        assertNotNull(vals);
        assertEquals(3, vals.length);
        assertEquals("v1", vals[0]);
        assertEquals("v2", vals[1]);
        assertEquals("v3", vals[2]);

        String[] valsLong = cmd.getOptionValues("multi");
        assertNotNull(valsLong);
        assertEquals(3, valsLong.length);

        assertNull(cmd.getOptionValues("unknown"));
    }

    public void testGetOptionValuesChar() {
        Option opt = new Option("m", "multi", true, "multiple values");
        opt.setArgs(2);
        opt.addValue("first");
        opt.addValue("second");
        cmd.addOption(opt);

        String[] vals = cmd.getOptionValues('m');
        assertNotNull(vals);
        assertEquals(2, vals.length);
        assertEquals("first", vals[0]);
        assertEquals("second", vals[1]);

        assertNull(cmd.getOptionValues('z
```