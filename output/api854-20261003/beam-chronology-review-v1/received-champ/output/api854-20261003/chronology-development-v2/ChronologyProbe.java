package org.joda.time;

import java.util.Arrays;
import org.joda.time.chrono.BuddhistChronology;
import org.joda.time.chrono.ISOChronology;

/** Bounded candidate oracle; package access preserves exact protected/internal targets. */
public final class ChronologyProbe {
    public static String activeCase = "";
    private static String observation = "{}";
    private static int executed, passed, fixtureErrors;
    private static final DateTimeZone OFFSET = DateTimeZone.forOffsetHours(7);
    private static final Chronology ISO = ISOChronology.getInstanceUTC();
    private static final Chronology BUDDHIST = BuddhistChronology.getInstanceUTC();
    private static final DateTimeFieldType HOUR = DateTimeFieldType.hourOfDay();
    private static final DateTimeFieldType YEAR = DateTimeFieldType.year();
    private static final DateTimeFieldType MONTH = DateTimeFieldType.monthOfYear();
    private static final DateTimeFieldType DAY = DateTimeFieldType.dayOfMonth();

    private interface Action { void run() throws Exception; }
    private static void require(boolean ok, String message) {
        if (!ok) throw new AssertionError(message);
    }
    private static String quote(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r") + "\"";
    }
    private static DateTimeFieldType[] dateTypes() { return new DateTimeFieldType[]{YEAR, MONTH, DAY}; }
    private static String snapshot(Partial p) {
        StringBuilder types = new StringBuilder("[");
        for (int i = 0; i < p.size(); i++) {
            if (i > 0) types.append(',');
            types.append(quote(p.getFieldType(i).getName()));
        }
        return "{\"chronology\":" + quote(p.getChronology().getClass().getName())
                + ",\"zone\":" + quote(p.getChronology().getZone().getID())
                + ",\"types\":" + types.append(']') + ",\"values\":" + Arrays.toString(p.getValues()) + "}";
    }
    private static void assertPartial(Partial p, Chronology chronology, DateTimeFieldType[] types, int[] values) {
        require(p.getChronology().equals(chronology), "Chronology projection differs");
        require(p.getChronology().getZone().equals(DateTimeZone.UTC), "Expected UTC normalization");
        require(p.size() == types.length && Arrays.equals(p.getFieldTypes(), types), "Field types differ");
        require(Arrays.equals(p.getValues(), values), "Field values differ");
        for (int i = 0; i < types.length; i++) require(p.get(types[i]) == values[i], "Named field value differs");
    }
    private static void rejection(Action action, Class<? extends Throwable> expected, String method) throws Exception {
        Throwable actual = null;
        try { action.run(); } catch (Throwable failure) { actual = failure; }
        observation = "{\"rejected\":" + (actual != null)
                + ",\"exception\":" + (actual == null ? "null" : quote(actual.getClass().getName())) + "}";
        require(actual != null && expected.isInstance(actual), "Expected rejection: " + expected.getName());
        boolean origin = false;
        for (StackTraceElement frame : actual.getStackTrace())
            if (frame.getClassName().equals("org.joda.time.Partial") && frame.getMethodName().equals(method)) origin = true;
        require(origin, "Exception did not pass through exact target");
    }
    private static void runCase(String name, Action action) {
        activeCase = "";
        observation = "{}";
        Throwable failure = null;
        try { action.run(); } catch (Throwable caught) { failure = caught; }
        boolean setup = name.equals(activeCase);
        if (setup) { executed++; if (failure == null) passed++; } else fixtureErrors++;
        System.out.println("{\"case\":" + quote(name) + ",\"setup_succeeded\":" + setup
                + ",\"target_check_passed\":" + (setup && failure == null) + ",\"observation\":" + observation
                + ",\"failure_class\":" + (failure == null ? "null" : quote(failure.getClass().getName()))
                + ",\"failure_reason\":" + (failure == null ? "null" : quote(String.valueOf(failure.getMessage()))) + "}");
        activeCase = "";
    }

