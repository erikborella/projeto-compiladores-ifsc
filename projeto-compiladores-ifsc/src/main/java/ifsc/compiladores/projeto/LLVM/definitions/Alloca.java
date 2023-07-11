package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public class Alloca implements Fragment {

    private final Variable returnVariable;
    private final Type alocationType;

    public Alloca(Variable returnVariable, Type alocationType) {
        this.returnVariable = returnVariable;
        this.alocationType = alocationType;
    }

    @Override
    public String getText() {
        return String.format("%%%s = alloca %s",
                this.returnVariable.name(),
                this.alocationType.getText());
    }
}
