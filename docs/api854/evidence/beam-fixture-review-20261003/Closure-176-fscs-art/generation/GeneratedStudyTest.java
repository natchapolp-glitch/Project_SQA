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
      assertEquals("value:flow:x=js-type:number|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : number\n    NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseChildren", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-0.61049100893172659, 0.93050221412222234, 0.84795280335358858, -0.065722643606052067, 0.3269412890601211, -0.57095406052406394, -0.55660750094751865, -0.42295513323748768, 0.384845491990635, -0.57524646328333784, 0.94221190270754729, -0.85928903139767154, -0.61342674253781793, -0.82227286033016833, 0.53978387322020516, -0.26697005045993127, -0.056635472382933427, -0.34794346632331141, 0.2748538373588767, -0.19221133854535144, -0.58346459175796883}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated2() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:HOOK 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    NUMBER 1.0 1 [source_file: [testcode]]\n    NUMBER 2.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseHook", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.36691307832350173, -0.98810455272076059, -0.83187732002215831, -0.93601413703448788, -0.30846918323353778, 0.89219684455009607, 0.75141415658992283, 0.67715127493774241, -0.76225586718579907, 0.41144902422832419, 0.5489576251125674, 0.61851368040772048, 0.58446834441411943, -0.71936832836615805, -0.64233525210009201, 0.58512023390622536, 0.47643327613055031, 0.78489882569515235, -0.10704748369600048, 0.77914445055590686, -0.72118204711048595}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated3() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:ADD 1 [source_file: [testcode]] : ?\n    NAME x 1 [source_file: [testcode]] : string\n    STRING s 1 [source_file: [testcode]]\n:ast-type=js-type:?:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseAdd", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.11494236361125787, -0.77372512305190511, 0.653686627674815, 0.73638090042259496, 0.70270018815450008, 0.53469862141164493, -0.51550320806685601, -0.41214996310872776, -0.89803007832073756, 0.57086755998041738, 0.71950204939621587, 0.87315269222121739, 0.20108144095977654, 0.5980109872017132, 0.69110682663567591, -0.7585478021062293, -0.86328557024709207, 0.75295676500355846, 0.94112295846847682, 0.43233990234790598, 0.23898751718308486}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated4() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:string:right=flow:x=js-type:string|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseAnd", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.17794965586031175, 0.76774328747836562, -0.12325744222066581, 0.83890797734224565, -0.93047816848165188, 0.14456609686776689, 0.55555428633830739, -0.012570542900460335, 0.6584849005482285, 0.96264560621563811, 0.67584522685185422, -0.042805948791273307, 0.38253983500007438, 0.74248431823911121, 0.24640617566158651, -0.90716706590137108, -0.58786880977873035, 0.22120297208654516, -0.76913764025865761, -0.68201901704743073, 0.9438754812895358}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated5() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:EMPTY:left=flow:x=js-type:string:right=flow:x=js-type:string|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    STRING s 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "newBooleanOutcomePair", "com.google.javascript.rhino.jstype.JSType,com.google.javascript.jscomp.type.FlowScope", new double[]{0.54193925884064509, -0.63130770392729896, 0.049962903853019158, 0.73826968840366591, 0.34985936671928797, -0.21633232256109958, -0.62211246129222908, 0.89138900046578184, 0.53648008546532622, 0.37779173320688852, -0.4880470136791526, -0.5192224365250413, 0.74449624476667586, 0.66263899573395202, 0.66452743886009769, -0.39154945881626602, 0.43128076570015517, 0.62932848047225609, 0.79222343410591334, 0.42607748466032369, -0.86912259745596199}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated6() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:js-type:?|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "getJSType", "com.google.javascript.rhino.Node", new double[]{-0.54375214136609373, -0.46361075965240528, -0.92367500129458158, 0.96017667012641694, -0.4773874618165006, -0.62097119989661032, 0.42614578326092634, -0.81436305415761168, 0.60767988163588127, 0.43805415735597597, -0.92204377773323465, 0.57703851657211969, -0.56872134998529167, -0.81136689263443462, 0.50386900054603889, 0.86286969366685673, -0.27620531769701828, -0.42063108662543991}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated7() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:RETURN 1 [source_file: [testcode]]\n    ADD 1 [source_file: [testcode]] : ?\n        NAME x 1 [source_file: [testcode]] : string\n        STRING s 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseReturn", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.67318586056721852, 0.5934339983341641, -0.72129534681475183, 0.77247511211051978, 0.95571409580348754, 0.60851211527398585, 0.85998926260855346, -0.37569062893746019, 0.58431216042043532, -0.11931584342079171, -0.95864665286489115, 0.11207992996941663, -0.77185690443305943, 0.75904533538856023, 0.45191120307341537, 0.044316557040686044, 0.72210176964182682, 0.84970251229997351, -0.45584484936465031, 0.15360540009902546, 0.54740425282301874}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated8() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:ADD 1 [source_file: [testcode]] : ?\n    NAME x 1 [source_file: [testcode]] : string\n    STRING s 1 [source_file: [testcode]]\n:ast-type=js-type:?:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "flowThrough", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.36514148413556202, -0.55139721195287983, -0.77124325186771614, 0.9599936390661008, -0.50486766041471176, -0.75220590649619545, 0.68386405690577989, -0.29677427159536163, -0.040898426562357759, 0.56579248385193126, 0.48191282270330582, 0.87294401255109078, 0.86414162428013386, 0.74174779558000337, -0.54173292633315606, 0.39251579759709321, -0.86754323563953317, -0.54161929323622982, -0.24890985127719834, 0.52904466726547228, -0.96823638530775424}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated9() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:number:right=flow:x=js-type:number|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : number\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseShortCircuitingBinOp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope,boolean", new double[]{-0.63938171428073587, -0.026616997661357944, -0.59074207851216842, -0.48874548906889981, 0.34963441136324414, 0.48152828104641698, -0.74410725308110792, -0.20588847772723096, 0.43736459054596799, 0.87491916472207465, -0.78547364810181963, 0.82235317398983909, -0.73922189697375296, 0.48754475534920139, -0.31786003657328354, 1.8753354709133419e-05, 0.73028272401272076, -0.72332013927301153, -0.97019736906559606, -0.10314850912325979, -0.52013038193896488, 0.56047386393708942, 0.44609661554898694, -0.68799737805123784}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated10() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:ARRAYLIT 1 [source_file: [testcode]] : Array\n    NAME x 1 [source_file: [testcode]] : string\n    NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=js-type:Array:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseArrayLiteral", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.069871033405986216, -0.99910392877351017, 0.52938123316885211, -0.72287649519215735, 0.58540567154050427, -0.2552904568592711, 0.36353144653593206, -0.14038097296058405, -0.8965158576309582, 0.94446775717526954, 0.64359852585334165, -0.16077208704091217, 0.14459692070075025, -0.98682871279350803, 0.18256162746995397, -0.98940455622569146, -0.86870780080231436, 0.26744455310514015, -0.69675432342278798, 0.22341924789928269, -0.48839531152945348}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated11() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:dHJ1ZQ==|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "isAddedAsNumber", "com.google.javascript.rhino.jstype.JSType", new double[]{-0.75894968997886036, 0.24244475727410197, -0.9601709808894523, 0.73613955612423121, -0.66077431416675192, 0.19709687534252884, -0.625600106524977, -0.37299189265428412, -0.7569194436413047, -0.095519049716676019, 0.65905629144481259, 0.6997651108290539, 0.83374793552631976, -0.54047378818764469, 0.57396842836566653, -0.41585977460827195, 0.051261417846185653, -0.2947478891557751}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated12() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:RETURN 1 [source_file: [testcode]]\n    ADD 1 [source_file: [testcode]] : ?\n        NAME x 1 [source_file: [testcode]] : string\n        STRING s 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseReturn", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.15611653735795827, 0.72641344688554632, 0.82479522583486653, -0.10902312064920117, 0.77461162984845555, 0.14796469104505627, 0.28971422027198623, 0.34503465804094424, -0.16822435093790067, -0.94448610931712906, -0.61093611522479141, -0.84704650246574431, -0.67100632960499462, 0.44495896461810336, -0.38345112322299624, 0.0030104471394956089, -0.99258285524268386, -0.70155894524125895, -0.15076475525979549, -0.09193908829600117, -0.025887227967772652}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated13() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:number:right=flow:x=js-type:number|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : number\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseShortCircuitingBinOp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope,boolean", new double[]{-0.59016432835204968, 0.33557859763182085, 0.43092391187212131, -0.68006976599206137, -0.95349274194400491, 0.7191650224970334, -0.79591913669176595, 0.95298602841417224, 0.09470784340200944, -0.89329850706501013, -0.57951905908098555, -0.45486811979836594, 0.84383141782599447, -0.89068741186895561, 0.9546620548011695, 0.61092975444770459, -0.68229236633603829, 0.12747302748573519, -0.82798266635542639, -0.766571282822196, -0.67705825056464142, 0.63230305043913271, -0.85823540946939048, -0.051330170394492614}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated14() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:js-type:BOTH|state=stateless-scalars", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "", "getBooleanOutcomes", "com.google.javascript.rhino.jstype.BooleanLiteralSet,com.google.javascript.rhino.jstype.BooleanLiteralSet,boolean", new double[]{-0.81339680005473358, -0.43742560007501474, 0.58041668579491046, 0.77214945308906491, -0.6148477673658086, -0.8892078564593433, -0.1515208106682997, -0.65330218273645801, -0.931403707953677}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated15() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=absent|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    STRING s 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "createInitialEstimateLattice", "", new double[]{0.042830027195691622, -0.60205608842406555, -0.45731990939349343, -0.98431711098953611, -0.15084146858395875, 0.29462850722895428, 0.30841947478212983, 0.80812243148762897, -0.012061964472882103, 0.62103773322496392, 0.75602974118497412, -0.31224685900030269, -0.30336053981033206, 0.76429619501299517, 0.15895834333980297}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated16() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=ast:NAME x 1 [source_file: [testcode]] : string\n:ast-type=js-type:string:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "updateScopeForTypeChange", "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType,com.google.javascript.rhino.jstype.JSType", new double[]{0.64355308873543815, -0.2793168616095989, 0.99040291043164874, 0.7558995915328659, -0.71458184186664875, 0.6262800803881432, 0.9116127969416532, 0.10259585162622642, -0.038176838894999454, -0.092145067840404105, -0.72156877722538604, -0.27297077506073242, 0.5695959339472374, -0.49285382457735261, -0.42017559327478393, -0.28901719766770007, 0.12134512641597928, 0.082142686387302488, -0.54214268321377279, -0.1520657477813443, 0.8947921721402925, -0.98504345013088157, 0.016876080460667353, -0.86263758515746658, 0.77569226407668812, -0.41745670966077086, 0.37358191174091404}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated17() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:boolean-pair:BOTH:BOTH:left=flow:x=js-type:string:right=flow:x=js-type:string|state=ast:AND 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    TRUE 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseWithinShortCircuitingBinOp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.96981792601800754, -0.89859859257254771, 0.41201986088461195, -0.081949953951679255, 0.13316063251359611, 0.6060473854464048, -0.58105126364615556, 0.77212538167497824, -0.59361516364473732, -0.87116233984253344, 0.3760350007190818, 0.44388502727169965, -0.94815653335540118, 0.29796888132953958, 0.88413741970627635, -0.40851444961398942, -0.88634418924470304, -0.437794709871836, -0.50121652275307604, -0.22962996676154535, 0.54374206746759768}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated18() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:js-type:FALSE|state=stateless-scalars", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "", "getBooleanOutcomes", "com.google.javascript.rhino.jstype.BooleanLiteralSet,com.google.javascript.rhino.jstype.BooleanLiteralSet,boolean", new double[]{0.0084148402078365603, -0.26374507572674744, -0.94238628523251045, -0.62629965652696451, -0.86286763272195555, -0.90847011531987332, 0.37188892499348225, -0.62774292206573268, -0.91315109769332481}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated19() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:js-type:EMPTY|state=stateless-scalars", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "", "getBooleanOutcomes", "com.google.javascript.rhino.jstype.BooleanLiteralSet,com.google.javascript.rhino.jstype.BooleanLiteralSet,boolean", new double[]{-0.97235105037238068, 0.55989952715179925, 0.17612192094471602, -0.76031241582984266, -0.8020841167045869, 0.93172813627794482, -0.50072051182482369, 0.38535634244655204, 0.025903243357031647}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated20() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:ZmFsc2U=|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    STRING s 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "isUnflowable", "com.google.javascript.jscomp.Scope$Var", new double[]{0.90074795770147431, 0.81529496134017787, -0.83481134436169424, 0.5867172147967743, -0.43742793859918705, 0.071479725058119614, 0.9500775907157446, 0.22561009383595065, 0.55084963554361699, -0.79622159088931177, -0.20562739335646119, -0.48346846472134231, -0.3559945767108883, -0.44849882631549431, -0.23595219394253242, -0.8529744357255884, 0.34791487833460111, -0.026075405764949267}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated21() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:HOOK 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    NUMBER 1.0 1 [source_file: [testcode]]\n    NUMBER 2.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseHook", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.4478881761137985, -0.20636544156033554, -0.28127420078007548, 0.45695874201916276, -0.65975194753691535, 0.99437158546539228, 0.37927106313692849, 0.68741804853616739, -0.20687189272827933, -0.57073272780799367, -0.64901861434060093, 0.8614907758651047, -0.77686192135699406, 0.63245655815715685, 0.91736885486913922, -0.97770972252492983, 0.73008688332629967, 0.74962237339985816, 0.55504789368906948, 0.36821955061810474, -0.57584003107544524}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated22() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:GETPROP 1 [source_file: [testcode]] : ?\n    NAME x 1 [source_file: [testcode]] : string\n    STRING p 1 [source_file: [testcode]]\n:ast-type=js-type:?:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseGetProp", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.69278460013505327, 0.88432488208908699, 0.92176799388544062, 0.852287115873684, -0.17705012664327868, -0.57339694265060914, 0.90894094715847817, 0.18319161303960074, -0.17204692158868173, 0.35712901804935404, 0.25435968321995217, 0.91067289166471466, -0.22122580470790609, -0.047875461312506085, -0.60310657868636453, 0.52275424840769991, 0.35259371084089386, -0.74248523741839101, -0.11132140320868555, 0.8524152396773319, -0.99938426938464775}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated23() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:NAME x 1 [source_file: [testcode]] : string\n:ast-type=js-type:string:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseName", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.65251098941604213, 0.93915690427939547, 0.35411152273613533, 0.58518056781737249, -0.6431320114858905, 0.70899061608734715, -0.48176805223323083, 0.72036090992559876, 0.80813903868619752, 0.54231163870775401, 0.89776498254658499, 0.27220674120369881, 0.67043139073971481, -0.82881368830382973, -0.99281102406193122, 0.17801523036766409, -0.96451670539404111, 0.055582539919314389, 0.20902236298621268, 0.25348107700114797, -0.88761939825288683}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated24() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:ZmFsc2U=|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "isUnflowable", "com.google.javascript.jscomp.Scope$Var", new double[]{-0.7825610728112451, 0.98573882257946766, -0.13948382108776891, 0.82999152122831887, -0.69926524406312063, -0.28931875329113588, -0.97661425788473299, -0.28950883194975119, 0.51772905251109291, -0.28344361852140709, -0.38294757430655713, -0.078851489742730818, 0.99007410011670305, 0.73055412183357249, -0.22237886890277658, 0.52067641914773799, -0.24127866596620495, 0.73718949158240177}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated25() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[flow:x=js-type:number;]|state=ast:RETURN 1 [source_file: [testcode]]\n    ADD 1 [source_file: [testcode]] : ?\n        NAME x 1 [source_file: [testcode]] : number\n        NUMBER 1.0 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "branchedFlowThrough", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{-0.1672823587688439, 0.086759926429468059, -0.36897036541467698, 0.081298404845621564, 0.90191992845489066, 0.69320543227765419, 0.15295368326416647, 0.76200096417307606, -0.36281353459172028, 0.35437045165855485, -0.33864255563557566, 0.55280924022252442, 0.64081577863804728, -0.64930302320567224, -0.51128700183492692, -0.1499146701672156, -0.58118569294126865, -0.22689453301427842, 0.75794675496774189, -0.83824590309498404, 0.4418531359703024}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated26() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:js-type:?|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    STRING s 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "getJSType", "com.google.javascript.rhino.Node", new double[]{0.77274663540471589, -0.75804345512580218, -0.62389427385427898, -0.93673522242251206, -0.88791084126787334, 0.92863965693182737, 0.445775514100887, -0.85662379189128601, -0.20637844346103718, 0.43622124364820447, -0.54199708136410663, 0.51672940408985668, -0.84412202373341549, 0.97589687925628033, 0.14533466498605718, 0.84981769679998242, -0.89335766750650736, 0.60801600505934217}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated27() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:string|state=ast:GETELEM 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]] : string\n    STRING p 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "traverseGetElem", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.type.FlowScope", new double[]{0.81668580608837016, 0.75666647332629933, 0.43512033710931974, -0.80740420556885972, 0.85347811780565719, -0.97041434598809229, -0.88600949995128331, 0.67941803762972919, -0.40883132678981515, 0.043916799507252025, 0.87904576489835162, -0.073200104875258409, -0.10095682568154118, -0.1905146236377302, -0.69406695270740193, 0.63307357644682494, 0.79055518886882226, 0.7836001188738122, -0.17819471083444149, -0.35324009330082196, 0.56006109319680597}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated28() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=js-type:number|state=ast:NAME x 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:number", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "narrowScope", "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType", new double[]{-0.034919159768281638, -0.76673819568983514, -0.5428593672215456, -0.73475038345652899, -0.72948753399684829, -0.25827737611025792, -0.16567114051155718, -0.37167324575801741, 0.40552324688925734, -0.56009971526736835, 0.34011830749678529, 0.82340199946923542, 0.79059028710470236, 0.059978329309360312, -0.83551487104520472, -0.99730609368625944, 0.81886722688520752, 0.6157327946386737, 0.25726051300860164, -0.50071055526841191, 0.13382547826287206, -0.88706229446490736, 0.04043241042288126, 0.79538286819612902}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated29() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:flow:x=absent|state=ast:ADD 1 [source_file: [testcode]]\n    NAME x 1 [source_file: [testcode]]\n    STRING s 1 [source_file: [testcode]]\n:ast-type=null:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "createInitialEstimateLattice", "", new double[]{0.20810475058746114, 0.92334957979425125, -0.22126005024345119, -0.88248600149339884, -0.96888394695115343, 0.10078994453738166, -0.82075962201775288, -0.13919896623564343, -0.74372967214784858, 0.0095609330835075834, -0.66338945481063227, 0.75419670949190976, -0.091315616501317587, 0.91340164018861714, -0.85711213322980351}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated30() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=ast:NAME x 1 [source_file: [testcode]] : string\n:ast-type=js-type:string:flow:x=js-type:string", SqaProbe.observeWithPolicy("com.google.javascript.jscomp.TypeInference", "com.google.javascript.jscomp.AbstractCompiler,com.google.javascript.jscomp.ControlFlowGraph,com.google.javascript.jscomp.type.ReverseAbstractInterpreter,com.google.javascript.jscomp.Scope,java.util.Map", "updateScopeForTypeChange", "com.google.javascript.jscomp.type.FlowScope,com.google.javascript.rhino.Node,com.google.javascript.rhino.jstype.JSType,com.google.javascript.rhino.jstype.JSType", new double[]{-0.28451817277135061, -0.054129963461504005, -0.81835842544245252, 0.7004888648958485, 0.86049877254652785, -0.20887637819990545, -0.55689445141143334, 0.82848421542069217, -0.52440913975149694, 0.55142147811974818, -0.94349495940731831, 0.93798374750554059, 0.61830970244620209, -0.81774204006054418, -0.43538685704166125, -0.57397421616618804, -0.63509639422755293, -0.82001055818414659, -0.90784812165464013, 0.052660587903260447, -0.87347678282249919, 0.91484300069757252, -0.96450854593296653, -0.34873263685576528, 0.89450786745097299, -0.76140838220309859, 0.45176517926470483}, "beam-explicit-fixtures-v3-proposal"));
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
