package ifsc.compiladores.projeto.complexity.generator;

import ifsc.compiladores.projeto.complexity.definitions.BasicCommandCost;
import ifsc.compiladores.projeto.complexity.definitions.BlockCost;
import ifsc.compiladores.projeto.complexity.definitions.Cost;
import ifsc.compiladores.projeto.complexity.definitions.position.Position;
import ifsc.compiladores.projeto.complexity.definitions.position.TokenPosition;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import java.util.ArrayList;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import ifsc.compiladores.projeto.complexity.definitions.CostResult;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;


public class ComplexityAnalysisGeneratorVisitor extends ParserGrammarBaseVisitor<CostResult> {

    public void analyseCode(ParserGrammar.ProgramaContext ctx) {
        visitPrograma(ctx);
    }
    
    @Override
    public BlockCost visitPrograma(ParserGrammar.ProgramaContext ctx) {
        ArrayList<CostResult> costs = new ArrayList<>();
        
        for (ParserGrammar.DecfuncaoContext decfuncaoContext : ctx.decfuncao()) {
            BlockCost decFuncaoCost = visitDecfuncao(decfuncaoContext);
            costs.addAll(decFuncaoCost.getCosts());
            costs.add(decFuncaoCost.getBlockCost());
        }
        
        TokenPosition position = TokenPosition.fromContext(ctx);
        Cost programCost = new Cost(position, 0);
        
        BlockCost programBlockCost = new BlockCost(programCost, costs);
        
        return programBlockCost;
    }

    @Override
    public BlockCost visitDecfuncao(ParserGrammar.DecfuncaoContext ctx) {
        return visitBloco(ctx.bloco());
    }

    public BlockCost visitBloco(ParserGrammar.BlocoContext ctx) {
        ArrayList<CostResult> costs = new ArrayList<>();
        
        for (ParserGrammar.DecvariavelContext decvariavelContext : ctx.decvariavel()) {

            TokenPosition tokenPosition = TokenPosition.fromContext(decvariavelContext);
            Cost declarationCost = new Cost(tokenPosition, BasicCommandCost.DECLARATION.getCost());

            costs.add(declarationCost);
        }

        for (ParserGrammar.ComandoContext comandoContext : ctx.comando()) {
            CostResult c = visitComando(comandoContext);
            costs.add(c);
        }
        
        return new BlockCost(costs);
    };

    @Override
    public CostResult visitComandoLinhaLeitura(ParserGrammar.ComandoLinhaLeituraContext ctx) {
        TokenPosition tokenPosition = TokenPosition.fromContext(ctx);

        return new Cost(tokenPosition, BasicCommandCost.SCANF.getCost());
    }

    @Override
    public CostResult visitComandoLinhaEscritaLn(ParserGrammar.ComandoLinhaEscritaLnContext ctx) {
        TokenPosition tokenPosition = TokenPosition.fromContext(ctx);

        return new Cost(tokenPosition, BasicCommandCost.PRINT.getCost());
    }

    @Override
    public CostResult visitComandoLinhaEscrita(ParserGrammar.ComandoLinhaEscritaContext ctx) {
        TokenPosition tokenPosition = TokenPosition.fromContext(ctx);

        return new Cost(tokenPosition, BasicCommandCost.PRINT.getCost());
    }

    @Override
    public CostResult visitComandoLinhaAtribuicao(ParserGrammar.ComandoLinhaAtribuicaoContext ctx) {
        TokenPosition tokenPosition = TokenPosition.fromContext(ctx);

        return new Cost(tokenPosition, BasicCommandCost.ATTRIBUITION.getCost());
    }

    @Override
    public CostResult visitComandoLinhaRetorno(ParserGrammar.ComandoLinhaRetornoContext ctx) {
        TokenPosition tokenPosition = TokenPosition.fromContext(ctx);

        return new Cost(tokenPosition, BasicCommandCost.RETURN.getCost());
    }

    @Override
    public CostResult visitChildren(RuleNode node) {
        CostResult result = defaultResult();
        int n = node.getChildCount();
        for (int i=0; i<n; i++) {
            if (!shouldVisitNextChild(node, result)) {
                break;
            }

            ParseTree c = node.getChild(i);

            if (c instanceof TerminalNodeImpl)
                continue;

            CostResult childResult = c.accept(this);
            result = aggregateResult(result, childResult);
        }

        return result;
    }
}
