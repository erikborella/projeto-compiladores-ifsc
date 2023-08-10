package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.ReturnableFragment;

public class Load extends ReturnableFragment {

    private final Variable loadVariable;

    public Load(Variable returnVariable, Variable loadVariable) {
        this.returnVariable = returnVariable;
        this.loadVariable = loadVariable;
    }

    public Load(String returnVariableName, Variable loadVariable) {
        this.returnVariable = new Variable(loadVariable.type(), returnVariableName);
        this.loadVariable = new Variable(loadVariable.type().getNewReferencePointerToThis(), loadVariable.name());
    }

    @Override
    public String getText() {
        return String.format("%s = load %s, %s %s",
                this.returnVariable.getNameInIRForm(),
                this.returnVariable.type().getText(),
                this.loadVariable.type().getText(),
                this.loadVariable.getNameInIRForm());
    }
}
