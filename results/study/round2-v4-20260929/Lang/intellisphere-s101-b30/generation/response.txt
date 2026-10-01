Rendered KKU IntelSphere response. Model label: Deepseek - deepseek-v4-pro

```java
org/apache/commons/lang3/math/NumberUtilsTest.java

```

```java
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
        invokePrivateStatic("validateArray", new Class<?>[]{Object.class}, new int[0](undefined));
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

    @Test    public void testToInt_defaultValue() {
        assertEquals(99, NumberUtils.toInt("abc", 99));
    }

    // ------------------------------------------------------------- toLong

    @Test    public void testToLong_null() {
        assertEquals(0L, NumberUtils.toLong(null));
    }

    @Test    public void testToLong_empty() {
        assertEquals(0L, NumberUtils.toLong(""));
    }

    @Test    public void testToLong_valid() {
        assertEquals(123L, NumberUtils.toLong("123"));
    }

    @Test
    public void testToLong_defaultValue() {
        assertEquals(99L, NumberUtils.toLong("abc", 99L));
    }

    // ------------------------------------------------------------- toFloat

    @Test    public void testToFloat_null() {
        assertEquals(0.0f, NumberUtils.toFloat(null), 0.0);
    }

    @Test    public void testToFloat_empty() {
        assertEquals(0.0f, NumberUtils.toFloat(""), 0.0);
    }

    @Test    public void testToFloat_valid() {
    assertEquals(1.5f, NumberUtils.toFloat("1.5"), 0.0);
    }

    @Test    public void testToFloat_defaultValue() {
    assertEquals(1.1f, NumberUtils.toFloat("abc", 1.1f), 0.0);
    }

    // ------------------------------------------------------------- toDouble

    @Test    public void testToDouble_null() {
    assertEquals(0.0d, NumberUtils.toDouble(null), 0.0);
    }

    @Test    public void testToDouble_empty() {
    assertEquals(0.0d, NumberUtils.toDouble(""), 0.0);
    }

    @Test    public void testToDouble_valid() {
    assertEquals(1.5d, NumberUtils.toDouble("1.5"), 0.0);
    }

    @Test
    public void testToDouble_defaultValue() {
    assertEquals(1.1d, NumberUtils.toDouble("abc", 1.1d), 0.0);
    }

    // ------------------------------------------------------------- createNumber

   @Test    public void testCreateNumber_null() throws Exception {
    assertNull(NumberUtils.createNumber(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_blank() throws Exception {
    NumberUtils.createNumber(" ");
    }

    @Test
    public void testCreateNumber_hexInteger() throws Exception {
    assertEquals(Integer.valueOf(15), NumberUtils.createNumber("0xF"));
    }

    @Test    public void testCreateNumber_integer() throws Exception {
    assertEquals(Integer.valueOf(123), NumberUtils.createNumber("123"));
    }

    @Test    public void testCreateNumber_long() throws Exception {
    assertEquals(Long.valueOf(123456789123L), NumberUtils.createNumber("123456789123"));
    }

    @Test    public void testCreateNumber_float() throws Exception {
    assertEquals(Float.valueOf(1.5f), NumberUtils.createNumber("1.5f"));
    }

    @Test    public void testCreateNumber_double() throws Exception {
    assertEquals(Double.valueOf(1.5d), NumberUtils.createNumber("1.5d"));
    }

    @Test
    public void testCreateNumber_bigInteger() throws Exception {
    assertEquals(new BigInteger("12345678901234567890"), NumberUtils.createNumber("12345678901234567890"));
    }

    @Test
    public void testCreateNumber_bigDecimal() throws Exception {
    assertEquals(new BigDecimal("1.12345678901234567890"), NumberUtils.createNumber("1.12345678901234567890"));
    }

    // ------------------------------------------------------------- createDouble

    @Test    public void testCreateDouble_null() {
    assertNull(NumberUtils.createDouble(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateDouble_empty() {
        NumberUtils.createDouble("");
    }

    @Test    public void testCreateDouble_valid() {
    assertEquals(Double.valueOf(1.5d), NumberUtils.createDouble("1.5"));
    }

    // ------------------------------------------------------------- createFloat

    @Test    public void testCreateFloat_null() {
        assertNull(NumberUtils.createFloat(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateFloat_empty() {
        NumberUtils.createFloat("");
    }

    @Test    public void testCreateFloat_valid() {
        assertEquals(Float.valueOf(1.5f), NumberUtils.createFloat("1.5"));
    }

    // ------------------------------------------------------------- createInteger

    @Test    public void testCreateInteger_null() {
        assertNull(NumberUtils.createInteger(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateInteger_empty() {
    NumberUtils.createInteger("");
    }

    @Test    public void testCreateInteger_valid() {
    assertEquals(Integer.valueOf(42), NumberUtils.createInteger("42"));
    }

    @Test
    public void testCreateInteger_hex() {
    assertEquals(Integer.valueOf(15), NumberUtils.createInteger("0xF"));
    }

    // ------------------------------------------------------------- createLong

    @Test    public void testCreateLong_null() {
    assertNull(NumberUtils.createLong(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateLong_empty() {
    NumberUtils.createLong("");
    }

    @Test    public void testCreateLong_valid() {
    assertEquals(Long.valueOf(42L), NumberUtils.createLong("42"));
    }

    @Test    public void testCreateLong_hex() {
    assertEquals(Long.valueOf(15L), NumberUtils.createLong("0xF"));
    }

    // ------------------------------------------------------------- createBigInteger

    @Test    public void testCreateBigInteger_null() {
    assertNull(NumberUtils.createBigInteger(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigInteger_empty() {
    NumberUtils.createBigInteger("");
    }

    @Test    public void testCreateBigInteger_valid() {
    assertEquals(new BigInteger("42"), NumberUtils.createBigInteger("42"));
    }

    @Test    public void testCreateBigInteger_negative() {
    assertEquals(new BigInteger("-42"), NumberUtils.createBigInteger("-42"));
    }

    @Test    public void testCreateBigInteger_hex() {
    assertEquals(new BigInteger("15"), NumberUtils.createBigInteger("0xF"));
    }

    // ------------------------------------------------------------- createBigDecimal

    @Test    public void testCreateBigDecimal_null() {
    assertNull(NumberUtils.createBigDecimal(null));
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimal_empty() {
    NumberUtils.createBigDecimal("");
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateBigDecimal_blank() {
    NumberUtils.createBigDecimal(" ");
    }

    @Test
    public void testCreateBigDecimal_valid() {
    assertEquals(new BigDecimal("3.14"), NumberUtils.createBigDecimal("3.14"));
    }

    // ------------------------------------------------------------- max / min (3 args & arrays)

    @Test    public void testMaxInt_three() {
    assertEquals(5, NumberUtils.max(3, 5, 2));
    }

    @Test    public void testMaxInt_array() {
    assertEquals(9, NumberUtils.max(new int[]{1, 9, 3}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxInt_array_null() {
    NumberUtils.max((int[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxInt_array_empty() {
    NumberUtils.max(new int[0](undefined));
    }

    @Test    public void testMinDdouble_three() {
    assertEquals(1.0, NumberUtils.min(5.0, 1.0, 3.0), 0.0);
    }

    @Test    public void testMinDouble_array() {
    assertEquals(1.0, NumberUtils.min(new double[]{5.0, 1.0, 3.0}), 0.0);
    }

    @Test    public void testMinDouble_array_NaN() {
    assertTrue(Double.isNaN(NumberUtils.min(new double[]{1.0, Double.NaN, 3.0})));
    }

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

```