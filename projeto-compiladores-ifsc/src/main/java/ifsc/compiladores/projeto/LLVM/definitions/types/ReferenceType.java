package ifsc.compiladores.projeto.LLVM.definitions.types;

import ifsc.compiladores.projeto.LLVM.Fragment;

public class ReferenceType implements Fragment {
    private final Type type;
    private final int pointerCount;

    public ReferenceType(Type type, int pointerCount) {
        if (pointerCount < 0) {
            throw new IllegalArgumentException("referenceQualifierNumber cannot be negative");
        }

        this.type = type;
        this.pointerCount = pointerCount;
    }

    public ReferenceType getNewReferencePointerToThis() {
        return new ReferenceType(this.type, this.pointerCount + 1);
    }

    public ReferenceType getNewDeferencePointerOfThis() {
        return new ReferenceType(this.type, this.pointerCount - 1);
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getText() {
        return this.type.getText() + "*".repeat(Math.max(0, this.pointerCount));
    }
}
