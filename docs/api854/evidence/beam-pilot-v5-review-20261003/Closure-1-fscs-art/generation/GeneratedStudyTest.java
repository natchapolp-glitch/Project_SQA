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
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.61049100893172659, 0.93050221412222234, 0.84795280335358858, -0.065722643606052067, 0.3269412890601211, -0.57095406052406394, -0.55660750094751865, -0.42295513323748768, 0.384845491990635, -0.57524646328333784, 0.94221190270754729, -0.85928903139767154, -0.61342674253781793, -0.82227286033016833, 0.53978387322020516}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated2() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 2.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.17636507857656336, -0.095855047296162565, -0.45173220679099213}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated3() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.58069819025202896, -0.79075443624841357, -0.17016169576919316, 0.36135591272590295, 0.7454734801707914, 0.75361241072534146, -0.97676182831534342, -0.64905527803088314, -0.7431800141723659, 0.19160823147655437, -0.18839500753596594, 0.8448070691515237, 0.23072520426123999, 0.22955587628344509, 0.37400460507040623, -0.59022897796634788, 0.78978837365062105, 0.7223060678295552}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated4() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.56494855881417627, -0.65500193967404052, -0.89348631522108879, -0.27005861700974854, 0.43383485830000312, -0.59850853985974695, 0.73783836724139884, 0.7146808182251756, 0.39337299061464015, -0.03178616316056071, -0.69909894121769867, 0.61885873023676607, -0.65464495104134235, -0.0080872704855685651, 0.51683311001622467, 0.27104287845524344, -0.52815714017449822, 0.75808883370512059}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated5() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.4674391378641074, -0.1927500206377728, 0.852326478110633, -0.76641583433768523, -0.85559359324580142, -0.80689531230884848, -0.038894590411273633, -0.35052557613571156, -0.6027455321141113, -0.98528479423193494, 0.57445842359216792, -0.88392679032320443, 0.26263063065272552, 0.93189660453542089, -0.49573131088154332}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated6() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.79029140335902959, 0.46407865056052855, -0.93994570194276972, 0.966099665948146, 0.48641624629789249, 0.54598939620456277, 0.71706818649720905, 0.64084173238258946, 0.52314451927353045, 0.86173976552067377, -0.86959109861480055, -0.84103882349875714, 0.4106994394996335, -0.76384569184218187, 0.60595520207192277, -0.16943291413599537, 0.50711678115407954, -0.78876006104653551}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated7() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.030832528654422431, 0.032909538211628231, 0.60082139668284174}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated8() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.57703851657211969, -0.56872134998529167, -0.81136689263443462}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated9() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.72047905246084043, -0.53764491618585497, 0.52584572440892585, -0.12399347102610037, -0.50529114913943518, 0.28941187648086797, -0.79582397119967929, 0.90610886262979884, -0.71337115786480632, 0.61322588314572957, -0.12050597134398844, -0.715692238781795, 0.5484581348038764, -0.84087073209154894, 0.64312248324027799}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated10() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.18704442542229582, 0.79455687227677263, -0.19093229420454438, -0.16572935385019094, -0.43191757181141499, 0.28619570014315765, 0.63706365305959833, 0.42922867378839125, -0.96148119216331929, -0.3252198497856047, -0.98611630433993303, -0.42959098303313836, -0.58462337716098167, -0.56295031698426978, -0.71789919770221799, -0.61037649805864613, 0.88479441957785099, -0.89425649223045256}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated11() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.54173292633315606, 0.39251579759709321, -0.86754323563953317, -0.54161929323622982, -0.24890985127719834, 0.52904466726547228, -0.96823638530775424, -0.62766923526819673, 0.72955351145814085, -0.91989587545136242, 0.3984078592352549, -0.68750887997675658, 0.76932570695046709, 0.098363534132564157, 0.59694637000177875, -0.64117716643378508, 0.15499050292840888, 0.83654601271711249}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated12() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 2.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.25601432371719213, 0.55685392210227191, 0.27807596837060689}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated13() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.26275039525103772, 0.076956123747116356, 0.97662911050975842, 0.96429011809984178, 0.7520925190258918, 0.77818186142647305, 0.62776048277099772, 0.74962615444860359, -0.23730780895694781, -0.42319555758804883, 0.77667947334658849, -0.66314560591098903, -0.29090734363217874, -0.74624559158194792, -0.37839850455267166}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated14() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 2.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.53759052683952246, 0.6633201910376203, -0.41949698602501018}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated15() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.46816581705424309, -0.6163205338110942, 0.092837824190546758, 0.46053828896679727, -0.80638770037910024, 0.26664360488731642, -0.18573837629818946, -0.99806786157742944, 0.44309995308826866, -0.21178432715555018, 0.62744444361373253, 0.77752997178654892, -0.75544131573450657, -0.83309455154365231, -0.20182058034927342, 0.5882105712714305, 0.4776759742951926, 0.82110990043547027}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated16() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.90592334930774121, -0.36134229311995858, 0.650341807950372, 0.73286804440319298, 0.2865409032130517, -0.64670397180459682, -0.57981550264203685, -0.70918413106551359, -0.94410704921415989, -0.33718012505661843, 0.38991587447352494, 0.53567618980245379, 0.52880566391416473, -0.69706696336752327, 0.020024811355388916}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated17() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.030300609926357414, -0.6034559116070628, -0.45624018914835607, 0.46734899083137682, -0.39466932524092568, -0.78903584530429782, 0.15404355269464598, -0.9950538930023991, -0.98934341771075185, 0.58037838444874645, -0.30258468095472568, 0.14876828485774185, -0.33834640426967688, -0.48040688018194233, -0.7028453854629888, 0.47265209657376861, -0.95619007134374989, 0.32293794453276226}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated18() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.68403399349314542, 0.27067905524890201, 0.95636569850030551, 0.098989879162221017, -0.9467593791514004, -0.47071076376433396, 0.74479416043316804, 0.32953507131520743, -0.77463972293020111, -0.716241366419474, -0.76611316762354398, 0.38247236663681705, -0.39649056001151251, -0.89109947603698614, -0.011642826515438687}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated19() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 2.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.67705825056464142, 0.63230305043913271, -0.85823540946939048}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated20() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.36646727001960366, -0.55862105205696189, 0.73163500723973307}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated21() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.062075672530611303, 0.3020422018394251, -0.16703519173258696, 0.58657385785658889, 0.54243640113052116, 0.10002418505899802, 0.70723790479527127, -0.87005281697670589, 0.13564887813863069, -0.030087612818670673, -0.99766434230900081, 0.73048044466984918, -0.23444995307184624, 0.69222647824107431, 0.53841539528835125, 0.89650471286339783, -0.4053258870665335, -0.85486086546925311}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated22() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.94261986535340614, 0.72655677408007802, -0.53451210624713297, 0.89855299275018319, -0.72675269634565187, -0.31337351053162488, 0.69304034242370327, -0.92437835119944034, -0.2694676912943883, 0.19416784578149882, 0.30117322905991473, 0.82929018853901826, 0.97711306370425577, 0.48915114230593559, -0.34002759269794058, -0.61685326028210352, 0.95536696491201112, -0.25829700204353401}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated23() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.54354257694409691, -0.38961843181565592, 0.49656546552489722, -0.36004821659432951, -0.72165857809222689, 0.33246960662360703, 0.29853018355141603, 0.089717275329015633, 0.93939577239376826, 0.4544851486177055, 0.81556679358744377, -0.8373973271784283, -0.53945168865691207, 0.06521465553327066, -0.5691774993247336, -0.8283757096238733, 0.36189456486478822, -0.45506207557317024}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated24() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.54214268321377279, -0.1520657477813443, 0.8947921721402925, -0.98504345013088157, 0.016876080460667353, -0.86263758515746658, 0.77569226407668812, -0.41745670966077086, 0.37358191174091404, 0.35259165667237791, -0.80299400366809603, -0.29851544659803464, 0.94127944292436272, 0.096603463314424376, 0.76073981576457905}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated25() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.29796888132953958, 0.88413741970627635, -0.40851444961398942}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated26() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{-0.88760302415946546, -0.84944076924297618, 0.67821557770989616, 0.30080682579426221, -0.19174738182569473, -0.48425147908625088, -0.58581653788317833, 0.73340982275915634, 0.50131460249772708, 0.6686391534175129, -0.60770377394382025, 0.23480681153487759, -0.2733042650005757, -0.0088889417817514804, -0.79256992076107391}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated27() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.79684750320811815, 0.93860053246937203, 0.99578487442064989, 0.74797366381875086, 0.43842338952389892, 0.76107799554522026, 0.61167087953324772, -0.15820577556967286, 0.81030147309868394, 0.11427433605733217, -0.573545002688026, 0.6358112403311067, -0.20232672781659811, 0.47691212802983363, -0.73984349999406596}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated28() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.016403929424657626, -0.97407992973547808, 0.94551479161174479, -0.94859240848819826, -0.45782935862931495, -0.2584026840871827, -0.79962617065872554, -0.59226780256680223, 0.20435652473244814, -0.21938883283160648, -0.030293204770823756, -0.41634102332292411, -0.60887792850904709, -0.0060660944425727781, 0.32881205558456705, 0.90398255289306473, -0.61059360566233001, 0.17947252450022355}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated29() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.69257358309537653, 0.58079461876382599, 0.52992537687696672}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated30() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:ast:PARAM_LIST 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n|state=cleanup:SCRIPT 1 [synthetic: 1] [source_file: [testcode]] [input_id: InputId: [testcode]]\n    VAR 1 [source_file: [testcode]]\n        NAME unused 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n    FUNCTION fixture 1 [source_file: [testcode]]\n        NAME fixture 1 [source_file: [testcode]]\n        PARAM_LIST 1 [source_file: [testcode]]\n            NAME x 1 [source_file: [testcode]]\n        BLOCK 1 [source_file: [testcode]]\n            VAR 1 [source_file: [testcode]]\n                NAME local 1 [source_file: [testcode]]\n                    NUMBER 3.0 1 [source_file: [testcode]]\n            RETURN 1 [source_file: [testcode]]\n                NAME x 1 [source_file: [testcode]]\n    EXPR_RESULT 1 [source_file: [testcode]]\n        CALL 1 [free_call: 1] [source_file: [testcode]]\n            NAME fixture 1 [source_file: [testcode]]\n            NUMBER 1.0 1 [source_file: [testcode]]\n", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.66029918114030361, 0.72178179088601402, -0.48546595766473888}, "beam-explicit-fixtures-v5-proposal"));
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
