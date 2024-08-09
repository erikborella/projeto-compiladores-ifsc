package ifsc.compiladores.projeto.LLVM.translator.definitions.io;

import ifsc.compiladores.projeto.LLVM.translator.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Println extends ReturnableFragment {

    private final Variable stringReference;
    private final ArrayList<Variable> arguments;

    public Println(Variable returnVariable, Variable stringReference, ArrayList<Variable> arguments) {
        this.returnVariable = returnVariable;
        this.stringReference = stringReference;
        this.arguments = arguments;
    }
    
    @Override
    public String getText() {
        StringBuilder argumentsText = new StringBuilder();
        
        argumentsText.append(String.format("i8* %s", this.stringReference.name()));
        
        if (!this.arguments.isEmpty()) {
            argumentsText.append(", ");
            
            argumentsText.append(
                    this.arguments.stream()
                        .map(argument -> argument.type().getText() + " " + argument.getNameInIRForm())
                        .collect(Collectors.joining(", "))
            );
        }
        
        return String.format("call i32 (i8*, ...) @printf(%s)", 
                argumentsText.toString());
    }
    
}
