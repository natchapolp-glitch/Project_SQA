# Defects4J unit test generation

You are a Java unit testing engineer. Write deterministic regression tests for the
production classes supplied below, using their documented and fixed-reference
behavior. This experiment measures test generation from fixed production source.

Produce Java test sources with assertions. Exercise normal cases, boundaries,
invalid inputs, exception paths and branches supported by the supplied source.
Inspect the supplied build configuration and use the JUnit version and dependencies
already available to this project. Defects4J projects can have different build
systems and JUnit versions; do not assume Maven or JUnit 5. Use Java 11 compatible
syntax unless the build configuration requires an older source level.

Constraints:

- Do not change production code or build files and do not add dependencies.
- Use the reference behavior to derive assertions; do not invent unsupported APIs.
- Avoid network access, external programs, wall-clock timing, random values without
  a fixed seed, machine-specific paths and environment-dependent assertions.
- Use test class names ending in `Test`, correctly matching Java file and package
  names. Place each source at its package-relative path, such as
  `org/example/GeneratedExampleTest.java`.
- Keep tests independent. Restore global state that a test changes.
- Do not ask for a bug patch, buggy revision, existing detecting test, or hidden
  evaluation results. Only the supplied reference source and build information may
  guide the initial generation.

Return each Java file in a separate fenced Java code block, preceded by its
package-relative path. Include complete imports and test class definitions. If the
context is insufficient to write a compiling test, explicitly state the missing
API or dependency rather than producing a fabricated result.

## Experiment context

Project: JacksonCore
Fixed reference revision: 1f
Target classes:
- com.fasterxml.jackson.core.io.NumberInput
- com.fasterxml.jackson.core.util.TextBuffer

## Production source src/main/java/com/fasterxml/jackson/core/io/NumberInput.java

