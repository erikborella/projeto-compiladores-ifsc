package ifsc.compiladores.projeto.LLVM;

import ifsc.compiladores.projeto.LLVM.translator.Fragment;
import ifsc.compiladores.projeto.LLVM.translator.errors.SyntaxErrorListener;
import ifsc.compiladores.projeto.LLVM.translator.generator.LLVMIRGeneratorVisitor;
import ifsc.compiladores.projeto.gramatica.LexerGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.io.IOException;

public class LLVMCompiler {

    public static String compileToIR(String sourceCode) {
        LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(sourceCode));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        ParserGrammar parser = new ParserGrammar(tokenStream);

        SyntaxErrorListener syntaxErrorListener = new SyntaxErrorListener();
        parser.addErrorListener(syntaxErrorListener);

        ParserGrammar.ProgramaContext programaContext = parser.programa();

        if (!syntaxErrorListener.getSyntaxErrors().isEmpty()) {
            throw new IllegalStateException(syntaxErrorListener.toString());
        }

        LLVMIRGeneratorVisitor irGenerator = new LLVMIRGeneratorVisitor();

        Fragment irCode = irGenerator.visitPrograma(programaContext);

        return irCode.getText();
    }

    public static boolean optimizeIR(String llvmOptimizer, File irCodePath, File destPath, String optLevelFlagValue) throws IOException, InterruptedException {
        String optFlag = "-" + optLevelFlagValue;

        Process optProcess = new ProcessBuilder(
                llvmOptimizer,
                optFlag,
                "-S",
                irCodePath.toString(),
                "-o",
                destPath.toString()
        )
        .start();

        return optProcess.waitFor() == 0;
    }

    public static boolean compileToAsm(String clangCompiler, File irCodePath, File destPath) throws IOException, InterruptedException {
        Process asmCompileProcess = new ProcessBuilder(
                clangCompiler,
                "-masm=intel",
                "-S",
                irCodePath.toString(),
                "-o",
                destPath.toString()
        )
        .start();

        return asmCompileProcess.waitFor() == 0;
    }

    public static boolean optimizeAsm(String clangCompiler, File irCodePath, File destPath, String optLevelFlagValue) throws IOException, InterruptedException {
        String optFlag = "-" + optLevelFlagValue;

        Process asmOptimizeProcess = new ProcessBuilder(
                clangCompiler,
                "-masm=intel",
                "-S",
                optFlag,
                irCodePath.toString(),
                "-o",
                destPath.toString()
        )
        .start();

        return asmOptimizeProcess.waitFor() == 0;
    }

    public static boolean compileToExecutable(String clangCompiler, File irCodePath, File destPath) throws IOException, InterruptedException {
        Process executableCompilingProcess = new ProcessBuilder(
                clangCompiler,
                irCodePath.toString(),
                "-o",
                destPath.toString()
        )
        .start();

        return executableCompilingProcess.waitFor() == 0;
    }
}