package ifsc.compiladores.projeto.API.features.compiler;

import ifsc.compiladores.projeto.API.features.compiler.domain.CodeCacheManager;
import ifsc.compiladores.projeto.API.features.compiler.domain.LLVMCompilerService;
import ifsc.compiladores.projeto.API.features.compiler.domain.OptLevel;
import ifsc.compiladores.projeto.API.features.compiler.domain.SyntaxTreeService;
import ifsc.compiladores.projeto.API.features.compiler.exceptions.CodeFileNotFoundException;
import ifsc.compiladores.projeto.API.features.compiler.exceptions.InvalidOptLevelException;
import ifsc.compiladores.projeto.syntax.SyntaxTreeBuilder;
import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.SyntaxTreeFragment;
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

    public CompilerController(CodeCacheManager codeCacheManager, LLVMCompilerService llvmCompilerService, SyntaxTreeService syntaxTreeService) {
        this.codeCacheManager = codeCacheManager;
        this.llvmCompilerService = llvmCompilerService;
        this.syntaxTreeService = syntaxTreeService;
    }

    @PostMapping("/upload")
    public String uploadCode(@RequestBody String code) throws IOException, NoSuchAlgorithmException {
        String codeTrimmed = code.trim();

        String codeId = codeCacheManager.saveCodeFile(codeTrimmed);

        return codeId;
    }

    @GetMapping("/{codeId}/llvm/ir")
    public String getLLVMIRCodeFromCodeId(@PathVariable("codeId") String codeId) throws IOException {
        Optional<String> llvmIrResult = this.llvmCompilerService.getLLVMIRCode(codeId);

        if (llvmIrResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return llvmIrResult.get();
    }

    @GetMapping("/{codeId}/asm")
    public String getAsmFromCodeId(@PathVariable("codeId") String codeId) throws IOException, InterruptedException {
        Optional<String> asmResult = this.llvmCompilerService.getAsmCode(codeId);

        if (asmResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return asmResult.get();
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

    @GetMapping("{codeId}/syntax")
    public String getSyntaxTree(@PathVariable("codeId") String codeId) throws IOException {
        Optional<String> syntaxTreeResult = this.syntaxTreeService.getSyntaxTree(codeId);

        if (syntaxTreeResult.isEmpty()) {
            throw CodeFileNotFoundException.inCodeId(codeId);
        }

        return syntaxTreeResult.get();
    }

}
