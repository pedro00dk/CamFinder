package controller;

import extractor.specific.CanonExtractor;
import javafx.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class SearchController {
    @RequestMapping(value = "/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("search");
        List<Pair<String, Boolean>> attributes = new ArrayList<>();

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
