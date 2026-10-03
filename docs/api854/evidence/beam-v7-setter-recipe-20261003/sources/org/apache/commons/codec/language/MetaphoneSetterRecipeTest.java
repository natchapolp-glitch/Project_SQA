package org.apache.commons.codec.language;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Prospective handwritten recipe proof, separate from the four study approaches. */
public class MetaphoneSetterRecipeTest {
    private static final AtomicInteger EXECUTED = new AtomicInteger();
    private static final AtomicInteger TARGET_CHECKS = new AtomicInteger();

    private void checkSetter(int limit) {
        EXECUTED.incrementAndGet();
        Metaphone receiver = new Metaphone();
        receiver.setMaxCodeLen(limit);
        TARGET_CHECKS.incrementAndGet();
        assertEquals("Setter must change observable receiver state", limit, receiver.getMaxCodeLen());
        String encoded = receiver.metaphone("architecture");
        assertTrue("Encoding respects the configured bound", encoded.length() <= limit);
        assertEquals("Encoding must preserve the configured limit", limit, receiver.getMaxCodeLen());
    }

    @Test(timeout=10000) public void zeroLimit() { checkSetter(0); }
    @Test(timeout=10000) public void oneCharacterLimit() { checkSetter(1); }
    @Test(timeout=10000) public void defaultSizedLimit() { checkSetter(4); }
    @Test(timeout=10000) public void largerLimit() { checkSetter(8); }

    @AfterClass public static void retainCounts() throws Exception {
        String counts = "{\"schema_version\":1,\"executed\":" + EXECUTED.get()
                + ",\"skipped\":0,\"target_checks\":" + TARGET_CHECKS.get() + "}\n";
        Files.write(Paths.get("sqa-stage-counts.json"), counts.getBytes(StandardCharsets.UTF_8));
    }
}
