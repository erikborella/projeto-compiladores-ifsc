package ifsc.compiladores.projeto.API.features.compiler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
//        reason = "The opt level informed is invalid. Valid opt levels: o1, o2, o3, os and oz"
public class InvalidOptLevelException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "The opt level informed is invalid \"%s\". Valid opt levels are: o1, o2, o3, os and oz";

    public InvalidOptLevelException(String message) {
        super(message);
    }

    public static InvalidOptLevelException inOptLevel(String optLevel) {
        String message = String.format(DEFAULT_MESSAGE, optLevel);

        return new InvalidOptLevelException(message);
    }
}
