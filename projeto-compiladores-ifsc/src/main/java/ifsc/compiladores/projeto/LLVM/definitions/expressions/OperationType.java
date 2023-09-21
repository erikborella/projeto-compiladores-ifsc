package ifsc.compiladores.projeto.LLVM.definitions.expressions;

import ifsc.compiladores.projeto.LLVM.Fragment;

public enum OperationType implements Fragment{
    
    ADD("add"),
    SUBTRACTION("sub"),
    MULTIPLICATION("mul"),
    DIVISION("sdiv"),
    MOD("srem"),
    
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
