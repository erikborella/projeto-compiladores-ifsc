package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.expressions.lexer;

import java.util.*;

public class CostExpressionTokenizer {
    public static List<CostExpressionToken> tokenize(String expression) {
        ArrayList<CostExpressionToken> tokens = new ArrayList<>();

        List<Character> expressionChars = expression.chars()
                .mapToObj(c -> (char) c)
                .toList();

        Queue<Character> charsQueue = new LinkedList<>(expressionChars);

        while (!charsQueue.isEmpty()) {
            char currentChar = charsQueue.peek();

            if (currentChar == ' ') {
                charsQueue.remove();
                continue;
            }

            if (Character.isDigit(currentChar)) {
                tokens.add(tokenizeNumber(charsQueue));
                continue;
            }

            if (currentChar == '+') {
                tokens.add(new CostExpressionToken(currentChar, CostExpressionType.PLUS_SIGN));
                charsQueue.remove();
                continue;
            }

            tokens.add(new CostExpressionToken(currentChar, CostExpressionType.OTHER));
            charsQueue.remove();
        }

        return tokens;
    }

    private static CostExpressionToken tokenizeNumber(Queue<Character> charsStack) {
        StringBuilder numberString = new StringBuilder();

        while (!charsStack.isEmpty() && Character.isDigit(charsStack.peek())) {
            numberString.append(charsStack.remove());
        }

        int number = Integer.parseInt(numberString.toString());

        return new CostExpressionToken(number, CostExpressionType.NUMBER);
    }
}
