package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testLongOptionWithEquals() {
        Options options = new Options();
        options.addOption("f", "file", true, "file option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[] { "--file=test.txt" }, true);

        assertArrayEquals(new String[] { "--file", "test.txt" }, result);
    }

    @Test
    public void testLongOptionWithoutEquals() {
        Options options = new Options();
        options.addOption("v", "verbose", false, "verbose flag");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[] { "--verbose" }, true);

        assertArrayEquals(new String[] { "--verbose" }, result);
    }

    @Test
    public void testBurstingClusteredFlags() {
        Options options = new Options();
        options.addOption("a", false, "a flag");
        options.addOption("b", false, "b flag");
        options.addOption("c", false, "c flag");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[] { "-abc" }, true);

        assertArrayEquals(new String[] { "-a", "-b", "-c" }, result);
    }

    @Test
    public void testShortOptionWithArgument() {
        Options options = new Options();
        options.addOption("f", true, "file option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[] { "-f", "test.txt" }, true);

        assertArrayEquals(new String[] { "-f", "test.txt" }, result);
    }

    @Test
    public void testDoubleHyphenStopsProcessing() {
        Options options = new Options();
        options.addOption("a", false, "a flag");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[] { "-a", "--", "-b", "value" }, true);

        assertArrayEquals(new String[] { "-a", "--", "-b", "value" }, result);
    }

    @Test
    public void testUnrecognizedOptionStopsAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "a flag");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[] { "-a", "-x", "value" }, true);

        assertArrayEquals(new String[] { "-a" }, result);
    }

    @Test
    public void testUnrecognizedOptionNoStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "a flag");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[] { "foo", "-a" }, false);

        assertArrayEquals(new String[] { "foo", "-a" }, result);
    }

    @Test
    public void testLoneHyphenTreatedAsToken() {
        Options options = new Options();
        options.addOption("a", false, "a flag");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[] { "-", "-a" }, true);

        assertArrayEquals(new String[] { "-", "-a" }, result);
    }

    @Test
    public void testEmptyArgumentsArray() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[0], true);

        assertEquals(0, result.length);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentsThrowsException() {
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, null, true);
    }
}
