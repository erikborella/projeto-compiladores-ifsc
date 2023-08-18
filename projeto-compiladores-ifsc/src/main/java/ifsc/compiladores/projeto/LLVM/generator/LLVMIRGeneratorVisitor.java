package ifsc.compiladores.projeto.LLVM.generator;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.FragmentBlock;
import ifsc.compiladores.projeto.LLVM.ReturnableFragment;
import ifsc.compiladores.projeto.LLVM.ReturnableFragmentBlock;
import ifsc.compiladores.projeto.LLVM.definitions.Alloca;
import ifsc.compiladores.projeto.LLVM.definitions.Load;
import ifsc.compiladores.projeto.LLVM.definitions.Store;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.expressions.Constant;
import ifsc.compiladores.projeto.LLVM.definitions.expressions.Operation;
import ifsc.compiladores.projeto.LLVM.definitions.expressions.OperationType;
import ifsc.compiladores.projeto.LLVM.definitions.functions.Function;
import ifsc.compiladores.projeto.LLVM.definitions.functions.Parameter;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import ifsc.compiladores.projeto.LLVM.scopeManager.ScopeManager;
import ifsc.compiladores.projeto.LLVM.scopeManager.SingleUseVariablesManager;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import javax.swing.text.html.parser.Parser;
import java.util.ArrayList;

public class LLVMIRGeneratorVisitor extends ParserGrammarBaseVisitor<Fragment> {

    private final ScopeManager scopeManager;
    private final SingleUseVariablesManager singleUseVariablesManager;

    public LLVMIRGeneratorVisitor() {
        this.scopeManager = new ScopeManager();
        this.singleUseVariablesManager = new SingleUseVariablesManager();
    }

    @Override
    public Fragment visitPrograma(ParserGrammar.ProgramaContext ctx) {
        FragmentBlock program = new FragmentBlock();

        for (ParserGrammar.DecfuncaoContext decfuncaoContext : ctx.decfuncao()) {
            program.add(visitDecfuncao(decfuncaoContext));
        }

        program.add(visitPrincipal(ctx.principal()));

        return program;
    }

    @Override
    public Function visitDecfuncao(ParserGrammar.DecfuncaoContext ctx) {
        Type functionReturnType = visitTiporetorno(ctx.tiporetorno());
        String functionName = ctx.ID().getText();

        Function function = new Function(functionReturnType, functionName);

        if (this.scopeManager.isFunctionDeclared(functionName)) {
            throw new IllegalStateException(String.format("Já existe uma função com o nome %s declarada.",
                    functionName));
        }

        this.scopeManager.declareFunction(function);

        this.scopeManager.startScope();
        this.singleUseVariablesManager.resetVariables();

        if (ctx.parametros() != null) {
            ArrayList<Parameter> parameters = getParameters(ctx.parametros());
            function.getParameters().addAll(parameters);

            FragmentBlock parametersDeclaration = declareParameters(parameters);
            function.getBody().addAll(parametersDeclaration);
        }

        FragmentBlock functionBody = visitBloco(ctx.bloco());
        function.getBody().addAll(functionBody);

        this.scopeManager.finishScope();

        return function;
    }

    private FragmentBlock declareParameters(ArrayList<Parameter> parameters) {
        FragmentBlock parametersDeclaration = new FragmentBlock();

        for (Parameter parameter : parameters) {
            Alloca parameterDeclaration = new Alloca(parameter.getVariable().name(), parameter.getVariable().type());
            parametersDeclaration.add(parameterDeclaration);


            Variable sourceParameterVariable = new Variable(parameter.getVariable().type(),
                    parameter.getNameInParameterForm());

            Store parameterStore = new Store(
                    sourceParameterVariable,
                    parameterDeclaration.getReturnVariable()
            );

            parametersDeclaration.add(parameterStore);
        }

        return parametersDeclaration;
    }

