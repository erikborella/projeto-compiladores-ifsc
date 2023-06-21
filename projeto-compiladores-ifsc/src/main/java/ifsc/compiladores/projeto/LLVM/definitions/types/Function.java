package ifsc.compiladores.projeto.LLVM.definitions.types;

import ifsc.compiladores.projeto.LLVM.Fragment;

public class Function implements Fragment {
    private final Type type;
    private final String name;

    public Function(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public String getText() {
        return String.format("define %s @%s() {\n}",
                type.getText(),
                name);
    }
}
