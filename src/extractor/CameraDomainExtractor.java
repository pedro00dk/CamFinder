package extractor;

import org.jsoup.nodes.Document;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CameraDomainExtractor {

    Map<String, Function<String, String>> getAtrributeTypeActions();

    default String domain() {
        return getClass().getSimpleName().replace("Extractor", "");
    }

    Map<String, String> extractWebSiteContent(Document document);

    static String formatMegapixel(String unformatted) {
        return unformatted.replaceAll("( \\.|\\. |:|[A-Za-z ])*", "") + " megapixels";
    }

    static String formatZoom(String unformatted) {
        String zooms = unformatted.replaceAll("( \\.|\\. |:|[A-Za-z ])*", "") + " ";
        return Stream.of(zooms.split(" "))
                .map(zoom -> zoom + "x")
                .collect(Collectors.joining(" - "));

    }
}
