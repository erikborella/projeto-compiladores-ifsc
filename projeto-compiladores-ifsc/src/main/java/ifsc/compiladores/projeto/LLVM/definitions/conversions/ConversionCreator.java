package ifsc.compiladores.projeto.LLVM.definitions.conversions;

import ifsc.compiladores.projeto.LLVM.FragmentBlock;
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
        
        else if (src.type().getBaseType() == BaseType.FLOAT && destType.getBaseType() != BaseType.FLOAT)
            conversionType = ConversionType.FP_TO_I;
        
        else if (src.type().getBaseType().getSize() < destType.getBaseType().getSize())
            conversionType = getExtIntegerConversionType(src, destType);

        else if (src.type().getBaseType().getSize() > destType.getBaseType().getSize())
            conversionType = ConversionType.TRUNC;
        
        else
            throw new IllegalStateException("Impossible conversion");
        
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
    
    public static NormalizedVariablesReturnableFragmentBlock normalize(SingleUseVariablesManager singleUseVariablesManager, Variable v1, Variable v2) {
        if (v1.type().getBaseType() == v2.type().getBaseType()) {
           return new NormalizedVariablesReturnableFragmentBlock(
                   new FragmentBlock(),
                   v1,
                   v2
           );
        }

        if (v1.type().getBaseType().getSize() > v2.type().getBaseType().getSize()) {
            ReturnableFragmentBlock conversion = convert(
                singleUseVariablesManager,
                v2,
                v1.type()
            );
            
            return new NormalizedVariablesReturnableFragmentBlock(
                conversion.getFragmentBlock(),
                v1,
                conversion.getReturnVariable()
            );
        }
        
        ReturnableFragmentBlock conversion = convert(
            singleUseVariablesManager,
            v1,
            v2.type()
        );

        return new NormalizedVariablesReturnableFragmentBlock(
            conversion.getFragmentBlock(),
            conversion.getReturnVariable(),
            v2
        );
        
    }
    
    private static ConversionType getIntegerToFloatConversionType(Variable src, Type destType) {
        if (src.type().getBaseType() == BaseType.BOOLEAN)
            return ConversionType.BOOLEAN_I_TO_FP;
                
        return ConversionType.I_TO_FP;
    }
    
    private static ConversionType getExtIntegerConversionType(Variable src, Type destType) {
        if (src.type().getBaseType() == BaseType.BOOLEAN)
            return ConversionType.BOOLEAN_EXT;
        
        return ConversionType.EXT;
    }
            
}
