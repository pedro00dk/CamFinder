package extractor.evaluators;

import extractor.CameraDomainExtractor;
import extractor.GeneralExtractor;
import extractor.specific.*;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class EvaluateGeneralExtractor {
    public static void main(String[] args) throws IOException {
        System.out.println("General Evaluate in Progress... ");
        //CANON
        int canonAttributesCount = 0;
        double canonPagesCount = 16.0;
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx540-hs"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx420-is-red"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx700-hs-black"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx610-hs-red"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-elph-180-silver"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-elph-190-is-black"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-ef-s-18-135mm-f-3-5-5-6-is-usm-kit-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-body-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-rebel-t5-red-ef-s-18-55mm-is-ii-lens-kit-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-80d-ef-s-18-55mm-f-3-5-5-6-is-stm-kit-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-rebel-t6-ef-s-18-55mm-f-35-56-is-ii-kit-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-m3-body-refurbished-camera"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-m10-ef-m-15-45mm-f-3-5-6-3-is-stm-kit-white-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-rebel-sl1-ef-s-18-55mm-is-stm-kit-white-ef-75-300mm-f-4-56-iii-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-5ds-r-body-refurbished"));
        canonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/eos-5ds-body-refurbished"));

        printMetrics(canonAttributesCount, canonPagesCount, "Canon");

        //SONY
        int sonyAttributesCount = 0;
        double sonyPagesCount = 12.0;
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-5100-body-kit/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-6300-body-kit/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-6500-body-kit/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilca-99m2/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-7rm2/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-7m2-body-kit/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx100m5/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx10m3/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx1rm2/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-hx90v/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-wx500/specifications"));
        sonyAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-rx10m2/specifications"));

        printMetrics(sonyAttributesCount, sonyPagesCount, "Sony");

        // NIKON
        int nikonAttributesCount = 0;
        double nikonPagesCount = 14.0;

        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d3400.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d7200.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d610.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d750.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d810.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/nikon-df.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d810.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/nikon1/nikon-1-v3.html#tab-ProductDetail-ProductTabs-TechSpecs"));
        nikonAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/nikon1/nikon-1-j5.html#tab-ProductDetail-ProductTabs-TechSpecs"));

        printMetrics(nikonAttributesCount, nikonPagesCount, "Nikon");

        //VISIONS
        int visionsAttributesCount = 0;
        double visionsPagesCount = 13.0;

        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=686&productId=28317&sku=P530BREFURB"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=21085&sku=DMCTS5A"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=21537&sku=EOSREBELT5I"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=31819&sku=EOSREBELT6KITDC"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27978&sku=DMCGF7KS"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27989&sku=EOSREBELT6I"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27458&sku=S3700BLACK"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=31595&sku=EOSREBELT6KITIS"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27881&sku=DMCFZ200K"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30974&sku=SX720HSRED"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=27464&sku=L32BLACK"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30973&sku=SX720HSBLACK"));
        visionsAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.visions.ca/catalogue/category/Details.aspx?categoryId=223&productId=30395&sku=SX420IS"));

        printMetrics(visionsAttributesCount, visionsPagesCount, "Visions");

        //SIGMAPHOTO
        int sigmaPhotoAttributesCount = 0;
        double sigmaPhotoPagesCount = 9.0;

        sigmaPhotoAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/mirrorless/sd-quattro-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/mirrorless/sd-quattro-h-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/dslr/sd1-merrill-digital-slr-camera-0"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp0-quattro-compact-digital-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp1-quattro-compact-digital-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp2-merrill-compact-digital-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp3-merrill-compact-digital-camera-0"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp2-quattro-compact-digital-camera"));
        sigmaPhotoAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.sigmaphoto.com/cameras/compact/dp3-quattro-compact-digital-camera"));

        printMetrics(sigmaPhotoAttributesCount, sigmaPhotoPagesCount, "Sigma Photo");

        //RICOH
        int ricohAttributesCount = 0;
        double ricohPagesCount = 11.0;

        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-645z"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-1"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-3-ii"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-70"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-s2"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/theta-s"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/wg-m2"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/gr-2"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/theta-sc"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-k-50"));
        ricohAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://us.ricoh-imaging.com/index.php/cameras/pentax-kp"));

        printMetrics(ricohAttributesCount, ricohPagesCount, "Ricoh");

        //DPPREVIEW
        int dpPreviewAttributesCount = 0;
        double dpPreviewPagesCount = 25.0;
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/canon/slrs/canon_eos77d/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/canon/slrs/canon_eosm6/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/canon/slrs/canon_eos800d/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/pentax/slrs/pentax_kp/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/fujifilm/compacts/fujifilm_x100f/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/fujifilm/slrs/fujifilm_xt20/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/fujifilm/compacts/fujifilm_xp120/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/canon/compacts/canon_g9xii/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/sony/compacts/sony_dschx350/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/fujifilm/slrs/fujifilm_xa10/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/nikon/slrs/nikon_d5600/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/leica/slrs/leica_tl/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/sony/slrs/sony_a6500/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/olympus/slrs/olympus_em1ii/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/olympus/slrs/oly_epl8/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/sony/compacts/sony_dscrx100m5/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/yi/slrs/yi_m1/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/sony/slrs/sony_slta99_ii/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/panasonic/compacts/panasonic_dmclx10/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/panasonic/slrs/panasonic_dmcg85/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/fujifilm/slrs/fujifilm_xa3/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/nikon/compacts/nikon_cpw100/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/hasselblad/slrs/hasselblad_x1d/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/fujifilm/slrs/fujifilm_xt2/specifications"));
        dpPreviewAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.dpreview.com/products/pentax/slrs/pentax_k70/specifications"));

        printMetrics(dpPreviewAttributesCount, dpPreviewPagesCount, "DP Preview");

        //NEWEGG
        int newEggAttributesCount = 0;
        double newEggPagesCount = 11.0;
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=N82E16830120835"));
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=N82E16830120844"));
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIAB2G5515538"));
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIAB925C69394"));
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIABMT5DF2426"));
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIABKX58W3053"));
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIABKX4GJ3904"));
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=9SIAB2G4781189"));
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=N82E16830120819"));
        newEggAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("https://www.newegg.com/Product/Product.aspx?Item=N82E16830120678"));

        printMetrics(newEggAttributesCount, newEggPagesCount, "New Egg");

        //CURRYS
        int currysAttributesCount = 0;
        double currysPagesCount = 16.0;
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d3300-dslr-camera-with-18-55-mm-f-3-5-5-6-lens-black-10135135-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-80d-dslr-camera-black-body-only-10145630-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d7200-dslr-camera-black-body-only-10124382-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-1300d-dslr-camera-with-18-55-mm-f-3-5-5-6-lens-black-10144243-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d3400-dslr-camera-with-18-55-mm-f-3-5-5-6-lens-black-10152821-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/nikon-d3300-dslr-camera-with-18-55-mm-f-3-5-5-6-vr-lens-black-21942035-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-1300d-dslr-camera-black-body-only-10144242-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-m10-mirrorless-camera-with-15-45-mm-f-3-5-6-3-lens-black-10139949-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/sony-a6000-mirrorless-camera-with-16-50-mm-f-3-5-5-6-lens-black-22095702-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/sony-a7-mirrorless-camera-with-28-70-mm-f-3-5-5-6-zoom-lens-21837723-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/canon-eos-m10-mirrorless-camera-with-15-45-mm-f-3-5-6-3-lens-white-10139950-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/dslr-and-compact-system-cameras/panasonic-lumix-dmc-g7eb-k-mirrorless-camera-with-14-42-mm-f-3-5-5-6-lens-black-10132843-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/compact-and-bridge-cameras/canon-ixus-180-compact-camera-black-10142577-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/compact-and-bridge-cameras/sony-cyber-shot-dsc-wx350b-superzoom-compact-camera-black-22074691-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/compact-and-bridge-cameras/polaroid-ie826-compact-camera-grey-10126999-pdt.html"));
        currysAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.currys.co.uk/gbuk/cameras-and-camcorders/digital-cameras/compact-and-bridge-cameras/canon-powershot-g7-x-mark-ii-high-performance-compact-camera-black-10145306-pdt.html"));

        printMetrics(currysAttributesCount, currysPagesCount, "Currys");

        //WEXPHOTOGRAPHIC
        int wexPhotographicAttributesCount = 0;
        double wexPhotographicPagesCount = 6.0;
        wexPhotographicAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.wexphotographic.com/nikon-d5-digital-slr-camera-body-dual-xqd-1589610/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.wexphotographic.com/canon-eos-6d-digital-slr-camera-body-1532845/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.wexphotographic.com/fuji-x-t2-digital-camera-body-1602117/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.wexphotographic.com/sony-alpha-a7r-mark-ii-digital-camera-body-1575237/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.wexphotographic.com/canon-eos-7d-mark-ii-digital-slr-camera-body-1560196/"));
        wexPhotographicAttributesCount += getExtractedAttributesSize(new GeneralExtractor(), new URL("http://www.wexphotographic.com/canon-eos-5d-mark-iii-digital-slr-camera-body-1530010/"));

        printMetrics(wexPhotographicAttributesCount, wexPhotographicPagesCount, "Wex Photographic");
    }

    static int getExtractedAttributesSize(CameraDomainExtractor extractor, URL url) throws IOException {
        return extractor.extractWebSiteContent(Jsoup.parse(url, 0)).size();
    }

    static void printMetrics(int attributesCount, double pagesCount, String extractorName) {
        double recall = attributesCount / ((8) * pagesCount);
        double fMeasure = 2 * (recall) / (recall + 1);
        System.out.println(extractorName + ":\nRecall:" + recall + "\nF-measure: " + fMeasure);
    }
}
