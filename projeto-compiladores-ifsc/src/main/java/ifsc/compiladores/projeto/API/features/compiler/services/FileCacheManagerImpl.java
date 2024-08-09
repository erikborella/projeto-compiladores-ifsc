package ifsc.compiladores.projeto.API.features.compiler.services;

import ifsc.compiladores.projeto.API.configuration.FileCacheConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.domain.FileCacheManager;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class FileCacheManagerImpl implements FileCacheManager {
    private final FileCacheConfiguration configuration;

    public FileCacheManagerImpl(FileCacheConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String computeFileId(byte[] fileBytes) throws NoSuchAlgorithmException {
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
    public String saveFile(byte[] fileBytes) throws NoSuchAlgorithmException, IOException {
        String fileId = this.computeFileId(fileBytes);

        File filePath = Paths.get(this.configuration.getPath(), fileId, this.configuration.getFileName()).toFile();

        filePath.getParentFile().mkdirs();

        Files.write(filePath.toPath(), fileBytes);

        return fileId;
    }
}
