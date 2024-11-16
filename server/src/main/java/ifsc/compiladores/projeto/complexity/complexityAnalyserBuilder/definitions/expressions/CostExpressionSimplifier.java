package ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.expressions;

import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.expressions.lexer.CostExpressionToken;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.expressions.lexer.CostExpressionTokenizer;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.expressions.lexer.CostExpressionType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class CostExpressionSimplifier {
    public static String simplify(String expression) {
        List<CostExpressionToken> expressionTokens = CostExpressionTokenizer.tokenize(expression);

        Queue<CostExpressionToken> tokensQueue = new LinkedList<>(expressionTokens);

        ArrayList<CostExpressionToken> expressionSimplified = new ArrayList<>();

        while (!tokensQueue.isEmpty()) {
            CostExpressionToken currentToken = tokensQueue.peek();

            if (currentToken.getType() != CostExpressionType.NUMBER) {
                expressionSimplified.add(tokensQueue.remove());
                continue;
            }

            tokensQueue.remove();

            if (tokensQueue.isEmpty() || tokensQueue.peek().getType() != CostExpressionType.PLUS_SIGN) {
                expressionSimplified.add(currentToken);
                continue;
            }

            CostExpressionToken sign = tokensQueue.remove();

            if (tokensQueue.isEmpty() || tokensQueue.peek().getType() != CostExpressionType.NUMBER) {
                expressionSimplified.add(currentToken);
                expressionSimplified.add(sign);
                continue;
            }

            CostExpressionToken rightValue = tokensQueue.remove();

            int summedValues = ((int)currentToken.getValue()) + ((int)rightValue.getValue());
            CostExpressionToken summedValuesToken = new CostExpressionToken(summedValues, CostExpressionType.NUMBER);

            expressionSimplified.add(summedValuesToken);
        }

        String tokensString = expressionSimplified.stream()
                .map(c -> {
                    if (c.getType() == CostExpressionType.PLUS_SIGN) {
                        return " + ";
                    }

                    return c.getValue().toString();
                })
                .collect(Collectors.joining());

        return tokensString;
    }
}
