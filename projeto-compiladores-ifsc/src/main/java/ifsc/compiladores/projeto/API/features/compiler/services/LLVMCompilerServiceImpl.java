package ifsc.compiladores.projeto.API.features.compiler.services;

import ifsc.compiladores.projeto.API.configuration.CompilerConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.domain.CodeCacheManager;
import ifsc.compiladores.projeto.API.features.compiler.domain.LLVMCompilerService;
import ifsc.compiladores.projeto.LLVM.LLVMCompiler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class LLVMCompilerServiceImpl implements LLVMCompilerService {

    private final CompilerConfiguration compilerConfiguration;
    private final CodeCacheManager codeCacheManager;

    public LLVMCompilerServiceImpl(CompilerConfiguration compilerConfiguration, CodeCacheManager codeCacheManager) {
        this.compilerConfiguration = compilerConfiguration;
        this.codeCacheManager = codeCacheManager;
    }

    @Override
    public Optional<String> getLLVMIRCode(String codeId) throws IOException {
        Optional<String> llvmIRCodeCacheResult = this.codeCacheManager.loadCodeArtifact(codeId, this.compilerConfiguration.getIrFileName());

        if (llvmIRCodeCacheResult.isPresent()) {
            return llvmIRCodeCacheResult;
        }

        Optional<String> sourceCodeCacheResult = this.codeCacheManager.loadCodeFromId(codeId);

        if (sourceCodeCacheResult.isEmpty()) {
            return Optional.empty();
        }

        String llvmIRCode = LLVMCompiler.compileToIR(sourceCodeCacheResult.get());

        this.codeCacheManager.saveCodeArtifact(codeId, this.compilerConfiguration.getIrFileName(), llvmIRCode);

        return Optional.of(llvmIRCode);
    }


}
