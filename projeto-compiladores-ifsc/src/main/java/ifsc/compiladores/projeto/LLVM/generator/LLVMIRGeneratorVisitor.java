package ifsc.compiladores.projeto.LLVM.generator;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.FragmentBlock;
import ifsc.compiladores.projeto.LLVM.FragmentTest;
import ifsc.compiladores.projeto.LLVM.definitions.types.Function;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

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
        ParserGrammar.TipobaseContext tipobaseContext = ctx.tipobase();

        if (tipobaseContext.TIPO_INT() != null) {
            return Type.INT;
        }

        if (tipobaseContext.TIPO_BOOLEAN() != null) {
            return Type.BOOLEAN;
        }

        if (tipobaseContext.TIPO_FLOAT() != null) {
            return Type.FLOAT;
        }

        if (tipobaseContext.TIPO_CHAR() != null) {
            return Type.CHAR;
        }

        //Unreachable code
        return null;
    }
}
