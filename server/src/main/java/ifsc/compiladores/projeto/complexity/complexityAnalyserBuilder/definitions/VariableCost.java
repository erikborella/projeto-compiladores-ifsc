package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions;

import ifsc.compiladores.projeto.common.position.TokenPosition;

public class VariableCost implements CostResult {

    private final String variable;
    private final int costRange;

    private final BlockCost blockCost;

    public VariableCost(String variable, int costRange, BlockCost blockCost) {
        this.variable = variable;
        this.costRange = costRange;
        this.blockCost = blockCost;
    }

    @Override
    public TokenPosition getPosition() {
        return this.blockCost.getPosition();
    }

    @Override
    public int getValue() {
        return this.blockCost.getValue();
    }

    @Override
    public String getStringRepresentation() {
        String variableExpression;

        if (this.costRange == 0)
            variableExpression = variable;
        else
            variableExpression = String.format("(%s%d)", this.variable, this.costRange);

        return String.format("%s(%s)", variableExpression, this.blockCost.getStringRepresentation());
    }

    public String getVariable() {
        return variable;
    }

    public int getCostRange() {
        return costRange;
    }

    public BlockCost getBlockCost() {
        return blockCost;
    }
}
