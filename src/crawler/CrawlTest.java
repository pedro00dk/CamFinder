package crawler;

import java.util.HashSet;
import java.util.Set;

public class CrawlTest {
	static Thread t[] = new Thread[10];
	static Crawl spider[] = new Crawl[10];

	public static void main(String[] args) {
		
		spider[0] = new Crawl("http://www.sony.com/electronics/cameras", "sony");
		spider[1] = new Crawl("http://www.nikonusa.com/en/index.page", "nikon");
		spider[2] = new Crawl("https://www.dpreview.com/products/cameras/all", "dpreview");
		spider[3] = new Crawl("https://shop.usa.canon.com/shop/en/catalog", "canon");
		spider[4] = new Crawl("http://www.currys.co.uk/gbuk/cameras-and-camcorders-38-u.html", "currys");
		spider[5] = new Crawl("http://www.wexphotographic.com/cameras/", "wexphotographic");
		spider[6] = new Crawl("http://www.visions.ca/Catalogue/Catego"
				+ "ry/ProductResults.aspx?categoryId=223&menu=205", "visions");
		spider[7] = new Crawl("https://www.sigmaphoto.com/cameras", "sigmaphoto");
		spider[8] = new Crawl("http://us.ricoh-imaging.com/index.php/shop/cameras", "ricoh");
		spider[9] = new Crawl("https://www.newegg.com/Product/ProductList.aspx?Submit="
				+ "ENE&IsNodeId=1&bop=And&PMSub=784%2012%2056756&PMSubCP=0cm_sp=Cat_Digita"
				+ "l-Cameras_1-_-VisNav-_-Cameras_1", "newegg");

		/*for (int i = 0; i <= 10 ; i++) {
			t[i] = new Thread(spider[i]);
			t[i].start();
		}*/
		t[1] = new Thread(spider[1]);
		t[1].start();
		
	}
}
