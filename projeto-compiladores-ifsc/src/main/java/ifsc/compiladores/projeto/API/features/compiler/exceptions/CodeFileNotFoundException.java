package ifsc.compiladores.projeto.API.features.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CodeFileNotFoundException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Code file with ID \"%s\" was not found on the server. Check if the ID used is correct or the code was uploaded.";

    public CodeFileNotFoundException(String message) {
        super(message);
    }

    public static CodeFileNotFoundException inCodeId(String codeId) {
        String message = String.format(DEFAULT_MESSAGE, codeId);

        return new CodeFileNotFoundException(message);
    }
}