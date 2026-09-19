import org.apache.commons.lang3.math.NumberUtils;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Stable, line-oriented oracle for running the same input in b/f revisions. */
public final class NumberUtilsOracle {
    private NumberUtilsOracle() { }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("expected one hexadecimal string");
        }
        try {
            Number result = NumberUtils.createNumber(args[0]);
            String type = result == null ? "null" : result.getClass().getName();
            String value = result == null ? "" : result.toString();
            System.out.println("{\"status\":\"value\",\"type\":\"" + type
                    + "\",\"value_base64\":\"" + encode(value) + "\"}");
        } catch (Throwable throwable) {
            String message = throwable.getMessage() == null ? "" : throwable.getMessage();
            System.out.println("{\"status\":\"exception\",\"type\":\""
                    + throwable.getClass().getName() + "\",\"message_base64\":\""
                    + encode(message) + "\"}");
        }
    }

    private static String encode(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }
}
