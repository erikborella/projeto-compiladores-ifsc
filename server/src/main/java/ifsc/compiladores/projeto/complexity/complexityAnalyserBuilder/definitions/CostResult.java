package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions;

import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.position.TokenPosition;

public interface CostResult {
    TokenPosition getPosition();
    int getValue();
}