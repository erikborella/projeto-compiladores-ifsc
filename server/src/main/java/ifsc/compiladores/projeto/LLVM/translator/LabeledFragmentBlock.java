package ifsc.compiladores.projeto.LLVM.translator;

import ifsc.compiladores.projeto.LLVM.translator.definitions.Label;

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
