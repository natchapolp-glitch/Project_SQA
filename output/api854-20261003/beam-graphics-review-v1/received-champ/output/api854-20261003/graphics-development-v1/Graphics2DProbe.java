package sqa.development;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.*;
import java.security.MessageDigest;
import java.util.*;
import org.jfree.chart.annotations.CategoryLineAnnotation;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import org.jfree.chart.util.Layer;
import org.jfree.data.category.DefaultCategoryDataset;

/** Real Graphics2D and production Chart fixtures; reference geometry is declared independently. */
public final class Graphics2DProbe {
    public static String activeCase = "";
    private static final String OWNER = "org.jfree.chart.renderer.category.AbstractCategoryItemRenderer";
    private static final String[] CASES = {
        "annotations_fg_v", "annotations_fg_h", "annotations_bg_v", "annotations_bg_h",
        "background_v", "background_h", "domain_line_v", "domain_line_h",
        "domain_line_null_paint", "domain_line_null_stroke",
        "domain_marker_line_v", "domain_marker_line_h", "domain_marker_band_v", "domain_marker_band_h",
        "domain_marker_missing", "outline_enabled", "outline_disabled",
        "range_value_v", "range_value_h", "range_interval_v", "range_interval_h", "range_outside",
        "initialise_dataset", "initialise_null_dataset"
    };
    private static String quote(String text) {
        return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }
    private static String json(Object value) {
        if (value == null) return "null";
        if (value instanceof String) return quote((String) value);
        if (value instanceof Map) {
            StringBuilder out = new StringBuilder("{");
            for (Object object : ((Map) value).entrySet()) {
                Map.Entry item = (Map.Entry) object;
                if (out.length() > 1) out.append(',');
                out.append(quote((String) item.getKey())).append(':').append(json(item.getValue()));
            }
            return out.append('}').toString();
        }
        if (value instanceof java.util.List) {
            StringBuilder out = new StringBuilder("[");
            for (Object item : (java.util.List) value) {
                if (out.length() > 1) out.append(',');
                out.append(json(item));
            }
            return out.append(']').toString();
        }
        return String.valueOf(value);
    }
    private static Map<String,Object> map(Object... pairs) {
        Map<String,Object> result = new LinkedHashMap<String,Object>();
        for (int i = 0; i < pairs.length; i += 2) result.put((String) pairs[i], pairs[i+1]);
        return result;
    }
    private static void check(boolean state, String reason) {
        if (!state) throw new AssertionError(reason);
    }
    private static BufferedImage canvas() {
        BufferedImage image = new BufferedImage(64, 64, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        try { g.setColor(Color.WHITE); g.fillRect(0, 0, 64, 64); }
        finally { g.dispose(); }
        return image;
    }
    private static Graphics2D graphics(BufferedImage image) {
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        g.setPaint(Color.BLACK); g.setStroke(new BasicStroke(1.0f));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        return g;
    }
    private static Map<String,Object> state(Graphics2D g) {
        return map("paint_rgb", ((Color) g.getPaint()).getRGB(), "stroke_width", ((BasicStroke) g.getStroke()).getLineWidth(),
                "composite_rule", ((AlphaComposite) g.getComposite()).getRule(),
                "composite_alpha", ((AlphaComposite) g.getComposite()).getAlpha(),
                "identity_transform", g.getTransform().isIdentity(), "clip_null", g.getClip() == null);
    }
    private static byte[] pixels(BufferedImage image) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        for (int y = 0; y < 64; y++) for (int x = 0; x < 64; x++) out.writeInt(image.getRGB(x, y));
        out.close(); return bytes.toByteArray();
    }
    private static String sha(byte[] raw) throws Exception {
        StringBuilder text = new StringBuilder();
        for (byte b : MessageDigest.getInstance("SHA-256").digest(raw)) text.append(String.format("%02x", b & 255));
        return text.toString();
    }
    private static void save(File root, String name, byte[] bytes) throws IOException {
        File file = new File(root, name);
        if (!file.createNewFile()) throw new IOException("Refuse to overwrite pixel evidence");
        try (FileOutputStream out = new FileOutputStream(file)) { out.write(bytes); }
    }
    private static final class Fixture implements AutoCloseable {
        final AreaRenderer renderer = new AreaRenderer();
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        final CategoryAxis domain = new CategoryAxis("Domain");
        final NumberAxis range = new NumberAxis("Range");
        final Rectangle2D area = new Rectangle2D.Double(10, 10, 40, 40);
        final BufferedImage actual = canvas(), reference = canvas();
        final Graphics2D g = graphics(actual), ref = graphics(reference);
        final CategoryPlot plot;
        Fixture(boolean horizontal) {
            dataset.addValue(2.0, "r1", "A"); dataset.addValue(8.0, "r1", "B");
            dataset.addValue(4.0, "r2", "A"); dataset.addValue(6.0, "r2", "B");
            domain.setLowerMargin(0); domain.setUpperMargin(0); domain.setCategoryMargin(0);
            range.setAutoRange(false); range.setRange(0, 10); range.setInverted(false);
            plot = new CategoryPlot(dataset, domain, range, renderer);
            plot.setOrientation(horizontal ? PlotOrientation.HORIZONTAL : PlotOrientation.VERTICAL);
            plot.setBackgroundPaint(Color.YELLOW); plot.setBackgroundAlpha(1.0f); plot.setBackgroundImage(null);
            plot.setOutlinePaint(Color.BLUE); plot.setOutlineStroke(new BasicStroke(2.0f));
            plot.setOutlineVisible(true);
            check(plot.getRenderer() == renderer && renderer.getPlot() == plot, "Production receiver binding");
        }
        public void close() { g.dispose(); ref.dispose(); }
    }
    private static void line(Graphics2D g, boolean horizontal, double value, Color color, float width) {
        g.setPaint(color); g.setStroke(new BasicStroke(width));
        g.draw(horizontal ? new Line2D.Double(10, value, 50, value) : new Line2D.Double(value, 10, value, 50));
    }
    private static Map<String,Object> run(String name, File images) throws Exception {
        boolean horizontal = name.endsWith("_h");
        try (Fixture f = new Fixture(horizontal)) {
            String method; Class<?>[] types; Object[] args;
            String expectedException = null, expectedMessage = null;
            Object expectedReturn = null;
            int rows = f.renderer.getRowCount(), columns = f.renderer.getColumnCount();
            boolean plotBound = true;
            if (name.startsWith("annotations_")) {
                method = "drawAnnotations";
                types = new Class<?>[]{Graphics2D.class, Rectangle2D.class, CategoryAxis.class, ValueAxis.class, Layer.class, PlotRenderingInfo.class};
                boolean foreground = name.contains("_fg_");
                f.renderer.addAnnotation(new CategoryLineAnnotation("A", 2, "B", 8, Color.RED, new BasicStroke(1.0f)), Layer.FOREGROUND);
                f.renderer.addAnnotation(new CategoryLineAnnotation("A", 8, "B", 2, Color.GREEN, new BasicStroke(1.0f)), Layer.BACKGROUND);
                args = new Object[]{f.g, f.area, f.domain, f.range, foreground ? Layer.FOREGROUND : Layer.BACKGROUND, null};
                f.ref.setPaint(foreground ? Color.RED : Color.GREEN); f.ref.setStroke(new BasicStroke(1.0f));
                // Two categories with zero margins have middle coordinates 20 and 40.
                // Range [0,10] maps value v to 50-4v vertically, or 10+4v horizontally.
                int a = foreground ? 2 : 8, b = foreground ? 8 : 2;
                if (horizontal) f.ref.drawLine(10+4*a, 20, 10+4*b, 40);
                else f.ref.drawLine(20, 50-4*a, 40, 50-4*b);
            } else if (name.startsWith("background_")) {
                method = "drawBackground"; types = new Class<?>[]{Graphics2D.class, CategoryPlot.class, Rectangle2D.class};
                args = new Object[]{f.g, f.plot, f.area};
                f.ref.setComposite(AlphaComposite.SrcOver); f.ref.setPaint(Color.YELLOW); f.ref.fillRect(10, 10, 40, 40);
                f.ref.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            } else if (name.startsWith("domain_line_")) {
                method = "drawDomainLine";
                types = new Class<?>[]{Graphics2D.class, CategoryPlot.class, Rectangle2D.class, double.class, Paint.class, Stroke.class};
                Paint paint = name.endsWith("null_paint") ? null : Color.RED;
                Stroke stroke = name.endsWith("null_stroke") ? null : new BasicStroke(2.0f);
                args = new Object[]{f.g, f.plot, f.area, 24.0, paint, stroke};
                if (paint == null || stroke == null) {
                    expectedException = "java.lang.IllegalArgumentException";
                    expectedMessage = paint == null ? "Null 'paint' argument." : "Null 'stroke' argument.";
                } else line(f.ref, horizontal, 24, Color.RED, 2);
            } else if (name.startsWith("domain_marker_")) {
                method = "drawDomainMarker";
                types = new Class<?>[]{Graphics2D.class, CategoryPlot.class, CategoryAxis.class, CategoryMarker.class, Rectangle2D.class};
                CategoryMarker marker = new CategoryMarker(name.endsWith("missing") ? "missing" : "A", Color.RED, new BasicStroke(2.0f));
                marker.setAlpha(1.0f); marker.setLabel(null); marker.setDrawAsLine(name.contains("_line_"));
                args = new Object[]{f.g, f.plot, f.domain, marker, f.area};
                if (!name.endsWith("missing")) {
                    f.ref.setComposite(AlphaComposite.SrcOver);
                    if (name.contains("_line_")) line(f.ref, horizontal, 20, Color.RED, 2);
                    else { f.ref.setPaint(Color.RED); f.ref.fillRect(10, 10, horizontal ? 40 : 20, horizontal ? 20 : 40); }
                    f.ref.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
            } else if (name.startsWith("outline_")) {
                method = "drawOutline"; types = new Class<?>[]{Graphics2D.class, CategoryPlot.class, Rectangle2D.class};
                boolean visible = name.endsWith("enabled"); f.plot.setOutlineVisible(visible);
                args = new Object[]{f.g, f.plot, f.area};
                if (visible) { f.ref.setPaint(Color.BLUE); f.ref.setStroke(new BasicStroke(2.0f)); f.ref.drawRect(10, 10, 40, 40); }
            } else if (name.startsWith("range_")) {
                method = "drawRangeMarker";
                types = new Class<?>[]{Graphics2D.class, CategoryPlot.class, ValueAxis.class, Marker.class, Rectangle2D.class};
                Marker marker;
                if (name.contains("_interval_")) {
                    IntervalMarker interval = new IntervalMarker(2, 8, Color.RED);
                    interval.setOutlinePaint(null); interval.setOutlineStroke(null); marker = interval;
                } else marker = new ValueMarker(name.endsWith("outside") ? 20 : 2, Color.RED, new BasicStroke(2.0f));
                marker.setAlpha(1.0f); marker.setLabel(null);
                args = new Object[]{f.g, f.plot, f.range, marker, f.area};
                if (!name.endsWith("outside")) {
                    f.ref.setComposite(AlphaComposite.SrcOver);
                    if (name.contains("_interval_")) {
                        f.ref.setPaint(Color.RED);
                        f.ref.fillRect(horizontal ? 18 : 10, horizontal ? 10 : 18, horizontal ? 24 : 40, horizontal ? 40 : 24);
                    } else line(f.ref, !horizontal, horizontal ? 18 : 42, Color.RED, 2);
                    f.ref.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                }
            } else {
                method = "initialise";
                types = new Class<?>[]{Graphics2D.class, Rectangle2D.class, CategoryPlot.class, org.jfree.data.category.CategoryDataset.class, PlotRenderingInfo.class};
                boolean withDataset = name.equals("initialise_dataset");
                f.renderer.setPlot(null);
                args = new Object[]{f.g, f.area, f.plot, withDataset ? f.dataset : null, null};
                rows = columns = withDataset ? 2 : 0;
                expectedReturn = map("class", "org.jfree.chart.renderer.category.CategoryItemRendererState", "info_null", true,
                        "bar_width", 0.0, "selection_matches_dataset", withDataset, "selection_null", !withDataset);
            }
            Method target = f.renderer.getClass().getMethod(method, types);
            check(target.getDeclaringClass().getName().equals(OWNER), "Exact inherited declaration binding");
            check(f.renderer.getClass() == AreaRenderer.class, "Default AreaRenderer receiver identity");
            byte[] expectedPixels = pixels(f.reference);
            Map<String,Object> expected = map("pixel_sha256", sha(expectedPixels), "graphics", state(f.ref),
                    "rows", rows, "columns", columns, "plot_bound", plotBound,
                    "dataset", Arrays.asList(2.0, 8.0, 4.0, 6.0), "return_state", expectedReturn,
                    "exception", expectedException, "message", expectedMessage);
            Throwable thrown = null; Object result = null;
            activeCase = name;
            try { result = target.invoke(f.renderer, args); }
            catch (InvocationTargetException failure) { thrown = failure.getCause(); }
            finally { activeCase = ""; }
            Object returned = null;
            if (result instanceof CategoryItemRendererState) {
                CategoryItemRendererState r = (CategoryItemRendererState) result;
                returned = map("class", r.getClass().getName(), "info_null", r.getInfo() == null,
                        "bar_width", r.getBarWidth(), "selection_matches_dataset", r.getSelectionState() == f.dataset,
                        "selection_null", r.getSelectionState() == null);
            }
            byte[] actualPixels = pixels(f.actual);
            Map<String,Object> actual = map("pixel_sha256", sha(actualPixels), "graphics", state(f.g),
                    "rows", f.renderer.getRowCount(), "columns", f.renderer.getColumnCount(), "plot_bound", f.renderer.getPlot() == f.plot,
                    "dataset", Arrays.asList(f.dataset.getValue(0,0), f.dataset.getValue(0,1), f.dataset.getValue(1,0), f.dataset.getValue(1,1)),
                    "return_state", returned, "exception", thrown == null ? null : thrown.getClass().getName(),
                    "message", thrown == null ? null : thrown.getMessage());
            save(images, name + ".actual.argb", actualPixels); save(images, name + ".reference.argb", expectedPixels);
            AssertionError assertion = null;
            try { check(json(actual).equals(json(expected)) && Arrays.equals(actualPixels, expectedPixels), "Declared image/value/state oracle differs"); }
            catch (AssertionError failure) { assertion = failure; }
            boolean passed = assertion == null;
            return map("case", name, "method", method, "receiver_class", f.renderer.getClass().getName(),
                    "declaring_class", target.getDeclaringClass().getName(), "setup_succeeded", true,
                    "target_invoked", true, "target_check_passed", passed,
                    "failure_class", passed ? null : assertion.getClass().getName(), "failure_reason", passed ? null : assertion.getMessage(),
                    "observation", actual, "expected_observation", expected);
        }
    }
    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Expected fresh pixel evidence directory");
        File images = new File(args[0]);
        if (!images.isDirectory()) throw new IllegalArgumentException("Pixel evidence directory must exist");
        int passed = 0, executed = 0, fixtureErrors = 0;
        for (String name : CASES) {
            Map<String,Object> row;
            try { row = run(name, images); executed++; if (Boolean.TRUE.equals(row.get("target_check_passed"))) passed++; }
            catch (Throwable failure) {
                fixtureErrors++;
                row = map("case", name, "setup_succeeded", false, "target_invoked", false,
                        "target_check_passed", false, "failure_class", failure.getClass().getName(), "failure_reason", failure.getMessage());
            }
            System.out.println(json(row));
        }
        System.out.println(json(map("summary", true, "executed", executed, "target_checks", executed,
                "passed", passed, "failed", executed-passed, "skipped", 0, "fixture_errors", fixtureErrors)));
        if (passed != CASES.length || fixtureErrors != 0) System.exit(1);
    }
}
