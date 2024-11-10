package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.generator;

import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.*;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.position.TokenPosition;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.variableManager.Variable;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.variableManager.VariableManager;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import java.util.ArrayList;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;


public class ComplexityAnalysisGeneratorVisitor extends ParserGrammarBaseVisitor<CostResult> {

    private final VariableManager variableManager;

    public ComplexityAnalysisGeneratorVisitor() {
        this.variableManager = new VariableManager();
    }

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
        this.variableManager.resetVariables();

        if (ctx.parametros() != null) {
            for (int i = 0; i < ctx.parametros().ID().size(); i++) {
                boolean isInput = i == 0 && ctx.parametros().INPUT() != null;
                Variable parameterVariable = new Variable(ctx.parametros().ID(i).getText(), null, isInput);

                this.variableManager.addVariable(parameterVariable);
            }
        }

        return visitBloco(ctx.bloco());
    }

    public BlockCost visitBloco(ParserGrammar.BlocoContext ctx) {
        ArrayList<CostResult> costs = new ArrayList<>();
        
        for (ParserGrammar.DecvariavelContext decvariavelContext : ctx.decvariavel()) {

            TokenPosition tokenPosition = TokenPosition.fromContext(decvariavelContext);
            Cost declarationCost = new Cost(tokenPosition, BasicCommandCost.DECLARATION.getCost());

            costs.add(declarationCost);
        }
        
        int blockTotalCostsValue = 0;

        for (ParserGrammar.ComandoContext comandoContext : ctx.comando()) {
            CostResult c = visitComando(comandoContext);
            costs.add(c);
            
            blockTotalCostsValue += c.getValue();
        }
        
        TokenPosition blockTotalCostPosition = TokenPosition.fromContext(ctx);
        Cost blockTotalCost = new Cost(blockTotalCostPosition, blockTotalCostsValue);
        
        return new BlockCost(blockTotalCost, costs);
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
    public CostResult visitSelecao(ParserGrammar.SelecaoContext ctx) {
        CostResult ifBlockCost = visitBloco(ctx.bloco());
        
        if (ctx.senao() == null)
            return ifBlockCost;
        
        CostResult elseBlockCost = visitBloco(ctx.senao().bloco());
                
        // Returns the block with the greatest costs
        if (elseBlockCost.getValue() > ifBlockCost.getValue())
            return elseBlockCost;
        
        return ifBlockCost;
    }

    @Override
    public CostResult visitPara(ParserGrammar.ParaContext ctx) {
        String forExpressionSizeVariableId = ctx.expressao().expr_ou().expr_e(0).expr_relacional(0).expr_aditiva(1).getText();
        Variable forExpressionSizeVariable = this.variableManager.getVariable(forExpressionSizeVariableId);

        if (forExpressionSizeVariable == null || !forExpressionSizeVariable.isInput())
            return new NullCost();

        if (!ctx.expressao().expr_ou().expr_e(0).expr_relacional(0).op_relacional(0).getText().equals("<"))
            return new NullCost();

        ArrayList<CostResult> costs = new ArrayList<>();

        Cost forExpressionCost = new Cost(TokenPosition.fromContext(ctx.expressao()), BasicCommandCost.EXPRESSION.getCost());
        costs.add(forExpressionCost);

        int initialVariableValue = Integer.parseInt(ctx.atribuicaoInicio.atribuicao(0).complemento().getText());

        int costRange = initialVariableValue * -1;
        BlockCost forBlockCost = visitBloco(ctx.bloco());

        VariableCost forVariableCost = new VariableCost(forExpressionSizeVariableId, costRange, forBlockCost);
        costs.add(forVariableCost);

        return new BlockCost(costs);
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
