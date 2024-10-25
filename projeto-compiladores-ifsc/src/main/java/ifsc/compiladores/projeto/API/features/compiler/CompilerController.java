package ifsc.compiladores.projeto.API.features.compiler;

import ifsc.compiladores.projeto.API.features.compiler.domain.*;
import ifsc.compiladores.projeto.API.features.compiler.exceptions.CodeFileNotFoundException;
import ifsc.compiladores.projeto.API.features.compiler.exceptions.CompilationException;
import ifsc.compiladores.projeto.API.features.compiler.exceptions.InvalidOptLevelException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/compiler")
public class CompilerController {

    private static final Logger log = LogManager.getLogger(CompilerController.class);
    private final CodeCacheManager codeCacheManager;
    private final LLVMCompilerService llvmCompilerService;
    private final SyntaxTreeService syntaxTreeService;
    private final TokenListService tokenListService;


    public CompilerController(CodeCacheManager codeCacheManager,
                              LLVMCompilerService llvmCompilerService,
                              SyntaxTreeService syntaxTreeService,
                              TokenListService tokenListService) {
        this.codeCacheManager = codeCacheManager;
        this.llvmCompilerService = llvmCompilerService;
        this.syntaxTreeService = syntaxTreeService;
        this.tokenListService = tokenListService;
    }

    @PostMapping("/upload")
    public String uploadCode(@RequestBody String code) throws IOException, NoSuchAlgorithmException {
        String codeTrimmed = code.trim();

        String codeId = this.codeCacheManager.saveCodeFile(codeTrimmed);
        log.info("Uploaded code with id {}.", codeId);

        Optional<Exception> hasCompilationError = this.llvmCompilerService.checkSuccessfulCompilation(codeId);

        if (hasCompilationError.isEmpty()) {
            return codeId;
        }

        log.warn("Code with id {} got some errors.", codeId, hasCompilationError.get());
        throw new CompilationException(hasCompilationError.get().getMessage());
    }

    @GetMapping("/{codeId}")
    public String getCode(@PathVariable("codeId") String codeId) throws IOException {
        log.info("Getting code with id {}.", codeId);
        Optional<String> codeResult = this.codeCacheManager.loadCodeFromId(codeId);

        if (codeResult.isEmpty()) {
            log.warn("Code with id {} was not found.", codeId);
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return codeResult.get();
    }

    @GetMapping("/{codeId}/llvm/ir")
    public String getLLVMIRCodeFromCodeId(@PathVariable("codeId") String codeId) throws IOException {
        log.info("Getting llvm ir code with id {}.", codeId);
        Optional<String> llvmIrResult = this.llvmCompilerService.getLLVMIRCode(codeId);

        if (llvmIrResult.isEmpty()) {
            log.warn("Code with id {} was not found.", codeId);
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return llvmIrResult.get();
    }

    @GetMapping("{codeId}/llvm/ir/opt/{optLevel}")
    public String getLLVMCodeOptimized(@PathVariable("codeId") String codeId, @PathVariable("optLevel") String optLevel) throws IOException, InterruptedException {
        log.info("Getting llvm ir optimized code with id {} and optimization level {}.", codeId, optLevel);
        Optional<OptLevel> optLevelResult = OptLevel.getOptLevelFromString(optLevel);

        if (optLevelResult.isEmpty()) {
            log.warn("Optimization level {} is invalid.", optLevel);
            throw InvalidOptLevelException.inOptLevel(optLevel);
        }

        Optional<String> optResult = this.llvmCompilerService.getOptLLVMIrCode(codeId, optLevelResult.get());

        if (optResult.isEmpty()) {
            log.warn("Code with id {} was not found.", codeId);
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return optResult.get();
    }

    @GetMapping("/{codeId}/asm")
    public String getAsmFromCodeId(@PathVariable("codeId") String codeId) throws IOException, InterruptedException {
        log.info("Getting assembly code with id {}.", codeId);
        Optional<String> asmResult = this.llvmCompilerService.getAsmCode(codeId);

        if (asmResult.isEmpty()) {
            log.warn("Code with id {} was not found.", codeId);
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return asmResult.get();
    }

    @GetMapping("/{codeId}/asm/opt/{optLevel}")
    public String getAsmCodeOptimized(@PathVariable("codeId") String codeId, @PathVariable("optLevel") String optLevel) throws IOException, InterruptedException {
        log.info("Getting assembly optimized code with id {} and optimization level {}.", codeId, optLevel);
        Optional<OptLevel> optLevelResult = OptLevel.getOptLevelFromString(optLevel);

        if (optLevelResult.isEmpty()) {
            log.warn("Optimization level {} is invalid.", optLevel);
            throw InvalidOptLevelException.inOptLevel(optLevel);
        }

        Optional<String> optResult = this.llvmCompilerService.getOptAsmCode(codeId, optLevelResult.get());

        if (optResult.isEmpty()) {
            log.warn("Code with id {} was not found.", codeId);
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return optResult.get();
    }

    @GetMapping("{codeId}/syntax")
    public String getSyntaxTree(@PathVariable("codeId") String codeId) throws IOException {
        log.info("Getting syntax representation of code with id {}.", codeId);
        Optional<String> syntaxTreeResult = this.syntaxTreeService.getSyntaxTree(codeId);

        if (syntaxTreeResult.isEmpty()) {
            log.warn("Code with id {} was not found.", codeId);
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return syntaxTreeResult.get();
    }

    @GetMapping("{codeId}/token")
    public String getTokenList(@PathVariable("codeId") String codeId) throws IOException {
        log.info("Getting token representation of code with id {}.", codeId);
        Optional<String> tokenListResult = this.tokenListService.getTokenList(codeId);

        if (tokenListResult.isEmpty()) {
            log.warn("Code with id {} was not found.", codeId);
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return tokenListResult.get();
    }

}
