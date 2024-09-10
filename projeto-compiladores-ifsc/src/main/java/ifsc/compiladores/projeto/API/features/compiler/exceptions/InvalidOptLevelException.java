package ifsc.compiladores.projeto.API.features.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(
        code = HttpStatus.BAD_REQUEST,
        reason = "The opt level informed is invalid. Valid opt levels: o1, o2, o3, os and oz"
)
public class InvalidOptLevelException extends RuntimeException {
}
