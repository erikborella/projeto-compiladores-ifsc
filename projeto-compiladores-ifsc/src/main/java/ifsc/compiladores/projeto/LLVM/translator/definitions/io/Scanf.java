package ifsc.compiladores.projeto.LLVM.translator.definitions.io;

import ifsc.compiladores.projeto.LLVM.translator.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;

public class Scanf extends ReturnableFragment {

    private final Variable stringReference;
    private final Variable scanVariable;

    public Scanf(Variable returnVariable, Variable stringReference, Variable scanVariable) {
        this.returnVariable = returnVariable;
        this.stringReference = stringReference;
        this.scanVariable = scanVariable;
    }
    
    @Override
    public String getText() {
        return String.format("call i32 (i8*, ...) @scanf(i8* %s, %s %s)", 
            this.stringReference.name(),
            this.scanVariable.type().getText(),
            this.scanVariable.getNameInIRForm());
    }
    
}
