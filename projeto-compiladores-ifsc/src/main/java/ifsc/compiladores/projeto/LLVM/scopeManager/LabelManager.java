package ifsc.compiladores.projeto.LLVM.scopeManager;

import ifsc.compiladores.projeto.LLVM.definitions.Label;

public class LabelManager {
    private int labelCount;

    public LabelManager(int labelCount) {
        this.labelCount = labelCount;
    }

    public LabelManager() {
        this(0);
    }
    
    public Label createLabel(String labelName) {
        Label label = new Label(labelName + String.valueOf(this.labelCount));
        
        return label;
    }
}
