package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.Fragment;


public class Label implements Fragment {
    private final String labelName;

    public Label(String labelName) {
        this.labelName = labelName;
    }

    @Override
    public String getText() {
        return this.labelName + ":";
    }
    
}
