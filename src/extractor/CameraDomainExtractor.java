package extractor;

import extractor.attribute.Attribute;
import extractor.attribute.MultiAttribute;
import extractor.attribute.NumericAttribute;
import extractor.attribute.StringAttribute;
import javafx.util.Pair;
import org.jsoup.nodes.Document;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface CameraDomainExtractor {

    default String domain() {
        return getClass().getSimpleName().replace("Extractor", "");
    }

    Map<String, String> extractWebSiteContent(Document document);

    default Map<String, Attribute> extractProcessedWebSiteContent(Document document) {
        Map<String, String> extracted = extractWebSiteContent(document);

        //processing values
        return extracted.entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), format(entry.getKey(), entry.getValue())))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    static Attribute format(String name, String value) {
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
        return new StringAttribute(value);
    }

    static Attribute formatMegapixel(String unformatted) {
        return new NumericAttribute(Double.parseDouble(unformatted.replaceAll("([^\\d. ]| \\.|\\. )+", "").trim().split(" +")[0]));
    }

    static Attribute formatZoom(String unformatted) {
        return new NumericAttribute(Double.parseDouble(unformatted.replaceAll("([^\\d. ]| \\.|\\. )+", "").trim().split(" +")[0]));
    }

    static Attribute formatStorage(String unformatted) {
        unformatted = unformatted.toUpperCase();
        return new MultiAttribute(
                Stream.of("SD", "SDHC", "SDXC", "EYE-FI", "UHS-I", "FLU", "PRO DUO", "PRO-HG DUO", "XC-HG DUO", "XQD", "COMPACTFLASH", "COMPACT FLASH")
                        .filter(unformatted::contains)
                        .collect(Collectors.toList())
        );
    }

    static Attribute formatSensitivity(String unformatted) {
        String[] values = unformatted.replaceAll("[.,]", "").replaceAll("[^\\d]+", " ").trim().split(" +");
        return new StringAttribute(values.length == 0 ? "undefined" : values.length == 1 ? values[0] + "-undefined" : values[0] + "-" + values[1]);
    }

    static Attribute formatShutterSpeed(String unformatted) {
        return new StringAttribute(unformatted);
    }

    static Attribute formatSensorSize(String unformatted) {
        return new StringAttribute(unformatted);
    }

    static NumericAttribute formatPrice(String unformatted) {
        return new NumericAttribute(Double.parseDouble(unformatted.replaceAll(",", "").replaceAll("([^\\d. ]| \\.|\\. )+", " ").trim().split(" +")[0]));
    }
}
