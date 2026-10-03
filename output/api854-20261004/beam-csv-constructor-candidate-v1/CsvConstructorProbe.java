package org.apache.commons.csv;

import java.io.*;
import java.lang.reflect.*;

/** Bounded real StringReader inputs; constructor state and ownership observations. */
public final class CsvConstructorProbe {
    public static String activeCase = "";
    public static final String[] CASES = {"empty", "ascii", "crlf", "unicode", "null_reader"};
    static String input(String name) {
        if (name.equals("empty")) return "";
        if (name.equals("ascii")) return "abc";
        if (name.equals("crlf")) return "\r\nnext";
        if (name.equals("unicode")) return "\u0e01\ud83d\ude00";
        if (name.equals("null_reader")) return null;
        throw new IllegalArgumentException(name);
    }
    static final class TrackedReader extends Reader {
        final StringReader delegate;
        int reads, closes;
        TrackedReader(String data) { delegate = new StringReader(data); }
        public int read(char[] data, int offset, int length) throws IOException {
            reads++; return delegate.read(data, offset, length);
        }
        public void close() { closes++; delegate.close(); }
    }
    static String observation(String name) throws Exception {
        String data = input(name);
        TrackedReader supplied = data == null ? null : new TrackedReader(data);
        ExtendedBufferedReader reader = null;
        String exception = "null";
        // The fixture is ready before activating the exact production constructor.
        activeCase = name;
        try { reader = new ExtendedBufferedReader(supplied); }
        catch (NullPointerException error) { exception = "\"java.lang.NullPointerException\""; }
        finally { activeCase = ""; }
        if (reader == null) {
            if (data != null) throw new IllegalStateException("SQA_HARNESS non-null constructor failed");
            return "{\"exception_class\":" + exception + ",\"initial_last\":null,\"initial_lines\":null,\"initial_reads\":null,\"initial_closes\":null,\"first\":null,\"last_after_first\":null,\"lines_after_first\":null,\"close_count\":null}";
        }
        // Read constructor-initialized fields before any follow-up method can alter them.
        Field last = ExtendedBufferedReader.class.getDeclaredField("lastChar");
        Field lines = ExtendedBufferedReader.class.getDeclaredField("lineCounter");
        last.setAccessible(true); lines.setAccessible(true);
        int initialLast = last.getInt(reader), initialLines = lines.getInt(reader);
        int initialReads = supplied.reads, initialCloses = supplied.closes;
        int first = reader.read(), afterLast = reader.readAgain(), afterLines = reader.getLineNumber();
        reader.close();
        return "{\"exception_class\":" + exception + ",\"initial_last\":" + initialLast
            + ",\"initial_lines\":" + initialLines + ",\"initial_reads\":" + initialReads
            + ",\"initial_closes\":" + initialCloses + ",\"first\":" + first
            + ",\"last_after_first\":" + afterLast + ",\"lines_after_first\":" + afterLines
            + ",\"close_count\":" + supplied.closes + "}";
    }
    public static void main(String[] args) throws Exception {
        for (String name : CASES)
            System.out.println("{\"case\":\"" + name + "\",\"observation\":" + observation(name) + "}");
    }
}
