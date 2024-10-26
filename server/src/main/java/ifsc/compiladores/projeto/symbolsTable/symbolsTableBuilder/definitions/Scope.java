package ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions;

import ifsc.compiladores.projeto.common.position.TokenPosition;

import java.util.List;

public record Scope(
        List<Variable> scopeVariables,
        List<Scope> children,
        TokenPosition position) {
}
