package ifsc.compiladores.projeto.token.tokenBuilder.generator;

import ifsc.compiladores.projeto.common.position.TokenPosition;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import ifsc.compiladores.projeto.token.tokenBuilder.definitions.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

public class TokenListGeneratorVisitor extends ParserGrammarBaseVisitor<List<Token>> {

    @Override
    public List<Token> visit(ParseTree tree) {
        if (tree instanceof TerminalNode) {
            return visitTerminal((TerminalNode) tree);
        }

        return visitChildren((RuleNode) tree);
    }

    @Override
    public List<Token> visitChildren(RuleNode node) {
        ArrayList<Token> children = new ArrayList<>();

        for (int i = 0; i < node.getChildCount(); i++) {
            children.addAll(visit(node.getChild(i)));
        }

        return children;
    }

    @Override
    public List<Token> visitTerminal(TerminalNode node) {
        TokenPosition position = TokenPosition.fromToken(node.getSymbol());

        String type = ParserGrammar.VOCABULARY.getSymbolicName(node.getSymbol().getType());

        Token token = new Token(position, type, node.getText());

        ArrayList<Token> tokenList = new ArrayList<>();
        tokenList.add(token);

        return tokenList;
    }
}
