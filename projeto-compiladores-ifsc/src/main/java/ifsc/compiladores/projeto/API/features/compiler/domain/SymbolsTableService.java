package ifsc.compiladores.projeto.API.features.compiler.domain;

import java.io.IOException;
import java.util.Optional;

public interface SymbolsTableService {
    Optional<String> getSymbolsTable(String codeId) throws IOException;
}