    private ArrayList<Parameter> getParameters(ParserGrammar.ParametrosContext ctx) {
        ArrayList<Parameter> parameters = new ArrayList<>();

        for (int i = 0; i < ctx.ID().size(); i++) {
            Type parameterType = visitTipo(ctx.tipo(i));
            String parameterName = ctx.ID(i).getText();

            Variable variable = new Variable(parameterType, parameterName);

            if (this.scopeManager.isVariableDeclared(parameterName)) {
                throw new IllegalStateException(String.format("Já existe um parametro com o nome %s declarado.",
                        parameterName));
            }

            this.scopeManager.declareVariable(variable);

            Parameter parameter = new Parameter(variable);

            parameters.add(parameter);
        }

        return parameters;
    }

    @Override
    public Function visitPrincipal(ParserGrammar.PrincipalContext ctx) {
        Type type = new Type(BaseType.INT);
        String name = "main";

        return new Function(type, name);
    }

    @Override
    public FragmentBlock visitBloco(ParserGrammar.BlocoContext ctx) {
        FragmentBlock block = new FragmentBlock();

        for (ParserGrammar.DecvariavelContext decvariavelContext : ctx.decvariavel()) {
            FragmentBlock variableDeclarations = visitDecvariavel(decvariavelContext);

            block.addAll(variableDeclarations);
        }

        for (ParserGrammar.ComandoContext comandoContext : ctx.comando()) {
            block.addAll((FragmentBlock) visitComando(comandoContext));
        }

        return block;
    }

    @Override
    public FragmentBlock visitDecvariavel(ParserGrammar.DecvariavelContext ctx) {
        FragmentBlock variableDeclarations = new FragmentBlock();
        Type variableType = visitTipo(ctx.tipo());

        for (int i = 0; i < ctx.ID().size(); i++) {
            String variableName = ctx.ID(i).getText();
            Variable variable = new Variable(variableType, variableName);

            if (this.scopeManager.isVariableDeclared(variableName)) {
                throw new IllegalStateException(String.format("Já existe uma variavel com o nome %s declarada.",
                        variableName));
            }

            this.scopeManager.declareVariable(variable);

            if (variableType.isArrayType()) {
                FragmentBlock arrayVariableDeclaration = declareArrayVariable(variable, variableName);
                variableDeclarations.addAll(arrayVariableDeclaration);
            }
            else {
                Fragment valueVariableDeclaration = declareValueVariable(variable, variableName);
                variableDeclarations.add(valueVariableDeclaration);
            }
        }

        return variableDeclarations;
    }

    private Fragment declareValueVariable(Variable variable, String variableName) {
        return new Alloca(variableName, variable.type());
    }

    private FragmentBlock declareArrayVariable(Variable variable, String variableName) {
        FragmentBlock arrayDeclaration = new FragmentBlock();

        Type arrayBaseType = variable.type().getNewDeferencePointerOfThis();

        Alloca arrayAlloca = new Alloca(this.singleUseVariablesManager.getNewVariableName(), arrayBaseType);
        arrayDeclaration.add(arrayAlloca);

        Alloca arrayReferenceAlloca = new Alloca(variableName, variable.type());
        arrayDeclaration.add(arrayReferenceAlloca);

        Store arrayReferenceStore = new Store(
                arrayAlloca.getReturnVariable(),
                arrayReferenceAlloca.getReturnVariable()
        );

        arrayDeclaration.add(arrayReferenceStore);

        return arrayDeclaration;
    }

    @Override
    public FragmentBlock visitAtribuicao(ParserGrammar.AtribuicaoContext ctx) {
        FragmentBlock attribuition = new FragmentBlock();
        String attributionId = ctx.ID().getText();

        if (!this.scopeManager.isVariableDeclared(attributionId)) {
            throw new IllegalStateException(String.format("Variavel %s não está declarada",
                    attributionId));
        }

        Variable id = this.scopeManager.getDeclaredVariable(attributionId);
        Variable storeId = new Variable(id.type().getNewReferencePointerToThis(), id.name());

        ReturnableFragmentBlock expressionReturn = (ReturnableFragmentBlock) visitExpressao(ctx.complemento().expressao());
        attribuition.addAll(expressionReturn.getFragmentBlock());

        Store idStore = new Store(expressionReturn.getReturnVariable(), storeId);
        attribuition.add(idStore);

        return attribuition;
    }

