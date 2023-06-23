package ifsc.compiladores.projeto.LLVM.definitions.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseTypeTest {
    @Test
    void shouldEmit_void_forBaseTypeVoid_usingGetText() {
        // Arrange
        BaseType baseTypeVoid = BaseType.VOID;

        // Act
        String emitValues = baseTypeVoid.getText();

        // Assert
        assertEquals("void", emitValues);
    }

    @Test
    void shouldEmit_i1_forBaseTypeBoolean_usingGetText() {
        // Arrange
        BaseType baseTypeBoolean = BaseType.BOOLEAN;

        // Act
        String emitValues = baseTypeBoolean.getText();

        // Assert
        assertEquals("i1", emitValues);
    }

    @Test
    void shouldEmit_i8_forBaseTypeChar_usingGetText() {
        // Arrange
        BaseType baseTypeChar = BaseType.CHAR;

        // Act
        String emitValues = baseTypeChar.getText();

        // Assert
        assertEquals("i8", emitValues);
    }

    @Test
    void shouldEmit_i32_forBaseTypeInt_usingGetText() {
        // Arrange
        BaseType baseTypeInt = BaseType.INT;

        // Act
        String emitValues = baseTypeInt.getText();

        // Assert
        assertEquals("i32", emitValues);
    }

    @Test
    void shouldEmit_double_forBaseTypeFloat_usingGetText() {
        // Arrange
        BaseType baseTypeFloat = BaseType.FLOAT;

        // Act
        String emitValues = baseTypeFloat.getText();

        // Assert
        assertEquals("double", emitValues);
    }
}