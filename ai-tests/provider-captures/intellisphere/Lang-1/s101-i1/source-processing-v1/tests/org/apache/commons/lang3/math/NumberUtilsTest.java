package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

/**
 * Unit tests for {@link NumberUtils} using documented reference behavior.
 */
public class NumberUtilsTest {

    // ------------------------------------------------------------- isAllZeros

    @Test
    public void testIsAllZeros_private_null() throws Exception {
        Boolean result = (Boolean) invokePrivateStatic("isAllZeros", new Class<?>[]{String.class}, (Object) null);
        assertTrue(result);
    }

    @Test
    public void testIsAllZeros_private_empty() throws Exception {
        Boolean result = (Boolean) invokePrivateStatic("isAllZeros", new Class<?>[]{String.class}, "");
        assertFalse(result);
    }

    @Test
    public void testIsAllZeros_private_allZeros() throws Exception {
        Boolean result = (Boolean) invokePrivateStatic("isAllZeros", new Class<?>[]{String.class}, "0000");
        assertTrue(result);
    }

    @Test
    public void testIsAllZeros_private_notAllZeros() throws Exception {
        Boolean result = (Boolean) invokePrivateStatic("isAllZeros", new Class<?>[]{String.class}, "0010");
        assertFalse(result);
    }

    // ------------------------------------------------------------- validateArray

    @Test(expected = IllegalArgumentException.class)
    public void testValidateArray_null() throws Exception {
        invokePrivateStatic("validateArray", new Class<?>[]{Object.class}, (Object) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateArray_empty() throws Exception {
        invokePrivateStatic("validateArray", new Class<?>[]{Object.class}, new int[0]);
    }

    @Test
    public void testValidateArray_nonEmpty() throws Exception {
        // Should not throw
        invokePrivateStatic("validateArray", new Class<?>[]{Object.class}, new int[]{1});
    }

    // ------------------------------------------------------------- isDigits

    @Test
    public void testIsDigits_null() {
        assertFalse(NumberUtils.isDigits(null));
    }

    @Test
    public void testIsDigits_empty() {
        assertFalse(NumberUtils.isDigits(""));
    }

    @Test
    public void testIsDigits_allDigits() {
        assertTrue(NumberUtils.isDigits("123"));
    }

    @Test
    public void testIsDigits_nonDigit() {
        assertFalse(NumberUtils.isDigits("123a"));
    }

    // ------------------------------------------------------------- isNumber

    @Test
    public void testIsNumber_null() {
        assertFalse(NumberUtils.isNumber(null));
    }

    @Test
    public void testIsNumber_empty() {
        assertFalse(NumberUtils.isNumber(""));
    }

    @Test
    public void testIsNumber_blank() {
        assertFalse(NumberUtils.isNumber(" "));
    }

    @Test
    public void testIsNumber_integer() {
        assertTrue(NumberUtils.isNumber("123"));
    }

    @Test
    public void testIsNumber_hex() {
        assertTrue(NumberUtils.isNumber("0x1A"));
    }

    @Test
    public void testIsNumber_floatWithPoint() {
        assertTrue(NumberUtils.isNumber("1.5"));
    }

    @Test
    public void testIsNumber_exponent() {
        assertTrue(NumberUtils.isNumber("1E5"));
    }

    @Test
    public void testIsNumber_invalidPattern() {
        assertFalse(NumberUtils.isNumber("1..2"));
    }

    // ------------------------------------------------------------- toByte

    @Test
    public void testToByte_null() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
    }

    @Test
    public void testToByte_empty() {
        assertEquals((byte) 0, NumberUtils.toByte(""));
    }

    @Test
    public void testToByte_valid() {
        assertEquals((byte) 42, NumberUtils.toByte("42"));
    }

    @Test
    public void testToByte_defaultValue() {
        assertEquals((byte) 10, NumberUtils.toByte("abc", (byte) 10));
    }

    // ------------------------------------------------------------- toShort

    @Test
    public void testToShort_null() {
        assertEquals((short) 0, NumberUtils.toShort(null));
    }

    @Test
    public void testToShort_empty() {
        assertEquals((short) 0, NumberUtils.toShort(""));
    }

    @Test
    public void testToShort_valid() {
        assertEquals((short) 123, NumberUtils.toShort("123"));
    }

    @Test    public void testToShort_defaultValue() {
        assertEquals((short) 5, NumberUtils.toShort(null, (short) 5));
    }

    // ------------------------------------------------------------- toInt

    @Test    public void testToInt_null() {
        assertEquals(0, NumberUtils.toInt(null));
    }

    @Test    public void testToInt_empty() {
        assertEquals(0, NumberUtils.toInt(""));
    }

    @Test
    public void testToInt_valid() {
        assertEquals(123, NumberUtils.toInt("123"));
    }

    

    // ------------------------------------------------------------- toLong

    

    

    

    

    // ------------------------------------------------------------- toFloat

    

    

    

    

    // ------------------------------------------------------------- toDouble

    

    

    

    

    // ------------------------------------------------------------- createNumber

   

    

    

    

    

    

    

    

    

    // ------------------------------------------------------------- createDouble

    

    

    

    // ------------------------------------------------------------- createFloat

    

    

    

    // ------------------------------------------------------------- createInteger

    

    

    

    

    // ------------------------------------------------------------- createLong

    

    

    

    

    // ------------------------------------------------------------- createBigInteger

    

    

    

    

    

    // ------------------------------------------------------------- createBigDecimal

    

    

    

    

    // ------------------------------------------------------------- max / min (3 args & arrays)

    

    

    

    

    

    

    

    // ------------------------------------------------------------- helpers

    /**
     * Invokes a private static method of NumberUtils by reflection.
     */
    private static Object invokePrivateStatic(String methodName, Class<?>[] paramTypes, Object... args) throws Exception {
        Method method = NumberUtils.class.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        try {
            return method.invoke(null, args);
        } catch (InvocationTargetException e) {
            throw (Exception) e.getCause();
        }
    }
}
