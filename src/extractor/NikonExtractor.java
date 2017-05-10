package extractor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class NikonExtractor implements  CameraDomainExtractor {
    @Override
    public Map<String, String> extractWebSiteContent(Document document, URL link) throws MalformedURLException {
        //get camera name
        String name = document.select(".breadcrumb.current").text();
        // get price
        Elements price = document.select(".current-price");
        return null;
    }
}
