package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions;
import ifsc.compiladores.projeto.common.position.TokenPosition;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.expressions.CostExpressionSimplifier;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.expressions.lexer.CostExpressionTokenizer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockCost implements CostResult {
    
    private final CostResult blockCost;
    private final ArrayList<CostResult> costs;

    public BlockCost(CostResult blockCost) {
        this.blockCost = blockCost;
        this.costs = new ArrayList<>();
    }

    public BlockCost(CostResult blockCost, ArrayList<CostResult> costs) {
        this.blockCost = blockCost;
        this.costs = costs;
    }

    public BlockCost(ArrayList<CostResult> costs) {
        this.blockCost = new NullCost();
        this.costs = costs;
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
        ArrayList<String> expressionParts = new ArrayList<>();

        int invariablePart = this.costs.stream()
                .filter(c -> c instanceof Cost)
                .map(CostResult::getValue)
                .reduce(0, Integer::sum);

        if (invariablePart > 0)
            expressionParts.add(String.valueOf(invariablePart));

        String variablePart = this.costs.stream()
                .filter(c -> c instanceof VariableCost)
                .map(CostResult::getStringRepresentation)
                .collect(Collectors.joining(" + "));

        if (!variablePart.isEmpty())
            expressionParts.add(variablePart);

        String nestedBlocks = this.costs.stream()
                .filter(c -> c instanceof BlockCost)
                .map(CostResult::getStringRepresentation)
                .collect(Collectors.joining(" + "));

        if (!nestedBlocks.isEmpty())
            expressionParts.add(nestedBlocks);

        String stringCost = String.join(" + ", expressionParts);

        if (stringCost.isEmpty())
            return "0";

        String costExpression = CostExpressionSimplifier.simplify(stringCost);

        return costExpression;
    }

    private boolean isNumeric(String value) {
        return value.chars().allMatch(Character::isDigit);
    }

    public CostResult getBlockCost() {
        return blockCost;
    }

    public ArrayList<CostResult> getCosts() {
        return costs;
    }
}
