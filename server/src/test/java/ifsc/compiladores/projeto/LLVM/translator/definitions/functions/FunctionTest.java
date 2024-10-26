package ifsc.compiladores.projeto.LLVM.translator.definitions.functions;

import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FunctionTest {

    @Test
    void shouldEmit_function_withValueReturnType_withoutParameters_usingGetText() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensions = new Type(baseType);
        Function function = new Function(typeWithoutDimensions, "myFunction");

        // Act
        String emitValue = function.getText();

        // Assert
        assertEquals("define i32 @myFunction() {\n\n}", emitValue);
    }

    @Test
    void shouldEmit_function_withValueReturnType_withParameters_usingGetText() {
        // Arrange
        BaseType baseTypeInt = BaseType.INT;
        Type typeWithoutDimensions = new Type(baseTypeInt);
        Function function = new Function(typeWithoutDimensions, "myFunction");
        Variable variable1 = new Variable(typeWithoutDimensions, "myVariable1");
        Variable variable2 = new Variable(typeWithoutDimensions, "myVariable2");

        Parameter parameter1 = new Parameter(variable1);
        Parameter parameter2 = new Parameter(variable2);

        function.getParameters().add(parameter1);
        function.getParameters().add(parameter2);

        // Act
        String emitValue = function.getText();

        // Assert
        assertEquals("define i32 @myFunction(i32 %myVariable1.param, i32 %myVariable2.param) {\n\n}", emitValue);
    }

    @Test
    void shouldEmit_function_withReferenceReturnType_withoutParameters_usingGetText() {
        // Arrange
        BaseType baseTypeInt = BaseType.FLOAT;
        Type typeWithOneDimension = new Type(baseTypeInt);
        typeWithOneDimension.getDimensions().add(5);
        Function function = new Function(typeWithOneDimension, "myFunction");

        // Act
        String emitValue = function.getText();

        // Assert
        assertEquals("define [5 x double] @myFunction() {\n\n}", emitValue);
    }

    @Test
    void shouldEmit_function_withReferenceReturnType_withParameters_usingGetText() {
        // Arrange
        BaseType baseTypeInt = BaseType.FLOAT;
        Type typeWithOneDimension = new Type(baseTypeInt);
        typeWithOneDimension.getDimensions().add(5);
        Function function = new Function(typeWithOneDimension, "myFunction");

        Variable variable1 = new Variable(typeWithOneDimension, "myVariable1");
        Variable variable2 = new Variable(typeWithOneDimension, "myVariable2");

        Parameter parameter1 = new Parameter(variable1);
        Parameter parameter2 = new Parameter(variable2);

        function.getParameters().add(parameter1);
        function.getParameters().add(parameter2);

        // Act
        String emitValue = function.getText();

        // Assert
        assertEquals("define [5 x double] @myFunction([5 x double] %myVariable1.param, [5 x double] %myVariable2.param) {\n\n}", emitValue);
    }
}