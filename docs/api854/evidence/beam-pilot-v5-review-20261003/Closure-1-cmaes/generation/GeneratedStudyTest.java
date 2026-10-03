import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
public class GeneratedStudyTest {
  private static final java.util.concurrent.atomic.AtomicInteger EXECUTED = new java.util.concurrent.atomic.AtomicInteger();
  private static final java.util.concurrent.atomic.AtomicInteger TARGET_CHECKS = new java.util.concurrent.atomic.AtomicInteger();
  @org.junit.AfterClass public static void retainStageCounts() throws Exception {
    String report = "{\"schema_version\":1,\"executed\":" + EXECUTED.get() + ",\"skipped\":0,\"target_checks\":" + TARGET_CHECKS.get() + "}\n";
    Files.write(Paths.get("sqa-stage-counts.json"), report.getBytes(StandardCharsets.UTF_8));
  }
  @Test(timeout=10000)
  public void generated1() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 2.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.19275191598468638, 1, -0.29502854980607007}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated2() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.33753704139927643, 0.23283417482554164, -0.46267521452595367, 0.21717172739210969, 0.85730445817292322, 0.71650494943003107, -0.37697080129172161, -0.80302803370382236, 0.4445014790358171, -0.40473810276689071, -0.45671139620883039, 0.43279433032780984, -0.73646665111255394, 0.5035220270807621, 0.6696236056587499, 0.027441509136262351, -0.53909145252019786, -0.12079846485769112}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated3() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.12832943474678932, 0.77217563711311499, 0.045872834140195586, 0.090821818057883724, 0.1307339591918435, 0.093150983379294144, 0.20070505829727012, 0.53243585678343874, 0.74023053518358728, -0.077508167005142722, -0.56432122761603942, -0.3921202415456484, -0.55710208988686183, -1, 0.011923870537382713, -0.4553092635391221, 1, -0.32926499126108122}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated4() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.25126079012048497, 0.0093941525821895529, 0.54158053821501517}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated5() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.21025390616344158, 1, -0.3317815852655342, -0.11732062694621649, 0.19129711544027342, -0.87495743092009015, -0.27542383336715137, -0.12175711119727206, 0.16675745809446776, -0.20054893332519608, 1, 1, 0.76124648573485165, -4.6455512270862728e-05, 0.40579399477970773}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated6() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.72038469793523263, 0.49371923411137969, 0.071252638175748256, -0.060797673131111001, -0.7930064999182006, -0.87752891804063471, -0.75292737371422824, 0.19928163591665377, 0.1481242503897125, -0.41743967712960767, -0.12834228878667803, 0.87243104909043756, -0.70306375355169737, -0.23990876993713933, -0.71320547653638811}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated7() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.37471399254636167, 0.39895865985604273, 0.14174545310793471, 0.31069901760123675, 0.13745628932118678, 0.39948256790261466, 0.37724703512595131, -0.44713268760754271, 0.47654779152030746, -0.45852951315237717, -0.77637662251180106, -0.21943002933363026, -0.52734220018268252, -0.3946036477174989, -0.6472267139526553, 0.52454705185353734, 0.5414362171196252, -0.37501790087675724}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated8() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.04253261418341437, -0.4432496125896625, -0.60681950885111802, 0.26202710574545679, -0.050106187479729405, 0.16510812825181748, 1, 0.57822425284876466, -0.040768529426973031, -0.64359658643514217, -0.23748795719091836, -0.41132936662745717, -0.36284272727406147, -1, 0.097564133299704162, -0.6641139476632324, 1, -0.13977256882343458}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated9() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.22121045864752714, -0.45867890837502273, 0.14245789368506659, -0.24843161395786006, -0.22308711057709732, -0.02125566985687833, -1, 0.85287664555289744, 0.347521163674963, -0.085763046033880561, 0.85134085161424855, -0.59396638020620018, -1, 0.16239113735875974, -0.52121884132143448, -0.042303679198522758, -0.33544049761425471, -0.1412387343766269}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated10() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 2.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.042180809789216935, 0.44091243239650135, 0.22183176037519303}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated11() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 2.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.030253853371241708, -1, -0.77087579699335562}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated12() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.17561465519680672, 0.71689414941506702, 0.50205195796701707, -0.043620017509802475, 0.089341850625662592, 0.95756262429139793, -0.58619046811762221, 0.49482630765244318, 0.68242419271397325, 0.95710864695336084, 0.88004482477531998, -0.48839542454055229, 0.49607593343184697, -0.21209279322324709, 0.50115229753827684}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated13() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.18148236763559078, 0.930025954693805, 0.33946899287623827, 0.75751613811415919, -0.057236520447652706, 0.093521339992942376, -0.87684604494070417, 0.045652830680038337, 1, -0.038241756040743688, 1, 1, 0.20543274717271956, 0.99293274253502462, 1}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated14() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.22444553591144867, -0.66983024416406334, -1}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated15() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-1, 0.95894373415080314, 0.60430376373202055, -0.030753268880501672, -0.39788614110077702, -0.13286868203775104, 1, -0.42408377308118395, -0.35921160360451598, -0.26617176955015642, 0.96990661482146034, 0.21674756065480913, -0.71159627514809487, -0.80145696086000873, 0.53993738117410084, -1, 0.84121092652944063, 0.44587490601736757}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated16() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.14295145033665452, 0.53856153217560998, -0.3292730659230203, -0.71793416598667115, -0.083442883736621232, -0.50675507826623312, 0.94881910670229896, 0.71018177867139276, 0.49646845338615148, 0.16382574230586872, 0.23353693346175353, 0.25508783074589941, 0.72891520388464659, 1, 1}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated17() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-1, 0.73821540655070472, 0.010831563679983197, -0.044008389823826682, -0.63156806653385311, 0.58647425114700669, -0.64308892395171557, -1, 0.40199354258388342, -0.20894397238880505, -0.54419584179193947, 0.34995871744721807, -0.15647467369693024, 0.41574977072750241, -0.28563609842022375, 0.21023647030950782, 0.31609730924789897, -0.34823285759726524}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated18() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 2.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.54846558362408737, 0.44389351441282343, -1}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated19() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.45499392956221346, 0.69724170623094095, -0.38586835035826866, -0.28964411362283538, 1, 0.54892825636794029, 0.088938177471432525, -0.095405950861685906, 0.83338220466150092, 0.097293786617706129, 0.2110826629839426, -0.04059437020482673, -1, 0.50448046806555702, 1}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated20() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.5291617709215668, 0.044805628186608937, -0.47898304760841598, 0.38793722360091576, 0.33581371299788298, 0.49140437906635098, 1, -0.88525571774235734, 1, 0.89857864992646774, 0.40273680212780638, 0.10330257513037724, 0.42265411684516296, 0.84748777976198186, 1}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated21() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.29813988904728439, 1, 0.17364207353192374, -0.76848254096643231, -0.56240843508153693, 0.11005200889171074, 0.0098954947353082079, -0.17677429327752306, 0.75378564141472526, -0.61277487711959822, 0.10085454461295423, 0.87625119619929359, -0.407150143726553, -0.1780376894488005, -0.10830725440840561}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated22() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 2.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.05227177462843445, 0.27914287336188387, 0.33298914193049839}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated23() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.40520351036846969, -0.19657346189604907, 0.15931202453422327, 0.50490030100498517, 0.65389171201926444, -0.95770738248743359, 0.14053536808983602, -0.78887861282597238, 1, 0.034059572369139285, 0.37249626229927651, 0.34009832428396808, -0.65022487751746405, 0.3162231959472428, 0.88117449452428487}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated24() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.58279964532287953, 0.57184613710992949, -0.029193862467845361, -1, 0.70742544650259631, -0.15063504565147623, 0.017842623291971949, -0.44427110839806955, 0.7331827531579691, 0.38240875345874303, 0.24687050501313565, 0.62011373694202421, -0.89324811134971527, 0.079879522841983408, -0.10930008059103891, 0.11092537106011896, 0.37871238425774473, -0.6037299741948372}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated25() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.0081791928370939845, -0.56811267197733906, 0.23490792211323713, -0.35970662656320845, 0.85661073961084988, 0.59197849574325156, 1, -0.12669435661102663, 0.38019124567503249, -0.51894566462243208, 0.56142042687278648, 0.70470448656796414, 0.27351196867932082, 0.52187668582039515, 0.015211862756557393}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated26() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.81984418638624734, 0.70803838164114064, -1, 0.14946168468226384, 0.10473143655463646, 0.37994612214544471, -0.4491118479303422, -0.47011395954912738, -0.32158743986414307, -0.48751498648290525, 0.96187574078315552, 0.51475500855438117, 0.84529568904075814, 0.80629533576529877, 1}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated27() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.87868834486621428, 0.66837753669567546, -1}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated28() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.012478369968553563, -0.044248306275511906, -0.16055355547029038, -0.33146724531982397, -0.071184978222344708, -0.81805954774904277, 0.93129383034359814, -0.73103065407708523, 0.6394515027039408, 0.12460642871286762, 1, 0.6251119758084811, -0.22576755149125194, 0.96511261586429942, 0.97950122431172826, -0.9704141177783846, 0.88859137113503783, -0.71622239896161011}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated29() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.62693321267191049, -0.43132606399367684, -0.13327880508534717, -1, 0.59076652607506974, 0.38088289521175822, -0.7080096365612093, -0.31052035381006049, 1, -0.48194818478697232, 0.7393404188101278, 0.81645734188412833, 0.59694007684661454, 1, 0.3888937622718901}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated30() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.47085778935323047, 0.13756188633912292, -0.80081229493014017}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }

