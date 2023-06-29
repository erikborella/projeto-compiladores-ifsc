package ifsc.compiladores.projeto.LLVM.scopeManager;

import ifsc.compiladores.projeto.LLVM.definitions.Variable;

import java.util.HashMap;

public class Scope {

    private final Scope parent;
    private final HashMap<String, Variable> declaredVariables;

    public Scope(Scope parent) {
        this.declaredVariables = new HashMap<>();
        this.parent = parent;
    }

    public Scope getParent() {
        return parent;
    }

    public HashMap<String, Variable> getDeclaredVariables() {
        return declaredVariables;
    }
}
