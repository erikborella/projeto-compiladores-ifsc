package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.definitions.types.ReferenceType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public record Variable(ReferenceType referenceType, String name) {
    @Override
    public String toString() {
        return "Variable{" +
                "referenceType=" + referenceType.getText() +
                ", name='" + name + '\'' +
                '}';
    }
}
