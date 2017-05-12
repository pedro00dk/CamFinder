package extractor;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GeneralExtractor implements CameraDomainExtractor {

    @Override
    public Map<String, String> extractWebSiteContent(Document document, URL link) {

        //CURRYS, VISIONS, SIGMAPHOTO, RICOH, DPPREVIEW
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

        return null;
    }

    /*
          Implementar uma busca em largura e adicionar os nós que possuem dois filhos.
          Isso pega alguma informação errada, mas certamente pega os atributos em uma página de camera que contém tabelas.
          Então filtrar pelo mapa.

       */
    public List<Element> bsf(Document document){

        return null;
    }

}
