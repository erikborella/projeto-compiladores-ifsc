package ifsc.compiladores.projeto.syntax;

import ifsc.compiladores.projeto.gramatica.LexerGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.SyntaxTreeFragment;
import ifsc.compiladores.projeto.syntax.syntaxBuilder.generator.SyntaxTreeGeneratorVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class SyntaxTreeBuilder {
    public static SyntaxTreeFragment buildSyntaxTree(String sourceCode) {

        LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(sourceCode));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        ParserGrammar parser = new ParserGrammar(tokenStream);

        ParserGrammar.ProgramaContext programaContext = parser.programa();

        SyntaxTreeGeneratorVisitor syntaxTreeGeneratorVisitor = new SyntaxTreeGeneratorVisitor();

        SyntaxTreeFragment syntaxTree = syntaxTreeGeneratorVisitor.visitPrograma(programaContext);

        return syntaxTree;
    }
}
