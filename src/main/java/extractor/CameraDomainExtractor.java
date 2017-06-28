package extractor;

import extractor.attribute.Attribute;
import extractor.attribute.MultiAttribute;
import extractor.attribute.NumericAttribute;
import extractor.attribute.StringAttribute;
import javafx.util.Pair;
import org.jsoup.nodes.Document;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface CameraDomainExtractor {

    default String domain() {
        return getClass().getSimpleName().replace("Extractor", "");
    }

    Map<String, String> extractWebSiteContent(Document document);

    default Map<String, String> extractProcessedWebSiteContent(Document document) {
        Map<String, String> extracted = extractWebSiteContent(document);

        //processing values
        return extracted.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), format(entry.getKey(), entry.getValue())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    static String format(String name, String value) {
        switch (name) {
            case "Megapixels":
                return formatMegapixel(value);
            case "Zoom":
                return formatZoom(value);
            case "Storage Mode":
                return formatStorage(value);
            case "Sensitivity":
                return formatSensitivity(value);
            case "Shutter Speed":
                return formatShutterSpeed(value);
            case "Sensor Size":
                return formatSensorSize(value);
            case "price":
                return formatPrice(value);
        }
        return value;
    }

    static String formatMegapixel(String unformatted) {
        double value = Double.parseDouble(unformatted.replaceAll("([^\\d. ]| \\.|\\. )+", "").trim().split(" +")[0]);
        int multiple = 4;
        double rest = value % multiple;
        int min = (int) (value - rest);
        int max = min + multiple;
        return Double.toString(min) + "-" + Double.toString(max);
    }

    static String formatZoom(String unformatted) {
        double value = Double.parseDouble(unformatted.replaceAll("([^\\d. ]| \\.|\\. )+", "").trim().split(" +")[0]);
        int multiple = 4;
        double rest = value % multiple;
        int min = (int) (value - rest);
        int max = min + multiple;
        return Double.toString(min) + "-" + Double.toString(max);
    }

    static String formatStorage(String unformatted) {
        return unformatted;
    }

    static String formatSensitivity(String unformatted) {
        return unformatted;
    }

    static String formatShutterSpeed(String unformatted) {
        return unformatted;
    }

    static String formatSensorSize(String unformatted) {
        return unformatted;
    }

    static String formatPrice(String unformatted) {
        double value = Double.parseDouble(unformatted.replaceAll(",", "").replaceAll("([^\\d. ]| \\.|\\. )+", " ").trim().split(" +")[0]);
        int multiple = 250;
        double rest = value % multiple;
        int min = (int) (value - rest);
        int max = min + multiple;
        return Double.toString(min) + "-" + Double.toString(max);
    }
}
