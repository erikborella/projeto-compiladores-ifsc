package ifsc.compiladores.projeto.LLVM.scopeManager;

import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import java.util.HashMap;


public class StringManager {
    private int stringsCount;
    private final HashMap<String, Variable> strings;

    public StringManager() {
        this.stringsCount = 0;
        this.strings = new HashMap<>();
    }
    
    public Variable getStringVariable(String str) {
        if (this.strings.containsKey(str)) {
            return this.strings.get(str);
        }
        
        // String length considerating that \\xx is only one char
        int irStringLen = str.length() - this.getEscapeCharsOccurence(str) * 2;
        
        Type stringType = new Type(BaseType.CHAR);
        stringType.getDimensions().add(irStringLen);
        
        String variableName = ".str" + stringsCount;
        this.stringsCount++;
        
        Variable newStringVariable = new Variable(stringType, variableName, true);
        
        this.strings.put(str, newStringVariable);
        
        return newStringVariable;
    }
    
    private int getEscapeCharsOccurence(String str) {
        int count = 0;
        
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\\')
                count++;
        }
        
        return count;
    }
}
