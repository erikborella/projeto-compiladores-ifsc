package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.types.ReferenceType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public class Alloca implements Fragment {

    private final Variable returnVariable;
    private final ReferenceType allocaType;

    public Alloca(Variable returnVariable, ReferenceType allocaType) {
        this.returnVariable = returnVariable;
        this.allocaType = allocaType;
    }

    @Override
    public String getText() {
        return String.format("%%%s = alloca %s",
                this.returnVariable.name(),
                this.allocaType.getText());
    }
}
