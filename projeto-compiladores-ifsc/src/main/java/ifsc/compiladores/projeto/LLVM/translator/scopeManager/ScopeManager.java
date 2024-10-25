package ifsc.compiladores.projeto.LLVM.translator.scopeManager;

import ifsc.compiladores.projeto.LLVM.translator.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.translator.definitions.functions.Function;
import ifsc.compiladores.projeto.LLVM.translator.definitions.types.Type;

import java.util.HashMap;
import java.util.Stack;

public class ScopeManager {

    private final Stack<Scope> scopeStack;
    private final HashMap<String, Function> declaredFunctions;
    
    private HashMap<String, Integer> declaredVariablesTable;

    public ScopeManager() {
        this.scopeStack = new Stack<>();
        this.declaredFunctions = new HashMap<>();
    }

    public void declareFunction(Function function) {
        String functionName = function.getName();

        this.declaredFunctions.put(functionName, function);
    }

    public boolean isFunctionDeclared(String functionName) {
        return this.declaredFunctions.containsKey(functionName);
    }

    public void startScope() {
        this.startScope(null);
    }
    
    public void startScope(Type returnType) {
        Scope currentScope = this.getCurrentScope();
        Scope newScope = new Scope(currentScope, returnType);
        
        if (currentScope == null) {
            this.declaredVariablesTable = new HashMap<>();
        }
        
        this.scopeStack.push(newScope);
    }

    public void finishScope() {
        this.scopeStack.pop();
        
        if (this.scopeStack.empty()) {
            this.declaredVariablesTable = null;
        }
    }

    public Variable declareVariable(Variable variable) {
        Scope currentScope = this.getCurrentScope();

        int currentVariableNameUseCount = this.declaredVariablesTable.getOrDefault(variable.name(), -1);
        int newVariableNameUseCount = currentVariableNameUseCount + 1;
        this.declaredVariablesTable.put(variable.name(), newVariableNameUseCount);

        Variable withoutNameConflictVariable = new Variable(variable.type(), variable.name(), newVariableNameUseCount);

        currentScope.getDeclaredVariables().put(withoutNameConflictVariable.name(), withoutNameConflictVariable);

        return withoutNameConflictVariable;
    }

    public Variable getDeclaredVariable(String variableName) {
        Scope scope = this.getCurrentScope();

        while (scope != null) {
            HashMap<String, Variable> declaredVariables = scope.getDeclaredVariables();

            if (declaredVariables.containsKey(variableName)) {
                Variable declaredVariable = declaredVariables.get(variableName);
                return declaredVariable;
            }
            
            scope = scope.getParent();
        }

        return null;
    }

    public boolean isVariableDeclared(String variableName) {
        return getDeclaredVariable(variableName) != null;
    }

    public boolean isVariableDeclaredInCurrentScope(String variableName) {
        Scope currentScope = this.getCurrentScope();

        if (currentScope == null) {
            return false;
        }

        return currentScope.getDeclaredVariables().containsKey(variableName);
    }
    
    public Function getDeclaredFunction(String functionName) {
        if (this.declaredFunctions.containsKey(functionName))
            return this.declaredFunctions.get(functionName);
        
        return null;
    }

    public HashMap<String, Function> getDeclaredFunctions() {
        return declaredFunctions;
    }

    public Stack<Scope> getScopeStack() {
        return scopeStack;
    }

    public Scope getCurrentScope() {
        if (this.scopeStack.empty())
            return null;

        return this.scopeStack.peek();
    }
    
    public Type getScopeReturnType() {
        Scope scope = this.getCurrentScope();
        
        while (scope != null) {
            Type returnType = scope.getReturnType();
            
            if (returnType != null) {
                return returnType;
            }
            
            scope = scope.getParent();
        }
        
        return null;
    }
}
