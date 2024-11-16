package ifsc.compiladores.projeto.complexity;

import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.CostResult;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.generator.ComplexityAnalysisGeneratorVisitor;
import ifsc.compiladores.projeto.gramatica.LexerGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;

public class ComplexityAnalyser {

    public static List<CostResult> analyseCodeComplexity(String sourceCode) {
        LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(sourceCode));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        ParserGrammar parser = new ParserGrammar(tokenStream);

        ParserGrammar.ProgramaContext programaContext = parser.programa();

        ComplexityAnalysisGeneratorVisitor complexityAnalysisGeneratorVisitor = new ComplexityAnalysisGeneratorVisitor();

        List<CostResult> programCosts = complexityAnalysisGeneratorVisitor.analyseCode(programaContext);

        return programCosts;
    }

}
