package ifsc.compiladores.projeto.token;

import ifsc.compiladores.projeto.gramatica.LexerGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.token.tokenBuilder.definitions.Token;
import ifsc.compiladores.projeto.token.tokenBuilder.generator.TokenListGeneratorVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.List;

public class TokenListBuilder {
    public static List<Token> buildTokenList(String sourceCode) {

        LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(sourceCode));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        ParserGrammar parser = new ParserGrammar(tokenStream);

        ParserGrammar.ProgramaContext programaContext = parser.programa();

        TokenListGeneratorVisitor tokenListGeneratorVisitor = new TokenListGeneratorVisitor();

        List<Token> tokenlist = tokenListGeneratorVisitor.visitPrograma(programaContext);

        return tokenlist;
    }
}
