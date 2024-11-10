package ifsc.compiladores.projeto.API.features.compiler.domain;

import java.io.IOException;
import java.util.Optional;

public interface ComplexityAnalysisService {
    Optional<String> getComplexityAnalysis(String codeId) throws IOException;
}
