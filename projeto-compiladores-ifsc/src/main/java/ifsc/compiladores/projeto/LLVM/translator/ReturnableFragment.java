package ifsc.compiladores.projeto.LLVM.translator;

import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;

public abstract class ReturnableFragment implements Fragment {

    protected Variable returnVariable;

    public Variable getReturnVariable() {
        return returnVariable;
    }

}
