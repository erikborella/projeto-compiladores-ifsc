package ifsc.compiladores.projeto.LLVM.definitions.types;

import ifsc.compiladores.projeto.LLVM.Fragment;

public class Type implements Fragment {
    private final BaseArrayType baseArrayType;
    private final int pointerCount;

    public Type(BaseArrayType baseArrayType, int pointerCount) {
        if (pointerCount < 0) {
            throw new IllegalArgumentException("pointerCount cannot be negative");
        }

        this.baseArrayType = baseArrayType;
        this.pointerCount = pointerCount;
    }

    public Type getNewReferencePointerToThis() {
        return new Type(this.baseArrayType, this.pointerCount + 1);
    }

    public Type getNewDeferencePointerOfThis() {
        return new Type(this.baseArrayType, this.pointerCount - 1);
    }

    public BaseArrayType getType() {
        return baseArrayType;
    }

    @Override
    public String getText() {
        return this.baseArrayType.getText() + "*".repeat(Math.max(0, this.pointerCount));
    }
}
