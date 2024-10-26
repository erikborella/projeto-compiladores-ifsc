package ifsc.compiladores.projeto.complexity.definitions;

import ifsc.compiladores.projeto.complexity.definitions.position.TokenPosition;

public interface CostResult {
    TokenPosition getPosition();
    int getValue();
}