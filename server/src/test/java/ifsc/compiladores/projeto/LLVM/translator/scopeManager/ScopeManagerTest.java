package ifsc.compiladores.projeto.LLVM.translator.scopeManager;

import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.functions.Function;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

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

    @Test
    void should_start_multiplesScopes() {
        // Arrange

        // Act
        this.scopeManager.startScope();
        this.scopeManager.startScope();

        // Assert
        Stack<Scope> scopeStack = this.scopeManager.getScopeStack();

        assertEquals(2, scopeStack.size());

        Scope currentScope = this.scopeManager.getCurrentScope();
        assertNotNull(currentScope);
        assertNotNull(currentScope.getParent());

        Scope parent = currentScope.getParent();
        assertNull(parent.getParent());
    }

    @Test
    void should_finish_oneScope() {
        // Arrange
        this.scopeManager.startScope();
        this.scopeManager.startScope();

        // Act
        this.scopeManager.finishScope();

        // Assert
        Stack<Scope> scopeStack = this.scopeManager.getScopeStack();

        assertEquals(1, scopeStack.size());

        Scope currentScope = this.scopeManager.getCurrentScope();
        assertNotNull(currentScope);
        assertNull(currentScope.getParent());
    }

    @Test
    void should_finish_allScopes() {
        // Arrange
        this.scopeManager.startScope();
        this.scopeManager.startScope();

        // Act
        this.scopeManager.finishScope();
        this.scopeManager.finishScope();

        // Assert
        Stack<Scope> scopeStack = this.scopeManager.getScopeStack();

        assertEquals(0, scopeStack.size());

        Scope currentScope = this.scopeManager.getCurrentScope();
        assertNull(currentScope);
    }

    @Test
    void should_declareVariables_inScope() {
        // Arrange
        Type type = new Type(BaseType.INT);

        Variable variable1 = new Variable(type, "var1");
        Variable variable2 = new Variable(type, "var2");

        // Act
        this.scopeManager.startScope();
        this.scopeManager.declareVariable(variable1);

        this.scopeManager.startScope();
        this.scopeManager.declareVariable(variable2);

        // Assert
        Scope currentScope = this.scopeManager.getCurrentScope();
        assertEquals(1, currentScope.getDeclaredVariables().size());
        assertTrue(currentScope.getDeclaredVariables().containsKey(variable2.name()));

        Scope parent = currentScope.getParent();
        assertEquals(1, parent.getDeclaredVariables().size());
        assertTrue(parent.getDeclaredVariables().containsKey(variable1.name()));
    }

    @Test
    void shouldCheck_if_variablesAreDeclared() {
        // Arrange
        Type type = new Type(BaseType.INT);

        Variable variable1 = new Variable(type, "testVar1");
        Variable variable2 = new Variable(type, "testVar2");
        Variable variable3 = new Variable(type, "testVar3");

        this.scopeManager.startScope();
        this.scopeManager.declareVariable(variable1);

        this.scopeManager.startScope();
        this.scopeManager.declareVariable(variable2);

        this.scopeManager.startScope();
        this.scopeManager.declareVariable(variable3);
        this.scopeManager.finishScope();

        // Act
        boolean testVar1IsDeclared = this.scopeManager.isVariableDeclared("testVar1");
        boolean testVar2IsDeclared = this.scopeManager.isVariableDeclared("testVar2");
        boolean testVar3IsDeclared = this.scopeManager.isVariableDeclared("testVar3");

        // Assert
        assertTrue(testVar1IsDeclared);
        assertTrue(testVar2IsDeclared);

        assertFalse(testVar3IsDeclared);
    }
}