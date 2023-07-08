package ifsc.compiladores.projeto.LLVM.definitions.functions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public class Parameter implements Fragment {

    private final Variable variable;

    public Parameter(Variable variable) {
        this.variable = variable;
    }

    public Variable getVariable() {
        return variable;
    }

    public String getNameInParameterForm() {
        return String.format("%s.param",
                this.variable.name());
    }

    @Override
    public String getText() {
        return String.format("%s %%%s",
                this.variable.referenceType().getText(),
                this.getNameInParameterForm());
    }
}