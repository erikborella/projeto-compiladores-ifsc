package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions;

import ifsc.compiladores.projeto.common.position.TokenPosition;

public class Cost implements CostResult {

    private final TokenPosition position;
    private final int value;
    private final boolean shouldShowInPlace;

    private Cost(TokenPosition position, int value, boolean shouldShowInPlace) {
        this.position = position;
        this.value = value;
        this.shouldShowInPlace = shouldShowInPlace;
    }

    public Cost(TokenPosition position, int value) {
        this(position, value, false);
    }
    public static Cost inPlace(TokenPosition position, int value) {
        return new Cost(position, value, true);
    }
    
    @Override
    public TokenPosition getPosition() {
        return this.position;
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public String getStringRepresentation() {
        return String.format("%d", this.value);
    }

    public boolean getShouldShowInPlace() {
        return this.shouldShowInPlace;
    }

}
