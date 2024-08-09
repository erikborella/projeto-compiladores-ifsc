package ifsc.compiladores.projeto.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class APIMain {
    public static void initAPI(String[] args) {
        SpringApplication.run(APIMain.class, args);
    }
}
