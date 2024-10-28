package ifsc.compiladores.projeto.LLVM.translator.definitions.expressions.comparisons;

import ifsc.compiladores.projeto.LLVM.translator.Fragment;

public enum FloatComparisonType implements Fragment {

    EQUALS("oeq"),
    NOT_EQUALS("one"),
    GREATER("ogt"),
    GREATER_EQUALS("oge"),
    LESS("olt"),
    LESS_EQUALS("ole");

    private final String type;

    private FloatComparisonType(String type) {
        this.type = type;
    }

    @Override
    public String getText() {
        return this.type;
    }
}
