package ifsc.compiladores.projeto.API.features.compiler.runner.threads;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class ProcessOutputListenerThread extends Thread {
    private final InputStream inputStream;
    private final Consumer<String> onOutputCallback;
    private final CountDownLatch latch;

    public ProcessOutputListenerThread(InputStream inputStream, CountDownLatch latch, Consumer<String> onOutputCallback) {
        this.inputStream = inputStream;
        this.latch = latch;
        this.onOutputCallback = onOutputCallback;
    }
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream))) {
            String line;

            while ((line = reader.readLine()) != null) {
                onOutputCallback.accept(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.latch.countDown();
        }
    }
}