    @Override
    public Fragment visitExpr_aditiva(ParserGrammar.Expr_aditivaContext ctx) {
        ReturnableFragmentBlock expression = new ReturnableFragmentBlock();
        
        for (int i = 0; i < ctx.children.size(); i++) {
            ParseTree child = ctx.getChild(i);
            
            if (child instanceof ParserGrammar.Expr_multiplicativaContext ExprMultiplicativaContext) {
                ReturnableFragmentBlock exprMultiplicativa = visitExpr_multiplicativa(ExprMultiplicativaContext);
                
                expression.getFragmentBlock().addAll(exprMultiplicativa.getFragmentBlock());
                expression.setReturnVariable(exprMultiplicativa.getReturnVariable());
                continue;
            }
            
            if (child instanceof ParserGrammar.Op_aditivoContext opAditivoContext) {
                OperationType operationType = switch (opAditivoContext.start.getType()) {
                    case ParserGrammar.SINAL_MAIS -> OperationType.ADD;
                    case ParserGrammar.SINAL_MENOS -> OperationType.SUBTRACTION;
                    default -> throw new IllegalStateException("Invalid operation type");
                };
                
                ReturnableFragmentBlock op2Expression = visitExpr_multiplicativa(
                        (ParserGrammar.Expr_multiplicativaContext) ctx.getChild(i+1));
                expression.getFragmentBlock().addAll(op2Expression.getFragmentBlock());
                i++;
                
                Variable returnVariable = this.singleUseVariablesManager.getNewVariableOfType(
                    expression.getReturnVariable().type());
                
                Variable op1 = expression.getReturnVariable();
                Variable op2 = op2Expression.getReturnVariable();
                
                Operation operation = new Operation(
                        operationType, 
                        returnVariable, 
                        op1, 
                        op2);
                
                expression.getFragmentBlock().add(operation);
                expression.setReturnVariable(operation.getReturnVariable());
            }
        }
        
        return expression;
    }

    @Override
    public ReturnableFragmentBlock visitExpr_multiplicativa(ParserGrammar.Expr_multiplicativaContext ctx) {
        ReturnableFragmentBlock expression = new ReturnableFragmentBlock();
        
        for (int i = 0; i < ctx.children.size(); i++) {
            ParseTree child = ctx.getChild(i);
            
            if (child instanceof ParserGrammar.FatorContext fatorContext) {
                ReturnableFragmentBlock fator = visitFator(fatorContext);
                
                expression.getFragmentBlock().addAll(fator.getFragmentBlock());
                expression.setReturnVariable(fator.getReturnVariable());
                continue;
            }
            
            if (child instanceof ParserGrammar.Op_multiplicativoContext opMultiplicativoContext) {
                OperationType operationType = switch (opMultiplicativoContext.start.getType()) {
                    case ParserGrammar.OP_MULTIPLICACAO -> OperationType.MULTIPLICATION;
                    case ParserGrammar.OP_DIVISAO -> OperationType.DIVISION;
                    case ParserGrammar.OP_RESTO_DIVISAO -> OperationType.MOD;
                    default -> throw new IllegalStateException("Invalid operation type");
                };
                
                ReturnableFragmentBlock op2Expression = visitFator((ParserGrammar.FatorContext) ctx.getChild(i+1));
                expression.getFragmentBlock().addAll(op2Expression.getFragmentBlock());
                i++;
                
                Variable returnVariable = this.singleUseVariablesManager.getNewVariableOfType(
                    expression.getReturnVariable().type());
                
                Variable op1 = expression.getReturnVariable();
                Variable op2 = op2Expression.getReturnVariable();
                
                Operation operation = new Operation(
                        operationType, 
                        returnVariable, 
                        op1, 
                        op2);
                
                expression.getFragmentBlock().add(operation);
                expression.setReturnVariable(operation.getReturnVariable());
            }
        }
        
        return expression;
    }

