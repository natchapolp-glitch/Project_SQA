import org.junit.Test;
public class FixtureFailureStudyTest {
 @Test public void setupFailureCannotBeFault() {
  GeneratedStudyTest.SqaProbe.observeWithPolicy("org.jfree.chart.renderer.category.AreaRenderer","","drawBackground","java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D",
   new double[]{Double.NaN,0,0},"aom-beam-champ-graphics-fixtures-v12-development");
 }
}
