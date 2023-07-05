package ifsc.compiladores.projeto.LLVM.definitions.functions;

import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParameterTest {
    @Test
    void shouldEmit_valueParameter_forTypeWithoutDimensions_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensions = new Type(baseType);
        Variable variable = new Variable(typeWithoutDimensions, "myVariable");
        Parameter parameter = new Parameter(variable);

        // Act
        String emitValue = parameter.getText();

        // Assert
        assertEquals("i32 %myVariable.param", emitValue);
    }

    @Test
    void shouldEmit_referenceParameter_forTypeWithDimensions_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithOneDimension = new Type(baseType);
        typeWithOneDimension.getDimensions().add(5);
        Variable variable = new Variable(typeWithOneDimension, "myArray");
        Parameter parameter = new Parameter(variable);

        // Act
        String emitValue = parameter.getText();

        // Assert
        assertEquals("[5 x i32]* %myArray.param", emitValue);
    }
}