public class CliOrderDiagnostic {
 public static void main(String[] args) {
  System.out.println("CLI_DIAGNOSTIC:"+SqaProbe.observeWithPolicy(
    "org.apache.commons.cli.CommandLine", "", "addOption", "org.apache.commons.cli.Option",
    new double[]{-0.8935972282600377,-0.43532870851728633,0.5969027960228479}, "aom-beam-champ-graphics-fixtures-v12-development"));
 }
}
