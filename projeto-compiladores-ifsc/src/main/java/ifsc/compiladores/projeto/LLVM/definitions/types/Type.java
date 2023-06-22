package ifsc.compiladores.projeto.LLVM.definitions.types;

import ifsc.compiladores.projeto.LLVM.Fragment;

import java.util.ArrayList;

public class Type implements Fragment {

    private final BaseType baseType;
    private final ArrayList<Integer> dimensions;

    public Type(BaseType baseType) {
        this.baseType = baseType;
        this.dimensions = new ArrayList<>();
    }

    public BaseType getBaseType() {
        return this.baseType;
    }

    public ArrayList<Integer> getDimensions() {
        return this.dimensions;
    }

    public boolean isArrayType() {
        return !this.dimensions.isEmpty();
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

        return builder.toString();
    }
}
