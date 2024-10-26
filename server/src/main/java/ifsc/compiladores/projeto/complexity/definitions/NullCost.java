package ifsc.compiladores.projeto.complexity.definitions;

import ifsc.compiladores.projeto.complexity.definitions.position.TokenPosition;

public class NullCost implements CostResult {

    @Override
    public TokenPosition getPosition() {
        return null;
    }

    @Override
    public int getValue() {
        return 0;
    }

}
