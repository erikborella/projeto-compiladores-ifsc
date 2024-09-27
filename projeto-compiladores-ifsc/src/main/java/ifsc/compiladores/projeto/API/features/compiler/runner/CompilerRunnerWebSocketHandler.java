package ifsc.compiladores.projeto.API.features.compiler.runner;

import ifsc.compiladores.projeto.API.configuration.CompilerConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.runner.threads.ProcessExitListenerThread;
import ifsc.compiladores.projeto.API.features.compiler.runner.threads.ProcessOutputListenerThread;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CompilerRunnerWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, Process> sessions = new ConcurrentHashMap<>();

    private final CompilerConfiguration configuration;

    public CompilerRunnerWebSocketHandler(CompilerConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();

        ProcessBuilder processBuilder = new ProcessBuilder("stdbuf", "-o0", "-e0", "./a.out");
        Process process = processBuilder.start();
        this.sessions.put(sessionId, process);

        startProcessThreads(session, process, sessionId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Process process = this.sessions.get(session.getId());

        if (process != null) {
            process.getOutputStream().write((message.getPayload() + "\n").getBytes());
            process.getOutputStream().flush();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Process process = this.sessions.get(session.getId());

        if (process != null) {
            process.destroy();
        }

        this.sessions.remove(session.getId());
    }

    private void startProcessThreads(WebSocketSession session, Process process, String sessionId) {
        new ProcessOutputListenerThread(process.getInputStream(), (message) -> {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new ProcessOutputListenerThread(process.getErrorStream(), (errorMessage) -> {
            try {
                session.sendMessage(new TextMessage("Error: " + errorMessage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new ProcessExitListenerThread(process, (exitCode) -> {
            try {
                session.sendMessage(new TextMessage("\n\n-- Execução do programa finalizado com código de saida: " + exitCode));
                session.close();

                this.sessions.remove(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
