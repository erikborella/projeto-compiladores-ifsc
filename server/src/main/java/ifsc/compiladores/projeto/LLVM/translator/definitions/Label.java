package ifsc.compiladores.projeto.LLVM.translator.definitions;

import ifsc.compiladores.projeto.LLVM.translator.Fragment;


public class Label implements Fragment {
    private final String labelName;

    public Label(String labelName) {
        this.labelName = labelName;
    }
    
    public String getNameInIRForm() {
        return "%" + this.labelName;
    }

    @Override
    public String getText() {
        return this.labelName + ":";
    }
    
}
