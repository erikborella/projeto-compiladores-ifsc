package ifsc.compiladores.projeto.LLVM.definitions.types;

import ifsc.compiladores.projeto.LLVM.Fragment;

public enum BaseType implements Fragment {
    VOID("void"),
    BOOLEAN("i1"),
    CHAR("i8"),
    INT("i32"),
    FLOAT("float");
    private final String irDefinition;

    BaseType(String irDefinition) {
        this.irDefinition = irDefinition;
    }

    @Override
    public String getText() {
        return this.irDefinition;
    }
}
