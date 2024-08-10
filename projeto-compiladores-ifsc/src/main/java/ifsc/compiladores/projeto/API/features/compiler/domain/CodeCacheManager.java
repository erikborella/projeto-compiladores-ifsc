package ifsc.compiladores.projeto.API.features.compiler.domain;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public interface CodeCacheManager {
    String computeCodeId(byte[] fileBytes) throws NoSuchAlgorithmException;
    String saveCodeFile(String codeContent) throws NoSuchAlgorithmException, IOException;
    Optional<String> loadCodeFromId(String codeId) throws IOException;
    void saveCodeArtifact(String codeId, String artifactName, String artifactContent) throws IOException;
    Optional<String> loadCodeArtifact(String codeId, String artifactName) throws IOException;
}
