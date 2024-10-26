package ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions;

import ifsc.compiladores.projeto.common.position.TokenPosition;

import java.util.List;

public record Function(
        String returnType,
        String name,
        TokenPosition position,
        List<Variable> parameters) {
}
