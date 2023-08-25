package ifsc.compiladores.projeto.LLVM.definitions.types;

import ifsc.compiladores.projeto.LLVM.Fragment;

import java.util.ArrayList;
import java.util.Arrays;

public class Type implements Fragment {

    private final BaseType baseType;
    private final ArrayList<Integer> dimensions;
    private final int pointerCount;

    public Type(BaseType baseType) {
        this.baseType = baseType;
        this.pointerCount = 0;
        this.dimensions = new ArrayList<>();
    }

    public Type(BaseType baseType, int pointerCount) {
        this.baseType = baseType;
        this.pointerCount = pointerCount;
        this.dimensions = new ArrayList<>();
    }

    public Type(BaseType baseType, int pointerCount, ArrayList<Integer> dimensions) {
        this.baseType = baseType;
        this.pointerCount = pointerCount;

        this.dimensions = dimensions;
    }

    public Type(BaseType baseType, ArrayList<Integer> dimensions) {
        this(baseType, 0, dimensions);
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public ArrayList<Integer> getDimensions() {
        return dimensions;
    }

    public int getPointerCount() {
        return pointerCount;
    }

    public boolean isArrayType() {
        return !this.dimensions.isEmpty();
    }

    public Type getNewReferencePointerToThis() {
        return new Type(this.baseType, this.pointerCount + 1, this.dimensions);
    }

    public Type getNewDeferencePointerOfThis() {
        return new Type(this.baseType, this.pointerCount - 1, this.dimensions);
    }
    
    public Type getNewDeferenceArrayOfThis(int deferenceNumber) {
        ArrayList<Integer> cloneDimensions = (ArrayList<Integer>) this.dimensions.clone();
        Type newType = new Type(this.baseType, this.pointerCount, cloneDimensions);
       
        for (int i = 0; i < deferenceNumber; i++) {
            newType.dimensions.remove(0);
        }
        
        return newType;
    }

    @Override
    public String getText() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.baseType.getText());

        for (int i = this.dimensions.size() - 1; i >= 0; i--) {
            int dimension = this.dimensions.get(i);

            // Insert "[dimension x" at the start
            String arrayDefinition = String.format("[%d x ", dimension);
            builder.insert(0, arrayDefinition);

            // Then close the ']' at the end
            builder.append(']');
        }

        String pointers = "*".repeat(Math.max(0, this.pointerCount));
        builder.append(pointers);

        return builder.toString();
    }
}
