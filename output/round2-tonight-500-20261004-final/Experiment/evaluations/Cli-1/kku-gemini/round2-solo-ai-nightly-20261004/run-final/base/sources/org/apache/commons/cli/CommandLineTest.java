package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOptionStringAndChar() throws Exception {
        CommandLine cmd = new CommandLine();
        Option option = new Option("a", "all", false, "display all");
        cmd.addOption(option);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("all"));
        assertTrue(cmd.hasOption('a'));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption('b'));
        assertFalse(cmd.hasOption((String) null));
    }

    @Test
    public void testGetOptionValueSingle() throws Exception {
        CommandLine cmd = new CommandLine();
        Option option = new Option("b", "build", true, "build target");
        option.addValue("release");
        cmd.addOption(option);

        assertEquals("release", cmd.getOptionValue("b"));
        assertEquals("release", cmd.getOptionValue("build"));
        assertEquals("release", cmd.getOptionValue('b'));
        assertNull(cmd.getOptionValue("nonexistent"));
        assertNull(cmd.getOptionValue('z'));
    }

    @Test
    public void testGetOptionValueWithDefault() throws Exception {
        CommandLine cmd = new CommandLine();
        Option option = new Option("t", "type", true, "type");
        option.addValue("fast");
        cmd.addOption(option);

        assertEquals("fast", cmd.getOptionValue("t", "default"));
        assertEquals("default", cmd.getOptionValue("missing", "default"));
        assertEquals("fast", cmd.getOptionValue('t', "default"));
        assertEquals("default", cmd.getOptionValue('z', "default"));
    }

    @Test
    public void testGetOptionValuesMultiple() throws Exception {
        CommandLine cmd = new CommandLine();
        Option option = new Option("f", "file", true, "files");
        option.addValue("file1.txt");
        option.addValue("file2.txt");
        cmd.addOption(option);

        String[] values1 = cmd.getOptionValues("f");
        String[] values2 = cmd.getOptionValues("file");
        String[] values3 = cmd.getOptionValues('f');

        assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, values1);
        assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, values2);
        assertArrayEquals(new String[]{"file1.txt", "file2.txt"}, values3);
        assertNull(cmd.getOptionValues("missing"));
        assertNull(cmd.getOptionValues('z'));
    }

    @Test
    public void testGetArgsAndArgList() throws Exception {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");
        cmd.addArg("arg2");

        assertArrayEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        List list = cmd.getArgList();
        assertNotNull(list);
        assertEquals(2, list.size());
        assertEquals("arg1", list.get(0));
        assertEquals("arg2", list.get(1));
    }

    @Test
    public void testIteratorAndGetOptions() throws Exception {
        CommandLine cmd = new CommandLine();
        Option opt1 = new Option("a", false, "opt a");
        Option opt2 = new Option("b", false, "opt b");
        cmd.addOption(opt1);
        cmd.addOption(opt2);

        Option[] options = cmd.getOptions();
        assertNotNull(options);
        assertEquals(2, options.length);

        Iterator iterator = cmd.iterator();
        assertNotNull(iterator);
        int count = 0;
        while (iterator.hasNext()) {
            assertNotNull(iterator.next());
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testGetOptionObject() throws Exception {
        CommandLine cmd = new CommandLine();
        Option option = new Option("n", "number", true, "number option");
        option.setType(Number.class);
        option.addValue("123");
        cmd.addOption(option);

        Object objStr = cmd.getOptionObject("n");
        Object objChar = cmd.getOptionObject('n');
        assertNotNull(objStr);
        assertEquals(Integer.valueOf(123), objStr);
        assertEquals(objStr, objChar);
        assertNull(cmd.getOptionObject("missing"));
    }
}
