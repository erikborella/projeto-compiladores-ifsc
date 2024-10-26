package ifsc.compiladores.projeto.LLVM.translator.definitions.expressions;

import ifsc.compiladores.projeto.LLVM.translator.Fragment;

public enum OperationType implements Fragment{
    
    ADD("add"),
    SUBTRACTION("sub"),
    MULTIPLICATION("mul"),
    DIVISION("sdiv"),
    MOD("srem"),
    
    FLOAT_ADD("fadd"),
    FLOAT_SUBTRACTION("fsub"),
    FLOAT_MULTIPLICATION("fmul"),
    FLOAT_DIVISION("fdiv"),
    FLOAT_MOD("frem"),
    
    AND("and"),
    OR("or"),
    XOR("xor");
    
    private final String type;

    private OperationType(String type) {
        this.type = type;
    }

    @Override
    public String getText() {
        return this.type;
    }
}
