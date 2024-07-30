package ifsc.compiladores.projeto;

import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.Callable;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.generator.LLVMIRGeneratorVisitor;
import ifsc.compiladores.projeto.complexity.generator.ComplexityAnalysisGeneratorVisitor;
import ifsc.compiladores.projeto.gramatica.LexerGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar.ProgramaContext;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import picocli.CommandLine;

import javax.swing.*;

@CommandLine.Command(
        name="ParseCommand",
        mixinStandardHelpOptions = true,
        version = "0.0.1",
        description = "Compile the executable to LLVM IR"
)
public class Main implements Callable<Integer> {
    
    static class FileArgs {
        @CommandLine.Parameters(index = "0", description = "The file to be compiled")
        private File inputFile;
        
        @CommandLine.Option(
                names = { "-o", "--out", "--outFile" },
                paramLabel = "OutputFile",
                description = "Specify the output file, if not set it will be send to stdout")
        private File outputFile;
    }

    
    @CommandLine.Option(names = {"-t", "--showTree"}, description = "Display the parse tree")
    private boolean showTree;
    
    @CommandLine.ArgGroup(exclusive = false)
    private FileArgs fileArgs;

    @Override
    public Integer call() throws IOException {
        if (this.fileArgs == null)
            runInteractiveCompiler();
        
        String code = new String(Files.readAllBytes(this.fileArgs.inputFile.toPath()));
        
        String input = code.trim();
        
        LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(input));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        ParserGrammar parser = new ParserGrammar(tokenStream);

        ProgramaContext programaContext = parser.programa();

        LLVMIRGeneratorVisitor visitor = new LLVMIRGeneratorVisitor();

        Fragment program = visitor.visitPrograma(programaContext);
        
        String irProgram = program.getText();
        
        PrintStream outStream;
        
        if (this.fileArgs.outputFile == null) {
            outStream = System.out;
        }
        else {
            outStream = new PrintStream(this.fileArgs.outputFile);
        }
        
        outStream.print(irProgram);
        
        outStream.close();

        if (showTree) {
            showTree(parser, programaContext);
            new Scanner(System.in).nextLine();
        }

        return 0;
    }
    
    private int runInteractiveCompiler() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            StringBuilder inputBuilder = new StringBuilder();
            System.out.println("Enter your code (type '--stop' to stop and compile the input or '--exit' to exit the program): ");
            while (true) {
                String line = scanner.nextLine();

                if (line.trim().equalsIgnoreCase("--exit")) {
                    System.exit(0);
                }

                if (line.trim().equalsIgnoreCase("--stop")) {
                    break;
                }

                inputBuilder.append(line).append(System.lineSeparator());
            }
            
            String input = inputBuilder.toString().trim();
            if (input.isEmpty()) {
                System.out.println("No input detected. Exiting...");
                return 0;
            }

            LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(input));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            ParserGrammar parser = new ParserGrammar(tokenStream);

            ProgramaContext programaContext = parser.programa();

            ComplexityAnalysisGeneratorVisitor analysisGeneratorVisitor = new ComplexityAnalysisGeneratorVisitor();
            analysisGeneratorVisitor.analyseCode(programaContext);

            LLVMIRGeneratorVisitor visitor = new LLVMIRGeneratorVisitor();

            Fragment program = visitor.visitPrograma(programaContext);

            System.out.println(program.getText());

            if (showTree) {
                showTree(parser, programaContext);
            }
        }
    }
    
    private static void showTree(ParserGrammar parser, ParseTree tree) {
        JFrame frame = new JFrame("Antlr Parser Tree");
        JPanel panel = new JPanel();
        TreeViewer viewer = new TreeViewer(Arrays.asList(
                parser.getRuleNames()), tree
        );

        viewer.setScale(1.5);
        panel.add(viewer);
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();

        // Centralizar o JFrame na tela
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - frame.getWidth()) / 2;
        int centerY = (screenSize.height - frame.getHeight()) / 2;
        frame.setLocation(centerX, centerY);

        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}