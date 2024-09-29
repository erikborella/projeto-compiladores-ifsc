package ifsc.compiladores.projeto.API.features.compiler.domain;

import java.util.Optional;

public enum OptLevel {
    O0("O0"),
    O1("O1"),
    O2("O2"),
    O3("O3"),
    OS("Os"),
    OZ("Oz");

    private final String stringValue;

    OptLevel(String stringValue) {
        this.stringValue = stringValue;
    }

    public static Optional<OptLevel> getOptLevelFromString(String optLevelString) {
        for (OptLevel level : OptLevel.values()) {
            if (level.stringValue.equalsIgnoreCase(optLevelString)) {
                return Optional.of(level);
            }
        }

        return Optional.empty();
    }

    public String getStringValue() {
        return stringValue;
    }
}
