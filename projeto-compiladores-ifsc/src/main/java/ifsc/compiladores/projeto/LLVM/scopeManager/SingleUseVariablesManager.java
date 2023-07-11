package ifsc.compiladores.projeto.LLVM.scopeManager;

import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.types.ReferenceType;

public class SingleUseVariablesManager {
    private final int startVariable;
    private int currentVariable;

    public SingleUseVariablesManager(int currentVariable) {
        this.startVariable = currentVariable;
        this.currentVariable = startVariable;
    }

    public SingleUseVariablesManager() {
        this(1);
    }

    public Variable getNewVariableOfType(ReferenceType variableType) {
        Variable singleUseVariable = new Variable(variableType, String.valueOf(this.currentVariable));

        this.currentVariable++;

        return singleUseVariable;
    }

    public void resetVariables() {
        this.currentVariable = startVariable;
    }
}
