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
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.23193057415244711, -0.66062835522866559, 0.42501389475041518}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "isRemovableVar", "com.google.javascript.jscomp.Scope$Var", new double[]{-0.056066811595451238, 0.69567924302145845, -0.21873539596685587, -0.1187138677087809, -0.10187778277773618, -0.24049182263782098, 0.62590141519900588, 0.94562334772950263, -0.53850870945868179, -0.3550557466945421, 0.60766718203480785, 0.48786322709157348, 0.62861515116548539, 0.36368451887341147, 0.82988544102631923}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{-0.038106558429710446, 0.62561450000450325, 0.82718695172110368}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseNode", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.Scope", new double[]{-0.81770260118187532, -0.22515967466913622, -0.98215191717850048, -0.25086731343430801, -0.77804917486551028, -0.99635078234395968, -0.40360687987828947, 0.43340194657488218, -0.64985301920300964, 0.93907413441739451, -0.6272756678173621, 0.65858311662870817, -0.45967785675823469, 0.75158340561484738, 0.013846546056835995, 0.68173534036063987, -0.1095163882023682, -0.44036633229521849, 0.77135693704992669, -0.95932509832423141, -0.74386693522555802}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "removeAllAssigns", "com.google.javascript.jscomp.Scope$Var", new double[]{0.38439771957165303, 0.94995393674000739, -0.81180106929639151, -0.1328238114672271, -0.723267248446674, -0.78891953989614239, 0.14350856935522893, -0.62617562371459345, -0.79341539074497591, 0.16073552650584322, 0.10902029336505015, -0.0088161530306096747, -0.94969575933633155, -0.73161148309047541, 0.078405446341503593}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{0.92412531455037095, 0.42743538965452532, -0.93887857470284741, 0.36113300025644679, -0.60440037416400538, 0.12257671945383342, 0.64385333195489669, -0.87385056517735027, 0.94600883751977305, 0.66856008808355472, -0.15632754155694406, 0.060321904013995287, -0.30942912990186744, 0.67298014530282435, 0.92988020807585214, -0.067305820836609742, 0.56983967123928481, -0.79226352963600655, -0.72129237011185077, 0.8504313362355993, -0.65330651567640063}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseFunction", "com.google.javascript.rhino.Node,com.google.javascript.jscomp.Scope", new double[]{-0.10640478711231638, -0.30185419308069328, -0.095262501734924454, 0.94920411530778326, -0.29667061794959637, -0.91336968744387326, 0.62525199206421789, -0.53999635403103463, -0.96637423033087577, -0.57414521659369666, -0.59138529801703421, 0.14498552376020823, -0.80022701363926307, 0.78302878610324966, 0.42438595826055403, 0.92030486721699778, 0.82015276098512535, 0.75183640290836662}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "interpretAssigns", "", new double[]{0.75768782841814586, 0.83951623430364464, -0.78858196917680079, -0.99196213626836149, 0.89853678815164062, -0.80937526672927862, 0.42820523274070132, 0.70292420699507208, 0.39961741479337176, -0.22091331497851363, -0.054762915353345321, -0.61536392606666657}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "traverseAndRemoveUnusedReferences", "com.google.javascript.rhino.Node", new double[]{0.75286313735841848, -0.67748369195948044, -0.67159074272056052, 0.75892110301842397, 0.57967127547153674, -0.53575515767542314, -0.57188843648154264, 0.81119099759133984, -0.56998948274504802, -0.92098657863674505, -0.73429186850703498, -0.64505193098304292, 0.98339574368981175, 0.89439381858189471, -0.10877341436485}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{0.45350324318343471, 0.88248280456148809, 0.47888478317187522, -0.92819820614156812, -0.68262039806887609, -0.53242537535929579, -0.31054536257194454, 0.61175128298268966, -0.13534052395300655, 0.94745716666631186, 0.32126186087355291, 0.51698930946207455, 0.54545759994005794, -0.6371972909622341, 0.68402876248751499, 0.92509478155889791, -0.1880216086843467, 0.53081572512412389, 0.95457050767459739, 0.84237596169574913, 0.051622202062840961}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{0.75866657239553237, -0.80489085575262442, 0.84017843636402767, 0.49384351079553368, -0.85813659807320475, 0.10939443565475782, -0.82567533361317302, 0.90896594359227967, -0.76990467567349774, 0.95600192099544912, 0.46527881453349651, -0.091705587076916117, 0.66093592041013438, 0.25754314238649201, 0.61671355398187599, -0.67340123276315511, 0.70405929735625383, 0.2042973739075411, -0.87785341050998156, 0.25239067435096763, 0.41053622502733678}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "", "getFunctionArgList", "com.google.javascript.rhino.Node", new double[]{0.17796514294041632, -0.87643395540552294, 0.86402460429917483}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{-0.56843855232000684, -0.4173031010348669, -0.0092612758126817685, -0.56281809768617408, -0.27147292901490028, -0.37707046494792329, -0.29340221319163717, -0.19734522518083342, -0.30363460328233915, 0.91572419227856727, 0.73645683074078128, -0.80024941949293038, 0.90267879124729711, -0.56434763218717543, 0.1852914538918502, 0.69136887153092941, 0.5391658579995493, 0.48322740434500822, -0.637090256030032, 0.37161562170918017, -0.46120674701191877}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{0.47738566953995409, 0.67525636285122115, -0.96029799897395951, 0.26945811835773292, -0.0021377568161216054, -0.63529215872684852, 0.40900035583542693, -0.687395618614141, -0.21529993715863371, 0.42719187367475486, -0.53581003616695111, -0.4878198275395047, -0.11265716480771171, -0.15478080291726881, -0.47983584129193368, -0.3895136468727638, 0.65538978180205332, 0.84394043575671329, -0.035521380836618821, -0.92187110479667678, -0.46225850466167828}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "interpretAssigns", "", new double[]{0.59638599317877872, 0.72760077543229196, -0.055490246011446587, -0.9255808117057045, -0.67729090219349941, 0.58557724159912161, -0.97922534790254567, 0.62002153775818925, 0.73473891324679363, -0.3274172811352245, -0.59676332014331579, -0.39214978030705328}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "removeAllAssigns", "com.google.javascript.jscomp.Scope$Var", new double[]{0.46206819445493119, 0.3283043974151667, 0.87798624978563344, 0.7840635511466596, 0.90460250473980919, -0.70064569885589756, -0.55190548641686621, -0.43158226580981429, -0.18806558715938748, -0.31712727962500442, -0.12209637832053954, -0.98266375004600537, -0.32217646422759549, -0.22774550889466516, 0.0081666589048479121}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{0.89145228029994694, -0.8737364399798655, -0.44879383618915902, -0.82385673953694116, -0.19165805434806793, -0.53282811328803459, 0.89793401269906203, 0.2861295499318206, 0.9550640188517785, 0.74628830576504979, -0.22233904081534406, -0.73018342042783924, 0.97722420645430863, -0.28357405502759914, -0.92250488032663713, -0.042300181189498076, -0.72516027714882947, -0.42501548160636582, -0.89306215819477841, 0.90895671632156816, 0.53356655783928897}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{0.91900443082482952, -0.2454507935083472, 0.52127294160556414, -0.91332406355081952, 0.31829340757317692, -0.35659068416121098, 0.68875580185073826, -0.30688304820803758, -0.24029961272635814, -0.66988731169405002, -0.9641837834471787, 0.031403481753594864, -0.34207035875848968, 0.38359573939409919, 0.035580169647624782, 0.82784927044760304, -0.9174156690782076, -0.048435006687306492}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "markReferencedVar", "com.google.javascript.jscomp.Scope$Var", new double[]{0.97386870218254229, 0.83518739233115258, -0.55057347494588771, -0.24901238881664378, 0.80708836569773612, 0.90100573096703696, 0.87383165991468537, 0.99723193752589512, 0.41019101316912154, -0.38491859107159532, -0.80246336697612564, -0.28919094921762079, -0.32327049685858644, -0.11378503857942812, 0.42831473293689859}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.67915368439839341, -0.17924529871515116, 0.45745219161630102, -0.16670580320409867, -0.70926359530185312, 0.18923096969050346, -0.59906978352759244, 0.83551439761969704, 0.74727211080373479, -0.15072843997515961, -0.909506175655175, -0.095879241983731323, 0.44553077170672273, -0.49969380223010962, 0.80553682219007405, 0.3057488986618957, 0.17543085125909519, -0.40486691066124836}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "removeUnreferencedFunctionArgs", "com.google.javascript.jscomp.Scope", new double[]{-0.74247390571773142, -0.25598248339361396, 0.37814877561519622, -0.95367656660133049, 0.41566123571961855, 0.44269295601856995, 0.82448841218519964, 0.8638547424501124, 0.38467152765229584, 0.50277599565569964, -0.75645013762906621, -0.46642312440712641, 0.75795111384951519, 0.54802436786553455, -0.048686590329110047}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.60368554675047648, -0.3094335592533255, -0.97322230758315142, -0.75997146123388082, -0.0086937839594920518, -0.27251013101453769, 0.59485905336959655, -0.06997026633490866, 0.60546458311478557, 0.92729264772752229, 0.82052970158028571, -0.857737777934793, -0.58960528260985168, 0.1101098633652966, -0.74123027109281536, -0.17784066073046856, -0.88046953382517534, 0.10434148731764958}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "collectMaybeUnreferencedVars", "com.google.javascript.jscomp.Scope", new double[]{0.4931518422067338, -0.51260774196091252, 0.027909760171503484, -0.084638238685649902, 0.84402170668335041, -0.79507660461234098, 0.44050746698953303, -0.46484396364540403, 0.95825379775121733, -0.81442475974756934, 0.26439504054654028, -0.76755026673000093, 0.67175424498135428, 0.59130543416149095, -0.35605682567067909}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.97544062551276745, -0.65250270540392186, -0.8297926794270909, 0.34373473887299699, -0.30772804075003268, 0.119076294278462, -0.55522229308972326, -0.69597476263657576, 0.45857544175916787, -0.62725018667218513, -0.21419444746949434, 0.49190976115291885, -0.51248913721472134, -0.70858298043562584, -0.94041176470551435, 0.99532849116682276, -0.96683136733108199, -0.8538369907683041}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{-0.31098649236824749, 0.76005693298955745, -0.80944406175375372, -0.53445089949337299, 0.38415354338121865, 0.32880702883117419, -0.68074834877785162, -0.74649784649826123, -0.16779257208113241, -0.31154942760022242, -0.37070932610099017, 0.31035959147717951, -0.72418814538056653, -0.044756580545210145, -0.42718936861023926, -0.52097977927070671, 0.84178934506829184, 0.095701579826987571, -0.96226514530344365, 0.64722518292712583, 0.92597049018525346}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "interpretAssigns", "", new double[]{-0.21932149484214758, -0.67506263573572656, 0.97027489879192985, -0.77159344261872254, 0.86503169659011925, -0.97099422658684476, 0.52813613264745474, 0.49903677696176718, -0.072204697480746871, -0.95539880485198103, 0.81983626225718065, -0.19975899601457159}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "removeUnreferencedVars", "", new double[]{-0.46085494927511839, 0.85991632364233039, -0.64218383237226062, 0.48636416888231171, 0.96183130512224357, 0.20519005943433322, -0.31390566172085776, -0.64388605699399437, 0.89701775377307547, 0.54842032565202103, 0.94004366746106527, -0.91845183028907296}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node", new double[]{-0.42789471545670033, -0.95724172224746384, -0.59112464322528968, 0.79501192495258999, -0.95090971767897581, 0.43432847432653809, 0.48954336360070538, -0.58148327542674871, -0.96165859462945225, -0.92216661899103158, 0.80934481810431391, -0.75940510125356586, -0.050960244913706321, -0.83352883170358982, -0.5847688630879313, -0.6807764176758595, 0.4883368861535109, -0.86623951244567809}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "isRemovableVar", "com.google.javascript.jscomp.Scope$Var", new double[]{0.92673204594706049, 0.90417723148500651, -0.26879890856718647, 0.66542483207985859, -0.35708312585934543, 0.40143552332481192, -0.74006391091072787, -0.80150664062015387, -0.60568948318858329, -0.77933762059659739, 0.75322675357524194, 0.84437715739701336, -0.18624560731627704, 0.17087124570907819, 0.17362070593235268}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("exception:java.lang.NullPointerException", SqaProbe.observe("com.google.javascript.jscomp.RemoveUnusedVars", "com.google.javascript.jscomp.AbstractCompiler,boolean,boolean,boolean", "process", "com.google.javascript.rhino.Node,com.google.javascript.rhino.Node,com.google.javascript.jscomp.SimpleDefinitionFinder", new double[]{0.68907497274044061, -0.54514332825578782, 0.43083637401438102, -0.59981175411027032, 0.016956887653048813, 0.95421720010654099, 0.40882908240747695, 0.45467957460002428, 0.013248608753034796, -0.37107057692143908, 0.79253255337034223, 0.10076675567363647, -0.71512022284881582, 0.84190864583632163, 0.20732986119298591, -0.15970955489195404, 0.44333794187408504, 0.053323398629332441, 0.47702272057809258, -0.13739542161383622, -0.9894836096283508}));
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
