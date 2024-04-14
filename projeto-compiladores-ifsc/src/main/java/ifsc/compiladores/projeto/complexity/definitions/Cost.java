package ifsc.compiladores.projeto.complexity.definitions;

import ifsc.compiladores.projeto.complexity.definitions.position.TokenPosition;

public class Cost implements CostResult {

    private final TokenPosition position;
    private final int value;

    public Cost(TokenPosition position, int value) {
        this.position = position;
        this.value = value;
    }
    
    @Override
    public TokenPosition getPosition() {
        return this.position;
    }

    @Override
    public int getValue() {
        return this.value;
    }
    
}
