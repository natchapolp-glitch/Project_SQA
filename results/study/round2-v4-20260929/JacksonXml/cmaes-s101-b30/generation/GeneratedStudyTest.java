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
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getNumberType", "", new double[]{-0.19275191598468638, 1, -0.29502854980607007, -0.86670299434726183, 0.18153854892674576, 0.09391060736277862, 0.41429512464883717, -0.22083770669199668, 0.89450074379313704, 0.37411400333875733, 1, 0.35544948241648838, 0.16823257361162219, 0.87231085730312385, 0.54488738352560939}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCurrentLocation", "", new double[]{0.21717172739210969, 0.85730445817292322, 0.71650494943003107, -0.37697080129172161, -0.80302803370382236, 0.4445014790358171, -0.40473810276689071, -0.45671139620883039, 0.43279433032780984, -0.73646665111255394, 0.5035220270807621, 0.6696236056587499, 0.027441509136262351, -0.53909145252019786, -0.12079846485769112}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "", new double[]{0.20070505829727012, 0.53243585678343874, 0.74023053518358728, -0.077508167005142722, -0.56432122761603942, -0.3921202415456484, -0.55710208988686183, -1, 0.011923870537382713, -0.4553092635391221, 1, -0.32926499126108122, -0.84840837512783807, 0.25126079012048497, 0.0093941525821895529}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "nextToken", "", new double[]{-0.99571928534083087, 0.38693633886282797, -0.99078136129558703, 0.23950509416686216, 0.29068450657000516, 0.25956817639741148, -0.94741527720343555, 0.50915387608824758, -0.4081749462454165, 0.25276695804981925, -0.21025390616344158, 1, -0.3317815852655342, -0.11732062694621649, 0.19129711544027342}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "<init>", "", new double[]{0.76124648573485165, -4.6455512270862728e-05, 0.40579399477970773, 1, 1, 0.42765683273523314, -0.30641914407703352, 0.72038469793523263, 0.49371923411137969, 0.071252638175748256, -0.060797673131111001, -0.7930064999182006, -0.87752891804063471, -0.75292737371422824, 0.19928163591665377}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "hasTextCharacters", "", new double[]{1, -0.21471907931710921, 0.87851295682988773, 0.4778499707230357, -0.37471399254636167, 0.39895865985604273, 0.14174545310793471, 0.31069901760123675, 0.13745628932118678, 0.39948256790261466, 0.37724703512595131, -0.44713268760754271, 0.47654779152030746, -0.45852951315237717, -0.77637662251180106}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "overrideFormatFeatures", "int,int", new double[]{0.81889869415101868, -0.04253261418341437, -0.4432496125896625, -0.60681950885111802, 0.26202710574545679, -0.050106187479729405, 0.16510812825181748, 1, 0.57822425284876466, -0.040768529426973031, -0.64359658643514217, -0.23748795719091836, -0.41132936662745717, -0.36284272727406147, -1, 0.097564133299704162, -0.6641139476632324, 1, -0.13977256882343458, 0.57721993083825507, 0.22121045864752714}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCurrentLocation", "", new double[]{0.14245789368506659, -0.24843161395786006, -0.22308711057709732, -0.02125566985687833, -1, 0.85287664555289744, 0.347521163674963, -0.085763046033880561, 0.85134085161424855, -0.59396638020620018, -1, 0.16239113735875974, -0.52121884132143448, -0.042303679198522758, -0.33544049761425471}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getNumberValue", "", new double[]{0.24773185217077776, 0.12484320837035356, 0.32906429197534376, -0.10006135265009714, -0.79200469044545263, -0.65721692046961855, -1, -0.15802941751145527, 0.052742451779413203, 0.26934194654189075, 0.67138944163684955, -0.29060678142363167, -1, -0.42926394030128417, -0.030253853371241708}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "nextToken", "", new double[]{0.037508049073048021, 0.13331535111008588, 0.1109752180079419, -0.72488596956316442, -0.61036189103624372, 0.8142893941175543, -0.033916962718022206, 0.60887936334597648, -0.023799151340420582, 0.14015956444045372, 0.15680555938382981, -0.17561465519680672, 0.71689414941506702, 0.50205195796701707, -0.043620017509802475}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "setCodec", "com.fasterxml.jackson.core.ObjectCodec", new double[]{-0.48839542454055229, 0.49607593343184697, -0.21209279322324709, 0.50115229753827684, 0.10352296251388154, 0.54102640497495369, -0.40451803306706047, 0.10536053262680273, 0.3269735749393563, 0.33104707018118368, 0.55374395277817734, 1, -0.56018379023919562, 0.049056027422870761, -1, 0.37770262109868047, 0.96924790563103014, 0.11910312223892786}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "nextTextValue", "", new double[]{0.58088760151671315, -0.45186013858035862, -0.73443136590021618, -0.23769692269886858, -0.52476886162906144, 0.37179485850244115, -1, -1, -1, 0.11905411110973055, -0.16420547481727224, -0.12422639055443263, -0.23160610738620699, -0.3058561175809279, -0.49052667668954181}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "<init>", "", new double[]{1, 0.68268027175671842, -1, 0.35644600078683591, 0.84310195246173303, 0.27097372804655967, -0.9279808518678182, -0.19650377135566952, 1, -0.12382644058220883, -0.90270323262821905, -0.13002936788223313, 0.58965976381119867, 0.035726405779614494, -0.72229099405219388}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCodec", "", new double[]{-0.11274156967005172, 0.59219623992948334, -0.31379589614733178, -0.91731590329832313, -0.69721224779627977, 1, 0.87224773564661162, -0.25822106127844302, 0.76896024449281875, -0.26165336623662261, 0.75788382159483669, 1, 0.42557370734234967, 0.77440328046103568, 0.61756799729411327}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getDoubleValue", "", new double[]{-1, 1, -0.59003308615261785, -1, -0.23082208852129582, 0.085961897194671427, -1, -0.11963782184824176, 0.37375183530623651, -0.11258915819017468, -0.048239482161504865, 0.75040502421774657, -0.10594390781315943, 0.055499450399344952, -0.45601869059111011}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCodec", "", new double[]{-0.2701455834482363, 1, 0.67369739377702353, -0.24251092467929769, -0.39797663124583332, 0.53979190576955427, 0.3809968363997126, -0.38129291192544279, 0.75701704561263039, 0.24198851167362578, 1, 0.40825137530857364, -0.47329766740690682, 0.27791130138646319, 0.0083608019853867732}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "isEnabled", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature", new double[]{-0.24756832476089663, 0.5117519100459007, -1, -0.33662556098896779, 0.97779135434921161, -0.2708781330170616, 0.73311164960550668, -0.71168078168848681, 0.20193689488120298, 0.58961394687573088, 0.094319315656539393, -0.007415963028079009, 0.52603950186373361, 0.072736164495677061, 0.64810328725851385, 1, -0.3870954008137979, 0.84360553194284615}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getTextLength", "", new double[]{0.34347319771675194, 1, -0.615901077339297, -0.37770197280662854, 0.46452225769479794, 0.48633985649546302, -0.31383696404867167, 0.18742161774914878, 0.81994128368849217, -0.54782320652294814, -0.34908769266600104, 0.37184737513928984, -0.20013537379934404, 0.32369953534526585, 0.4708283083233975}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "", new double[]{1, 1, -0.67051504559098962, -0.30194814386998775, -0.48007915036381865, 0.64727603357781383, 0.032663760349563115, -0.3514758496431879, 0.099816867370494999, 0.67438615984546035, -0.13293286723098308, 0.095440084196557418, -0.74700047666095482, 0.78736899113369452, 1}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCurrentLocation", "", new double[]{0.49359913920345977, -0.06474843299445654, 0.44916409808355079, 0.41633965377616911, 0.050524564373679054, -0.85970700776278219, -0.064438223306849646, -0.74872484940790829, 0.98865372061112122, 0.11381789812608825, 0.62624653537975161, 0.46227154734005038, -0.7493482736698216, 0.049253229554408445, 0.62778150472505423}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "enable", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature", new double[]{-0.75397268163951059, 0.97962507254299347, -0.089503507331463436, -0.41795598552726765, -0.26674519194162011, 0.37814226295542319, 0.38636438834891795, -0.4476792704728032, 0.88502790681988608, -0.91174221095796959, 0.2947164421337331, -0.26188140428954665, 0.19415786785712752, 0.27473231411108584, -0.18290436482945069, 0.17947101248349356, 0.19150918204656187, -0.54307581423905771}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "java.lang.String", new double[]{1, 0.9033605777808591, -0.035506679816397588, -0.67628660474410573, -0.22161374670583942, 0.53517243725669772, -0.096515906958949557, -0.47428865333510029, -0.19423040748177361, -0.24863264181078878, 1, 0.753414015499835, 0.10833571110763573, 1, 0.81511031491212127, -0.60048880141598426, 0.79557886921743115, -0.12232419967290753}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_updateState", "com.fasterxml.jackson.core.JsonToken", new double[]{-0.31519115778746393, 1, 0.28716917325073321, 0.26734396760792345, -0.064000899309309536, 0.66630721741948906, 0.28802065324991311, 0.089750277867367445, 1, -0.25873835046361415, 1, 0.88759309280807563, -1, -0.94146396555510803, 0.48066507699823713, 0.39053779480607598, -0.18810906132177752, 1}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getTextCharacters", "", new double[]{-0.74764714456155446, 0.63737660730257595, -0.32127516717653393, 0.083569644158701273, -0.91263784301993267, 0.73287015847035675, 1, -0.28855321611585816, 0.27042987221944836, 0.26232201982500286, 0.74112556651546069, -0.15871160194589418, -0.89907856623524063, 1, -0.2723376722196178}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "", new double[]{-0.72701217968357668, 1, -0.9334903231415671, -0.30690983203270816, -0.80684016609086229, -0.43845644438592207, 0.23219601473785137, -1, 0.67090038516952011, 0.43030675905373855, 0.011338749306212814, 0.29237165471938709, 0.58045122863766441, -0.074838465358026074, 0.28824986952055337}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getNumberType", "", new double[]{-0.78355514505442569, 1, 0.13750768011044257, -0.69717663988243572, 0.40960046634156844, 0.7095052547942704, 0.407850368635578, -0.88381771081919913, -0.045657948991691788, -0.78524697959431289, 0.71353701902733968, 1, 0.54154039922362607, 0.94027139699619888, -0.58658195713043448}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getDecimalValue", "", new double[]{-0.60771176717897091, 1, -1, -0.62124741500316893, -0.00016216002478830482, 1, -0.77201090281775575, -0.41636506466893763, -0.14565682938617086, -0.97963709889530237, 0.92851162677000121, 0.60591571756706419, 0.39329468414157587, 0.46813139110617397, 0.11498214391827913}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getDoubleValue", "", new double[]{-0.79606734953455671, 1, -0.1158812993789991, -1, 0.11323054980491332, 0.90655992471525804, 0.058527462422748616, 0.17199002082739212, 1, -0.92877587639885695, 0.71693177631336891, 0.30190223431676499, -0.34352940323004044, 0.55560693152210938, -0.020850824849461325}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCurrentLocation", "", new double[]{-0.22045516119509201, 0.66078361015832576, 0.039911632757852034, -1, 0.032261926527603707, -0.023208858875471139, -0.9315344266430392, 0.048590974013268451, 0.4529883324248305, -0.089253813715301569, 0.84690912810688623, -0.056471649551814274, -0.25466717035847325, -0.13744072666214302, -0.054367817144307218}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getLongValue", "", new double[]{-0.59444375579276698, 1, -0.77088260583855273, -0.86037159263165286, -0.53567506482877136, 0.76352871928056509, 0.05783252496930269, 0.15911046782851063, 0.60196987293943516, -0.48860716881261657, -0.2628920948777016, 0.92319531196202576, -0.52281261314654814, 0.64457226413952329, -0.95071865149972901}));
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
