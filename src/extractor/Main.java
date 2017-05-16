package extractor;

import extractor.specific.RicohExtractor;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
//        //CANON
//        testExtractor(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx540-hs"));
//        testExtractor(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-body-refurbished"));
//        testExtractor(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-5ds-r-body-refurbished"));
//
//        //SONY
//        testExtractor(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-5100-body-kit/specifications"));
//        testExtractor(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-wx500/specifications"));
//        testExtractor(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx1rm2/specifications"));
//
//        //NIKON
//        testExtractor(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        testExtractor(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d3400.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        testExtractor(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//
//        //VISIONS
//        testExtractor(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=686&productId=28317&sku=P530BREFURB"));
//        testExtractor(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30395&sku=SX420IS"));
//        testExtractor(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27458&sku=S3700BLACK"));
//
//        //SIGMA PHOTO
//        testExtractor(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/mirrorless/sd-quattro-camera"));
//        testExtractor(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp1-quattro-compact-digital-camera"));
//        testExtractor(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp2-merrill-compact-digital-camera"));
//
//        //RICOH
//        testExtractor(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-645z"));
//        testExtractor(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/wg-m2"));
//        testExtractor(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-s2"));
//
//        //DPPREVIEW
//        testExtractor(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/fujifilm/slrs/fujifilm_xa3/specifications"));
//        testExtractor(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/canon/slrs/canon_eosm6/specifications"));
//        testExtractor(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/sony/compacts/sony_dscrx100m5/specifications"));
//        testExtractor(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/canon/slrs/canon_eos77d/specifications"));
//
//        //NEW EGG
//        testExtractor(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIABKX58W3053"));
//        testExtractor(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIAB2G5515538"));
//        testExtractor(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIAB925C69394"));
//
//        //CURRYS
//        testExtractor(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d3300-dslr-camera-with-18-55-mm-f-3-5-5-6-lens-black-10135135-pdt.html"));
//        testExtractor(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-80d-dslr-camera-black-body-only-10145630-pdt.html"));
//        testExtractor(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d7200-dslr-camera-black-body-only-10124382-pdt.html"));
//
//        //WEB PHOTO GRAPHIC
//        testExtractor(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/nikon-d5-digital-slr-camera-body-dual-xqd-1589610/"));
//        testExtractor(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/canon-eos-7d-mark-ii-digital-slr-camera-body-1560196/"));
//        testExtractor(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/pentax-k-1-digital-slr-camera-body-1592180/"));
//        testExtractor(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/canon-eos-5d-mark-iii-digital-slr-camera-body-1530010/"));


        //GENERAL EXTRACTOR
        System.out.println("NIKON");
        testExtractor(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d3400.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        System.out.println("NEW EGG");
        testExtractor(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=N82E16830120835"));
        System.out.println("CURRYS");
        testExtractor(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d3300-dslr-camera-with-18-55-mm-f-3-5-5-6-lens-black-10135135-pdt.html"));
        System.out.println("CANON");
        testExtractor(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx60-hs"));
        System.out.println("SONY");
        testExtractor(new GeneralExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilca-99m2/specifications"));
        System.out.println("VISIONS");
        testExtractor(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=21085&sku=DMCTS5A"));
        System.out.println("SIGMAPHOTO");
        testExtractor(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/mirrorless/sd-quattro-camera"));
        System.out.println("RICOH");
        testExtractor(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-3-ii"));
        System.out.println("DP PREVIEW");
        testExtractor(new GeneralExtractor(), new URL("https://www.dpreview.com/products/pentax/slrs/pentax_kp/specifications"));
        System.out.println("WEB PHOTOGRAPHIC");
        testExtractor(new GeneralExtractor(), new URL("http://www.wexphotographic.com/nikon-d5-digital-slr-camera-body-dual-xqd-1589610/"));

    }


    static void testExtractor(CameraDomainExtractor extractor, URL url) throws IOException {
        System.out.println("Extracting page content from " + extractor.domain());
        Map<String, String> content = extractor.extractWebSiteContent(Jsoup.parse(url, 0));
        content.entrySet()
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));
        System.out.println();
    }
}