package ifsc.compiladores.projeto.LLVM.generator;

import ifsc.compiladores.projeto.LLVM.LabeledFragmentBlock;
import ifsc.compiladores.projeto.LLVM.ReturnableFragmentBlock;
import ifsc.compiladores.projeto.LLVM.definitions.Alloca;
import ifsc.compiladores.projeto.LLVM.definitions.Label;
import ifsc.compiladores.projeto.LLVM.definitions.Load;
import ifsc.compiladores.projeto.LLVM.definitions.Store;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.jumps.UnconditionalJump;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import ifsc.compiladores.projeto.LLVM.scopeManager.LabelManager;

public class LLVMIRShortCircuitCreator extends ReturnableFragmentBlock {
    
    private static int retValCount = 0;
    
    private LabeledFragmentBlock trueBlock;
    private LabeledFragmentBlock falseBlock;
    private LabeledFragmentBlock endBlock;
    private Variable retValHolder;

    public LLVMIRShortCircuitCreator(String structureName, LabelManager labelManager) {
        retValCount++;

        this.retValHolder = new Variable(new Type(BaseType.BOOLEAN, 1), "..retValHolder" + retValCount);

        Alloca retValHolderAlloca = new Alloca(this.retValHolder, new Type(BaseType.BOOLEAN));
        this.fragmentBlock.add(retValHolderAlloca);

        Label endLabel = labelManager.createLabel(".." + structureName + ".end");
        this.endBlock = new LabeledFragmentBlock(endLabel);        
        
        this.trueBlock = this.generateTrueBlock(labelManager.createLabel(".." + structureName + ".t"));
        this.falseBlock = this.generateFalseBlock(labelManager.createLabel(".." + structureName + ".f"));
        
        this.returnVariable = new Variable(new Type(BaseType.BOOLEAN), "..retVal" + retValCount);
        Load returnVariableLoad = new Load(this.returnVariable, this.retValHolder);
        this.endBlock.getFragmentBlock().add(returnVariableLoad);
        
        this.fragmentBlock.addAll(this.trueBlock.getFragmentBlock());
        this.fragmentBlock.addAll(this.falseBlock.getFragmentBlock());
        this.fragmentBlock.addAll(this.endBlock.getFragmentBlock());
    }

    public LabeledFragmentBlock getTrueBlock() {
        return trueBlock;
    }

    public LabeledFragmentBlock getFalseBlock() {
        return falseBlock;
    }
    
    
    private LabeledFragmentBlock generateTrueBlock(Label trueLabel) {
        LabeledFragmentBlock newTrueBlock = new LabeledFragmentBlock(trueLabel);
        
        Variable trueValue = Variable.asConstant(new Type(BaseType.BOOLEAN), "true");
        Store trueValueSet = new Store(trueValue, this.retValHolder);
        newTrueBlock.getFragmentBlock().add(trueValueSet);
        
        UnconditionalJump toEndJump = new UnconditionalJump(this.endBlock.getLabel());
        newTrueBlock.getFragmentBlock().add(toEndJump);
        
        return newTrueBlock;
    }
    
    private LabeledFragmentBlock generateFalseBlock(Label falseLabel) {
        LabeledFragmentBlock newTrueBlock = new LabeledFragmentBlock(falseLabel);
        
        Variable trueValue = Variable.asConstant(new Type(BaseType.BOOLEAN), "false");
        Store trueValueSet = new Store(trueValue, this.retValHolder);
        newTrueBlock.getFragmentBlock().add(trueValueSet);
        
        UnconditionalJump toEndJump = new UnconditionalJump(this.endBlock.getLabel());
        newTrueBlock.getFragmentBlock().add(toEndJump);
        
        return newTrueBlock;
    }
    
}