    @Override
    public ReturnableFragmentBlock visitFator(ParserGrammar.FatorContext ctx) {

        switch (ctx.getStart().getType()) {
            case ParserGrammar.TEXTO, ParserGrammar.OP_NEGACAO, ParserGrammar.PARENTESE_ABRE -> {
                return null;
            }
        }

        ReturnableFragmentBlock term = visitTermo(ctx.termo());

        return term;
    }

    @Override
    public ReturnableFragmentBlock visitTermo(ParserGrammar.TermoContext ctx) {
        if (ctx.getChild(0) instanceof TerminalNodeImpl) {
            String id = ctx.start.getText();
            Load idLoad = this.loadId(id);


            return new ReturnableFragmentBlock(idLoad);
        }

        return (ReturnableFragmentBlock) super.visitTermo(ctx);
    }

    @Override
    public ReturnableFragmentBlock visitConstante(ParserGrammar.ConstanteContext ctx) {
        Token constantToken = ctx.getStart();

        Constant constant = switch (constantToken.getType()) {
            case ParserGrammar.NUM_INT -> new Constant(BaseType.INT, constantToken.getText());
            case ParserGrammar.NUM_DEC -> new Constant(BaseType.FLOAT, constantToken.getText());
            default -> throw new IllegalStateException("Invalid type");
        };

        ReturnableFragmentBlock returnableFragmentBlock = new ReturnableFragmentBlock();
        returnableFragmentBlock.setReturnVariable(constant.getReturnVariable());

        return returnableFragmentBlock;
    }

    private Load loadId(String idName) {
        if (!this.scopeManager.isVariableDeclared(idName)) {
            throw new IllegalStateException(String.format("Variavel %s não está declarada",
                    idName));
        }

        Variable id = this.scopeManager.getDeclaredVariable(idName);

        return new Load(this.singleUseVariablesManager.getNewVariableName(), id);
    }

    @Override
    public Type visitTiporetorno(ParserGrammar.TiporetornoContext ctx) {
        boolean isVoidType = ctx.TIPO_VOID() != null;

        if (isVoidType) {
            return new Type(BaseType.VOID);
        }

        return visitTipo(ctx.tipo());
    }

    @Override
    public Type visitTipo(ParserGrammar.TipoContext ctx) {
        BaseType baseType = visitTipobase(ctx.tipobase());
        ArrayList<Integer> dimensions = new ArrayList<>();

        for (ParserGrammar.DimensaoContext dimensaoContext : ctx.dimensao()) {
            int dimension = Integer.parseInt(dimensaoContext.NUM_INT().getText());
            dimensions.add(dimension);
        }

        if (!dimensions.isEmpty())
            return new Type(baseType, 1, dimensions);

        return new Type(baseType);
    }

    @Override
    public BaseType visitTipobase(ParserGrammar.TipobaseContext ctx) {
        return switch (ctx.getStart().getType()) {
            case ParserGrammar.TIPO_INT -> BaseType.INT;
            case ParserGrammar.TIPO_BOOLEAN -> BaseType.BOOLEAN;
            case ParserGrammar.TIPO_FLOAT -> BaseType.FLOAT;
            case ParserGrammar.TIPO_CHAR -> BaseType.CHAR;
            default -> throw new IllegalStateException("Invalid type");
        };
    }

    @Override
    public Fragment visitChildren(RuleNode node) {
        Fragment result = defaultResult();
        int n = node.getChildCount();
        for (int i=0; i<n; i++) {
            if (!shouldVisitNextChild(node, result)) {
                break;
            }

            ParseTree c = node.getChild(i);

            if (c instanceof TerminalNodeImpl)
                continue;

            Fragment childResult = c.accept(this);
            result = aggregateResult(result, childResult);
        }

        return result;
    }
}