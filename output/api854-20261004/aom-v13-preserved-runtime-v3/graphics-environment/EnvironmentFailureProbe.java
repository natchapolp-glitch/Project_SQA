public class EnvironmentFailureProbe {
 public static void main(String[] args) {
  try {
   SqaProbe.observeWithPolicy("org.jfree.chart.renderer.category.AreaRenderer","","drawBackground","java.awt.Graphics2D,org.jfree.chart.plot.CategoryPlot,java.awt.geom.Rectangle2D",new double[]{0,0,0},"aom-beam-champ-codec-fixtures-v13-development");
   throw new AssertionError("Control escaped fixture guard");
  } catch(Throwable failure) {
   failure.printStackTrace(System.out);
   StringBuilder chain=new StringBuilder();
   for(Throwable t=failure;t!=null;t=t.getCause()) chain.append(t.getClass().getName()).append(":").append(t.getMessage()).append("\n");
   boolean binding=args[0].equals("binding_assertion");
   boolean valid=failure.getClass().getName().equals("SqaProbe$FixtureFailure")
    && failure.getMessage().contains("SQA_HARNESS Graphics")
    && SqaProbe.targetInvoked()==!binding
    && (binding ? chain.toString().contains("java.lang.AssertionError:Production receiver binding")
                : chain.toString().contains("java.lang.LinkageError:controlled environment failure"));
   System.out.println("CONTROL_VALID="+valid+";TARGET_INVOKED="+SqaProbe.targetInvoked());
   if(!valid) System.exit(1);
  }
 }
}
