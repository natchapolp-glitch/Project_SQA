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
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "java.lang.String", new double[]{0.66590947354708863, 0.45803966823341336, 0.82928909475281765, -0.46043473069732355, -0.37045971698347407, -0.57359663273266814, -0.52191718761276218, 0.71039957628821593, 0.19781276765747477, -0.53069739928378745, -0.49621752434238758, 0.71086565841001481, -0.67208203741790584, -0.30835671335960285, 0.30024953046113839, 0.7585033920445543, 0.78813956625265424, -0.30385214520275933}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getFloatValue", "", new double[]{0.72636126217288544, 0.51087477187740371, 0.66163265129573756, -0.61116365894584956, -0.6521928944867218, 0.0058964401248932582, 0.26354434127739718, -0.45589316586317874, 0.79367696683281352, -0.99369976831238582, -0.061978994107967769, -0.54903435929521083, 0.25393093846270959, 0.30325781717188682, -0.19900075226792249}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "requiresCustomCodec", "", new double[]{-1, 0.38923445281966829, -0.76325446625780369, -1, 0.27178640073623811, -0.79520130025219837, 0.27430036163934651, 0.61656543028135391, -0.66927083313095648, -0.76499691609893472, 0.62695280847735346, -1, -0.7850396798941025, -0.53032789279071368, -0.51770017297130644}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "<init>", "", new double[]{0.52106353457184451, 0.4377737687138975, -0.39717786622146456, -0.027040768233070524, 0.91094667782099037, 0.38631901537418206, -0.15264315247666979, -0.83746341027498661, 0.28477708035222149, 0.11327712920975604, 0.61074896830476733, 0.42739463154248336, 0.53642830541779363, 0.030971614560335842, 0.24409916344278193}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "enable", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature", new double[]{-0.61492945583721614, 0.016451544182311924, 0.38691521573118937, -0.28868213886604432, 0.94646078169459547, 0.20131234958452696, 0.64044855444759596, 0.39363532710196519, -1, -1, 0.27045988225061912, -0.26937884170222592, -0.33567425927367506, 0.30570041004511367, 0.20184972276381927, 0.14877106628699838, -0.57736673033200481, 0.82045958929981899}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getText", "", new double[]{0.023077724888272519, 0.16852466938758731, 0.85178601545546928, -0.4028636675486596, -1, -0.42278628988583694, -1, -0.44031718489737826, -0.51941031617868361, 0.24903600263667564, -0.41056811449385672, 0.17220455960984593, -0.95206336371672895, 0.1419253215691843, -0.15720361529355009}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "<init>", "", new double[]{-1, -0.17494384295644252, 0.27070669836112876, 0.56675650468942673, -0.33756113392432174, 0.0026923999637000853, 0.32730893608386846, 0.79196200442305997, 0.045022838105293263, -0.14805838790895745, 0.75048227767732145, 0.61399432939529197, -0.31605002993490444, 0.21361575678414924, -0.064388765649605398}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getBinaryValue", "com.fasterxml.jackson.core.Base64Variant", new double[]{-0.39211338418737302, -0.18590236012338116, 1, 0.67168422706608044, -0.64708300884038361, 0.73698768774142087, -0.19316466200026236, 0.81071383902249716, 0.44204507282359445, 0.79087190855236822, 0.61251238644515194, -0.24295561146200687, -0.32843363031835071, 0.12906200712853214, 0.18343469008746752, 0.7354293006736683, 1, 0.17199211974411074}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getBinaryValue", "com.fasterxml.jackson.core.Base64Variant", new double[]{0.098027987629407579, 0.43356865309848763, -0.098757274068388454, -0.40442545172093824, 0.041817221465168017, 0.71125947920886989, 0.58175418966666026, -0.36823715281021518, 0.58740240625207663, -0.091069927165182737, -0.24227206222262337, 0.86205014156420334, 0.30724180123949268, 0.11578398620455005, -0.0045456745531381529, -1, 0.57384091823682759, 0.90153989851216054}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getBigIntegerValue", "", new double[]{0.23351654859173449, 0.34443122209773869, -0.48785981153949515, -0.61010764145368057, 0.23871166726464293, -0.79970646440096582, 0.75330299588945582, 0.42827745797742672, -0.073882431720565744, -0.47625154306153283, 0.97258144903526911, -0.041970271052441797, -1, -0.55317534642915633, -1}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getEmbeddedObject", "", new double[]{-0.29660724130663391, -0.10031824597580928, -1, 0.035170746971268256, 0.12624134422157623, 0.19375447155677999, 0.3745519002863163, -0.47411583832544346, -1, 0.38409662095745506, -0.83284251657844355, 1, -0.21745051842328189, -1, 0.50310931919235269}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getLongValue", "", new double[]{0.046260367438965028, -0.44999400812803575, 0.19243325913023804, 0.67095388853800675, 0.28869627137605913, -0.13613597381319056, 0.23501645859539808, -0.73689091033728449, 0.52526017317843454, 0.92343752266104939, 0.50467432105157795, 0.58615790934481027, 0.33811507504785504, -0.79193074139988573, 0.88031875235082813}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "isExpectedStartArrayToken", "", new double[]{-0.72290694574733039, 0.21171041281706099, 0.55219075635975456, 0.51707036338276635, -0.1525552969057381, 0.20169027119862598, -0.28347862287515746, 0.70615269300607897, 0.062792418356925672, -0.16693165739331225, 0.79015430690530752, 0.021088547783395077, -0.16763597529392721, -0.58038485267917739, 0.0024655529508379819}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "nextToken", "", new double[]{0.15432379398686713, 0.22318846374237294, 1, -0.1997578412521494, -0.39411190172670141, -0.52070695790045174, 0.16413189010637724, -0.16838759076783355, 0.55409907365258459, 0.04352401397189587, 0.40743572604340161, -0.55550276772050389, 0.541390214891736, -0.063653663203527416, 0.92897223834636178}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "overrideCurrentName", "java.lang.String", new double[]{1, -0.097123760142335036, 1, -0.86903535296108614, -0.27558502075653002, 0.88585697963545607, -0.15535704743735543, 0.19017326460859577, -0.61566602552365923, -0.6041022870408842, -1, 0.16579711882817241, 0.079045129048592344, -0.062893008901189401, -0.55540947489655723, 1, 0.42151340715751967, -0.6476990538270162}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "isExpectedStartArrayToken", "", new double[]{-0.17375090442140051, -0.75435351026755781, 0.5227686633615598, -0.79781271839468348, -0.32108105567126377, -0.1662655176918946, -0.39787008818537345, 1, -0.59604845028690967, -0.49781011291457866, 0.31762159394441802, -0.1762415032760023, -0.91544366359953333, -0.77927820115950663, 0.67857242491874992}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getIntValue", "", new double[]{-0.19327755164730692, 0.97605836649454603, 0.26572204043653447, -0.13018733660687021, 0.16837997257485307, -0.66351910177463569, -0.12706129831210547, 0.26401165194340709, -0.37031334290557222, -0.64957603103331085, -0.44382205311898681, -0.81432880562438759, -0.27207401353148525, 0.011117702436515678, 0.46163659624449532}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_releaseBuffers", "", new double[]{-0.14059711952348736, 1, -0.43950391989441073, 0.22686654801271, -0.55013431276476654, -0.4996296925092083, -0.61548292066811916, 1, 0.91375105061757345, -1, 0.22293889009392651, -0.89445693928957248, -0.60775733652320751, 0.01620893836731542, -0.23062481752161626}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "isClosed", "", new double[]{-0.29271277404137791, 0.96661902225252316, 0.32648706090988699, 0.13713936649915182, 0.52447024389273111, 0.8163538768085572, -0.54926702582328724, -0.40604725275242343, 0.26999557320836448, 0.16963103563663673, 0.74491247512823666, -0.033768755651710905, -0.010306409484688805, 0.93825408020265466, 0.7892095398586052}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getText", "", new double[]{0.90867259443977799, 0.91052078446284734, -0.774101923821222, 0.090800016031181374, 0.2841178102432742, 0.29119438411929605, -0.59154726286062187, 0.2958756809527015, -0.32891316593284498, -0.71306139363777521, -0.030203203899585827, 0.23222441509888714, -1, 0.44833037013055294, 1}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "<init>", "", new double[]{0.44305497518359416, -0.19381811193158904, -0.16894888559468646, -0.75685916129586961, -1, -0.052330356330070105, 0.43198074128873382, -0.74969770022447624, -0.47876013352599972, -1, -0.55220396271813499, 0.47181903928055963, -1, -0.11208091361584172, 0.37767211855722216}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "<init>", "", new double[]{-0.00083677835156908964, 0.70980527824277884, 0.90087096712732473, -0.31822457671255711, 0.36714266976528243, -0.36705412499990447, -0.91259644253703109, -0.57479776448914599, -0.67234189084809937, -0.19940565512609387, -0.044688984953109655, -0.22256679563310039, -1, 1, 0.31556669997943143}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getText", "", new double[]{0.36434953780167934, 0.32103936195080707, -0.36104360355856036, -1, 1, -0.50940685766130889, -0.67380262129326296, -0.11484593129801007, -1, -0.25146655726877282, -0.41396145538102369, 0.072241281295725837, -0.70921062378159583, -1, 1}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getNumberType", "", new double[]{0.47332844009240499, 1, -0.33424155977943215, -0.50707864862153018, 0.43089667645912566, 0.42189746554751179, -0.62239055752110017, -0.44328640698256278, -0.3390109728531065, -0.40239028717101016, 0.66356914085321006, -0.0086226804604176838, -0.91247552558508982, -0.61423835065220989, 0.33742391921929732}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getTextLength", "", new double[]{0.73511926964369201, 0.38902853267811521, 0.70646731325685308, -0.079632998353384832, -0.16099855750168132, 0.39616623468161166, 0.32040199515290974, 0.65258876336265326, 0.066469483556178544, -0.5417058821208256, 0.69851408866600251, -0.27357201306742468, 0.24507201154822228, -1, -0.86280090970914514}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "version", "", new double[]{-0.20491179257950215, -0.094828364696012524, -0.036929036536215432, -0.37304208958632168, 0.11614972187542713, -1, 1, -0.45734456051780231, 0.0845166098159017, -1, 0.2806631232029978, -0.34635323873234708, 0.1257189509245708, -0.4878200870174047, 0.63737668985583573}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getParsingContext", "", new double[]{-0.76605946419090021, 0.53600576790046828, 0.32592517090891315, -0.61837120034236803, -0.57613647022878367, -0.27279154626287461, 0.12148199426088194, 0.33739082396364151, -1, 0.024873492517221762, -0.51437019780291715, 0.4221366730410176, -0.37183507649250269, -0.71673964134269552, -0.31756651166554606}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getDoubleValue", "", new double[]{0.027608267766318251, 0.34896151296783201, 0.70990311418254004, -0.48006699819569104, -0.03749830599119261, 0.35403970150301683, -0.093815031600563203, 0.026904897132832228, -0.21743755590906691, -0.0043606680141472509, -0.15140304452430031, -0.20871317401044179, -0.25415969386222909, 0.45240517801812552, -0.11457620725880796}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getText", "", new double[]{0.79074654547733658, 0.42813329158991376, 0.27552626848032941, -1, -0.73082918052347523, 0.80932399154222534, -0.074953988181326078, 0.17784522137178244, 0.20075557170233088, -0.88651818296598295, -0.080469454615571367, -1, 0.57339307618599755, 0.72410469906943309, 0.39338711252341924}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "close", "", new double[]{0.6609783986932366, -0.52233330995278204, 1, -0.25337137034102408, -1, 0.34047293822643293, -0.23348303522224023, -0.5409878112950014, -0.16232482707303594, -0.99243386450620386, -0.70275575416511749, -0.32807419886540212, -0.65210541951677992, -0.52629895600215337, -0.082301631400671649}));
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
