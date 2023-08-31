package ifsc.compiladores.projeto.LLVM;

import ifsc.compiladores.projeto.LLVM.definitions.Label;

public class LabeledFragmentBlock implements Fragment {

    private final FragmentBlock fragmentBlock;
    private final Label label;

    public LabeledFragmentBlock(Label label) {
        this.label = label;
        this.fragmentBlock = new FragmentBlock();
        this.fragmentBlock.add(label);
    }

    public FragmentBlock getFragmentBlock() {
        return fragmentBlock;
    }

    public Label getLabel() {
        return label;
    }
   
    @Override
    public String getText() {
        return this.fragmentBlock.getText();
    }
    
}
