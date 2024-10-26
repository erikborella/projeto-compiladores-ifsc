package ifsc.compiladores.projeto.API.features.compiler.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ifsc.compiladores.projeto.API.configuration.CompilerConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.domain.CodeCacheManager;
import ifsc.compiladores.projeto.API.features.compiler.domain.SymbolsTableService;
import ifsc.compiladores.projeto.symbolsTable.SymbolsTableBuilder;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions.SymbolsTable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class SymbolsTableServiceImpl implements SymbolsTableService {

    private final CompilerConfiguration configuration;
    private final CodeCacheManager codeCacheManager;

    public SymbolsTableServiceImpl(CompilerConfiguration configuration, CodeCacheManager codeCacheManager) {
        this.configuration = configuration;
        this.codeCacheManager = codeCacheManager;
    }

    @Override
    public Optional<String> getSymbolsTable(String codeId) throws IOException {
        Optional<String> symbolsTableCacheResult = this.codeCacheManager.loadCodeArtifact(codeId, this.configuration.getSymbolsTableFileName());

        if (symbolsTableCacheResult.isPresent()) {
            return symbolsTableCacheResult;
        }

        Optional<String> sourceCodeCacheResult = this.codeCacheManager.loadCodeFromId(codeId);

        if (sourceCodeCacheResult.isEmpty()) {
            return Optional.empty();
        }

        SymbolsTable symbolsTable = SymbolsTableBuilder.buildSymbolsTable(sourceCodeCacheResult.get());

        ObjectMapper mapper = new ObjectMapper();
        String symbolsTableString = mapper.writeValueAsString(symbolsTable);

        this.codeCacheManager.saveCodeArtifact(codeId, this.configuration.getSymbolsTableFileName(), symbolsTableString);

        return Optional.of(symbolsTableString);
    }
}
