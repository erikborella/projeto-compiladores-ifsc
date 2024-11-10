package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions;

public enum BasicCommandCost  {
    
    DECLARATION(0),
    ATTRIBUITION(1),
    EXPRESSION(1),
    RETURN(0),
    SCANF(1),
    PRINT(1);
    
    private final int cost;

    private BasicCommandCost(int cost) {
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }
}
