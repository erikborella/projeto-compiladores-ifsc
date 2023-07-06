package ifsc.compiladores.projeto.LLVM.definitions.functions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Function implements Fragment {
    private final Type type;
    private final String name;
    private final ArrayList<Parameter> parameters;

    public Function(Type type, String name) {
        this.type = type;
        this.name = name;
        this.parameters = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getText() {
        String typeDefinition = type.getText() + (type.isArrayType() ? '*' : "");
        
        String parametersDefinition = this.parameters.stream()
                .map(Parameter::getText)
                .collect(Collectors.joining(", "));

        return String.format("define %s @%s(%s) {\n}",
                typeDefinition,
                this.name,
                parametersDefinition);
    }
}
