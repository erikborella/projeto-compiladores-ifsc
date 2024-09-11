package ifsc.compiladores.projeto.syntax.syntaxBuilder.generator;

import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.SyntaxTreeFragment;
import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.SyntaxTreeLeaf;
import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.SyntaxTreeNode;
import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.position.TokenPosition;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;

public class SyntaxTreeGeneratorVisitor extends ParserGrammarBaseVisitor<SyntaxTreeFragment> {
    
    @Override
    public SyntaxTreeFragment visit(ParseTree tree) {
        if (tree instanceof TerminalNode) {
            return visitTerminal((TerminalNode) tree);
        }

        return visitChildren((RuleNode) tree);
    }

    @Override
    public SyntaxTreeFragment visitChildren(RuleNode node) {
        ArrayList<SyntaxTreeFragment> children = new ArrayList<>();

        for (int i = 0; i < node.getChildCount(); i++) {
            children.add(visit(node.getChild(i)));
        }

        TokenPosition position = TokenPosition.fromContext((ParserRuleContext) node);

        String ruleName = ParserGrammar.ruleNames[node.getRuleContext().getRuleIndex()];
        
        SyntaxTreeNode syntaxTreeNode = new SyntaxTreeNode(position, ruleName, children);
        return syntaxTreeNode;
    }

    @Override
    public SyntaxTreeFragment visitTerminal(TerminalNode node) {
        TokenPosition position = TokenPosition.fromToken(node.getSymbol());

        String name = ParserGrammar.VOCABULARY.getSymbolicName(node.getSymbol().getType());

        SyntaxTreeLeaf syntaxTreeLeaf = new SyntaxTreeLeaf(position, name, node.getText());
        return  syntaxTreeLeaf;
    }
    
}