/** Fixed-revision observations for explicitly supported, deterministic Java APIs.
 * No buggy source, patch, or triggering test is used during input generation.
 * The same source is packaged with the generated JUnit suite.
 */
public static final class SqaProbe {
    private static final String[] STRINGS = {
        "", "0", "1", "-1", "null", "true", "false", "abc", "ABC", " ",
        "0x0", "0x1", "0xFFFFFFFF", "1.0", "1e3", "NaN", "Infinity",
        "{}", "[]", "[1]", "{\"a\":1}", "a=b", "--help", "-x", "a,b",
        "1970-01-01", "a\\nb", "a\nb", "a\tb", "\u0e17\u0e14\u0e2a\u0e2d\u0e1a"
    };
    private static final long[] NUMBERS = {0, 1, -1, 2, -2, 10, -10, 127, 128,
        255, 256, 32767, -32768, Integer.MAX_VALUE, Integer.MIN_VALUE};

    private SqaProbe() { }

    /** Schema scaffolding carried in the suite; no benchmark test classes. */
    public static class GenericFixture<T> { public T value; public T[] array; public List<T> items; }
    public static class StringBinding extends GenericFixture<String> { }
    public static class IntegerBinding extends GenericFixture<Integer> { }
    public static class FixtureBean { public String value = "fixture-value"; }
    public interface FixtureMock { String accept(String value); }

    public static final String EXPLICIT_FIXTURES = "beam-explicit-fixtures-v3-proposal";
    public static final String SCALAR_FIXTURES = "beam-explicit-fixtures-v4-proposal";
    public static final String PILOT_FIXTURES = "beam-explicit-fixtures-v5-proposal";
    private static final ThreadLocal<FixtureSession> FIXTURES = new ThreadLocal<FixtureSession>();
    private static final ThreadLocal<Boolean> INVOKED = new ThreadLocal<Boolean>();

    /** A setup failure is never an observation of an uncalled target method. */
    private static final class FixtureFailure extends RuntimeException {
        FixtureFailure(String message, Throwable cause) { super(message, cause); }
    }

