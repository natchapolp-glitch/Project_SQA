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
  @Test(timeout=10000)
  public void generated1() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getFormatFeatures", "", new double[]{-0.61049100893172659, 0.93050221412222234, 0.84795280335358858, -0.065722643606052067, 0.3269412890601211, -0.57095406052406394, -0.55660750094751865, -0.42295513323748768, 0.384845491990635, -0.57524646328333784, 0.94221190270754729, -0.85928903139767154, -0.61342674253781793, -0.82227286033016833, 0.53978387322020516}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCurrentName", "", new double[]{0.5489576251125674, 0.61851368040772048, 0.58446834441411943, -0.71936832836615805, -0.64233525210009201, 0.58512023390622536, 0.47643327613055031, 0.78489882569515235, -0.10704748369600048, 0.77914445055590686, -0.72118204711048595, 0.91424696370938041, 0.76894999015236598, -0.32488679327043735, -0.86489456803234699}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getText", "", new double[]{-0.8253894866562046, -0.71927621884485715, 0.11886588018702704, 0.65294478053326022, -0.76127853086529318, -0.70511699494364155, -0.00029633983345034309, -0.38999514007687308, -0.71897096655282411, -0.68288313598834605, -0.1507464575623858, 0.58535644500795625, -0.6573642438878502, 0.6117983592538061, -0.85069598899090493}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "", new double[]{0.54472726175783026, -0.85199491055693621, -0.60251824327927483, -0.95927859507000579, -0.022742343552412114, -0.77969723812921909, 0.0414728044236925, -0.67790886891076019, 0.051685684809550381, -0.43607248083801275, 0.49828342170214723, 0.63090894984433077, 0.91886390517681127, -0.073436437908427132, 0.77902634680737881}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getStaxReader", "", new double[]{-0.089527056865675636, 0.39900415996629501, -0.10732999822971312, 0.49535919872600109, -0.81069271903954676, -0.654085689083864, 0.39524876994353786, 0.48102268501493017, -0.27878864224389477, -0.91763150413511818, 0.22469045705684221, -0.79034658601617491, -0.22590167408965112, 0.61568159754379903, 0.60151205531523599}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_releaseBuffers", "", new double[]{-0.21845459129939471, -0.21270498193563436, 0.11805760283482591, -0.40620842354632702, -0.071836814237768776, 0.77200137794929091, -0.77773460938878469, -0.82529123207582544, 0.06092426285988517, -0.96692925617130232, -0.27262284832010031, -0.5021951441398802, 0.54193925884064509, -0.63130770392729896, 0.049962903853019158}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_updateState", "com.fasterxml.jackson.core.JsonToken", new double[]{-0.38289414597493621, 0.6914049140374452, -0.76977400556791475, 0.010992254129749801, 0.48044563229243265, 0.88576192693419342, 0.28884721544337544, -0.90554600453063872, 0.51461208747364329, 0.70255766774680639, 0.68749606090888382, 0.5784869614919621, 0.43392845273291614, 0.50443893101900605, -0.13243323611928126, -0.88478199518098721, -0.56637180650245789, -0.059930217435762989}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getTextOffset", "", new double[]{0.53621463965060201, 0.56161339766844232, -0.23537278758944691, -0.24945979958987974, 0.81625157302459095, 0.56243921950794062, -0.3192686434187888, 0.42849970875947729, -0.84405535859154623, 0.54791974565605295, -0.42883946052443767, -0.71745918941552489, 0.19436220927344028, 0.08045407515223002, 0.1743767291238616}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getTextLength", "", new double[]{-0.12966531966888284, 0.26978665172634009, -0.16455097276160524, 0.52221115497255655, -0.77317283340073972, 0.80638278689885867, 0.20661010488708365, 0.89090038595145926, -0.0077476359651635907, 0.53536542818460386, -0.22042734684835996, 0.94444066738082144, -0.67246918955994794, -0.48516532358778974, 0.88660141125453684}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_updateState", "com.fasterxml.jackson.core.JsonToken", new double[]{0.61961110905620975, -0.39438439874314724, -0.90565847071879735, 0.48145629624093589, 0.8740524434242003, -0.78967364585693955, 0.78393761455901512, 0.79205760205312492, -0.77778266961134124, -0.38585005671933903, 0.076613993061711794, 0.25352601062382685, -0.5383310703755233, -0.34642875763970249, -0.99480035004714007, 0.43864148954313054, -0.48372357860922843, -0.38155599419594499}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCurrentLocation", "", new double[]{-0.61029078218132282, -0.4277308391499548, 0.61507727560346637, -0.9715899485416053, 0.12123001336147632, -0.4180373476074879, -0.12539877633816965, 0.30230691158680512, 0.91493904119436897, 0.022936364420055311, -0.60525877413566453, -0.37316181837628215, 0.7616519596256941, 0.45138533945337955, 0.22116311165071934}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "disable", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature", new double[]{-0.8965158576309582, 0.94446775717526954, 0.64359852585334165, -0.16077208704091217, 0.14459692070075025, -0.98682871279350803, 0.18256162746995397, -0.98940455622569146, -0.86870780080231436, 0.26744455310514015, -0.69675432342278798, 0.22341924789928269, -0.48839531152945348, 0.84876779286718862, -0.6700019564498918, 0.98099778492322232, -0.12431107964735011, 0.46608471978745314}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_decodeBase64", "com.fasterxml.jackson.core.Base64Variant", new double[]{0.0081050272377378718, -0.027592535443315969, 0.97245942383848805, 0.27932010381167593, 0.17654636780124355, -0.68437329517750567, -0.53759052683952246, 0.6633201910376203, -0.41949698602501018, 0.96865022643413701, 0.67430056837111962, -0.27382044578794451, -0.027692829292715437, 0.13286219440124736, -0.0053640329435715728, 0.016757599917411214, 0.94744456139654409, 0.50112266930991911}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getEmbeddedObject", "", new double[]{-0.06189755936782837, 0.49373587934783147, 0.83141204982072447, -0.9603697458245648, 0.28181107286196672, 0.8616395178538756, 0.88182390752125706, -0.50048106390201275, 0.39074108642478489, -0.77927686736859103, -0.44055905208852408, 0.43641043124511492, -0.69761130692227247, -0.73853457388104271, -0.31416198525230987}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getBigIntegerValue", "", new double[]{-0.02064387005588153, -0.89588949241556293, 0.54037790694295817, 0.27773640290700019, 0.64980826953536197, -0.29490688447657454, 0.93838160294561712, -0.87316184510178152, 0.62320400291556011, 0.42662374094465161, 0.38969620538775307, 0.11129117208176731, -0.22059748045474614, 0.91796145052702216, 0.18049323529905248}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "setXMLTextElementName", "java.lang.String", new double[]{0.09470784340200944, -0.89329850706501013, -0.57951905908098555, -0.45486811979836594, 0.84383141782599447, -0.89068741186895561, 0.9546620548011695, 0.61092975444770459, -0.68229236633603829, 0.12747302748573519, -0.82798266635542639, -0.766571282822196, -0.67705825056464142, 0.63230305043913271, -0.85823540946939048, -0.051330170394492614, -0.8531270960809727, -0.43677403020368155}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getBigIntegerValue", "", new double[]{0.18167364031676292, 0.88173713340832083, 0.19169605175126914, 0.75208841775190405, 0.30321832830952156, 0.67423272876881835, 0.44525059352459984, 0.27122093692990634, -0.52885697536398846, -0.22177922485031054, 0.47287705362210608, -0.57063682199140087, 0.33153566099534371, 0.6450760458453193, -0.7128816923455521}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "<init>", "", new double[]{-0.31641952275897256, 0.91395048028648618, 0.023293949005840586, 0.71318854480356619, 0.32826269582652245, -0.72224747891364527, 0.63332472293811692, -0.23331182547806395, -0.38659846130505415, -0.6313364865437141, 0.22805713210703416, 0.85040190137464866, 0.81694636604794879, 0.22078652814510513, 0.94504940498214518}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "", new double[]{0.61422641331315941, -0.56732694898269065, 0.31252089141325445, 0.81206393203097127, 0.60656450775568649, -0.13319954279844937, -0.43134369579331344, -0.71286088636989753, 0.67820312877969879, 0.26631545294030046, -0.57541521832441433, -0.45857838072842849, 0.83560909017761986, -0.46887065336925526, 0.58321473345081332}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCurrentLocation", "", new double[]{0.79740578371596671, -0.563034492371284, -0.94447440417365591, 0.7571516539940395, -0.28620545364888517, 0.10600959414467637, 0.90471630071859188, -0.44839511520002584, -0.73579419431189086, 0.96683941179956712, -0.96986646151829081, 0.8652928797986863, 0.57040429522300884, 0.79296894472082236, 0.36951063464445189}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_getByteArrayBuilder", "", new double[]{0.77569226407668812, -0.41745670966077086, 0.37358191174091404, 0.35259165667237791, -0.80299400366809603, -0.29851544659803464, 0.94127944292436272, 0.096603463314424376, 0.76073981576457905, -0.094356895646034777, -0.68034096586018356, 0.9565984004931245, -0.76839179393312618, 0.90815766815435039, 0.26416600209036667}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getTextOffset", "", new double[]{0.88413741970627635, -0.40851444961398942, -0.88634418924470304, -0.437794709871836, -0.50121652275307604, -0.22962996676154535, 0.54374206746759768, -0.20585093882022099, -0.81257441429389754, -0.85252217495162186, 0.65765217188848601, 0.73829644366153624, -0.13649296284607093, -0.94307653239216616, 0.63367688626620988}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getNumberType", "", new double[]{0.96256431783963192, -0.39483804025293301, -0.5073010121470185, 0.22825289118885839, -0.86171433303048062, 0.9182925718034527, 0.24530872973252427, -0.9044322745302511, -0.74145872374695898, 0.051867751636335058, -0.93959763579212763, 0.12201160592230864, -0.54454418501958224, -0.39481691950720887, -0.81586601506237511}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getFormatFeatures", "", new double[]{-0.33531948449195226, 0.3923724463633993, -0.50233171723850534, -0.85508531623947781, 0.31723731760003315, -0.75784789459855317, -0.70540338537313962, -0.7252399258055664, 0.72292392795216975, 0.77770059296490279, 0.95793895609063062, -0.78077412136788782, -0.91073202307895507, 0.52122588809292658, -0.003120726759273218}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getNumberValue", "", new double[]{0.90398255289306473, -0.61059360566233001, 0.17947252450022355, -0.87190969746693892, -0.65712359399481146, 0.52894770379101241, -0.47303010030310833, 0.73539159967207679, 0.35720765039407665, -0.99690338938055967, -0.50361421288107056, 0.37596127470613583, -0.37142897155220433, 0.8142511721925263, -0.49157301252254393}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "isEnabled", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature", new double[]{-0.74828687611516176, -0.54865541680148788, 0.72623461913080622, -0.39175851447502663, -0.72671285942109254, 0.58314862853712368, -0.79769877447308901, -0.75506978152020032, -0.79047922205998877, -0.37890263033086824, -0.10447711770125934, 0.48491369784726546, -0.58965994958662349, -0.020684699182744737, 0.37342394060537787, -0.19639240088808196, 0.1859336791153694, -0.56113248428451534}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getStaxReader", "", new double[]{0.45387698735132243, -0.45816302625710059, 0.67990100415054355, -0.79070438681358124, -0.41019205762346633, 0.53225069374139933, -0.97731787388240443, 0.39965712580684953, -0.68368627844457075, -0.95775205181629874, 0.68514905891277111, 0.87371445636094314, 0.19399531301401929, 0.70822810642124212, 0.28028225232415949}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "addVirtualWrapping", "java.util.Set", new double[]{-0.96610093772271277, -0.31223632755916331, 0.25643110743219411, -0.80578450684738212, 0.46757855795007353, -0.73620123854695518, 0.76747473939468214, 0.11251329915721131, -0.57072496050472288, -0.99381035901999604, -0.023196589558776903, 0.4254824741974752, -0.17255268456300654, -0.47222832124270209, -0.042495018594294498, 0.70578053085066816, 0.59146674096459728, -0.93763326728815888}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getTokenLocation", "", new double[]{-0.88386281466669669, -0.97973170497017437, -0.97684973183289148, 0.61687829291595531, -0.249248684511584, -0.13003572383193762, 0.23474282323653561, -0.38781826602756331, 0.46631144149732129, -0.0085710758524515374, 0.35107196975020805, -0.97587177990224871, -0.81078457165938422, -0.45317070858884168, 0.17932333114367749}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "java.lang.String", new double[]{0.31081594765274145, 0.84973141544288811, -0.22341514690746989, -0.25120062995124526, 0.32545975049560028, -0.39223081383956671, -0.28533840851347114, 0.19727268380542973, 0.91049143854125125, -0.11978924775360733, 0.29531986624279005, 0.77394798167196499, -0.74038325339004607, -0.88907012153248388, -0.98814858975208009, -0.98869971533845602, 0.1316373199281915, 0.65150195691365775}));
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
                receiver = ctor.newInstance(arguments(ctorTypes, vector, 0));
            }
            if (method == null) return "constructed:" + target.getName();
            Object result = method.invoke(receiver, arguments(parameterTypes, vector, ctorTypes.length * 3));
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
        if (args.length != 6 || !args[0].equals("observe"))
            throw new IllegalArgumentException("SQA_HARNESS expected discover classes or observe class ctor method types vector");
        String[] pieces = args[5].split(",");
        double[] vector = new double[pieces.length];
        for (int i = 0; i < pieces.length; i++) {
            vector[i] = Double.parseDouble(pieces[i]);
            if (!Double.isFinite(vector[i]))
                throw new IllegalArgumentException("SQA_HARNESS nonfinite vector");
        }
        String outcome = observe(args[1], args[2], args[3], args[4], vector);
        System.out.println("SQA_RESULT:" + Base64.getEncoder().encodeToString(outcome.getBytes(StandardCharsets.UTF_8)));
    }
}
}
