package crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlBody

{
	//Com esse fake USER o servidor vai pensar que nossa aplicacaoo e um
	// navegador normal
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

	private List<String> links = new LinkedList<String>();
	private Document htmlDocument;
	FileWriter documento;

	/*
	 * O JSOUP facilita mto a vida porque a gente n vai ter que implementar a
	 * parte de requisi��es na rede, etc
	 */
	public boolean crawl(String url, int i, String dominio)
    {
    	//if(url=="http://www.nikonusa.com/en/index.page") pause();
    	
    	if(notEmpty(url)==true){
    		try
            {
            	//Faz a conexao com o servidor de determinado link usando nosso fake agent
                Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
                //Pega o HTML da pagina
                Document htmlDocument = connection.get();
                this.htmlDocument = htmlDocument;
                //content type
                String content = connection.response().contentType();
                System.out.println(content);
                
                /*Aqui eu vou salvar os HTML em formato html* "("+i+")"+".html"*/
                try {
                documento = new FileWriter(new File("C:\\Users\\bjcc\\Documents\\Pages Crawler", dominio+"("+i+")"+".html"));
                documento.write(htmlDocument.html());
                documento.close();
                
                } catch (IOException e){
                	e.printStackTrace();
                }catch (Exception e) {
        			e.printStackTrace();
        		}
                
                
                /*Aqui exibe se eu consegui visitar ou nao a pagina*/
                if(connection.response().statusCode() == 200)  //200 significa OK, sem erros
                {
                    System.out.println("\n**Visitando**  " + url);
                }
                if(!connection.response().contentType().contains("text/html"))
                {
                    System.out.println("**Erro ao visitar este link**  " + url);
                    return false;
                }
                
                /*Pega os links da pagina*/
                Elements linksOnPage = htmlDocument.select("a[href]"); //"a[href] � uma tag para linkar outras p�ginas no HTML
                System.out.println("Essa pagina tem (" + linksOnPage.size() + ") links");
                for(Element link : linksOnPage)
                {
                    this.links.add(link.absUrl("href"));
                }
                return true;
            }
            catch(IOException ioe)
            {
                //Se der merda
                return false;
            }
    	}
    		return false;
    		
    }


	public static boolean notEmpty(String string) {
		if (string == null || string.length() == 0) return false;
			//throw new IllegalArgumentException("A URL t� vazia");
		return true;

	}

	// Lista de links daquela pagina
	public List<String> getLinks() {
		return this.links;
	}

}