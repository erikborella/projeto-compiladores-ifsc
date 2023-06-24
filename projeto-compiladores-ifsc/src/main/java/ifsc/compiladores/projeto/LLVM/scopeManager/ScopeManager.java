package ifsc.compiladores.projeto.LLVM.scopeManager;

import ifsc.compiladores.projeto.LLVM.definitions.functions.Function;

import java.util.HashMap;
import java.util.Stack;

public class ScopeManager {

    private final Stack<Scope> scopeStack;
    private final HashMap<String, Function> declaredFunctions;

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
        Scope currentScope = this.getCurrentScope();
        Scope newScope = new Scope(currentScope);

        this.scopeStack.push(newScope);
    }

    public void finishScope() {
        this.scopeStack.pop();
    }

    // TODO: declareVariable and isVariableDeclared

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
}
