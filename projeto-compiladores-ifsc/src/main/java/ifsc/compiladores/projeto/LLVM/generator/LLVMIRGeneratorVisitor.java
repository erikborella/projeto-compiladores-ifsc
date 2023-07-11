package ifsc.compiladores.projeto.LLVM.generator;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.FragmentBlock;
import ifsc.compiladores.projeto.LLVM.definitions.Alloca;
import ifsc.compiladores.projeto.LLVM.definitions.Store;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.functions.Function;
import ifsc.compiladores.projeto.LLVM.definitions.functions.Parameter;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.ReferenceType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import ifsc.compiladores.projeto.LLVM.scopeManager.ScopeManager;
import ifsc.compiladores.projeto.LLVM.scopeManager.SingleUseVariablesManager;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;

import java.util.ArrayList;

public class LLVMIRGeneratorVisitor extends ParserGrammarBaseVisitor<Fragment> {

    private final ScopeManager scopeManager;
    private final SingleUseVariablesManager singleUseVariablesManager;

    public LLVMIRGeneratorVisitor() {
        this.scopeManager = new ScopeManager();
        this.singleUseVariablesManager = new SingleUseVariablesManager();
    }

    @Override
    public Fragment visitPrograma(ParserGrammar.ProgramaContext ctx) {
        FragmentBlock program = new FragmentBlock();

        for (ParserGrammar.DecfuncaoContext decfuncaoContext : ctx.decfuncao()) {
            program.add(visitDecfuncao(decfuncaoContext));
        }

        program.add(visitPrincipal(ctx.principal()));

        return program;
    }

    @Override
    public Function visitDecfuncao(ParserGrammar.DecfuncaoContext ctx) {
        ReferenceType type = visitTiporetorno(ctx.tiporetorno());
        String name = ctx.ID().getText();

        Function function = new Function(type, name);

        if (this.scopeManager.isFunctionDeclared(name)) {
            throw new IllegalStateException(String.format("Já existe uma função com o nome %s declarada.", name));
        }

        this.scopeManager.declareFunction(function);

        this.scopeManager.startScope();
        this.singleUseVariablesManager.resetVariables();

        if (ctx.parametros() != null) {
            ArrayList<Parameter> parameters = getParameters(ctx.parametros());
            function.getParameters().addAll(parameters);

            FragmentBlock parametersDeclaration = declareParameters(parameters);
            function.getBody().addAll(parametersDeclaration);
        }

        FragmentBlock functionBody = visitBloco(ctx.bloco());
        function.getBody().addAll(functionBody);

        this.scopeManager.finishScope();

        return function;
    }

    private FragmentBlock declareParameters(ArrayList<Parameter> parameters) {
        FragmentBlock parametersDeclaration = new FragmentBlock();

        for (Parameter parameter : parameters) {
            ReferenceType allocaReturnType = parameter.getVariable().referenceType().getNewReferencePointerToThis();
            Variable allocaReturnVariable = new Variable(allocaReturnType, parameter.getVariable().name());

            Alloca parameterDeclaration = new Alloca(allocaReturnVariable, parameter.getVariable().referenceType());
            parametersDeclaration.add(parameterDeclaration);

            Variable sourceParameterVariable = new Variable(parameter.getVariable().referenceType(),
                    parameter.getNameInParameterForm());
            Store parameterStore = new Store(sourceParameterVariable, allocaReturnVariable);
            parametersDeclaration.add(parameterStore);
        }

        return parametersDeclaration;
    }

    private ArrayList<Parameter> getParameters(ParserGrammar.ParametrosContext ctx) {
        ArrayList<Parameter> parameters = new ArrayList<>();

        for (int i = 0; i < ctx.ID().size(); i++) {
            ReferenceType type = visitTipo(ctx.tipo(i));
            String name = ctx.ID(i).getText();

            Variable variable = new Variable(type, name);

            if (this.scopeManager.isVariableDeclared(name)) {
                throw new IllegalStateException(String.format("Já existe um parametro com o nome %s declarado.", name));
            }

            this.scopeManager.declareVariable(variable);

            Parameter parameter = new Parameter(variable);

            parameters.add(parameter);
        }

        return parameters;
    }

    @Override
    public Function visitPrincipal(ParserGrammar.PrincipalContext ctx) {
        ReferenceType type = new Type(BaseType.INT).asReferenceType();
        String name = "main";

        return new Function(type, name);
    }

