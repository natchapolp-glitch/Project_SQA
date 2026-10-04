package org.apache.commons.lang3;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class LocaleUtilsAdditionalTest {

    @Test
    public void testConstructor() {
        assertNotNull(new LocaleUtils());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLength1() {
        LocaleUtils.toLocale("a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLength3() {
        LocaleUtils.toLocale("en_");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidLength6() {
        LocaleUtils.toLocale("en_GB_x");
    }
}
