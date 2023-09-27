package ifsc.compiladores.projeto.LLVM.definitions.conversions;

import ifsc.compiladores.projeto.LLVM.Fragment;

public enum ConversionType implements Fragment {
    BOOLEAN_I_TO_FP("uitofp"),
    I_TO_FP("sitofp"),
    FP_TO_I("fptosi"),
    BOOLEAN_EXT("zext"),
    EXT("sext"),
    TRUNC("trunc");
    
    private final String type;

    private ConversionType(String type) {
        this.type = type;
    }
    
    @Override
    public String getText() {
        return type;
    }
    
}
