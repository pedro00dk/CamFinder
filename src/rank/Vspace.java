package rank;


import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Vspace {
    private List<String> querys;
    private Map<String, Pair<Integer, List<Pair<URL, Integer>>>> indice;
    private int documentCount;
    List<Pair<String, List<Pair<URL, Double>>>> tfidfLists = new ArrayList<>();


    /*
        Calculo o tfidf para cada termo do meu conjunto de termos

    */
    void calculateTfIdf(){
        for (String query:querys) {
            tfidfLists.add(getTfidfList(query));
        }
    }

    /*
        Calcular o espaço vetorial para cada documento
     */

    void vectorSpace(URL url){
        List<Pair<URL, List<Double>>> documents = new ArrayList<>();
        for (int i = 0; i < documentCount ; i++) {
            for (int j = 0; j < tfidfLists.size(); j++) {
                documents.add(new Pair<URL, List<Double>>(url, null));
            }
        }
    }

    /*
        Faço meu cálculo de tfIdf para o determinado termo da minha base de documentos
        ex.: zoom.10 --> 5, 6,7,8....
     */
    Pair<String, List<Pair<URL, Double>>> getTfidfList(String query){
        Pair<Integer, List<Pair<URL, Integer>>> queryPages = indice.get(query);
        if(queryPages == null){
            return null;
        }
        List<Pair<URL, Double>> tfidfList = new ArrayList<>();
        for (Pair<URL, Integer> pageInfo : queryPages.getValue()) {
            double tf = 1+Math.log10(pageInfo.getValue());
            double idf = Math.log10(documentCount/queryPages.getKey());
            double tfidf = tf*idf;
            tfidfList.add(new Pair<>(pageInfo.getKey(), tfidf));
        }
        return new Pair<>(query, tfidfList);
    }



    double similar(double document[], double query[]) {
        double denominador = 0;
        double numeradorD = 0;
        double numeradorQ = 0;
        double numerador = 0;

        for (int i = 0; i < 10; i++) {
            denominador = denominador + document[i] * query[i];
            numeradorD = numeradorD + document[i] * document[i];
            numeradorQ = numeradorQ + query[i] * query[i];
            numerador = Math.sqrt(numeradorD * numeradorQ);
        }

        return denominador / numerador;
    }



}
