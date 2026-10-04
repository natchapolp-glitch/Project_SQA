package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Properties;

public class ParserTest {

    private Parser newParser() {
        return new GnuParser();
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptionThrows() throws Exception {
        Options options = new Options();
        options.addOption(OptionBuilder.isRequired().create("a"));

        Parser parser = newParser();
        parser.parse(options, new String[] {});
    }

    @Test
    public void testRequiredOptionPresentSucceeds() throws Exception {
        Options options = new Options();
        options.addOption(OptionBuilder.isRequired().create("a"));

        Parser parser = newParser();
        CommandLine cmd = parser.parse(options, new String[] { "-a" });

        assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParserStateResetAcrossInvocations() throws Exception {
        Options firstOptions = new Options();
        firstOptions.addOption(OptionBuilder.isRequired().create("a"));

        Parser parser = newParser();

        try {
            parser.parse(firstOptions, new String[] {});
            fail("Expected MissingOptionException");
        }
        catch (MissingOptionException expected) {
            // expected
        }

        Options secondOptions = new Options();
        secondOptions.addOption(OptionBuilder.create("b"));

        CommandLine cmd = parser.parse(secondOptions, new String[] { "-b" });
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testMultipleMissingRequiredOptionsReportedTogether() throws Exception {
        Options options = new Options();
        options.addOption(OptionBuilder.isRequired().create("a"));
        options.addOption(OptionBuilder.isRequired().create("b"));

        Parser parser = newParser();

        try {
            parser.parse(options, new String[] {});
            fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e) {
            String message = e.getMessage();
            assertTrue(message.contains("a"));
            assertTrue(message.contains("b"));
        }
    }

    @Test
    public void testRequiredOptionSatisfiedByProperties() throws Exception {
        Options options = new Options();
        options.addOption(OptionBuilder.hasArg().isRequired().create("a"));

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        Parser parser = newParser();
        CommandLine cmd = parser.parse(options, new String[] {}, properties);

        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testNoRequiredOptionsSucceedsWithArbitraryArgs() throws Exception {
        Options options = new Options();
        options.addOption(OptionBuilder.create("x"));

        Parser parser = newParser();
        CommandLine cmd = parser.parse(options, new String[] { "arg1", "arg2" });

        assertEquals(2, cmd.getArgs().length);
        assertFalse(cmd.hasOption("x"));
    }
}
