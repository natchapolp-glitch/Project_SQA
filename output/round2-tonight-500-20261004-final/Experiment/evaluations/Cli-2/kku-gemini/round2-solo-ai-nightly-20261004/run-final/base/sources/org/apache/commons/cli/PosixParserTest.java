package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenLongOptionWithEquals() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("b", "block", false, "block size");

        String[] args = new String[] { "--block=1024" };
        String[] flattened = parser.flatten(options, args, false);

        assertArrayEquals(new String[] { "--block", "1024" }, flattened);
    }

    @Test
    public void testFlattenSingleHyphen() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] args = new String[] { "-" };
        String[] flattened = parser.flatten(options, args, false);

        assertArrayEquals(new String[] { "-" }, flattened);
    }

    @Test
    public void testFlattenDoubleHyphenStop() {
        PosixParser parser = new PosixParser();
        Options options = new Options();

        String[] args = new String[] { "--", "-f", "file.txt" };
        String[] flattened = parser.flatten(options, args, false);

        assertArrayEquals(new String[] { "--", "-f", "file.txt" }, flattened);
    }

    @Test
    public void testBurstTokenSimple() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");

        String[] args = new String[] { "-ab" };
        String[] flattened = parser.flatten(options, args, false);

        assertArrayEquals(new String[] { "-a", "-b" }, flattened);
    }

    @Test
    public void testStopAtNonOption() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = new String[] { "-a", "nonoption", "-a" };
        String[] flattened = parser.flatten(options, args, true);

        assertArrayEquals(new String[] { "-a", "--", "nonoption", "-a" }, flattened);
    }

    @Test
    public void testBurstTokenWithOptionsWithArg() {
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option a");
        Option optB = new Option("b", true, "option b");
        options.addOption(optB);

        String[] args = new String[] { "-abvalue" };
        String[] flattened = parser.flatten(options, args, false);

        assertArrayEquals(new String[] { "-a", "-b", "value" }, flattened);
    }
}
