package ifsc.compiladores.projeto.LLVM.translator.definitions.expressions;

import ifsc.compiladores.projeto.LLVM.translator.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;

public class Constant extends ReturnableFragment {

    public Constant(BaseType constantType, String value) {
        this.returnVariable = Variable.asConstant(new Type(constantType), value);
    }

    @Override
    public String getText() {
        return null;
    }
}
