package ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.generator;

import ifsc.compiladores.projeto.LLVM.translator.FragmentBlock;
import ifsc.compiladores.projeto.common.position.Position;
import ifsc.compiladores.projeto.common.position.TokenPosition;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions.Function;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions.SymbolsTable;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions.Variable;

import java.util.ArrayList;
import java.util.List;

public class SymbolsTableGeneratorVisitor extends ParserGrammarBaseVisitor<Void> {
    private final ScopeManagerEmulator scopeManagerEmulator;

    public SymbolsTableGeneratorVisitor() {
        this.scopeManagerEmulator = new ScopeManagerEmulator();
    }

    public SymbolsTable getSymbolsTable() {
        return scopeManagerEmulator.getSymbolsTable();
    }

    @Override
    public Void visitPrincipal(ParserGrammar.PrincipalContext ctx) {
        String type = "int";
        String name = "main";

        TokenPosition position = TokenPosition.fromContext(ctx);

        Function mainFunction = new Function(type, name, position, null);

        this.scopeManagerEmulator.declareFunction(mainFunction);

        this.scopeManagerEmulator.startScope(position);

        visitBloco(ctx.bloco());

        this.scopeManagerEmulator.finishScope();

        return null;
    }

    @Override
    public Void visitDecfuncao(ParserGrammar.DecfuncaoContext ctx) {
        String returnType = ctx.tiporetorno().getText();
        String name = ctx.ID().getText();

        TokenPosition position = TokenPosition.fromContext(ctx);

        this.scopeManagerEmulator.startScope(position);

        ArrayList<Variable> parameters = null;

        if (ctx.parametros() != null) {
            parameters = this.getParameters(ctx.parametros());
        }

        Function function = new Function(returnType, name, position, parameters);

        this.scopeManagerEmulator.declareFunction(function);

        visitBloco(ctx.bloco());

        this.scopeManagerEmulator.finishScope();

        return null;
    }

    private ArrayList<Variable> getParameters(ParserGrammar.ParametrosContext ctx) {
        ArrayList<Variable> parameters = new ArrayList<>();

        for (int i = 0; i < ctx.ID().size(); i++) {
            String type = ctx.tipo(i).getText();
            String name = ctx.ID(i).getText();

            Variable parameterVariable = new Variable(type, name);

            this.scopeManagerEmulator.declareVariable(parameterVariable);

            parameters.add(parameterVariable);
        }

        return parameters;
    }

    @Override
    public Void visitBloco(ParserGrammar.BlocoContext ctx) {
        for (ParserGrammar.DecvariavelContext decvariavelContext : ctx.decvariavel()) {
            visitDecvariavel(decvariavelContext);
        }

        for (ParserGrammar.ComandoContext comandoContext : ctx.comando()) {
            visitComando(comandoContext);
        }

        return null;
    }

    @Override
    public Void visitDecvariavel(ParserGrammar.DecvariavelContext ctx) {
        String type = ctx.tipo().getText();

        for (int i = 0; i < ctx.ID().size(); i++) {
            String name = ctx.ID(i).getText();

            Variable variable = new Variable(type, name);

            this.scopeManagerEmulator.declareVariable(variable);
        }

        return null;
    }

    @Override
    public Void visitSelecao(ParserGrammar.SelecaoContext ctx) {
        TokenPosition scopePosition = TokenPosition.fromContext(ctx.bloco());
        this.scopeManagerEmulator.startScope(scopePosition);

        visitBloco(ctx.bloco());

        this.scopeManagerEmulator.finishScope();

        if (ctx.senao() != null) {
            TokenPosition elseScopePosition = TokenPosition.fromContext(ctx.senao().bloco());
            this.scopeManagerEmulator.startScope(elseScopePosition);

            visitBloco(ctx.senao().bloco());

            this.scopeManagerEmulator.finishScope();
        }

        return null;
    }

    @Override
    public Void visitEnquanto(ParserGrammar.EnquantoContext ctx) {
        TokenPosition scopePosition = TokenPosition.fromContext(ctx.bloco());
        this.scopeManagerEmulator.startScope(scopePosition);

        visitBloco(ctx.bloco());

        this.scopeManagerEmulator.finishScope();

        return null;
    }

    @Override
    public Void visitPara(ParserGrammar.ParaContext ctx) {
        TokenPosition scopePosition = TokenPosition.fromContext(ctx.bloco());
        this.scopeManagerEmulator.startScope(scopePosition);

        visitBloco(ctx.bloco());

        this.scopeManagerEmulator.finishScope();

        return null;
    }

    @Override
    public Void visitComandoLinhaEscritaLn(ParserGrammar.ComandoLinhaEscritaLnContext ctx) {
        String stringDeclaration = ctx.escritaln().TEXTO().getText();

        this.scopeManagerEmulator.declareString(stringDeclaration);

        for (ParserGrammar.TermoescritaContext termoescritaContext : ctx.escritaln().termoescrita()) {
            visit(termoescritaContext);
        }

        return null;
    }

    @Override
    public Void visitComandoLinhaEscrita(ParserGrammar.ComandoLinhaEscritaContext ctx) {
        String stringDeclaration = ctx.escrita().TEXTO().getText();

        this.scopeManagerEmulator.declareString(stringDeclaration);

        for (ParserGrammar.TermoescritaContext termoescritaContext : ctx.escrita().termoescrita()) {
            visit(termoescritaContext);
        }

        return null;
    }

    @Override
    public Void visitTermoEscritaTexto(ParserGrammar.TermoEscritaTextoContext ctx) {
        String stringDeclaration = ctx.TEXTO().getText();

        this.scopeManagerEmulator.declareString(stringDeclaration);

        return null;
    }

}
