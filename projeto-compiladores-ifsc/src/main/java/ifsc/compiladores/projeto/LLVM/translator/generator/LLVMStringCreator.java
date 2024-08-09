package ifsc.compiladores.projeto.LLVM.translator.generator;

import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.strings.StringDeclaration;
import ifsc.compiladores.projeto.LLVM.translator.scopeManager.StringManager;
import java.util.ArrayList;
import java.util.HashSet;


public class LLVMStringCreator {
    private final StringManager stringManager;
    private final ArrayList<StringDeclaration> stringsDeclaration;
    private final HashSet<Variable> usedVariables;

    public LLVMStringCreator(StringManager stringManager) {
        this.stringManager = stringManager;
        this.stringsDeclaration = new ArrayList<>();
        this.usedVariables = new HashSet<>();
    }
    
    public Variable declareLLVMString(String str) {
        String cString = addStringEnd(str);
        
        Variable stringVariable = this.stringManager.getStringVariable(cString);
        
        if (!this.usedVariables.contains(stringVariable)) {
            StringDeclaration stringDeclaration = new StringDeclaration(stringVariable, cString);
            this.stringsDeclaration.add(stringDeclaration);
            
            this.usedVariables.add(stringVariable);
        }
        
        return stringVariable;
    }

    public ArrayList<StringDeclaration> getStringsDeclaration() {
        return stringsDeclaration;
    }
    
    private String addStringEnd(String str) {
        return String.format("%s\\00", str);
    }
}
