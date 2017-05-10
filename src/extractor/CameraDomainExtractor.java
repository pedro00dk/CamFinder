package extractor;

import org.jsoup.nodes.Document;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CameraDomainExtractor {

    default String domain() {
        return getClass().getSimpleName().replace("Extractor", "");
    }

    Map<String, String> extractWebSiteContent(Document document, URL link) throws MalformedURLException;

    static String formatMegapixel(String unformatted) {
        return unformatted.replaceAll("( \\.|\\. |:|[A-Za-z ])*", "") + " megapixels";
    }

    static String formatZoom(String unformatted) {
        String zooms = unformatted.replaceAll("( \\.|\\. |:|[A-Za-z ])*", "") + " ";
        return Stream.of(zooms.split(" "))
                .map(zoom -> zoom + "x")
                .collect(Collectors.joining(" - "));

    }

  /*  static String formatStorage(String unformatted){
        return unformatted.split(",")[0];
    }*/
}
