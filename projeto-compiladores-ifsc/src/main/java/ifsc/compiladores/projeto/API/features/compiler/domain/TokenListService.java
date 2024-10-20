package ifsc.compiladores.projeto.API.features.compiler.domain;

import java.io.IOException;
import java.util.Optional;

public interface TokenListService {
    Optional<String> getTokenList(String codeId) throws IOException;
}
