package ifsc.compiladores.projeto.LLVM.translator.definitions.functions;

import ifsc.compiladores.projeto.LLVM.translator.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class FunctionCall extends ReturnableFragment {

    private final String functionName;
    private final ArrayList<Variable> arguments;
    private boolean hasReturn;
    
    private FunctionCall(Variable returnVariable, String functionName, ArrayList<Variable> arguments, boolean hasReturn) {
        this.returnVariable = returnVariable;
        this.functionName = functionName;
        this.arguments = arguments;
        this.hasReturn = hasReturn;
    }

    public FunctionCall(Variable returnVariable, String functionName, ArrayList<Variable> arguments) {
        this.returnVariable = returnVariable;
        this.functionName = functionName;
        this.arguments = arguments;
        this.hasReturn = true;
    }

    public FunctionCall(Variable returnVariable, String functionName) {
        this.returnVariable = returnVariable;
        this.functionName = functionName;
        this.arguments = new ArrayList<>();
        this.hasReturn = true;
    }
    
    public static FunctionCall withoutReturn(Type functionReturnType, String functionName, ArrayList<Variable> arguments) {
        Variable returnVariable = new Variable(functionReturnType, null);
        return new FunctionCall(returnVariable, functionName, arguments, false);
    }

    public ArrayList<Variable> getArguments() {
        return arguments;
    }
        
    @Override
    public String getText() {
        String argumentsText = this.arguments.stream()
                .map(argument -> argument.type().getText() + " " + argument.getNameInIRForm())
                .collect(Collectors.joining(", "));

        if (this.hasReturn) {
            return String.format("%s = call %s @%s(%s)",
                this.returnVariable.getNameInIRForm(),
                this.returnVariable.type().getText(),
                this.functionName,
                argumentsText);
        }
        
        return String.format("call %s @%s(%s)",
                this.returnVariable.type().getText(),
                this.functionName,
                argumentsText);
    }
    
}
