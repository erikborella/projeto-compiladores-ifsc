package ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions;

import java.util.List;
import java.util.Set;

public record SymbolsTable(
        List<Function> functions,
        List<Scope> scopes,
        Set<String> strings) {
}
