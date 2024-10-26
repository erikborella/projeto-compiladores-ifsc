package ifsc.compiladores.projeto.LLVM.translator.errors;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SyntaxErrorListener extends BaseErrorListener {
    private final List<String> syntaxErrors = new ArrayList<>();

    public SyntaxErrorListener() {
    }

    public List<String> getSyntaxErrors() {
        return syntaxErrors;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
                            Object offendingSymbol,
                            int line,
                            int charPositionInLine,
                            String msg,
                            RecognitionException e) {
        this.syntaxErrors.add(msg);
    }

    @Override
    public String toString() {
        return String.join(", ", this.syntaxErrors);
    }
}
