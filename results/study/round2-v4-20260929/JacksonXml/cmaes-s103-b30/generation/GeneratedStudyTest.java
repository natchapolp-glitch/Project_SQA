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
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getBigIntegerValue", "", new double[]{-0.086937624036138444, -0.19015235631540686, -0.95901584820932984, 0.052908039485623448, 0.23601192846300303, -0.24439142138918518, -0.18407899986821655, -0.93038801527718762, 0.84951370501017076, 0.20941741692076871, -0.21705431303519587, 0.37009804347218705, 0.21028105214878293, 0.063263881649785525, -0.11841903632738525}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "enable", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature", new double[]{-0.95603439494467901, -0.90719309326957998, 0.53622340027329785, -1, -0.494475084655083, 0.0040459293084874875, 0.57588908140818373, -1, -0.70081578666644284, -0.50113769869357083, -0.46085807148078128, -0.0029363649767365388, 0.48769792318058458, 0.37512556639819078, 1, 0.96299361550790896, -0.83209298245225327, -0.92681512075952299}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getEmbeddedObject", "", new double[]{0.49625672576755975, -0.0046774982393349472, 0.27535261788000204, 0.23665043871965327, -0.13117818995662031, 0.35841915942539188, -0.8587121499689726, 0.58361920202428197, 0.37065590652482211, 1, -0.045888503638938767, -0.95092217036816717, -0.3995003944947873, -0.12795993751545498, -0.48168387187935235}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getDecimalValue", "", new double[]{0.25735876399775598, 0.83215100037503487, 0.23857085487326285, 0.52341535362575675, -0.53904954727556542, -0.76779168134462372, -0.33450071728107311, 0.077811003374410548, 0.587736522224383, -0.76187097188586372, -0.40965437410895439, -0.49845129324760695, 0.4677090632944122, -0.37078033349470269, 0.66660148570462618}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getTextCharacters", "", new double[]{1, 0.23662650335317686, 0.51885379858549985, 0.062979045034559852, -0.14100915120929239, -0.91397048212208643, -0.80059105517979157, 0.85280232423584723, -0.31312610907568278, -0.30468207493397836, 0.44495096073372831, -0.15146624000610745, -0.16363337342217113, 1, 0.21798460744162149}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "configure", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature,boolean", new double[]{-0.97742064597392486, -0.19148884403157623, 0.41531073220126435, -0.60086033173516162, -0.080177229273994899, -0.44451393946724854, -0.29794854924127456, 0.17292909312057625, -0.60222459698836828, 0.67926583082859771, 0.78172321722889715, 0.71693376794823249, -0.55589727153761115, -1, -0.35041663779105175, 0.074874623103097324, -0.033076502397980982, -0.11711722977620029, 0.0090729064389799378, -0.22369761006560127, 0.38638519770635621}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_decodeBase64", "com.fasterxml.jackson.core.Base64Variant", new double[]{0.21681961560453919, 0.29925894523433499, -0.35783594604369945, 1, 0.16485626464931935, 0.15206171104654095, -0.74320253936103509, 0.14974438981123375, 0.12771801940096719, 0.67443908661948493, -0.29624600251635902, 0.66946354790222218, -0.80876985293587966, 0.0075383956419654884, -1, -0.0095755286211449669, 0.45091429928517623, -0.93744493846438104}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "requiresCustomCodec", "", new double[]{-0.32723396633914326, 0.30519603837958426, 0.8353490226347372, -0.41054155326767255, -0.59301160500835093, -0.24291033756519892, -1, 0.26789523004211813, -0.099146375465534689, -0.19278762538973429, 0.80393788959155721, -0.1815178919580728, -0.45616377706507272, 0.60483559273273857, -0.28103971312865006}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "addVirtualWrapping", "java.util.Set", new double[]{-0.23951543124484828, -0.25585780706101652, 0.50203698315375944, -0.28186743667688774, 0.28186048742777087, 0.67686626882402023, 0.5450730041315891, -0.25757179577958134, 0.60733904014424578, -0.12184354468442087, -0.46456748007649978, -0.49297755276624744, 0.1960933537037885, -0.3991536873908611, 0.79429559257717897, 0.49361797443022087, 0.35163925568831372, 0.6890707700116796}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_decodeBase64", "com.fasterxml.jackson.core.Base64Variant", new double[]{0.095729003137862437, -1, -0.16539478562051466, -0.26169802474911641, -0.81810999048055666, 0.32528479478033084, 0.066396666378400734, 0.0035722632681421639, -0.61828121474022568, 0.8427313188789588, -0.12989575945095033, 0.44099173679774661, -0.88880139339016806, 0.855178284020783, -0.20188789579810346, 0.58408519427599992, -0.24114885350315773, -0.031638961553815188}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "enable", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature", new double[]{-0.14974819272687948, 0.65643927268109714, 0.11605103929929961, -0.54574828658776042, -0.15655007957885142, 0.46378535397527132, 0.057842583599639757, 0.69004174675856145, -0.67396950858011362, 0.1982344633407738, -0.43228311665633801, -0.19651626439982442, -0.34586456820038963, -0.47936304996354967, 1, 0.11380928704064609, 0.48600314566689073, 0.24358478483517046}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getEmbeddedObject", "", new double[]{-0.53930037878558801, 0.89784551660993672, -0.52386842928216792, -0.13451348996507848, 0.41919673910429328, -0.63440595058901317, -0.15129271301301397, 0.21594860485691339, -0.84493448372360602, -0.038011920107823755, -0.8148808189911606, -0.086391929255750524, 0.98664149399191303, 0.84866762183842026, -0.098919872794376182}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "hasTextCharacters", "", new double[]{0.12201336370550167, 0.1597738218044889, 0.09928601017494422, 0.37947883437376001, 0.36377347998110859, 0.014164959760839601, 0.33287571391607912, -0.28692122711628559, 0.46197787733575929, 0.63068955666131854, -1, 0.98032536583817032, -0.038268397379483864, 0.44129235177266085, 0.031207817372969567}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "configure", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature,boolean", new double[]{-0.20935173773967752, -0.50339489869676357, -0.085644269433217096, 0.38459776328008299, -0.0308971995965312, 0.5339186371227358, -0.50807186998320975, -0.64351624516238048, 0.17156838027362592, -0.43834982258223798, -0.0024437415212109903, -0.43590486316948257, -0.23487581319792841, 0.23860206830155606, -0.21528810897532211, 0.085084474413902197, -0.25424058833489355, 0.3407169933788779, 0.96665930777780562, 0.090380724342426388, 0.023603931946757417}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "addVirtualWrapping", "java.util.Set", new double[]{1, -0.40122151706004661, -0.27565343732755054, 0.56595482410928089, -0.16283968557541856, -0.37644579759178198, 0.4512114757771965, -0.9556640829325016, 0.79415762136647006, -0.41159105342112984, 0.055408128589040234, -0.1095584455519325, -0.28986835398649258, 0.53447338639722863, -0.59625085902606079, 0.38138316893977531, -0.30416688790019347, 0.31538268259370644}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getDoubleValue", "", new double[]{-0.13349466594221446, 0.29985042205649648, 1, -0.018696228996461478, -0.49977796978051303, -0.055855295405773261, -0.24822597601299956, -0.70989610979744366, -0.48815520086139064, 0.84284887579315093, -1, -0.18851933548707539, -0.87367302294625593, -1, 0.48929577981014649}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getLongValue", "", new double[]{0.4835491673800707, -0.93905332612464498, -0.079390333097793517, 0.68906264551478469, 0.038121769892134477, -0.88863321046928545, 0.37607793842354281, -0.4660756300191024, 0.37592892619087814, -0.72164118358437268, -0.050112401912154741, 0.065130443909206137, -0.49515233873340281, 0.33778909744908275, -0.02215221168025816}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getParsingContext", "", new double[]{0.41414586981069523, 0.056649387901100856, 0.046650289829372388, -0.46598154929205027, -0.49521949149649558, 0.46433541404327683, -0.25279896299417726, -0.66914496354775488, 0.54041035254523728, -0.60458077596391724, -0.32286797918642129, -0.11531344968642879, 1, -0.83505937934390229, 0.15637621363794654}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getFormatFeatures", "", new double[]{-0.23336112494959613, -0.42976987558901159, -0.026399224363494275, 0.42248059006993854, 0.061400974944311709, 0.31138057057264557, -0.93616088497356964, 0.41038613949067654, -0.11617224544391597, -0.50344167688520913, -0.70814032974666086, 0.21386157421677587, 0.093018388506724059, -0.58050069959951434, 0.10935635674622092}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getIntValue", "", new double[]{-0.81046065300859593, 0.19911012503566955, -0.64132899108308372, 0.63654178799154071, 0.38043590622010082, -0.091340587949011717, 0.54826937407057685, 0.3299416569547125, -0.098390887924022008, 0.98440637989289181, 0.22939980674612079, 0.53786026543944843, 0.036220705288585647, 0.077352709509425674, 0.79801658570912071}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "overrideCurrentName", "java.lang.String", new double[]{0.88791936144802386, 0.18700853899480219, -0.62718170449783139, 0.3170304360169478, 0.2737955136271305, 0.36183075744795878, 0.25884581156081476, -0.047637196915404134, 0.78565881742737531, -0.35838408438347996, 0.04822511553703604, -0.64466423358000446, -0.16937013994659086, 0.53185621160375307, -0.21364030764755085, -0.70797826774718298, -0.58606216382739884, 0.10924129232341456}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "_getByteArrayBuilder", "", new double[]{-0.19410748556548552, 0.24260022583120205, -0.24586713692697348, 0.17583241776741801, -0.53477630332337223, -0.17583405371197255, 0.58048266126426507, -0.89243949921606136, 1, 0.15009364862737784, -0.29700622059204568, 1, 0.062683723862160062, 0.61818200329483841, 0.81206598597419477}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "java.lang.String", new double[]{-0.65676731620205708, -0.35702371598205462, 0.45508222053592245, -0.68498327937563463, -0.25668411471187985, 0.68039075551907668, 0.4455222131962992, -0.38090808019629907, 0.58445072287986999, 0.31958998360425039, 1, 0.61578500397215874, 0.5262951047190032, -0.25170620897483997, 0.031730328403892949, 0.17326498507718899, -0.53873594664545255, -0.15657425732137939}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getCodec", "", new double[]{-0.6573815592741491, 0.931726352261526, 0.72295497965984645, 0.17340135313062335, 0.24718361544646081, -0.041320230980967654, -0.56366862775734761, -0.30282217567234559, -0.57879708157592891, 0.063078652754978259, -0.73732720308773148, -0.15967145963512469, 0.77372588015801413, 0.014822054829644368, 0.12140321490800608}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "isEnabled", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature", new double[]{0.37169078223656299, 0.41354515370500755, -0.75960149187785064, -0.8625550938261739, -0.39446774821375252, 0.47144851257563669, -0.54756504861940702, -0.1874186670451215, 1, 0.33407232699887962, -0.13213003394391298, 0.12489161283279053, 0.66992338226779746, -0.80972263158140323, -0.27788588477184828, 0.050848090756350398, 0.92179049796013923, 0.87957592971349929}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "isEnabled", "com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser$Feature", new double[]{-0.034689122475004677, -0.49972291457559193, 0.11828463063440336, -0.28028909621376952, 0.039992699245240737, 0.3777042253488766, -0.28202545597821566, -0.22998055718839908, 0.40884411383363473, -0.16783332649819604, -0.14304647602062587, -0.27990815385525492, -0.24238229951608001, -0.21641566452149413, -0.1862496799125414, -0.77135143414253093, 0.52899528405617779, -1}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getFormatFeatures", "", new double[]{-0.55753255985842398, -0.43620015164441556, 1, 0.53787543973298702, -0.1613367463822547, 0.18999647868816869, -1, 0.42058843564120935, 0.089332149095584679, -0.26968075187535873, -0.42171007981883984, 0.5286955689966194, -0.63166745580013717, -0.49749427225645426, 1}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getEmbeddedObject", "", new double[]{0.91540311275221364, -0.25406567411526659, 0.33865999991205414, -0.096356986093195568, -1, 0.0064480156955093459, -0.66297477271460659, -0.73933071134074646, -0.63662754008217981, 0.15868715240952844, -0.92278022166579543, 0.14574406016610192, 0.63547511959227654, -0.51723272289886391, 0.37678261945394076}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getFormatFeatures", "", new double[]{0.34739123690855833, -0.18223236640436702, 0.55640456959219886, 0.46075196942759722, -0.23194980271769514, -0.30757997510032731, -0.17339232557376028, -0.44864154828067032, -0.051201537779067274, 0.11065709159753118, -0.23300973465786168, -0.031336344027478313, -0.3013890463199555, -0.69355336453936867, 0.25657144558917544}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.fasterxml.jackson.dataformat.xml.deser.FromXmlParser", "com.fasterxml.jackson.core.io.IOContext,int,int,com.fasterxml.jackson.core.ObjectCodec,javax.xml.stream.XMLStreamReader", "getValueAsString", "java.lang.String", new double[]{0.45923441023969147, 0.21853121253568886, -0.279295334455052, 0.58109709058886627, 0.22232361546067406, 0.1787681916867094, -0.64874225384539319, -0.49416933739889235, 0.34582976352261718, 0.46957028177223753, -0.80306268155795502, 0.095903283737584499, 0.080139204065956482, -1, -0.30485765985071406, -0.50862254499549808, -0.65573158714424451, -0.38503947952239792}));
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
