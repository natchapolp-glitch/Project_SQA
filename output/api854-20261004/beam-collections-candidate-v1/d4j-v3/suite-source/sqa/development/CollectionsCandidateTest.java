package sqa.development;
import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.map.Flat3Map;

public class CollectionsCandidateTest {
    static int executed, checks;
    static void group(String[][] cases) throws Exception {
        executed++; String failures = "";
        for (String[] c : cases) {
            java.util.Map observation;
            try { observation = CollectionsCandidateProbe.observation(c); }
            catch (Throwable error) { throw new AssertionError("SQA_HARNESS Collections setup/projection failed: " + c[0], error); }
            if (!CollectionsCandidateProbe.json(observation).equals(c[4])) failures += c[0] + " ";
        }
        checks++; org.junit.Assert.assertEquals("Full independent bounded observations differ: " + failures, "", failures);
    }
    @org.junit.AfterClass public static void counts() throws Exception {
        String json = "{\"schema_version\":1,\"executed\":" + executed + ",\"skipped\":0,\"target_checks\":" + checks + "}";
        java.nio.file.Files.write(java.nio.file.Paths.get("sqa-stage-counts.json"), json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
public static final class CollectionsCandidateProbe {
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

@org.junit.Test public void bounded_convertToMap() throws Exception { group(new String[][]{new String[]{"convertToMap_empty","convertToMap","empty","()V","{\"value\":null,\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":true}"},new String[]{"convertToMap_one","convertToMap","one","()V","{\"value\":null,\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[[\"k0\",\"v0\"]],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":true}"},new String[]{"convertToMap_three","convertToMap","three","()V","{\"value\":null,\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":true}"}}); }
@org.junit.Test public void bounded_createDelegateMap() throws Exception { group(new String[][]{new String[]{"createDelegateMap_empty","createDelegateMap","empty","()Lorg/apache/commons/collections/map/AbstractHashedMap;","{\"value\":[],\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"createDelegateMap_one","createDelegateMap","one","()Lorg/apache/commons/collections/map/AbstractHashedMap;","{\"value\":[],\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[[\"k0\",\"v0\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"createDelegateMap_three","createDelegateMap","three","()Lorg/apache/commons/collections/map/AbstractHashedMap;","{\"value\":[],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"createDelegateMap_four","createDelegateMap","four","()Lorg/apache/commons/collections/map/AbstractHashedMap;","{\"value\":[],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"delegate\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":null}"}}); }
@org.junit.Test public void bounded_entrySet() throws Exception { group(new String[][]{new String[]{"entrySet_empty","entrySet","empty","()Ljava/util/Set;","{\"value\":[],\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"entrySet_one","entrySet","one","()Ljava/util/Set;","{\"value\":[[\"k0\",\"v0\"]],\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"entrySet_three","entrySet","three","()Ljava/util/Set;","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"entrySet_four","entrySet","four","()Ljava/util/Set;","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"post_map\":[],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"delegate\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":null}"}}); }
@org.junit.Test public void bounded_hashCode() throws Exception { group(new String[][]{new String[]{"hashCode_empty","hashCode","empty","()I","{\"value\":0,\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"hashCode_one","hashCode","one","()I","{\"value\":863,\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[[\"k0\",\"v0\"]],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"hashCode_three","hashCode","three","()I","{\"value\":7931,\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"hashCode_four","hashCode","four","()I","{\"value\":8784,\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"delegate\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":null}"}}); }
@org.junit.Test public void bounded_keySet() throws Exception { group(new String[][]{new String[]{"keySet_empty","keySet","empty","()Ljava/util/Set;","{\"value\":[],\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"keySet_one","keySet","one","()Ljava/util/Set;","{\"value\":[\"k0\"],\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"keySet_three","keySet","three","()Ljava/util/Set;","{\"value\":[null,\"k1\",\"k2\"],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"keySet_four","keySet","four","()Ljava/util/Set;","{\"value\":[null,\"k1\",\"k2\",\"k3\"],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"post_map\":[],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"delegate\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":null}"}}); }
@org.junit.Test public void bounded_mapIterator() throws Exception { group(new String[][]{new String[]{"mapIterator_empty","mapIterator","empty","()Lorg/apache/commons/collections/MapIterator;","{\"value\":[],\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"mapIterator_one","mapIterator","one","()Lorg/apache/commons/collections/MapIterator;","{\"value\":[[\"k0\",\"v0\"]],\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[[\"k0\",\"changed\"]],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"mapIterator_three","mapIterator","three","()Lorg/apache/commons/collections/MapIterator;","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"changed\"]],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"mapIterator_four","mapIterator","four","()Lorg/apache/commons/collections/MapIterator;","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"changed\"]],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"delegate\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":null}"}}); }
@org.junit.Test public void bounded_readObject() throws Exception { group(new String[][]{new String[]{"readObject_empty","readObject","empty","(Ljava/io/ObjectInputStream;)V","{\"value\":[],\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"absent\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"readObject_one","readObject","one","(Ljava/io/ObjectInputStream;)V","{\"value\":[[\"k0\",\"v0\"]],\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[[\"k0\",\"v0\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"absent\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"readObject_three","readObject","three","(Ljava/io/ObjectInputStream;)V","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"absent\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"readObject_four","readObject","four","(Ljava/io/ObjectInputStream;)V","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"absent\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":null}"}}); }
@org.junit.Test public void bounded_values() throws Exception { group(new String[][]{new String[]{"values_empty","values","empty","()Ljava/util/Collection;","{\"value\":[],\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"values_one","values","one","()Ljava/util/Collection;","{\"value\":[\"v0\"],\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"values_three","values","three","()Ljava/util/Collection;","{\"value\":[null,\"v0\",\"v2\"],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"values_four","values","four","()Ljava/util/Collection;","{\"value\":[null,\"v0\",\"v2\",\"v3\"],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"post_map\":[],\"source_unchanged\":false,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"delegate\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":null}"}}); }
@org.junit.Test public void bounded_writeObject() throws Exception { group(new String[][]{new String[]{"writeObject_empty","writeObject","empty","(Ljava/io/ObjectOutputStream;)V","{\"value\":[],\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"writeObject_one","writeObject","one","(Ljava/io/ObjectOutputStream;)V","{\"value\":[[\"k0\",\"v0\"]],\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[[\"k0\",\"v0\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"writeObject_three","writeObject","three","(Ljava/io/ObjectOutputStream;)V","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"flat\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"writeObject_four","writeObject","four","(Ljava/io/ObjectOutputStream;)V","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"delegate\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":null}"}}); }
@org.junit.Test public void bounded_constructor() throws Exception { group(new String[][]{new String[]{"constructor_empty","<init>","empty","(Ljava/util/Map;)V","{\"value\":[],\"exception_class\":null,\"pre_map\":[],\"post_map\":[],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"absent\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"constructor_one","<init>","one","(Ljava/util/Map;)V","{\"value\":[[\"k0\",\"v0\"]],\"exception_class\":null,\"pre_map\":[[\"k0\",\"v0\"]],\"post_map\":[[\"k0\",\"v0\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"absent\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"constructor_three","<init>","three","(Ljava/util/Map;)V","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"absent\",\"storage_after\":\"flat\",\"flat_slots_cleared\":null}"},new String[]{"constructor_four","<init>","four","(Ljava/util/Map;)V","{\"value\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"exception_class\":null,\"pre_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"post_map\":[[null,\"v0\"],[\"k1\",null],[\"k2\",\"v2\"],[\"k3\",\"v3\"]],\"source_unchanged\":true,\"distinct_result\":true,\"independent_result\":true,\"storage_before\":\"absent\",\"storage_after\":\"delegate\",\"flat_slots_cleared\":null}"},new String[]{"constructor_null","<init>","null","(Ljava/util/Map;)V","{\"value\":null,\"exception_class\":\"java.lang.NullPointerException\",\"pre_map\":null,\"post_map\":null,\"source_unchanged\":true,\"distinct_result\":null,\"independent_result\":null,\"storage_before\":\"absent\",\"storage_after\":\"absent\",\"flat_slots_cleared\":null}"}}); }
}
