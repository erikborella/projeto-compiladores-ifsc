package ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.position;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

public record TokenPosition(Position start, Position end) {

    public static TokenPosition fromContext(ParserRuleContext context) {
        Position start = new Position(context.start.getLine(), context.start.getCharPositionInLine());
        Position end = new Position(context.stop.getLine(), context.stop.getCharPositionInLine());
        return new TokenPosition(start, end);
    }

    public static TokenPosition fromToken(Token token) {
        Position start = new Position(token.getLine(), token.getStartIndex());
        Position end = new Position(token.getLine(), token.getStopIndex());
        return new TokenPosition(start, end);
    }
}
