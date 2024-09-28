package ifsc.compiladores.projeto.API.features.compiler.runner;

import ifsc.compiladores.projeto.API.configuration.CompilerConfiguration;
import ifsc.compiladores.projeto.API.features.compiler.domain.LLVMCompilerService;
import ifsc.compiladores.projeto.API.features.compiler.runner.threads.ProcessExitListenerThread;
import ifsc.compiladores.projeto.API.features.compiler.runner.threads.ProcessOutputListenerThread;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CompilerRunnerWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, Process> sessions = new ConcurrentHashMap<>();

    private final LLVMCompilerService llvmCompilerService;

    public CompilerRunnerWebSocketHandler(LLVMCompilerService llvmCompilerService) {
        this.llvmCompilerService = llvmCompilerService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (session.getUri() == null) {
            session.sendMessage(new TextMessage("-- Erro no servidor: URI não encontrada"));
            session.close();
            return;
        }

        Optional<String> codeId = this.getCodeIdFromQuery(session.getUri());

        if (codeId.isEmpty()) {
            session.sendMessage(new TextMessage("-- Erro de cliente: codeId não está presente"));
            session.close();
            return;
        }

        Optional<File> executablePath = this.llvmCompilerService.getExecutableCodePath(codeId.get());

        if (executablePath.isEmpty()) {
            session.sendMessage(new TextMessage("-- Erro de cliente: código não encontrado."));
            session.close();
            return;
        }

        Process process = this.startProcess(executablePath.get());

        String sessionId = session.getId();
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
                synchronized (session) {
                    session.sendMessage(new TextMessage(message));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new ProcessOutputListenerThread(process.getErrorStream(), (errorMessage) -> {
            try {
                synchronized (session) {
                    session.sendMessage(new TextMessage("Error: " + errorMessage));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new ProcessExitListenerThread(process, (exitCode) -> {
            try {
                synchronized (session) {
                    session.sendMessage(new TextMessage("\n\n-- Execução do programa finalizado com código de saida: " + exitCode));
                    session.close();
                }

                this.sessions.remove(sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private Optional<String> getCodeIdFromQuery(URI uri) {
        String uriQuery = uri.getQuery();

        if (uriQuery == null) {
            return Optional.empty();
        }

        String[] params = uriQuery.split("&");

        for (String param : params) {
            String[] pair = param.split("=");

            if (pair.length > 1 && "codeId".equals(pair[0])) {
                return Optional.ofNullable(pair[1]);
            }
        }

        return Optional.empty();
    }

    private Process startProcess(File executableFile) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("stdbuf", "-o0", "-e0", executableFile.getCanonicalPath());
        return processBuilder.start();
    }

}
