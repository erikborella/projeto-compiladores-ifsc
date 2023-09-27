package ifsc.compiladores.projeto.LLVM.definitions.types;

import ifsc.compiladores.projeto.LLVM.Fragment;

public enum BaseType implements Fragment {

    VOID("void", 0),
    BOOLEAN("i1", 1),
    CHAR("i8", 8),
    INT("i32", 32),
    FLOAT("double", 64);

    private final String irDefinition;
    private final int size;

    BaseType(String irDefinition, int size) {
        this.irDefinition = irDefinition;
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String getText() {
        return this.irDefinition;
    }
}
