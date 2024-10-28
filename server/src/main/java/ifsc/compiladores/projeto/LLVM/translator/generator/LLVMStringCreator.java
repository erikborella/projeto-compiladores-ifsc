package ifsc.compiladores.projeto.LLVM.translator.generator;

import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.strings.StringDeclaration;
import ifsc.compiladores.projeto.LLVM.translator.scopeManager.StringManager;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;


public class LLVMStringCreator {
    private final StringManager stringManager;
    private final ArrayList<StringDeclaration> stringsDeclaration;
    private final HashSet<Variable> usedVariables;

    private final byte ASCII_PRINTABLE_LOW_LIMIT = 0x20;
    private final byte ASCII_PRINTABLE_HIGH_LIMIT = 0x7E;

    public LLVMStringCreator(StringManager stringManager) {
        this.stringManager = stringManager;
        this.stringsDeclaration = new ArrayList<>();
        this.usedVariables = new HashSet<>();
    }
    
    public Variable declareLLVMString(String str) {
        String hexString = toHexString(str);
        String cString = addStringEnd(hexString);

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

    private String toHexString(String str) {
        StringBuilder hexString = new StringBuilder();

        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);

        for (byte b : strBytes) {
            int unsignedByte = b & 0xFF;

            if (unsignedByte >= ASCII_PRINTABLE_LOW_LIMIT && unsignedByte <= ASCII_PRINTABLE_HIGH_LIMIT) {
                hexString.append((char) unsignedByte);
            } else {
                hexString.append(String.format("\\%02X", unsignedByte));
            }
        }

        return hexString.toString();
    }


}
