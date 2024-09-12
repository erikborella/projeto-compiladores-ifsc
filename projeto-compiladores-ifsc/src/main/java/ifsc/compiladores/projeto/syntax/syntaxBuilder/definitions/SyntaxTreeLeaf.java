package ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions;

import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.position.TokenPosition;

public class SyntaxTreeLeaf extends SyntaxTreeFragment {
    private static final String LEAF_TYPE = "leaf";

    private final String value;

    public SyntaxTreeLeaf(TokenPosition position, String label, String value) {
        super(position, label, LEAF_TYPE);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
