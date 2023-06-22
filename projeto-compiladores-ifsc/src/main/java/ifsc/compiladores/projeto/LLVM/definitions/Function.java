package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public class Function implements Fragment {
    private final Type type;
    private final String name;

    public Function(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String getText() {
        String typeDefinition = type.getText() + (type.isArrayType() ? '*' : "");

        return String.format("define %s @%s() {\n}",
                typeDefinition,
                this.name);
    }
}
