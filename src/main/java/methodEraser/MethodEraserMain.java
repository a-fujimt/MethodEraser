package methodEraser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class MethodEraserMain {

    @Option(name = "-i", aliases = "--input", metaVar = "input file name", required = true)
    private String inputFilename;

    @Option(name = "-o", aliases = "--output", metaVar = "output file name")
    private String outputFilename;

    @Option(name = "-l", aliases = "--method_list", metaVar = "method list", required = true)
    private String methodListFilename;

    @Option(name = "-erase", metaVar = "is erase method List")
    private boolean isErase = false;

    public static void main(String[] args) throws IOException {
        new MethodEraserMain().run(args);
    }

    public void run(String[] args) throws IOException {
        try {
            parseArguments(args);
        } catch (CmdLineException e) {
            new CmdLineParser(this).printUsage(System.out);
            return;
        }

        if (outputFilename == null)
            outputFilename = inputFilename;
        run(inputFilename, outputFilename, methodListFilename, isErase);
    }

    public void run(String inputFilename, String outputFilename, String methodsFile, boolean isErase) throws IOException {
        final ClassElement javaClassElement = new JavaClassElement(inputFilename, outputFilename);
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

    void parseArguments(String[] args) throws CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);
        parser.parseArgument(args);
    }

}
