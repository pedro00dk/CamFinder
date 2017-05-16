package extractor;

import extractor.specific.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;

public class EvaluateExtractor {

    public static void main(String[] args) throws IOException {
        //CANON
//        int canonAttributesCount = 0;
//        double canonPagesCount = 16.0;
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx540-hs"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx420-is-red"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx700-hs-black"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx610-hs-red"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-elph-180-silver"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-elph-190-is-black"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-ef-s-18-135mm-f-3-5-5-6-is-usm-kit-refurbished"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-body-refurbished"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-rebel-t5-red-ef-s-18-55mm-is-ii-lens-kit-refurbished"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-ef-s-18-55mm-f-3-5-5-6-is-stm-kit-refurbished"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-rebel-t6-ef-s-18-55mm-f-35-56-is-ii-kit-refurbished"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-m3-body-refurbished-camera"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-m10-ef-m-15-45mm-f-3-5-6-3-is-stm-kit-white-refurbished"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-rebel-sl1-ef-s-18-55mm-is-stm-kit-white-ef-75-300mm-f-4-56-iii-refurbished"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-5ds-r-body-refurbished"));
//        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-5ds-body-refurbished"));
//
//        System.out.println("Canon -> " + canonAttributesCount / ((CanonExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2)* canonPagesCount));

//        //SONY
//        int sonyAttributesCount = 0;
//        double sonyPagesCount = 12.0;
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-5100-body-kit/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-6300-body-kit/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-6500-body-kit/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilca-99m2/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-7rm2/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-7m2-body-kit/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx100m5/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx10m3/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx1rm2/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-hx90v/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-wx500/specifications"));
//        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx10m2/specifications"));
//
//        System.out.println("Sony -> " + sonyAttributesCount / ((SonyExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * sonyPagesCount));
//
//       // NIKON
//        int nikonAttributesCount = 0;
//        double nikonPagesCount = 14.0;
//
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d3400.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d7200.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d610.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d750.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d810.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/nikon-df.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d810.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/nikon1/nikon-1-v3.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/nikon1/nikon-1-j5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
//
//        System.out.println("Nikon -> " + nikonAttributesCount / ((NikonExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * nikonPagesCount));
//        //VISIONS
//        int visionsAttributesCount = 0;
//        double visionsPagesCount = 13.0;
//
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=686&productId=28317&sku=P530BREFURB"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=21085&sku=DMCTS5A"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=21537&sku=EOSREBELT5I"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=31819&sku=EOSREBELT6KITDC"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27978&sku=DMCGF7KS"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27989&sku=EOSREBELT6I"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27458&sku=S3700BLACK"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=31595&sku=EOSREBELT6KITIS"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27881&sku=DMCFZ200K"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30974&sku=SX720HSRED"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27464&sku=L32BLACK"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30973&sku=SX720HSBLACK"));
//        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30395&sku=SX420IS"));
//
//        System.out.println("Visions -> " + visionsAttributesCount / ((VisionsExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * visionsPagesCount));
//        //SIGMAPHOTO
//        int sigmaPhotoAttributesCount = 0;
//        double sigmaPhotoPagesCount = 9.0;
//
//        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/mirrorless/sd-quattro-camera"));
//        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/mirrorless/sd-quattro-h-camera"));
//        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/dslr/sd1-merrill-digital-slr-camera-0"));
//        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp0-quattro-compact-digital-camera"));
//        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp1-quattro-compact-digital-camera"));
//        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp2-merrill-compact-digital-camera"));
//        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp3-merrill-compact-digital-camera-0"));
//        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp2-quattro-compact-digital-camera"));
//        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp3-quattro-compact-digital-camera"));
//
//        System.out.println("Sigma Photo -> " + sigmaPhotoAttributesCount / ((SigmaPhotoExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * sigmaPhotoPagesCount));


        //RICOH
        int ricohAttributesCount = 0;
        double ricohPagesCount = 11.0;

        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-645z"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-1"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-3-ii"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-70"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-s2"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/theta-s"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/wg-m2"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/gr-2"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/theta-sc"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-50"));
        ricohAttributesCount += getExtractedAttributesSize(new RicohExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-kp"));

        System.out.println("Ricoh -> " + ricohAttributesCount / ((RicohExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * ricohPagesCount));
    }


    static int getExtractedAttributesSize(CameraDomainExtractor extractor, URL url) throws IOException {
        return extractor.extractWebSiteContent(Jsoup.parse(url, 0)).size();
    }
}
