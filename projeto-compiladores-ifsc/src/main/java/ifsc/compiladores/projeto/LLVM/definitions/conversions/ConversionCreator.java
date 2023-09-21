package ifsc.compiladores.projeto.LLVM.definitions.conversions;

import ifsc.compiladores.projeto.LLVM.ReturnableFragmentBlock;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import ifsc.compiladores.projeto.LLVM.scopeManager.SingleUseVariablesManager;

public class ConversionCreator {
    
    public static ReturnableFragmentBlock convert(SingleUseVariablesManager singleUseVariablesManager, Variable src, Type destType) {
        if (src.type().getBaseType() == destType.getBaseType()) {
            ReturnableFragmentBlock withoutConvertionBlock = new ReturnableFragmentBlock();
            withoutConvertionBlock.setReturnVariable(src);
            
            return withoutConvertionBlock;
        }
        
        ConversionType conversionType;
        
        if (src.type().getBaseType() != BaseType.FLOAT && destType.getBaseType() == BaseType.FLOAT)
            conversionType = getIntegerToFloatConversionType(src, destType);
        else
            conversionType = null;
        
        ReturnableFragmentBlock conversionBlock = new ReturnableFragmentBlock();

        Variable returnVariable = singleUseVariablesManager.getNewVariableOfType(destType);
        
        Conversion conversion = new Conversion(
                conversionType,
                returnVariable,
                src
        );
        
        conversionBlock.getFragmentBlock().add(conversion);
        conversionBlock.setReturnVariable(returnVariable);
        
        return conversionBlock;
    }
    
    private static ConversionType getIntegerToFloatConversionType(Variable src, Type destType) {
        if (src.type().getBaseType() == BaseType.BOOLEAN)
            return ConversionType.BOOLEAN_I_TO_FP;
        
        return ConversionType.I_TO_FP;
    }
    
}
