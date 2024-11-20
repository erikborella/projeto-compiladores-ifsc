package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions;
import ifsc.compiladores.projeto.common.position.TokenPosition;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class BlockCost implements CostResult {
    
    private final CostResult blockCost;
    private final ArrayList<CostResult> costs;
    private final boolean isTopLevel;
    private final String topLevelId;

    public BlockCost(CostResult blockCost, ArrayList<CostResult> costs) {
        this.blockCost = blockCost;
        this.costs = costs;
        this.isTopLevel = false;
        this.topLevelId = null;
    }

    public BlockCost(ArrayList<CostResult> costs) {
        this.blockCost = new NullCost();
        this.costs = costs;
        this.isTopLevel = false;
        this.topLevelId = null;
    }

    private BlockCost(CostResult blockCost, ArrayList<CostResult> costs, boolean isTopLevel, String topLevelId) {
        this.blockCost = blockCost;
        this.costs = costs;
        this.isTopLevel = isTopLevel;
        this.topLevelId = topLevelId;
    }

    public BlockCost asTopLevel(String topLeveId) {
        return new BlockCost(this.blockCost, this.costs, true, topLeveId);
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

        String invariablePart = this.costs.stream()
                .filter(c -> c instanceof Cost)
                .map(CostResult::getValue)
                .filter(v -> v > 0)
                .map(String::valueOf)
                .collect(Collectors.joining(" + "));

        if (!invariablePart.isEmpty())
            expressionParts.add(invariablePart);

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

        return stringCost;
    }

    public CostResult getBlockCost() {
        return blockCost;
    }

    public ArrayList<CostResult> getCosts() {
        return costs;
    }

    public boolean isTopLevel() {
        return isTopLevel;
    }

    public String getTopLevelId() {
        return topLevelId;
    }
}
