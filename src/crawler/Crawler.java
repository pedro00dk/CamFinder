package crawler;

import javafx.util.Pair;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class Crawler {
    private URL startPage;
    private Function<URL, Integer> strategy;
    private Queue<Pair<URL, Integer>> pagesToVisit;
    private Set<URL> visitedPages;

    private Downloader downloader;
    private AtomicBoolean started;

    private Pair<Integer, List<URL>> robots;

    public Crawler(URL domain, Function<URL, Integer> strategy) throws IOException {
        this.startPage = domain;
        this.strategy = strategy;
        pagesToVisit = new PriorityQueue<>((pair1, pair2) -> pair1.getValue() - pair2.getValue());
        visitedPages = new HashSet<>();
        downloader = new Downloader();
        started = new AtomicBoolean(false);

        pagesToVisit.add(new Pair<>(domain, strategy.apply(domain)));
        robots = readRobots(domain);
    }


    private Pair<Integer, List<URL>> readRobots(URL domain) throws IOException {
        String robots = Jsoup.parse(new URL(domain.toString() + "/robots.txt"), 0).text();
        List<URL> disallows = new ArrayList<>();
        int crawDelay = 0;
        for (String line : robots.split("\n\r|\n|\r")) {
            if (line.startsWith("Disallow:")) {
                disallows.add(new URL(domain.toString() + line.substring(10).trim()));
            } else if (line.startsWith("Crawl-delay:")) {
                crawDelay = Integer.parseInt(line.substring(12).trim());
            }
        }
        return new Pair<>(crawDelay, disallows);
    }

    public boolean shouldVisitURL(URL url) {
        if (visitedPages.contains(url)) {
            return false;
        }
        boolean containedInDisallow = false;
        for (URL disallow : robots.getValue()) {
            containedInDisallow |= url.toString().contains(disallow.toString());
        }
        return !containedInDisallow;
    }


}
