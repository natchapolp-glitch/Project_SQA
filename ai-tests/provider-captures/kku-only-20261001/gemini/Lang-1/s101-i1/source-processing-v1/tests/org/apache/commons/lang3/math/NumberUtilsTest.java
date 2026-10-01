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
package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

public class NumberUtilsTest {

    @Test
    public void testToInt() {
        assertEquals(0, NumberUtils.toInt(null));
        assertEquals(0, NumberUtils.toInt(""));
        assertEquals(12, NumberUtils.toInt("12"));
        assertEquals(-1, NumberUtils.toInt(null, -1));
        assertEquals(5, NumberUtils.toInt("invalid", 5));
        assertEquals(1, NumberUtils.toInt("2147483648", 1)); // overflow defaults
    }

    @Test
    public void testToLong() {
        assertEquals(0L, NumberUtils.toLong(null));
        assertEquals(0L, NumberUtils.toLong(""));
        assertEquals(123456789012L, NumberUtils.toLong("123456789012"));
        assertEquals(-1L, NumberUtils.toLong(null, -1L));
        assertEquals(99L, NumberUtils.toLong("invalid", 99L));
    }

    @Test
    public void testToByteAndShort() {
        assertEquals((byte) 0, NumberUtils.toByte(null));
        assertEquals((byte) 10, NumberUtils.toByte("10"));
        assertEquals((byte) 5, NumberUtils.toByte("invalid", (byte) 5));

        assertEquals((short) 0, NumberUtils.toShort(null));
        assertEquals((short) 20, NumberUtils.toShort("20"));
        assertEquals((short) 5, NumberUtils.toShort("invalid", (short) 5));
    }

    @Test
    public void testToFloatAndDouble() {
        assertEquals(0.0f, NumberUtils.toFloat(null), 0.0001f);
        assertEquals(1.5f, NumberUtils.toFloat("1.5"), 0.0001f);
        assertEquals(2.5f, NumberUtils.toFloat("invalid", 2.5f), 0.0001f);

        assertEquals(0.0d, NumberUtils.toDouble(null), 0.0001d);
        assertEquals(1.25d, NumberUtils.toDouble("1.25"), 0.0001d);
        assertEquals(3.75d, NumberUtils.toDouble("invalid", 3.75d), 0.0001d);
    }

    @Test
    public void testMinMaxByte() {
        assertEquals((byte) 1, NumberUtils.min((byte) 1, (byte) 2, (byte) 3));
        assertEquals((byte) 1, NumberUtils.min((byte) 3, (byte) 1, (byte) 2));
        assertEquals((byte) 1, NumberUtils.min((byte) 3, (byte) 2, (byte) 1));

        assertEquals((byte) 3, NumberUtils.max((byte) 3, (byte) 2, (byte) 1));
        assertEquals((byte) 3, NumberUtils.max((byte) 1, (byte) 3, (byte) 2));
        assertEquals((byte) 3, NumberUtils.max((byte) 1, (byte) 2, (byte) 3));
    }

    @Test
    public void testMinMaxShort() {
        assertEquals((short) 10, NumberUtils.min((short) 10, (short) 20, (short) 30));
        assertEquals((short) 10, NumberUtils.min((short) 30, (short) 10, (short) 20));
        assertEquals((short) 10, NumberUtils.min((short) 30, (short) 20, (short) 10));

        assertEquals((short) 30, NumberUtils.max((short) 30, (short) 20, (short) 10));
        assertEquals((short) 30, NumberUtils.max((short) 10, (short) 30, (short) 20));
        assertEquals((short) 30, NumberUtils.max((short) 10, (short) 20, (short) 30));
    }

    @Test
    public void testMinMaxInt() {
        assertEquals(100, NumberUtils.min(100, 200, 300));
        assertEquals(100, NumberUtils.min(300, 100, 200));
        assertEquals(100, NumberUtils.min(300, 200,
