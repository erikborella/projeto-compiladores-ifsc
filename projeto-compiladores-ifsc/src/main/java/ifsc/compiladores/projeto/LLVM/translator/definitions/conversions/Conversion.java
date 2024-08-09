package ifsc.compiladores.projeto.LLVM.translator.definitions.conversions;

import ifsc.compiladores.projeto.LLVM.translator.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;

public class Conversion extends ReturnableFragment {

    private final ConversionType conversionType;
    private final Variable src;

    public Conversion(ConversionType conversionType, Variable returnVariable, Variable src) {
        this.conversionType = conversionType;
        this.returnVariable = returnVariable;
        this.src = src;
    }
    
    @Override
    public String getText() {
        return String.format("%s = %s %s %s to %s",
                this.returnVariable.getNameInIRForm(),
                this.conversionType.getText(),
                this.src.type().getText(),
                this.src.getNameInIRForm(),
                this.returnVariable.type().getText());
    }
    
}
