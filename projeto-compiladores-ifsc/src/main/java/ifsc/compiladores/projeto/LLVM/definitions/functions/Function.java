package ifsc.compiladores.projeto.LLVM.definitions.functions;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.FragmentBlock;
import ifsc.compiladores.projeto.LLVM.definitions.types.ReferenceType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Function implements Fragment {
    private final ReferenceType returnType;
    private final String name;
    private final ArrayList<Parameter> parameters;
    private final FragmentBlock body;

    public Function(ReferenceType returnType, String name) {
        this.returnType = returnType;
        this.name = name;
        this.parameters = new ArrayList<>();
        this.body = new FragmentBlock();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public ReferenceType getReturnType() {
        return returnType;
    }

    public FragmentBlock getBody() {
        return body;
    }

    @Override
    public String getText() {
        String parametersDefinition = this.parameters.stream()
                .map(Parameter::getText)
                .collect(Collectors.joining(", "));

        return String.format("define %s @%s(%s) {\n%s\n}",
                this.returnType.getText(),
                this.name,
                parametersDefinition,
                this.body.getIndentedText(1));
    }
}
