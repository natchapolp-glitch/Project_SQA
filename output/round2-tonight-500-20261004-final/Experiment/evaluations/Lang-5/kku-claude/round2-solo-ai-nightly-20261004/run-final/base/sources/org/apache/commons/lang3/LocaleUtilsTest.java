package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocaleValidFormats() {
        assertEquals(new Locale("en", ""), LocaleUtils.toLocale("en"));
        assertEquals(new Locale("en", "GB"), LocaleUtils.toLocale("en_GB"));
        assertEquals(new Locale("en", "GB", "xxx"), LocaleUtils.toLocale("en_GB_xxx"));
        assertEquals(new Locale("en", "", "POSIX"), LocaleUtils.toLocale("en__POSIX"));
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test
    public void testToLocaleInvalidFormats() {
        try {
            LocaleUtils.toLocale("E");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("EN");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en_gb");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("en-GB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        try {
            LocaleUtils.toLocale("enGB");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupListSingleArg() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertEquals(3, list.size());
        assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        try {
            list.add(Locale.US);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testLocaleLookupListWithDefault() {
        Locale locale = new Locale("fr", "CA", "xxx");
        Locale defaultLocale = new Locale("en");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertEquals(4, list.size());
        assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
        assertEquals(new Locale("en"), list.get(3));

        List<Locale> emptyList = LocaleUtils.localeLookupList(null, null);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testAvailableLocales() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertFalse(list.isEmpty());
        assertFalse(set.isEmpty());
        assertTrue(list.contains(Locale.US));
        assertTrue(set.contains(Locale.US));
        assertEquals(set.size(), list.size());
        try {
            list.add(Locale.US);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testIsAvailableLocale() {
        assertTrue(LocaleUtils.isAvailableLocale(Locale.US));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("xx", "XX")));
        assertFalse(LocaleUtils.isAvailableLocale(null));
    }

    @Test
    public void testLanguagesByCountry() {
        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
        assertTrue(LocaleUtils.languagesByCountry("XX").isEmpty());

        List<Locale> list = LocaleUtils.languagesByCountry("GB");
        assertFalse(list.isEmpty());
        for (Locale l : list) {
            assertEquals("GB", l.getCountry());
            assertTrue(l.getVariant().isEmpty());
        }
        List<Locale> list2 = LocaleUtils.languagesByCountry("GB");
        assertEquals(list, list2);
        try {
            list.add(Locale.US);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testCountriesByLanguage() {
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
        assertTrue(LocaleUtils.countriesByLanguage("xx").isEmpty());

        List<Locale> list = LocaleUtils.countriesByLanguage("en");
        assertFalse(list.isEmpty());
        for (Locale l : list) {
            assertEquals("en", l.getLanguage());
            assertFalse(l.getCountry().isEmpty());
            assertTrue(l.getVariant().isEmpty());
        }
        List<Locale> list2 = LocaleUtils.countriesByLanguage("en");
        assertEquals(list, list2);
        try {
            list.add(Locale.US);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}
