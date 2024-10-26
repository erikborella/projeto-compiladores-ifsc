package ifsc.compiladores.projeto.LLVM.translator.definitions.expressions;

import ifsc.compiladores.projeto.LLVM.translator.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;

public class Operation extends ReturnableFragment {

    private final OperationType operationType;
    
    private final Variable op1;
    private final Variable op2;

    public Operation(OperationType operationType, Variable returnVariable, Variable op1, Variable op2) {
        this.operationType = operationType;
        this.returnVariable = returnVariable;
        this.op1 = op1;
        this.op2 = op2;
    }
    
    @Override
    public String getText() {
        return String.format("%s = %s %s %s, %s",
                this.returnVariable.getNameInIRForm(),
                this.operationType.getText(),
                this.returnVariable.type().getText(),
                this.op1.getNameInIRForm(),
                this.op2.getNameInIRForm());
    }
    
}
