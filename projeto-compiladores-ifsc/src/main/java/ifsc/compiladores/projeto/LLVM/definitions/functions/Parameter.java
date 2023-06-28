package ifsc.compiladores.projeto.LLVM.definitions.functions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public class Parameter implements Fragment {

    private final Variable variable;

    public Parameter(Variable variable) {
        this.variable = variable;
    }

    @Override
    public String getText() {
        Type type = this.variable.type();
        String typeDefinition = type.getText() + (type.isArrayType() ? '*' : "");

        return String.format("%s %%%s",
                typeDefinition,
                this.variable.name());
    }
}