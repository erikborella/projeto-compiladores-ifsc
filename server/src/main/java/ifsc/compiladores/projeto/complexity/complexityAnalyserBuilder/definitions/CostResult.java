package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions;

import ifsc.compiladores.projeto.common.position.TokenPosition;

public interface CostResult {
    TokenPosition getPosition();
    int getValue();
    String getStringRepresentation();
}