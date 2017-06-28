package crawler;

import javafx.util.Pair;
import org.jsoup.nodes.Document;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

public class CrawlerModule {
    private List<Crawler> crawlers;
    private BlockingQueue<Pair<URL, Document>> outputQueue;

    public CrawlerModule(List<Crawler> crawlers, BlockingQueue<Pair<URL, Document>> outputQueue) {
        this.crawlers = Objects.requireNonNull(crawlers, "The crawler can not be null");
        this.outputQueue = Objects.requireNonNull(outputQueue, "The classified queue can not be null.");
    }

    public void start() {
        for (Crawler crawler : crawlers) {
            crawler.start();
        }
    }

    public void stop() {
        for (Crawler crawler : crawlers) {
            crawler.stop();
        }
    }
}
