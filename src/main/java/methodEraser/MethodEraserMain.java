package methodEraser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class MethodEraserMain {

    public static void main(String[] args) throws IOException {
        new MethodEraserMain().run(args);
    }

    public void run(String[] args) throws IOException {
        run(args[0], args[1], args[2], false);
    }

    public void run(String inputFilename, String outputFilename, String methodsFile, boolean isErase) throws IOException {
        final ClassElement javaClassElement = new JavaClassElement(inputFilename);
        final List<String> methods = getIgnoreMethods(methodsFile);
        if (isErase) {
            methods.forEach(javaClassElement::erase);
        } else {
            javaClassElement.erase(methods);
        }
        writeFile(javaClassElement.getContents(), outputFilename);
    }

    private List<String> getIgnoreMethods(String filename) throws IOException {
        return Files.readAllLines(Paths.get(filename));
    }

    private void writeFile(String contents, String filename) throws IOException {
        Files.write(Paths.get(filename), contents.getBytes(StandardCharsets.UTF_8));
    }

}
