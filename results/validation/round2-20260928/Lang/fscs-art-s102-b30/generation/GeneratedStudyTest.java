import org.junit.Test;
import static org.junit.Assert.assertEquals;
public class GeneratedStudyTest {
  @Test(timeout=10000)
  public void generated1() {
    assertEquals("value:java.lang.Byte:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String", new double[]{0.23193057415244711, -0.66062835522866559, 0.42501389475041518}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:java.lang.Short:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String", new double[]{0.055293009405522842, -0.20312547505984835, 0.87541208377393009}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("value:java.lang.Short:NjY2MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{-0.96304950195694539, -0.09554478487794138, 0.98930809070094106, 0.66606765917338406, 0.97875982970856268, 0.058378662565180761}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String", new double[]{0.37819345299037721, 0.95678803630640608, 0.94455460946833392}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:java.lang.Double:OTg1LjQ2OTAzMDE0Nzc2MDI=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toDouble", "java.lang.String,double", new double[]{0.75880069910296233, 0.11602159400727974, -0.64032158202602241, 0.98546903014776022, 0.80104242154634453, 0.49747174959193163}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:java.lang.Long:LTMyNzY4", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[J", new double[]{0.12624821663825592, -0.63808562999606644, 0.88241364422984003}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("value:java.lang.Double:LTY3NS45NDU2ODI0MjIwNDg5", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[D", new double[]{-0.67594568242204889, 0.98584609537283541, -0.44278481970304262}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.Byte:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[B", new double[]{-0.92385666634700714, -0.060896351091694445, -0.47103070305443895}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:java.lang.Short:LTEw", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "short,short,short", new double[]{-0.87967828237632606, -0.6537835696641443, -0.74242963070718271, -0.15274239221694708, -0.32085340773255888, -0.45464792044170133, 0.54967057249544449, 0.87390709232190922, 0.87594629153294523}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("exception:java.lang.IllegalArgumentException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[S", new double[]{0.91920247368690444, -0.59377799022618394, -0.85567911262472607}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("value:java.lang.Byte:LTc5", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String,byte", new double[]{-0.95328320387746479, 0.91772377012025497, 0.79457209146469276, -0.44314045313352945, 0.054614624296899228, -0.76603082339389905}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:null", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createDouble", "java.lang.String", new double[]{-0.98162668565096611, -0.74584636951008521, 0.95789076468411172}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("value:java.lang.Double:NjA1Ljk5MzY1Njk2OTgxMDY=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "[D", new double[]{0.60599365696981056, 0.94311505312892607, 0.8612050076012161}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.Byte:LTkx", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String,byte", new double[]{-0.3949385839045767, -0.42699939646705709, -0.94904161481753535, -0.034732872242312585, 0.59934650266831069, 0.23609890888957374}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String", new double[]{0.92368297316534731, 0.24726538162849243, -0.5469630893428854}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("value:java.lang.Float:MTI2LjU3NjM3", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "[F", new double[]{-0.04342362691763757, 0.96733804106028676, -0.021863054249898095}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createInteger", "java.lang.String", new double[]{0.49354999593045989, -0.75353270600974254, -0.60947225581860431}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("value:java.lang.Long:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "long,long,long", new double[]{0.49580167076397097, -0.42048951354839414, -0.97641035723244429, 0.22905229379415504, -0.50505401363652069, -0.69476487324885738, -0.88102268830722541, -0.35931703016113037, -0.86665409161031315}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("value:java.lang.Long:LTgyOA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "long,long,long", new double[]{0.95457050767459739, 0.84237596169574913, 0.051622202062840961, -0.082825329163203065, 0.46922742993342248, -0.8409392613259683, -0.55232274615247379, -0.61150220663624633, -0.12354201357029937}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("value:java.lang.Short:ODY1MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{-0.99299811614653999, -0.67853273401723047, 0.1820182136335724, 0.86501585299772676, 0.95766313413813298, 0.81834684363712862}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:java.lang.Byte:MTA=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toByte", "java.lang.String,byte", new double[]{-0.23162125117156673, -0.73931195429845298, -0.35342156475437214, -0.25104900708801647, -0.11854167812094341, 0.192155791528694}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "createLong", "java.lang.String", new double[]{-0.52584739797930191, -0.25136945783419495, -0.94700282496530108}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("value:java.lang.Short:LTk1NjY=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "short,short,short", new double[]{0.92226478769584141, -0.65860549423755477, -0.056440584061172139, -0.95658303725301175, 0.51080409482081635, 0.98138737648828811, -0.34271011272425955, -0.6760447881423497, 0.13505661858788942}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.Long:MQ==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toLong", "java.lang.String", new double[]{-0.85031746965309352, -0.735071530041147, -0.18892372491188647}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:java.lang.Short:MA==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{-0.9346867988827261, 0.97042906907132309, 0.078797641498781879, -0.41260064013612863, 0.92336865309131788, -0.2808601860148725}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Long:NjkyNg==", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "long,long,long", new double[]{-0.8674311172652136, 0.89245068457546339, -0.37533434482710759, 0.69262817603482452, 0.10025095956581365, 0.11353549642854799, -0.2038052171019249, 0.72452833147328621, -0.52914221773314041}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("value:java.lang.Long:MjU1", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "long,long,long", new double[]{0.20449375576437245, -0.99858838901774605, 0.0027262440051427639, -0.62391208006741516, -0.80612039289814019, -0.87828309609939126, 0.12233434237382945, -0.10285775707056111, 0.85065361182883481}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:java.lang.Short:LTI=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "toShort", "java.lang.String,short", new double[]{0.51660411431363884, -0.17681893780098945, 0.2688611034508126, -0.4423144793495557, -0.37016898299449386, 0.91886697062896894}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Byte:MTg=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "max", "byte,byte,byte", new double[]{0.17091109910962943, 0.41279724741728741, 0.7353128567528171, 0.17724257038128943, -0.68786129658598716, -0.88199710710638968, 0.51383444149642044, 0.59698133773913487, -0.44161595428843703}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:java.lang.Long:LTk4OTI=", SqaProbe.observe("org.apache.commons.lang3.math.NumberUtils", "", "min", "long,long,long", new double[]{-0.82253337692121842, 0.7515534960290513, 0.40782860627179818, -0.72120889274289457, -0.86314448831498436, -0.42787561819528053, -0.98921012403474906, 0.16721110565526454, -0.87920339813831339}));
  }
}