    @Override
    public FragmentBlock visitBloco(ParserGrammar.BlocoContext ctx) {
        FragmentBlock block = new FragmentBlock();

        for (ParserGrammar.DecvariavelContext decvariavelContext : ctx.decvariavel()) {
            FragmentBlock variableDeclarations = visitDecvariavel(decvariavelContext);

            block.addAll(variableDeclarations);
        }

        return block;
    }

    @Override
    public FragmentBlock visitDecvariavel(ParserGrammar.DecvariavelContext ctx) {
        FragmentBlock variableDeclarations = new FragmentBlock();
        ReferenceType variableType = visitTipo(ctx.tipo());

        for (int i = 0; i < ctx.ID().size(); i++) {
            String variableName = ctx.ID(i).getText();
            Variable variable = new Variable(variableType, variableName);

            if (this.scopeManager.isVariableDeclared(variableName)) {
                throw new IllegalStateException(String.format("Já existe uma variavel com o nome %s declarada.",
                        variableName));
            }

            this.scopeManager.declareVariable(variable);

            if (variableType.getType().isArrayType()) {
                FragmentBlock arrayVariableDeclaration = declareArrayVariable(variable, variableName);
                variableDeclarations.addAll(arrayVariableDeclaration);
            }
            else {
                Fragment valueVariableDeclaration = declareValueVariable(variable, variableName);
                variableDeclarations.add(valueVariableDeclaration);
            }
        }

        return variableDeclarations;
    }

    private Fragment declareValueVariable(Variable variable, String variableName) {
        ReferenceType referenceToValue = variable.referenceType().getNewReferencePointerToThis();
        Variable variableAllocaReturnType = new Variable(referenceToValue, variableName);

        return new Alloca(variableAllocaReturnType, variable.referenceType());
    }

    private FragmentBlock declareArrayVariable(Variable variable, String variableName) {
        FragmentBlock arrayDeclaration = new FragmentBlock();

        Variable arrayAllocaVariable = this.singleUseVariablesManager.getNewVariableOfType(variable.referenceType());
        ReferenceType arrayBaseType = variable.referenceType().getNewDeferencePointerOfThis();

        Alloca arrayAlloca = new Alloca(arrayAllocaVariable, arrayBaseType);
        arrayDeclaration.add(arrayAlloca);

        ReferenceType arrayReferenceType = variable.referenceType().getNewReferencePointerToThis();
        Variable referenceAllocaVariable = new Variable(arrayReferenceType, variableName);

        Alloca arrayReferenceAlloca = new Alloca(referenceAllocaVariable, variable.referenceType());
        arrayDeclaration.add(arrayReferenceAlloca);

        Store arrayReferenceStore = new Store(arrayAllocaVariable, referenceAllocaVariable);
        arrayDeclaration.add(arrayReferenceStore);

        return arrayDeclaration;
    }

    @Override
    public ReferenceType visitTiporetorno(ParserGrammar.TiporetornoContext ctx) {
        boolean isVoidType = ctx.TIPO_VOID() != null;

        if (isVoidType) {
            return new Type(BaseType.VOID).asReferenceType();
        }

        return visitTipo(ctx.tipo());
    }

    @Override
    public ReferenceType visitTipo(ParserGrammar.TipoContext ctx) {
        BaseType baseType = visitTipobase(ctx.tipobase());
        Type type = new Type(baseType);

        for (ParserGrammar.DimensaoContext dimensaoContext : ctx.dimensao()) {
            int dimension = Integer.parseInt(dimensaoContext.NUM_INT().getText());
            type.getDimensions().add(dimension);
        }

        if (type.isArrayType())
            return type.asReferenceType().getNewReferencePointerToThis();

        return type.asReferenceType();
    }

    @Override
    public BaseType visitTipobase(ParserGrammar.TipobaseContext ctx) {
        return switch (ctx.getStart().getType()) {
            case ParserGrammar.TIPO_INT -> BaseType.INT;
            case ParserGrammar.TIPO_BOOLEAN -> BaseType.BOOLEAN;
            case ParserGrammar.TIPO_FLOAT -> BaseType.FLOAT;
            case ParserGrammar.TIPO_CHAR -> BaseType.CHAR;
            default -> throw new IllegalStateException("Invalid type");
        };
    }
}