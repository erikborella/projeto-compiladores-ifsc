package ifsc.compiladores.projeto.API;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class APIMain {
    public static void initAPI(String[] args) {
        SpringApplication.run(APIMain.class, args);
    }
}
