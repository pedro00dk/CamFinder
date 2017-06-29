package controller;

import index.InvertedIndex;
import javafx.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import rank.Rank;

import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class SearchController {
    static final InvertedIndex INVERTED_INDEX;
    static final Map<URL, Map<String, String>> URL_ATTRIBUTES;
    static final Map<String, List<String>> RECOMENDATIONS;
    static final Map<String, List<String>> MI_RECOMENDATIONS;
    static final Rank RANK;

    static {
        InvertedIndex invertedIndex = null;
        Map<URL, Map<String, String>> urlAttributes = null;
        Rank rank = null;
        try {
            //CHANGE THE PATH TO YOUR LOCAL pages/extracted
            Path extractedContentPath = Paths.get("C:\\Users\\ghps\\IdeaProjects\\CamFinder\\pages\\extracted", "extracted.data");
            ObjectInputStream is = new ObjectInputStream(Files.newInputStream(extractedContentPath));

            //noinspection unchecked
            BlockingQueue<Pair<URL, Map<String, String>>> extractorOutput = (BlockingQueue<Pair<URL, Map<String, String>>>) is.readObject();
            invertedIndex = new InvertedIndex(extractorOutput, 5);
            urlAttributes = extractorOutput.stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue));
            rank = new Rank(invertedIndex);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            INVERTED_INDEX = invertedIndex;
            URL_ATTRIBUTES = urlAttributes;
            RANK = rank;
        }

        //OLD RECOMENDATION
        RECOMENDATIONS = INVERTED_INDEX != null ? INVERTED_INDEX.attributes.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        attribute -> INVERTED_INDEX.termDocuments.entrySet().stream()
                                .filter(entry -> entry.getKey().split("\\.")[0].equals(attribute))
                                .sorted((e1, e2) -> e2.getValue().size() - e1.getValue().size())
                                .limit(3)
                                .map(entry -> entry.getKey().split("\\.")[1])
                                .collect(Collectors.toList())
                        )
                ) : null;

        //MUTUAL INFORMATION CALCULATION
        Map<String, Double> attributeFrequencies = INVERTED_INDEX.attributes.stream()
                .collect(Collectors.toMap(Function.identity(),
                        attribute -> INVERTED_INDEX.termDocuments.entrySet().stream()
                                .filter(entry -> entry.getKey().split("\\.")[0].equals(attribute))
                                .mapToInt(entry -> entry.getValue().size())
                                .sum() / (double) INVERTED_INDEX.indexedDocuments.size()
                        )
                );

        Map<String, Double> termProbabilities = INVERTED_INDEX.termDocuments.entrySet().stream()
                .map(entry -> {
                    double termProbability = entry.getValue().size() / (double) INVERTED_INDEX.indexedDocuments.size();
                    String attribute = entry.getKey().split("\\.")[0];
                    double attributeProbability = attributeFrequencies.get(attribute);
                    return new Pair<>(entry.getKey(), termProbability / (termProbability * attributeProbability));
                })
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        MI_RECOMENDATIONS =  INVERTED_INDEX.attributes.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        attribute -> termProbabilities.entrySet().stream()
                                .filter(entry -> entry.getKey().split("\\.")[0].equals(attribute))
                                .sorted((e1, e2) -> (int) Math.signum(e2.getValue() - e1.getValue()))
                                .limit(3)
                                .map(entry -> entry.getKey().split("\\.")[1])
                                .collect(Collectors.toList())
                        )
                );


    }

    @RequestMapping(value = "/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("search");

        List<Pair<String, Boolean>> attributes = new ArrayList<>();
        attributes.add(new Pair<>("price", Boolean.TRUE));
        attributes.add(new Pair<>("name", Boolean.TRUE));
        attributes.add(new Pair<>("Megapixels", Boolean.FALSE));
        attributes.add(new Pair<>("Zoom", Boolean.FALSE));
        attributes.add(new Pair<>("Storage Mode", Boolean.TRUE));
        attributes.add(new Pair<>("Sensitivity", Boolean.TRUE));
        attributes.add(new Pair<>("Shutter Speed", Boolean.TRUE));
        attributes.add(new Pair<>("Sensor Size", Boolean.FALSE));

        mav.addObject("attributes", attributes);
        mav.addObject("suggestions", MI_RECOMENDATIONS);
        return mav;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView processFormRequest(@RequestParam("name") String name, @RequestParam("price") String price,
                                           @RequestParam("Megapixels") String megapixel, @RequestParam("Zoom") String zoom,
                                           @RequestParam("Storage Mode") String storageMode, @RequestParam("Sensitivity") String sensitivity,
                                           @RequestParam("Shutter Speed") String shutterSpeed, @RequestParam("Sensor Size") String sensor_size) {
        List query = new ArrayList();
        if (!name.isEmpty()) {
            Stream.of(name.split(" +")).forEach(part -> query.add("name." + part));
        }
        if (!price.isEmpty()) {
            Stream.of(price.split(" +")).forEach(part -> query.add("price." + part));
        }
        if (!shutterSpeed.isEmpty()) {
            Stream.of(shutterSpeed.split(" +")).forEach(part -> query.add("Shutter Speed." + part));
        }
        if (!storageMode.isEmpty()) {
            Stream.of(storageMode.split(" +")).forEach(part -> query.add("Storage Mode." + part));
        }
        if (!sensitivity.isEmpty()) {
            Stream.of(sensitivity.split(" +")).forEach(part -> query.add("Sensitivity." + part));
        }

        List<URL> rank = RANK.rank(query, false);
        List<Pair<String, Map<String, String>>> mappedUrls = (ArrayList) rank.stream().limit(25).map(url -> new Pair<>(url.toExternalForm(), URL_ATTRIBUTES.get(url))).collect(Collectors.toList());

        ModelAndView mav = new ModelAndView("result");
        mav.addObject("mappedUrls", mappedUrls);
        return mav;
    }
}
