package ifsc.compiladores.projeto.API.features.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CompilationException extends RuntimeException {
    public CompilationException(String message) {
        super(message);
    }
}
