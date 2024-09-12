package ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions;

import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.position.TokenPosition;

public abstract class SyntaxTreeFragment {
    private final TokenPosition position;
    private final String label;
    private final String type;

    public SyntaxTreeFragment(TokenPosition position, String label, String type) {
        this.position = position;
        this.label = label;
        this.type = type;
    }

    public TokenPosition getPosition() {
        return position;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }
}
