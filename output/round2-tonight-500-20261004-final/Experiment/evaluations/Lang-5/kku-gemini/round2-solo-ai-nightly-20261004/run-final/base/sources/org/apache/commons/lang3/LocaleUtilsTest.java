package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Test;

public class LocaleUtilsTest {

    @Test
    public void testToLocale_Valid() {
        Locale loc1 = LocaleUtils.toLocale("en");
        assertNotNull(loc1);
        assertEquals("en", loc1.getLanguage());
        assertEquals("", loc1.getCountry());

        Locale loc2 = LocaleUtils.toLocale("en_GB");
        assertNotNull(loc2);
        assertEquals("en", loc2.getLanguage());
        assertEquals("GB", loc2.getCountry());

        Locale loc3 = LocaleUtils.toLocale("en_GB_xxx");
        assertNotNull(loc3);
        assertEquals("en", loc3.getLanguage());
        assertEquals("GB", loc3.getCountry());
        assertEquals("xxx", loc3.getVariant());
    }

    @Test
    public void testToLocale_Null() {
        assertNull(LocaleUtils.toLocale(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToLocale_InvalidFormat() {
        LocaleUtils.toLocale("invalid");
    }

    @Test
    public void testLocaleLookupList() {
        Locale locale = new Locale("fr", "CA", "xxx");
        List<Locale> list = LocaleUtils.localeLookupList(locale);
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(new Locale("fr", "CA", "xxx"), list.get(0));
        assertEquals(new Locale("fr", "CA"), list.get(1));
        assertEquals(new Locale("fr"), list.get(2));
    }

    @Test
    public void testLocaleLookupListWithDefault() {
        Locale locale = new Locale("fr", "CA");
        Locale defaultLocale = new Locale("en", "US");
        List<Locale> list = LocaleUtils.localeLookupList(locale, defaultLocale);
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(new Locale("fr", "CA"), list.get(0));
        assertEquals(new Locale("fr"), list.get(1));
        assertEquals(new Locale("en", "US"), list.get(2));
    }

    @Test
    public void testAvailableLocaleMethods() {
        List<Locale> list = LocaleUtils.availableLocaleList();
        assertNotNull(list);
        assertFalse(list.isEmpty());

        Set<Locale> set = LocaleUtils.availableLocaleSet();
        assertNotNull(set);
        assertFalse(set.isEmpty());

        Locale us = Locale.US;
        assertEquals(list.contains(us), LocaleUtils.isAvailableLocale(us));
        assertFalse(LocaleUtils.isAvailableLocale(new Locale("q1", "Q2", "Q3")));
    }

    @Test
    public void testLanguagesAndCountriesBy() {
        List<Locale> langs = LocaleUtils.languagesByCountry("US");
        assertNotNull(langs);

        List<Locale> countries = LocaleUtils.countriesByLanguage("en");
        assertNotNull(countries);

        assertTrue(LocaleUtils.languagesByCountry(null).isEmpty());
        assertTrue(LocaleUtils.countriesByLanguage(null).isEmpty());
    }
}
