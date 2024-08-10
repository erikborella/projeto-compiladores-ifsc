package ifsc.compiladores.projeto.API.features.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.NOT_FOUND,
        reason = "Code was not found in the server, check if the ID used is correct or upload the code"
)
public class CodeFileNotFoundException extends RuntimeException {
}
