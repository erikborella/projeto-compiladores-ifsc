package ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions;

import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.position.TokenPosition;

import java.util.List;

public class SyntaxTreeNode extends SyntaxTreeFragment {
    private final static String NODE_TYPE = "node";

    private final List<SyntaxTreeFragment> children;

    public SyntaxTreeNode(TokenPosition position, String label, List<SyntaxTreeFragment> children) {
        super(position, label, NODE_TYPE);
        this.children = children;
    }

    public List<SyntaxTreeFragment> getChildren() {
        return children;
    }
}
