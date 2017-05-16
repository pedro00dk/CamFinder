package crawler;

import weka.classifiers.Classifier;

import java.util.*;

/**
 * Created by bjcc on 14/05/2017.
 */
public class CrawlClassifier implements Runnable {

    private static final int MAXIMO_PAGINAS = 80;
    /* Eh set pq o conjunto deve ter valores unicos*/
    public Set<String> pagesVisited = new HashSet<String>();
    List<String> pagesForView = new LinkedList<>();
    List<String> positive = new LinkedList<>();
    List<String> next0 = new LinkedList<>();
    List<String> next1 = new LinkedList<>();
    List<String> next2 = new LinkedList<>();
    public String url;
    public String domain;
    public CrawlRobots robots = new CrawlRobots();
    LinkClassifier classifier;
    int i = 0;


    public CrawlClassifier(String url, String domain, Classifier classifier) throws Exception {

        classifier = new LinkClassifier(0.65f);
        this.url = url;
        this.domain = domain;
    }


    private String nextURL() throws Exception {
        String nextURL;
        if (positive.size() > 0) {
            nextURL = positive.remove(0);
            pagesVisited.add(nextURL);
            return nextURL;
        }
        if (next0.size() > 0) {
            nextURL = next0.remove(0);
            pagesVisited.add(nextURL);
            return nextURL;
        }
        if (next1.size() > 0) {
            nextURL = next1.remove(0);
            pagesVisited.add(nextURL);
            return nextURL;
        } else {
            nextURL = pagesForView.remove(0);
            pagesVisited.add(nextURL);
            return nextURL;
        }

    }

    public void run() {
        /* Aqui que eh mandado a URL para o metodo de crawler da classe SpiderLEG */
        System.out.println(url);
        robots.robotstxt(domain, pagesVisited);

        while (this.pagesVisited.size() < MAXIMO_PAGINAS) {
            String currentUrl = null;
            Downloader leg = new Downloader();
            if (this.pagesForView.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);
            } else {
                try {
                    currentUrl = this.nextURL();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            leg.download(currentUrl, i, domain); // aqui que a magica acontece

            if (domain.equals("nikon")) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i = i + 1;
            //Adiciona os links daquela pagina a nosssa lista

            for (int j = 0; j < leg.getLinks().size(); j++) {
                double x = 0;
                String y = leg.getLinks().get(j);
                try {
                    x = classifier.classify(leg.getLinks().get(j));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (x == 3.0 && !next2.contains(y)) next2.add(y);
                if (x == 2.0 && !next1.contains(y)) next1.add(y);
                if (x == 1.0 && !next0.contains(y)) next0.add(y);
                if (x == 0.0 && !positive.contains(y)) positive.add(y);



            }

            pagesForView.addAll(next0);
            pagesForView.addAll(next1);
            pagesForView.addAll(next2);
            pagesForView.addAll(positive);

        }
        System.out.println(String.format("**Pronto!!** Foi visitado %s paginas na web", this.pagesVisited.size()));

    }

}
