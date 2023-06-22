package ifsc.compiladores.projeto.LLVM.definitions.types;

import ifsc.compiladores.projeto.LLVM.Fragment;

import java.util.ArrayList;

public class Type implements Fragment {

    private BaseType baseType;
    private ArrayList<Integer> dimensions;

    public Type(BaseType baseType) {
        this.baseType = baseType;
        this.dimensions = new ArrayList<>();
    }

    public BaseType getBaseType() {
        return baseType;
    }

    public ArrayList<Integer> getDimensions() {
        return dimensions;
    }

    @Override
    public String getText() {
        StringBuilder builder = new StringBuilder();

        builder.append(this.baseType.getText());

        for (int dimension : this.dimensions) {
            builder.append('*');
        }

        return builder.toString();
    }
}
