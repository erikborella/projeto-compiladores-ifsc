package ifsc.compiladores.projeto.LLVM.translator.definitions.expressions.comparisons;

import ifsc.compiladores.projeto.LLVM.translator.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;

public class FloatComparison extends ReturnableFragment {

    private final FloatComparisonType comparisonType;

    private final Variable op1;
    private final Variable op2;

    public FloatComparison(FloatComparisonType comparisonType, String returnVariableName, Variable op1, Variable op2) {
        this.returnVariable = new Variable(new Type(BaseType.BOOLEAN), returnVariableName);
        this.comparisonType = comparisonType;
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public String getText() {
        return String.format("%s = fcmp %s %s %s, %s",
                this.returnVariable.getNameInIRForm(),
                this.comparisonType.getText(),
                this.op1.type().getText(),
                this.op1.getNameInIRForm(),
                this.op2.getNameInIRForm());
    }
}
