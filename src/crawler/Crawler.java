package crawler;

import javafx.util.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class Crawler {
    private URL startPage;
    private Function<URL, Integer> ranker;
    private BlockingQueue<Pair<URL, Document>> downloadedPages;
    private Queue<Pair<URL, Integer>> pagesToVisit;
    private Set<URL> visitedPages;
    private AtomicBoolean started;

    private Pair<Integer, List<URL>> robots;

    public Crawler(URL domain, Function<URL, Integer> ranker, BlockingQueue<Pair<URL, Document>> downloadedPages) throws IOException {
        this.startPage = domain;
        this.ranker = ranker;
        this.downloadedPages = downloadedPages;
        pagesToVisit = new PriorityQueue<>((pair1, pair2) -> pair1.getValue() - pair2.getValue());
        visitedPages = new HashSet<>();
        started = new AtomicBoolean(false);

        pagesToVisit.offer(new Pair<>(domain, ranker.apply(domain)));
        robots = readRobots(domain);
    }


    private Pair<Integer, List<URL>> readRobots(URL domain) {
        try {
            String robots = Jsoup.parse(new URL(domain.toString() + "/robots.txt"), 0).text();
            List<URL> disallows = new ArrayList<>();
            int crawDelay = 0;
            String userAgent = null;
            for (String line : robots.split("\n\r|\n|\r")) {
                if (line.startsWith("User-agent:")) {
                    userAgent = line.substring(11).trim();
                } else if (userAgent != null && userAgent.equals("*")) {
                    if (line.startsWith("Disallow:")) {
                        disallows.add(new URL(domain.toString() + line.substring(10).trim()));
                    } else if (line.startsWith("Crawl-delay:")) {
                        crawDelay = Integer.parseInt(line.substring(12).trim());
                    }
                }
            }
            return new Pair<>(crawDelay, disallows);
        } catch (IOException e) {
            return new Pair<>(0, new ArrayList<>());
        }
    }

    private boolean shouldVisitURL(URL url) {
        if (visitedPages.contains(url)) {
            return false;
        }
        boolean containedInDisallow = false;
        for (URL disallow : robots.getValue()) {
            containedInDisallow |= url.toString().contains(disallow.toString());
        }
        return !containedInDisallow;
    }

    public void start() {
        if (started.get()) {
            throw new IllegalStateException("Crawler already started.");
        }
        started.set(true);
        new Thread(() -> {
            while (started.get()) {
                try {
                    Pair<URL, Integer> currentURL = pagesToVisit.poll();
                    Pair<URL, Document> page = Downloader.download(currentURL.getKey());
                    if (page == null || !shouldVisitURL(currentURL.getKey())) {
                        continue;
                    }
                    visitedPages.add(currentURL.getKey());
                    downloadedPages.add(page);
                    Elements pageInternalLinks = page.getValue().select("a[href]");
                    for (Element pageInternalLink : pageInternalLinks) {
                        URL pageInternalUrl = null;
                        try {
                            pageInternalUrl = new URL(pageInternalLink.absUrl("href"));
                        } catch (MalformedURLException e) {
                            continue;
                        }
                        pagesToVisit.offer(new Pair<>(pageInternalUrl, ranker.apply(pageInternalUrl)));
                    }
                    Thread.sleep(robots.getKey() * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stop() {
        started.set(false);
    }

}
