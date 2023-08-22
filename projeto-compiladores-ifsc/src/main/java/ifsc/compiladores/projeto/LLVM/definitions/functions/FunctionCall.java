package ifsc.compiladores.projeto.LLVM.definitions.functions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FunctionCall extends ReturnableFragment {

    private final String functionName;
    private final ArrayList<Variable> arguments;

    public FunctionCall(Variable returnVariable, String functionName, ArrayList<Variable> arguments) {
        this.returnVariable = returnVariable;
        this.functionName = functionName;
        this.arguments = arguments;
    }

    public FunctionCall(Variable returnVariable, String functionName) {
        this.returnVariable = returnVariable;
        this.functionName = functionName;
        this.arguments = new ArrayList<>();
    }

    public ArrayList<Variable> getArguments() {
        return arguments;
    }
        
    @Override
    public String getText() {
        String argumentsText = this.arguments.stream()
                .map(argument -> argument.type().getText() + " " + argument.getNameInIRForm())
                .collect(Collectors.joining(", "));

        
        return String.format("%s = call %s @%s(%s)",
                this.returnVariable.getNameInIRForm(),
                this.returnVariable.type().getText(),
                this.functionName,
                argumentsText);
    }
    
}
