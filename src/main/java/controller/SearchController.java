package controller;

import extractor.specific.CanonExtractor;
import index.InvertedIndex;
import javafx.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class SearchController {
    static final InvertedIndex INVERTED_INDEX;
    static final Map<URL, Map<String, String>> URL_ATTRIBUTES;
    static final Map<String, List<String>> RECOMENDATIONS;

    static {
        InvertedIndex invertedIndex = null;
        Map<URL, Map<String, String>> urlAttributes = null;

        try {
            Path extractedContentPath = Paths.get("C:\\Users\\Guilherme\\IdeaProjects\\CamFinder\\pages\\extracted", "extracted.data");
            ObjectInputStream is = new ObjectInputStream(Files.newInputStream(extractedContentPath));

            //noinspection unchecked
            BlockingQueue<Pair<URL, Map<String, String>>> extractorOutput = (BlockingQueue<Pair<URL, Map<String, String>>>) is.readObject();
            invertedIndex = new InvertedIndex(extractorOutput, 5);
            urlAttributes = extractorOutput.stream().collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            INVERTED_INDEX = invertedIndex;
            URL_ATTRIBUTES = urlAttributes;
        }

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
    }

    @RequestMapping(value = "/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("search");
        List<Pair<String, Boolean>> attributes = new ArrayList<>();

//        System.out.println(RECOMENDATIONS.toString());
        attributes.add(new Pair<>("Name", Boolean.TRUE));
        attributes.add(new Pair<>("Price", Boolean.TRUE));
        attributes.add(new Pair<>("Megapixels", Boolean.TRUE));
        attributes.add(new Pair<>("Zoom", Boolean.FALSE));
        attributes.add(new Pair<>("Storage Mode", Boolean.FALSE));
        attributes.add(new Pair<>("Sensitivity", Boolean.TRUE));
        attributes.add(new Pair<>("Shutter Speed", Boolean.TRUE));
        attributes.add(new Pair<>("Sensor Size", Boolean.FALSE));

        mav.addObject("attributes", attributes);
        return mav;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView processFormRequest(@RequestParam("Name") String name, @RequestParam("Price") String price,
                                           @RequestParam("Megapixels") String megapixel, @RequestParam("Zoom") String zoom,
                                           @RequestParam("Storage Mode") String storage_mode, @RequestParam("Sensitivity") String sensitivity,
                                           @RequestParam("Shutter Speed") String shutter_speed, @RequestParam("Sensor Size") String sensor_size) {


        CanonExtractor c = new CanonExtractor();
        //Here goes the calls for Bruno's methods
        ModelAndView mav = new ModelAndView("result");
        mav.addObject("name", name);
        mav.addObject("price", price);
        mav.addObject("megapixel", megapixel);
        mav.addObject("zoom", zoom);
        mav.addObject("storage_mode", storage_mode);
        mav.addObject("sensitivity", sensitivity);
        mav.addObject("shutter_speed", shutter_speed);
        mav.addObject("sensor_size", sensor_size);
        //return list of objects here and change template
        return mav;
    }
}
