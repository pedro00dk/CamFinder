package crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CrawlBFS implements Runnable {

    private static final int MAXIMO_PAGINAS = 80;
    /* Eh set pq o conjunto deve ter valores unicos*/
    public Set<String> visited = new HashSet<String>();
    public List<String> pagesToVisit = new LinkedList<String>();
    public String url;
    public String domain;
    public CrawlRobots robots = new CrawlRobots();
    int i = 0;


    public CrawlBFS(String url, String domain) {
        this.url = url;
        this.domain = domain;
    }


    /*
     * Escolhe o proximo link que sera visitado. Caso esse link ja tenha sido
     * visitado, outro link sera escolhido
     */
    private String ProximaURL() {
        String ProximaURL;
        if(domain.equals("nikon") || domain.equals("currys") || domain.equals("wexphotographic")){
            do {
                ProximaURL = this.pagesToVisit.remove(0);
            } while (this.visited.contains(ProximaURL) && robots.verifyURL(ProximaURL, domain));
            this.visited.add(ProximaURL);
            return ProximaURL;
        }
        else{
            do {
                ProximaURL = this.pagesToVisit.remove(0);
            } while (this.visited.contains(ProximaURL));
            this.visited.add(ProximaURL);
            return ProximaURL;
        }
    }


    public void run() {
        /* Aqui que eh mandado a URL para o metodo de crawler da classe SpiderLEG */
        System.out.println(url);
        robots.robotstxt(domain, visited);

        while (this.visited.size() < MAXIMO_PAGINAS) {
            String currentUrl;
            Downloader leg = new Downloader();
            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.visited.add(url);
            } else {
                currentUrl = this.ProximaURL();
            }
            leg.download(currentUrl, i, domain); // aqui que a magica acontece
            if (domain.equals("nikon")) {
                try {
                    Thread.sleep(10000);
                    System.out.println("pausoouu");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("acordou");
                }
            }
            i = i + 1;
            // Adiciona os links daquela pagina a nosssa lista
            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println(String.format("**Pronto!!** Foi visitado %s paginas na web", this.visited.size()));

    }

}
