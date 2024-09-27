package ifsc.compiladores.projeto.API.features.compiler.runner.threads;

import java.util.function.IntConsumer;

public class ProcessExitListenerThread extends Thread {
    private final Process process;
    private final IntConsumer onExitCallback;

    public ProcessExitListenerThread(Process process, IntConsumer onExitCallback) {
        this.process = process;
        this.onExitCallback = onExitCallback;
    }

    @Override
    public void run() {
        try {
            int exitCode = this.process.waitFor();

            onExitCallback.accept(exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
