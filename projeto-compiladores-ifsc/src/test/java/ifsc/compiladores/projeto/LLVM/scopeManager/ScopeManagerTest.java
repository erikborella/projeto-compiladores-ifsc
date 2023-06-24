package ifsc.compiladores.projeto.LLVM.scopeManager;

import ifsc.compiladores.projeto.LLVM.definitions.functions.Function;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScopeManagerTest {

    private ScopeManager scopeManager;

    @BeforeEach
    void setUp() {
        this.scopeManager = new ScopeManager();
    }

    @Test
    void shouldDeclare_functionsWithSuccess() {
        // Arrange
        Type type = new Type(BaseType.VOID);

        Function function1 = new Function(type, "testFunction1");
        Function function2 = new Function(type, "testFunction2");
        Function function3 = new Function(type, "testFunction3");

        // Act
        this.scopeManager.declareFunction(function1);
        this.scopeManager.declareFunction(function2);
        this.scopeManager.declareFunction(function3);

        // Assert
        assertEquals(3, this.scopeManager.getDeclaredFunctions().size());
        assertEquals(function1, this.scopeManager.getDeclaredFunctions().get(function1.getName()));
        assertEquals(function2, this.scopeManager.getDeclaredFunctions().get(function2.getName()));
        assertEquals(function3, this.scopeManager.getDeclaredFunctions().get(function3.getName()));
    }

    @Test
    void shouldCheck_if_functionsAreDeclared() {
        // Arrange
        Type type = new Type(BaseType.VOID);

        Function function1 = new Function(type, "testFunction1");
        Function function2 = new Function(type, "testFunction2");
        Function function3 = new Function(type, "testFunction3");

        this.scopeManager.declareFunction(function1);
        this.scopeManager.declareFunction(function2);
        this.scopeManager.declareFunction(function3);

        // Act
        boolean testFunction1IsDeclared = this.scopeManager.isFunctionDeclared("testFunction1");
        boolean testFunction2IsDeclared = this.scopeManager.isFunctionDeclared("testFunction2");
        boolean testFunction3IsDeclared = this.scopeManager.isFunctionDeclared("testFunction3");

        boolean testFunction4IsDeclared = this.scopeManager.isFunctionDeclared("testFunction4");
        boolean testFunction5IsDeclared = this.scopeManager.isFunctionDeclared("testFunction5");
        boolean testFunction6IsDeclared = this.scopeManager.isFunctionDeclared("testFunction6");

        // Assert
        assertTrue(testFunction1IsDeclared);
        assertTrue(testFunction2IsDeclared);
        assertTrue(testFunction3IsDeclared);

        assertFalse(testFunction4IsDeclared);
        assertFalse(testFunction5IsDeclared);
        assertFalse(testFunction6IsDeclared);
    }
}