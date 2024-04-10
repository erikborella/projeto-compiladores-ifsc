package ifsc.compiladores.projeto.complexity.generator;

import ifsc.compiladores.projeto.complexity.definitions.BasicCommandCost;
import ifsc.compiladores.projeto.complexity.definitions.Cost;
import ifsc.compiladores.projeto.complexity.definitions.position.Position;
import ifsc.compiladores.projeto.complexity.definitions.position.TokenPosition;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import java.util.ArrayList;
import org.antlr.v4.runtime.tree.TerminalNode;


public class ComplexityAnalysisGeneratorVisitor extends ParserGrammarBaseVisitor<ArrayList<Cost>> {

    public void analyseCode(ParserGrammar.ProgramaContext ctx) {
        visitPrograma(ctx);
    }
    
    @Override
    public ArrayList<Cost> visitPrograma(ParserGrammar.ProgramaContext ctx) {
        ArrayList<Cost> costs = new ArrayList<>();
        
        for (ParserGrammar.DecfuncaoContext decfuncaoContext : ctx.decfuncao()) {
            costs.addAll(visitDecfuncao(decfuncaoContext));
        }
        
        return costs;
    }

    @Override
    public ArrayList<Cost> visitDecfuncao(ParserGrammar.DecfuncaoContext ctx) {
        ArrayList<Cost> costs = new ArrayList<>();
        
        costs.addAll(visitBloco(ctx.bloco()));
        
        return costs;
    }

    @Override
    public ArrayList<Cost> visitBloco(ParserGrammar.BlocoContext ctx) {
        ArrayList<Cost> costs = new ArrayList<>();
        
        for (ParserGrammar.DecvariavelContext decvariavelContext : ctx.decvariavel()) {
            
            if (decvariavelContext.ID().size() > 1) {
                for (TerminalNode variableNode : decvariavelContext.ID()) {
                    Position position = new Position(
                            variableNode.getSymbol().getLine(), 
                            variableNode.getSymbol().getCharPositionInLine()
                    );
                    TokenPosition tokenPosition = new TokenPosition(position, position);

                    Cost declarationCost = new Cost(tokenPosition, BasicCommandCost.DECLARATION.getCost());
                    costs.add(declarationCost);
                }
            }
            
            int totalDeclarationCost = decvariavelContext.ID().size() * BasicCommandCost.DECLARATION.getCost();
            
            TokenPosition tokenPosition = TokenPosition.fromContext(decvariavelContext);
            Cost declarationTotalCost = new Cost(tokenPosition, totalDeclarationCost);
           
            costs.add(declarationTotalCost);
        }
        
        return costs;
    }
    
}
