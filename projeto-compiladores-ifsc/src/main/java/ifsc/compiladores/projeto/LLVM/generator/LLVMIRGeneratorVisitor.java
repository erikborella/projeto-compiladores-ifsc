package ifsc.compiladores.projeto.LLVM.generator;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.FragmentBlock;
import ifsc.compiladores.projeto.LLVM.LabeledFragmentBlock;
import ifsc.compiladores.projeto.LLVM.ReturnableFragmentBlock;
import ifsc.compiladores.projeto.LLVM.definitions.Alloca;
import ifsc.compiladores.projeto.LLVM.definitions.GetElementPtr;
import ifsc.compiladores.projeto.LLVM.definitions.Label;
import ifsc.compiladores.projeto.LLVM.definitions.Load;
import ifsc.compiladores.projeto.LLVM.definitions.Store;
import ifsc.compiladores.projeto.LLVM.definitions.Variable;
import ifsc.compiladores.projeto.LLVM.definitions.expressions.Constant;
import ifsc.compiladores.projeto.LLVM.definitions.expressions.Operation;
import ifsc.compiladores.projeto.LLVM.definitions.expressions.OperationType;
import ifsc.compiladores.projeto.LLVM.definitions.expressions.Return;
import ifsc.compiladores.projeto.LLVM.definitions.expressions.comparisons.IntegerComparison;
import ifsc.compiladores.projeto.LLVM.definitions.expressions.comparisons.IntegerComparisonType;
import ifsc.compiladores.projeto.LLVM.definitions.functions.Function;
import ifsc.compiladores.projeto.LLVM.definitions.functions.FunctionCall;
import ifsc.compiladores.projeto.LLVM.definitions.functions.Parameter;
import ifsc.compiladores.projeto.LLVM.definitions.jumps.ConditionalJump;
import ifsc.compiladores.projeto.LLVM.definitions.jumps.UnconditionalJump;
import ifsc.compiladores.projeto.LLVM.definitions.types.BaseType;
import ifsc.compiladores.projeto.LLVM.definitions.types.Type;
import ifsc.compiladores.projeto.LLVM.scopeManager.LabelManager;
import ifsc.compiladores.projeto.LLVM.scopeManager.ScopeManager;
import ifsc.compiladores.projeto.LLVM.scopeManager.SingleUseVariablesManager;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammarBaseVisitor;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.ArrayList;
import java.util.Arrays;
import org.antlr.v4.runtime.ParserRuleContext;

public class LLVMIRGeneratorVisitor extends ParserGrammarBaseVisitor<Fragment> {

    private final ScopeManager scopeManager;
    private final SingleUseVariablesManager singleUseVariablesManager;
    private final LabelManager labelManager;

    public LLVMIRGeneratorVisitor() {
        this.scopeManager = new ScopeManager();
        this.singleUseVariablesManager = new SingleUseVariablesManager();
        this.labelManager = new LabelManager();
    }

    @Override
    public Fragment visitPrograma(ParserGrammar.ProgramaContext ctx) {
        FragmentBlock program = new FragmentBlock();

        declareFunctions(ctx);
        
        for (ParserGrammar.DecfuncaoContext decfuncaoContext : ctx.decfuncao()) {
            program.add(visitDecfuncao(decfuncaoContext));
        }

        program.add(visitPrincipal(ctx.principal()));

        return program;
    }
    
    private void declareFunctions(ParserGrammar.ProgramaContext ctx) {
        for (ParserGrammar.DecfuncaoContext decfuncaoContext : ctx.decfuncao()) {
            Type functionReturnType = visitTiporetorno(decfuncaoContext.tiporetorno());
            String functionName = decfuncaoContext.ID().getText();

            Function function = new Function(functionReturnType, functionName);

            if (this.scopeManager.isFunctionDeclared(functionName)) {
                throw new IllegalStateException(String.format("Já existe uma função com o nome %s declarada.",
                        functionName));
            }

            this.scopeManager.declareFunction(function);
        }
    }

    @Override
    public Function visitDecfuncao(ParserGrammar.DecfuncaoContext ctx) {
        String functionName = ctx.ID().getText();
        Function function = this.scopeManager.getDeclaredFunction(functionName);

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

            if (this.scopeManager.isVariableDeclared(parameterName)) {
                throw new IllegalStateException(String.format("Já existe um parametro com o nome %s declarado.",
                        parameterName));
            }

            Variable declareVariable = new Variable(
                    parameterType.getNewReferencePointerToThis(), 
                    parameterName
            );
            this.scopeManager.declareVariable(declareVariable);

            Variable parameterVariable = new Variable(parameterType, parameterName);
            Parameter parameter = new Parameter(parameterVariable);

            parameters.add(parameter);
        }

