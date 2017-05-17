package extractor;

import javafx.util.Pair;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExtractorModule {

    private CameraDomainExtractor extractor;
    private BlockingQueue<Pair<URL, Document>> classifiedQueue;
    private BlockingQueue<Pair<URL, Map<String, String>>> extractedAttributesQueue;
    private AtomicBoolean started;


    public ExtractorModule(CameraDomainExtractor extractor, BlockingQueue<Pair<URL, Document>> classifiedQueue, BlockingQueue<Pair<URL, Map<String, String>>> extractedAttributesQueue) {
        this.extractor = Objects.requireNonNull(extractor, "The extractor cannot be null.");
        this.classifiedQueue = Objects.requireNonNull(classifiedQueue, "The classified queue can not be null.");
        this.extractedAttributesQueue = Objects.requireNonNull(extractedAttributesQueue, "The extracted attributes queue can not be null");
        started = new AtomicBoolean(false);
    }

    /**
     * Starts the extractor module.
     */
    public void start() {
        if (started.get()) {
            throw new IllegalStateException("Extractor module already started.");
        }
        started.set(true);
        new Thread(() -> {
            while (started.get()) {
                try {
                    Pair<URL, Document> toExtract = classifiedQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (toExtract == null || toExtract.getKey() == null || toExtract.getValue() == null) {
                        continue;
                    }
                    Map<String, String> contentExtracted = extractor.extractWebSiteContent(toExtract.getValue());
                    if (contentExtracted.size() > 1) {
                        extractedAttributesQueue.put(new Pair<>(toExtract.getKey(), contentExtracted));
                    }
                } catch (InterruptedException ignored) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Stops the extractor module
     */
    public void stop() {
        started.set(false);
    }
}

