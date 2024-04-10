package ifsc.compiladores.projeto.complexity.definitions;

public enum BasicCommandCost {
    
    DECLARATION(0),
    RETURN(0),
    ATTRIBUTION(1),
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
