import org.junit.runner.*;
public final class CsvMeasurementRunner {
 public static void main(String[] args)throws Exception {
  Class<?>[] classes=new Class<?>[args.length];
  for(int i=0;i<args.length;i++)classes[i]=Class.forName(args[i]);
  Result r=new JUnitCore().run(classes);
  for(org.junit.runner.notification.Failure f:r.getFailures())System.out.println(f.getTrace());
  System.out.println("CSV_COUNTS:{\"executed\":"+r.getRunCount()+",\"skipped\":"+r.getIgnoreCount()
    +",\"failed\":"+r.getFailureCount()+"}");
  if(!r.wasSuccessful())System.exit(1);
 }
}
