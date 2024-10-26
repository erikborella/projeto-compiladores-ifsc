package ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.generator;

import ifsc.compiladores.projeto.common.position.TokenPosition;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions.Function;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions.Scope;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions.SymbolsTable;
import ifsc.compiladores.projeto.symbolsTable.symbolsTableBuilder.definitions.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class ScopeManagerEmulator {
    private final SymbolsTable symbolsTable;

    private final Stack<Scope> scopeStack;
    private HashMap<String, Integer> declaredVariablesTable;

    public ScopeManagerEmulator() {
        this.symbolsTable = new SymbolsTable(new ArrayList<>(), new ArrayList<>());
        this.scopeStack = new Stack<>();
        this.declaredVariablesTable = new HashMap<>();
    }

    public void declareFunction(Function function) {
        this.symbolsTable.declaredFunction().add(function);
    }

    public void startScope(TokenPosition scopePosition) {
        Scope currentScope = this.getCurrentScope();
        Scope newScope = new Scope(new ArrayList<>(), new ArrayList<>(), scopePosition);

        if (currentScope == null) {
            this.declaredVariablesTable = new HashMap<>();
            this.symbolsTable.scopes().add(newScope);

        }

        if (currentScope != null) {
            currentScope.children().add(newScope);
        }

        this.scopeStack.push(newScope);
    }

    public void finishScope() {
        this.scopeStack.pop();

        if (this.scopeStack.empty()) {
            this.declaredVariablesTable = null;
        }
    }

    public void declareVariable(Variable variable) {
        Scope currentScope = this.getCurrentScope();

        int currentVariableNameUseCount = this.declaredVariablesTable.getOrDefault(variable.name(), -1);
        int newVariableNameUseCount = currentVariableNameUseCount + 1;
        this.declaredVariablesTable.put(variable.name(), newVariableNameUseCount);

        String withoutNameConflictName = variable.name();

        if (newVariableNameUseCount != 0) {
            withoutNameConflictName += String.valueOf(newVariableNameUseCount);
        }

        Variable withoutNameConflictVariable = new Variable(variable.type(), withoutNameConflictName);

        currentScope.scopeVariables().add(withoutNameConflictVariable);
    }

    public Scope getCurrentScope() {
        if (this.scopeStack.empty())
            return null;

        return this.scopeStack.peek();
    }

    public SymbolsTable getSymbolsTable() {
        return symbolsTable;
    }
}