        return parameters;
    }

    @Override
    public Function visitPrincipal(ParserGrammar.PrincipalContext ctx) {
        Type type = new Type(BaseType.INT);
        String name = "main";
        
        Function mainFunction = new Function(type, name);
        
        this.scopeManager.startScope();
        this.singleUseVariablesManager.resetVariables();

        FragmentBlock functionBody = visitBloco(ctx.bloco());
        mainFunction.getBody().addAll(functionBody);

        this.scopeManager.finishScope();

        return mainFunction;
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
        Type variableType = visitTipo(ctx.tipo())
                .getNewReferencePointerToThis();

        for (int i = 0; i < ctx.ID().size(); i++) {
            String variableName = ctx.ID(i).getText();
            Variable variable = new Variable(variableType, variableName);

            if (this.scopeManager.isVariableDeclared(variableName)) {
                throw new IllegalStateException(String.format("Já existe uma variavel com o nome %s declarada.",
                        variableName));
            }

            this.scopeManager.declareVariable(variable);
            Variable variableWithoutConflit = this.scopeManager.getVariableWithoutNameConflit(variable);
            String variableNameWithoutConflit = variableWithoutConflit.name();

            if (variableType.isArrayType()) {
                FragmentBlock arrayVariableDeclaration = declareArrayVariable(variableWithoutConflit, variableNameWithoutConflit);
                variableDeclarations.addAll(arrayVariableDeclaration);
            }
            else {
                Fragment valueVariableDeclaration = declareValueVariable(variableWithoutConflit, variableNameWithoutConflit);
                variableDeclarations.add(valueVariableDeclaration);
            }
        }

        return variableDeclarations;
    }

    private Fragment declareValueVariable(Variable variable, String variableName) {
        return new Alloca(variableName, variable.type().getNewDeferencePointerOfThis());
    }
    
    private FragmentBlock declareArrayVariable(Variable variable, String variableName) {
        FragmentBlock arrayDeclaration = new FragmentBlock();
        
        Type arrayReferenceType = variable.type().getNewDeferencePointerOfThis();
        
        Alloca arrayReferenceAlloca = new Alloca(variableName, arrayReferenceType);
        arrayDeclaration.add(arrayReferenceAlloca);
        
        Type arrayBaseType = arrayReferenceType.getNewDeferencePointerOfThis();
        
        Alloca arrayAlloca = new Alloca(this.singleUseVariablesManager.getNewVariableName(), arrayBaseType);
        arrayDeclaration.add(arrayAlloca);
        
        Store arrayReferenceStore = new Store(
            arrayAlloca.getReturnVariable(),
            arrayReferenceAlloca.getReturnVariable()
        );

        arrayDeclaration.add(arrayReferenceStore);

        return arrayDeclaration;
    }

    @Override
    public FragmentBlock visitComandoLinhaFuncao(ParserGrammar.ComandoLinhaFuncaoContext ctx) {
        FragmentBlock functionCallExpresion = new FragmentBlock();
        String functionName = ctx.funcao().ID().getText();
        
        if (!this.scopeManager.isFunctionDeclared(functionName)) {
            throw new IllegalStateException(String.format("Função %s não está declarada",
                    functionName));
        }
        
        Function functionDefinition = this.scopeManager.getDeclaredFunction(functionName);
        ArrayList<Variable> arguments = new ArrayList<>();
        
        if (ctx.funcao().argumentos() != null) {
            for (int i = 0; i < ctx.funcao().argumentos().expressao().size(); i++) {
                ParserGrammar.ExpressaoContext argumentExpression = ctx.funcao().argumentos().expressao(i);

                ReturnableFragmentBlock argument = (ReturnableFragmentBlock) visit(argumentExpression);  
                functionCallExpresion.addAll(argument.getFragmentBlock());

                arguments.add(argument.getReturnVariable());
            }
        }
        
        FunctionCall functionCall = FunctionCall.withoutReturn(
                functionDefinition.getReturnType(), 
                functionName, 
                arguments
        );
        
        functionCallExpresion.add(functionCall);
   
        return functionCallExpresion;
    } 
    
    @Override
    public FragmentBlock visitAtribuicao(ParserGrammar.AtribuicaoContext ctx) {
        FragmentBlock attribuition = new FragmentBlock();
        
        ReturnableFragmentBlock idAccess = (ReturnableFragmentBlock) visit(ctx.acesso_id());
        attribuition.addAll(idAccess.getFragmentBlock());

        Variable storeId = idAccess.getReturnVariable();

        ReturnableFragmentBlock expressionReturn = (ReturnableFragmentBlock) visitExpressao(ctx.complemento().expressao());
        attribuition.addAll(expressionReturn.getFragmentBlock());

        Store idStore = new Store(expressionReturn.getReturnVariable(), storeId);
        attribuition.add(idStore);

        return attribuition;
    }
    
    @Override
    public FragmentBlock visitRetorno(ParserGrammar.RetornoContext ctx) {
        FragmentBlock returnBlock = new FragmentBlock();
        
        if (ctx.expressao() == null) {
            Return voidReturn = Return.asVoid();
            returnBlock.add(voidReturn);
            return returnBlock;
        }
        
        ReturnableFragmentBlock expression = (ReturnableFragmentBlock) visit(ctx.expressao());
        returnBlock.addAll(expression.getFragmentBlock());
        
        Return returnExpression = new Return(expression.getReturnVariable());
        returnBlock.add(returnExpression);
        
        return returnBlock;
    }

    @Override
    public FragmentBlock visitSelecao(ParserGrammar.SelecaoContext ctx) {
        FragmentBlock ifStructure = new FragmentBlock();
        
        ReturnableFragmentBlock ifExpression = visitExpressao(ctx.expressao());
        ifStructure.addAll(ifExpression.getFragmentBlock());
        
        Label ifLabel = this.labelManager.createLabel("..if");
        Label endIfLabel = this.labelManager.createLabel("..endIf");
        Label falseIfLabel = endIfLabel;
        
        if (ctx.senao() != null) {
            falseIfLabel = this.labelManager.createLabel("..else");
        }
        
        ConditionalJump ifEnterJump = new ConditionalJump(
                ifExpression.getReturnVariable(), 
                ifLabel, 
                falseIfLabel
        );
        ifStructure.add(ifEnterJump);
        ifStructure.add(ifLabel);
        
        this.scopeManager.startScope();
        FragmentBlock ifBlock = visitBloco(ctx.bloco());
        this.scopeManager.finishScope();
                
        ifStructure.addAll(ifBlock);
        
        if (!(ifBlock.get(ifBlock.size() - 1) instanceof Return)) {
            UnconditionalJump exitIfJump = new UnconditionalJump(endIfLabel);
            ifStructure.add(exitIfJump);       
        }
        
        if (ctx.senao() != null) {
            ifStructure.add(falseIfLabel);
            
            this.scopeManager.startScope();
            FragmentBlock elseBlock = visitBloco(ctx.senao().bloco());
            this.scopeManager.finishScope();
            
            ifStructure.addAll(elseBlock);
            
            if (!(elseBlock.get(elseBlock.size() - 1) instanceof Return)) {
                UnconditionalJump exitElseJump = new UnconditionalJump(endIfLabel);
                ifStructure.add(exitElseJump);
            }
        }
        
        ifStructure.add(endIfLabel);
        
        return ifStructure;
    }

    @Override
    public ReturnableFragmentBlock visitExpressao(ParserGrammar.ExpressaoContext ctx) {
        return visitExpr_ou(ctx.expr_ou());
    }
    
    @Override
    public ReturnableFragmentBlock visitExpr_ou(ParserGrammar.Expr_ouContext ctx) {
        // Skips generation of short-circuit when is not necessary
        if (ctx.children.size() == 1 && ctx.expr_e(0).children.size() == 1) {
            return visitExpr_relacional(ctx.expr_e(0).expr_relacional(0));
        }
        
        LLVMIRShortCircuitCreator circuitCreator = new LLVMIRShortCircuitCreator(this.labelManager);
        
        for (ParserGrammar.Expr_eContext expr_eContext : ctx.expr_e()) {
            expr_eContext.label = this.labelManager.createLabel("..or");
            expr_eContext.trueLabel = circuitCreator.getTrueBlock().getLabel();
        }
        
        Label currentLabel = circuitCreator.getFalseBlock().getLabel();
        for (int i = ctx.expr_e().size() - 1; i >= 0; i--) {
            ParserGrammar.Expr_eContext expr_eContext = ctx.expr_e(i);
            
            expr_eContext.falseLabel = currentLabel;
            currentLabel = expr_eContext.label;
        }
        
        ReturnableFragmentBlock orExpressionBlock = new ReturnableFragmentBlock();
        orExpressionBlock.getFragmentBlock().addAll(circuitCreator.getHeadBlock());
        
        UnconditionalJump jumpToExpression = new UnconditionalJump(ctx.expr_e(0).label);
        orExpressionBlock.getFragmentBlock().add(jumpToExpression);
        
        for (ParserGrammar.Expr_eContext expr_eContext : ctx.expr_e()) {
            ReturnableFragmentBlock andExpression = visitExpr_e(expr_eContext);
            
            orExpressionBlock.getFragmentBlock().addAll(andExpression.getFragmentBlock());
        }
        
        orExpressionBlock.getFragmentBlock().addAll(circuitCreator.getTrueBlock().getFragmentBlock());
        orExpressionBlock.getFragmentBlock().addAll(circuitCreator.getFalseBlock().getFragmentBlock());
        orExpressionBlock.getFragmentBlock().addAll(circuitCreator.getEndBlock().getFragmentBlock());
        
        orExpressionBlock.setReturnVariable(circuitCreator.getReturnVariable());
        
        return orExpressionBlock;
    }

    @Override
    public ReturnableFragmentBlock visitExpr_e(ParserGrammar.Expr_eContext ctx) {
        for (int i = 0; i < ctx.expr_relacional().size(); i++) {
            ParserGrammar.Expr_relacionalContext expr_relacionalContext = ctx.expr_relacional(i);
            expr_relacionalContext.label = this.labelManager.createLabel("..and");
            expr_relacionalContext.falseLabel = ctx.falseLabel;
        }
        
        ReturnableFragmentBlock andExpressionBlock = new ReturnableFragmentBlock();
        
        for (int i = 0; i < ctx.expr_relacional().size(); i++) {;
            ParserGrammar.Expr_relacionalContext expr_relacionalContext = ctx.expr_relacional(i);
            
            Label trueLabel = (i == ctx.expr_relacional().size() - 1) 
                    ? ctx.trueLabel 
                    : ctx.expr_relacional(i+1).label;
            expr_relacionalContext.trueLabel = trueLabel;
            
            if (i == 0)
                andExpressionBlock.getFragmentBlock().add(ctx.label);
            else
                andExpressionBlock.getFragmentBlock().add(expr_relacionalContext.label);
            
            ReturnableFragmentBlock expresionReturn = visitExpr_relacional(expr_relacionalContext);
            andExpressionBlock.getFragmentBlock().addAll(expresionReturn.getFragmentBlock());
            
            ConditionalJump jump = new ConditionalJump(
                expresionReturn.getReturnVariable(),
                expr_relacionalContext.trueLabel,
                expr_relacionalContext.falseLabel);
            andExpressionBlock.getFragmentBlock().add(jump);
        }
        
        return andExpressionBlock;
    }

    @Override
    public ReturnableFragmentBlock visitExpr_relacional(ParserGrammar.Expr_relacionalContext ctx) {
        ReturnableFragmentBlock expression = new ReturnableFragmentBlock();
        
        for (int i = 0; i < ctx.children.size(); i++) {
            ParseTree child = ctx.getChild(i);
            
            if (child instanceof ParserGrammar.Op_relacionalContext opRelacionalContext) {
                IntegerComparisonType comparisonType = switch (opRelacionalContext.start.getType()) {
                    case ParserGrammar.OP_IGUAL -> IntegerComparisonType.EQUALS;
                    case ParserGrammar.OP_DIFERENTE -> IntegerComparisonType.NOT_EQUALS;
                    case ParserGrammar.OP_MENOR -> IntegerComparisonType.LESS;
                    case ParserGrammar.OP_MENOR_IGUAL -> IntegerComparisonType.LESS_EQUALS;
                    case ParserGrammar.OP_MAIOR -> IntegerComparisonType.GREATER;
                    case ParserGrammar.OP_MAIOR_IGUAL -> IntegerComparisonType.GREATER_EQUALS;
                    default -> throw new IllegalStateException("Invalid operation type");
                };
                
                ReturnableFragmentBlock op2Expression = (ReturnableFragmentBlock) visit(ctx.getChild(i+1));
                expression.getFragmentBlock().addAll(op2Expression.getFragmentBlock());
                i++;
                
                String returnVariableName = this.singleUseVariablesManager.getNewVariableName();
                
                Variable op1 = expression.getReturnVariable();
                Variable op2 = op2Expression.getReturnVariable();
                
                IntegerComparison comparison = new IntegerComparison(
                        comparisonType,
                        returnVariableName,
                        op1,
                        op2
                );
                
                expression.getFragmentBlock().add(comparison);
                expression.setReturnVariable(comparison.getReturnVariable());
                continue;
            }
            
            ReturnableFragmentBlock initialOp = (ReturnableFragmentBlock) visit(child);
                
            expression.getFragmentBlock().addAll(initialOp.getFragmentBlock());
            expression.setReturnVariable(initialOp.getReturnVariable());
        }
        
        return expression;
    }

    @Override
    public ReturnableFragmentBlock visitExpr_aditiva(ParserGrammar.Expr_aditivaContext ctx) {
        return createExpression(ctx);
    }

    @Override
    public ReturnableFragmentBlock visitExpr_multiplicativa(ParserGrammar.Expr_multiplicativaContext ctx) {
        return createExpression(ctx);
    }

    public ReturnableFragmentBlock createExpression(ParserRuleContext ctx) {
        ReturnableFragmentBlock expression = new ReturnableFragmentBlock();

        ArrayList<Class> operatorsContextClass = new ArrayList<>(Arrays.asList(
            ParserGrammar.Op_multiplicativoContext.class,
            ParserGrammar.Op_aditivoContext.class
        ));
        
        for (int i = 0; i < ctx.children.size(); i++) {
            ParseTree child = ctx.getChild(i);

            if (operatorsContextClass.stream().anyMatch(op -> child.getClass() == op)) {
                ParserRuleContext operatorContext = (ParserRuleContext) child;
                
                OperationType operationType = switch (operatorContext.start.getType()) {
                    case ParserGrammar.OP_MULTIPLICACAO -> OperationType.MULTIPLICATION;
                    case ParserGrammar.OP_DIVISAO -> OperationType.DIVISION;
                    case ParserGrammar.OP_RESTO_DIVISAO -> OperationType.MOD;
                    case ParserGrammar.SINAL_MAIS -> OperationType.ADD;
                    case ParserGrammar.SINAL_MENOS -> OperationType.SUBTRACTION;
                    default -> throw new IllegalStateException("Invalid operation type");
                };
                
                ReturnableFragmentBlock op2Expression = (ReturnableFragmentBlock) visit(ctx.getChild(i+1));
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
                continue;
            }
            
            ReturnableFragmentBlock initialOp = (ReturnableFragmentBlock) visit(child);
                
            expression.getFragmentBlock().addAll(initialOp.getFragmentBlock());
            expression.setReturnVariable(initialOp.getReturnVariable());
        }
        
        return expression;
    }

    @Override
    public ReturnableFragmentBlock visitFatorTermo(ParserGrammar.FatorTermoContext ctx) {
        ReturnableFragmentBlock term = (ReturnableFragmentBlock) visit(ctx.termo());
        
        if (ctx.sinal() !=  null && ctx.sinal().getStart().getType() == ParserGrammar.SINAL_MENOS) {
            Variable valueVariable = term.getReturnVariable();            
            Variable negateReturnVariable = this.singleUseVariablesManager.getNewVariableOfType(valueVariable.type());
                    
            Operation negation = new Operation(
                    OperationType.MULTIPLICATION,
                    negateReturnVariable, 
                    valueVariable, 
                    Variable.asConstant(valueVariable.type(), "-1"));
            
            term.getFragmentBlock().add(negation);
            term.setReturnVariable(negation.getReturnVariable());
        }
        
        return term;
    }

    @Override
    public Fragment visitFatorExpressao(ParserGrammar.FatorExpressaoContext ctx) {
        ReturnableFragmentBlock expression = (ReturnableFragmentBlock) visit(ctx.expressao());
        
        if (ctx.sinal() !=  null && ctx.sinal().getStart().getType() == ParserGrammar.SINAL_MENOS) {
            Variable valueVariable = expression.getReturnVariable();            
            Variable negateReturnVariable = this.singleUseVariablesManager.getNewVariableOfType(valueVariable.type());
                    
            Operation negation = new Operation(
                    OperationType.MULTIPLICATION,
                    negateReturnVariable, 
                    valueVariable, 
                    Variable.asConstant(valueVariable.type(), "-1"));
            
            expression.getFragmentBlock().add(negation);
            expression.setReturnVariable(negation.getReturnVariable());
        }
        
        return expression;
    }
    
    @Override
    public ReturnableFragmentBlock visitTermoVariavel(ParserGrammar.TermoVariavelContext ctx) {
        ReturnableFragmentBlock term = new ReturnableFragmentBlock();
        
        ReturnableFragmentBlock idAccess = (ReturnableFragmentBlock) visit(ctx.acesso_id());
        term.getFragmentBlock().addAll(idAccess.getFragmentBlock());
       
        Load idLoad = new Load(
                this.singleUseVariablesManager.getNewVariableName(), 
                idAccess.getReturnVariable()
        );
        
        term.getFragmentBlock().add(idLoad);
        term.setReturnVariable(idLoad.getReturnVariable());
        
        return term;
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

    @Override
    public ReturnableFragmentBlock visitFuncao(ParserGrammar.FuncaoContext ctx) {
        ReturnableFragmentBlock functionCallExpresion = new ReturnableFragmentBlock();
        String functionName = ctx.ID().getText();
        
        if (!this.scopeManager.isFunctionDeclared(functionName)) {
            throw new IllegalStateException(String.format("Função %s não está declarada",
                    functionName));
        }
        
        Function functionDefinition = this.scopeManager.getDeclaredFunction(functionName);
        ArrayList<Variable> arguments = new ArrayList<>();
        
        if (ctx.argumentos() != null) {
            for (int i = 0; i < ctx.argumentos().expressao().size(); i++) {
                ParserGrammar.ExpressaoContext argumentExpression = ctx.argumentos().expressao(i);

                ReturnableFragmentBlock argument = (ReturnableFragmentBlock) visit(argumentExpression);  
                functionCallExpresion.getFragmentBlock().addAll(argument.getFragmentBlock());

                arguments.add(argument.getReturnVariable());
            }
        }
        
        Variable functionCallReturnVariable = 
                this.singleUseVariablesManager.getNewVariableOfType(functionDefinition.getReturnType());
        FunctionCall functionCall = new FunctionCall(functionCallReturnVariable, functionName, arguments);
        
        functionCallExpresion.getFragmentBlock().add(functionCall);
        functionCallExpresion.setReturnVariable(functionCall.getReturnVariable());
        
        return functionCallExpresion;
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
    public ReturnableFragmentBlock visitAcessoId(ParserGrammar.AcessoIdContext ctx) {
        String id = ctx.ID().getText();
        
        if (!this.scopeManager.isVariableDeclared(id)) {
            throw new IllegalStateException(String.format("Variavel %s não está declarada",
                    id));
        }
        
        Variable idVariable = this.scopeManager.getDeclaredVariable(id);
        
        ReturnableFragmentBlock idAccess = new ReturnableFragmentBlock();        
        idAccess.setReturnVariable(idVariable);
        
        return idAccess;
    }

    @Override
    public ReturnableFragmentBlock visitAcessoIdArray(ParserGrammar.AcessoIdArrayContext ctx) {
        ReturnableFragmentBlock arrayAccess = new ReturnableFragmentBlock();
        
        String id = ctx.ID().getText();
        
        if (!this.scopeManager.isVariableDeclared(id)) {
            throw new IllegalStateException(String.format("Variavel %s não está declarada",
                    id));
        }
        
        Variable idVariable = this.scopeManager.getDeclaredVariable(id);
        
        Load arrayLoad = new Load(this.singleUseVariablesManager.getNewVariableName(), idVariable);
        arrayAccess.getFragmentBlock().add(arrayLoad);
        
        ArrayList<Variable> indexes = new ArrayList<>();
        
        for (ParserGrammar.Dimensao2Context dimensao2Context : ctx.dimensao2()) {
            ReturnableFragmentBlock dimensionExpression = visitExpr_aditiva(dimensao2Context.expr_aditiva());
            arrayAccess.getFragmentBlock().addAll(dimensionExpression.getFragmentBlock());

            indexes.add(dimensionExpression.getReturnVariable());
        }
        
        Type returnType = arrayLoad.getReturnVariable()
                .type()
                .getNewDeferenceArrayOfThis(indexes.size());
        
        Variable returnVariable = this.singleUseVariablesManager.getNewVariableOfType(returnType);
        GetElementPtr arrayElementPtr = new GetElementPtr(
                returnVariable, 
                arrayLoad.getReturnVariable(), 
                indexes
        );
        
        arrayElementPtr.getText();
        
        arrayAccess.getFragmentBlock().add(arrayElementPtr);
        arrayAccess.setReturnVariable(returnVariable);

        return arrayAccess;
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