```java
package com.fasterxml.jackson.core.io;

import java.math.BigDecimal;

public final class NumberInput
{
    /**
     * Textual representation of a double constant that can cause nasty problems
     * with JDK (see http://www.exploringbinary.com/java-hangs-when-converting-2-2250738585072012e-308).
     */
    public final static String NASTY_SMALL_DOUBLE = "2.2250738585072012e-308";

    /**
     * Constants needed for parsing longs from basic int parsing methods
     */
    final static long L_BILLION = 1000000000;

    final static String MIN_LONG_STR_NO_SIGN = String.valueOf(Long.MIN_VALUE).substring(1);
    final static String MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);
    
    /**
     * Fast method for parsing integers that are known to fit into
     * regular 32-bit signed int type. This means that length is
     * between 1 and 9 digits (inclusive)
     *<p>
     * Note: public to let unit tests call it
     */
    public static int parseInt(char[] digitChars, int offset, int len)
    {
        int num = digitChars[offset] - '0';
        len += offset;
        // This looks ugly, but appears the fastest way (as per measurements)
        if (++offset < len) {
            num = (num * 10) + (digitChars[offset] - '0');
            if (++offset < len) {
                num = (num * 10) + (digitChars[offset] - '0');
                if (++offset < len) {
                    num = (num * 10) + (digitChars[offset] - '0');
                    if (++offset < len) {
                        num = (num * 10) + (digitChars[offset] - '0');
                        if (++offset < len) {
                            num = (num * 10) + (digitChars[offset] - '0');
                            if (++offset < len) {
                                num = (num * 10) + (digitChars[offset] - '0');
                                if (++offset < len) {
                                    num = (num * 10) + (digitChars[offset] - '0');
                                    if (++offset < len) {
                                        num = (num * 10) + (digitChars[offset] - '0');
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return num;
    }

    /**
     * Helper method to (more) efficiently parse integer numbers from
     * String values.
     */
    public static int parseInt(String str)
    {
        /* Ok: let's keep strategy simple: ignoring optional minus sign,
         * we'll accept 1 - 9 digits and parse things efficiently;
         * otherwise just defer to JDK parse functionality.
         */
        char c = str.charAt(0);
        int length = str.length();
        boolean negative = (c == '-');
        int offset = 1;
        // must have 1 - 9 digits after optional sign:
        // negative?
        if (negative) {
            if (length == 1 || length > 10) {
                return Integer.parseInt(str);
            }
            c = str.charAt(offset++);
        } else {
            if (length > 9) {
                return Integer.parseInt(str);
            }
        }
        if (c > '9' || c < '0') {
            return Integer.parseInt(str);
        }
        int num = c - '0';
        if (offset < length) {
            c = str.charAt(offset++);
            if (c > '9' || c < '0') {
                return Integer.parseInt(str);
            }
            num = (num * 10) + (c - '0');
            if (offset < length) {
                c = str.charAt(offset++);
                if (c > '9' || c < '0') {
                    return Integer.parseInt(str);
                }
                num = (num * 10) + (c - '0');
                // Let's just loop if we have more than 3 digits:
                if (offset < length) {
                    do {
                        c = str.charAt(offset++);
                        if (c > '9' || c < '0') {
                            return Integer.parseInt(str);
                        }
                        num = (num * 10) + (c - '0');
                    } while (offset < length);
                }
            }
        }
        return negative ? -num : num;
    }
    
    public static long parseLong(char[] digitChars, int offset, int len)
    {
        // Note: caller must ensure length is [10, 18]
        int len1 = len-9;
        long val = parseInt(digitChars, offset, len1) * L_BILLION;
        return val + (long) parseInt(digitChars, offset+len1, 9);
    }

    public static long parseLong(String str)
    {
        /* Ok, now; as the very first thing, let's just optimize case of "fake longs";
         * that is, if we know they must be ints, call int parsing
         */
        int length = str.length();
        if (length <= 9) {
            return (long) parseInt(str);
        }
        // !!! TODO: implement efficient 2-int parsing...
        return Long.parseLong(str);
    }
    
    /**
     * Helper method for determining if given String representation of
     * an integral number would fit in 64-bit Java long or not.
     * Note that input String must NOT contain leading minus sign (even
     * if 'negative' is set to true).
     *
     * @param negative Whether original number had a minus sign (which is
     *    NOT passed to this method) or not
     */
    public static boolean inLongRange(char[] digitChars, int offset, int len,
            boolean negative)
    {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        if (len < cmpLen) return true;
        if (len > cmpLen) return false;

        for (int i = 0; i < cmpLen; ++i) {
            int diff = digitChars[offset+i] - cmpStr.charAt(i);
            if (diff != 0) {
                return (diff < 0);
            }
        }
        return true;
    }

    /**
     * Similar to {@link #inLongRange(char[],int,int,boolean)}, but
     * with String argument
     *
     * @param negative Whether original number had a minus sign (which is
     *    NOT passed to this method) or not
     */
    public static boolean inLongRange(String numberStr, boolean negative)
    {
        String cmpStr = negative ? MIN_LONG_STR_NO_SIGN : MAX_LONG_STR;
        int cmpLen = cmpStr.length();
        int actualLen = numberStr.length();
        if (actualLen < cmpLen) return true;
        if (actualLen > cmpLen) return false;

        // could perhaps just use String.compareTo()?
        for (int i = 0; i < cmpLen; ++i) {
            int diff = numberStr.charAt(i) - cmpStr.charAt(i);
            if (diff != 0) {
                return (diff < 0);
            }
        }
        return true;
    }

    public static int parseAsInt(String input, int defaultValue)
    {
        if (input == null) {
            return defaultValue;
        }
        input = input.trim();
        int len = input.length();
        if (len == 0) {
            return defaultValue;
        }
        // One more thing: use integer parsing for 'simple'
        int i = 0;
        if (i < len) { // skip leading sign:
            char c = input.charAt(0);
            if (c == '+') { // for plus, actually physically remove
                input = input.substring(1);
                len = input.length();
            } else if (c == '-') { // minus, just skip for checks, must retain
                ++i;
            }
        }
        for (; i < len; ++i) {
            char c = input.charAt(i);
            // if other symbols, parse as Double, coerce
            if (c > '9' || c < '0') {
                try {
                    return (int) parseDouble(input);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) { }
        return defaultValue;
    }

    public static long parseAsLong(String input, long defaultValue)
    {
        if (input == null) {
            return defaultValue;
        }
        input = input.trim();
        int len = input.length();
        if (len == 0) {
            return defaultValue;
        }
        // One more thing: use long parsing for 'simple'
        int i = 0;
        if (i < len) { // skip leading sign:
            char c = input.charAt(0);
            if (c == '+') { // for plus, actually physically remove
                input = input.substring(1);
                len = input.length();
            } else if (c == '-') { // minus, just skip for checks, must retain
                ++i;
            }
        }
        for (; i < len; ++i) {
            char c = input.charAt(i);
            // if other symbols, parse as Double, coerce
            if (c > '9' || c < '0') {
                try {
                    return (long) parseDouble(input);
                } catch (NumberFormatException e) {
                    return defaultValue;
                }
            }
        }
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) { }
        return defaultValue;
    }
    
    public static double parseAsDouble(String input, double defaultValue)
    {
        if (input == null) {
            return defaultValue;
        }
        input = input.trim();
        int len = input.length();
        if (len == 0) {
            return defaultValue;
        }
        try {
            return parseDouble(input);
        } catch (NumberFormatException e) { }
        return defaultValue;
    }

    public static double parseDouble(String numStr) throws NumberFormatException
    {
        // [JACKSON-486]: avoid some nasty float representations... but should it be MIN_NORMAL or MIN_VALUE?
        /* as per [JACKSON-827], let's use MIN_VALUE as it is available on all JDKs; normalized
         * only in JDK 1.6. In practice, should not really matter.
         */
        if (NASTY_SMALL_DOUBLE.equals(numStr)) {
            return Double.MIN_VALUE;
        }
        return Double.parseDouble(numStr);
    }

    public static BigDecimal parseBigDecimal(String numStr) throws NumberFormatException
    {
        try {
            return new BigDecimal(numStr);
        } catch (NumberFormatException e) {
            throw _badBigDecimal(numStr);
        }
    }

    public static BigDecimal parseBigDecimal(char[] buffer) throws NumberFormatException {
        return parseBigDecimal(buffer, 0, buffer.length);
    }
    
    public static BigDecimal parseBigDecimal(char[] buffer, int offset, int len)
            throws NumberFormatException
    {
        try {
            return new BigDecimal(buffer, offset, len);
        } catch (NumberFormatException e) {
            throw _badBigDecimal(new String(buffer, offset, len));
        }
    }

    private static NumberFormatException _badBigDecimal(String str) {
        return new NumberFormatException("Value \""+str+"\" can not be represented as BigDecimal");
    }
}

```

