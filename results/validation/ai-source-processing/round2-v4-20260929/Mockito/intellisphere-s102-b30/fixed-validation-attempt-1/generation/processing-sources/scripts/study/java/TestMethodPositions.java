import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.util.JavacTask;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/** Locate test method source spans without resolving dependencies or running code. */
public final class TestMethodPositions {
    public static void main(String[] args) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager files = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            JavacTask task = (JavacTask) compiler.getTask(null, files, diagnostics,
                Arrays.asList("-proc:none"), null, files.getJavaFileObjects(args));
            Iterable<? extends CompilationUnitTree> units = task.parse();
            for (Diagnostic<?> diagnostic : diagnostics.getDiagnostics()) {
                if (diagnostic.getKind() == Diagnostic.Kind.ERROR) {
                    System.err.println(diagnostic);
                    System.exit(2);
                }
            }
            Trees trees = Trees.instance(task);
            for (CompilationUnitTree unit : units) {
                final String packageName = unit.getPackageName() == null ? "" : unit.getPackageName() + ".";
                new TreePathScanner<Void, Void>() {
                    final Deque<String> classes = new ArrayDeque<>();
                    @Override public Void visitClass(ClassTree node, Void unused) {
                        classes.addLast(node.getSimpleName().toString());
                        super.visitClass(node, unused);
                        classes.removeLast();
                        return null;
                    }
                    @Override public Void visitMethod(MethodTree node, Void unused) {
                        boolean annotated = node.getModifiers().getAnnotations().stream().anyMatch(a ->
                            a.getAnnotationType().toString().equals("Test") ||
                            a.getAnnotationType().toString().equals("org.junit.Test"));
                        boolean junit3 = node.getName().toString().startsWith("test") &&
                            node.getModifiers().getFlags().contains(Modifier.PUBLIC) &&
                            node.getParameters().isEmpty() && node.getReturnType() != null &&
                            node.getReturnType().toString().equals("void");
                        if (annotated || junit3) {
                            long start = trees.getSourcePositions().getStartPosition(unit, node);
                            long end = trees.getSourcePositions().getEndPosition(unit, node);
                            System.out.println(packageName + String.join("$", classes) + "\t" +
                                node.getName() + "\t" + start + "\t" + end);
                        }
                        return super.visitMethod(node, unused);
                    }
                }.scan(unit, null);
            }
        }
    }
}
