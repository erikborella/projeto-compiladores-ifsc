package ifsc.compiladores.projeto;

import ifsc.compiladores.projeto.API.APIMain;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(
        name = "api",
        description = "Runs the API",
        mixinStandardHelpOptions = true
)
public class APICommand implements Runnable {

    @CommandLine.Parameters(
            paramLabel = "<SpringArgs>",
            description = "Sets the SpringBoot args"
    )
    private final List<String> springArgs = List.of();

    @Override
    public void run() {
        String[] args = springArgs.toArray(new String[0]);
        APIMain.initAPI(args);
    }
}
