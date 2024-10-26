package ifsc.compiladores.projeto.LLVM.translator.definitions.expressions.comparisons;

import ifsc.compiladores.projeto.LLVM.translator.Fragment;

public enum IntegerComparisonType implements Fragment {
    
    EQUALS("eq"),
    NOT_EQUALS("ne"),
    GREATER("sgt"),
    GREATER_EQUALS("sge"),
    LESS("slt"),
    LESS_EQUALS("sle");
    
    private final String type;

    private IntegerComparisonType(String type) {
        this.type = type;
    }

    @Override
    public String getText() {
        return this.type;
    }
}
