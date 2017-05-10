package extractor;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {
        //testExtractor(new CanonExtractor(), new URL("https://shop.usa.canon.com/shop/en/catalog/powershot-sx540-hs"));

        //testExtractor(new SonyExtractor(), new URL("http://www.sony.com/electronics/interchangeable-lens-cameras/ilce-5100-body-kit/specifications"));
        //testExtractor(new SonyExtractor(), new URL("http://www.sony.com/electronics/cyber-shot-compact-cameras/dsc-wx500/specifications"));

        //testExtractor(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
         testExtractor(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d3400.html#tab-ProductDetail-ProductTabs-TechSpecs"));
       // testExtractor(new NikonExtractor(), new URL("http://www.nikonusa.com/en/nikon-products/product/dslr-cameras/d5500.html#tab-ProductDetail-ProductTabs-TechSpecs"));
    }


    static void testExtractor(CameraDomainExtractor extractor, URL url) throws IOException {
        System.out.println("Extracting page content from " + extractor.domain());
        Map<String, String> content = extractor.extractWebSiteContent(Jsoup.parse(url, 0), url);
        content.entrySet()
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));
    }
}















