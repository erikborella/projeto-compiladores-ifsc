package ifsc.compiladores.projeto.LLVM;

import ifsc.compiladores.projeto.LLVM.translator.Fragment;
import ifsc.compiladores.projeto.LLVM.translator.generator.LLVMIRGeneratorVisitor;
import ifsc.compiladores.projeto.gramatica.LexerGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class LLVMCompiler {

    public static String compileToIR(String sourceCode) {
        LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(sourceCode));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        ParserGrammar parser = new ParserGrammar(tokenStream);

        ParserGrammar.ProgramaContext programaContext = parser.programa();

        LLVMIRGeneratorVisitor irGenerator = new LLVMIRGeneratorVisitor();

        Fragment irCode = irGenerator.visitPrograma(programaContext);

        return irCode.getText();
    }

}
