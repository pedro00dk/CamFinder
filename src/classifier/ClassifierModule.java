package classifier;

import javafx.util.Pair;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Class that process a queue of documents classifying then and saving only the positive classified pages in another
 * queue.
 *
 * @author Pedro Henrique
 */
public class ClassifierModule {

    /**
     * The page classifier of this module.
     */
    private PageClassifier pageClassifier;

    /**
     * The input queue.
     */
    private BlockingQueue<Pair<URL, Document>> inputQueue;

    /**
     * The positive classified queue.
     */
    private BlockingQueue<Pair<URL, Document>> classifiedQueue;

    /**
     * Saves the total classified pages.
     */
    private volatile float totalClassifiedPages;

    /**
     * Saves the positive classified pages.
     */
    private volatile float positiveClassifiedPages;

    /**
     * Saves the invalid pages.
     */
    private volatile float invalidPages;

    /**
     * If the module was started.
     */
    private AtomicBoolean started;

    /**
     * Starts the classifier module with the received page classifier, input and classifier queues
     *
     * @param pageClassifier  the page classifier
     * @param inputQueue      the document input queue
     * @param classifiedQueue the positive classified queue
     */
    public ClassifierModule(PageClassifier pageClassifier, BlockingQueue<Pair<URL, Document>> inputQueue, BlockingQueue<Pair<URL, Document>> classifiedQueue) {
        this.pageClassifier = Objects.requireNonNull(pageClassifier, "The apge classifier can not be null.");
        this.inputQueue = Objects.requireNonNull(inputQueue, "The input queue can not be null");
        this.classifiedQueue = Objects.requireNonNull(classifiedQueue, "The classified queue can not be null.");
        started = new AtomicBoolean(false);
    }

    /**
     * Starts the classifier module.
     */
    public void start() {
        if (started.get()) {
            throw new IllegalStateException("Classifier module already started.");
        }
        started.set(true);
        new Thread(() -> {
            totalClassifiedPages = 0;
            positiveClassifiedPages = 0;
            invalidPages = 0;
            while (started.get()) {
                try {
                    Pair<URL, Document> toClassify = inputQueue.poll(100, TimeUnit.MILLISECONDS);
                    totalClassifiedPages += 1;
                    if (toClassify == null || toClassify.getKey() == null || toClassify.getValue() == null) {
                        invalidPages += 1;
                        continue;
                    }
                    if (pageClassifier.classify(toClassify.getValue()).equals(PageClassifier.POSITIVE)) {
                        classifiedQueue.add(toClassify);
                        positiveClassifiedPages += 1;
                    }
                } catch (InterruptedException ignored) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Stops the classifier module
     */
    public void stop() {
        started.set(false);
    }

    /**
     * Returns the current harvest ratio. The harvest ratio is the number of positive classified pages divided by the
     * total number of pages. If the module has not started or no pages are classified, NaN is returned. If the module
     * is stopped and started again, harvest ratio is reset.
     *
     * @return the current harvest ratio
     */
    public float getHarvestRatio() {
        return positiveClassifiedPages / totalClassifiedPages;
    }

    public float getTotalClassifiedPages() {
        return totalClassifiedPages;
    }

    public float getPositiveClassifiedPages() {
        return positiveClassifiedPages;
    }

    public float getInvalidPages() {
        return invalidPages;
    }
}
