package sqa.development;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.Flat3Map;

/** Bounded real map/stream development. Expected JSON is supplied before execution. */
public final class CollectionsCandidateProbe {
    public static String activeCase = "";
    static String q(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }
    static String json(Object o) {
        if (o == null || o instanceof Boolean || o instanceof Number) return String.valueOf(o);
        if (o instanceof String) return q((String)o);
        if (o instanceof Map) {
            List<String> parts = new ArrayList<String>();
            for (Object entry : ((Map)o).entrySet()) {
                Map.Entry e = (Map.Entry)entry;
                parts.add(q((String)e.getKey()) + ":" + json(e.getValue()));
            }
            return "{" + String.join(",", parts) + "}";
        }
        List<String> parts = new ArrayList<String>();
        for (Object item : (List)o) parts.add(json(item));
        return "[" + String.join(",", parts) + "]";
    }
    static int compare(Object a, Object b) {
        return a == null ? (b == null ? 0 : -1) : b == null ? 1 : ((String)a).compareTo((String)b);
    }
    static List pairs(Map map) {
        List<List> entries = new ArrayList<List>();
        for (Object entry : map.entrySet()) {
            Map.Entry e = (Map.Entry)entry;
            entries.add(Arrays.asList(e.getKey(), e.getValue()));
        }
        Collections.sort(entries, new Comparator<List>() { public int compare(List a, List b) { return CollectionsCandidateProbe.compare(a.get(0), b.get(0)); } });
        return entries;
    }
    static Map seed(String name) {
        if (name.equals("null")) return null;
        Map map = new LinkedHashMap();
        if (name.equals("one")) map.put("k0", "v0");
        if (name.equals("three") || name.equals("four")) {
            map.put(null, "v0"); map.put("k1", null); map.put("k2", "v2");
        }
        if (name.equals("four")) map.put("k3", "v3");
        return map;
    }
    static byte[] serialize(Object map) throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bytes)) { out.writeObject(map); }
        return bytes.toByteArray();
    }
    static Flat3Map deserialize(byte[] bytes) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (Flat3Map)in.readObject();
        }
    }
    static Object field(Flat3Map map, String name) throws Exception {
        Field f = Flat3Map.class.getDeclaredField(name); f.setAccessible(true); return f.get(map);
    }
    static String storage(Flat3Map map) throws Exception {
        return map == null ? "absent" : field(map, "delegateMap") == null ? "flat" : "delegate";
    }
    static boolean cleared(Flat3Map map) throws Exception {
        for (String name : Arrays.asList("size", "hash1", "hash2", "hash3"))
            if (!field(map, name).equals(0)) return false;
        for (String name : Arrays.asList("key1", "key2", "key3", "value1", "value2", "value3"))
            if (field(map, name) != null) return false;
        return true;
    }
    static Map observation(String[] c) throws Exception {
        String method = c[1], fixture = c[2];
        Map input = seed(fixture);
        List before = input == null ? null : pairs(input);
        Flat3Map map = method.equals("<init>") || method.equals("readObject") ? null : new Flat3Map(input);
        String storageBefore = storage(map);
        byte[] serialized = method.equals("readObject") ? serialize(new Flat3Map(input)) : null;
        // All fixture construction and source serialization finish before target activation.
        Object value = null, distinct = null, independent = null, flatCleared = null;
        String exception = null, returnedStorage = null;
        boolean sourceUnchanged = true;
        try {
            if (method.equals("<init>")) {
                activeCase = c[0];
                try { map = new Flat3Map(input); } finally { activeCase = ""; }
                value = pairs(map); distinct = map != input; returnedStorage = storage(map);
                sourceUnchanged = pairs(input).equals(before);
                input.put("__later", "input-only"); independent = pairs(map).equals(before);
            } else if (method.equals("readObject")) {
                activeCase = c[0];
                try { map = deserialize(serialized); } finally { activeCase = ""; }
                value = pairs(map); distinct = map != input; returnedStorage = storage(map);
                map.put("__later", "copy-only"); independent = pairs(input).equals(before);
                map.remove("__later");
            } else if (method.equals("writeObject")) {
                activeCase = c[0];
                byte[] bytes;
                try { bytes = serialize(map); } finally { activeCase = ""; }
                Flat3Map copy = deserialize(bytes); value = pairs(copy); distinct = copy != map;
                copy.put("__later", "copy-only"); independent = pairs(map).equals(before);
            } else {
                Method target = Flat3Map.class.getDeclaredMethod(method);
                target.setAccessible(true);
                Object result;
                activeCase = c[0];
                try { result = target.invoke(map); } finally { activeCase = ""; }
                if (method.equals("convertToMap")) {
                    flatCleared = cleared(map);
                } else if (method.equals("createDelegateMap")) {
                    Map delegate = (Map)result; value = pairs(delegate); distinct = delegate != map;
                    delegate.put("__later", "delegate-only"); independent = pairs(map).equals(before);
                } else if (method.equals("hashCode")) {
                    value = result;
                } else if (method.equals("mapIterator")) {
                    MapIterator iterator = (MapIterator)result;
                    List<List> entries = new ArrayList<List>();
                    while (iterator.hasNext()) {
                        Object key = iterator.next();
                        Object old = iterator.getValue();
                        if (!Objects.equals(key, iterator.getKey())) throw new AssertionError("Iterator key differs");
                        entries.add(Arrays.asList(key, old));
                        String changedKey = fixture.equals("one") ? "k0" : fixture.equals("three") ? "k2" : "k3";
                        if (Objects.equals(key, changedKey) && !Objects.equals(old, iterator.setValue("changed")))
                            throw new AssertionError("Iterator old value differs");
                    }
                    Collections.sort(entries, new Comparator<List>() { public int compare(List a, List b) { return CollectionsCandidateProbe.compare(a.get(0), b.get(0)); } }); value = entries;
                } else {
                    Collection view = (Collection)result;
                    List items = new ArrayList();
                    if (method.equals("entrySet")) {
                        for (Object entry : view) {
                            Map.Entry e = (Map.Entry)entry;
                            items.add(Arrays.asList(e.getKey(), e.getValue()));
                        }
                        Collections.sort(items, new Comparator() { public int compare(Object a, Object b) { return CollectionsCandidateProbe.compare(((List)a).get(0), ((List)b).get(0)); } });
                    } else {
                        items.addAll(view); Collections.sort(items, new Comparator() { public int compare(Object a, Object b) { return CollectionsCandidateProbe.compare(a, b); } });
                    }
                    value = items; view.clear();
                }
                sourceUnchanged = pairs(map).equals(before);
            }
        } catch (InvocationTargetException error) {
            exception = error.getCause().getClass().getName();
        } catch (RuntimeException error) {
            exception = error.getClass().getName();
        } finally { activeCase = ""; }
        Map row = new LinkedHashMap();
        row.put("value", value); row.put("exception_class", exception);
        row.put("pre_map", before); row.put("post_map", map == null ? null : pairs(map));
        row.put("source_unchanged", sourceUnchanged); row.put("distinct_result", distinct);
        row.put("independent_result", independent); row.put("storage_before", storageBefore);
        row.put("storage_after", returnedStorage == null ? storage(map) : returnedStorage);
        row.put("flat_slots_cleared", flatCleared);
        return row;
    }
    public static void main(String[] args) throws Exception {
        // Load classes and private fields outside the target activation window.
        new Flat3Map(); Flat3Map.class.getDeclaredField("delegateMap");
        int count = 0, failed = 0, fixtureErrors = 0;
        for (String line : Files.readAllLines(Paths.get(args[0]), StandardCharsets.UTF_8)) {
            String[] c = line.split("\t", -1);
            Map obs = null; String setupError = null;
            try { obs = observation(c); } catch (Exception error) { setupError = error.getClass().getName(); }
            boolean pass = setupError == null && json(obs).equals(c[4]);
            Map row = new LinkedHashMap(); row.put("case", c[0]); row.put("method", c[1]);
            row.put("descriptor", c[3]); row.put("declaring_class", Flat3Map.class.getName());
            row.put("setup_succeeded", setupError == null); row.put("fixture_error", setupError);
            row.put("observation", obs); row.put("target_check_passed", pass);
            row.put("failure_class", pass ? null : "java.lang.AssertionError");
            System.out.println(json(row)); count++; if (!pass) failed++; if (setupError != null) fixtureErrors++;
        }
        Map summary = new LinkedHashMap(); summary.put("summary", true); summary.put("executed", count);
        summary.put("target_checks", count); summary.put("passed", count - failed); summary.put("failed", failed);
        summary.put("skipped", 0); summary.put("fixture_errors", fixtureErrors);
        System.out.println(json(summary)); if (failed > 0) System.exit(1);
    }
}
