import classifier.ClassifierModule;
import classifier.PageClassifier;
import crawler.Crawler;
import crawler.CrawlerModule;
import crawler.LinkClassifier;
import extractor.ExtractorModule;
import extractor.GeneralExtractor;
import javafx.util.Pair;
import org.jsoup.nodes.Document;
import util.SerializationUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        BlockingQueue<Pair<URL, Document>> crawlersOutput = new LinkedBlockingQueue<>();
        BlockingQueue<Pair<URL, Document>> classifierOutput = new LinkedBlockingQueue<>();
        BlockingQueue<Pair<URL, Map<String, String>>> extractorOutput = new LinkedBlockingQueue<>();

        System.out.println("Creating crawler module");
        CrawlerModule crawlerModule = createCrawlerModule(crawlersOutput);
        System.out.println("Creating classifier module");
        ClassifierModule classifierModule = createClassifierModule(crawlersOutput, classifierOutput);
        System.out.println("Creating extractor module");
        ExtractorModule extractorModule = createExtractorModule(classifierOutput, extractorOutput);

        final AtomicBoolean printerRoutineRunning = new AtomicBoolean(true);
        final Thread printerRoutine = new Thread(() -> {
            while (printerRoutineRunning.get()) {
                System.out.println("Craw out: " + crawlersOutput.size());
                System.out.println("Class out: " + classifierOutput.size());
                System.out.println("Total positive classified pages " + classifierModule.getPositiveClassifiedPages());
                System.out.println("Total classified pages " + classifierModule.getTotalClassifiedPages());
                System.out.println("Harvest ratio: " + classifierModule.getHarvestRatio());
                System.out.println("Extr out: " + extractorOutput.size());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("Starting output printer routine");
        printerRoutine.start();

        System.out.println("Starting modules");
        crawlerModule.start();
        classifierModule.start();
        extractorModule.start();

        long startTime = System.currentTimeMillis();
        new Scanner(System.in).nextLine();

        System.out.println("Stoping modules");
        crawlerModule.stop();
        classifierModule.stop();
        extractorModule.stop();

        System.out.println("Stoping output printer routine");
        printerRoutineRunning.set(false);

        long stopTime = System.currentTimeMillis();
        System.out.println("Total time = " + (stopTime - startTime) / 1000.0 + "secs");

        Path extractedContentPath = Paths.get("pages", "extracted", "extracted.data");
        ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(extractedContentPath));
        os.writeObject(extractorOutput);
        os.close();
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

        // se for BFS Function<URL, Integer> bsfRank = url -> 1;

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
        crawlers.add(new Crawler(new URL("https://www.newegg.com/Product/ProductList.aspx?Submit=ENE&IsNodeId=1&bop=And&PMSub=784%2012%2056756&PMSubCP=0cm_sp=Cat_Digital-Cameras_1-_-VisNav-_-Cameras_1"), classRank, crawlersOutput));
        return new CrawlerModule(crawlers, crawlersOutput);
    }

    private static ClassifierModule createClassifierModule(BlockingQueue<Pair<URL, Document>> crawlersOutput, BlockingQueue<Pair<URL, Document>> classifierOutput) throws IOException, ClassNotFoundException {
        List<PageClassifier> pageClassifiers = SerializationUtils.deserialize(Paths.get("classifiers", "serialized.model"));
        PageClassifier selectedPageClassifier = pageClassifiers.get(0);
        selectedPageClassifier.setDefaultClassifierIndex(6);
        return new ClassifierModule(selectedPageClassifier, crawlersOutput, classifierOutput);
    }

    private static ExtractorModule createExtractorModule(BlockingQueue<Pair<URL, Document>> classifierOutput, BlockingQueue<Pair<URL, Map<String, String>>> extractorOutput) {
        return new ExtractorModule(new GeneralExtractor(), classifierOutput, extractorOutput);
    }
}
