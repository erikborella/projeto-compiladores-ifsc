package ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions;

import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.position.TokenPosition;

public abstract class SyntaxTreeFragment {
    private final TokenPosition position;
    private final String label;

    public SyntaxTreeFragment(TokenPosition position, String label) {
        this.position = position;
        this.label = label;
    }

    public TokenPosition getPosition() {
        return position;
    }

    public String getLabel() {
        return label;
    }
}
