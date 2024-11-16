package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.expressions.lexer;

public class CostExpressionToken {
    private Object value;
    private final CostExpressionType type;

    public CostExpressionToken(Object value, CostExpressionType type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public CostExpressionType getType() {
        return type;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
