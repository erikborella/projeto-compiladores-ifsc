package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.ReturnableFragment;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GetElementPtr extends ReturnableFragment {

    private final Variable accessVariable;
    private final ArrayList<Variable> indexes;

    public GetElementPtr(Variable returnVariable, Variable accessVariable, ArrayList<Variable> indexes) {
        this.returnVariable  = returnVariable;
        this.accessVariable = accessVariable;
        this.indexes = indexes;
    }
    
    @Override
    public String getText() {
        String indexesString = this.indexes.stream()
                .map(i -> i.type().getText() + " " + i.getNameInIRForm())
                .collect(Collectors.joining(", "));
        
        String inboundTypeString = this.accessVariable.type()
                .getNewDeferencePointerOfThis()
                .getText();
        
        return String.format("%s = getelementptr inbounds %s, %s %s, i32 0, %s",
                this.returnVariable.getNameInIRForm(),
                inboundTypeString,
                this.accessVariable.type().getText(),
                this.accessVariable.getNameInIRForm(),
                indexesString);
    }
    
}
