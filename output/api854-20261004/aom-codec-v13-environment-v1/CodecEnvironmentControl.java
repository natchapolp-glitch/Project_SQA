public class CodecEnvironmentControl {
 public static void main(String[] args) {
  try {
   SqaProbe.observeWithPolicy("org.apache.commons.codec.language.Metaphone","","isNextChar",
       "java.lang.StringBuffer,int,char",new double[]{-0.875,0,0},"aom-beam-champ-codec-fixtures-v13-development");
   throw new AssertionError("Fixture guard escaped");
  } catch(Throwable failure) {
   failure.printStackTrace(System.out);
   StringBuilder chain=new StringBuilder();
   for(Throwable t=failure;t!=null;t=t.getCause())chain.append(t.getClass().getName()).append(":").append(t.getMessage()).append("\n");
   boolean setup=args[0].equals("default_state");
   boolean valid=failure.getClass().getName().equals("SqaProbe$FixtureFailure")
    && failure.getMessage().contains("SQA_HARNESS Codec") && SqaProbe.targetInvoked()==!setup
    && SqaProbe.CodecRecipe.lastEvidence==null
    && chain.toString().contains(setup?"java.lang.AssertionError:Exact receiver initial state":"java.lang.LinkageError:controlled Codec environment failure");
   System.out.println("CONTROL_VALID="+valid+";TARGET_INVOKED="+SqaProbe.targetInvoked());
   if(!valid)System.exit(1);
  }
 }
}
