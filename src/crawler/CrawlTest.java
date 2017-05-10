package crawler;

import java.util.HashSet;
import java.util.Set;

public class CrawlTest {

	public static Set<String> PaginasSony = new HashSet<String>();
	public static Set<String> PaginasNikon = new HashSet<String>();
	public static Set<String> PaginasDPreview = new HashSet<String>();

	
	


	public static void main(String[] args) {
		
		Crawl spider1 = new Crawl("http://www.sony.com.br/electronics/cameras-digitais", "sony");
		Crawl spider2 = new Crawl("http://www.nikonusa.com/en/index.page", "nikon");
		Crawl spider3 = new Crawl("https://www.dpreview.com/products/cameras/all", "dpreview");
		Crawl spider4 = new Crawl("https://shop.usa.canon.com/shop/en/catalog", "canon");
		Crawl spider5 = new Crawl("http://www.samsung.com/uk/cameras/finder/?cameras", "samsung");
		Crawl spider6 = new Crawl("http://www.letsgodigital.org/"
				+ "en/camera/products/HP.html", "letsgodigital");
		Crawl spider7 = new Crawl("http://www.visions.ca/Catalogue/Catego"
				+ "ry/ProductResults.aspx?categoryId=223&menu=205", "visions");
		Crawl spider8 = new Crawl("https://www.sigmaphoto.com/cameras", "sigmaphoto");
		Crawl spider9 = new Crawl("http://us.ricoh-imaging.com/index.php/shop/cameras", "ricoh");
		Crawl spider10 = new Crawl("https://www.newegg.com/Product/ProductList.aspx?Submit="
				+ "ENE&IsNodeId=1&bop=And&PMSub=784%2012%2056756&PMSubCP=0cm_sp=Cat_Digita"
				+ "l-Cameras_1-_-VisNav-_-Cameras_1", "newegg");
		
		
		Thread t1 = new Thread(spider1);
		Thread t2 = new Thread(spider2);
		Thread t3 = new Thread(spider3);
		Thread t4 = new Thread(spider4);
		Thread t5 = new Thread(spider5);
		Thread t6 = new Thread(spider6);
		Thread t7 = new Thread(spider7);
		Thread t8 = new Thread(spider8);
		Thread t9 = new Thread(spider9);
		Thread t10 = new Thread(spider10);
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		t6.start();
		t7.start();
		t8.start();
		t9.start();
		t10.start();
		
	}
}
