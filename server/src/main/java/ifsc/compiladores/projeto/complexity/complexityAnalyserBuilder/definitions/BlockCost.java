package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.position.TokenPosition;
import java.util.ArrayList;

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

    public CostResult getBlockCost() {
        return blockCost;
    }

    public ArrayList<CostResult> getCosts() {
        return costs;
    }
}
