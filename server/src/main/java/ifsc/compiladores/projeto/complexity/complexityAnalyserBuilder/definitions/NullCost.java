package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions;

import ifsc.compiladores.projeto.common.position.TokenPosition;

public class NullCost implements CostResult {

    @Override
    public TokenPosition getPosition() {
        return null;
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public String getStringRepresentation() {
        return null;
    }

}