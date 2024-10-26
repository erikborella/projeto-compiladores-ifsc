package ifsc.compiladores.projeto.API.features.compiler.domain;

import java.io.IOException;
import java.util.Optional;

public interface SyntaxTreeService {
    Optional<String> getSyntaxTree(String codeId) throws IOException;
}
