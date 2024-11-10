package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.variableManager;

import java.util.HashMap;

public class VariableManager {
    private HashMap<String, Variable> variables;

    public VariableManager() {
        this.variables = new HashMap<>();
    }

    public void resetVariables() {
        this.variables = new HashMap<>();
    }

    public void addVariable(Variable variable) {
        this.variables.put(variable.getId(), variable);
    }

    public Variable getVariable(String variableId) {
        return this.variables.getOrDefault(variableId, null);
    }
}
