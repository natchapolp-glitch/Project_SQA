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
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:number:right=flow:x=js-type:number|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : number\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseShortCircuitingBinOp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope,boolean", new double[]{-0.19275191598468638, 1, -0.29502854980607007, -0.86670299434726183, 0.18153854892674576, 0.09391060736277862, 0.41429512464883717, -0.22083770669199668, 0.89450074379313704, 0.37411400333875733, 1, 0.35544948241648838, 0.16823257361162219, 0.87231085730312385, 0.54488738352560939, -0.45305075975128561, 0.50412928835565052, -0.39292398935287515, 0.75899392485952166, -0.33753704139927643, 0.23283417482554164, -0.46267521452595367, 0.21717172739210969, 0.85730445817292322}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated2() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:number|state=ast:NAME x 1 [source_file: [testcode]] : number\n:ast-type=js-type:number:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseName", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-0.40473810276689071, -0.45671139620883039, 0.43279433032780984, -0.73646665111255394, 0.5035220270807621, 0.6696236056587499, 0.027441509136262351, -0.53909145252019786, -0.12079846485769112, 0.92817365087904291, 0.12832943474678932, 0.77217563711311499, 0.045872834140195586, 0.090821818057883724, 0.1307339591918435, 0.093150983379294144, 0.20070505829727012, 0.53243585678343874, 0.74023053518358728, -0.077508167005142722, -0.56432122761603942}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated3() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:number:right=flow:x=js-type:number|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : number\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseShortCircuitingBinOp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope,boolean", new double[]{-0.84840837512783807, 0.25126079012048497, 0.0093941525821895529, 0.54158053821501517, 0.11035764021659317, 1, -0.40707862171624226, 0.15945578831224816, -0.25630375578480008, -0.28437430425406618, -0.99571928534083087, 0.38693633886282797, -0.99078136129558703, 0.23950509416686216, 0.29068450657000516, 0.25956817639741148, -0.94741527720343555, 0.50915387608824758, -0.4081749462454165, 0.25276695804981925, -0.21025390616344158, 1, -0.3317815852655342, -0.11732062694621649}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated4() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:ADD 1 [source_file: [testcode]] : ?\n    NAME x 1 [source_file: [testcode]] : string\n    STRING s 1 [source_file: [testcode]]\n:ast-type=js-type:?:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverse", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.16675745809446776, -0.20054893332519608, 1, 1, 0.76124648573485165, -4.6455512270862728e-05, 0.40579399477970773, 1, 1, 0.42765683273523314, -0.30641914407703352, 0.72038469793523263, 0.49371923411137969, 0.071252638175748256, -0.060797673131111001, -0.7930064999182006, -0.87752891804063471, -0.75292737371422824, 0.19928163591665377, 0.1481242503897125, -0.41743967712960767}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated5() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:string:right=flow:x=js-type:string|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseWithinShortCircuitingBinOp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.87851295682988773, 0.4778499707230357, -0.37471399254636167, 0.39895865985604273, 0.14174545310793471, 0.31069901760123675, 0.13745628932118678, 0.39948256790261466, 0.37724703512595131, -0.44713268760754271, 0.47654779152030746, -0.45852951315237717, -0.77637662251180106, -0.21943002933363026, -0.52734220018268252, -0.3946036477174989, -0.6472267139526553, 0.52454705185353734, 0.5414362171196252, -0.37501790087675724, 0.81889869415101868}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated6() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:js-type:TRUE|state=stateless-scalars", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "", "getBooleanOutcomes", "com.google.javascript.rhino.jstype.BooleanLiteralSet,com.google.javascript.rhino.jstype.BooleanLiteralSet,boolean", new double[]{0.57822425284876466, -0.040768529426973031, -0.64359658643514217, -0.23748795719091836, -0.41132936662745717, -0.36284272727406147, -1, 0.097564133299704162, -0.6641139476632324}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated7() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:number|state=ast:ADD 1 [source_file: [testcode]] : ?\n    NAME x 1 [source_file: [testcode]] : number\n    NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=js-type:?:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseAdd", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-0.33544049761425471, -0.1412387343766269, -0.60722547666579907, -0.042180809789216935, 0.44091243239650135, 0.22183176037519303, 1, 0.35916822883112648, 0.24773185217077776, 0.12484320837035356, 0.32906429197534376, -0.10006135265009714, -0.79200469044545263, -0.65721692046961855, -1, -0.15802941751145527, 0.052742451779413203, 0.26934194654189075, 0.67138944163684955, -0.29060678142363167, -1}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated8() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:js-type:?|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "getJSType", "com.google.javascript.rhino.Node", new double[]{-0.4471830694364814, -0.31297178479493531, 0.037508049073048021, 0.13331535111008588, 0.1109752180079419, -0.72488596956316442, -0.61036189103624372, 0.8142893941175543, -0.033916962718022206, 0.60887936334597648, -0.023799151340420582, 0.14015956444045372, 0.15680555938382981, -0.17561465519680672, 0.71689414941506702, 0.50205195796701707, -0.043620017509802475, 0.089341850625662592}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated9() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:OBJECTLIT 1 [source_file: [testcode]] : Object\n    STRING_KEY p 1 [source_file: [testcode]]\n        NAME x 1 [source_file: [testcode]] : string\n:ast-type=js-type:Object:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseObjectLiteral", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.10352296251388154, 0.54102640497495369, -0.40451803306706047, 0.10536053262680273, 0.3269735749393563, 0.33104707018118368, 0.55374395277817734, 1, -0.56018379023919562, 0.049056027422870761, -1, 0.37770262109868047, 0.96924790563103014, 0.11910312223892786, 0.7230833981724305, 1, 0.26457767328688342, 0.55606303699209825, 0.58088760151671315, -0.45186013858035862, -0.73443136590021618}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated10() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:?|state=ast:ASSIGN 1 [source_file: [testcode]] : ?\n    NAME x 1 [source_file: [testcode]] : ?\n    NUMBER 2.0 1 [source_file: [testcode]]\n:ast-type=js-type:?:flow:x=js-type:?", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseAssign", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-0.16420547481727224, -0.12422639055443263, -0.23160610738620699, -0.3058561175809279, -0.49052667668954181, 0.11073819431273219, -0.14375072797309646, 0.16887677441032825, -0.059013230670000152, 0.040485053221575092, 0.015065587601309084, 1, 1, 0.68268027175671842, -1, 0.35644600078683591, 0.84310195246173303, 0.27097372804655967, -0.9279808518678182, -0.19650377135566952, 1}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated11() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:?|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    STRING s 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "createEntryLattice", "", new double[]{0.10212119696893154, -1, 0.56897061249192227, 0.70616848772299179, 0.21362968844847313, -0.020280258563064153, -0.085659299959347915, -0.16294344845727537, -0.46478134950875294, -0.59035357081568895, -0.59528468253542322, 0.94228735996821333, 1, 0.015115351273826393, 0.32464585324401246}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated12() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:number|state=ast:GETELEM 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : number\n    STRING p 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseGetElem", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-1, 0.57236849247325317, -0.75779045239931786, -1, -0.093837936283420659, -0.064756061939454171, -1, 0.1706825514517907, -0.10591327093173249, -0.043957736223304497, -0.79371574020668945, 0.45928911818040419, 0.036731914837410562, -0.15603598153270706, -0.71965769896602005, -0.45633707874565077, -0.18869496839366903, -1, -0.50317952798124754, 0.75467601913972415, -0.095715697725894444}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated13() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:HOOK 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    NUMBER 1.0 1 [source_file: [testcode]]\n    NUMBER 2.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseHook", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.56465737480561617, -0.11185155612060674, 0.31066476642354929, 0.34283623497713017, 0.52955971499653343, 0.097458568150685779, -0.35468754119252494, 0.081315911524943607, -0.22192163468121898, -0.0045484799310758694, 0.8917858900272857, 0.52852122156554893, 0.023542094778459716, 0.24525908889010725, 0.35828611941160021, 0.26769514675244493, -0.23084206755717754, -0.2470327821141608, -1, 0.02952222146842683, 1}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated14() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:string:right=flow:x=js-type:string|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseShortCircuitingBinOp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope,boolean", new double[]{0.62737734499554776, 0.17272867789830992, 0.51196047431894642, 1, -0.24922788063827001, 0.89962048000940309, 1, -0.017103567558053746, 0.42021289860935268, 0.92821167152402362, 0.80199035992159229, 1, -0.65723337882391308, 0.32129947843095918, 0.7823500821032312, -0.011053620164092448, -0.14947018778735255, 0.36628616225174704, 0.89408241773555375, -0.6061321412636671, -1, -0.26491533856876737, 0.032653148080198223, -0.016249273640010969}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated15() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:js-type:?|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "getJSType", "com.google.javascript.rhino.Node", new double[]{-0.39564867271962328, -0.34402622251037812, -0.48906110279963966, -0.0085573335949186702, 1, 0.89236423635390483, -0.57968269980295328, 0.063410960332796029, 0.15958619063642987, 0.97419748594440814, 0.60148307488781905, 0.36818240348436487, -0.28644055081172937, 1, -0.63045152245820202, -0.55827568026993923, -0.58026457805919107, 0.50931578739824912}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated16() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:ADD 1 [source_file: [testcode]] : ?\n    NAME x 1 [source_file: [testcode]] : string\n    STRING s 1 [source_file: [testcode]]\n:ast-type=js-type:?:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "flowThrough", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.29365990426480065, 1, 0.26134866953391533, -1, 0.40555167554453275, -0.25846161700372688, 0.7629317674578695, 0.18119185866468704, 0.47295808426718561, 0.63602420404370708, -0.16400942924622086, 0.30560443718163727, 0.50569135412739896, 0.55069645157713376, 1, 0.065891273695290242, 1, -0.54986053300807969, 0.50046066564822667, 0.02065958510287802, -0.74986434085548948}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated17() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:number|state=ast:NAME x 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "narrowScope", "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType", new double[]{0.386044621459254, -0.54243234889922243, -0.31778196002591197, -0.91501269923515949, 0.66095849794868511, 0.30382925583383597, -0.17653173137103065, 0.11455306451429303, 0.49679829469522546, -0.25053728501781525, 1, 0.48587047514424769, 0.57592192092394323, 1, 1, -0.18718316004476976, -0.12699460801401269, -0.40361125668076592, 0.46648654559731573, 0.26289746938326125, 0.015445158704950397, -0.52733872905304358, -0.56151197847338574, -0.30469604077074253}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated18() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=ast:NAME x 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "redeclareSimpleVar", "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType", new double[]{0.5683667318405965, -0.36089516082652323, 0.66880309979495878, -0.63349802496091223, 0.82440875841993377, -0.16014235759217543, 0.017305785615339409, -0.92456811267023187, 0.21447380261141208, 0.80420120519077232, 0.593951647054622, 1, 0.16345205195527091, 0.84687601233952237, 0.61297453462091356, 0.013238389367204673, 0.610536725708821, -0.27274767138255673, 1, 0.43629952033259772, -1, -1, 0.35813045816214262, 0.18032546126820434}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated19() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:NAME x 1 [source_file: [testcode]] : string\n:ast-type=js-type:string:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseName", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.74492509763416126, 0.00023804043367459116, -0.64284334701675871, -0.4498383925629782, -0.11360872078974521, 0.67276156679058241, -0.5579527612536993, 0.58324478825941961, 1, 0.44935773727195644, 0.24555683227216382, 0.77519717797197318, 0.14817948487542215, -0.14245838491899759, -0.58449092375787481, 0.70184329994821515, -0.46060287049758897, 0.015496276363129904, 0.88447018152742185, 0.35296523933355317, 0.25217507690825336}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated20() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:number|state=ast:ARRAYLIT 1 [source_file: [testcode]] : Array\n    NAME x 1 [source_file: [testcode]] : number\n    NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=js-type:Array:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseArrayLiteral", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-0.70157638732652772, -0.28566565932824578, 0.44737241051863952, -1, 0.53479646382869395, 0.72816661520159109, -0.45456292209763227, -0.030242855203537127, 1, 0.20071708923139098, 0.51369712736311768, 0.92986002942796431, 0.45809996131972508, 1, -0.1310235066440926, 0.81718177886254928, 0.46262017534741184, -0.20939277053911171, -0.20557127470200809, 0.35358782344902573, -0.015717778235743517}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated21() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[flow:x=js-type:number;]|state=ast:RETURN 1 [source_file: [testcode]]\n    ADD 1 [source_file: [testcode]] : ?\n        NAME x 1 [source_file: [testcode]] : number\n        NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "branchedFlowThrough", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-0.0084983055249209238, 1, 0.75196532337668709, 0.39652859341497299, -0.47612051770111458, 0.26584288859167204, -0.1963985294527151, -0.051532574815163018, 0.6767803538638788, 0.70912654438373235, -0.65097369608670308, 1, -0.21275354235197397, 0.65003543045830825, -0.85216692818599526, -0.19568141891496668, 0.1674041722960054, 0.96774848577313199, -0.51024133577796571, -0.28556023736752256, -0.79046106082197087}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated22() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:number|state=ast:NAME x 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "narrowScope", "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType", new double[]{-0.82494917971267201, 0.76037899849289348, 0.45302634390016461, -0.48659371350524827, 1, 0.31108069664487498, -0.27148016976263223, 0.28283075826839099, 0.93030910752781126, -0.23440364026448723, 0.71831198529970675, 0.91097958303513171, -0.076593474933565886, 0.80170360448239575, 0.81114313061207011, -1, 0.44657425637703935, -0.46594876873612068, -0.046928703936914506, 0.017497720957025287, -0.36833378550675866, -0.25739800486952452, -0.24910089031146185, -0.84121771786030797}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated23() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:string:right=flow:x=js-type:string|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseAnd", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.081160893879287266, 0.20180471792805699, 0.64782646321597692, -0.66611014993135886, 0.52408760669526455, -0.2841094046369802, -0.97101181100654455, 0.32799670409115717, 0.38608312547511697, 0.53269583369342532, 0.99631209469272508, -0.27014832965798696, -0.40205171449047461, -0.22811455069580699, -0.17160175183706752, 1, 0.015349337877471864, 0.056504115391646792, 0.12426946043268078, -0.10661670559844409, -0.17996066006035277}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated24() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:number|state=ast:GETPROP 1 [source_file: [testcode]] : ?\n    NAME x 1 [source_file: [testcode]] : number\n    STRING p 1 [source_file: [testcode]]\n:ast-type=js-type:?:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseGetProp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-0.19224390865298333, 0.75083538954210827, 0.095810021609757684, -0.69963285035969669, -0.26911489036726705, 0.52136475219423595, -0.5769142369686221, 0.24514631531894118, -0.81428012209171319, 0.70744429897057115, 0.53369804191763115, 0.36601256822531592, 0.36767885331945727, -0.52413825802718084, -0.11030173169614774, -0.066973458928943241, 0.62393194152001308, -0.080430857848344209, 0.63052265202011681, -0.16405221617095753, 0.40596663755151863}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated25() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=ast:NAME x 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "redeclareSimpleVar", "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType", new double[]{1, 0.17944786594439549, 0.7968713882975752, -0.94059837148302439, 0.71904507241230586, 0.79150790463765985, -0.31227005844234679, -0.75329497883641838, 1, 1, 1, 0.26251788527778885, -0.289200623696381, 1, 0.089934107913534261, -1, -0.018774016951820176, 0.27691860218425113, 0.8134698118050927, -1, 0.15000519555004346, -1, 1, -0.9149197435792964}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated26() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:number|state=ast:RETURN 1 [source_file: [testcode]]\n    ADD 1 [source_file: [testcode]] : ?\n        NAME x 1 [source_file: [testcode]] : number\n        NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseReturn", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-0.39971895992728856, 0.52735883351004287, 0.29353709170751452, -0.12436849299219396, -0.35686328277997509, 1, -0.27272880967801716, -0.56227135312054199, 0.32003547571015567, 0.58626778907487309, -1, 0.20232280550579748, 0.71161110889954604, 0.4403077442802984, 0.28805663263245035, 0.16076173469575233, 0.23025304157000404, -0.90987911111921693, -0.22506691493054964, -0.11926470373348298, -1}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated27() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:string:right=flow:x=js-type:string|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseAnd", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.52178406719347326, -0.27083883744200277, 0.77592347509360426, -0.23290225331525993, -0.078768598659774314, 0.31291893736437465, 0.84411187687058054, -0.53720720000099997, 0.4221915568105718, 0.34176111086871086, 1, 0.74460910739520025, -0.74772168310726461, 0.47939524267650174, 0.22704525853496327, 0.27585979602785077, 0.095919694406764899, 0.13326322940986951, 0.82210047230633831, -0.5962794420234695, -0.62728219888965675}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated28() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:string:right=flow:x=js-type:string|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseWithinShortCircuitingBinOp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{1, 0.61777168315793007, -0.12087526467119214, 0.01325137691866278, 0.12990823415409458, -0.44991647857876194, 0.12281908351091048, -1, 0.50355127690063128, 1, 0.41612394455058654, -0.08392481384906525, 0.10295787640131622, 1, 0.2258846988074894, 0.38278018909950379, 0.32851478108385568, -0.29424792181387666, 0.54148531108616027, 0.6510637422031379, 0.5307334639825364}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated29() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:?|state=ast:ASSIGN 1 [source_file: [testcode]] : ?\n    NAME x 1 [source_file: [testcode]] : ?\n    NUMBER 2.0 1 [source_file: [testcode]]\n:ast-type=js-type:?:flow:x=js-type:?", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseAssign", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.63278856967635955, -0.54409840765180473, 0.27562780889376015, 0.24111657132292852, 0.6757353974274054, -0.35906622036919655, 1, -0.3090991633735593, 1, -0.16255575946562872, -0.4543910211582064, 0.64080076381449957, 0.41281536609152208, 0.11411984008514786, 0.78758824910825942, -1, -0.57939955671335586, -0.43701173686010025, 0.63364961828600253, 0.59195727390745001, -0.42279023080921324}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated30() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:js-type:BOTH|state=stateless-scalars", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "", "getBooleanOutcomes", "com.google.javascript.rhino.jstype.BooleanLiteralSet,com.google.javascript.rhino.jstype.BooleanLiteralSet,boolean", new double[]{0.10180924252700274, 0.56154865625486794, 0.32282954112297269, -0.4937170654957917, -0.10464126229668069, 0.60318195305897437, 1, 0.49866176008830515, 0.11478634983174341}, "beam-explicit-fixtures-v3-proposal"));
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

    public static final String EXPLICIT_FIXTURES = "beam-explicit-fixtures-v3-proposal";
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
        boolean constructing;
        Object compiler, registry, scope, cfg, reverse, flow, closureNode, receiver;
        org.w3c.dom.Element domRoot;
        org.w3c.dom.Node domChild;
        Object jdomRoot, jdomChild;

        FixtureSession(String targetClass, String method) {
            this.targetClass = targetClass;
            this.method = method;
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
            javax.xml.parsers.DocumentBuilderFactory factory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
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
                if (scalar(type)) {
                    if (type == String.class && method.equals("getRelativePositionOfPI")) return a < 0 ? "fixture" : "other";
                    if (type == String.class && (method.equals("namespacePointer") || method.equals("getNamespaceURI")))
                        return a < 0 ? "r" : "i";
                    return legacyArgument(type, Math.max(-0.95, a), b, c, depth);
                }
                if (type.isArray()) {
                    Object array = Array.newInstance(type.getComponentType(), bucket(c, 5));
                    for (int i = 0; i < Array.getLength(array); i++)
                        Array.set(array, i, argument(type.getComponentType(), a, b, c, depth + 1));
                    return array;
                }
                String name = type.getName();
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
            java.lang.reflect.Field field = value.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(value);
        }

        String projection(Object result, int depth) throws ReflectiveOperationException {
            if (depth > 8) throw new FixtureFailure("Oracle projection depth exceeded", null);
            if (result == null) return "null";
            String name = result.getClass().getName();
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
                    if (session != null) session.receiver = receiver;
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
        if (!EXPLICIT_FIXTURES.equals(policy)) throw new IllegalArgumentException("Unknown explicit fixture policy");
        FIXTURES.set(new FixtureSession(className, methodName));
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
