package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.Fragment;

public class Store implements Fragment {

    private final Variable source;
    private final Variable destination;

    public Store(Variable source, Variable destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    public String getText() {
        return String.format("store %s %%%s, %s %%%s",
                this.source.type().getText(),
                this.source.name(),
                this.destination.type().getText(),
                this.destination.name());
    }
}
