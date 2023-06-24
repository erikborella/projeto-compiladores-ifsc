package ifsc.compiladores.projeto.LLVM.generator;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.FragmentBlock;
import ifsc.compiladores.projeto.LLVM.definitions.functions.Function;
import ifsc.compiladores.projeto.LLVM.definitions.functions.Parameter;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import ifsc.compiladores.projeto.LLVM.scopeManager.ScopeManager;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;

public class LLVMIRGeneratorVisitor extends ParserGrammarBaseVisitor<Fragment> {

    private final ScopeManager scopeManager;

    public LLVMIRGeneratorVisitor() {
        this.scopeManager = new ScopeManager();
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
        Type type = visitTiporetorno(ctx.tiporetorno());
        String name = ctx.ID().getText();

        Function function = new Function(type, name);

        if (this.scopeManager.isFunctionDeclared(name)) {
            throw new IllegalStateException(String.format("Já existe uma função com o nome %s declarada.", name));
        }

        ArrayList<Parameter> parameters = getParameters(ctx.parametros());
        function.getParameters().addAll(parameters);

        this.scopeManager.declareFunction(function);

        return function;
    }

    private ArrayList<Parameter> getParameters(ParserGrammar.ParametrosContext ctx) {
        ArrayList<Parameter> parameters = new ArrayList<>();

        for (int i = 0; i < ctx.ID().size(); i++) {
            Type type = visitTipo(ctx.tipo(i));
            String name = ctx.ID(i).getText();

            Parameter parameter = new Parameter(type, name);

            parameters.add(parameter);
        }

        return parameters;
    }

    @Override
    public Function visitPrincipal(ParserGrammar.PrincipalContext ctx) {
        Type type = new Type(BaseType.INT);
        String name = "main";

        Function mainFunction = new Function(type, name);

        return mainFunction;
    }

    @Override
    public Fragment visitParametros(ParserGrammar.ParametrosContext ctx) {
        return super.visitParametros(ctx);
    }

    @Override
    public Type visitTiporetorno(ParserGrammar.TiporetornoContext ctx) {
        boolean isVoidType = ctx.TIPO_VOID() != null;

        if (isVoidType) {
            return new Type(BaseType.VOID);
        }

        return visitTipo(ctx.tipo());
    }

    @Override
    public Type visitTipo(ParserGrammar.TipoContext ctx) {
        BaseType baseType = visitTipobase(ctx.tipobase());
        Type type = new Type(baseType);

        for (ParserGrammar.DimensaoContext dimensaoContext : ctx.dimensao()) {
            int dimension = Integer.parseInt(dimensaoContext.NUM_INT().getText());
            type.getDimensions().add(dimension);
        }

        return type;
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
