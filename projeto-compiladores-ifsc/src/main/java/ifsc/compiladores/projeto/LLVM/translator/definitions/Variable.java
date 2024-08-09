package ifsc.compiladores.projeto.LLVM.translator.definitions;

import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;

public record Variable(Type type, String name, boolean isConstant) {

    public Variable(Type type, String name) {
        this(type, name, false);
    }

    public static Variable asConstant(Type type, String name) {
        return new Variable(type, name, true);
    }

    public String getNameInIRForm() {
        if (this.isConstant)
            return this.name;

        return "%" + this.name;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "type=" + type.getText() +
                ", name='" + name + '\'' +
                '}';
    }
}
