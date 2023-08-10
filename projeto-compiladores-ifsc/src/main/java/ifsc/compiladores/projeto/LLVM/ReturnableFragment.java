package ifsc.compiladores.projeto.LLVM;

import ifsc.compiladores.projeto.LLVM.definitions.Variable;

public abstract class ReturnableFragment implements Fragment {

    protected Variable returnVariable;

    public Variable getReturnVariable() {
        return returnVariable;
    }

}
