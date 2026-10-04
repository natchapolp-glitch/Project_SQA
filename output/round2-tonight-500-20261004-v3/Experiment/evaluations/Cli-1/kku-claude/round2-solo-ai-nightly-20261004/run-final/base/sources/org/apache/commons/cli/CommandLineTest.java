package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class CommandLineTest {

    private CommandLine cmd;

    @Before
    public void setUp() {
        cmd = new CommandLine();
    }

    @Test
    public void testHasOptionTrueAndFalse() throws Exception {
        Option opt = new Option("f", "file", false, "file option");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("f"));
        assertTrue(cmd.hasOption('f'));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption('x'));
    }

    @Test
    public void testGetOptionValueWithAndWithoutArgs() throws Exception {
        Option withArg = new Option("v", "value", true, "has value");
        withArg.addValue("hello");
        cmd.addOption(withArg);

        Option noArg = new Option("n", "noarg", false, "no value");
        cmd.addOption(noArg);

        assertEquals("hello", cmd.getOptionValue("v"));
        assertEquals("hello", cmd.getOptionValue('v'));
        assertNull(cmd.getOptionValue("n"));
        assertNull(cmd.getOptionValue("missing"));
    }

    @Test
    public void testGetOptionValueDefault() throws Exception {
        Option withArg = new Option("v", "value", true, "has value");
        withArg.addValue("present");
        cmd.addOption(withArg);

        assertEquals("present", cmd.getOptionValue("v", "default"));
        assertEquals("default", cmd.getOptionValue("missing", "default"));
        assertEquals("default", cmd.getOptionValue('m', "default"));
    }

    @Test
    public void testGetOptionValuesMultiple() throws Exception {
        Option multi = new Option("l", "list", true, "multi value");
        multi.addValue("one");
        multi.addValue("two");
        multi.addValue("three");
        cmd.addOption(multi);

        String[] values = cmd.getOptionValues("l");
        assertArrayEquals(new String[] {"one", "two", "three"}, values);
        assertArrayEquals(new String[] {"one", "two", "three"}, cmd.getOptionValues('l'));

        assertNull(cmd.getOptionValues("missing"));
    }

    @Test
    public void testGetArgsAndArgList() {
        assertEquals(0, cmd.getArgs().length);
        assertTrue(cmd.getArgList().isEmpty());

        cmd.addArg("arg1");
        cmd.addArg("arg2");

        String[] args = cmd.getArgs();
        assertArrayEquals(new String[] {"arg1", "arg2"}, args);

        List argList = cmd.getArgList();
        assertEquals(2, argList.size());
        assertEquals("arg1", argList.get(0));
        assertEquals("arg2", argList.get(1));
    }

    @Test
    public void testGetOptionsAndIterator() throws Exception {
        Option opt1 = new Option("a", "aaa", false, "a option");
        Option opt2 = new Option("b", "bbb", false, "b option");
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Option[] options = cmd.getOptions();
        assertEquals(2, options.length);

        Iterator it = cmd.iterator();
        int count = 0;
        while (it.hasNext()) {
            Object o = it.next();
            assertTrue(o instanceof Option);
            count++;
        }
        assertEquals(2, count);
    }
}
