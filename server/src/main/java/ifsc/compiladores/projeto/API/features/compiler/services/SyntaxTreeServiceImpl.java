package ifsc.compiladores.projeto.API.features.compiler.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifsc.compiladores.projeto.API.configuration.CompilerConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.domain.CodeCacheManager;
import ifsc.compiladores.projeto.API.features.compiler.domain.SyntaxTreeService;
import ifsc.compiladores.projeto.syntax.SyntaxTreeBuilder;
import ifsc.compiladores.projeto.syntax.syntaxBuilder.definitions.SyntaxTreeFragment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class SyntaxTreeServiceImpl implements SyntaxTreeService {

    private final CompilerConfiguration configuration;
    private final CodeCacheManager codeCacheManager;

    public SyntaxTreeServiceImpl(CompilerConfiguration configuration, CodeCacheManager codeCacheManager) {
        this.configuration = configuration;
        this.codeCacheManager = codeCacheManager;
    }

    @Override
    public Optional<String> getSyntaxTree(String codeId) throws IOException {
        Optional<String> syntaxTreeCacheResult = this.codeCacheManager.loadCodeArtifact(codeId, this.configuration.getSyntaxTreeFileName());

        if (syntaxTreeCacheResult.isPresent()) {
            return syntaxTreeCacheResult;
        }

        Optional<String> sourceCodeCacheResult = this.codeCacheManager.loadCodeFromId(codeId);

        if (sourceCodeCacheResult.isEmpty()) {
            return Optional.empty();
        }

        SyntaxTreeFragment syntaxTree = SyntaxTreeBuilder.buildSyntaxTree(sourceCodeCacheResult.get());

        ObjectMapper mapper = new ObjectMapper();
        String syntaxTreeString = mapper.writeValueAsString(syntaxTree);

        this.codeCacheManager.saveCodeArtifact(codeId, this.configuration.getSyntaxTreeFileName(), syntaxTreeString);

        return Optional.of(syntaxTreeString);
    }
}
