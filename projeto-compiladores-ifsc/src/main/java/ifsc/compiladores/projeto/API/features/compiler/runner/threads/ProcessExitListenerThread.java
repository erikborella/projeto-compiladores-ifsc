package ifsc.compiladores.projeto.API.features.compiler.runner.threads;

import java.util.concurrent.CountDownLatch;
import java.util.function.IntConsumer;

public class ProcessExitListenerThread extends Thread {
    private final Process process;
    private final CountDownLatch latch;
    private final IntConsumer onExitCallback;

    public ProcessExitListenerThread(Process process, CountDownLatch latch, IntConsumer onExitCallback) {
        this.process = process;
        this.latch = latch;
        this.onExitCallback = onExitCallback;
    }

    @Override
    public void run() {
        try {
            int exitCode = this.process.waitFor();
            latch.await();

            onExitCallback.accept(exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
