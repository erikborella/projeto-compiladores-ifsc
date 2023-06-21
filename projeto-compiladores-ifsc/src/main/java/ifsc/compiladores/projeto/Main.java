package ifsc.compiladores.projeto;

import java.util.Scanner;
import java.util.concurrent.Callable;

import ifsc.compiladores.projeto.LLVM.Fragment;
import ifsc.compiladores.projeto.LLVM.generator.LLVMIRGeneratorVisitor;
import ifsc.compiladores.projeto.gramatica.LexerGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar;
import ifsc.compiladores.projeto.gramatica.ParserGrammar.ProgramaContext;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import picocli.CommandLine;

@CommandLine.Command(name="ParseCommand", mixinStandardHelpOptions = true, version = "0.0.1",
                     description = "Parser!")
public class Main implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        String input = "";
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Escreva seu codigo: ");
            input = scanner.nextLine();

            if (input.equals("exit")) continue;

            LexerGrammar lexer = new LexerGrammar(CharStreams.fromString(input));
            CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            ParserGrammar parser = new ParserGrammar(tokenStream);

            ProgramaContext programaContext = parser.programa();

            LLVMIRGeneratorVisitor visitor = new LLVMIRGeneratorVisitor();

            Fragment program = visitor.visitPrograma(programaContext);

            System.out.println(program.getText());

        } while (!input.equals("exit"));
        return 0;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }
}