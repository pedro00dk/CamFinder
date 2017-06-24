package rank;


import javafx.util.Pair;

import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Vspace {

    private Map<String, Pair<Integer, List<Pair<URL, Integer>>>> indice;
    private List<String> terms;
    static List<Pair<String, List<Pair<URL, Double>>>> tfidfLists = new ArrayList<>();
    static Map<URL, List<Double>> vectors = new HashMap<>();
    static Map<URL, Double> rank = new HashMap<>();
    private int documentCount;
    private String queryG;
    Double[] idfQuery = new Double[documentCount];



    /*
        Calculo o tfidf para cada termo do meu conjunto de termos
    */
    void calculateTfIdf() {
        for (String query : terms) {
            tfidfLists.add(getTfidfList(query));
        }
    }

    void vectorQuery() {
        String[] words = queryG.split(" ");
        for (int i = 0; i < words.length; i++) {
            Pair<Integer, List<Pair<URL, Integer>>> count = indice.get(words[i]);
            idfQuery[i] = 1 + Math.log10(documentCount + count.getKey());
        }
    }

    /*
        Calcular o espaço vetorial para cada documento
     */

    void vectorSpace(URL url) {
        List<Pair<URL, Double>> urlsTfidf;
        List<Double> valor[] = null;
        URL urlD = null;

        for (int i = 0; i < tfidfLists.size(); i++) {
            urlsTfidf = tfidfLists.get(i).getValue();
            for (int j = 0; j < urlsTfidf.size(); j++) {
                valor[i].add(urlsTfidf.get(i).getValue());
                vectors.put(urlsTfidf.get(i).getKey(), valor[i]);
                valor[i].clear();
            }
        }

    }

    /*
        Faço meu cálculo de tfIdf para o determinado termo da minha base de documentos
        ex.: zoom.10 --> 5, 6,7,8....
     */
    Pair<String, List<Pair<URL, Double>>> getTfidfList(String query) {
        Pair<Integer, List<Pair<URL, Integer>>> queryPages = indice.get(query);
        if (queryPages == null) {
            return null;
        }
        List<Pair<URL, Double>> tfidfList = new ArrayList<>();
        for (Pair<URL, Integer> pageInfo : queryPages.getValue()) {
            double tf = 1 + Math.log10(pageInfo.getValue());
            double idf = Math.log10(documentCount / queryPages.getKey());
            double tfidf = tf * idf;
            tfidfList.add(new Pair<>(pageInfo.getKey(), tfidf));
        }
        return new Pair<>(query, tfidfList);
    }

    void similar() {
        double denominador = 0;
        double numeradorD = 0;
        double numeradorQ = 0;
        double numerador = 0;
        List<URL> urls= new ArrayList<>();

        for (URL getKey:vectors.keySet()) {
         urls.add(getKey);
        }

        for (int i = 0; i < vectors.size(); i++) {
            for (int j = 0; j <idfQuery.length ; j++) {
                denominador = denominador + vectors.get(i).get(j) * idfQuery[j];
                numeradorD = numeradorD + vectors.get(i).get(j) * vectors.get(i).get(j);
                numeradorQ = numeradorQ + idfQuery[j] * idfQuery[j];
                numerador = Math.sqrt((numeradorD*numeradorQ));
                rank.put(urls.get(i), denominador/numerador);
            }
        }

    }

    public static void main(String[] args) throws MalformedURLException {
        URL testeURL1 = new URL("http://www.1d.com");
        URL testeURL2 = new URL("http://www.2d.com");
        List<Double> tested = new ArrayList<>();
        Map<URL, List<Double>> vectors2 = new HashMap<>();
        String query1 = "teste";
        String query2 = "teste2";
        List<Pair<URL, Double>> x = new ArrayList<>();
        x.add(new Pair<>(testeURL1, 2.0));
        tested.add(2.0);
        tested.add(1.2);

        List<Pair<URL, Double>> tfidfList = new ArrayList<>();
        tfidfLists.add(new Pair<>(query1, x));
        tfidfLists.add(new Pair<>(query2, x));


//        for (int i = 0; i <tfidfLists.size() ; i++) {
//            System.out.println(tfidfLists.size());
//            System.out.println(tfidfLists.get(i).getValue().get(0).getKey());
//        }

        List<Double> tested2 = new ArrayList<>();
        List<Double> all = new ArrayList<>();
        tested2.add(5.0);
        tested2.add(1.0);
        vectors2.put(testeURL1, tested);
        vectors2.put(testeURL2, tested);
        //System.out.println(vectors2);
        tested.add(3.5);
        vectors2.put(testeURL1, tested);
        tested.clear();
        tested.add(1.0);
        Set<URL> getKey = vectors2.keySet();
        for (URL url:getKey) {
            System.out.println(url);
        }



    }
}
