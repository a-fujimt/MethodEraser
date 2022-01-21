package methodEraser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class JavaClassElement implements ClassElement {

    private CompilationUnit compilationUnit;

    public JavaClassElement(String inputFilename, String outputFilename) throws IOException {
        this.compilationUnit = createTree(inputFilename);
        final String[] pathSeparated = outputFilename.split(File.separator);
        final String className = pathSeparated[pathSeparated.length - 1].split("\\.")[0];
        final List types = compilationUnit.types();
        if (types.size() != 0) {
            ((TypeDeclaration)(types.get(0))).getName().setIdentifier(className);
        }
    }

    private CompilationUnit createTree(String filename) throws IOException {
        ASTParser parser = ASTParser.newParser(AST.JLS11);
        Hashtable<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_11);
        options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_11);
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_11);
        options.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
        parser.setCompilerOptions(options);
        parser.setSource(Files.readString(Paths.get(filename)).toCharArray());
        return (CompilationUnit) parser.createAST(new NullProgressMonitor());
    }

    @Override
    public void erase(String methodName) {
        compilationUnit.accept(new ASTVisitor() {
            @Override
            public boolean visit(MethodDeclaration node) {
                if (node.getName().getIdentifier().equals(methodName))
                    node.delete();
                return super.visit(node);
            }
        });
    }

    @Override
    public void erase(List<String> noEraseMethods) {
        compilationUnit.accept(new MethodDeleteVisitor(noEraseMethods));
    }

    @Override
    public String getContents() {
        return compilationUnit.toString();
    }

    class MethodDeleteVisitor extends ASTVisitor {

        private final List<String> noEraseMethods;

        MethodDeleteVisitor(List<String> noEraseMethods) {
            this.noEraseMethods = noEraseMethods;
        }

        @Override
        public boolean visit(MethodDeclaration node) {
            if (!noEraseMethods.contains(node.getName().getIdentifier()))
                node.delete();
            return super.visit(node);
        }
    }

}
