package ifsc.compiladores.projeto.LLVM.generator;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.FragmentBlock;
import ifsc.compiladores.projeto.LLVM.definitions.Function;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import org.antlr.v4.runtime.Token;

public class LLVMIRGeneratorVisitor extends ParserGrammarBaseVisitor<Fragment> {
    @Override
    public Fragment visitPrograma(ParserGrammar.ProgramaContext ctx) {
        FragmentBlock program = new FragmentBlock();

        for (ParserGrammar.DecfuncaoContext decfuncaoContext : ctx.decfuncao()) {
            program.add(visitDecfuncao(decfuncaoContext));
        }

        return program;
    }

    @Override
    public Fragment visitDecfuncao(ParserGrammar.DecfuncaoContext ctx) {
        Type type = visitTiporetorno(ctx.tiporetorno());
        String name = ctx.ID().getText();

        Function function = new Function(type, name);

        return function;
    }

    @Override
    public Type visitTiporetorno(ParserGrammar.TiporetornoContext ctx) {
        boolean isVoidType = ctx.TIPO_VOID() != null;

        if (isVoidType) {
            return Type.VOID;
        }

        return visitTipo(ctx.tipo());
    }

    @Override
    public Type visitTipo(ParserGrammar.TipoContext ctx) {
        Token tipobaseToken = ctx.tipobase().getStart();

        return switch (tipobaseToken.getType()) {
            case ParserGrammar.TIPO_INT -> Type.INT;
            case ParserGrammar.TIPO_BOOLEAN -> Type.BOOLEAN;
            case ParserGrammar.TIPO_FLOAT -> Type.FLOAT;
            case ParserGrammar.TIPO_CHAR -> Type.CHAR;
            default -> throw new IllegalStateException("Invalid type");
        };
    }
}
