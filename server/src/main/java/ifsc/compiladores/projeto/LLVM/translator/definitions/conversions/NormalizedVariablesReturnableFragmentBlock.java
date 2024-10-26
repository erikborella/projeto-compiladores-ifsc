package ifsc.compiladores.projeto.LLVM.translator.definitions.conversions;

import ifsc.compiladores.projeto.LLVM.translator.FragmentBlock;
import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;

public class NormalizedVariablesReturnableFragmentBlock {
    private final FragmentBlock normalizationBlock;
    private final Variable v1Normalized;
    private final Variable v2Normalized;

    public NormalizedVariablesReturnableFragmentBlock(FragmentBlock normalizationBlock, Variable v1Normalized, Variable v2Normalized) {
        this.normalizationBlock = normalizationBlock;
        this.v1Normalized = v1Normalized;
        this.v2Normalized = v2Normalized;
    }

    public FragmentBlock getNormalizationBlock() {
        return normalizationBlock;
    }

    public Variable getV1Normalized() {
        return v1Normalized;
    }

    public Variable getV2Normalized() {
        return v2Normalized;
    }
}
