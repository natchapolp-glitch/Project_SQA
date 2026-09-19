import org.apache.commons.lang3.math.NumberUtils;

/** Stable, line-oriented oracle for running the same input in b/f revisions. */
public final class NumberUtilsOracle {
    private NumberUtilsOracle() { }

    public static void main(String[] args) {
        if (args.length != 1) {
            throw new IllegalArgumentException("expected one hexadecimal string");
        }
        Number result = NumberUtils.createNumber(args[0]);
        System.out.println(result == null ? "null" : result.getClass().getName() + ":" + result);
    }
}
