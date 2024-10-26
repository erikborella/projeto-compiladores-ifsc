package ifsc.compiladores.projeto.LLVM.translator.generator;

import ifsc.compiladores.projeto.LLVM.translator.FragmentBlock;
import ifsc.compiladores.projeto.LLVM.translator.LabeledFragmentBlock;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Alloca;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Label;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Load;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Store;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.jumps.UnconditionalJump;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;
import ifsc.compiladores.projeto.LLVM.translator.scopeManager.LabelManager;

public class LLVMIRShortCircuitCreator {
    
    private static int retValCount = 0;
    
    private LabeledFragmentBlock trueBlock;
    private LabeledFragmentBlock falseBlock;
    private LabeledFragmentBlock endBlock;
    
    private FragmentBlock headBlock;
    
    private Variable retValHolder;
    private Variable returnVariable;

    public LLVMIRShortCircuitCreator(LabelManager labelManager) {
        this.retValHolder = new Variable(new Type(BaseType.BOOLEAN, 1), "..retValHolder" + retValCount);
   
        Alloca retValHolderAlloca = new Alloca(this.retValHolder, new Type(BaseType.BOOLEAN));
        
        this.headBlock = new FragmentBlock();
        this.headBlock.add(retValHolderAlloca);

        Label endLabel = labelManager.createLabel("..end");
        this.endBlock = new LabeledFragmentBlock(endLabel);        
        
        this.trueBlock = this.generateTrueBlock(labelManager.createLabel("..t"));
        this.falseBlock = this.generateFalseBlock(labelManager.createLabel("..f"));
        
        this.returnVariable = new Variable(new Type(BaseType.BOOLEAN), "..retVal" + retValCount);
        Load returnVariableLoad = new Load(this.returnVariable, this.retValHolder);
        this.endBlock.getFragmentBlock().add(returnVariableLoad);

        retValCount++;
    }

    public LabeledFragmentBlock getTrueBlock() {
        return trueBlock;
    }

    public LabeledFragmentBlock getFalseBlock() {
        return falseBlock;
    }

    public LabeledFragmentBlock getEndBlock() {
        return endBlock;
    }

    public FragmentBlock getHeadBlock() {
        return headBlock;
    }

    public Variable getReturnVariable() {
        return returnVariable;
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
