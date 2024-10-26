package ifsc.compiladores.projeto.token.tokenBuilder.definitions;

import ifsc.compiladores.projeto.common.position.TokenPosition;

public class Token {
    private final TokenPosition position;
    private final String type;
    private final String value;

    public Token(TokenPosition position, String type, String value) {
        this.position = position;
        this.type = type;
        this.value = value;
    }

    public TokenPosition getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
