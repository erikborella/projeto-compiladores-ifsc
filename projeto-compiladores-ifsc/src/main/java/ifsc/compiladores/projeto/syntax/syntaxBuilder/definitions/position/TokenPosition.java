package ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.position;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public record TokenPosition(Position start, Position end) {

    public static TokenPosition fromContext(ParserRuleContext context) {
        Position start = new Position(context.start.getLine(), context.start.getCharPositionInLine() + 1);
        
        int stopInitialPositionInLine = context.stop.getCharPositionInLine() + 1;
        int stopFinalPositionInLine = context.stop.getText().length() + stopInitialPositionInLine;
       
        Position end = new Position(context.stop.getLine(), stopFinalPositionInLine);
        return new TokenPosition(start, end);
    }

    public static TokenPosition fromToken(Token token) {
        int startPositionInLine = token.getCharPositionInLine() + 1;
        int stopPositionInLine =  token.getText().length() + startPositionInLine;
        
        Position start = new Position(token.getLine(), startPositionInLine);
        Position end = new Position(token.getLine(), stopPositionInLine);
        return new TokenPosition(start, end);
    }
}
