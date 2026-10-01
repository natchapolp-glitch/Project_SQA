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
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "<init>", "", new double[]{-0.087403447416303459, 0.43769406820047285, 0.46977248517067904, -0.57019666300587324, -0.84392406176975188, 0.20548647253427887, -0.75616099455463548, -0.235546731037382, 0.77940564361408771, 0.74430058412690125, -0.76262190209165159, -0.83553208213848285, -0.55502784873171396, 0.65618222911565893, -0.95055104726464834}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "overrideFormatFeatures", "int,int", new double[]{0.83912651510651481, -0.63619806963829006, -0.27313023016473226, 0.63370563979327565, -0.93483540863911641, 0.22898160872225781, 0.74263318868543027, 0.88798956064889678, 0.36436467598353994, -0.30888022283196337, -0.33541338671305443, 0.26228499037568032, -0.86655509703096478, -0.94373258021961348, 0.76331985569044059, 0.25380277237250026, -0.67427808924914734, -0.14252075814192966, -0.42184886676974265, 0.73857490438093576, -0.42751810554968572}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getParsingContext", "", new double[]{0.087436382762479337, -0.82301400276114767, -0.90408353948070186, -0.7567250725309933, -0.36253660119363973, -0.51353696884213251, -0.71424191121575542, -0.32309315832829388, -0.32629185932969595, 0.6483456769921756, 0.87034673878072111, 0.98758062590746998, 0.47463475836083857, -0.97752681177011214, -0.52397759138456879}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getNumberType", "", new double[]{-0.14833665099431603, 0.75108509696296921, 0.44665300739577551, 0.11704092534003974, 0.27377718022464226, 0.82773394071721484, 0.23860223203934372, -0.57815931772627804, 0.77811773610273471, -0.91194655468138497, -0.72089714093268498, -0.61217360840898727, -0.23460363259699846, 0.1513705786306796, 0.65272591325507379}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "nextTextValue", "", new double[]{-0.69266748437299475, -0.96220948241492232, -0.4211275497066016, -0.72132153943246347, 0.15208346534056916, -0.35013308531831933, 0.644645743035269, 0.56245168270534673, -0.24651059500951544, 0.83082575202300979, 0.74330585369217994, 0.87729925009498744, 0.91991235983297837, 0.78910300743310091, 0.87947747645893304}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_decodeBase64", "com.fasterxml.jackson.core.Base64Variant", new double[]{-0.59939835931271968, -0.21522543943821848, 0.1492229050817846, -0.92282705705137325, -0.75410717226372781, 0.37256242251082794, 0.49478239821626224, 0.21430572075335963, 0.36191293535804281, -0.19976456393509778, 0.80164628822202211, -0.7280883029084102, 0.5954916215708943, 0.42807515941135588, 0.51953611002849343, 0.94212788178288664, -0.44043170471851689, -0.30624747140371733}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getBinaryValue", "com.fasterxml.jackson.core.Base64Variant", new double[]{-0.9613262935206135, -0.56659813101402756, -0.97402400443264092, 0.84791769335985423, 0.50527582920234471, 0.98249581404261344, 0.30903983092749177, -0.52919955559244269, -0.55962143737855308, 0.65769659666799796, 0.71159547324631389, -0.76351762694761094, 0.36175285714827932, -0.3196127887919884, -0.7955976202447177, -0.57044841061319462, -0.21613220838791425, -0.78589985097872828}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_decodeBase64", "com.fasterxml.jackson.core.Base64Variant", new double[]{-0.099427091662782408, 0.8751546562630268, -0.010313296933422977, -0.59626668036855368, -0.054824100262841124, 0.6919620312036312, -0.06388132816528147, 0.91907077186527752, -0.35510655462020613, -0.50158324908527696, 0.95961306762242748, 0.9443296083756767, -0.052992970951414753, -0.67964301923406212, 0.74422101781393879, -0.85936185656000097, 0.93630595097501312, -0.83500778568019607}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "nextToken", "", new double[]{0.79376001310341304, -0.10372456432011146, 0.46527511825177359, -0.91779691443732037, 0.60291003693273781, -0.19085772867650053, 0.28907099842504569, 0.4634408839273505, -0.7705503697450713, 0.31393011209431432, -0.16994242347554978, -0.17556745866633605, -0.30566955867292678, 0.7303858193589019, -0.98801668913048513}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "java.lang.String", new double[]{-0.31834666765325714, 0.35665123321005021, 0.89421396568353284, 0.79996450156792043, -0.71943041854137357, 0.99664736858528369, -0.87123696871330947, 0.62357582132730593, 0.80930967973999524, -0.68526428194765909, -0.57965402064863425, 0.69746735136255622, -0.22670260554778254, -0.15179162932383217, -0.29441276329653343, 0.93018804351968498, -0.88278086626176688, 0.77015955316142959}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCurrentLocation", "", new double[]{0.76014203427636562, 0.88973457199010264, 0.8149021650350099, -0.69112109919662346, -0.042877128239918294, -0.37427218987905775, 0.64801132046012655, 0.74008914800174042, -0.49365766679478296, 0.78701654043789482, -0.98010565589327969, 0.98900077379787277, 0.34046990907892916, -0.27382471233320449, 0.31825506569600415}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "java.lang.String", new double[]{0.57006878763729119, -0.3233013519421557, 0.61077828823122959, -0.81145511569078943, 0.11986350696664672, -0.82605765765430705, -0.48997704189804137, -0.053757111964814808, 0.64842180869092769, -0.67880896178239358, -0.99626013747771025, -0.80282540656009838, -0.56552115804724257, -0.48137282137124071, -0.71852830846445559, 0.810447869015682, 0.99868333130394338, -0.75181253244972424}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCurrentLocation", "", new double[]{0.46738162700946417, -0.74698587410352424, 0.88454678621053584, 0.49319847697748975, 0.31767152833435564, -0.36028657886680171, -0.56681131741929658, -0.87520422874163772, 0.81955582957445405, -0.49536095908260713, 0.92442607743804017, -0.91234946825777796, 0.29412610231720193, 0.10205645400189023, -0.65120571131827076}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getEmbeddedObject", "", new double[]{0.62804710780997564, -0.23564862158613953, 0.78724508855786435, 0.68576912536534862, 0.16737723171955143, -0.45858543809821484, -0.43002633313690608, -0.56441875656506291, 0.011458217631675094, 0.24450283994752509, 0.21657921976285643, 0.61612024143498645, 0.8014946276591659, 0.95204061765157033, 0.87338196603291052}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_getByteArrayBuilder", "", new double[]{0.00067297420036016575, 0.68835194725039361, -0.4455935227915635, 0.0024410289074843217, 0.032302886171538647, -0.86988609020710594, 0.9728200317225173, -0.076654133380858802, -0.59167874373972529, -0.80885307683195062, -0.46382029299104532, 0.52453628907629546, -0.83314195626861398, -0.66104570734156098, -0.62152845238240517}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getFormatFeatures", "", new double[]{0.51208161176662137, 0.93876276072625986, -0.14572082480636306, 0.7878219339859418, 0.6237137844277949, -0.15685959016763062, 0.84426975673813143, -0.15368459415884228, 0.91353776944861886, 0.98916798422504826, 0.48910569238749879, 0.17444907403474774, 0.76829562054342904, -0.70888433027328235, -0.16072863691065487}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "overrideCurrentName", "java.lang.String", new double[]{0.70444256072075806, -0.28214696857449728, -0.39212625364063913, 0.48270870181061931, -0.12688531933004565, 0.091035895003026779, -0.70629385009743517, 0.58392727700908931, 0.91971777145757883, 0.87049558957895345, 0.63544062002429125, -0.9056856983227084, -0.16771819194921833, 0.50106626957424627, 0.091102077405686099, 0.53820132564019607, 0.72085501146923692, 0.60123446301886774}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "addVirtualWrapping", "java.util.Set", new double[]{0.34297180054048759, -0.92730520180244436, -0.43393949267569853, -0.16601585601104474, 0.63449241610406193, -0.66192203639060643, -0.63363518633172244, 0.12242126034042555, 0.26816678737341904, 0.12334292498926724, 0.37430056639700005, 0.74191664467925333, -0.98763871052024688, 0.64424642927159814, 0.74909649934910272, -0.87608564942089928, 0.25077232828602058, -0.53675259367340611}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getLongValue", "", new double[]{-0.92083576508591491, -0.035857287308134866, 0.69609842650818865, 0.70265592447119629, 0.64474488483134951, 0.79979363373787304, 0.87532208883367524, 0.34692799473710734, -0.57666196228547184, 0.88146998525256359, -0.58488828410330562, 0.11284991197553396, -0.033419015653149753, 0.29513596263267416, -0.98275950049411698}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getDecimalValue", "", new double[]{0.27111076003004908, -0.89995498354954373, 0.31189386114300555, -0.82388688983082825, -0.060849654799072894, 0.46556132101500802, 0.62766184858319196, 0.97016033393486123, -0.67427675854012881, -0.26056003034981501, 0.27437218325537871, 0.90517030780346386, 0.077145879715131205, -0.6516445142101035, -0.97649588208581983}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "setCodec", "com.fasterxml.jackson.core.ObjectCodec", new double[]{0.13187122003520813, 0.060522410014017147, -0.89855304113586909, -0.039207339454417234, -0.22842302393989411, 0.54553179488895975, 0.43155962838216677, -0.91920677543437845, -0.26246821055810021, 0.65750464538173192, 0.43630483968278422, -0.5435941007710785, -0.87862350985994331, -0.95984155386612713, -0.039023918184964979, -0.93263047600615612, -0.45710618861087271, 0.17478893908271376}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "overrideCurrentName", "java.lang.String", new double[]{0.72450378074494348, -0.16873331503608835, 0.77707826292190929, -0.13046446244731813, -0.93529796946635013, 0.016907703847917954, 0.93965637053739193, 0.15240105116791192, -0.38628786109188251, -0.62011863892902519, -0.21218390485042371, 0.081999888714408442, 0.028777343701167801, 0.60415982215932851, 0.36394656113137436, -0.51002756947431171, 0.18077235560369442, -0.87991898255390977}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getDoubleValue", "", new double[]{-0.91008871147354742, -0.83181874021589941, 0.80321654716362234, -0.073895964699079997, 0.55670265530082363, -0.53959694161405669, -0.6219737545821773, -0.45714158412050132, -0.39774461070452483, 0.53237627181813019, 0.03983214308329508, 0.40758844487465673, 0.058345311813994716, -0.77106524618919425, -0.20996846008257997}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_releaseBuffers", "", new double[]{-0.98129670430065619, 0.23793467629069776, 0.92614322241513736, -0.96139937646506679, -0.29211975195640427, -0.55972862252131605, 0.99493827750021802, 0.74692709318309425, -0.49037017368607749, 0.27352873241435427, 0.99614179082447052, 0.95290131548335766, 0.24123088537691673, 0.084634448141648999, 0.89979807318177518}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getBigIntegerValue", "", new double[]{-0.52073498582280431, -0.82641178798435089, 0.023032009189961622, -0.38885887589467516, 0.14965569089752462, -0.04096358353607954, -0.72633346449439751, 0.52859503906376126, -0.83037586839110111, -0.79648686396536084, -0.64093382095419127, -0.92426129241985833, 0.38816028572603178, 0.37978631247620021, 0.75219418337262534}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getIntValue", "", new double[]{-0.89557642188104536, 0.95208864633686563, -0.96384604919360872, -0.94071896711602476, -0.62916293694153058, -0.038345547538302771, 0.38503474112110614, 0.28868441826235647, -0.31249830756093511, -0.039834325745327748, -0.23574242519331268, -0.93629946777937856, 0.16659551829246433, 0.76908033565517164, -0.19262606967064588}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getBigIntegerValue", "", new double[]{-0.77346673545951883, -0.69175813304445177, 0.12245683246575245, 0.92771094018855105, -0.086153134266008902, 0.86084093799766936, 0.54552570529429567, -0.17163921717737129, 0.87313992268227847, -0.7412354046453371, 0.088783764300476964, 0.033604774359800693, 0.0045896550289650229, -0.17759983422975822, 0.85338019921156416}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getEmbeddedObject", "", new double[]{-0.80312884982002752, 0.04263073314873278, -0.80088524528341209, 0.10986747681727604, 0.54837280337242333, 0.92891301464796694, -0.9132977417631265, 0.30004001270383096, -0.86891761584907434, 0.66547799927384133, 0.31594304622413061, -0.9598718523580072, 0.82447317535473541, -0.6069150470199145, 0.69418487306087773}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "isClosed", "", new double[]{0.11093187032037721, 0.85196653717005533, -0.056761328790333243, 0.91884599938148526, -0.88420572911632678, 0.64146687433886673, -0.55866770899607543, 0.88114623917085622, 0.23336856505120629, 0.62611350054080583, 0.46264714613277214, 0.31530390723027857, 0.77202105555321032, -0.35810793355676629, 0.39941577509616932}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getEmbeddedObject", "", new double[]{-0.51048881490387044, 0.89900943286314217, 0.807448316496159, -0.95146340572447041, 0.8988204627674059, -0.13372364116897617, -0.26926873973625454, -0.49487444548188475, 0.79687350872919449, 0.57966634879183832, -0.17049416736112488, 0.38757984904429499, 0.55855381801860005, 0.60839959561633794, -0.44727934741655417}));
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
