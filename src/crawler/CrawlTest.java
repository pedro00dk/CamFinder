package crawler;

import java.util.Scanner;

public class CrawlTest {

	static Thread t[] = new Thread[10];
	static CrawlBFS spider[] = new CrawlBFS[10];
	static CrawlClassifier spider2[] = new CrawlClassifier[10];
	static LinkClassifier classifier;

	public static void main(String[] args) throws Exception {

		int x;
		Scanner in = new Scanner(System.in);

		System.out.println("(1) - Busca em Largura\n(2) - Algoritmo de Apredizagem");
		x = in.nextInt();

		if(x == 1 )crawler1();
		else crawler2();
		
	}

		public static void crawler1(){

			spider[0] = new CrawlBFS("http://www.sony.com/electronics/cameras", "sony");
			spider[1] = new CrawlBFS("http://www.nikonusa.com/en/index.page", "nikon");
			spider[2] = new CrawlBFS("https://www.dpreview.com/products/cameras/all", "dpreview");
			spider[3] = new CrawlBFS("https://shop.usa.canon.com/shop/en/catalog", "canon");
			spider[4] = new CrawlBFS("http://www.currys.co.uk/gbuk/cameras-and-camcorders-38-u.html", "currys");
			spider[5] = new CrawlBFS("http://www.wexphotographic.com/cameras/", "wexphotographic");
			spider[6] = new CrawlBFS("http://www.visions.ca/Catalogue/Catego"
					+ "ry/ProductResults.aspx?categoryId=223&menu=205", "visions");
			spider[7] = new CrawlBFS("https://www.sigmaphoto.com/cameras", "sigmaphoto");
			spider[8] = new CrawlBFS("http://us.ricoh-imaging.com/index.php/shop/cameras", "ricoh");
			spider[9] = new CrawlBFS("https://www.newegg.com/Product/ProductList.aspx?Submit="
					+ "ENE&IsNodeId=1&bop=And&PMSub=784%2012%2056756&PMSubCP=0cm_sp=Cat_Digita"
					+ "l-Cameras_1-_-VisNav-_-Cameras_1", "newegg");

		/*for (int i = 0; i <= 10 ; i++) {
			t[i] = new Thread(spider[i]);
			t[i].start();
		}*/
			t[1] = new Thread(spider[1]);
			t[1].start();
	 }

	 public static void crawler2() throws Exception {
		 	spider2[0] = new CrawlClassifier("http://www.nikonusa.com/en/index.page", "nikon");
		 	t[0] = new Thread(spider2[0]);
		 	t[0].start();

	 }
}
