package org.apache.commons.cli;

import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;

/**
 * Regression tests for {@link CommandLine}.
 */
public class CommandLineRegressionTest extends TestCase {

    public void testEmptyCommandLine() {
        CommandLine cmd = new CommandLine();

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
        CommandLine cmd = new CommandLine();
        cmd.addArg("first");

        String[] args = cmd.getArgs();
        assertNotNull(args);
        assertEquals(1, args.length);
        assertEquals("first", args[0]);

        List argList = cmd.getArgList();
        assertNotNull(argList);
        assertEquals(1, argList.size());
        assertEquals("first", argList.get(0));
    }

    public void testAddMultipleArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");
        cmd.addArg("arg3");

        String[] args = cmd.getArgs();
        assertEquals(3, args.length);
        assertEquals("arg1", args[0]);
        assertEquals("arg2", args[1]);
        assertEquals("arg3", args[2]);

        List list = cmd.getArgList();
        assertEquals(3, list.size());
        assertEquals("arg1", list.get(0));
        assertEquals("arg2", list.get(1));
        assertEquals("arg3", list.get(2));
    }

    public void testAddNullArg() {
        CommandLine cmd = new CommandLine();
        cmd.addArg(null);

        String[] args = cmd.getArgs();
        assertEquals(1, args.length);
        assertNull(args[0]);

        List list = cmd.getArgList();
        assertEquals(1, list.size());
        assertNull(list.get(0));
    }

    public void testGetArgListIsDirectReference() {
        CommandLine cmd = new CommandLine();
        List list = cmd.getArgList();
        list.add("injected");

        assertEquals(1, cmd.getArgs().length);
        assertEquals("injected", cmd.getArgs()[0]);
    }

    public void testAddOptionAndGetOptions() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("a", "alpha");
        cmd.addOption(opt);

        Option[] options = cmd.getOptions();
        assertNotNull(options);
        assertEquals(1, options.length);
        assertEquals(opt, options[0]);
    }

    public void testAddDuplicateOption() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("a", "alpha");
        cmd.addOption(opt);
        cmd.addOption(opt);

        Option[] options = cmd.getOptions();
        assertEquals(1, options.length);
        assertEquals(opt, options[0]);
    }

    public void testIterator() {
        CommandLine cmd = new CommandLine();
        Option optA = new Option("a", "first");
        Option optB = new Option("b", "second");
        cmd.addOption(optA);
        cmd.addOption(optB);

        Iterator it = cmd.iterator();
        assertNotNull(it);
        int count = 0;
        while (it.hasNext()) {
            Object obj = it.next();
            assertTrue(obj instanceof Option);
            count++;
        }
        assertEquals(2, count);
    }

    public void testHasOptionStringShort() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("s", "short-opt", false, "desc");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("s"));
    }

    public void testHasOptionStringLong() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("s", "short-opt", false, "desc");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("short-opt"));
    }

    public void testHasOptionChar() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("c", "char-opt", false, "desc");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption('c'));
        assertFalse(cmd.hasOption('z'));
    }

    public void testHasOptionHyphens() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("f", "file", false, "file option");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("-f"));
        assertTrue(cmd.hasOption("--file"));
    }

    public void testHasOptionNotFound() {
        CommandLine cmd = new CommandLine();
        Option opt = new Option("x", "exist", false, "existing option");
        cmd.addOption(opt);

        assertFalse(cmd.hasOption("y"));
        assertFalse(cmd.hasOption("--none"));
        assertFalse(cmd.hasOption('y'));
    }
}
