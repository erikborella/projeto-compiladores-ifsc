package ifsc.compiladores.projeto.LLVM.scopeManager;

import ifsc.compiladores.projeto.LLVM.definitions.Label;
import java.util.HashMap;

public class LabelManager {
    private final HashMap<String, Integer> uniqueLabelsTable;

    public LabelManager() {
        this.uniqueLabelsTable = new HashMap<>();
    }
    
    public Label createLabel(String labelName) {
        int labelCount = this.uniqueLabelsTable.getOrDefault(labelName, 0);
      
        Label label = new Label(labelName + String.valueOf(labelCount));
        labelCount++;
        
        this.uniqueLabelsTable.put(labelName, labelCount);
        
        return label;
    }
}
