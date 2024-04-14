package ifsc.compiladores.projeto.complexity.generator;

import ifsc.compiladores.projeto.complexity.definitions.BasicCommandCost;
import ifsc.compiladores.projeto.complexity.definitions.BlockCost;
import ifsc.compiladores.projeto.complexity.definitions.Cost;
import ifsc.compiladores.projeto.complexity.definitions.position.Position;
import ifsc.compiladores.projeto.complexity.definitions.position.TokenPosition;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import java.util.ArrayList;
import org.antlr.v4.runtime.tree.TerminalNode;
import ifsc.compiladores.projeto.complexity.definitions.CostResult;


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
        ArrayList<CostResult> costs = new ArrayList<>();
        
        BlockCost blockCost = visitBloco(ctx.bloco());
        
        return null;
    }

    public BlockCost visitBloco(ParserGrammar.BlocoContext ctx) {
        ArrayList<CostResult> costs = new ArrayList<>();
        
//        for (ParserGrammar.DecvariavelContext decvariavelContext : ctx.decvariavel()) {
//            
//            if (decvariavelContext.ID().size() > 1) {
//                for (TerminalNode variableNode : decvariavelContext.ID()) {
//                    Position position = new Position(
//                            variableNode.getSymbol().getLine(), 
//                            variableNode.getSymbol().getCharPositionInLine()
//                    );
//                    TokenPosition tokenPosition = new TokenPosition(position, position);
//
//                    CostResult declarationCost = new CostResult(tokenPosition, BasicCommandCost.DECLARATION.getCost(), true);
//                    costs.add(declarationCost);
//                }
//            }
//            
//            int totalDeclarationCost = decvariavelContext.ID().size() * BasicCommandCost.DECLARATION.getCost();
//            
//            TokenPosition tokenPosition = TokenPosition.fromContext(decvariavelContext);
//            CostResult declarationTotalCost = new CostResult(tokenPosition, totalDeclarationCost);
//           
//            costs.add(declarationTotalCost);
//        }
        
        return null;
    };
    
    
}
