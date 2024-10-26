package ifsc.compiladores.projeto.LLVM.translator.definitions;

import ifsc.compiladores.projeto.LLVM.translator.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class VariableTest {
    
    @Test
    void shouldEmit_nameInIRForm_forVariable() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensions = new Type(baseType);
        Variable variable = new Variable(typeWithoutDimensions, "teste");

        // Act
        String variableName = variable.getNameInIRForm();

        // Assert
        assertEquals("%teste", variableName);
    }
    
    @Test
    void shouldEmit_rawName_forVariableAsContant() {
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensions = new Type(baseType);
        Variable variable = Variable.asConstant(typeWithoutDimensions, "teste");

        // Act
        String variableName = variable.getNameInIRForm();

        // Assert
        assertEquals("teste", variableName);
    }
}
