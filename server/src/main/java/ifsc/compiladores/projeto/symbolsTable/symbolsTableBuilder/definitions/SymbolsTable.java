package ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions;

import java.util.List;

public record SymbolsTable(
        List<Function> declaredFunction,
        List<Scope> scopes) {
}