    // Production factories only: no dataset test classes, patches or buggy results.
    // Reflection keeps the helper compilable without project-specific dependencies.
    private static Object call(Object receiver, String name, Class<?>[] parameterTypes, Object... values)
            throws ReflectiveOperationException {
        Class<?> declaring = receiver instanceof Class ? (Class<?>)receiver : receiver.getClass();
        while (declaring != null) {
            try {
                Method method = declaring.getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                return method.invoke(receiver instanceof Class ? null : receiver, values);
            } catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }
        }
        throw new NoSuchMethodException(name);
    }

    private static Object construct(String name, Class<?>[] parameterTypes, Object... values)
            throws ReflectiveOperationException {
        Constructor<?> ctor = Class.forName(name).getDeclaredConstructor(parameterTypes);
        ctor.setAccessible(true);
        return ctor.newInstance(values);
    }

    private static final class FixtureSession {
        final String targetClass;
        final String method;
        final boolean pilot;
        boolean constructing;
        Object compiler, registry, scope, cfg, reverse, flow, closureNode, receiver;
        org.w3c.dom.Element domRoot;
        org.w3c.dom.Node domChild;
        Object jdomRoot, jdomChild;
        java.io.ByteArrayOutputStream archiveBytes;
        Object mapper, parser, context, collectionType, collectionDeserializer;
        Object mock, baseInvocation, actualInvocation;
        Object chartDataset, chartPlot, chartAxis, cleanupScript, cleanupExterns;
        int cleanupNodeIndex;

        @SuppressWarnings({"unchecked", "rawtypes"})
        void unusedClosure(double a) throws ReflectiveOperationException {
            if (compiler != null) return;
            Class<?> node = Class.forName("com.google.javascript.rhino.Node");
            Class<?> ac = Class.forName("com.google.javascript.jscomp.AbstractCompiler");
            compiler = construct("com.google.javascript.jscomp.Compiler", new Class<?>[]{});
            Object options = construct("com.google.javascript.jscomp.CompilerOptions", new Class<?>[]{});
            call(compiler, "initOptions", new Class<?>[]{options.getClass()}, options);
            cleanupExterns = call(compiler, "parseTestCode", new Class<?>[]{String.class}, "");
            cleanupScript = call(compiler, "parseTestCode", new Class<?>[]{String.class},
                "var unused = 1; function fixture(x) { var local = " + (a < 0 ? "2" : "3") + "; return x; } fixture(1);");
            // Normalize traverses sibling roots and requires their common parent.
            int block = Class.forName("com.google.javascript.rhino.Token").getField("BLOCK").getInt(null);
            Object roots = construct(node.getName(), new Class<?>[]{int.class}, block);
            call(roots, "addChildToBack", new Class<?>[]{node}, cleanupExterns);
            call(roots, "addChildToBack", new Class<?>[]{node}, cleanupScript);
            Object normalize = construct("com.google.javascript.jscomp.Normalize", new Class<?>[]{ac, boolean.class}, compiler, false);
            call(normalize, "process", new Class<?>[]{node, node}, cleanupExterns, cleanupScript);
            Class<?> lifecycle = Class.forName("com.google.javascript.jscomp.AbstractCompiler$LifeCycleStage");
            call(compiler, "setLifeCycleStage", new Class<?>[]{lifecycle}, Enum.valueOf((Class)lifecycle, "NORMALIZED"));
            closureNode = cleanupScript;
        }

        void chart(double a) throws ReflectiveOperationException {
            if (chartDataset != null) return;
            Class<?> dataset = Class.forName("org.jfree.data.category.CategoryDataset");
            Class<?> axis = Class.forName("org.jfree.chart.axis.CategoryAxis");
            Class<?> valueAxis = Class.forName("org.jfree.chart.axis.ValueAxis");
            Class<?> renderer = Class.forName("org.jfree.chart.renderer.category.CategoryItemRenderer");
            chartDataset = construct("org.jfree.data.category.DefaultCategoryDataset", new Class<?>[]{});
            call(chartDataset, "addValue", new Class<?>[]{double.class, Comparable.class, Comparable.class}, a < 0 ? -2.0 : 2.0, "row-a", "column-a");
            call(chartDataset, "addValue", new Class<?>[]{double.class, Comparable.class, Comparable.class}, 5.0, "row-b", "column-a");
            chartAxis = construct(axis.getName(), new Class<?>[]{String.class}, "Domain");
            Object rangeAxis = construct("org.jfree.chart.axis.NumberAxis", new Class<?>[]{String.class}, "Range");
            chartPlot = construct("org.jfree.chart.plot.CategoryPlot", new Class<?>[]{dataset, axis, valueAxis, renderer},
                chartDataset, chartAxis, rangeAxis, receiver);
            java.awt.Graphics2D graphics = new java.awt.image.BufferedImage(16,16,java.awt.image.BufferedImage.TYPE_INT_RGB).createGraphics();
            try {
                call(receiver, "initialise", new Class<?>[]{java.awt.Graphics2D.class, java.awt.geom.Rectangle2D.class,
                    chartPlot.getClass(), dataset, Class.forName("org.jfree.chart.plot.PlotRenderingInfo")},
                    graphics, new java.awt.geom.Rectangle2D.Double(0,0,16,16), chartPlot, chartDataset, null);
            } finally { graphics.dispose(); }
        }

        Object beanWriter() throws ReflectiveOperationException {
            Object objectMapper = construct("com.fasterxml.jackson.databind.ObjectMapper", new Class<?>[]{});
            Object provider = call(objectMapper, "getSerializerProvider", new Class<?>[]{});
            provider = call(provider, "createInstance", new Class<?>[]{Class.forName("com.fasterxml.jackson.databind.SerializationConfig"),
                Class.forName("com.fasterxml.jackson.databind.ser.SerializerFactory")},
                call(objectMapper, "getSerializationConfig", new Class<?>[]{}), call(objectMapper, "getSerializerFactory", new Class<?>[]{}));
            Object serializer = call(provider, "findValueSerializer", new Class<?>[]{Class.class, Class.forName("com.fasterxml.jackson.databind.BeanProperty")}, FixtureBean.class, null);
            return Array.get(field(serializer, "_props"), 0);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        void jacksonCollection(double a) throws ReflectiveOperationException {
            if (mapper != null) return;
            mapper = construct("com.fasterxml.jackson.databind.ObjectMapper", new Class<?>[]{});
            Class<?> feature = Class.forName("com.fasterxml.jackson.databind.DeserializationFeature");
            call(mapper, "configure", new Class<?>[]{feature, boolean.class}, Enum.valueOf((Class)feature, "ACCEPT_SINGLE_VALUE_AS_ARRAY"), true);
            Object typeFactory = call(mapper, "getTypeFactory", new Class<?>[]{});
            collectionType = call(typeFactory, "constructCollectionType", new Class<?>[]{Class.class, Class.class}, java.util.ArrayList.class, String.class);
            Object factory = call(mapper, "getFactory", new Class<?>[]{});
            String input = method.equals("handleNonArray") ? a < 0 ? "\"alpha\"" : "\"beta\""
                : a < 0 ? "[\"alpha\",\"beta\"]" : "[\"left\",\"right\"]";
            parser = call(factory, "createParser", new Class<?>[]{String.class}, input);
            call(parser, "nextToken", new Class<?>[]{});
            Object blueprint = call(mapper, "getDeserializationContext", new Class<?>[]{});
            context = call(blueprint, "createInstance", new Class<?>[]{Class.forName("com.fasterxml.jackson.databind.DeserializationConfig"),
                Class.forName("com.fasterxml.jackson.core.JsonParser"), Class.forName("com.fasterxml.jackson.databind.InjectableValues")},
                call(mapper, "getDeserializationConfig", new Class<?>[]{}), parser, null);
            collectionDeserializer = call(context, "findRootValueDeserializer", new Class<?>[]{Class.forName("com.fasterxml.jackson.databind.JavaType")}, collectionType);
        }

        void mockito(double a) throws ReflectiveOperationException {
            if (mock != null) return;
            mock = call(Class.forName("org.mockito.Mockito"), "mock", new Class<?>[]{Class.class}, FixtureMock.class);
            call(mock, "accept", new Class<?>[]{String.class}, "alpha");
            call(mock, "accept", new Class<?>[]{String.class}, a < 0 ? "alpha" : "beta");
            Object util = construct("org.mockito.internal.util.MockUtil", new Class<?>[]{});
            Object handler = call(util, "getMockHandler", new Class<?>[]{Object.class}, mock);
            Object container = call(handler, "getInvocationContainer", new Class<?>[]{});
            List<?> invocations = (List<?>)call(container, "getInvocations", new Class<?>[]{});
            baseInvocation = invocations.get(0);
            actualInvocation = invocations.get(1);
        }

        FixtureSession(String targetClass, String method, String policy) {
            this.targetClass = targetClass;
            this.method = method;
            this.pilot = PILOT_FIXTURES.equals(policy);
        }

        Object option(String name, String text) throws ReflectiveOperationException {
            Object option = construct("org.apache.commons.cli.Option",
                    new Class<?>[]{String.class, boolean.class, String.class}, name, true, "fixture");
            call(option, "setType", new Class<?>[]{Object.class}, String.class);
            call(option, "addValue", new Class<?>[]{String.class}, text);
            return option;
        }

        Object archiveEntry(String name, long size) throws ReflectiveOperationException {
            Object entry = construct("org.apache.commons.compress.archivers.cpio.CpioArchiveEntry",
                    new Class<?>[]{String.class}, name);
            call(entry, "setSize", new Class<?>[]{long.class}, size);
            call(entry, "setTime", new Class<?>[]{long.class}, 0L);
            call(entry, "setMode", new Class<?>[]{long.class}, 0100644L);
            return entry;
        }

        Object prepareReceiver(Object value, double a) throws ReflectiveOperationException {
            if (!pilot) return value;
            if (targetClass.equals("org.apache.commons.cli.CommandLine")) {
                call(value, "addOption", new Class<?>[]{Class.forName("org.apache.commons.cli.Option")}, option("x", a < 0 ? "alpha" : "beta"));
                call(value, "addArg", new Class<?>[]{String.class}, "positional");
            } else if (targetClass.equals("com.fasterxml.jackson.core.util.TextBuffer")) {
                char[] content = (a < 0 ? "123" : "45.5").toCharArray();
                call(value, "resetWithCopy", new Class<?>[]{char[].class, int.class, int.class}, content, 0, content.length);
            } else if (targetClass.equals("org.jsoup.nodes.Document")) {
                Object html = call(value, "appendElement", new Class<?>[]{String.class}, "html");
                call(html, "appendElement", new Class<?>[]{String.class}, "head");
                Object body = call(html, "appendElement", new Class<?>[]{String.class}, "body");
                call(body, "text", new Class<?>[]{String.class}, a < 0 ? "alpha" : "beta");
                call(value, "title", new Class<?>[]{String.class}, "Fixture");
            } else if (targetClass.endsWith("CpioArchiveOutputStream")) {
                call(value, "putNextEntry", new Class<?>[]{Class.forName("org.apache.commons.compress.archivers.cpio.CpioArchiveEntry")},
                        archiveEntry("fixture.txt", method.equals("write") ? 1 : 0));
            } else if (targetClass.equals("org.joda.time.Partial")) {
                return call(value, "with", new Class<?>[]{Class.forName("org.joda.time.DateTimeFieldType"), int.class},
                        call(Class.forName("org.joda.time.DateTimeFieldType"), "hourOfDay", new Class<?>[]{}), 10);
            } else if (targetClass.equals("org.jfree.chart.renderer.category.AreaRenderer")) {
                receiver = value;
                chart(a);
            } else if (targetClass.equals("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser")) {
                // Real StAX input; getters start on a named leaf VALUE_STRING.
                for (int i = 0; i < 8; i++) {
                    Object token = call(value, "nextToken", new Class<?>[]{});
                    if (token != null && token.toString().equals("VALUE_STRING")) break;
                }
            }
            return value;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        Object nativeType(String name, boolean object) throws ReflectiveOperationException {
            Class<?> nativeClass = Class.forName("com.google.javascript.rhino.jstype.JSTypeNative");
            Object key = Enum.valueOf((Class)nativeClass, name);
            return call(registry, object ? "getNativeObjectType" : "getNativeType", new Class<?>[]{nativeClass}, key);
        }

        void closure(double a) throws ReflectiveOperationException {
            if (compiler != null) return;
            Class<?> node = Class.forName("com.google.javascript.rhino.Node");
            Class<?> scopeClass = Class.forName("com.google.javascript.jscomp.Scope");
            Class<?> abstractCompiler = Class.forName("com.google.javascript.jscomp.AbstractCompiler");
            compiler = construct("com.google.javascript.jscomp.Compiler", new Class<?>[]{});
            Object options = construct("com.google.javascript.jscomp.CompilerOptions", new Class<?>[]{});
            call(compiler, "initOptions", new Class<?>[]{options.getClass()}, options);
            registry = call(compiler, "getTypeRegistry", new Class<?>[]{});
            String expression = a < 0 ? "x + 1" : "x + 's'";
            if (method.contains("And") || method.contains("ShortCircuit")) expression = "x && true";
            if (method.contains("Or")) expression = "x || false";
            if (method.equals("traverseArrayLiteral")) expression = "[x, 1]";
            if (method.equals("traverseObjectLiteral")) expression = "({p:x})";
            if (method.equals("traverseHook")) expression = "x ? 1 : 2";
            if (method.equals("traverseAssign")) expression = "x = 2";
            if (method.equals("traverseGetElem")) expression = "x['p']";
            if (method.equals("traverseGetProp") || method.contains("Property")) expression = "x.p";
            if (method.equals("traverseName") || method.equals("redeclareSimpleVar")
                    || method.equals("narrowScope") || method.equals("updateScopeForTypeChange")) expression = "x";
            Object script = call(compiler, "parseTestCode", new Class<?>[]{String.class},
                    "function fixture(x) { return " + expression + "; }");
            Object function = call(script, "getFirstChild", new Class<?>[]{});
            Object global = call(scopeClass, "createGlobalScope", new Class<?>[]{node}, script);
            scope = construct(scopeClass.getName(), new Class<?>[]{scopeClass, node}, global, function);
            Object astParameters = call(call(function, "getFirstChild", new Class<?>[]{}), "getNext", new Class<?>[]{});
            Object name = call(astParameters, "getFirstChild", new Class<?>[]{});
            call(scope, "declare", new Class<?>[]{String.class, node,
                    Class.forName("com.google.javascript.rhino.jstype.JSType"),
                    Class.forName("com.google.javascript.jscomp.CompilerInput")}, "x", name, nativeType("UNKNOWN_TYPE", false), null);
            Object body = call(function, "getLastChild", new Class<?>[]{});
            Object returnNode = call(body, "getFirstChild", new Class<?>[]{});
            closureNode = method.equals("traverseReturn") || method.equals("branchedFlowThrough")
                    ? returnNode : call(returnNode, "getFirstChild", new Class<?>[]{});
            if (method.equals("traverseObjectLiteral"))
                call(closureNode, "setJSType", new Class<?>[]{Class.forName("com.google.javascript.rhino.jstype.JSType")}, nativeType("OBJECT_TYPE", true));
            Object analysis = construct("com.google.javascript.jscomp.ControlFlowAnalysis",
                    new Class<?>[]{abstractCompiler, boolean.class, boolean.class}, compiler, false, true);
            call(analysis, "process", new Class<?>[]{node, node}, null, function);
            cfg = call(analysis, "getCfg", new Class<?>[]{});
            Object convention = call(compiler, "getCodingConvention", new Class<?>[]{});
            reverse = construct("com.google.javascript.jscomp.type.SemanticReverseAbstractInterpreter",
                    new Class<?>[]{Class.forName("com.google.javascript.jscomp.CodingConvention"), registry.getClass()}, convention, registry);
            flow = call(Class.forName("com.google.javascript.jscomp.LinkedFlowScope"), "createEntryLattice",
                    new Class<?>[]{scopeClass}, scope);
            call(flow, "inferSlotType", new Class<?>[]{String.class, Class.forName("com.google.javascript.rhino.jstype.JSType")},
                    "x", nativeType(a < 0 ? "NUMBER_TYPE" : "STRING_TYPE", false));
        }

        void dom(double a) throws Exception {
            if (domRoot != null) return;
            javax.xml.parsers.DocumentBuilderFactory factory = pilot
                ? javax.xml.parsers.DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", SqaProbe.class.getClassLoader())
                : javax.xml.parsers.DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            org.w3c.dom.Document document = factory.newDocumentBuilder().newDocument();
            domRoot = document.createElementNS("urn:sqa:root", "r:root");
            document.appendChild(domRoot);
            domRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:r", "urn:sqa:root");
            domRoot.setAttributeNS("http://www.w3.org/XML/1998/namespace", "xml:lang", "en");
            org.w3c.dom.Element element = document.createElementNS("urn:sqa:item", "i:item");
            domChild = element;
            element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:i", "urn:sqa:item");
            element.setAttribute("id", a < 0 ? "left" : "right");
            domChild.appendChild(document.createTextNode(a < 0 ? "alpha" : "beta"));
            org.w3c.dom.Element grandchild = document.createElementNS("urn:sqa:item", "i:item");
            grandchild.appendChild(document.createTextNode("nested"));
            domChild.appendChild(grandchild);
            org.w3c.dom.Element last = document.createElementNS("urn:sqa:item", "i:item");
            last.appendChild(document.createTextNode("nested-last"));
            domChild.appendChild(last);
            if (method.equals("getRelativePositionOfPI")) {
                domRoot.appendChild(document.createProcessingInstruction("fixture", "before"));
                domChild = document.createProcessingInstruction("fixture", a < 0 ? "alpha" : "beta");
            } else if (method.equals("getRelativePositionOfTextNode")) {
                domRoot.appendChild(document.createCDATASection("before"));
                domChild = document.createTextNode(a < 0 ? "alpha" : "beta");
            }
            domRoot.appendChild(domChild);
        }

        void jdom(double a) throws ReflectiveOperationException {
            if (jdomRoot != null) return;
            Class<?> element = Class.forName("org.jdom.Element");
            jdomRoot = construct(element.getName(), new Class<?>[]{String.class}, "root");
            jdomChild = construct(element.getName(), new Class<?>[]{String.class}, "item");
            call(jdomChild, "setText", new Class<?>[]{String.class}, a < 0 ? "alpha" : "beta");
            call(jdomChild, "setAttribute", new Class<?>[]{String.class, String.class}, "id", a < 0 ? "left" : "right");
            Object grandchild = construct(element.getName(), new Class<?>[]{String.class}, "item");
            call(grandchild, "setText", new Class<?>[]{String.class}, "nested");
            call(jdomChild, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, grandchild);
            Object last = construct(element.getName(), new Class<?>[]{String.class}, "item");
            call(last, "setText", new Class<?>[]{String.class}, "nested-last");
            call(jdomChild, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, last);
            if (method.equals("getRelativePositionOfPI")) {
                Object before = construct("org.jdom.ProcessingInstruction", new Class<?>[]{String.class, String.class}, "fixture", "before");
                call(jdomRoot, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, before);
                jdomChild = construct("org.jdom.ProcessingInstruction", new Class<?>[]{String.class, String.class}, "fixture", a < 0 ? "alpha" : "beta");
            } else if (method.equals("getRelativePositionOfTextNode")) {
                Object before = construct("org.jdom.CDATA", new Class<?>[]{String.class}, "before");
                call(jdomRoot, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, before);
                jdomChild = construct("org.jdom.Text", new Class<?>[]{String.class}, a < 0 ? "alpha" : "beta");
            }
            call(jdomRoot, "addContent", new Class<?>[]{Class.forName("org.jdom.Content")}, jdomChild);
        }

        void configurePointer(Object pointer) throws ReflectiveOperationException {
            Class<?> resolverClass = Class.forName("org.apache.commons.jxpath.ri.NamespaceResolver");
            Object resolver = construct(resolverClass.getName(), new Class<?>[]{resolverClass}, new Object[]{null});
            call(resolver, "registerNamespace", new Class<?>[]{String.class, String.class}, "i", "urn:sqa:item");
            call(resolver, "registerNamespace", new Class<?>[]{String.class, String.class}, "r", "urn:sqa:root");
            call(resolver, "setNamespaceContextPointer", new Class<?>[]{Class.forName("org.apache.commons.jxpath.ri.model.NodePointer")}, pointer);
            call(pointer, "setNamespaceResolver", new Class<?>[]{resolverClass}, resolver);
        }

        Object argument(Class<?> type, double a, double b, double c, int depth) {
            try {
                if (depth > 2) throw new FixtureFailure("Fixture recursion limit: " + type.getName(), null);
                String name = type.getName();
                if (pilot) {
                    if (targetClass.equals("com.google.gson.TypeInfoFactory")) {
                        java.lang.reflect.Field value = GenericFixture.class.getField(a < 0 ? "value" : "items");
                        if (type == java.lang.reflect.TypeVariable.class) return GenericFixture.class.getTypeParameters()[0];
                        if (type == java.lang.reflect.Field.class) return value;
                        if (type == Class.class) return GenericFixture.class;
                        if (type == java.lang.reflect.Type.class) {
                            if (method.equals("getTypeInfoForArray")) return a < 0 ? String[].class : Integer[].class;
                            return a < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();
                        }
                    }
                    if (targetClass.equals("com.google.javascript.jscomp.RemoveUnusedVars")) {
                        unusedClosure(a);
                        if (type == boolean.class) return false; // No call-site optimizer prerequisite.
                        if (name.equals("com.google.javascript.jscomp.AbstractCompiler")) return compiler;
                        if (name.equals("com.google.javascript.rhino.Node")) {
                            if (method.equals("process")) return cleanupNodeIndex++ == 0 ? cleanupExterns : cleanupScript;
                            if (method.equals("getFunctionArgList")) {
                                Object child = call(cleanupScript, "getFirstChild", new Class<?>[]{});
                                while (child != null && !(Boolean)call(child, "isFunction", new Class<?>[]{}))
                                    child = call(child, "getNext", new Class<?>[]{});
                                if (child == null) throw new FixtureFailure("Missing parsed function", null);
                                return child;
                            }
                            return cleanupScript;
                        }
                    }
                    if (targetClass.equals("org.jfree.chart.renderer.category.AreaRenderer")) {
                        chart(a);
                        if (name.equals("org.jfree.data.category.CategoryDataset")) return chartDataset;
                        if (name.equals("org.jfree.chart.axis.CategoryAxis")) return chartAxis;
                        if (type == Comparable.class) return a < 0 ? "row-a" : "column-a";
                        if (type == java.awt.geom.Rectangle2D.class) return new java.awt.geom.Rectangle2D.Double(0,0,16,16);
                        if (name.equals("org.jfree.chart.util.RectangleEdge")) return type.getField("BOTTOM").get(null);
                        if (type == int.class) return 0;
                    }
                    if (targetClass.equals("com.fasterxml.jackson.databind.ser.BeanPropertyWriter")) {
                        if (name.equals(targetClass)) return beanWriter();
                        if (name.equals("com.fasterxml.jackson.databind.util.NameTransformer"))
                            return call(type, "simpleTransformer", new Class<?>[]{String.class, String.class}, a < 0 ? "left_" : "right_", "_suffix");
                        if (type == Object.class) return method.equals("get") ? new FixtureBean() : a < 0 ? "fixture-key" : "fixture-value";
                    }
                    if (targetClass.equals("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer")) {
                        jacksonCollection(a);
                        if (name.equals("com.fasterxml.jackson.databind.JavaType")) return collectionType;
                        if (name.equals("com.fasterxml.jackson.core.JsonParser")) return parser;
                        if (name.equals("com.fasterxml.jackson.databind.DeserializationContext")) return context;
                        if (name.equals("com.fasterxml.jackson.databind.deser.ValueInstantiator"))
                            return call(collectionDeserializer, "getValueInstantiator", new Class<?>[]{});
                        if (name.equals("com.fasterxml.jackson.databind.JsonDeserializer"))
                            return Class.forName("com.fasterxml.jackson.databind.deser.std.StringDeserializer").getField("instance").get(null);
                    }
                    if (targetClass.equals("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser")) {
                        String xml = a < 0 ? "<root><item>123</item><other>alpha</other></root>" : "<root><item>45</item><other>beta</other></root>";
                        if (type == int.class && constructing) return 0;
                        if (name.equals("com.fasterxml.jackson.core.io.IOContext"))
                            return construct(name, new Class<?>[]{Class.forName("com.fasterxml.jackson.core.util.BufferRecycler"), Object.class, boolean.class},
                                construct("com.fasterxml.jackson.core.util.BufferRecycler", new Class<?>[]{}), xml, false);
                        if (name.equals("com.fasterxml.jackson.core.ObjectCodec")) return construct("com.fasterxml.jackson.dataformat.xml.XmlMapper", new Class<?>[]{});
                        if (type == javax.xml.stream.XMLStreamReader.class) {
                            javax.xml.stream.XMLStreamReader reader = javax.xml.stream.XMLInputFactory.newInstance().createXMLStreamReader(new java.io.StringReader(xml));
                            while (reader.hasNext() && reader.getEventType() != javax.xml.stream.XMLStreamConstants.START_ELEMENT) reader.next();
                            return reader;
                        }
                    }
                    if (targetClass.equals("org.mockito.internal.invocation.InvocationMatcher")) {
                        mockito(a);
                        if (name.equals("org.mockito.invocation.Invocation")) return constructing ? baseInvocation : actualInvocation;
                    }
                    if (targetClass.startsWith("org.apache.commons.math3.fraction.")) {
                        int number = 1 + bucket(a, 8);
                        if (type == double.class) return (a < 0 ? -1 : 1) * number / 4.0;
                        if (type == int.class) return number;
                        if (type == long.class) return (long)number;
                        if (type == java.math.BigInteger.class) return java.math.BigInteger.valueOf(number);
                        if (name.equals("org.apache.commons.math3.fraction.BigFraction") || name.equals("org.apache.commons.math3.fraction.Fraction"))
                            return construct(name, new Class<?>[]{int.class, int.class}, number, 3);
                    }
                    if (targetClass.equals("org.apache.commons.cli.CommandLine")) {
                        if (type == String.class) return constructing ? "fixture" : a < -0.33 ? "x" : a < 0.33 ? "missing" : "extra";
                        if (type == char.class) return a < 0 ? 'x' : 'z';
                        if (name.equals("org.apache.commons.cli.Option")) return option("extra", a < 0 ? "left" : "right");
                    }
                    if (targetClass.equals("org.jsoup.nodes.Document") && type == String.class)
                        return constructing ? "https://fixture.invalid/" : method.equals("createElement") ? a < 0 ? "span" : "section"
                            : STRINGS[bucket(a, STRINGS.length)];
                    if (targetClass.equals("org.joda.time.Partial")) {
                        if (type == int.class) return bucket(a, 24);
                        if (name.equals("org.joda.time.DateTimeFieldType"))
                            return call(type, "hourOfDay", new Class<?>[]{});
                    }
                    if (name.equals("org.joda.time.DurationFieldType")) return call(type, a < 0 ? "hours" : "days", new Class<?>[]{});
                    if (name.equals("org.joda.time.DurationField")) return call(Class.forName("org.joda.time.field.UnsupportedDurationField"),
                        "getInstance", new Class<?>[]{Class.forName("org.joda.time.DurationFieldType")},
                        call(Class.forName("org.joda.time.DurationFieldType"), "hours", new Class<?>[]{}));
                    if (name.equals("com.fasterxml.jackson.core.util.BufferRecycler")) return construct(name, new Class<?>[]{});
                    if (type == java.io.OutputStream.class && targetClass.endsWith("CpioArchiveOutputStream")) {
                        archiveBytes = new java.io.ByteArrayOutputStream();
                        return archiveBytes;
                    }
                    if (name.equals("org.apache.commons.compress.archivers.ArchiveEntry") || name.equals("org.apache.commons.compress.archivers.cpio.CpioArchiveEntry"))
                        return archiveEntry(a < 0 ? "next-left.txt" : "next-right.txt", 0);
                    if (targetClass.equals("com.fasterxml.jackson.core.io.NumberInput") && type == String.class)
                        return new String[]{"0", "1", "12", "2147483647"}[bucket(a, 4)];
                }
                if (scalar(type)) {
                    if (type == String.class && method.equals("getRelativePositionOfPI")) return a < 0 ? "fixture" : "other";
                    if (type == String.class && (method.equals("namespacePointer") || method.equals("getNamespaceURI")))
                        return a < 0 ? "r" : "i";
                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);
                }
                if (type.isArray()) {
                    Object array = Array.newInstance(type.getComponentType(), pilot && (targetClass.endsWith("NumberUtils") || targetClass.endsWith("TypeInfoFactory")) ? 1 + bucket(c, 4) : bucket(c, 5));
                    for (int i = 0; i < Array.getLength(array); i++)
                        Array.set(array, i, argument(type.getComponentType(), a, b, c, depth + 1));
                    return array;
                }
                if (type == java.io.Reader.class && targetClass.equals("org.apache.commons.csv.ExtendedBufferedReader"))
                    return new java.io.StringReader(STRINGS[bucket(a, STRINGS.length)]);
                if (name.startsWith("com.google.javascript.")) {
                    closure(a);
                    if (name.endsWith(".AbstractCompiler")) return compiler;
                    if (name.endsWith(".ControlFlowGraph")) return cfg;
                    if (name.endsWith(".ReverseAbstractInterpreter")) return reverse;
                    if (name.endsWith(".Scope")) return scope;
                    if (name.endsWith(".Scope$Var")) return call(scope, "getVar", new Class<?>[]{String.class}, "x");
                    if (name.endsWith(".FlowScope")) return flow;
                    if (name.endsWith(".Node")) return closureNode;
                    if (name.endsWith(".JSType")) return nativeType(a < 0 ? "NUMBER_TYPE" : "STRING_TYPE", false);
                    if (name.endsWith(".ObjectType")) return nativeType("OBJECT_TYPE", true);
                }
                if (name.startsWith("org.w3c.dom.")) {
                    dom(a);
                    if (type.isInstance(domChild)) return domChild;
                    if (type.isInstance(domChild.getOwnerDocument())) return domChild.getOwnerDocument();
                }
                if (type == java.util.Locale.class) return java.util.Locale.ROOT;
                if (name.equals("org.apache.commons.jxpath.ri.QName"))
                    return construct(name, new Class<?>[]{String.class}, method.equals("attributeIterator") ? "id" : "item");
                if (name.equals("org.apache.commons.jxpath.ri.compiler.NodeTest"))
                    return construct("org.apache.commons.jxpath.ri.compiler.NodeNameTest",
                            new Class<?>[]{Class.forName("org.apache.commons.jxpath.ri.QName"), String.class},
                            targetClass.contains(".jdom.")
                                ? construct("org.apache.commons.jxpath.ri.QName", new Class<?>[]{String.class}, "item")
                                : construct("org.apache.commons.jxpath.ri.QName", new Class<?>[]{String.class, String.class}, "i", "item"),
                            targetClass.contains(".jdom.") ? null : "urn:sqa:item");
                if (name.equals("org.apache.commons.jxpath.ri.model.NodePointer")) {
                    if (targetClass.contains(".jdom.")) {
                        jdom(a);
                        if (!constructing && (method.equals("childIterator") || method.equals("compareChildNodePointers"))) {
                            List<?> children = (List<?>)call(jdomChild, "getContent", new Class<?>[]{});
                            Object anchor = children.get(a < 0 ? 0 : children.size() - 1);
                            Object pointer = construct(targetClass, new Class<?>[]{type, Object.class}, receiver, anchor);
                            configurePointer(pointer);
                            return pointer;
                        }
                        Object pointer = construct("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer",
                                new Class<?>[]{Object.class, java.util.Locale.class}, jdomRoot, java.util.Locale.ROOT);
                        configurePointer(pointer);
                        return pointer;
                    }
                    dom(a);
                    if (!constructing && (method.equals("childIterator") || method.equals("compareChildNodePointers"))) {
                        org.w3c.dom.Node anchor = a < 0 ? domChild.getFirstChild() : domChild.getLastChild();
                        Object pointer = construct(targetClass, new Class<?>[]{type, org.w3c.dom.Node.class}, receiver, anchor);
                        configurePointer(pointer);
                        return pointer;
                    }
                    Object pointer = construct("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer",
                            new Class<?>[]{org.w3c.dom.Node.class, java.util.Locale.class}, domRoot, java.util.Locale.ROOT);
                    configurePointer(pointer);
                    return pointer;
                }
                if (type == Object.class && targetClass.contains(".jdom.")
                        && (constructing || !method.equals("setValue"))) { jdom(a); return jdomChild; }
                if (type == java.util.Iterator.class) return new ArrayList<Object>().iterator();
                if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)
                    return new ArrayList<Object>();
                if (type == java.util.Set.class) return new java.util.HashSet<Object>();
                if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();
                if (type == Object.class || type == Number.class || type == java.util.Date.class)
                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);
                throw new FixtureFailure("No explicit recipe: " + name, null);
            } catch (FixtureFailure failure) { throw failure; }
            catch (Exception failure) { throw new FixtureFailure("Fixture recipe failed: " + type.getName()
                    + ":" + failure.getClass().getName() + ":" + failure.getMessage(), failure); }
        }

        String nodeSnapshot(org.w3c.dom.Node node, int depth) {
            if (depth > 8) return "depth-limit";
            StringBuilder out = new StringBuilder("node:").append(node.getNodeType()).append(':')
                    .append(quote(node.getNodeName())).append(':').append(quote(String.valueOf(node.getNodeValue())));
            org.w3c.dom.NamedNodeMap attributes = node.getAttributes();
            List<String> attrs = new ArrayList<String>();
            if (attributes != null) for (int i = 0; i < attributes.getLength(); i++)
                attrs.add(nodeSnapshot(attributes.item(i), depth + 1));
            java.util.Collections.sort(attrs);
            out.append(attrs.toString()).append('[');
            org.w3c.dom.NodeList children = node.getChildNodes();
            for (int i = 0; i < Math.min(256, children.getLength()); i++) out.append(nodeSnapshot(children.item(i), depth + 1));
            return out.append("]children:").append(children.getLength()).toString();
        }

        Object field(Object value, String name) throws ReflectiveOperationException {
            for (Class<?> type = value.getClass(); type != null; type = type.getSuperclass()) {
                try {
                    java.lang.reflect.Field field = type.getDeclaredField(name);
                    field.setAccessible(true);
                    return field.get(value);
                } catch (NoSuchFieldException missing) { }
            }
            throw new NoSuchFieldException(name);
        }

        String projection(Object result, int depth) throws ReflectiveOperationException {
            if (depth > 8) throw new FixtureFailure("Oracle projection depth exceeded", null);
            if (result == null) return "null";
            String name = result.getClass().getName();
            if (pilot && result instanceof java.lang.reflect.Type) return "type:" + ((java.lang.reflect.Type)result).getTypeName();
            if (pilot && result instanceof Method) return "method:" + ((Method)result).toGenericString();
            if (pilot && name.startsWith("com.google.gson.TypeInfo"))
                return "type-info:" + projection(call(result, "getActualType", new Class<?>[]{}), depth + 1);
            if (pilot && name.equals("com.google.javascript.rhino.Node")) return "ast:" + call(result, "toStringTree", new Class<?>[]{});
            if (pilot && name.equals("org.apache.commons.jxpath.ri.NamespaceResolver"))
                return "namespaces:r=" + call(result, "getNamespaceURI", new Class<?>[]{String.class}, "r")
                    + ":i=" + call(result, "getNamespaceURI", new Class<?>[]{String.class}, "i");
            if (pilot && name.equals("org.jfree.data.Range"))
                return "range:" + call(result, "getLowerBound", new Class<?>[]{}) + ':' + call(result, "getUpperBound", new Class<?>[]{});
            if (pilot && name.equals("org.jfree.chart.LegendItem")) return "legend:" + call(result, "getLabel", new Class<?>[]{});
            if (pilot && name.equals("org.jfree.chart.LegendItemCollection")) {
                StringBuilder out = new StringBuilder("legends[");
                int count = ((Number)call(result, "getItemCount", new Class<?>[]{})).intValue();
                if (count > 256) throw new FixtureFailure("Legend limit exceeded", null);
                for (int i = 0; i < count; i++) out.append(projection(call(result, "get", new Class<?>[]{int.class}, i), depth + 1)).append(';');
                return out.append(']').toString();
            }
            if (pilot && name.startsWith("com.fasterxml.jackson.databind.type.")) return "java-type:" + call(result, "toCanonical", new Class<?>[]{});
            if (pilot && name.equals("com.fasterxml.jackson.core.io.SerializedString")) return "serialized-name:" + call(result, "getValue", new Class<?>[]{});
            if (pilot && name.equals("com.fasterxml.jackson.databind.ser.BeanPropertyWriter"))
                return "property:" + call(result, "getName", new Class<?>[]{}) + ':' + projection(call(result, "getType", new Class<?>[]{}), depth + 1);
            if (pilot && targetClass.equals("org.mockito.internal.invocation.InvocationMatcher")
                    && Class.forName("org.mockito.invocation.Invocation").isInstance(result))
                return "invocation:" + projection(call(result, "getMethod", new Class<?>[]{}), depth + 1)
                    + ':' + projection(call(result, "getArguments", new Class<?>[]{}), depth + 1)
                    + ":verified=" + call(result, "isVerified", new Class<?>[]{});
            if (pilot && result.getClass().isArray()) {
                int length = Array.getLength(result);
                if (length > 100000) throw new FixtureFailure("Oracle array limit exceeded", null);
                StringBuilder out = new StringBuilder("array[");
                for (int i = 0; i < length; i++) out.append(projection(Array.get(result, i), depth + 1)).append(';');
                return out.append(']').toString();
            }
            if (pilot && (name.equals("org.jsoup.nodes.Document") || name.equals("org.jsoup.nodes.Element")))
                return "html:" + call(result, "outerHtml", new Class<?>[]{});
            if (pilot && name.equals("org.apache.commons.cli.Option"))
                return "option:" + call(result, "getOpt", new Class<?>[]{}) + ':' + projection(call(result, "getValues", new Class<?>[]{}), depth + 1);
            if (pilot && result instanceof java.util.Iterator) {
                StringBuilder out = new StringBuilder("iterator[");
                java.util.Iterator<?> iterator = (java.util.Iterator<?>)result;
                int count = 0;
                while (iterator.hasNext()) {
                    if (++count > 256) throw new FixtureFailure("Oracle iterator limit exceeded", null);
                    out.append(projection(iterator.next(), depth + 1)).append(';');
                }
                return out.append(']').toString();
            }
            if (pilot && (name.equals("org.apache.commons.math3.fraction.BigFraction") || name.equals("org.apache.commons.math3.fraction.Fraction")))
                return "fraction:" + call(result, "getNumerator", new Class<?>[]{}) + '/' + call(result, "getDenominator", new Class<?>[]{});
            if (pilot && name.startsWith("org.joda.time.")) {
                if (name.equals("org.joda.time.Partial")) return "partial:" + call(result, "toStringList", new Class<?>[]{});
                if (Class.forName("org.joda.time.DurationFieldType").isInstance(result)) return "duration-type:" + call(result, "getName", new Class<?>[]{});
                if (Class.forName("org.joda.time.DurationField").isInstance(result))
                    return "duration:" + call(result, "getName", new Class<?>[]{}) + ':' + call(result, "isSupported", new Class<?>[]{});
            }
            if (result instanceof org.w3c.dom.Node) return nodeSnapshot((org.w3c.dom.Node)result, 0);
            if (name.equals("org.jdom.Element") || name.equals("org.jdom.ProcessingInstruction")
                    || name.equals("org.jdom.Text") || name.equals("org.jdom.CDATA")) {
                Object writer = construct("org.jdom.output.XMLOutputter", new Class<?>[]{});
                return "xml:" + call(writer, "outputString", new Class<?>[]{result.getClass()}, result);
            }
            if (name.equals("org.apache.commons.jxpath.ri.QName")) return "qname:" + result.toString();
            if (name.startsWith("com.google.javascript.rhino.jstype.")) return "js-type:" + result.toString();
            if (name.equals("com.google.javascript.jscomp.LinkedFlowScope")) {
                Object slot = call(result, "getSlot", new Class<?>[]{String.class}, "x");
                return "flow:x=" + (slot == null ? "absent" : projection(call(slot, "getType", new Class<?>[]{}), depth + 1));
            }
            if (name.endsWith("TypeInference$BooleanOutcomePair"))
                return "boolean-pair:" + field(result, "toBooleanOutcomes") + ':' + field(result, "booleanValues")
                    + ":left=" + projection(field(result, "leftScope"), depth + 1)
                    + ":right=" + projection(field(result, "rightScope"), depth + 1);
            if (result instanceof List) {
                StringBuilder out = new StringBuilder("list[");
                if (((List<?>)result).size() > 256) throw new FixtureFailure("Oracle collection limit exceeded", null);
                for (Object item : (List<?>)result) out.append(projection(item, depth + 1)).append(';');
                return out.append(']').toString();
            }
            if (result instanceof java.util.Map) {
                java.util.Map<?,?> map = (java.util.Map<?,?>)result;
                if (map.size() > 256) throw new FixtureFailure("Oracle map limit exceeded", null);
                List<String> entries = new ArrayList<String>();
                for (java.util.Map.Entry<?,?> entry : map.entrySet())
                    entries.add(projection(entry.getKey(), depth + 1) + "=" + projection(entry.getValue(), depth + 1));
                java.util.Collections.sort(entries);
                return "map:" + entries.toString();
            }
            if (name.startsWith("org.apache.commons.jxpath.ri.model.")) {
                Class<?> pointer = Class.forName("org.apache.commons.jxpath.ri.model.NodePointer");
                if (pointer.isInstance(result))
                    return "pointer:" + projection(call(result, "getImmediateNode", new Class<?>[]{}), depth + 1);
                if (Class.forName("org.apache.commons.jxpath.ri.model.NodeIterator").isInstance(result)) {
                    StringBuilder out = new StringBuilder("iterator[");
                    for (int i = 1; i <= 9; i++) {
                        boolean present = (Boolean)call(result, "setPosition", new Class<?>[]{int.class}, i);
                        if (!present) return out.append(']').toString();
                        if (i == 9) throw new FixtureFailure("Oracle iterator limit exceeded", null);
                        out.append(projection(call(result, "getNodePointer", new Class<?>[]{}), depth + 1)).append(';');
                    }
                }
            }
            String simple = value(result);
            if (simple.startsWith("object-type:")) throw new FixtureFailure("No structural oracle: " + name, null);
            return simple;
        }

        String state() throws ReflectiveOperationException {
            if (pilot && targetClass.equals("com.google.javascript.jscomp.RemoveUnusedVars"))
                return "cleanup:" + call(cleanupScript, "toStringTree", new Class<?>[]{});
            if (pilot && targetClass.equals("org.jfree.chart.renderer.category.AreaRenderer"))
                return "chart:rows=" + call(chartDataset, "getRowCount", new Class<?>[]{}) + ":columns=" + call(chartDataset, "getColumnCount", new Class<?>[]{});
            if (pilot && targetClass.equals("com.fasterxml.jackson.databind.ser.BeanPropertyWriter"))
                return projection(receiver, 0) + ":setting=" + projection(call(receiver, "getInternalSetting", new Class<?>[]{Object.class}, "fixture-key"), 0);
            if (pilot && targetClass.equals("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer"))
                return "json-token:" + call(parser, "getCurrentToken", new Class<?>[]{});
            if (pilot && targetClass.equals("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser"))
                return "xml:closed=" + call(receiver, "isClosed", new Class<?>[]{}) + ":token=" + call(receiver, "getCurrentToken", new Class<?>[]{})
                    + ":text=" + projection(field(receiver, "_currText"), 0);
            if (pilot && targetClass.equals("org.mockito.internal.invocation.InvocationMatcher"))
                return projection(baseInvocation, 0) + ":candidate=" + projection(actualInvocation, 0);
            if (pilot && targetClass.equals("org.apache.commons.cli.CommandLine"))
                return "cli:" + projection(call(receiver, "getOptions", new Class<?>[]{}), 0) + ':' + projection(call(receiver, "getArgs", new Class<?>[]{}), 0);
            if (pilot && targetClass.equals("com.fasterxml.jackson.core.util.TextBuffer"))
                return "text:" + call(receiver, "contentsAsString", new Class<?>[]{}) + ":size=" + call(receiver, "size", new Class<?>[]{});
            if (pilot && targetClass.equals("org.jsoup.nodes.Document") && receiver != null) return projection(receiver, 0);
            if (pilot && targetClass.endsWith("CpioArchiveOutputStream")) return "archive:" + value(archiveBytes.toByteArray());
            if (pilot && targetClass.startsWith("org.apache.commons.math3.fraction.")) return projection(receiver, 0);
            if (pilot && targetClass.equals("org.joda.time.Partial")) return projection(receiver, 0);
            if (pilot && targetClass.equals("org.joda.time.field.UnsupportedDurationField") && receiver != null) return projection(receiver, 0);
            if (targetClass.equals("org.apache.commons.collections.map.Flat3Map")) return projection(receiver, 0);
            if (targetClass.equals("org.apache.commons.csv.ExtendedBufferedReader"))
                return "reader:line=" + call(receiver, "getLineNumber", new Class<?>[]{})
                    + ":last=" + call(receiver, "readAgain", new Class<?>[]{});
            if (compiler != null) {
                Object jsType = call(closureNode, "getJSType", new Class<?>[]{});
                return "ast:" + call(closureNode, "toStringTree", new Class<?>[]{})
                    + ":ast-type=" + projection(jsType, 0) + ':' + projection(flow, 0);
            }
            if (domRoot != null) return nodeSnapshot(domRoot, 0) + ":child=" + nodeSnapshot(domChild, 0)
                    + ":attached=" + (domChild.getParentNode() != null);
            if (jdomRoot != null) return projection(jdomRoot, 0) + ":child=" + projection(jdomChild, 0)
                    + ":attached=" + (call(jdomChild, "getParent", new Class<?>[]{}) != null);
            return "stateless-scalars";
        }
    }

    private static String quote(String value) {
        StringBuilder out = new StringBuilder("\"");
        for (char c : value.toCharArray()) {
            if (c == '"' || c == '\\') out.append('\\').append(c);
            else if (c < 32) out.append(String.format("\\u%04x", (int)c));
            else out.append(c);
        }
        return out.append('"').toString();
    }

    private static String typeNames(Class<?>[] types) {
        List<String> names = new ArrayList<String>();
        for (Class<?> type : types) names.add(type.getName());
        return String.join(",", names);
    }

    private static boolean scalar(Class<?> type) {
        return type.isPrimitive() || type == String.class || type == Boolean.class
            || type == Character.class || type == Byte.class || type == Short.class
            || type == Integer.class || type == Long.class || type == Float.class
            || type == Double.class || type.isEnum();
    }

    private static boolean supported(Class<?> type) {
        return scalar(type) || (type.isArray() && scalar(type.getComponentType()));
    }

    private static boolean supportedParameters(Class<?>[] types) {
        if (types.length > 6) return false;
        for (Class<?> type : types) if (type == void.class) return false;
        return true;
    }

    private static Class<?> type(String name) throws ClassNotFoundException {
        if (name.equals("boolean")) return boolean.class;
        if (name.equals("byte")) return byte.class;
        if (name.equals("short")) return short.class;
        if (name.equals("int")) return int.class;
        if (name.equals("long")) return long.class;
        if (name.equals("float")) return float.class;
        if (name.equals("double")) return double.class;
        if (name.equals("char")) return char.class;
        return Class.forName(name);
    }

    private static Class<?>[] types(String names) throws ClassNotFoundException {
        if (names.length() == 0) return new Class<?>[0];
        String[] split = names.split(",", -1);
        Class<?>[] result = new Class<?>[split.length];
        for (int i = 0; i < split.length; i++) result[i] = type(split[i]);
        return result;
    }

    private static int bucket(double coordinate, int size) {
        double unit = Math.max(0, Math.min(1, (coordinate + 1) / 2));
        return Math.min(size - 1, (int)(unit * size));
    }

    private static Object argument(Class<?> type, double a, double b, double c) {
        return argument(type, a, b, c, 0);
    }

    private static Object argument(Class<?> type, double a, double b, double c, int depth) {
        FixtureSession session = FIXTURES.get();
        return session == null ? legacyArgument(type, a, b, c, depth) : session.argument(type, a, b, c, depth);
    }

    private static Object legacyArgument(Class<?> type, double a, double b, double c, int depth) {
        if (depth > 2) return null;
        if (type.isArray()) {
            int length = bucket(c, 5);
            Object array = Array.newInstance(type.getComponentType(), length);
            for (int i = 0; i < length; i++) {
                Array.set(array, i, argument(type.getComponentType(),
                    Math.max(-1, Math.min(1, a + i * 0.17)), b, c, depth + 1));
            }
            return array;
        }
        if (!type.isPrimitive() && a < -0.96) return null;
        if (type == String.class) {
            int selection = bucket(a, STRINGS.length + 4);
            if (selection < STRINGS.length) return STRINGS[selection];
            int length = bucket(c, 33);
            char character = "0123456789abcdefXYZ +-_.".charAt(bucket(b, 23));
            char[] value = new char[length];
            Arrays.fill(value, character);
            return new String(value);
        }
        if (type == boolean.class || type == Boolean.class) return a >= 0;
        if (type == char.class || type == Character.class) return (char)bucket(a, 128);
        if (type.isEnum()) {
            Object[] values = type.getEnumConstants();
            return values.length == 0 ? null : values[bucket(a, values.length)];
        }
        long integer = b < 0 ? NUMBERS[bucket(a, NUMBERS.length)] : Math.round(a * 10000);
        if (type == byte.class || type == Byte.class) return (byte)integer;
        if (type == short.class || type == Short.class) return (short)integer;
        if (type == int.class || type == Integer.class) return (int)integer;
        if (type == long.class || type == Long.class) return integer;
        double real = b < 0 ? integer : a * 1000;
        if (type == float.class || type == Float.class) return (float)real;
        if (type == double.class || type == Double.class) return real;
        if (type == Number.class) return Double.valueOf(real);
        if (type == Object.class) return b < 0 ? STRINGS[bucket(a, STRINGS.length)] : Long.valueOf(integer);
        if (type == java.util.Date.class) return new java.util.Date(integer);
        if (type == java.util.List.class || type == java.util.Collection.class || type == Iterable.class)
            return new java.util.ArrayList<Object>();
        if (type == java.util.Set.class) return new java.util.HashSet<Object>();
        if (type == java.util.Map.class) return new java.util.HashMap<Object,Object>();
        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers()) && !type.getName().startsWith("java.")) {
            Constructor<?>[] constructors = type.getDeclaredConstructors();
            Arrays.sort(constructors, new Comparator<Constructor<?>>() {
                public int compare(Constructor<?> left, Constructor<?> right) {
                    int count = left.getParameterCount() - right.getParameterCount();
                    return count != 0 ? count : left.toString().compareTo(right.toString());
                }
            });
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() > 3) continue;
                try {
                    constructor.setAccessible(true);
                    Class<?>[] parameters = constructor.getParameterTypes();
                    Object[] values = new Object[parameters.length];
                    for (int i = 0; i < values.length; i++) values[i] = argument(parameters[i], a, b, c, depth + 1);
                    return constructor.newInstance(values);
                } catch (ReflectiveOperationException error) {
                    // Failed fixture construction yields an explicit null boundary input.
                } catch (RuntimeException error) {
                    // Encapsulated/unconstructible fixture yields the same null boundary.
                }
            }
        }
        return null;
    }

    private static Object[] arguments(Class<?>[] types, double[] vector, int offset) {
        Object[] values = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            int start = offset + 3 * i;
            values[i] = argument(types[i], vector[start % vector.length],
                vector[(start + 1) % vector.length], vector[(start + 2) % vector.length]);
        }
        FixtureSession session = FIXTURES.get();
        if (session != null && session.pilot && !session.constructing) {
            if (session.targetClass.equals("com.google.gson.TypeInfoFactory")) {
                java.lang.reflect.Type parent = vector[0] < 0 ? StringBinding.class.getGenericSuperclass() : IntegerBinding.class.getGenericSuperclass();
                try {
                    if (session.method.equals("getActualType")) {
                        values[0] = GenericFixture.class.getField("items").getGenericType();
                        values[1] = parent;
                        values[2] = GenericFixture.class;
                    } else if (session.method.equals("extractRealTypes")) {
                        values[0] = new java.lang.reflect.Type[]{GenericFixture.class.getField("value").getGenericType()};
                        values[1] = parent;
                        values[2] = GenericFixture.class;
                    }
                } catch (NoSuchFieldException failure) { throw new FixtureFailure("Generic schema field missing", failure); }
            }
            if (session.targetClass.equals("org.jfree.chart.renderer.category.AreaRenderer") && session.method.equals("getItemMiddle")) {
                values[0] = "row-a";
                values[1] = "column-a";
            }
        }
        return values;
    }

    private static String value(Object value) {
        if (value == null) return "null";
        Class<?> type = value.getClass();
        if (type.isArray()) {
            StringBuilder out = new StringBuilder(type.getName()).append('[');
            int length = Array.getLength(value);
            if (length > 100000) throw new IllegalStateException("SQA_HARNESS oversized outcome");
            for (int i = 0; i < length; i++) out.append(value(Array.get(value, i))).append(';');
            return out.append(']').toString();
        }
        if (value instanceof Class) return "class:" + ((Class<?>)value).getName();
        if (!scalar(type) && !(value instanceof Number)) return "object-type:" + type.getName();
        String text = value instanceof Enum ? ((Enum<?>) value).name() : String.valueOf(value);
        return type.getName() + ":" + Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    private static String snapshot(String observed) {
        // JVM string constants are limited to 65,535 encoded bytes. Long exact
        // observations use a deterministic digest rather than enormous literals.
        if (observed.length() <= 16000) return observed;
        byte[] bytes = observed.getBytes(StandardCharsets.UTF_8);
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(bytes);
            StringBuilder hex = new StringBuilder();
            for (byte item : digest) hex.append(String.format("%02x", item & 255));
            return "sha256:" + hex + ":bytes:" + bytes.length;
        } catch (NoSuchAlgorithmException error) {
            throw new IllegalStateException("SQA_HARNESS SHA-256 unavailable", error);
        }
    }

    public static String observe(String className, String constructorTypes, String methodName,
                                 String methodTypes, double[] vector) {
        INVOKED.set(false);
        if (vector.length == 0) throw new IllegalArgumentException("SQA_HARNESS empty vector");
        try {
            Class<?> target = Class.forName(className);
            Class<?>[] ctorTypes = types(constructorTypes);
            Class<?>[] parameterTypes = types(methodTypes);
            Object receiver = null;
            Method method = null;
            if (!methodName.equals("<init>")) {
                Class<?> declaring = target;
                while (declaring != null) {
                    try { method = declaring.getDeclaredMethod(methodName, parameterTypes); break; }
                    catch (NoSuchMethodException missing) { declaring = declaring.getSuperclass(); }
                }
                if (method == null) throw new NoSuchMethodException(methodName);
                method.setAccessible(true);
            }
            if (method == null || !Modifier.isStatic(method.getModifiers())) {
                Constructor<?> ctor = target.getDeclaredConstructor(ctorTypes);
                ctor.setAccessible(true);
                FixtureSession session = FIXTURES.get();
                if (session != null) session.constructing = true;
                try {
                    Object[] values = arguments(ctorTypes, vector, 0);
                    if (method == null) INVOKED.set(true);
                    receiver = ctor.newInstance(values);
                    if (session != null) receiver = session.prepareReceiver(receiver, vector[0]);
                    if (session != null) session.receiver = receiver;
                    if (session != null && className.equals("org.apache.commons.collections.map.Flat3Map")) {
                        call(receiver, "put", new Class<?>[]{Object.class, Object.class}, "fixture-a", "value-a");
                        call(receiver, "put", new Class<?>[]{Object.class, Object.class}, "fixture-b", "value-b");
                    }
                    if (session != null && className.startsWith("org.apache.commons.jxpath.ri.model.")) session.configurePointer(receiver);
                } catch (InvocationTargetException error) {
                    if (session != null && method != null)
                        throw new FixtureFailure("Receiver constructor failed before method invocation", error.getCause());
                    throw error;
                } finally { if (session != null) session.constructing = false; }
            }
            if (method == null) {
                if (FIXTURES.get() == null) return "constructed:" + target.getName();
                try { return snapshot("constructed:" + target.getName() + ":state=" + FIXTURES.get().state()); }
                catch (ReflectiveOperationException failure) { throw new FixtureFailure("Constructor state oracle failed", failure); }
            }
            Object[] values = arguments(parameterTypes, vector, ctorTypes.length * 3);
            INVOKED.set(true);
            Object result = method.invoke(receiver, values);
            if (FIXTURES.get() != null) {
                FixtureSession session = FIXTURES.get();
                try {
                    return snapshot((method.getReturnType() == void.class ? "void" : "value:" + session.projection(result, 0))
                            + "|state=" + session.state());
                } catch (ReflectiveOperationException failure) { throw new FixtureFailure("Structural oracle failed", failure); }
            }
            return method.getReturnType() == void.class ? "void" : snapshot("value:" + value(result));
        } catch (InvocationTargetException error) {
            Throwable cause = error.getCause();
            if (cause instanceof VirtualMachineError || cause instanceof LinkageError || cause instanceof ThreadDeath)
                throw new IllegalStateException("SQA_HARNESS JVM failure", cause);
            return "exception:" + cause.getClass().getName();
        } catch (ReflectiveOperationException error) {
            throw new IllegalStateException("SQA_HARNESS reflection failure", error);
        } catch (LinkageError error) {
            throw new IllegalStateException("SQA_HARNESS linkage failure", error);
        }
    }

    public static String observeWithPolicy(String className, String constructorTypes, String methodName,
            String methodTypes, double[] vector, String policy) {
        if (!EXPLICIT_FIXTURES.equals(policy) && !SCALAR_FIXTURES.equals(policy) && !PILOT_FIXTURES.equals(policy))
            throw new IllegalArgumentException("Unknown explicit fixture policy");
        FIXTURES.set(new FixtureSession(className, methodName, policy));
        try { return observe(className, constructorTypes, methodName, methodTypes, vector); }
        finally { FIXTURES.remove(); }
    }

    public static boolean targetInvoked() { return Boolean.TRUE.equals(INVOKED.get()); }

    private static String descriptor(String className, String ctor, String method, String params, int count) {
        return "{\"class\":" + quote(className) + ",\"constructor_types\":" + quote(ctor)
            + ",\"method\":" + quote(method) + ",\"parameter_types\":" + quote(params)
            + ",\"dimensions\":" + Math.max(3, count * 3) + "}";
    }

    private static void discover(String[] classes, List<String> fixtureClasses) {
        List<String> targets = new ArrayList<String>();
        List<String> errors = new ArrayList<String>();
        for (String className : classes) {
            try {
                Class<?> target = Class.forName(className, false, SqaProbe.class.getClassLoader());
                Class<?> receiverType = target;
                if (Modifier.isAbstract(target.getModifiers())) {
                    for (String name : fixtureClasses) {
                        try {
                            Class<?> candidate = Class.forName(name, false, SqaProbe.class.getClassLoader());
                            if (!Modifier.isAbstract(candidate.getModifiers()) && target.isAssignableFrom(candidate)
                                    && candidate.getDeclaredConstructors().length > 0) {
                                receiverType = candidate;
                                break;
                            }
                        } catch (ClassNotFoundException ignored) { } catch (LinkageError ignored) { }
                    }
                }
                List<Constructor<?>> constructors = new ArrayList<Constructor<?>>();
                if (!Modifier.isAbstract(receiverType.getModifiers()) && !receiverType.isEnum()) {
                    Constructor<?>[] all = receiverType.getDeclaredConstructors();
                    Arrays.sort(all, new Comparator<Constructor<?>>() {
                        public int compare(Constructor<?> a, Constructor<?> b) { return a.toString().compareTo(b.toString()); }
                    });
                    for (Constructor<?> ctor : all) {
                        if (supportedParameters(ctor.getParameterTypes())) constructors.add(ctor);
                    }
                    // Select a constructor before generating inputs; prefer the simplest fixture.
                    java.util.Collections.sort(constructors, new Comparator<Constructor<?>>() {
                        public int compare(Constructor<?> a, Constructor<?> b) { return a.getParameterCount() - b.getParameterCount(); }
                    });
                }
                Method[] methods = target.getDeclaredMethods();
                Arrays.sort(methods, new Comparator<Method>() {
                    public int compare(Method a, Method b) { return a.toString().compareTo(b.toString()); }
                });
                for (Method method : methods) {
                    if (method.isSynthetic() || method.getName().equals("main")
                        || method.isBridge() || !supportedParameters(method.getParameterTypes())
                        ) continue;
                    if (Modifier.isStatic(method.getModifiers())) {
                        targets.add(descriptor(className, "", method.getName(),
                            typeNames(method.getParameterTypes()), method.getParameterCount()));
                    } else if (!constructors.isEmpty()) {
                        Constructor<?> ctor = constructors.get(0);
                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), method.getName(),
                            typeNames(method.getParameterTypes()), ctor.getParameterCount() + method.getParameterCount()));
                    }
                }
                for (Constructor<?> ctor : constructors) {
                    if (ctor.getParameterCount() > 0)
                        targets.add(descriptor(receiverType.getName(), typeNames(ctor.getParameterTypes()), "<init>", "", ctor.getParameterCount()));
                }
            } catch (Throwable error) {
                if (error instanceof VirtualMachineError || error instanceof ThreadDeath) throw (Error)error;
                errors.add(quote(className + ":" + error.getClass().getName()));
            }
        }
        System.out.println("{\"targets\":[" + String.join(",", targets) + "],\"errors\":[" + String.join(",", errors) + "]}");
    }

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("discover")) {
            int start = 1;
            List<String> fixtures = new ArrayList<String>();
            if (args.length > 2 && args[1].equals("--fixtures")) {
                fixtures = Files.readAllLines(Paths.get(args[2]), StandardCharsets.UTF_8);
                start = 3;
            }
            discover(Arrays.copyOfRange(args, start, args.length), fixtures);
            return;
        }
        if ((args.length != 6 && args.length != 7) || !args[0].equals("observe"))
            throw new IllegalArgumentException("SQA_HARNESS expected discover classes or observe class ctor method types vector");
        String[] pieces = args[5].split(",");
        double[] vector = new double[pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            vector[i] = Double.parseDouble(pieces[i]);
            if (!Double.isFinite(vector[i]))
                throw new IllegalArgumentException("SQA_HARNESS nonfinite vector");
        }
        String outcome;
        try {
            outcome = args.length == 7 ? observeWithPolicy(args[1], args[2], args[3], args[4], vector, args[6])
                : observe(args[1], args[2], args[3], args[4], vector);
        } catch (FixtureFailure failure) {
            System.out.println("SQA_FIXTURE_FAILURE:" + Base64.getEncoder().encodeToString(failure.getMessage().getBytes(StandardCharsets.UTF_8)));
            return;
        }
        System.out.println("SQA_TRACE:{\"target_invoked\":" + Boolean.TRUE.equals(INVOKED.get()) + "}");
        System.out.println("SQA_RESULT:" + Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)));
    }
}
}
