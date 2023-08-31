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
    
    @Test
    void shouldEmit_rawTypeWithOnePointer_forTypeWithoutDimensionsWithOnePointer_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensionsWithOnePointer = new Type(baseType, 1);

        // Act
        String emitValue = typeWithoutDimensionsWithOnePointer.getText();

        // Assert
        assertEquals("i32*", emitValue);
    }
    
    @Test
    void shouldEmit_rawTypeWithTwoPointer_forTypeWithoutDimensionsWithTwoPointer_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensionsWithTwoPointer = new Type(baseType, 2);

        // Act
        String emitValue = typeWithoutDimensionsWithTwoPointer.getText();

        // Assert
        assertEquals("i32**", emitValue);
    }
    
    @Test
    void shouldEmit_oneDimensionArrayTypeWithOnePointer_forTypeWithOneDimensionAndOnePointer_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithOneDimensionAndOnePointer = new Type(baseType, 1);

        typeWithOneDimensionAndOnePointer.getDimensions().add(5);

        // Act
        String emitValue = typeWithOneDimensionAndOnePointer.getText();

        // Assert
        assertEquals("[5 x i32]*", emitValue);
    }
    
    @Test
    void shouldEmit_oneDimensionArrayTypeWithTwoPointer_forTypeWithOneDimensionAndTwoPointer_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithOneDimensionAndTwoPointer = new Type(baseType, 2);

        typeWithOneDimensionAndTwoPointer.getDimensions().add(5);

        // Act
        String emitValue = typeWithOneDimensionAndTwoPointer.getText();

        // Assert
        assertEquals("[5 x i32]**", emitValue);
    }
    
    @Test
    void shouldEmit_twoDimensionArrayTypeWithOnePointer_forTypeWithTwoDimensionAndOnePointer_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithTwoDimensionAndOnePointer = new Type(baseType, 1);

        typeWithTwoDimensionAndOnePointer.getDimensions().add(2);
        typeWithTwoDimensionAndOnePointer.getDimensions().add(4);

        // Act
        String emitValue = typeWithTwoDimensionAndOnePointer.getText();

        // Assert
        assertEquals("[2 x [4 x i32]]*", emitValue);
    }
    
    @Test
    void shouldEmit_twoDimensionArrayTypeWithTwoPointer_forTypeWithTwoDimensionAndTwoPointer_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithTwoDimensionAndTwoPointer = new Type(baseType, 2);

        typeWithTwoDimensionAndTwoPointer.getDimensions().add(2);
        typeWithTwoDimensionAndTwoPointer.getDimensions().add(4);

        // Act
        String emitValue = typeWithTwoDimensionAndTwoPointer.getText();

        // Assert
        assertEquals("[2 x [4 x i32]]**", emitValue);
    }
}