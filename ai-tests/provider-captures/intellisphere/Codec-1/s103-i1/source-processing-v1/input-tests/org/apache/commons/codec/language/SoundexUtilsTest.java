package org.apache.commons.codec.language;

import junit.framework.TestCase;

/**
 * Tests for {@link SoundexUtils}. Since the class is package-private,
 * we must test via a package-accessible way.
 * We use a simple custom StringEncoder for testing difference methods.
 */
public class SoundexUtilsTest extends TestCase {

    public void testCleanNull() {
        // We need a public caller or reflection.
        // Since SoundexUtils.clean is package-private, we test indirectly.
        // Typically, we can test this via a Soundex instance, but we are
        // constrained to not assume Soundex is a target.
        // Let's use reflection for direct test.
        try {
            java.lang.reflect.Method method = SoundexUtils.class.getDeclaredMethod("clean", String.class);
            method.setAccessible(true);
            assertNull(method.invoke(null, (String) null));
        } catch (Exception e) {
            fail("Reflection test failed: " + e.getMessage());
        }
    }

    public void testCleanEmpty() {
        try {
            java.lang.reflect.Method method = SoundexUtils.class.getDeclaredMethod("clean", String.class);
            method.setAccessible(true);
            assertEquals("", method.invoke(null, ""));
        } catch (Exception e) {
            fail("Reflection test failed: " + e.getMessage());
        }
    }

    public void testCleanLettersOnly() {
        try {
            java.lang.reflect.Method method = SoundexUtils.class.getDeclaredMethod("clean", String.class);
            method.setAccessible(true);
            assertEquals("ABC", method.invoke(null, "abc"));
        } catch (Exception e) {
            fail("Reflection test failed: " + e.getMessage());
        }
    }

    public void testCleanNumbersRemoved() {
        try {
            java.lang.reflect.Method method = SoundexUtils.class.getDeclaredMethod("clean", String.class);
            method.setAccessible(true);
            assertEquals("ABC", method.invoke(null, "a1b2c3"));
        } catch (Exception e) {
            fail("Reflection test failed: " + e.getMessage());
        }
    }

    public void testCleanMixed() {
        try {
            java.lang.reflect.Method method = SoundexUtils.class.getDeclaredMethod("clean", String.class);
            method.setAccessible(true);
            assertEquals("AB", method.invoke(null, "a-1 b!2"));
        } catch (Exception e) {
            fail("Reflection test failed: " + e.getMessage());
        }
    }

    // differenceEncoded tests

    public void testDifferenceEncodedNulls() {
        assertEquals(0, SoundexUtils.differenceEncoded(null, null));
        assertEquals(0, SoundexUtils.differenceEncoded("A", null));
        assertEquals(0, SoundexUtils.differenceEncoded(null, "A"));
    }

    public void testDifferenceEncodedEmpty() {
        assertEquals(0, SoundexUtils.differenceEncoded("", ""));
        assertEquals(0, SoundexUtils.differenceEncoded("A", ""));
    }

    public void testDifferenceEncodedSame() {
        assertEquals(4, SoundexUtils.differenceEncoded("ABCD", "ABCD"));
    }

    public void testDifferenceEncodedPartial() {
        assertEquals(2, SoundexUtils.differenceEncoded("ABC", "AXC"));
    }

    // difference tests

    public void testDifference() throws Exception {
        // Use Metaphone as concrete encoder
        org.apache.commons.codec.StringEncoder encoder = new Metaphone();
        assertEquals(4, SoundexUtils.difference(encoder, "test", "test"));
    }

    public void testDifferenceDifferentStrings() throws Exception {
        org.apache.commons.codec.StringEncoder encoder = new Metaphone();
        assertTrue(SoundexUtils.difference(encoder, "test", "jest") < 4);
    }
}
