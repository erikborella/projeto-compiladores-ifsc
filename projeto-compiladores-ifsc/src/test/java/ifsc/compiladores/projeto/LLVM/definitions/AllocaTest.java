package ifsc.compiladores.projeto.LLVM.definitions;

import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class AllocaTest {
    
    @Test
    void shouldEmit_alloca_for_DefinedVariables() {
        
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensions = new Type(baseType);
        Variable variable = new Variable(typeWithoutDimensions, "teste");
        Alloca alloca = new Alloca(variable, typeWithoutDimensions);

        // Act
        String emitValue = alloca.getText();

        // Assert
        assertEquals("%teste = alloca i32", emitValue);
    }
    
    @Test
    void shouldEmit_alloca_forInferedType() {
        
        // Arrange
        BaseType baseType = BaseType.INT;
        Type typeWithoutDimensions = new Type(baseType);
        Alloca alloca = new Alloca("teste", typeWithoutDimensions);

        // Act
        String emitValue = alloca.getText();
        Variable returnVariable = alloca.getReturnVariable();
        Type returnVariableType = returnVariable.type();
        

        // Assert
        assertEquals("%teste = alloca i32", emitValue);
        assertEquals(baseType, returnVariableType.getBaseType());
        assertEquals(typeWithoutDimensions.getPointerCount() + 1, returnVariableType.getPointerCount());
    }
}
