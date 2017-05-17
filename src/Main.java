import classifier.ClassifierModule;
import classifier.PageClassifier;
import crawler.Crawler;
import crawler.CrawlerModule;
import crawler.LinkClassifier;
import javafx.util.Pair;
import org.jsoup.nodes.Document;
import util.SerializationUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        BlockingQueue<Pair<URL, Document>> crawlersOutput = new LinkedBlockingQueue<>();
        BlockingQueue<Pair<URL, Document>> classifierOutput = new LinkedBlockingQueue<>();

        CrawlerModule crawlerModule = createCrawlerModule(crawlersOutput);
        ClassifierModule classifierModule = createClassifierModule(crawlersOutput, classifierOutput);

        crawlerModule.start();
        classifierModule.start();

        Thread.sleep(300000);

        crawlerModule.stop();
        classifierModule.stop();

        System.out.println("Harvest ratio: " + classifierModule.getHarvestRatio());
        System.out.println("Total positive classified pages " + classifierModule.getPositiveClassifiedPages());
        System.out.println("Total classified pages " + classifierModule.getTotalClassifiedPages());
        System.out.println("Total invalid pages " + classifierModule.getInvalidPages());
    }

    private static CrawlerModule createCrawlerModule(BlockingQueue<Pair<URL, Document>> crawlersOutput) throws Exception {
        Function<String, URL> stringToUrl = link -> {
            try {
                return new URL(link);
            } catch (MalformedURLException e) {
                return null;
            }
        };
        List<URL> negatives = Files.lines(Paths.get("pages", "ranked", "negative.txt")).map(stringToUrl).filter(Objects::nonNull).collect(Collectors.toList());
        List<URL> nexts0 = Files.lines(Paths.get("pages", "ranked", "next0.txt")).map(stringToUrl).filter(Objects::nonNull).collect(Collectors.toList());
        List<URL> nexts1 = Files.lines(Paths.get("pages", "ranked", "next1.txt")).map(stringToUrl).filter(Objects::nonNull).collect(Collectors.toList());
        List<URL> positives = Files.lines(Paths.get("pages", "ranked", "positive.txt")).map(stringToUrl).filter(Objects::nonNull).collect(Collectors.toList());
        LinkClassifier linkClassifier = new LinkClassifier(negatives, nexts0, nexts1, positives, 0.75f);
        Function<URL, Integer> classRank = url -> {
            try {
                String clazz = linkClassifier.classify(url);
                switch (clazz) {
                    case LinkClassifier.NEGATIVE:
                        return 4;
                    case LinkClassifier.NEXT_0:
                        return 3;
                    case LinkClassifier.NEXT_1:
                        return 2;
                    case LinkClassifier.POSITIVE:
                        return 1;
                }
            } catch (Exception ignored) {
            }
            return 5;
        };

        // Function<URL, Integer> bsfRank = url -> 1;

        List<Crawler> crawlers = new ArrayList<>();
        crawlers.add(new Crawler(new URL("http://www.sony.com/electronics/cameras"), classRank, crawlersOutput));
        crawlers.add(new Crawler(new URL("http://www.nikonusa.com/en/index.page"), classRank, crawlersOutput));
        crawlers.add(new Crawler(new URL("https://www.dpreview.com/products/cameras/all"), classRank, crawlersOutput));
        crawlers.add(new Crawler(new URL("https://shop.usa.canon.com/shop/en/catalog"), classRank, crawlersOutput));
        crawlers.add(new Crawler(new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders-38-u.html"), classRank, crawlersOutput));
        crawlers.add(new Crawler(new URL("http://www.wexphotographic.com/cameras/"), classRank, crawlersOutput));
        crawlers.add(new Crawler(new URL("http://www.visions.ca/Catalogue/Category/ProductResults.aspx?categoryId=223&menu=205"), classRank, crawlersOutput));
        crawlers.add(new Crawler(new URL("https://www.sigmaphoto.com/cameras"), classRank, crawlersOutput));
        crawlers.add(new Crawler(new URL("http://us.ricoh-imaging.com/index.php/shop/cameras"), classRank, crawlersOutput));
        crawlers.add(new Crawler(new URL("https://www.newegg.com/Product/ProductList.aspx?Submit=ENE&IsNodeId=1&bop=And&PMSub=784%2012%2056756&PMSubCP=0cm_sp=Cat_Digital-Cameras_1-_-VisNav-_-Cameras_1"), classRank, null));
        return new CrawlerModule(crawlers, crawlersOutput);
    }

    private static ClassifierModule createClassifierModule(BlockingQueue<Pair<URL, Document>> crawlersOutput, BlockingQueue<Pair<URL, Document>> classifierOutput) throws IOException, ClassNotFoundException {
        List<PageClassifier> pageClassifiers = SerializationUtils.deserialize(Paths.get("classifiers", "serialized.model"));
        PageClassifier selectedPageClassifier = pageClassifiers.get(0);
        selectedPageClassifier.setDefaultClassifierIndex(6);
        return new ClassifierModule(selectedPageClassifier, crawlersOutput, classifierOutput);
    }
}
