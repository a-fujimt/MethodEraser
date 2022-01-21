package methodEraser;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.jupiter.api.Test;

class JavaClassElementTest {

    @Test
    public void testWithErase() throws Exception {
        // クラスローダでファイルの位置を指定する
        String input = JavaClassElementTest.class.getClassLoader().getResource("exampleTests/Example01Test.java").getPath();
        final JavaClassElement javaClassElement = new JavaClassElement(input, input);
        List<String> methods = Arrays.asList("testPlus", "testMinus");
        methods.forEach(javaClassElement::erase);
        final Field field = javaClassElement.getClass().getDeclaredField("compilationUnit");
        field.setAccessible(true);
        final CompilationUnit cu = (CompilationUnit)(field.get(javaClassElement));
        final TypeDeclaration td = (TypeDeclaration) cu.types().get(0);
        assertEquals(1, td.getMethods().length);
    }

    @Test
    public void testWithNoErase() throws Exception {
        String input = JavaClassElementTest.class.getClassLoader().getResource("exampleTests/Example01Test.java").getPath();
        final JavaClassElement javaClassElement = new JavaClassElement(input, input);
        List<String> methods = Arrays.asList("testPlus", "testMinus");
        javaClassElement.erase(methods);
        final Field field = javaClassElement.getClass().getDeclaredField("compilationUnit");
        field.setAccessible(true);
        final CompilationUnit cu = (CompilationUnit)(field.get(javaClassElement));
        final TypeDeclaration td = (TypeDeclaration) cu.types().get(0);
        assertEquals(2, td.getMethods().length);
    }

    @Test
    public void testNoList() throws Exception {
        String input = JavaClassElementTest.class.getClassLoader().getResource("exampleTests/Example01Test.java").getPath();
        final JavaClassElement javaClassElement = new JavaClassElement(input, input);
        List<String> methods = new ArrayList<>();
        javaClassElement.erase(methods);
        final Field field = javaClassElement.getClass().getDeclaredField("compilationUnit");
        field.setAccessible(true);
        final CompilationUnit cu = (CompilationUnit)(field.get(javaClassElement));
        final TypeDeclaration td = (TypeDeclaration) cu.types().get(0);
        assertEquals(0, td.getMethods().length);
    }

    @Test
    public void testNewFileName() throws Exception {
        String input = JavaClassElementTest.class.getClassLoader().getResource("exampleTests/Example01Test.java").getPath();
        String output = "path/to/Example01TestMinimize.java";
        final JavaClassElement javaClassElement = new JavaClassElement(input, output);
        final Field field = javaClassElement.getClass().getDeclaredField("compilationUnit");
        field.setAccessible(true);
        final CompilationUnit cu = (CompilationUnit)(field.get(javaClassElement));
        final TypeDeclaration td = (TypeDeclaration) cu.types().get(0);
        assertEquals("Example01TestMinimize", td.getName().getIdentifier());
    }

}