## Production source src/main/java/com/fasterxml/jackson/core/util/TextBuffer.java

```java
package com.fasterxml.jackson.core.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.core.io.NumberInput;

/**
 * TextBuffer is a class similar to {@link StringBuffer}, with
 * following differences:
 *<ul>
 *  <li>TextBuffer uses segments character arrays, to avoid having
 *     to do additional array copies when array is not big enough.
 *     This means that only reallocating that is necessary is done only once:
 *     if and when caller
 *     wants to access contents in a linear array (char[], String).
 *    </li>
*  <li>TextBuffer can also be initialized in "shared mode", in which
*     it will just act as a wrapper to a single char array managed
*     by another object (like parser that owns it)
 *    </li>
 *  <li>TextBuffer is not synchronized.
 *    </li>
 * </ul>
 */
public final class TextBuffer
{
    final static char[] NO_CHARS = new char[0];

    /**
     * Let's start with sizable but not huge buffer, will grow as necessary
     */
    final static int MIN_SEGMENT_LEN = 1000;
    
    /**
     * Let's limit maximum segment length to something sensible
     * like 256k
     */
    final static int MAX_SEGMENT_LEN = 0x40000;
    
    /*
    /**********************************************************
    /* Configuration:
    /**********************************************************
     */

    private final BufferRecycler _allocator;

    /*
    /**********************************************************
    /* Shared input buffers
    /**********************************************************
     */

    /**
     * Shared input buffer; stored here in case some input can be returned
     * as is, without being copied to collector's own buffers. Note that
     * this is read-only for this Object.
     */
    private char[] _inputBuffer;

    /**
     * Character offset of first char in input buffer; -1 to indicate
     * that input buffer currently does not contain any useful char data
     */
    private int _inputStart;

    private int _inputLen;

    /*
    /**********************************************************
    /* Aggregation segments (when not using input buf)
    /**********************************************************
     */

    /**
     * List of segments prior to currently active segment.
     */
    private ArrayList<char[]> _segments;

    /**
     * Flag that indicates whether _seqments is non-empty
     */
    private boolean _hasSegments = false;

    // // // Currently used segment; not (yet) contained in _seqments

    /**
     * Amount of characters in segments in {@link _segments}
     */
    private int _segmentSize;

    private char[] _currentSegment;

    /**
     * Number of characters in currently active (last) segment
     */
    private int _currentSize;

    /*
    /**********************************************************
    /* Caching of results
    /**********************************************************
     */

    /**
     * String that will be constructed when the whole contents are
     * needed; will be temporarily stored in case asked for again.
     */
    private String _resultString;

    private char[] _resultArray;

    /*
    /**********************************************************
    /* Life-cycle
    /**********************************************************
     */

    public TextBuffer(BufferRecycler allocator)
    {
        _allocator = allocator;
    }

    /**
     * Method called to indicate that the underlying buffers should now
     * be recycled if they haven't yet been recycled. Although caller
     * can still use this text buffer, it is not advisable to call this
     * method if that is likely, since next time a buffer is needed,
     * buffers need to reallocated.
     * Note: calling this method automatically also clears contents
     * of the buffer.
     */
    public void releaseBuffers()
    {
        if (_allocator == null) {
            resetWithEmpty();
        } else {
            if (_currentSegment != null) {
                // First, let's get rid of all but the largest char array
                resetWithEmpty();
                // And then return that array
                char[] buf = _currentSegment;
                _currentSegment = null;
                _allocator.releaseCharBuffer(BufferRecycler.CharBufferType.TEXT_BUFFER, buf);
            }
        }
    }

    /**
     * Method called to clear out any content text buffer may have, and
     * initializes buffer to use non-shared data.
     */
    public void resetWithEmpty()
    {
        _inputStart = -1; // indicates shared buffer not used
        _currentSize = 0;
        _inputLen = 0;

        _inputBuffer = null;
        _resultString = null;
        _resultArray = null;

        // And then reset internal input buffers, if necessary:
        if (_hasSegments) {
            clearSegments();
        }
    }

    /**
     * Method called to initialize the buffer with a shared copy of data;
     * this means that buffer will just have pointers to actual data. It
     * also means that if anything is to be appended to the buffer, it
     * will first have to unshare it (make a local copy).
     */
    public void resetWithShared(char[] buf, int start, int len)
    {
        // First, let's clear intermediate values, if any:
        _resultString = null;
        _resultArray = null;

        // Then let's mark things we need about input buffer
        _inputBuffer = buf;
        _inputStart = start;
        _inputLen = len;

        // And then reset internal input buffers, if necessary:
        if (_hasSegments) {
            clearSegments();
        }
    }

    public void resetWithCopy(char[] buf, int start, int len)
    {
        _inputBuffer = null;
        _inputStart = -1; // indicates shared buffer not used
        _inputLen = 0;

        _resultString = null;
        _resultArray = null;

        // And then reset internal input buffers, if necessary:
        if (_hasSegments) {
            clearSegments();
        } else if (_currentSegment == null) {
            _currentSegment = findBuffer(len);
        }
        _currentSize = _segmentSize = 0;
        append(buf, start, len);
    }

    public void resetWithString(String value)
    {
        _inputBuffer = null;
        _inputStart = -1;
        _inputLen = 0;

        _resultString = value;
        _resultArray = null;

        if (_hasSegments) {
            clearSegments();
        }
        _currentSize = 0;
        
    }
    
    /**
     * Helper method used to find a buffer to use, ideally one
     * recycled earlier.
     */
    private char[] findBuffer(int needed)
    {
        if (_allocator != null) {
            return _allocator.allocCharBuffer(BufferRecycler.CharBufferType.TEXT_BUFFER, needed);
        }
        return new char[Math.max(needed, MIN_SEGMENT_LEN)];
    }

    private void clearSegments()
    {
        _hasSegments = false;
        /* Let's start using _last_ segment from list; for one, it's
         * the biggest one, and it's also most likely to be cached
         */
        /* 28-Aug-2009, tatu: Actually, the current segment should
         *   be the biggest one, already
         */
        //_currentSegment = _segments.get(_segments.size() - 1);
        _segments.clear();
        _currentSize = _segmentSize = 0;
    }

    /*
    /**********************************************************
    /* Accessors for implementing public interface
    /**********************************************************
     */

    /**
     * @return Number of characters currently stored by this collector
     */
    public int size() {
        if (_inputStart >= 0) { // shared copy from input buf
            return _inputLen;
        }
        if (_resultArray != null) {
            return _resultArray.length;
        }
        if (_resultString != null) {
            return _resultString.length();
        }
        // local segmented buffers
        return _segmentSize + _currentSize;
    }

    public int getTextOffset()
    {
        /* Only shared input buffer can have non-zero offset; buffer
         * segments start at 0, and if we have to create a combo buffer,
         * that too will start from beginning of the buffer
         */
        return (_inputStart >= 0) ? _inputStart : 0;
    }

    /**
     * Method that can be used to check whether textual contents can
     * be efficiently accessed using {@link #getTextBuffer}.
     */
    public boolean hasTextAsCharacters()
    {
        // if we have array in some form, sure
        if (_inputStart >= 0 || _resultArray != null) {
            return true;
        }
        // not if we have String as value
        if (_resultString != null) {
            return false;
        }
        return true;
    }
    
    public char[] getTextBuffer()
    {
        // Are we just using shared input buffer?
        if (_inputStart >= 0) {
            return _inputBuffer;
        }
        if (_resultArray != null) {
            return _resultArray;
        }
        if (_resultString != null) {
            return (_resultArray = _resultString.toCharArray());
        }
        // Nope; but does it fit in just one segment?
        if (!_hasSegments) {
            return _currentSegment;
        }
        // Nope, need to have/create a non-segmented array and return it
        return contentsAsArray();
    }

    /*
    /**********************************************************
    /* Other accessors:
    /**********************************************************
     */

    public String contentsAsString()
    {
        if (_resultString == null) {
            // Has array been requested? Can make a shortcut, if so:
            if (_resultArray != null) {
                _resultString = new String(_resultArray);
            } else {
                // Do we use shared array?
                if (_inputStart >= 0) {
                    if (_inputLen < 1) {
                        return (_resultString = "");
                    }
                    _resultString = new String(_inputBuffer, _inputStart, _inputLen);
                } else { // nope... need to copy
                    // But first, let's see if we have just one buffer
                    int segLen = _segmentSize;
                    int currLen = _currentSize;
                    
                    if (segLen == 0) { // yup
                        _resultString = (currLen == 0) ? "" : new String(_currentSegment, 0, currLen);
                    } else { // no, need to combine
                        StringBuilder sb = new StringBuilder(segLen + currLen);
                        // First stored segments
                        if (_segments != null) {
                            for (int i = 0, len = _segments.size(); i < len; ++i) {
                                char[] curr = _segments.get(i);
                                sb.append(curr, 0, curr.length);
                            }
                        }
                        // And finally, current segment:
                        sb.append(_currentSegment, 0, _currentSize);
                        _resultString = sb.toString();
                    }
                }
            }
        }
        return _resultString;
    }
 
    public char[] contentsAsArray()
    {
        char[] result = _resultArray;
        if (result == null) {
            _resultArray = result = buildResultArray();
        }
        return result;
    }

    /**
     * Convenience method for converting contents of the buffer
     * into a {@link BigDecimal}.
     */
    public BigDecimal contentsAsDecimal()
        throws NumberFormatException
    {
        // Already got a pre-cut array?
        if (_resultArray != null) {
            return NumberInput.parseBigDecimal(_resultArray);
        }
        // Or a shared buffer?
        if ((_inputStart >= 0) && (_inputBuffer != null)) {
            return NumberInput.parseBigDecimal(_inputBuffer, _inputStart, _inputLen);
        }
        // Or if not, just a single buffer (the usual case)
        if ((_segmentSize == 0) && (_currentSegment != null)) {
            return NumberInput.parseBigDecimal(_currentSegment, 0, _currentSize);
        }
        // If not, let's just get it aggregated...
        return NumberInput.parseBigDecimal(contentsAsArray());
    }

    /**
     * Convenience method for converting contents of the buffer
     * into a Double value.
     */
    public double contentsAsDouble()
        throws NumberFormatException
    {
        return NumberInput.parseDouble(contentsAsString());
    }

    /*
    /**********************************************************
    /* Public mutators:
    /**********************************************************
     */

    /**
     * Method called to make sure that buffer is not using shared input
     * buffer; if it is, it will copy such contents to private buffer.
     */
    public void ensureNotShared() {
        if (_inputStart >= 0) {
            unshare(16);
        }
    }

    public void append(char c) {
        // Using shared buffer so far?
        if (_inputStart >= 0) {
            unshare(16);
        }
        _resultString = null;
        _resultArray = null;
        // Room in current segment?
        char[] curr = _currentSegment;
        if (_currentSize >= curr.length) {
            expand(1);
            curr = _currentSegment;
        }
        curr[_currentSize++] = c;
    }

    public void append(char[] c, int start, int len)
    {
        // Can't append to shared buf (sanity check)
        if (_inputStart >= 0) {
            unshare(len);
        }
        _resultString = null;
        _resultArray = null;

        // Room in current segment?
        char[] curr = _currentSegment;
        int max = curr.length - _currentSize;
            
        if (max >= len) {
            System.arraycopy(c, start, curr, _currentSize, len);
            _currentSize += len;
            return;
        }
        // No room for all, need to copy part(s):
        if (max > 0) {
            System.arraycopy(c, start, curr, _currentSize, max);
            start += max;
            len -= max;
        }
        /* And then allocate new segment; we are guaranteed to now
         * have enough room in segment.
         */
        // Except, as per [Issue-24], not for HUGE appends... so:
        do {
            expand(len);
            int amount = Math.min(_currentSegment.length, len);
            System.arraycopy(c, start, _currentSegment, 0, amount);
            _currentSize += amount;
            start += amount;
            len -= amount;
        } while (len > 0);
    }

    public void append(String str, int offset, int len)
    {
        // Can't append to shared buf (sanity check)
        if (_inputStart >= 0) {
            unshare(len);
        }
        _resultString = null;
        _resultArray = null;

        // Room in current segment?
        char[] curr = _currentSegment;
        int max = curr.length - _currentSize;
        if (max >= len) {
            str.getChars(offset, offset+len, curr, _currentSize);
            _currentSize += len;
            return;
        }
        // No room for all, need to copy part(s):
        if (max > 0) {
            str.getChars(offset, offset+max, curr, _currentSize);
            len -= max;
            offset += max;
        }
        /* And then allocate new segment; we are guaranteed to now
         * have enough room in segment.
         */
        // Except, as per [Issue-24], not for HUGE appends... so:
        do {
            expand(len);
            int amount = Math.min(_currentSegment.length, len);
            str.getChars(offset, offset+amount, _currentSegment, 0);
            _currentSize += amount;
            offset += amount;
            len -= amount;
        } while (len > 0);
    }

    /*
    /**********************************************************
    /* Raw access, for high-performance use:
    /**********************************************************
     */

    public char[] getCurrentSegment()
    {
        /* Since the intention of the caller is to directly add stuff into
         * buffers, we should NOT have anything in shared buffer... ie. may
         * need to unshare contents.
         */
        if (_inputStart >= 0) {
            unshare(1);
        } else {
            char[] curr = _currentSegment;
            if (curr == null) {
                _currentSegment = findBuffer(0);
            } else if (_currentSize >= curr.length) {
                // Plus, we better have room for at least one more char
                expand(1);
            }
        }
        return _currentSegment;
    }

    public char[] emptyAndGetCurrentSegment()
    {
        // inlined 'resetWithEmpty()'
        _inputStart = -1; // indicates shared buffer not used
        _currentSize = 0;
        _inputLen = 0;

        _inputBuffer = null;
        _resultString = null;
        _resultArray = null;

        // And then reset internal input buffers, if necessary:
        if (_hasSegments) {
            clearSegments();
        }
        char[] curr = _currentSegment;
        if (curr == null) {
            _currentSegment = curr = findBuffer(0);
        }
        return curr;
    }

    public int getCurrentSegmentSize() {
        return _currentSize;
    }

    public void setCurrentLength(int len) {
        _currentSize = len;
    }

    public char[] finishCurrentSegment()
    {
        if (_segments == null) {
            _segments = new ArrayList<char[]>();
        }
        _hasSegments = true;
        _segments.add(_currentSegment);
        int oldLen = _currentSegment.length;
        _segmentSize += oldLen;
        // Let's grow segments by 50%
        int newLen = Math.min(oldLen + (oldLen >> 1), MAX_SEGMENT_LEN);
        char[] curr = _charArray(newLen);
        _currentSize = 0;
        _currentSegment = curr;
        return curr;
    }

    /**
     * Method called to expand size of the current segment, to
     * accommodate for more contiguous content. Usually only
     * used when parsing tokens like names if even then.
     */
    public char[] expandCurrentSegment()
    {
        final char[] curr = _currentSegment;
        // Let's grow by 50%
        final int len = curr.length;
        // Must grow by at least 1 char, no matter what
        int newLen = (len == MAX_SEGMENT_LEN) ?
            (MAX_SEGMENT_LEN + 1) : Math.min(MAX_SEGMENT_LEN, len + (len >> 1));
        return (_currentSegment = Arrays.copyOf(curr, newLen));
    }

    /*
    /**********************************************************
    /* Standard methods:
    /**********************************************************
     */

    /**
     * Note: calling this method may not be as efficient as calling
     * {@link #contentsAsString}, since it's not guaranteed that resulting
     * String is cached.
     */
    @Override
    public String toString() {
         return contentsAsString();
    }

    /*
    /**********************************************************
    /* Internal methods:
    /**********************************************************
     */

    /**
     * Method called if/when we need to append content when we have been
     * initialized to use shared buffer.
     */
    private void unshare(int needExtra)
    {
        int sharedLen = _inputLen;
        _inputLen = 0;
        char[] inputBuf = _inputBuffer;
        _inputBuffer = null;
        int start = _inputStart;
        _inputStart = -1;

        // Is buffer big enough, or do we need to reallocate?
        int needed = sharedLen+needExtra;
        if (_currentSegment == null || needed > _currentSegment.length) {
            _currentSegment = findBuffer(needed);
        }
        if (sharedLen > 0) {
            System.arraycopy(inputBuf, start, _currentSegment, 0, sharedLen);
        }
        _segmentSize = 0;
        _currentSize = sharedLen;
    }

    /**
     * Method called when current segment is full, to allocate new
     * segment.
     */
    private void expand(int minNewSegmentSize)
    {
        // First, let's move current segment to segment list:
        if (_segments == null) {
            _segments = new ArrayList<char[]>();
        }
        char[] curr = _currentSegment;
        _hasSegments = true;
        _segments.add(curr);
        _segmentSize += curr.length;
        int oldLen = curr.length;
        // Let's grow segments by 50% minimum
        int sizeAddition = oldLen >> 1;
        if (sizeAddition < minNewSegmentSize) {
            sizeAddition = minNewSegmentSize;
        }
        _currentSize = 0;
        _currentSegment = _charArray(Math.min(MAX_SEGMENT_LEN, oldLen + sizeAddition));
    }

    private char[] buildResultArray()
    {
        if (_resultString != null) { // Can take a shortcut...
            return _resultString.toCharArray();
        }
        // Do we use shared array?
        if (_inputStart >= 0) {
            final int len = _inputLen;
            if (len < 1) {
                return NO_CHARS;
            }
            final int start = _inputStart;
            if (start == 0) {
                return Arrays.copyOf(_inputBuffer, len);
            }
            return Arrays.copyOfRange(_inputBuffer, start, start+len);
        }
        // nope, not shared
        int size = size();
        if (size < 1) {
            return NO_CHARS;
        }
        int offset = 0;
        final char[] result = _charArray(size);
        if (_segments != null) {
            for (int i = 0, len = _segments.size(); i < len; ++i) {
                char[] curr = (char[]) _segments.get(i);
                int currLen = curr.length;
                System.arraycopy(curr, 0, result, offset, currLen);
                offset += currLen;
            }
        }
        System.arraycopy(_currentSegment, 0, result, offset, _currentSize);
        return result;
    }

    private char[] _charArray(int len) {
        return new char[len];
    }
}

```

