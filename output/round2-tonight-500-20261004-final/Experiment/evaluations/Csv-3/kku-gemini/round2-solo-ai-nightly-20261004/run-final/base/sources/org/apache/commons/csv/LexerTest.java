package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

public class LexerTest {

    private static final class ConcreteLexer extends Lexer {
        ConcreteLexer(CSVFormat format, ExtendedBufferedReader in) {
            super(format, in);
        }

        @Override
        Token nextToken(Token reusableToken) throws IOException {
            return reusableToken;
        }
    }

    private Lexer createLexer(CSVFormat format, String input) {
        return new ConcreteLexer(format, new ExtendedBufferedReader(new StringReader(input)));
    }

    @Test
    public void testIsWhitespace() {
        CSVFormat format = CSVFormat.DEFAULT.withDelimiter(',');
        Lexer lexer = createLexer(format, "");
        assertTrue(lexer.isWhitespace(' '));
        assertTrue(lexer.isWhitespace('\t'));
        assertFalse(lexer.isWhitespace(','));
        assertFalse(lexer.isWhitespace('a'));
    }

    @Test
    public void testIsStartOfLine() {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = createLexer(format, "");
        assertTrue(lexer.isStartOfLine('\n'));
        assertTrue(lexer.isStartOfLine('\r'));
        assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));
        assertFalse(lexer.isStartOfLine('a'));
    }

    @Test
    public void testIsEndOfFile() {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = createLexer(format, "");
        assertTrue(lexer.isEndOfFile(Constants.END_OF_STREAM));
        assertFalse(lexer.isEndOfFile(0));
    }

    @Test
    public void testReadEscapeValid() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = createLexer(format, "n");
        assertEquals(Constants.LF, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscapeEof() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = createLexer(format, "");
        lexer.readEscape();
    }

    @Test
    public void testTrimTrailingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = createLexer(format, "");
        StringBuilder sb = new StringBuilder("abc   ");
        lexer.trimTrailingSpaces(sb);
        assertEquals("abc", sb.toString());
    }

    @Test
    public void testReadEndOfLine() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        Lexer lexer = createLexer(format, "\n");
        assertTrue(lexer.readEndOfLine('\n'));
        assertTrue(lexer.readEndOfLine('\r'));
        assertFalse(lexer.readEndOfLine('a'));
    }

    @Test
    public void testCharacterChecks() {
        CSVFormat format = CSVFormat.DEFAULT
                .withDelimiter(',')
                .withEscape('\\')
                .withQuoteChar('"')
                .withCommentStart('#');
        Lexer lexer = createLexer(format, "");

        assertTrue(lexer.isDelimiter(','));
        assertTrue(lexer.isEscape('\\'));
        assertTrue(lexer.isQuoteChar('"'));
        assertTrue(lexer.isCommentStart('#'));

        assertFalse(lexer.isDelimiter('a'));
        assertFalse(lexer.isEscape('a'));
        assertFalse(lexer.isQuoteChar('a'));
        assertFalse(lexer.isCommentStart('a'));
    }
}
