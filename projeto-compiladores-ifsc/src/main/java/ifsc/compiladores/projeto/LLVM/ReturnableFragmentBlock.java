package ifsc.compiladores.projeto.LLVM;

import ifsc.compiladores.projeto.LLVM.definitions.Variable;

public class ReturnableFragmentBlock extends ReturnableFragment {

    protected final FragmentBlock fragmentBlock;

    public ReturnableFragmentBlock() {
        this.fragmentBlock = new FragmentBlock();
    }

    public ReturnableFragmentBlock(ReturnableFragment returnableFragment) {
        this.fragmentBlock = new FragmentBlock();
        this.fragmentBlock.add(returnableFragment);

        this.returnVariable = returnableFragment.getReturnVariable();
    }

    public FragmentBlock getFragmentBlock() {
        return fragmentBlock;
    }

    public void setReturnVariable(Variable returnVariable) {
        this.returnVariable = returnVariable;
    }

    @Override
    public String getText() {
        return this.fragmentBlock.getText();
    }
}
