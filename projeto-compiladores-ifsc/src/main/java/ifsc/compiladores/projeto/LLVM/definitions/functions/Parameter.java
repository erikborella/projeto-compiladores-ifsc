package ifsc.compiladores.projeto.LLVM.definitions.functions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public class Parameter implements Fragment {

    private final Type type;
    private final String name;

    public Parameter(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String getText() {
        String typeDefinition = type.getText() + (type.isArrayType() ? '*' : "");

        return String.format("%s %%%s",
                typeDefinition,
                this.name);
    }
}