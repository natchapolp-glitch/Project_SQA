org/apache/commons/cli/CommandLineRegressionTest.java
```java
package org.apache.commons.cli;

import junit.framework.TestCase;
import java.util.Iterator;
import java.util.List;

/**
 * Regression test suite for {@link CommandLine}.
 */
public class CommandLineRegressionTest extends TestCase {

    /**
     * Subclass of Option allowing controlled value and type behavior.
     */
    private static class TestOption extends Option {
        private String[] values;

        TestOption(String opt, String longOpt) {
            super(opt, longOpt, false, null);
        }

        TestOption(String opt, String longOpt, String[] values) {
            super(opt, longOpt, values != null && values.length > 0, null);
            this.values = values;
        }

        public String[] getValues() {
            return values;
        }
    }

    public void testEmptyCommandLine() {
        CommandLine cmd = new CommandLine();

        assertNotNull(cmd.getArgs());
        assertEquals(0, cmd.getArgs().length);
        assertNotNull(cmd.getArgList());
        assertEquals(0, cmd.getArgList().size());

        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption('a'));

        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue('a'));
        assertNull(cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues('a'));

        assertNotNull(cmd.getOptions());
        assertEquals(0, cmd.getOptions().length);
        assertFalse(cmd.iterator().hasNext());
    }

    public void testAddArgAndGetArgs() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("arg1");

        String[] args = cmd.getArgs();
        assertEquals(1, args.length);
        assertEquals("arg1", args[0]);

        List list = cmd.getArgList();
        assertEquals(1, list.size());
        assertEquals("arg1", list.get(0));
    }

    public void testAddMultipleArgsOrder() {
        CommandLine cmd = new CommandLine();
        cmd.addArg("first");
        cmd.addArg("second");
        cmd.addArg("third");

        String[] args = cmd.getArgs();
        assertEquals(3, args.length);
        assertEquals("first", args[0]);
        assertEquals("second", args[1]);
        assertEquals("third", args[2]);

        List list = cmd.getArgList();
        assertEquals(3, list.size());
        assertEquals("first", list.get(0));
        assertEquals("second", list.get(1));
        assertEquals("third", list.get(2));
    }

    public void testAddArgNull() {
        CommandLine cmd = new CommandLine();
        cmd.addArg(null);

        String[] args = cmd.getArgs();
        assertEquals(1, args.length);
        assertNull(args[0]);
        assertEquals(1, cmd.getArgList().size());
        assertNull(cmd.getArgList().get(0));
    }

    public void testGetArgListModifiable() {
        CommandLine cmd = new CommandLine();
        cmd.getArgList().add("external");

        assertEquals(1, cmd.getArgs().length);
        assertEquals("external", cmd.getArgs()[0]);
    }

    public void testHasOptionStringShort() {
        CommandLine cmd = new CommandLine();
        TestOption opt = new TestOption("a", "alpha");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
    }

    public void testHasOptionStringWithSingleHyphen() {
        CommandLine cmd = new CommandLine();
        TestOption opt = new TestOption("a", "alpha");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("-a"));
        assertFalse(cmd.hasOption("-b"));
    }

    public void testHasOptionStringWithDoubleHyphen() {
        CommandLine cmd = new CommandLine();
        TestOption opt = new TestOption("a", "alpha");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("--a"));
        assertFalse(cmd.hasOption("--b"));
    }

    public void testHasOptionChar() {
        CommandLine cmd = new CommandLine();
        TestOption opt = new TestOption("a", "alpha");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption('a'));
        assertFalse(cmd.hasOption('b'));
    }

    public void testHasOptionLongOpt() {
        CommandLine cmd = new CommandLine();
        TestOption opt = new TestOption("a", "alpha");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("alpha"));
        assertFalse(cmd.hasOption("beta"));
    }

    public void testHasOptionLongOptWithHyphens() {
        CommandLine cmd = new CommandLine();
        TestOption opt = new TestOption("a", "alpha");
        cmd.addOption(opt);

        assertTrue(cmd.hasOption("-alpha"));
        assertTrue(cmd.hasOption("--alpha"));
    }

    public void testHasOptionMissing() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption("nonexistent"));
        assertFalse(cmd.hasOption("-nonexistent"));
        assertFalse(cmd.hasOption("--nonexistent"));
    }

    public void testHasOptionCharMissing() {
        CommandLine cmd = new CommandLine();
        assertFalse(cmd.hasOption('z'));
    }

    public void testGetOptionValueString() {
        CommandLine cmd = new CommandLine();
        TestOption opt = new TestOption("a", "alpha", new String[]{"val1", "val2"});
        cmd.