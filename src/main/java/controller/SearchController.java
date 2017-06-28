package controller;

import extractor.specific.CanonExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class SearchController {
    @RequestMapping(value = "/")
    public ModelAndView home() {
        ModelAndView mav = new ModelAndView("search");
        mav.addObject("attributes", Stream.of("Name", "Price", "Megapixels", "Zoom").collect(Collectors.toList()));
        return mav;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ModelAndView processFormRequest(@RequestParam("name") String name, @RequestParam("price") String price,
                                           @RequestParam("megapixel") String megapixel, @RequestParam("zoom") String zoom,
                                           @RequestParam("storage_mode") String storage_mode, @RequestParam("sensitivity") String sensitivity,
                                           @RequestParam("shutter_speed") String shutter_speed, @RequestParam("sensor_size") String sensor_size) {
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
