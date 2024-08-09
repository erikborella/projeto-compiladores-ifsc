package ifsc.compiladores.projeto.LLVM.translator.definitions;

import ifsc.compiladores.projeto.LLVM.translator.ReturnableFragment;

public class Load extends ReturnableFragment {

    private final Variable loadVariable;

    public Load(Variable returnVariable, Variable loadVariable) {
        this.returnVariable = returnVariable;
        this.loadVariable = loadVariable;
    }

    public Load(String returnVariableName, Variable loadVariable) {
        this.returnVariable = new Variable(loadVariable.type().getNewDeferencePointerOfThis(), returnVariableName);
        this.loadVariable = new Variable(loadVariable.type(), loadVariable.name());
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
