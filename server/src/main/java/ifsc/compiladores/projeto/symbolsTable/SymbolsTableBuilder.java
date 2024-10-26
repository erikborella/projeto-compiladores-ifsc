package ifsc.compiladores.projeto.symbolsTable;

import ifsc.compiladores.projeto.gramatica.LexerGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions.SymbolsTable;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.generator.SymbolsTableGeneratorVisitor;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class SymbolsTableBuilder {
    public static SymbolsTable buildSymbolsTable(String sourceCode) {

        LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(sourceCode));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);

        ParserGrammar parser = new ParserGrammar(tokenStream);

        ParserGrammar.ProgramaContext programaContext = parser.programa();

        SymbolsTableGeneratorVisitor symbolsTableGeneratorVisitor = new SymbolsTableGeneratorVisitor();

        symbolsTableGeneratorVisitor.visitPrograma(programaContext);

        SymbolsTable symbolsTable = symbolsTableGeneratorVisitor.getSymbolsTable();

        return symbolsTable;
    }
}
