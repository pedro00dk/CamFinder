package crawler;

import java.util.Set;

public class CrawlRobots {
	public String url;

	public void robotstxt(String dominio, Set<String> PaginasVisitadas) {
		if (dominio == "nikon") {
			url = "http://www.nikon.com";
			PaginasVisitadas.add(url + "/mobile/");
			PaginasVisitadas.add(url + "/Ajax/");
			PaginasVisitadas.add(url + "/servlet/FormsServlet");
			PaginasVisitadas.add(url + "/servlet/proxy/solr.nikon");
			PaginasVisitadas.add(url + "/Dealers/");
			PaginasVisitadas.add(url + "/en/Nikon-Products/WhereToBuy.page");
			PaginasVisitadas.add(url + "/Find-Your-Nikon/WhereToBuy.page");
			PaginasVisitadas.add(url + "/en/Find-Your-Nikon/WhereToBuy.page");
			PaginasVisitadas.add(url + "/en/Nikon-Store/AddToCartConfirmation.page");
			PaginasVisitadas.add(url + "/*Hunting-TV/");
			PaginasVisitadas.add(url + "/Press-Room/");
			PaginasVisitadas.add(url + "/en/nikon-store/terms-and-conditions.page");
			PaginasVisitadas.add(url + "/en/about-nikon/terms-of-use.page");
		}

		if (dominio == "sony") {
			url = "http://www.sony.com";
			PaginasVisitadas.add(url + "/buy/");
			PaginasVisitadas.add(url + "/my-favorites");
		}

		if (dominio == "dpreview") {
			url = "https://www.dpreview.com";
			PaginasVisitadas.add(url + "/ad");
			PaginasVisitadas.add(url + "/api");
			PaginasVisitadas.add(url + "/forums/reply");
			PaginasVisitadas.add(url + "/reviews_data");
			PaginasVisitadas.add(url + "/search");
		}

		if (dominio == "canon") {
			url = "https://shop.usa.canon.com";
			PaginasVisitadas.add(url + "/eStore/checkout/");
			PaginasVisitadas.add(url + "/shop/AjaxOrderChangeServiceItemAdd");
			PaginasVisitadas.add(url + "/webapp/wcs/stores/servlet/shop/AjaxOrderChangeServiceItemAdd");
			PaginasVisitadas.add(url + "/webapp/wcs/stores/servlet/OrderItemAdd");
		}

		if (dominio == "visions") {
			url = "http://www.visions.ca";
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=661");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=662");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=713");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=646");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=645");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=652");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=651");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=653");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=659");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=656");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=654");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=655");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=648");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=649");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=663");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=647");
			PaginasVisitadas.add(url + "/catalogue/category/productresults.aspx?categoryid=650");
			PaginasVisitadas.add(url + "/Catalogue/category/productresults.aspx?categoryId=1054");
			PaginasVisitadas.add(url + "/Catalogue/category/productresults.aspx?categoryId=1055");
			PaginasVisitadas.add(url + "/content/internal/");
			PaginasVisitadas.add(url + "/catalogue/category/PrintView.aspx");
			PaginasVisitadas.add(url + "/catalogue/tellFriend/TellFriend.aspx");
			PaginasVisitadas.add(url + "/Images/Catalogue/Product/Manuals/");
		}

		if (dominio == "newegg") {
			url = "https://www.newegg.com";
			PaginasVisitadas.add(url + "/Common/BML/");
			PaginasVisitadas.add(url + "/Common/ThirdParty/");
			PaginasVisitadas.add(url + "/App/");
			PaginasVisitadas.add(url + "/Application/");
			PaginasVisitadas.add(url + "/Test.aspx");
			PaginasVisitadas.add(url + "/GiftCertificate/GiftCartPlus.aspx");
			PaginasVisitadas.add(url + "/GiftCertificate/GiftOrderCompleteHandle.aspx");
			PaginasVisitadas.add(url + "/GiftCertificate/GiftResponseFromBank.aspx");
			PaginasVisitadas.add(url + "/MyAccount/AudioCAPTCHA/");
			PaginasVisitadas.add(url + "/MyAccount/ImageValidator.aspx/");
			PaginasVisitadas.add(url + "/RMA/LabelSOResponseFromBank.aspx");
			PaginasVisitadas.add(url + "/RMA/RMALabelTest.aspx");
			PaginasVisitadas.add(url + "/Shopping/AddtoCart.aspx");
			PaginasVisitadas.add(url + "/Shopping/ResponseOrderFromBank.aspx");
			PaginasVisitadas.add(url + "/WishList/WishCartPlus.aspx");
			PaginasVisitadas.add(url + "/WishList/WishCartPlus.aspx");
			PaginasVisitadas.add(url + "/NewMyAccount/");
			PaginasVisitadas.add(url + "/MyNewegg/");
			PaginasVisitadas.add(url + "/SingleProductReview.aspx");
			PaginasVisitadas.add(url + "/singleproductreview.aspx");
			PaginasVisitadas.add(url + "/profile/reviews/");
			PaginasVisitadas.add(url + "/failedaddtocart.aspx");
			PaginasVisitadas.add(url + "/ProductBuzz2013.aspx");
			PaginasVisitadas.add(url + "/feedback/custratingallreview.aspx");
			PaginasVisitadas.add(url + "/feedback/videocenter.aspx");
			PaginasVisitadas.add(url + "/feedback/reviews.aspx");
			PaginasVisitadas.add(url + "/app/viewproductdesc.asp");
			PaginasVisitadas.add(url + "/app/viewproduct.asp");
			PaginasVisitadas.add(url + "/app/manufact.asp");
			PaginasVisitadas.add(url + "/app/showimage.asp");
			PaginasVisitadas.add(url + "/app/manufactory.asp");
			PaginasVisitadas.add(url + "/app/custratingreview.asp");
			PaginasVisitadas.add(url + "/app/catalog.asp");
			PaginasVisitadas.add(url + "/app/searchproductresult.asp");
			PaginasVisitadas.add(url + "/app/listproduct.asp");
			PaginasVisitadas.add(url + "/app/clearselect.asp");
			PaginasVisitadas.add(url + "/info/trackorder.aspx");
			PaginasVisitadas.add(url + "/info/cartforgamecrate.aspx");
			PaginasVisitadas.add(url + "/info/faqaboutshopping.aspx");
			PaginasVisitadas.add(url + "/HelpInfo/FAQDetail.aspx");
			PaginasVisitadas.add(url + "/info/alltermsandconditions.aspx");
			PaginasVisitadas.add(url + "/info/cartforgamecrate.aspx");
			PaginasVisitadas.add(url + "/info/cingularplan.aspx");
			PaginasVisitadas.add(url + "/info/cartforunlocked.aspx");
			PaginasVisitadas.add(url + "/info/cookie.aspx");
			PaginasVisitadas.add(url + "/info/faqorderinfo.aspx");
			PaginasVisitadas.add(url + "/info/policynonrefundable.aspx");
			PaginasVisitadas.add(url + "/info/trackorder.aspx");
			PaginasVisitadas.add(url + "/ImageGallery.aspx");
			PaginasVisitadas.add(url + "/SellerProfile.aspx");
			PaginasVisitadas.add(url + "/MessagePage.aspx");
			PaginasVisitadas.add(url + "/NewProduct.aspx");
			PaginasVisitadas.add(url + "/Product/PowerSearch.aspx");
			PaginasVisitadas.add(url + "/product/productcompare.aspx");
			PaginasVisitadas.add(url + "/CustomerReviews.aspx");
			PaginasVisitadas.add("https://www.newegg.com/neweggpremier");
		}

		if (dominio == "ricoh") {
			url = "http://us.ricoh-imaging.com";
			PaginasVisitadas.add(url + "/administrator/");
			PaginasVisitadas.add(url + "/bin/");
			PaginasVisitadas.add(url + "/cache/");
			PaginasVisitadas.add(url + "/cli/");
			PaginasVisitadas.add(url + "/components/");
			PaginasVisitadas.add(url + "/includes/");
			PaginasVisitadas.add(url + "/installation/");
			PaginasVisitadas.add(url + "/language/");
			PaginasVisitadas.add(url + "/layouts/");
			PaginasVisitadas.add(url + "/libraries/");
			PaginasVisitadas.add(url + "/logs/");
			PaginasVisitadas.add(url + "/modules/");
			PaginasVisitadas.add(url + "/plugins/");
			PaginasVisitadas.add(url + "/tmp/");

		}

		if (dominio == "sigmaphoto") {
			url = "https://www.sigmaphoto.com";
			PaginasVisitadas.add(url + "/404/");
			PaginasVisitadas.add(url + "/app/");
			PaginasVisitadas.add(url + "/manage/");
			PaginasVisitadas.add(url + "/cgi-bin/");
			PaginasVisitadas.add(url + "/downloader/");
			PaginasVisitadas.add(url + "/includes/");
			PaginasVisitadas.add(url + "/js/");
			PaginasVisitadas.add(url + "/lib/");
			PaginasVisitadas.add(url + "/magento/");
			PaginasVisitadas.add(url + "/media/");
			PaginasVisitadas.add(url + "/pkginfo/");
			PaginasVisitadas.add(url + "/report/");
			PaginasVisitadas.add(url + "/scripts/");
			PaginasVisitadas.add(url + "/shell/");
			PaginasVisitadas.add(url + "/skin/");
			PaginasVisitadas.add(url + "/stats/");
			PaginasVisitadas.add(url + "/var/");
		}

	}

}
