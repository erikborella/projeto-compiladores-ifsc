package ifsc.compiladores.projeto.LLVM.scopeManager;

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
