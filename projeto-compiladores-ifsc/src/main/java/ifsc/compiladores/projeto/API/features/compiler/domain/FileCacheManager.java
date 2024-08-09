package ifsc.compiladores.projeto.API.features.compiler.domain;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface FileCacheManager {
    String computeFileId(byte[] fileBytes) throws NoSuchAlgorithmException;
    String saveFile(byte[] fileBytes) throws NoSuchAlgorithmException, IOException;
}
