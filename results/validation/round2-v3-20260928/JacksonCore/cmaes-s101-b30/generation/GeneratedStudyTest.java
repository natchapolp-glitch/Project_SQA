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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
public class GeneratedStudyTest {
  @Test(timeout=10000)
  public void generated1() {
    assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "_charArray", "int", new double[]{-0.19275191598468638, 1, -0.29502854980607007, -0.86670299434726183, 0.18153854892674576, 0.09391060736277862}));
  }
  @Test(timeout=10000)
  public void generated2() {
    assertEquals("value:null", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getTextBuffer", "", new double[]{0.87231085730312385, 0.54488738352560939, -0.45305075975128561}));
  }
  @Test(timeout=10000)
  public void generated3() {
    assertEquals("exception:java.lang.NegativeArraySizeException", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "_charArray", "int", new double[]{-0.80302803370382236, 0.4445014790358171, -0.40473810276689071, -0.45671139620883039, 0.43279433032780984, -0.73646665111255394}));
  }
  @Test(timeout=10000)
  public void generated4() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getCurrentSegment", "", new double[]{0.77217563711311499, 0.045872834140195586, 0.090821818057883724}));
  }
  @Test(timeout=10000)
  public void generated5() {
    assertEquals("value:object-type:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "_badBigDecimal", "java.lang.String", new double[]{0.011923870537382713, -0.4553092635391221, 1}));
  }
  @Test(timeout=10000)
  public void generated6() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "findBuffer", "int", new double[]{-0.28437430425406618, -0.99571928534083087, 0.38693633886282797, -0.99078136129558703, 0.23950509416686216, 0.29068450657000516}));
  }
  @Test(timeout=10000)
  public void generated7() {
    assertEquals("constructed:com.fasterxml.jackson.core.util.TextBuffer", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "<init>", "", new double[]{-0.3317815852655342, -0.11732062694621649, 0.19129711544027342}));
  }
  @Test(timeout=10000)
  public void generated8() {
    assertEquals("value:java.lang.String:", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsString", "", new double[]{1, 1, 0.42765683273523314}));
  }
  @Test(timeout=10000)
  public void generated9() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getCurrentSegment", "", new double[]{-0.41743967712960767, -0.12834228878667803, 0.87243104909043756}));
  }
  @Test(timeout=10000)
  public void generated10() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "getCurrentSegment", "", new double[]{0.31069901760123675, 0.13745628932118678, 0.39948256790261466}));
  }
  @Test(timeout=10000)
  public void generated11() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "append", "char", new double[]{0.5414362171196252, -0.37501790087675724, 0.81889869415101868, -0.04253261418341437, -0.4432496125896625, -0.60681950885111802}));
  }
  @Test(timeout=10000)
  public void generated12() {
    assertEquals("value:java.lang.Integer:LTEwMDAw", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseAsInt", "java.lang.String,int", new double[]{0.047516550553828335, 0.16698230180487394, -0.47464519824018331, -1, 0.019173559401136145, -0.30366888491273369}));
  }
  @Test(timeout=10000)
  public void generated13() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseLong", "java.lang.String", new double[]{0.061299136376913255, 0.52733286415680081, -1}));
  }
  @Test(timeout=10000)
  public void generated14() {
    assertEquals("value:java.lang.Long:MTIxNw==", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseAsLong", "java.lang.String,long", new double[]{0.13663763025830022, -0.016470968379985518, -0.17862905583729824, 0.12165761865764607, 0.12752433688604436, 1}));
  }
  @Test(timeout=10000)
  public void generated15() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseDouble", "java.lang.String", new double[]{-0.75497544025826524, 0.39677420791488804, -0.092398385806895883}));
  }
  @Test(timeout=10000)
  public void generated16() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "setCurrentLength", "int", new double[]{0.86573599841356008, 0.12799144763624121, -0.43379615688455975, -0.23729966069679082, 0.046729274868766171, 0.41173044128120728}));
  }
  @Test(timeout=10000)
  public void generated17() {
    assertEquals("value:[C[]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsArray", "", new double[]{0.41199434213774599, 0.38390341086570123, 0.51230760082678117}));
  }
  @Test(timeout=10000)
  public void generated18() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "[C", new double[]{0.72393652529634611, 0.35620131635504343, 0.31396000746488867}));
  }
  @Test(timeout=10000)
  public void generated19() {
    assertEquals("void", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "unshare", "int", new double[]{-0.66073076911386341, 0.89120459082050063, 0.74249170615626547, -0.18902725211435562, 0.59148551503337965, 1}));
  }
  @Test(timeout=10000)
  public void generated20() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseInt", "java.lang.String", new double[]{0.60932388219455746, -0.71853624875341904, -1}));
  }
  @Test(timeout=10000)
  public void generated21() {
    assertEquals("value:[C[]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "contentsAsArray", "", new double[]{0.21308314188518285, 0.58322864340345726, -0.13401342908911859}));
  }
  @Test(timeout=10000)
  public void generated22() {
    assertEquals("constructed:com.fasterxml.jackson.core.util.TextBuffer", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "<init>", "", new double[]{0.15515290810070517, -0.30315995021851894, -0.25829791672647678}));
  }
  @Test(timeout=10000)
  public void generated23() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseBigDecimal", "[C", new double[]{-0.054899786947762583, 0.11283194451615297, -0.93130770541546459}));
  }
  @Test(timeout=10000)
  public void generated24() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "hasTextAsCharacters", "", new double[]{0.54453439260637504, 0.62661323352968745, -0.10687287357011283}));
  }
  @Test(timeout=10000)
  public void generated25() {
    assertEquals("value:object-type:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "_badBigDecimal", "java.lang.String", new double[]{-0.073447892728023434, 0.20121417678680534, -1}));
  }
  @Test(timeout=10000)
  public void generated26() {
    assertEquals("value:java.lang.Integer:LTM5NjQ=", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseAsInt", "java.lang.String,int", new double[]{-0.94141000823982046, -0.18725911553605579, 0.11170378311707729, -0.396414254665437, 0.12156369449968385, -0.13710498531668436}));
  }
  @Test(timeout=10000)
  public void generated27() {
    assertEquals("exception:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "parseInt", "java.lang.String", new double[]{0.28177582074054619, 0.55057607423539667, -0.087184849071418158}));
  }
  @Test(timeout=10000)
  public void generated28() {
    assertEquals("value:[C[java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;java.lang.Character:AA==;]", SqaProbe.observe("com.fasterxml.jackson.core.util.TextBuffer", "com.fasterxml.jackson.core.util.BufferRecycler", "findBuffer", "int", new double[]{0.24885904904657397, 0.056339653453352567, -0.73941050993735502, -1, 0.16806462115357393, 1}));
  }
  @Test(timeout=10000)
  public void generated29() {
    assertEquals("value:java.lang.Boolean:dHJ1ZQ==", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "inLongRange", "java.lang.String,boolean", new double[]{0.62026495909844259, 0.14359874343686652, -0.13377179367363606, 0.98734861558515297, -0.38829688153724196, 0.74045411249641435}));
  }
  @Test(timeout=10000)
  public void generated30() {
    assertEquals("value:object-type:java.lang.NumberFormatException", SqaProbe.observe("com.fasterxml.jackson.core.io.NumberInput", "", "_badBigDecimal", "java.lang.String", new double[]{0.0090519615814508032, 0.81502651067744658, -0.23812050916671956}));
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
            return method.getReturnType() == void.class ? "void" : "value:" + value(result);
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
