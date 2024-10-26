package ifsc.compiladores.projeto.LLVM.translator.scopeManager;

import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;

import java.util.HashMap;

public class Scope {

    private final Scope parent;
    private final HashMap<String, Variable> declaredVariables;
    private final Type returnType;

    public Scope(Scope parent) {
        this.declaredVariables = new HashMap<>();
        this.parent = parent;
        this.returnType = null;
    }
    
    public Scope(Scope parent, Type returnType) {
        this.declaredVariables = new HashMap<>();
        this.parent = parent;
        this.returnType = returnType;
    }

    public Scope getParent() {
        return parent;
    }

    public HashMap<String, Variable> getDeclaredVariables() {
        return declaredVariables;
    }
    
    public Type getReturnType() {
        return this.returnType;
    }
}
