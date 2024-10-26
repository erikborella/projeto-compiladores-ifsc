package ifsc.compiladores.projeto.complexity.definitions.position;

import org.antlr.v4.runtime.ParserRuleContext;

public record TokenPosition(Position start, Position end) {
    
    public static TokenPosition fromContext(ParserRuleContext context) {
        Position start = new Position(context.start.getLine(), context.start.getCharPositionInLine());
        Position end  = new Position(context.stop.getLine(), context.stop.getCharPositionInLine());
        
        return new TokenPosition(start, end);
    }
}
