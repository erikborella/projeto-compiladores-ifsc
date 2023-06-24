package ifsc.compiladores.projeto.LLVM.definitions.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeTest {

    @Test
    void shouldEmit_rawType_forTypeWithoutDimensions_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensions = new Type(baseType);

        // Act
        String emitValue = typeWithoutDimensions.getText();

        // Assert
        assertEquals("i32", emitValue);
    }

    @Test
    void shouldEmit_oneDimensionArrayType_forTypeWithOneDimension_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithOneDimension = new Type(baseType);

        typeWithOneDimension.getDimensions().add(5);

        // Act
        String emitValue = typeWithOneDimension.getText();

        // Assert
        assertEquals("[5 x i32]", emitValue);
    }

    @Test
    void shouldEmit_twoDimensionArrayType_forTypeWithTwoDimension_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithTwoDimension = new Type(baseType);

        typeWithTwoDimension.getDimensions().add(5);
        typeWithTwoDimension.getDimensions().add(7);

        // Act
        String emitValue = typeWithTwoDimension.getText();

        // Assert
        assertEquals("[5 x [7 x i32]]", emitValue);
    }

    @Test
    void shouldEmit_threeDimensionArrayType_forTypeWithThreeDimension_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithThreeDimension = new Type(baseType);

        typeWithThreeDimension.getDimensions().add(5);
        typeWithThreeDimension.getDimensions().add(7);
        typeWithThreeDimension.getDimensions().add(3);

        // Act
        String emitValue = typeWithThreeDimension.getText();

        // Assert
        assertEquals("[5 x [7 x [3 x i32]]]", emitValue);
    }

    @Test
    void shouldReturnFalse_forTypeWithoutDimension_usingIsArrayType() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensions = new Type(baseType);

        // Act
        boolean isArrayType = typeWithoutDimensions.isArrayType();

        // Assert
        assertFalse(isArrayType);
    }

    @Test
    void shouldReturnTrue_forTypeWithDimensions_usingIsArrayType() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithDimensions = new Type(baseType);

        typeWithDimensions.getDimensions().add(5);

        // Act
        boolean isArrayType = typeWithDimensions.isArrayType();

        // Assert
        assertTrue(isArrayType);
    }
}