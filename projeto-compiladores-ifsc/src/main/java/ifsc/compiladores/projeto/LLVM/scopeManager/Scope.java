package ifsc.compiladores.projeto.LLVM.scopeManager;

import ifsc.compiladores.projeto.LLVM.definitions.Function;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

import java.util.ArrayList;
import java.util.Map;

public class Scope {
    private Scope parent = null;

    public Scope() {
    }

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Scope getParent() {
        return parent;
    }
}
