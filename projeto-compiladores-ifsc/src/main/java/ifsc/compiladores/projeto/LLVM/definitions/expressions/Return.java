package ifsc.compiladores.projeto.LLVM.definitions.expressions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;

public class Return implements Fragment {

    private final Variable toReturnVariable;
    private final boolean isVoid;

    private Return(Variable toReturnVariable, boolean isVoid) {
        this.toReturnVariable = toReturnVariable;
        this.isVoid = isVoid;
    }

    public Return(Variable toReturnVariable) {
        this(toReturnVariable, false);
    }
    
    public static Return asVoid() {
        return new Return(null, true);
    }
    
    @Override
    public String getText() {
        if (this.isVoid)
            return "ret void";
        
        return String.format("ret %s %s", 
                this.toReturnVariable.type().getText(), 
                this.toReturnVariable.getNameInIRForm());
    }
    
}