    public static void main(String[] args) {
        // All inputs and assertions are fixed before any buggy execution.
        runCase("empty_iso_offset", () -> {
            Chronology input = ISOChronology.getInstance(OFFSET);
            activeCase = "empty_iso_offset";
            Partial p = new Partial(input);
            observation = snapshot(p);
            assertPartial(p, ISO, new DateTimeFieldType[0], new int[0]);
        });
        runCase("empty_null", () -> {
            activeCase = "empty_null";
            Partial p = new Partial((Chronology) null);
            observation = snapshot(p);
            assertPartial(p, ISO, new DateTimeFieldType[0], new int[0]);
        });
        runCase("single_hour_iso", () -> {
            Chronology input = ISOChronology.getInstance(OFFSET);
            activeCase = "single_hour_iso";
            Partial p = new Partial(HOUR, 10, input);
            observation = snapshot(p);
            assertPartial(p, ISO, new DateTimeFieldType[]{HOUR}, new int[]{10});
        });
        runCase("single_invalid_hour", () -> {
            activeCase = "single_invalid_hour";
            rejection(() -> new Partial(HOUR, 24, ISO), IllegalArgumentException.class, "<init>");
        });
        runCase("arrays_leap_iso", () -> {
            DateTimeFieldType[] types = dateTypes();
            int[] values = new int[]{2024, 2, 29};
            Chronology input = ISOChronology.getInstance(OFFSET);
            activeCase = "arrays_leap_iso";
            Partial p = new Partial(types, values, input);
            types[0] = HOUR; values[0] = 1;
            DateTimeFieldType[] returnedTypes = p.getFieldTypes(); returnedTypes[0] = HOUR;
            int[] returnedValues = p.getValues(); returnedValues[0] = 1;
            observation = snapshot(p);
            assertPartial(p, ISO, dateTypes(), new int[]{2024, 2, 29});
        });
        runCase("arrays_invalid_date", () -> {
            DateTimeFieldType[] types = dateTypes();
            activeCase = "arrays_invalid_date";
            rejection(() -> new Partial(types, new int[]{2024, 2, 30}, ISO), IllegalArgumentException.class, "<init>");
        });
        runCase("arrays_bad_order", () -> {
            DateTimeFieldType[] types = new DateTimeFieldType[]{YEAR, DAY, DateTimeFieldType.era()};
            activeCase = "arrays_bad_order";
            rejection(() -> new Partial(types, new int[]{1, 1, 1}, ISO), IllegalArgumentException.class, "<init>");
        });
        runCase("internal_iso", () -> {
            // Package-private constructor deliberately skips validation/normalization.
            // Supply an already validated UTC input; do not claim it clones its inputs.
            DateTimeFieldType[] types = dateTypes();
            int[] values = new int[]{2024, 2, 29};
            Partial validated = new Partial(types, values, ISO);
            assertPartial(validated, ISO, types, values);
            activeCase = "internal_iso";
            Partial p = new Partial(ISO, types, values);
            observation = snapshot(p);
            assertPartial(p, ISO, types, values);
        });
        runCase("getfield_buddhist", () -> {
            // Begin with the target's default receiver, then seed a valid field.
            Partial receiver = new Partial().with(YEAR, 2024);
            assertPartial(receiver, ISO, new DateTimeFieldType[]{YEAR}, new int[]{2024});
            String before = snapshot(receiver);
            Chronology input = BuddhistChronology.getInstance(OFFSET);
            activeCase = "getfield_buddhist";
            DateTimeField field = receiver.getField(0, input);
            observation = "{\"field\":" + quote(field.getName()) + ",\"epoch_year\":" + field.get(0L)
                    + ",\"receiver\":" + snapshot(receiver) + "}";
            require(field == input.year(), "Field must come from supplied chronology");
            require(field.getType() == YEAR && field.get(0L) == 2513, "Buddhist year oracle differs");
            require(snapshot(receiver).equals(before), "Receiver state changed");
        });
        runCase("getfield_bad_index", () -> {
            Partial receiver = new Partial().with(HOUR, 10);
            assertPartial(receiver, ISO, new DateTimeFieldType[]{HOUR}, new int[]{10});
            String before = snapshot(receiver);
            activeCase = "getfield_bad_index";
            rejection(() -> receiver.getField(1, ISO), IndexOutOfBoundsException.class, "getField");
            require(snapshot(receiver).equals(before), "Receiver changed after rejection");
        });
        runCase("withchrono_buddhist", () -> {
            Partial receiver = new Partial().with(HOUR, 10);
            assertPartial(receiver, ISO, new DateTimeFieldType[]{HOUR}, new int[]{10});
            Chronology input = BuddhistChronology.getInstance(OFFSET);
            String before = snapshot(receiver);
            activeCase = "withchrono_buddhist";
            Partial result = receiver.withChronologyRetainFields(input);
            observation = snapshot(result);
            require(result != receiver, "Different chronology must produce a new partial");
            assertPartial(result, BUDDHIST, new DateTimeFieldType[]{HOUR}, new int[]{10});
            require(snapshot(receiver).equals(before), "Original receiver changed");
        });
        runCase("withchrono_same", () -> {
            Partial receiver = new Partial().with(HOUR, 10);
            Chronology input = ISOChronology.getInstance(OFFSET);
            activeCase = "withchrono_same";
            Partial result = receiver.withChronologyRetainFields(input);
            observation = snapshot(result);
            require(result == receiver, "Same normalized chronology must reuse receiver");
            assertPartial(result, ISO, new DateTimeFieldType[]{HOUR}, new int[]{10});
        });
        runCase("withchrono_null", () -> {
            Partial receiver = new Partial().with(HOUR, 10).withChronologyRetainFields(BUDDHIST);
            assertPartial(receiver, BUDDHIST, new DateTimeFieldType[]{HOUR}, new int[]{10});
            String before = snapshot(receiver);
            activeCase = "withchrono_null";
            Partial result = receiver.withChronologyRetainFields(null);
            observation = snapshot(result);
            require(result != receiver, "Null chronology must normalize to ISO");
            assertPartial(result, ISO, new DateTimeFieldType[]{HOUR}, new int[]{10});
            require(snapshot(receiver).equals(before), "Original receiver changed");
        });
        System.out.println("{\"summary\":true,\"executed\":" + executed + ",\"target_checks\":" + executed
                + ",\"passed\":" + passed + ",\"failed\":" + (executed - passed)
                + ",\"skipped\":0,\"fixture_errors\":" + fixtureErrors + "}");
        if (passed != 13 || fixtureErrors != 0) System.exit(1);
    }
}
