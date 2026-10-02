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
      assertEquals("value:java.lang.String:YWJj|state=xml:<root><item id=\"left\">alpha<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"left\">alpha<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "escape", "java.lang.String", new double[]{-0.61049100893172659, 0.93050221412222234, 0.84795280335358858, -0.065722643606052067, 0.3269412890601211, -0.57095406052406394, -0.55660750094751865, -0.42295513323748768, 0.384845491990635}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated2() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:ZmFsc2U=|state=xml:<root><item id=\"right\">beta<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "equals", "java.lang.Object", new double[]{0.099611061944426593, -0.74495720180287872, 0.091683366240700837, -0.36330222636152953, 0.351806496100463, 0.46954937660043217, -0.51855518099492737, -0.1297493397659677, -0.09019668438788786}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated3() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Integer:Mg==|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:7:\"fixture\":\"before\"[][]children:0node:7:\"fixture\":\"beta\"[][]children:0]children:2:child=node:7:\"fixture\":\"beta\"[][]children:0:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getRelativePositionOfPI", "java.lang.String", new double[]{0.78478225134511193, -0.74305101849953359, 0.18531934652102366, 0.32443345670100587, 0.28793407900111045, 0.3646199497093785, -0.26137160100133205, 0.58451784999718814, 0.87692353812026647}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated4() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:dHJ1ZQ==|state=xml:<root><item id=\"right\">beta<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "", "testNode", "org.apache.commons.jxpath.ri.model.NodePointer,java.lang.Object,org.apache.commons.jxpath.ri.compiler.NodeTest", new double[]{0.95093252942901096, -0.68420391290776905, 0.83297772922514812, 0.42767775025884358, -0.97780401441966536, -0.7835617880855843, -0.12844983005794042, 0.77484182685865188, 0.74712643296428149}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated5() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:ZmFsc2U=|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:1:\"i:item\":\"null\"[node:2:\"id\":\"left\"[][node:3:\"#text\":\"left\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"alpha\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3]children:1:child=node:1:\"i:item\":\"null\"[node:2:\"id\":\"left\"[][node:3:\"#text\":\"left\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"alpha\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "equals", "java.lang.Object", new double[]{-0.86328557024709207, 0.75295676500355846, 0.94112295846847682, 0.43233990234790598, 0.23898751718308486, -0.20798303320732625, 0.71676021563636927, -0.42993821748262762, -0.89159790237395886}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated6() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][]children:0:child=node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3:attached=false", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "remove", "", new double[]{0.38253983500007438, 0.74248431823911121, 0.24640617566158651, -0.90716706590137108, -0.58786880977873035, 0.22120297208654516}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated7() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:ZmFsc2U=|state=xml:<root><item id=\"left\">alpha<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"left\">alpha<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "equals", "java.lang.Object", new double[]{-0.67961534597528939, -0.74206265735887045, -0.27150288227226693, -0.90944126990629326, -0.29411118734671593, -0.27626639820187116, -0.7509934738813262, -0.71234529671624647, 0.27560753499374746}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated8() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Integer:MQ==|state=xml:<root><item id=\"right\">beta<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "getRelativePositionByName", "", new double[]{0.60082139668284174, 0.098126926936504821, 0.84139304286006622, -0.3273851284919711, -0.048110147303941586, -0.29725185719501668}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated9() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=xml:<root />:child=xml:<item id=\"left\">alpha<item>nested</item><item>nested-last</item></item>:attached=false", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "remove", "", new double[]{-0.4773874618165006, -0.62097119989661032, 0.42614578326092634, -0.81436305415761168, 0.60767988163588127, 0.43805415735597597}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated10() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.String:aXRlbQ==|state=xml:<root><item id=\"left\">alpha<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"left\">alpha<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "", "getLocalName", "java.lang.Object", new double[]{-0.71337115786480632, 0.61322588314572957, -0.12050597134398844}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated11() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.String:|state=xml:<root><item id=\"left\">alpha<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"left\">alpha<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "escape", "java.lang.String", new double[]{-0.75937036971471872, -0.57247562887681092, -0.24468625444014447, 0.71577715204412362, -0.65210364051781444, -0.58118570116458002, -0.96185412342443288, -0.29616737172755703, -0.84117611269756676}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated12() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3]children:1:child=node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getBaseValue", "", new double[]{0.38186368747764132, 0.81863929423460857, -0.74318740779903902, -0.14175354160061793, -0.93083678866422348, 0.93777338039779545}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated13() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Integer:MQ==|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:7:\"fixture\":\"before\"[][]children:0node:7:\"fixture\":\"beta\"[][]children:0]children:2:child=node:7:\"fixture\":\"beta\"[][]children:0:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getRelativePositionOfPI", "java.lang.String", new double[]{0.3984078592352549, -0.68750887997675658, 0.76932570695046709, 0.098363534132564157, 0.59694637000177875, -0.64117716643378508, 0.15499050292840888, 0.83654601271711249, -0.40826008141295134}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated14() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.String:|state=xml:<root><item id=\"right\">beta<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "asPath", "", new double[]{0.84167960624279203, -0.50698895344902351, -0.81051834353311381, -0.19656495045187961, -0.62865166310796106, -0.80810141008169212}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated15() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=xml:<root />:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=false", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "remove", "", new double[]{0.7520925190258918, 0.77818186142647305, 0.62776048277099772, 0.74962615444860359, -0.23730780895694781, -0.42319555758804883}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated16() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:null|state=xml:<root><item id=\"left\">alpha<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"left\">alpha<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "", "getNamespaceURI", "java.lang.Object", new double[]{-0.74362088039633356, -0.57524010434622763, 0.85295648397569157}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated17() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Integer:MQ==|state=xml:<root><item id=\"right\">beta<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "getRelativePositionByName", "", new double[]{0.94744456139654409, 0.50112266930991911, 0.16850873462727289, -0.91111736742881622, 0.98716674690377637, 0.35692485816226105}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated18() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Integer:MQ==|state=xml:<root><item id=\"right\">beta<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "getLength", "", new double[]{0.75211838774428941, 0.42381514167564149, -0.33593638022486672, 0.84973257431518268, -0.17102042309033472, 0.90190996260711653}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated19() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.String:YmV0YW5lc3RlZG5lc3RlZC1sYXN0|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3]children:1:child=node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "stringValue", "org.w3c.dom.Node", new double[]{0.20356770481891662, -0.62773127330384337, 0.78862676944371857, 0.26715017650346984, 0.64875043522789277, -0.88203956528257188, -0.67155463043068986, -0.95930479325010953, 0.76863685479835087}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated20() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:xml:<root><item id=\"left\">alpha<item>nested</item><item>nested-last</item></item></root>|state=xml:<root><item id=\"left\">alpha<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"left\">alpha<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "nodeParent", "java.lang.Object", new double[]{-0.67831342949878382, -0.47733324733817017, -0.93378403645783692, 0.62542638031081355, 0.61738739146493016, -0.55406694529705613, 0.586075370025821, -0.62962362791475979, -0.20498462966737674}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated21() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:null|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3]children:1:child=node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getDefaultNamespaceURI", "", new double[]{0.86817106975744274, 0.6307857319559842, 0.51943976700195149, -0.44398705081583589, -0.95556500386137677, 0.37153505594831548}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated22() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("void|state=xml:<root />:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=false", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "remove", "", new double[]{0.51930849098685594, 0.82966108603124633, -0.4744019698469617, -0.96654635008770073, -0.72455242794482166, 0.14524115287763562}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated23() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:ZmFsc2U=|state=stateless-scalars", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "", "equalStrings", "java.lang.String,java.lang.String", new double[]{-0.77362771038050449, 0.97406155366866809, 0.53181117694610536, -0.1786439336698713, 0.56250151585323516, 0.85795108999601366}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated24() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.String:YmV0YW5lc3RlZG5lc3RlZC1sYXN0|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3]children:1:child=node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "stringValue", "org.w3c.dom.Node", new double[]{0.028242324189133194, 0.70976648910582485, -0.86235943950772098, 0.13704090110385558, -0.6884190061900195, 0.15494021391521806, -0.51580607517021559, -0.81623760580039129, 0.074737548769140405}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated25() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:qname:item|state=xml:<root><item id=\"left\">alpha<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"left\">alpha<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "getName", "", new double[]{-0.53451210624713297, 0.89855299275018319, -0.72675269634565187, -0.31337351053162488, 0.69304034242370327, -0.92437835119944034}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated26() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:dHJ1ZQ==|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3]children:1:child=node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "isActual", "", new double[]{0.67820312877969879, 0.26631545294030046, -0.57541521832441433, -0.45857838072842849, 0.83560909017761986, -0.46887065336925526}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated27() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:iterator[pointer:xml:<item>nested</item>;]|state=xml:<root><item id=\"right\">beta<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "java.lang.Object,java.util.Locale", "childIterator", "org.apache.commons.jxpath.ri.compiler.NodeTest,boolean,org.apache.commons.jxpath.ri.model.NodePointer", new double[]{0.46739342811680129, 0.2431024783250455, -0.9670840895429238, 0.025904666443100188, -0.43628888624106321, -0.040213972905711826, -0.3543462508293802, 0.85914965617325767, 0.42649119327444329, 0.087885076781162974, 0.95436003027893079, -0.72420941737015587, 0.94794281390188062, -0.45573298960430142, 0.79740578371596671}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated28() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:java.lang.Boolean:dHJ1ZQ==|state=xml:<root><item id=\"right\">beta<item>nested</item><item>nested-last</item></item></root>:child=xml:<item id=\"right\">beta<item>nested</item><item>nested-last</item></item>:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.jdom.JDOMNodePointer", "", "testNode", "org.apache.commons.jxpath.ri.model.NodePointer,java.lang.Object,org.apache.commons.jxpath.ri.compiler.NodeTest", new double[]{0.055133929983918639, 0.11592039043027991, 0.36711079461456753, 0.30181327364955268, -0.54610522324658661, 0.47449870890282497, -0.11504719089712245, 0.92839702281204639, 0.64355308873543815}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated29() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:qname:i:item|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:1:\"i:item\":\"null\"[node:2:\"id\":\"left\"[][node:3:\"#text\":\"left\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"alpha\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3]children:1:child=node:1:\"i:item\":\"null\"[node:2:\"id\":\"left\"[][node:3:\"#text\":\"left\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"alpha\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getName", "", new double[]{-0.43724294885254955, -0.5161688829686244, 0.27912360210913367, 0.30864293446242996, 0.44236870057573174, 0.28439775935253198}, "beam-explicit-fixtures-v3-proposal"));
    } finally { if (SqaProbe.targetInvoked()) TARGET_CHECKS.incrementAndGet(); }
  }
  @Test(timeout=10000)
  public void generated30() {
    EXECUTED.incrementAndGet();
    try {
      assertEquals("value:node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3|state=node:1:\"r:root\":\"null\"[node:2:\"xml:lang\":\"en\"[][node:3:\"#text\":\"en\"[][]children:0]children:1, node:2:\"xmlns:r\":\"urn:sqa:root\"[][node:3:\"#text\":\"urn:sqa:root\"[][]children:0]children:1][node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3]children:1:child=node:1:\"i:item\":\"null\"[node:2:\"id\":\"right\"[][node:3:\"#text\":\"right\"[][]children:0]children:1, node:2:\"xmlns:i\":\"urn:sqa:item\"[][node:3:\"#text\":\"urn:sqa:item\"[][]children:0]children:1][node:3:\"#text\":\"beta\"[][]children:0node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested\"[][]children:0]children:1node:1:\"i:item\":\"null\"[][node:3:\"#text\":\"nested-last\"[][]children:0]children:1]children:3:attached=true", SqaProbe.observeWithPolicy("org.apache.commons.jxpath.ri.model.dom.DOMNodePointer", "org.apache.commons.jxpath.ri.model.NodePointer,org.w3c.dom.Node", "getImmediateNode", "", new double[]{0.0089498972771384633, 0.55291372316965881, -0.62449635056194275, 0.48670733489451012, -0.74158192075564244, 0.90525907041046461}, "beam-explicit-fixtures-v3-proposal"));
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
