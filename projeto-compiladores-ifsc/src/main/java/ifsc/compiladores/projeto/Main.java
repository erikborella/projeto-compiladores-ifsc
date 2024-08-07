package ifsc.compiladores.projeto;

import picocli.CommandLine;

@CommandLine.Command(
        name="Projeto Compiladores IFSC",
        mixinStandardHelpOptions = true,
        version = "0.0.1",
        description = "CLI do projeto de Compiladores do IFSC",
        subcommands = {
                CompilerCommand.class,
                APICommand.class
        }
)
public class Main {

    public static void main(String[] args) {
        new CommandLine(new Main()).execute(args);
    }
}