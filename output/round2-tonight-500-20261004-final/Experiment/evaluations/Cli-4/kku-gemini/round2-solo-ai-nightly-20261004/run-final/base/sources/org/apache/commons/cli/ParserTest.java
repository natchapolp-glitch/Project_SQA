package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;
import org.junit.Test;

public class ParserTest {

    private static class ConcreteParser extends Parser {
        @Override
        protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
            return arguments;
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(OptionBuilder.isRequired().withLongOpt("required").create("r"));
        Parser parser = new ConcreteParser();
        parser.parse(options, new String[0]);
    }

    @Test
    public void testParseWithSatisfiedRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption(OptionBuilder.isRequired().withLongOpt("required").create("r"));
        Parser parser = new ConcreteParser();
        CommandLine cl = parser.parse(options, new String[] { "-r" });
        assertNotNull(cl);
        assertTrue(cl.hasOption("r"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("p", "prop", false, "property option"));
        Properties props = new Properties();
        props.setProperty("p", "true");

        Parser parser = new ConcreteParser();
        CommandLine cl = parser.parse(options, new String[0], props);
        assertNotNull(cl);
        assertTrue(cl.hasOption("p"));
    }

    @Test
    public void testParseNullArguments() throws ParseException {
        Options options = new Options();
        Parser parser = new ConcreteParser();
        CommandLine cl = parser.parse(options, null);
        assertNotNull(cl);
        assertEquals(0, cl.getArgs().length);
    }

    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        Parser parser = new ConcreteParser();
        CommandLine cl = parser.parse(options, new String[] { "non-option" }, true);
        assertNotNull(cl);
        assertEquals(1, cl.getArgs().length);
        assertEquals("non-option", cl.getArgs()[0]);
    }
}
