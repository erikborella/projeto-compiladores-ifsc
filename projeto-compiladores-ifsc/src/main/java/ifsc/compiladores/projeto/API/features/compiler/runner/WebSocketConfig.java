package ifsc.compiladores.projeto.API.features.compiler.runner;


import ifsc.compiladores.projeto.API.configuration.CorsConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CorsConfiguration corsConfiguration;
    private final CompilerRunnerWebSocketHandler compilerRunnerWebSocketHandler;

    public WebSocketConfig(CorsConfiguration corsConfiguration, CompilerRunnerWebSocketHandler compilerRunnerWebSocketHandler) {
        this.corsConfiguration = corsConfiguration;
        this.compilerRunnerWebSocketHandler = compilerRunnerWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry
            .addHandler(compilerRunnerWebSocketHandler, "/compiler/runner/ws")
            .setAllowedOrigins(this.corsConfiguration.getAllowedOrigins());
    }
}
