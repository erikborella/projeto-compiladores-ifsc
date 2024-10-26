package ifsc.compiladores.projeto.LLVM.translator.definitions;

import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;

public record Variable(Type type, String name, boolean isConstant, int duplicatedNumber) {

    public Variable(Type type, String name) {
        this(type, name, false, 0);
    }

    public Variable(Type type, String name, int duplicatedNumber) {
        this(type, name, false, duplicatedNumber);
    }

    public static Variable asConstant(Type type, String name) {
        return new Variable(type, name, true, 0);
    }

    public String getRawNameWithoutConflict() {
        String duplicatedIndicator = (this.duplicatedNumber == 0) ? "" : String.valueOf(this.duplicatedNumber);

        return this.name + duplicatedIndicator;
    }

    public String getNameInIRForm() {
        if (this.isConstant)
            return this.name;

        return "%" + this.getRawNameWithoutConflict();
    }

    @Override
    public String toString() {
        return "Variable{" +
                "type=" + type.getText() +
                ", name='" + name + '\'' +
                '}';
    }
}
