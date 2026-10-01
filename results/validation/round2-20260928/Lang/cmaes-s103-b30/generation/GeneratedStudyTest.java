import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class GeneratedStudyTest {
  @Test(timeout=10000)
  public void generated1() {
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String", new double[]{-0.086937624036138444, -0.19015235631540686, -0.95901584820932984}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:java.lang.Integer:MTI3", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String,int", new double[]{-0.21705431303519587, 0.37009804347218705, 0.21028105214878293, 0.063263881649785525, -0.11841903632738525, 0.044382125342745417}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createDouble", "java.lang.String", new double[]{0.56272119404747301, -0.10137957821232831, -0.95603439494467901}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Boolean:ZmFsc2U=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isDigits", "java.lang.String", new double[]{-0.70081578666644284, -0.50113769869357083, -0.46085807148078128}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "isNumber", "java.lang.String", new double[]{-0.16817690795811496, -0.1980481460954189, 0.65862114415714479}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Integer:MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createInteger", "java.lang.String", new double[]{-0.8587121499689726, 0.58361920202428197, 0.37065590652482211}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Byte:MTA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "byte,byte,byte", new double[]{-0.24964434344920541, -0.87251372910350555, 0.68511466207888938, -0.93762671828781829, 0.3428094044683011, 0.59795106071021442, 0.25735876399775598, 0.83215100037503487, 0.23857085487326285}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.Long:Mg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[J", new double[]{-0.53904954727556542, -0.76779168134462372, -0.33450071728107311}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Double:LTIuMA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String,double", new double[]{0.66660148570462618, -0.23985059497177683, -0.47793331026552927, -0.46334439603861455, -1, -1}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String,int", new double[]{0.51885379858549985, 0.062979045034559852, -0.14100915120929239, -0.91397048212208643, -0.80059105517979157, 0.85280232423584723}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String", new double[]{-0.21958624056942289, 1, -0.34137568365581789}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.Integer:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toInt", "java.lang.String", new double[]{-0.9568017819952368, -0.22457270191658013, -0.17116371374309419}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.Short:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String", new double[]{0.63781651785499238, 0.5981472731777433, -1}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.Integer:MTI4", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{0.27858609442608717, -0.84314198865756385, -0.35897084370713139, 0.17514247213161063, -0.11066772081681014, 1, 0.17097460974374504, -0.029188419263073667, -0.20073281575792334}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createLong", "java.lang.String", new double[]{0.044713278620551758, 0.55957544311144258, -0.83078793020462083}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[D", new double[]{0.30338149605136083, 0.20077406163148226, -1}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:java.lang.Integer:LTEwMDAw", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{-1, 0.19078831420779668, -0.64275160587238189, -0.26407879728870209, 0.9463901505360931, -0.39402974989479933, -0.40082935910198803, 0.35254630094773365, 0.28879299600063041}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createFloat", "java.lang.String", new double[]{-0.24851612514434879, -0.071951536412682843, -0.44227775596221286}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.Integer:MzUzOA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[I", new double[]{0.18382697301614437, 0.56209781590139629, -0.039151610331270681}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.Float:NTMwLjE5OTQ2", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toFloat", "java.lang.String,float", new double[]{0.64789793496252046, 0.39663981888463951, -0.21702005323499718, 0.53019945459273354, 0.71854578884934872, -0.63820659396849611}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[D", new double[]{-0.018041995225951707, 0.29653162792451043, -1}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("value:java.lang.Short:MjU1", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "short,short,short", new double[]{-0.59885784363948491, 1, -0.76499504438594856, 0.10435213679250799, -0.30367486558850504, -0.23816359461161724, 0.28156082428617601, -0.084727268551884866, 1}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createDouble", "java.lang.String", new double[]{0.0048546611378553622, 1, -0.48282786349966511}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[J", new double[]{-0.22504206069958302, 0.36676771303384698, -0.87519106453054674}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Integer:LTQ4NDM=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "int,int,int", new double[]{-0.48431756177864593, 0.86206086694596484, -1, 0.37913517772465433, -0.5248658909763082, -0.30433253795728421, 0.39055218168041972, -0.59067784811606039, 0.19147994808177779}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Long:LTU2NTA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "long,long,long", new double[]{-0.56495374879646942, 0.49862193622111906, -1, -0.43934849260812914, 0.72942961076344381, 0.48366277562722504, -0.036172492909414745, -0.18472144711266547, -0.013517319479800394}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.lang.Short:MzI3Njc=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "short,short,short", new double[]{0.49805886587978399, -0.13968581676045344, -1, -0.98155591365725336, 0.020300177440361444, -0.071071114538780469, 0.11349971400719239, 0.21070187024108514, 0.63875712029238341}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createInteger", "java.lang.String", new double[]{0.40372791761521165, 0.29316188330495896, -0.19182731188890362}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Short:LTM3NDc=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{0.13352049795689963, 0.75950027046089696, -0.87812103361814731, -0.3747272810459184, 0.12571766526421754, -0.68856166308822475}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Short:Mg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "short,short,short", new double[]{0.21364046559371461, 1, -0.93576673588318893, -0.56203898505820948, -0.18664830740689237, -0.66398694912906964, 0.22649191050047079, -0.43048195943491741, -0.016745577240594556}));
  }
}
