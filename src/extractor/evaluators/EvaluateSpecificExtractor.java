package extractor.evaluators;

import extractor.CameraDomainExtractor;
import extractor.specific.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;

public class EvaluateSpecificExtractor {

    public static void main(String[] args) throws IOException {
        //CANON
        int canonAttributesCount = 0;
        double canonPagesCount = 16.0;
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx540-hs"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx420-is-red"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx700-hs-black"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx610-hs-red"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-elph-180-silver"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-elph-190-is-black"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-ef-s-18-135mm-f-3-5-5-6-is-usm-kit-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-body-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-rebel-t5-red-ef-s-18-55mm-is-ii-lens-kit-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-ef-s-18-55mm-f-3-5-5-6-is-stm-kit-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-rebel-t6-ef-s-18-55mm-f-35-56-is-ii-kit-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-m3-body-refurbished-camera"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-m10-ef-m-15-45mm-f-3-5-6-3-is-stm-kit-white-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-rebel-sl1-ef-s-18-55mm-is-stm-kit-white-ef-75-300mm-f-4-56-iii-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-5ds-r-body-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-5ds-body-refurbished"));

        double canonRecall = canonAttributesCount / ((CanonExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * canonPagesCount);
        double canonFMeasure = 2*(canonRecall)/(canonRecall+1);
        System.out.println("Canon:\n Recall: " + canonRecall + "\nF-measure" + canonFMeasure );

        //SONY
        int sonyAttributesCount = 0;
        double sonyPagesCount = 12.0;
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-5100-body-kit/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-6300-body-kit/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-6500-body-kit/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilca-99m2/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-7rm2/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-7m2-body-kit/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx100m5/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx10m3/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx1rm2/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-hx90v/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-wx500/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx10m2/specifications"));

        double sonyRecall = sonyAttributesCount / ((SonyExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * sonyPagesCount);
        double sonyFMeasure = 2*(sonyRecall)/(sonyRecall+1);
        System.out.println("Sony:\n Recall: " + sonyRecall + "\nF-measure" + sonyFMeasure );

        // NIKON
        int nikonAttributesCount = 0;
        double nikonPagesCount = 14.0;

        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d3400.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d7200.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d610.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d750.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d810.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/nikon-df.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d810.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/nikon1/nikon-1-v3.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/nikon1/nikon-1-j5.html#tab-ProductDetail-ProductTabs-TechSpecs"));

        double nikonRecall = nikonAttributesCount / ((NikonExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * nikonPagesCount);
        double nikonFMeasure = 2*(nikonRecall)/(nikonRecall+1);
        System.out.println("Nikon:\n Recall: " + nikonRecall + "\nF-measure" + nikonFMeasure );

        //VISIONS
        int visionsAttributesCount = 0;
        double visionsPagesCount = 13.0;

        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=686&productId=28317&sku=P530BREFURB"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=21085&sku=DMCTS5A"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=21537&sku=EOSREBELT5I"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=31819&sku=EOSREBELT6KITDC"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27978&sku=DMCGF7KS"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27989&sku=EOSREBELT6I"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27458&sku=S3700BLACK"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=31595&sku=EOSREBELT6KITIS"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27881&sku=DMCFZ200K"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30974&sku=SX720HSRED"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27464&sku=L32BLACK"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30973&sku=SX720HSBLACK"));
        visionsAttributesCount += getExtractedAttributesSize(new VisionsExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30395&sku=SX420IS"));

        double visionsRecall = visionsAttributesCount / ((VisionsExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * visionsPagesCount);
        double visionsFMeasure = 2*(visionsRecall)/(visionsRecall+1);
        System.out.println("Visions:\n Recall: " + visionsRecall + "\nF-measure" + visionsFMeasure );


        //SIGMAPHOTO
        int sigmaPhotoAttributesCount = 0;
        double sigmaPhotoPagesCount = 9.0;

        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/mirrorless/sd-quattro-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/mirrorless/sd-quattro-h-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/dslr/sd1-merrill-digital-slr-camera-0"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp0-quattro-compact-digital-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp1-quattro-compact-digital-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp2-merrill-compact-digital-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp3-merrill-compact-digital-camera-0"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp2-quattro-compact-digital-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new SigmaPhotoExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp3-quattro-compact-digital-camera"));

        double sigmaPhotoRecall = sigmaPhotoAttributesCount / ((SigmaPhotoExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * sigmaPhotoPagesCount);
        double sigmaPhotoFMeasure = 2*(sigmaPhotoRecall)/(sigmaPhotoRecall+1);
        System.out.println("Sigma Photo:\n Recall: " + sigmaPhotoRecall + "\nF-measure" + sigmaPhotoFMeasure );


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

        double ricohPhotoRecall = ricohAttributesCount / ((RicohExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * ricohPagesCount);
        double ricohPhotoFMeasure = 2*(ricohPhotoRecall)/(ricohPhotoRecall+1);

        System.out.println("Ricoh:\n Recall: " + ricohPhotoRecall + "\nF-measure" + ricohPhotoFMeasure );

        //DPPREVIEW
        int dpPreviewAttributesCount = 0;
        double dpPreviewPagesCount = 25.0;
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/canon/slrs/canon_eos77d/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/canon/slrs/canon_eosm6/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/canon/slrs/canon_eos800d/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/pentax/slrs/pentax_kp/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/fujifilm/compacts/fujifilm_x100f/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/fujifilm/slrs/fujifilm_xt20/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/fujifilm/compacts/fujifilm_xp120/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/canon/compacts/canon_g9xii/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/sony/compacts/sony_dschx350/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/fujifilm/slrs/fujifilm_xa10/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/nikon/slrs/nikon_d5600/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/leica/slrs/leica_tl/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/sony/slrs/sony_a6500/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/olympus/slrs/olympus_em1ii/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/olympus/slrs/oly_epl8/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/sony/compacts/sony_dscrx100m5/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/yi/slrs/yi_m1/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/sony/slrs/sony_slta99_ii/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/panasonic/compacts/panasonic_dmclx10/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/panasonic/slrs/panasonic_dmcg85/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/fujifilm/slrs/fujifilm_xa3/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/nikon/compacts/nikon_cpw100/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/hasselblad/slrs/hasselblad_x1d/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/fujifilm/slrs/fujifilm_xt2/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new DPPreviewExtractor(), new URL("https://www.dpreview.com/products/pentax/slrs/pentax_k70/specifications"));

        double dpPreviewPhotoRecall = dpPreviewAttributesCount / ((DPPreviewExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 1) * dpPreviewPagesCount);
        double dpPreviewPhotoFMeasure = 2*(dpPreviewPhotoRecall)/(dpPreviewPhotoRecall+1);
        System.out.println("DP Preview:\n Recall: " + dpPreviewPhotoRecall + "\nF-measure" + dpPreviewPhotoFMeasure );


        //NEWEGG
        int newEggAttributesCount = 0;
        double newEggPagesCount = 11.0;
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=N82E16830120835"));
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=N82E16830120844"));
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIAB2G5515538"));
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIAB925C69394"));
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIABMT5DF2426"));
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIABKX58W3053"));
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIABKX4GJ3904"));
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIAB2G4781189"));
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=N82E16830120819"));
        newEggAttributesCount += getExtractedAttributesSize(new NewEggExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=N82E16830120678"));

        double newEggPhotoRecall = newEggAttributesCount / ((NewEggExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * newEggPagesCount);
        double newEggPhotoFMeasure = 2*(newEggPhotoRecall)/(newEggPhotoRecall+1);
        System.out.println("New Egg:\n Recall: " + newEggPhotoRecall + "\nF-measure" + newEggPhotoFMeasure );

        //CURRYS
        int currysAttributesCount = 0;
        double currysPagesCount = 16.0;
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d3300-dslr-camera-with-18-55-mm-f-3-5-5-6-lens-black-10135135-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-80d-dslr-camera-black-body-only-10145630-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d7200-dslr-camera-black-body-only-10124382-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-1300d-dslr-camera-with-18-55-mm-f-3-5-5-6-lens-black-10144243-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d3400-dslr-camera-with-18-55-mm-f-3-5-5-6-lens-black-10152821-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d3300-dslr-camera-with-18-55-mm-f-3-5-5-6-vr-lens-black-21942035-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-1300d-dslr-camera-black-body-only-10144242-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-m10-mirrorless-camera-with-15-45-mm-f-3-5-6-3-lens-black-10139949-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/sony-a6000-mirrorless-camera-with-16-50-mm-f-3-5-5-6-lens-black-22095702-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/sony-a7-mirrorless-camera-with-28-70-mm-f-3-5-5-6-zoom-lens-21837723-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-m10-mirrorless-camera-with-15-45-mm-f-3-5-6-3-lens-white-10139950-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/panasonic-lumix-dmc-g7eb-k-mirrorless-camera-with-14-42-mm-f-3-5-5-6-lens-black-10132843-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/compact-and-bridge-cameras/canon-ixus-180-compact-camera-black-10142577-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/compact-and-bridge-cameras/sony-cyber-shot-dsc-wx350b-superzoom-compact-camera-black-22074691-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/compact-and-bridge-cameras/polaroid-ie826-compact-camera-grey-10126999-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new CurrysExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/compact-and-bridge-cameras/canon-powershot-g7-x-mark-ii-high-performance-compact-camera-black-10145306-pdt.html"));

        double currysPhotoRecall = currysAttributesCount / ((CurrysExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * currysPagesCount);
        double currysPhotoFMeasure = 2*(currysPhotoRecall)/(currysPhotoRecall+1);
        System.out.println("Currys:\n Recall:" + currysPhotoRecall + "\nF-measure: " + currysPhotoFMeasure );

        //WEXPHOTOGRAPHIC
        int wexPhotographicAttributesCount = 0;
        double wexPhotographicPagesCount = 6.0;
        wexPhotographicAttributesCount += getExtractedAttributesSize(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/nikon-d5-digital-slr-camera-body-dual-xqd-1589610/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/canon-eos-6d-digital-slr-camera-body-1532845/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/fuji-x-t2-digital-camera-body-1602117/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/sony-alpha-a7r-mark-ii-digital-camera-body-1575237/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/canon-eos-7d-mark-ii-digital-slr-camera-body-1560196/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new WexPhotoGraphicExtractor(), new URL("http://www.wexphotographic.com/canon-eos-5d-mark-iii-digital-slr-camera-body-1530010/"));

        double wexPhotographicRecall = wexPhotographicAttributesCount / ((WexPhotoGraphicExtractor.ATTRIBUTE_TYPE_ACTIONS.size() + 2) * wexPhotographicPagesCount);
        double wexPhotographicPhotoFMeasure = 2*(wexPhotographicRecall)/(wexPhotographicRecall+1);
        System.out.println("Wex Photographic:\n Recall:" + wexPhotographicRecall + "\nF-measure: " + wexPhotographicPhotoFMeasure );


    }

    static int getExtractedAttributesSize(CameraDomainExtractor extractor, URL url) throws IOException {
        return extractor.extractWebSiteContent(Jsoup.parse(url, 0)).size();
    }
}
