package extractor;

import extractor.specific.*;
import javafx.util.Pair;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GeneralExtractor implements CameraDomainExtractor {
    public static final Map<String, String> GERALATTRIBUTES;

    public static final Map<String, Function<String, String>> ATTRIBUTE_TYPE_ACTIONS;

    static {
        GERALATTRIBUTES = new HashMap<>();
        GERALATTRIBUTES.putAll(CanonExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(CurrysExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(DPPreviewExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(NewEggExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(NikonExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(RicohExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(SigmaPhotoExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(SonyExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(VisionsExtractor.MAPPED_ATTRIBUTE_NAMES);
        GERALATTRIBUTES.putAll(WebPhotoGraphicExtractor.MAPPED_ATTRIBUTE_NAMES);

        ATTRIBUTE_TYPE_ACTIONS = new HashMap<>();
        ATTRIBUTE_TYPE_ACTIONS.putAll(CurrysExtractor.ATTRIBUTE_TYPE_ACTIONS);
        ATTRIBUTE_TYPE_ACTIONS.put("price",Function.identity());

    }
    /*
    static {
        Map<String, String> mappedAttributeNames = new HashMap<>();
        //megapixel formats
        mappedAttributeNames.put("Resolution", "Megapixels");
        mappedAttributeNames.put("Number of Pixels", "Megapixels");
        mappedAttributeNames.put("Number of Pixels (total)", "Megapixels");
        mappedAttributeNames.put("# of Megapixels", "Megapixels");
        mappedAttributeNames.put("Effective pixels", "Megapixels");
        mappedAttributeNames.put("Effective Pixels", "Megapixels");
        mappedAttributeNames.put("Total Pixels", "Megapixels");
        mappedAttributeNames.put("Megapixels", "Megapixels");

        //


       /* mappedAttributeNames.put("Digital Zoom", "Zoom");
        mappedAttributeNames.put("Recording Media", "Storage Mode");
        mappedAttributeNames.put("Storage Media", "Storage Mode");
        mappedAttributeNames.put("Sensitivity", "Sensitivity");
        mappedAttributeNames.put("Shutter Speed", "Shutter Speed");
        mappedAttributeNames.put("Shutter Speeds", "Shutter Speed");
        MAPPED_ATTRIBUTE_NAMES = Collections.unmodifiableMap(mappedAttributeNames);

        Map<String, Function<String, String>> attributeTypeActions = new HashMap<>();
        attributeTypeActions.put("Megapixels", CameraDomainExtractor::formatMegapixel);
        /*attributeTypeActions.put("Zoom", CameraDomainExtractor::formatZoom);
        attributeTypeActions.put("Storage Mode", Function.identity());
        attributeTypeActions.put("Sensitivity", Function.identity());
        attributeTypeActions.put("Shutter Speed", Function.identity());

        ATTRIBUTE_TYPE_ACTIONS = Collections.unmodifiableMap(attributeTypeActions);
    }
*/
    @Override
    public Map<String, String> extractWebSiteContent(Document document, URL link) {

       /* //CURRYS, VISIONS, SIGMAPHOTO, RICOH, DPPREVIEW,  WebPhotoGraphic
        List<Element> e2 = document.getElementsByTag("tr").stream()
                .filter(ch -> ch.children().size() == 2).collect(Collectors.toList());

        //NEWEGG, SONY
        List<Element> e1 = document.getElementsByTag("dl").stream()
                .filter(ch -> ch.children().size() == 2).collect(Collectors.toList());

        //NIKON
        List<Element> e3 = document.getElementsByTag("li").stream()
                .filter(ch -> ch.children().size() == 2).collect(Collectors.toList());
        //CANON
        List<Element> e4 = document.getElementsByTag("p").stream()
                .filter(ch -> ch.children().size() == 2).collect(Collectors.toList());


        /*List<Element> e5 = document.children().stream()
                .filter(ch -> ch.children().size() == 2).collect(Collectors.toList());*/

        List<Element> data = search(document);

        Map<String, String> attributes = IntStream.range(0, data.size())
                .filter(index -> GERALATTRIBUTES.containsKey(data.get(index).child(0).text()))
                .mapToObj(index -> new Pair<>(data.get(index).child(0).text(), data.get(index).child(1).text()))
                .collect(Collectors.toMap(pair -> GERALATTRIBUTES.get(pair.getKey()), Pair::getValue, (v1, v2) -> v1));

        //processing values
        attributes.entrySet()
                .forEach(entry -> entry.setValue(ATTRIBUTE_TYPE_ACTIONS.get(entry.getKey()).apply(entry.getValue())));

        return attributes;

    }

    /*
          Implementar uma busca em largura e adicionar os nós que possuem dois filhos.
          Isso pega alguma informação errada, mas certamente pega os atributos em uma página de camera que contém tabelas.
          Então filtrar pelo mapa.

       */

    private List<Element> search(Document root) {
        Queue<Element> queue = new LinkedList<>();
        List<Element> selected = new ArrayList<>();

        queue.add(root);
        Element current;
        while ((current = queue.poll()) != null) {
            queue.addAll(current.children());
            /*if (current.children().size() == 2) {
                selected.add(current);
            }*/

            if (current.children().size() >= 2) {
                if (GERALATTRIBUTES.containsKey(current.child(0).text())) {
                    selected.add(current);

                }
            }
        }

        return selected;
    }

}
