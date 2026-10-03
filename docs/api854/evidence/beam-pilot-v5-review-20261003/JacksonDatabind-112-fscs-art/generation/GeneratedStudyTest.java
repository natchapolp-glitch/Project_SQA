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
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{-0.61049100893172659, 0.93050221412222234, 0.84795280335358858, -0.065722643606052067, 0.3269412890601211, -0.57095406052406394, -0.55660750094751865, -0.42295513323748768, 0.384845491990635, -0.57524646328333784, 0.94221190270754729, -0.85928903139767154, -0.61342674253781793, -0.82227286033016833, 0.53978387322020516}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated2() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{0.5489576251125674, 0.61851368040772048, 0.58446834441411943, -0.71936832836615805, -0.64233525210009201, 0.58512023390622536, 0.47643327613055031, 0.78489882569515235, -0.10704748369600048, 0.77914445055590686, -0.72118204711048595, 0.91424696370938041, 0.76894999015236598, -0.32488679327043735, -0.86489456803234699}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated3() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{-0.8253894866562046, -0.71927621884485715, 0.11886588018702704, 0.65294478053326022, -0.76127853086529318, -0.70511699494364155, -0.00029633983345034309, -0.38999514007687308, -0.71897096655282411, -0.68288313598834605, -0.1507464575623858, 0.58535644500795625, -0.6573642438878502, 0.6117983592538061, -0.85069598899090493}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated4() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{0.54472726175783026, -0.85199491055693621, -0.60251824327927483, -0.95927859507000579, -0.022742343552412114, -0.77969723812921909, 0.0414728044236925, -0.67790886891076019, 0.051685684809550381, -0.43607248083801275, 0.49828342170214723, 0.63090894984433077, 0.91886390517681127, -0.073436437908427132, 0.77902634680737881}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated5() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{-0.089527056865675636, 0.39900415996629501, -0.10732999822971312, 0.49535919872600109, -0.81069271903954676, -0.654085689083864, 0.39524876994353786, 0.48102268501493017, -0.27878864224389477, -0.91763150413511818, 0.22469045705684221, -0.79034658601617491, -0.22590167408965112, 0.61568159754379903, 0.60151205531523599, 0.64369762828296739, -0.79029140335902959, 0.46407865056052855}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated6() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;]|state=json-token:VALUE_STRING", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "handleNonArray", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{-0.21845459129939471, -0.21270498193563436, 0.11805760283482591, -0.40620842354632702, -0.071836814237768776, 0.77200137794929091, -0.77773460938878469, -0.82529123207582544, 0.06092426285988517, -0.96692925617130232, -0.27262284832010031, -0.5021951441398802, 0.54193925884064509, -0.63130770392729896, 0.049962903853019158, 0.73826968840366591, 0.34985936671928797, -0.21633232256109958}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated7() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;]|state=json-token:VALUE_STRING", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "handleNonArray", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{-0.38289414597493621, 0.6914049140374452, -0.76977400556791475, 0.010992254129749801, 0.48044563229243265, 0.88576192693419342, 0.28884721544337544, -0.90554600453063872, 0.51461208747364329, 0.70255766774680639, 0.68749606090888382, 0.5784869614919621, 0.43392845273291614, 0.50443893101900605, -0.13243323611928126, -0.88478199518098721, -0.56637180650245789, -0.059930217435762989}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated8() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{0.53621463965060201, 0.56161339766844232, -0.23537278758944691, -0.24945979958987974, 0.81625157302459095, 0.56243921950794062, -0.3192686434187888, 0.42849970875947729, -0.84405535859154623, 0.54791974565605295, -0.42883946052443767, -0.71745918941552489, 0.19436220927344028, 0.08045407515223002, 0.1743767291238616}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated9() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{-0.12966531966888284, 0.26978665172634009, -0.16455097276160524, 0.52221115497255655, -0.77317283340073972, 0.80638278689885867, 0.20661010488708365, 0.89090038595145926, -0.0077476359651635907, 0.53536542818460386, -0.22042734684835996, 0.94444066738082144, -0.67246918955994794, -0.48516532358778974, 0.88660141125453684}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated10() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YmV0YQ==;]|state=json-token:VALUE_STRING", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "handleNonArray", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{0.61961110905620975, -0.39438439874314724, -0.90565847071879735, 0.48145629624093589, 0.8740524434242003, -0.78967364585693955, 0.78393761455901512, 0.79205760205312492, -0.77778266961134124, -0.38585005671933903, 0.076613993061711794, 0.25352601062382685, -0.5383310703755233, -0.34642875763970249, -0.99480035004714007, 0.43864148954313054, -0.48372357860922843, -0.38155599419594499}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated11() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserializeUsingCustom", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection,com.fasterxml.jackson.databind.JsonDeserializer", new double[]{-0.61029078218132282, -0.4277308391499548, 0.61507727560346637, -0.9715899485416053, 0.12123001336147632, -0.4180373476074879, -0.12539877633816965, 0.30230691158680512, 0.91493904119436897, 0.022936364420055311, -0.60525877413566453, -0.37316181837628215, 0.7616519596256941, 0.45138533945337955, 0.22116311165071934, -0.80332141242446031, -0.97032926066144087, -0.017108542528683213, -0.35307938949416062, -0.39898866609045092, 0.61168068855137747}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated12() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserializeUsingCustom", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection,com.fasterxml.jackson.databind.JsonDeserializer", new double[]{-0.8965158576309582, 0.94446775717526954, 0.64359852585334165, -0.16077208704091217, 0.14459692070075025, -0.98682871279350803, 0.18256162746995397, -0.98940455622569146, -0.86870780080231436, 0.26744455310514015, -0.69675432342278798, 0.22341924789928269, -0.48839531152945348, 0.84876779286718862, -0.6700019564498918, 0.98099778492322232, -0.12431107964735011, 0.46608471978745314, 0.7700080014386379, -0.69703678075015008, 0.49059699063169671}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated13() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YmV0YQ==;]|state=json-token:VALUE_STRING", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "handleNonArray", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{0.0081050272377378718, -0.027592535443315969, 0.97245942383848805, 0.27932010381167593, 0.17654636780124355, -0.68437329517750567, -0.53759052683952246, 0.6633201910376203, -0.41949698602501018, 0.96865022643413701, 0.67430056837111962, -0.27382044578794451, -0.027692829292715437, 0.13286219440124736, -0.0053640329435715728, 0.016757599917411214, 0.94744456139654409, 0.50112266930991911}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated14() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{-0.06189755936782837, 0.49373587934783147, 0.83141204982072447, -0.9603697458245648, 0.28181107286196672, 0.8616395178538756, 0.88182390752125706, -0.50048106390201275, 0.39074108642478489, -0.77927686736859103, -0.44055905208852408, 0.43641043124511492, -0.69761130692227247, -0.73853457388104271, -0.31416198525230987}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated15() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{-0.02064387005588153, -0.89588949241556293, 0.54037790694295817, 0.27773640290700019, 0.64980826953536197, -0.29490688447657454, 0.93838160294561712, -0.87316184510178152, 0.62320400291556011, 0.42662374094465161, 0.38969620538775307, 0.11129117208176731, -0.22059748045474614, 0.91796145052702216, 0.18049323529905248, 0.15966024790343125, -0.19072414439192098, 0.23882062455090369}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated16() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{0.09470784340200944, -0.89329850706501013, -0.57951905908098555, -0.45486811979836594, 0.84383141782599447, -0.89068741186895561, 0.9546620548011695, 0.61092975444770459, -0.68229236633603829, 0.12747302748573519, -0.82798266635542639, -0.766571282822196, -0.67705825056464142, 0.63230305043913271, -0.85823540946939048, -0.051330170394492614, -0.8531270960809727, -0.43677403020368155}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated17() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{0.18167364031676292, 0.88173713340832083, 0.19169605175126914, 0.75208841775190405, 0.30321832830952156, 0.67423272876881835, 0.44525059352459984, 0.27122093692990634, -0.52885697536398846, -0.22177922485031054, 0.47287705362210608, -0.57063682199140087, 0.33153566099534371, 0.6450760458453193, -0.7128816923455521, 0.065787259662688058, 0.60978033096806628, -0.85156892250713834}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated18() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{-0.31641952275897256, 0.91395048028648618, 0.023293949005840586, 0.71318854480356619, 0.32826269582652245, -0.72224747891364527, 0.63332472293811692, -0.23331182547806395, -0.38659846130505415, -0.6313364865437141, 0.22805713210703416, 0.85040190137464866, 0.81694636604794879, 0.22078652814510513, 0.94504940498214518, -0.035254100008364153, -0.90441256998534758, -0.12350440711820787}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated19() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{0.61422641331315941, -0.56732694898269065, 0.31252089141325445, 0.81206393203097127, 0.60656450775568649, -0.13319954279844937, -0.43134369579331344, -0.71286088636989753, 0.67820312877969879, 0.26631545294030046, -0.57541521832441433, -0.45857838072842849, 0.83560909017761986, -0.46887065336925526, 0.58321473345081332}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated20() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserializeUsingCustom", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection,com.fasterxml.jackson.databind.JsonDeserializer", new double[]{0.79740578371596671, -0.563034492371284, -0.94447440417365591, 0.7571516539940395, -0.28620545364888517, 0.10600959414467637, 0.90471630071859188, -0.44839511520002584, -0.73579419431189086, 0.96683941179956712, -0.96986646151829081, 0.8652928797986863, 0.57040429522300884, 0.79296894472082236, 0.36951063464445189, 0.71220153983321088, -0.13913863709498608, 0.5716340437923273, -0.21944529238002275, -0.51534052186033641, -0.57778488538034467}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated21() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YmV0YQ==;]|state=json-token:VALUE_STRING", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "handleNonArray", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{0.77569226407668812, -0.41745670966077086, 0.37358191174091404, 0.35259165667237791, -0.80299400366809603, -0.29851544659803464, 0.94127944292436272, 0.096603463314424376, 0.76073981576457905, -0.094356895646034777, -0.68034096586018356, 0.9565984004931245, -0.76839179393312618, 0.90815766815435039, 0.26416600209036667, -0.70757763320551548, -0.70206038172081175, -0.070650669033126112}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated22() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{0.88413741970627635, -0.40851444961398942, -0.88634418924470304, -0.437794709871836, -0.50121652275307604, -0.22962996676154535, 0.54374206746759768, -0.20585093882022099, -0.81257441429389754, -0.85252217495162186, 0.65765217188848601, 0.73829644366153624, -0.13649296284607093, -0.94307653239216616, 0.63367688626620988}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated23() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserializeUsingCustom", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection,com.fasterxml.jackson.databind.JsonDeserializer", new double[]{0.96256431783963192, -0.39483804025293301, -0.5073010121470185, 0.22825289118885839, -0.86171433303048062, 0.9182925718034527, 0.24530872973252427, -0.9044322745302511, -0.74145872374695898, 0.051867751636335058, -0.93959763579212763, 0.12201160592230864, -0.54454418501958224, -0.39481691950720887, -0.81586601506237511, -0.65006029475264393, 0.38917003576298814, 0.27908523224337367, -0.62804254441703211, -0.43220071526906412, 0.47862545225609043}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated24() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{-0.33531948449195226, 0.3923724463633993, -0.50233171723850534, -0.85508531623947781, 0.31723731760003315, -0.75784789459855317, -0.70540338537313962, -0.7252399258055664, 0.72292392795216975, 0.77770059296490279, 0.95793895609063062, -0.78077412136788782, -0.91073202307895507, 0.52122588809292658, -0.003120726759273218}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated25() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{0.90398255289306473, -0.61059360566233001, 0.17947252450022355, -0.87190969746693892, -0.65712359399481146, 0.52894770379101241, -0.47303010030310833, 0.73539159967207679, 0.35720765039407665, -0.99690338938055967, -0.50361421288107056, 0.37596127470613583, -0.37142897155220433, 0.8142511721925263, -0.49157301252254393}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated26() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{-0.74828687611516176, -0.54865541680148788, 0.72623461913080622, -0.39175851447502663, -0.72671285942109254, 0.58314862853712368, -0.79769877447308901, -0.75506978152020032, -0.79047922205998877, -0.37890263033086824, -0.10447711770125934, 0.48491369784726546, -0.58965994958662349, -0.020684699182744737, 0.37342394060537787}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated27() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{0.45387698735132243, -0.45816302625710059, 0.67990100415054355, -0.79070438681358124, -0.41019205762346633, 0.53225069374139933, -0.97731787388240443, 0.39965712580684953, -0.68368627844457075, -0.95775205181629874, 0.68514905891277111, 0.87371445636094314, 0.19399531301401929, 0.70822810642124212, 0.28028225232415949, 0.63586816814477154, -0.30052038376152335, 0.34536704250279104}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated28() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection", new double[]{-0.96610093772271277, -0.31223632755916331, 0.25643110743219411, -0.80578450684738212, 0.46757855795007353, -0.73620123854695518, 0.76747473939468214, 0.11251329915721131, -0.57072496050472288, -0.99381035901999604, -0.023196589558776903, 0.4254824741974752, -0.17255268456300654, -0.47222832124270209, -0.042495018594294498, 0.70578053085066816, 0.59146674096459728, -0.93763326728815888}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated29() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:YWxwaGE=;java.lang.String:YmV0YQ==;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserializeUsingCustom", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext,java.util.Collection,com.fasterxml.jackson.databind.JsonDeserializer", new double[]{-0.88386281466669669, -0.97973170497017437, -0.97684973183289148, 0.61687829291595531, -0.249248684511584, -0.13003572383193762, 0.23474282323653561, -0.38781826602756331, 0.46631144149732129, -0.0085710758524515374, 0.35107196975020805, -0.97587177990224871, -0.81078457165938422, -0.45317070858884168, 0.17932333114367749, -0.022065964635389568, 0.16874430201229407, -0.9843896633425584, 0.58672827236907366, 0.11135156455092243, 0.55685896416859193}, "beam-explicit-fixtures-v5-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated30() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:list[java.lang.String:bGVmdA==;java.lang.String:cmlnaHQ=;]|state=json-token:END_ARRAY", SqaProbe.observeWithPolicy("com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer", "com.fasterxml.jackson.databind.JavaType,com.fasterxml.jackson.databind.JsonDeserializer,com.fasterxml.jackson.databind.deser.ValueInstantiator", "deserialize", "com.fasterxml.jackson.core.JsonParser,com.fasterxml.jackson.databind.DeserializationContext", new double[]{0.31081594765274145, 0.84973141544288811, -0.22341514690746989, -0.25120062995124526, 0.32545975049560028, -0.39223081383956671, -0.28533840851347114, 0.19727268380542973, 0.91049143854125125, -0.11978924775360733, 0.29531986624279005, 0.77394798167196499, -0.74038325339004607, -0.88907012153248388, -0.98814858975208009}, "beam-explicit-fixtures-v5-proposal"));
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
