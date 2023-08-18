package ifsc.compiladores.projeto.LLVM.definitions.expressions;

import ifsc.compiladores.projeto.LLVM.Fragment;

public enum OperationType implements Fragment{
    
    MULTIPLICATION("mul"),
    DIVISION("sdiv"),
    MOD("srem");
    
    private final String type;

    private OperationType(String type) {
        this.type = type;
    }

    @Override
    public String getText() {
        return this.type;
    }
}
