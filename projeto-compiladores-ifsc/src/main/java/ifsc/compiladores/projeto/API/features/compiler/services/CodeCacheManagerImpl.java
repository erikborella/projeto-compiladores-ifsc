package ifsc.compiladores.projeto.API.features.compiler.services;

import ifsc.compiladores.projeto.API.configuration.CompilerConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.domain.CodeCacheManager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class CodeCacheManagerImpl implements CodeCacheManager {
    private final CompilerConfiguration configuration;

    public CodeCacheManagerImpl(CompilerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String computeCodeId(byte[] fileBytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fileBytes);

        BigInteger no = new BigInteger(1, hashBytes);
        StringBuilder fileHashBuilder = new StringBuilder(no.toString(16));

        while (fileHashBuilder.length() < 64) {
            fileHashBuilder.insert(0, "0");
        }

        return fileHashBuilder.toString();
    }

    @Override
    public String saveCodeFile(String codeContent) throws NoSuchAlgorithmException, IOException {
        byte[] codeBytes = codeContent.getBytes();

        String codeId = this.computeCodeId(codeBytes);

        this.saveCodeArtifact(codeId, this.configuration.getCodeFileName(), codeBytes);

        return codeId;
    }

    @Override
    public Optional<String> loadCodeFromId(String codeId) throws IOException {
        return this.loadCodeArtifact(codeId, this.configuration.getCodeFileName());
    }

    @Override
    public void saveCodeArtifact(String codeId, String artifactName, String artifactContent) throws IOException {
        this.saveCodeArtifact(codeId, artifactName, artifactContent.getBytes());
    }

    public void saveCodeArtifact(String codeId, String artifactName, byte[] artifactContent) throws IOException {
        File artifactPath = this.buildFilePath(codeId, artifactName);

        artifactPath.getParentFile().mkdirs();

        Files.write(artifactPath.toPath(), artifactContent);
    }

    @Override
    public Optional<String> loadCodeArtifact(String codeId, String artifactName) throws IOException {
        File filePath = this.buildFilePath(codeId, artifactName);

        if (!filePath.exists()) {
            return Optional.empty();
        }

        String codeContent = Files.readString(filePath.toPath());

        return Optional.of(codeContent);
    }

    @Override
    public boolean artifactExists(String codeId, String artifactName) throws IOException {
        File filePath = this.buildFilePath(codeId, artifactName);

        return filePath.exists();
    }

    public File buildFilePath(String codeId, String fileName) {
        return Paths.get(this.configuration.getCachePath(), codeId, fileName).toFile();
    }
}
