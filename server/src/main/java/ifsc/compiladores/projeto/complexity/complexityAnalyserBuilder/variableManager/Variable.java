package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.variableManager;

public class Variable {

    private final String id;
    private final String value;
    private final boolean isInput;

    public Variable(String id, String value, boolean isInput) {
        this.id = id;
        this.value = value;
        this.isInput = isInput;
    }

    public String getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public boolean isInput() {
        return isInput;
    }
}
