package ifsc.compiladores.projeto.LLVM.translator.definitions.functions;

import ifsc.compiladores.projeto.LLVM.translator.Fragment;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;

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
                this.variable.type().getText(),
                this.getNameInParameterForm());
    }
}