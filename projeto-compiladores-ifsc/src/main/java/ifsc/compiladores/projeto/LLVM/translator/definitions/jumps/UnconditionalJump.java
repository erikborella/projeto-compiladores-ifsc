package ifsc.compiladores.projeto.LLVM.translator.definitions.jumps;

import ifsc.compiladores.projeto.LLVM.translator.Fragment;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Label;

public class UnconditionalJump implements Fragment {

    private final Label jumpLabel;

    public UnconditionalJump(Label jumpLabel) {
        this.jumpLabel = jumpLabel;
    }
    
    @Override
    public String getText() {
        return String.format("br label %s",
                this.jumpLabel.getNameInIRForm());
    }
    
}
