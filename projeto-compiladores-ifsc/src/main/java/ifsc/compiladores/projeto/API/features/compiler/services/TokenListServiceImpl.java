package ifsc.compiladores.projeto.API.features.compiler.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifsc.compiladores.projeto.API.configuration.CompilerConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.domain.CodeCacheManager;
import ifsc.compiladores.projeto.API.features.compiler.domain.TokenListService;
import ifsc.compiladores.projeto.token.TokenListBuilder;
import ifsc.compiladores.projeto.token.tokenBuilder.definitions.Token;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TokenListServiceImpl implements TokenListService {

    private final CompilerConfiguration configuration;
    private final CodeCacheManager codeCacheManager;

    public TokenListServiceImpl(CompilerConfiguration configuration, CodeCacheManager codeCacheManager) {
        this.configuration = configuration;
        this.codeCacheManager = codeCacheManager;
    }

    @Override
    public Optional<String> getTokenList(String codeId) throws IOException {
        Optional<String> tokenListCacheResult = this.codeCacheManager.loadCodeArtifact(codeId, this.configuration.getTokenListFileName());

        if (tokenListCacheResult.isPresent()) {
            return tokenListCacheResult;
        }

        Optional<String> sourceCodeCacheResult = this.codeCacheManager.loadCodeFromId(codeId);

        if (sourceCodeCacheResult.isEmpty()) {
            return Optional.empty();
        }

        List<Token> tokenList = TokenListBuilder.buildTokenList(sourceCodeCacheResult.get());

        ObjectMapper mapper = new ObjectMapper();
        String tokenListString = mapper.writeValueAsString(tokenList);

        return Optional.of(tokenListString);
    }

}
