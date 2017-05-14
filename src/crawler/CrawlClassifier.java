package crawler;

import com.sun.org.apache.regexp.internal.RE;

import java.util.*;

/**
 * Created by bjcc on 14/05/2017.
 */
public class CrawlClassifier implements Runnable {

    private static final int MAXIMO_PAGINAS = 80;
    /* Eh set pq o conjunto deve ter valores unicos*/
    public Set<String> PaginasVisitadas = new HashSet<String>();
    List<String> paginasParaVisitar = new LinkedList<>();
    List<String> positive = new LinkedList<>();
    List<String> next0 = new LinkedList<>();
    List<String> next1 = new LinkedList<>();
    List<String> next2 = new LinkedList<>();
    public String url;
    public String dominio;
    public CrawlRobots robots = new CrawlRobots();
    LinkClassifier classifier = new LinkClassifier(0.65f);
    int i = 0;


    public CrawlClassifier(String url, String dominio) throws Exception {
        this.url = url;
        this.dominio = dominio;
    }

    private String ProximaURL() throws Exception {
        String ProximaURL;
        if (positive.size()>0){
            ProximaURL = positive.remove(0);
            return ProximaURL;
        }
        if (next0.size()>0){
            ProximaURL = next0.remove(0);
            return ProximaURL;
        }
        if (next1.size()>0){
            ProximaURL = next1.remove(0);
            return ProximaURL;
        }
        if (next2.size()>0){
            ProximaURL = next2.remove(0);
            return ProximaURL;
        }

        ProximaURL = paginasParaVisitar.remove(0);
        return ProximaURL;
    }

    public void run() {
        /* Aqui que eh mandado a URL para o metodo de crawler da classe SpiderLEG */
        System.out.println(url);
        robots.robotstxt(dominio, PaginasVisitadas);

        while (this.PaginasVisitadas.size() < MAXIMO_PAGINAS) {
            String currentUrl = null;
            CrawlBody leg = new CrawlBody();
            if (this.paginasParaVisitar.isEmpty()) {
                currentUrl = url;
                this.PaginasVisitadas.add(url);
            } else {
                try {
                    currentUrl = this.ProximaURL();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            leg.crawl(currentUrl, i, dominio); // aqui que a magica acontece
            if (dominio.equals("nikon")) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i = i + 1;
            // Adiciona os links daquela �p�gina a nosssa lista

            for (int j = 0; j < leg.getLinks().size(); j++) {
                try {
                    if (classifier.classify(leg.getLinks().get(j)) == 0.2) {
                        next2.add(leg.getLinks().get(j));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (classifier.classify(leg.getLinks().get(j)) == 0.6) {
                        next1.add(leg.getLinks().get(j));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (classifier.classify(leg.getLinks().get(j)) == 0.8) {
                        next0.add(leg.getLinks().get(j));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (classifier.classify(leg.getLinks().get(j)) == 1) {
                        positive.add(leg.getLinks().get(j));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            paginasParaVisitar.addAll(next0);
            paginasParaVisitar.addAll(next1);
            paginasParaVisitar.addAll(next2);
            paginasParaVisitar.addAll(positive);

        }
        System.out.println(String.format("**Pronto!!** Foi visitado %s paginas na web", this.PaginasVisitadas.size()));

    }

}
