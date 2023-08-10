package ifsc.compiladores.projeto.LLVM.definitions.expressions;

import ifsc.compiladores.projeto.LLVM.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

public class Constant extends ReturnableFragment {

    public Constant(BaseType constantType, String value) {
        this.returnVariable = Variable.asConstant(new Type(constantType), value);
    }

    @Override
    public String getText() {
        return null;
    }
}
