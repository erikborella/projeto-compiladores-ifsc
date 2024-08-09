package ifsc.compiladores.projeto.API.features.compiler;

import ifsc.compiladores.projeto.API.features.compiler.domain.FileCacheManager;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/compiler")
public class CompilerController {

    private final FileCacheManager fileHashService;

    public CompilerController(FileCacheManager fileHashService) {
        this.fileHashService = fileHashService;
    }

    @PostMapping("/upload")
    public String UploadCode(@RequestBody String code) throws IOException, NoSuchAlgorithmException {
        String codeTrimmed = code.trim();
        byte[] codeBytes = codeTrimmed.getBytes();

        String fileHash = fileHashService.computeFileId(codeBytes);

        fileHashService.saveFile(codeBytes);

        return fileHash;
    }

}
