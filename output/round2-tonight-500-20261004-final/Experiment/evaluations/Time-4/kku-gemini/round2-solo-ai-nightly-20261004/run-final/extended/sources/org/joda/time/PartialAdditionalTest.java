package org.joda.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PartialAdditionalTest {

    @Test(expected = IllegalArgumentException.class)
    public void testArrayConstructorNullTypes() {
        new Partial(null, new int[] { 2023 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayConstructorNullValues() {
        new Partial(new DateTimeFieldType[] { DateTimeFieldType.year() }, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayConstructorMismatchedLengths() {
        new Partial(new DateTimeFieldType[] { DateTimeFieldType.year() }, new int[] { 2023, 6 });
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayConstructorNullTypeInArray() {
        new Partial(new DateTimeFieldType[] { null }, new int[] { 2023 });
    }

}
