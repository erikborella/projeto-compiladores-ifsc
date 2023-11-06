package ifsc.compiladores.projeto.LLVM.definitions.strings;

import ifsc.compiladores.projeto.LLVM.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;

public class StringDeclaration extends ReturnableFragment {
    
    private final String str;

    public StringDeclaration(Variable returnVariable, String str) {
        this.returnVariable = returnVariable;
        this.str = str;
    }

    @Override
    public String getText() {
        return String.format("%s = private constant %s c\"%s\"",
                this.returnVariable.name(),
                this.returnVariable.type().getText(),
                this.str);
    }
    
}
