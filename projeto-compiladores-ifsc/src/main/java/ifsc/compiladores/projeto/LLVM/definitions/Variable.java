package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public record Variable(Type type, String name) {
    @Override
    public String toString() {
        return "Variable{" +
                "type=" + type.getText() +
                ", name='" + name + '\'' +
                '}';
    }
}
