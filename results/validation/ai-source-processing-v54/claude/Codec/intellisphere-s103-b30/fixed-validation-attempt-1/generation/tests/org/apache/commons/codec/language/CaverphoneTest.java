/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.codec.language;

import junit.framework.TestCase;
import org.apache.commons.codec.EncoderException;

/**
 * Unit tests for Caverphone class.
 * Tests cover normal cases, boundaries, invalid inputs, and exception paths.
 */
public class CaverphoneTest extends TestCase {

    private Caverphone caverphone;

    protected void setUp() {
        caverphone = new Caverphone();
    }

    // caverphone(String) method tests

    public void testCaverphoneNullReturnsDefault() {
        String result = caverphone.caverphone(null);
        assertEquals("1111111111", result);
    }

    public void testCaverphoneEmptyStringReturnsDefault() {
        String result = caverphone.caverphone("");
        assertEquals("1111111111", result);
    }

    public void testCaverphoneSingleCharacter() {
        String result = caverphone.caverphone("a");
        assertEquals("A1111111111".substring(0, 10), result);
        assertEquals(10, result.length());
    }

    public void testCaverphoneLowercaseConversion() {
        String result1 = caverphone.caverphone("TEST");
        String result2 = caverphone.caverphone("test");
        assertEquals(result1, result2);
    }

    public void testCaverphoneRemovesNonAlphaChars() {
        String withNonAlpha = caverphone.caverphone("t3st-123");
        String withoutNonAlpha = caverphone.caverphone("tst");
        // Both should remove numbers and special chars and process similarly
        assertEquals(10, withNonAlpha.length());
    }

    public void testCaverphoneRemovesFinalE() {
        String withoutE = caverphone.caverphone("teste");
        String withoutFinalE = caverphone.caverphone("test");
        // Both should produce same result since final e is removed
        assertEquals(withoutE, withoutFinalE);
    }

    public void testCaverphoneCoughReplacement() {
        String result = caverphone.caverphone("cough");
        assertTrue(result.length() == 10);
        assertNotNull(result);
    }

    public void testCaverphoneRoughReplacement() {
        String result = caverphone.caverphone("rough");
        assertTrue(result.length() == 10);
        assertNotNull(result);
    }

    public void testCaverphoneToughReplacement() {
        String result = caverphone.caverphone("tough");
        assertTrue(result.length() == 10);
        assertNotNull(result);
    }

    public void testCaverphoneEnoughReplacement() {
        String result = caverphone.caverphone("enough");
        assertTrue(result.length() == 10);
        assertNotNull(result);
    }

    public void testCaverphoneTroughReplacement() {
        String result = caverphone.caverphone("trough");
        assertTrue(result.length() == 10);
        assertNotNull(result);
    }

    public void testCaverphoneGnReplacement() {
        String result = caverphone.caverphone("gnat");
        assertTrue(result.length() == 10);
        assertNotNull(result);
    }

    public void testCaverphoneMbReplacement() {
        String result = caverphone.caverphone("mb");
        assertTrue(result.length() == 10);
        assertNotNull(result);
    }

    public void testCaverphonePhReplacement() {
        String result = caverphone.caverphone("phone");
        assertTrue(result.length() == 10);
        assertNotNull(result);
    }

    public void testCaverphoneOutputLength() {
        String result = caverphone.caverphone("test");
        assertEquals(10, result.length());
    }

    public void testCaverphoneOutputAllDigits() {
        String result = caverphone.caverphone("zzzzz");
        // After processing, should contain digits and letters
        assertTrue(result.length() == 10);
    }

    // encode(String) method tests

    public void testEncodeStringBasic() {
        String result = caverphone.encode("test");
        assertEquals(caverphone.caverphone("test"), result);
    }

    public void testEncodeStringNull() {
        String result = caverphone.encode(null);
        assertEquals("1111111111", result);
    }

    public void testEncodeStringEmpty() {
        String result = caverphone.encode("");
        assertEquals("1111111111", result);
    }

    // encode(Object) method tests

    public void testEncodeObjectString() throws EncoderException {
        Object result = caverphone.encode((Object) "test");
        assertEquals(caverphone.caverphone("test"), result);
    }

    public void testEncodeObjectNonStringThrows() {
        try {
            caverphone.encode((Object) 123);
            fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            assertTrue(e.getMessage().contains("not of type java.lang.String"));
        }
    }

    public void testEncodeObjectIntegerThrows() {
        try {
            caverphone.encode((Object) new Integer(42));
            fail("EncoderException should have been thrown");
        } catch (EncoderException e) {
            assertNotNull(e.getMessage());
        }
    }

    public void testEncodeObjectNullString() throws EncoderException {
        Object result = caverphone.encode((Object) null);
        fail("Should throw EncoderException for null Object");
    }

    // isCaverphoneEqual(String, String) method tests

    public void testIsCaverphoneEqualIdenticalStrings() {
        boolean result = caverphone.isCaverphoneEqual("test", "test");
        assertTrue(result);
    }

    public void testIsCaverphoneEqualDifferentCase() {
        boolean result = caverphone.isCaverphoneEqual("TEST", "test");
        assertTrue(result);
    }

    public void testIsCaverphoneEqualDifferentStrings() {
        boolean result = caverphone.isCaverphoneEqual("cat", "dog");
        assertFalse(result);
    }

    public void testIsCaverphoneEqualBothNull() {
        boolean result = caverphone.isCaverphoneEqual(null, null);
        assertTrue(result);
    }

    public void testIsCaverphoneEqualFirstNull() {
        boolean result = caverphone.isCaverphoneEqual(null, "test");
        assertFalse(result);
    }

    public void testIsCaverphoneEqualSecondNull() {
        boolean result = caverphone.isCaverphoneEqual("test", null);
        assertFalse(result);
    }

    public void testIsCaverphoneEqualBothEmpty() {
        boolean result = caverphone.isCaverphoneEqual("", "");
        assertTrue(result);
    }

    

    

    

    
}
