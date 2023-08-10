package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public class Alloca extends ReturnableFragment {

    private final Type alocationType;

    public Alloca(Variable returnVariable, Type alocationType) {
        this.returnVariable = returnVariable;
        this.alocationType = alocationType;
    }

    public Alloca(String returnVariableName, Type alocationType) {
        this.returnVariable = new Variable(alocationType.getNewReferencePointerToThis(), returnVariableName);
        this.alocationType = alocationType;
    }

    public Type getAlocationType() {
        return alocationType;
    }

    @Override
    public String getText() {
        return String.format("%s = alloca %s",
                this.returnVariable.getNameInIRForm(),
                this.alocationType.getText());
    }
}
