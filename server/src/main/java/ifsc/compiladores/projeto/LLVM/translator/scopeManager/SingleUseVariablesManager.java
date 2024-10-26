package ifsc.compiladores.projeto.LLVM.translator.scopeManager;

import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;

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

    public Variable getNewVariableOfType(Type variableType) {
        Variable singleUseVariable = new Variable(variableType, String.valueOf(this.currentVariable));

        this.currentVariable++;

        return singleUseVariable;
    }

    public String getNewVariableName() {
        String singleUseVariableName = String.valueOf(this.currentVariable);

        this.currentVariable++;

        return singleUseVariableName;
    }

    public void resetVariables() {
        this.currentVariable = startVariable;
    }
}
