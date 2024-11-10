package ifsc.compiladores.projeto.API.features.compiler.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifsc.compiladores.projeto.API.configuration.CompilerConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.domain.CodeCacheManager;
import ifsc.compiladores.projeto.API.features.compiler.domain.ComplexityAnalysisService;
import ifsc.compiladores.projeto.complexity.ComplexityAnalyser;
import ifsc.compiladores.projeto.complexity.complexityAnalyserBuilder.definitions.CostResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class ComplexityAnalysisServiceImpl implements ComplexityAnalysisService {

    private final CompilerConfiguration configuration;
    private final CodeCacheManager codeCacheManager;

    public ComplexityAnalysisServiceImpl(CompilerConfiguration configuration, CodeCacheManager codeCacheManager) {
        this.configuration = configuration;
        this.codeCacheManager = codeCacheManager;
    }

    @Override
    public Optional<String> getComplexityAnalysis(String codeId) throws IOException {
        Optional<String> complexityAnalysisCacheResult = this.codeCacheManager.loadCodeArtifact(codeId, this.configuration.getComplexityAnalysisFileName());

        if (complexityAnalysisCacheResult.isPresent()) {
            return complexityAnalysisCacheResult;
        }

        Optional<String> sourceCodeCacheResult = this.codeCacheManager.loadCodeFromId(codeId);

        if (sourceCodeCacheResult.isEmpty()) {
            return Optional.empty();
        }

        CostResult programCost = ComplexityAnalyser.analyseCodeComplexity(sourceCodeCacheResult.get());

        ObjectMapper mapper = new ObjectMapper();
        String programComplexity = mapper.writeValueAsString(programCost);

        this.codeCacheManager.saveCodeArtifact(codeId, this.configuration.getComplexityAnalysisFileName(), programComplexity);

        return Optional.of(programComplexity);
    }

}
