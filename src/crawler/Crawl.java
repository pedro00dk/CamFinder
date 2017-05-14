package crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Crawl implements Runnable{

	private static final int MAXIMO_PAGINAS = 80;
	/* � set pq o conjunto deve ter valores �nicos*/
	public Set<String> PaginasVisitadas = new HashSet<String>(); 
	public List<String> PaginasParaVisitar = new LinkedList<String>();
	public String url;
	public String dominio;
	public CrawlRobots robots = new CrawlRobots();
	int i = 0;

	public Crawl(String url, String dominio) {
		this.url = url;
		this.dominio = dominio;
	}

	
	/*
	 * Escolhe o proximo link que ser� visitado. Caso esse link j� tenha sido
	 * visitado, outro link ser� escolhido
	 */
	private String ProximaURL() {
		String ProximaURL;
		do {
			ProximaURL = this.PaginasParaVisitar.remove(0);
		} while (this.PaginasVisitadas.contains(ProximaURL));
		this.PaginasVisitadas.add(ProximaURL);
		return ProximaURL;
	}

	
	
	public void run(){
		/* Aqui que eh mandado a URL para o metodo de crawler da classe SpiderLEG */
			System.out.println(url);
			robots.robotstxt(dominio, PaginasVisitadas);
			
			while (this.PaginasVisitadas.size() < MAXIMO_PAGINAS) {
				String currentUrl;
				CrawlBody leg = new CrawlBody();
				if (this.PaginasParaVisitar.isEmpty()) {
					currentUrl = url;
					this.PaginasVisitadas.add(url);
				} else {
					currentUrl = this.ProximaURL();
				}
				leg.crawl(currentUrl, i, dominio); // aqui que a magica acontece
				if (dominio.equals("nikon")) {
					try {
						Thread.sleep(10000);
						System.out.println("pausoouu");
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("acordou");
					}
				}
				i = i + 1;
				// Adiciona os links daquela �p�gina a nosssa lista
				this.PaginasParaVisitar.addAll(leg.getLinks());
			}
			System.out.println(String.format("**Pronto!!** Foi visitado %s paginas na web", this.PaginasVisitadas.size()));
		
	}
	
	public Set<String> getPaginasVisitadas() {
		return PaginasVisitadas;
	}

	public void setPaginasVisitadas(Set<String> paginasVisitadas) {
		PaginasVisitadas = paginasVisitadas;
	}
}
