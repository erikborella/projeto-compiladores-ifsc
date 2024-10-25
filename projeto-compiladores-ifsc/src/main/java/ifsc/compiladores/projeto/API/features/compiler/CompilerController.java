package ifsc.compiladores.projeto.API.features.compiler;

import ifsc.compiladores.projeto.API.features.compiler.domain.*;
import ifsc.compiladores.projeto.API.features.compiler.exceptions.CodeFileNotFoundException;
import ifsc.compiladores.projeto.API.features.compiler.exceptions.CompilationException;
import ifsc.compiladores.projeto.API.features.compiler.exceptions.InvalidOptLevelException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/compiler")
public class CompilerController {

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

        Optional<Exception> hasCompilationError = this.llvmCompilerService.checkSuccessfulCompilation(codeId);

        if (hasCompilationError.isEmpty()) {
            return codeId;
        }

        throw new CompilationException(hasCompilationError.get().getMessage());
    }

    @GetMapping("/{codeId}")
    public String getCode(@PathVariable("codeId") String codeId) throws IOException {
        Optional<String> codeResult = this.codeCacheManager.loadCodeFromId(codeId);

        if (codeResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return codeResult.get();
    }

    @GetMapping("/{codeId}/llvm/ir")
    public String getLLVMIRCodeFromCodeId(@PathVariable("codeId") String codeId) throws IOException {
        Optional<String> llvmIrResult = this.llvmCompilerService.getLLVMIRCode(codeId);

        if (llvmIrResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return llvmIrResult.get();
    }

    @GetMapping("{codeId}/llvm/ir/opt/{optLevel}")
    public String getLLVMCodeOptimized(@PathVariable("codeId") String codeId, @PathVariable("optLevel") String optLevel) throws IOException, InterruptedException {
        Optional<OptLevel> optLevelResult = OptLevel.getOptLevelFromString(optLevel);

        if (optLevelResult.isEmpty()) {
            throw InvalidOptLevelException.inOptLevel(optLevel);
        }

        Optional<String> optResult = this.llvmCompilerService.getOptLLVMIrCode(codeId, optLevelResult.get());

        if (optResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return optResult.get();
    }

    @GetMapping("/{codeId}/asm")
    public String getAsmFromCodeId(@PathVariable("codeId") String codeId) throws IOException, InterruptedException {
        Optional<String> asmResult = this.llvmCompilerService.getAsmCode(codeId);

        if (asmResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return asmResult.get();
    }

    @GetMapping("/{codeId}/asm/opt/{optLevel}")
    public String getAsmCodeOptimized(@PathVariable("codeId") String codeId, @PathVariable("optLevel") String optLevel) throws IOException, InterruptedException {
        Optional<OptLevel> optLevelResult = OptLevel.getOptLevelFromString(optLevel);

        if (optLevelResult.isEmpty()) {
            throw InvalidOptLevelException.inOptLevel(optLevel);
        }

        Optional<String> optResult = this.llvmCompilerService.getOptAsmCode(codeId, optLevelResult.get());

        if (optResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return optResult.get();
    }

    @GetMapping("{codeId}/syntax")
    public String getSyntaxTree(@PathVariable("codeId") String codeId) throws IOException {
        Optional<String> syntaxTreeResult = this.syntaxTreeService.getSyntaxTree(codeId);

        if (syntaxTreeResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return syntaxTreeResult.get();
    }

    @GetMapping("{codeId}/token")
    public String getTokenList(@PathVariable("codeId") String codeId) throws IOException {
        Optional<String> tokenListResult = this.tokenListService.getTokenList(codeId);

        if (tokenListResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return tokenListResult.get();
    }

}