## Build configuration build.xml

```text
<?xml version="1.0" encoding="UTF-8"?>

<!-- ====================================================================== -->
<!-- Ant build file (http://ant.apache.org/) for Ant 1.6.2 or above.        -->
<!-- ====================================================================== -->

<project name="jackson-core" default="package" basedir=".">

  <!-- ====================================================================== -->
  <!-- Import maven-build.xml into the current project                        -->
  <!-- ====================================================================== -->

  <import file="maven-build.xml"/>
  
  <!-- ====================================================================== -->
  <!-- Help target                                                            -->
  <!-- ====================================================================== -->

  <target name="help">
    <echo message="Please run: $ant -projecthelp"/>
  </target>

  <target name="compile" depends="jackson-core-from-maven.compile"> </target>
  <target name="compile.tests" depends="jackson-core-from-maven.compile-tests"> </target>

</project>

```

## Build configuration pom.xml

```text
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion> 
  <parent>
    <groupId>com.fasterxml</groupId>
    <artifactId>oss-parent</artifactId>
    <version>11</version>
  </parent>

  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-core</artifactId>
  <name>Jackson-core</name>
  <version>2.3.0-SNAPSHOT</version>
  <description>Core Jackson abstractions, basic JSON streaming API implementation
  </description>

  <url>http://wiki.fasterxml.com/JacksonHome</url>
  <scm>
    <connection>scm:git:git@github.com:FasterXML/jackson-core.git</connection>
    <developerConnection>scm:git:git@github.com:FasterXML/jackson-core.git</developerConnection>
    <url>http://github.com/FasterXML/jackson-core</url>    
    <tag>HEAD</tag>
  </scm>

  <properties>
    <!--
     | Configuration properties for the OSGi maven-bundle-plugin
    -->
    <osgi.export>com.fasterxml.jackson.core;version=${project.version},
com.fasterxml.jackson.core.*;version=${project.version}
    </osgi.export>

    <!-- Generate PackageVersion.java into this directory. -->
    <packageVersion.dir>com/fasterxml/jackson/core/json</packageVersion.dir>
    <packageVersion.package>com.fasterxml.jackson.core.json</packageVersion.package>
  </properties>

  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.8.2</version>
      <scope>test</scope>
    </dependency>
  </dependencies>

  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-javadoc-plugin</artifactId>
          <version>2.8.1</version>
          <configuration>
            <source>1.6</source>
            <target>1.6</target>
            <encoding>UTF-8</encoding>
            <maxmemory>512m</maxmemory>
            <links>
              <link>http://docs.oracle.com/javase/6/docs/api/</link>
            </links>
          </configuration>
          <executions>
                    <execution>
                        <id>attach-javadocs</id>
                        <phase>verify</phase>
                        <goals>
                            <goal>jar</goal>
                        </goals>
                    </execution>
          </executions>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-site-plugin</artifactId>
            <version>3.1</version>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-surefire-plugin</artifactId>
            <version>${surefire.version}</version>
            <configuration>
                <redirectTestOutputToFile>${surefire.redirectTestOutputToFile}</redirectTestOutputToFile>
            </configuration>
        </plugin>
      <plugin>
        <!-- Inherited from oss-base. Generate PackageVersion.java.-->
        <groupId>com.google.code.maven-replacer-plugin</groupId>
        <artifactId>replacer</artifactId>
        <executions>
          <execution>
            <id>process-packageVersion</id>
            <phase>generate-sources</phase>
          </execution>
        </executions>
      </plugin>
    </plugins>
    <extensions>
        <!-- Enabling the use of SSH -->
        <extension>
            <groupId>org.apache.maven.wagon</groupId>
            <artifactId>wagon-ssh-external</artifactId>
            <version>1.0-beta-6</version>
        </extension>
        <extension>
            <groupId>org.apache.maven.scm</groupId>
            <artifactId>maven-scm-provider-gitexe</artifactId>
            <version>1.6</version>
        </extension>
        <extension>
            <groupId>org.apache.maven.scm</groupId>
            <artifactId>maven-scm-manager-plexus</artifactId>
            <version>1.6</version>
        </extension>
        <extension>
            <groupId>org.kathrynhuxtable.maven.wagon</groupId>
            <artifactId>wagon-gitsite</artifactId>
            <version>0.3.1</version>
        </extension>
    </extensions>
  </build>

  <reporting>
    <plugins>
      <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-javadoc-plugin</artifactId>
          <version>2.8.1</version>
          <configuration>
              <aggregate>true</aggregate>
              <source>1.6</source>
              <encoding>UTF-8</encoding>
              <maxmemory>1g</maxmemory>
              <links>
                  <!-- JDK, other Jackson pkgs -->
                  <link>http://docs.oracle.com/javase/6/docs/api/</link>
                  <link>http://fasterxml.github.com/jackson-core/javadoc/2.3.0/</link>
              </links>
              <excludePackageNames>${javadoc.package.exclude}</excludePackageNames>
              <bootclasspath>${sun.boot.class.path}</bootclasspath>
              <doclet>com.google.doclava.Doclava</doclet>
              <useStandardDocletOptions>false</useStandardDocletOptions>
              <additionalJOption>-J-Xmx1024m</additionalJOption>
              <docletArtifact>
                  <groupId>com.google.doclava</groupId>
                  <artifactId>doclava</artifactId>
                  <version>1.0.3</version>
              </docletArtifact>
              <additionalparam>
                  -hdf project.name "${project.name} ${project.version}"
                  -d ${project.reporting.outputDirectory}/apidocs
              </additionalparam>
          </configuration>
          <reportSets>
              <reportSet>
                  <id>default</id>
                  <reports>
                      <report>javadoc</report>
                  </reports>
              </reportSet>
          </reportSets>
      </plugin>
    </plugins>
  </reporting>

</project>

```
