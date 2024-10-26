package ifsc.compiladores.projeto.API.features.compiler.services;

import ifsc.compiladores.projeto.API.configuration.CompilerConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.domain.CodeCacheManager;
import ifsc.compiladores.projeto.API.features.compiler.domain.LLVMCompilerService;
import ifsc.compiladores.projeto.API.features.compiler.domain.OptLevel;
import ifsc.compiladores.projeto.LLVM.LLVMCompiler;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class LLVMCompilerServiceImpl implements LLVMCompilerService {

    private final CompilerConfiguration configuration;
    private final CodeCacheManager codeCacheManager;

    public LLVMCompilerServiceImpl(CompilerConfiguration configuration, CodeCacheManager codeCacheManager) {
        this.configuration = configuration;
        this.codeCacheManager = codeCacheManager;
    }

    @Override
    public Optional<String> getLLVMIRCode(String codeId) throws IOException {
        Optional<String> llvmIRCodeCacheResult = this.codeCacheManager.loadCodeArtifact(codeId, this.configuration.getIrFileName());

        if (llvmIRCodeCacheResult.isPresent()) {
            return llvmIRCodeCacheResult;
        }

        Optional<String> sourceCodeCacheResult = this.codeCacheManager.loadCodeFromId(codeId);

        if (sourceCodeCacheResult.isEmpty()) {
            return Optional.empty();
        }

        String llvmIRCode = LLVMCompiler.compileToIR(sourceCodeCacheResult.get());

        this.codeCacheManager.saveCodeArtifact(codeId, this.configuration.getIrFileName(), llvmIRCode);

        return Optional.of(llvmIRCode);
    }

    @Override
    public Optional<String> getOptLLVMIrCode(String codeId, OptLevel optLevel) throws IOException, InterruptedException {
        String optimizedFileName = String.format(this.configuration.getIrOptFileNameTemplate(), optLevel.getStringValue());

        Optional<String> optimizedCodeCacheResult = this.codeCacheManager.loadCodeArtifact(codeId, optimizedFileName);

        if (optimizedCodeCacheResult.isPresent()) {
            return optimizedCodeCacheResult;
        }

        Optional<String> llvmIRCodeResult = this.getLLVMIRCode(codeId);

        if (llvmIRCodeResult.isEmpty()) {
            return Optional.empty();
        }

        File irFilePath = this.codeCacheManager.buildFilePath(codeId, configuration.getIrFileName());
        File optFilePath = this.codeCacheManager.buildFilePath(codeId, optimizedFileName);

        boolean optSuccess = LLVMCompiler.optimizeIR(this.configuration.getLLVMOptimizer(), irFilePath, optFilePath, optLevel.getStringValue());

        if (!optSuccess) {
            return Optional.empty();
        }

        return this.codeCacheManager.loadCodeArtifact(codeId, optimizedFileName);
    }

    @Override
    public Optional<String> getAsmCode(String codeId) throws IOException, InterruptedException {
        Optional<String> asmCodeCacheResult = this.codeCacheManager.loadCodeArtifact(codeId, this.configuration.getAsmFileName());

        if (asmCodeCacheResult.isPresent()) {
            return asmCodeCacheResult;
        }

        Optional<String> llvmIRCodeResult = this.getLLVMIRCode(codeId);

        if (llvmIRCodeResult.isEmpty()) {
            return Optional.empty();
        }

        File irFilePath = this.codeCacheManager.buildFilePath(codeId, configuration.getIrFileName());
        File asmFilePath = this.codeCacheManager.buildFilePath(codeId, configuration.getAsmFileName());

        boolean asmCompilationSuccess = LLVMCompiler.compileToAsm(this.configuration.getClangCompiler(), irFilePath, asmFilePath);

        if (!asmCompilationSuccess) {
            return Optional.empty();
        }

        return this.codeCacheManager.loadCodeArtifact(codeId, configuration.getAsmFileName());
    }

    @Override
    public Optional<String> getOptAsmCode(String codeId, OptLevel optLevel) throws IOException, InterruptedException {
        String optimizedFileName = String.format(this.configuration.getAsmOptFileNameTemplate(), optLevel.getStringValue());

        Optional<String> optimizedCodeCacheResult = this.codeCacheManager.loadCodeArtifact(codeId, optimizedFileName);

        if (optimizedCodeCacheResult.isPresent()) {
            return optimizedCodeCacheResult;
        }

        Optional<String> asmOptCodeResult = this.getLLVMIRCode(codeId);

        if (asmOptCodeResult.isEmpty()) {
            return Optional.empty();
        }

        File irFilePath = this.codeCacheManager.buildFilePath(codeId, configuration.getIrFileName());
        File optFilePath = this.codeCacheManager.buildFilePath(codeId, optimizedFileName);

        boolean optSuccess = LLVMCompiler.optimizeAsm(this.configuration.getClangCompiler(), irFilePath, optFilePath, optLevel.getStringValue());

        if (!optSuccess) {
            return Optional.empty();
        }

        return this.codeCacheManager.loadCodeArtifact(codeId, optimizedFileName);
    }

    @Override
    public Optional<File> getExecutableCodePath(String codeId) throws IOException, InterruptedException {
        OptLevel optimizationUsed = OptLevel.OS;
        Optional<String> optimizedCode = this.getOptLLVMIrCode(codeId, optimizationUsed);

        if (optimizedCode.isEmpty()) {
            return Optional.empty();
        }

        String optimizedFileName = String.format(this.configuration.getIrOptFileNameTemplate(), optimizationUsed.getStringValue());
        File optFilePath = this.codeCacheManager.buildFilePath(codeId, optimizedFileName);
        File executableFilePath = this.codeCacheManager.buildFilePath(codeId, this.configuration.getExecutableFileName());

        boolean elfCompileSuccess = LLVMCompiler.compileToExecutable(this.configuration.getClangCompiler(), optFilePath, executableFilePath);

        if (!elfCompileSuccess) {
            return Optional.empty();
        }

        return Optional.of(executableFilePath);
    }

    @Override
    public Optional<Exception> checkSuccessfulCompilation(String codeId) {
        try {
            this.getLLVMIRCode(codeId);
            return Optional.empty();
        }
        catch (Exception e) {
            return Optional.of(e);
        }
    }

}
