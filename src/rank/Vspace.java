package rank;


import javafx.util.Pair;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Vspace {
    static Map<URL, List<Double>> vectors = new HashMap<>();
    static Map<URL, Double> rank = new HashMap<>();
    List<Double> idfQuery = new ArrayList<>();

    /* Vector to queryUser */
    List<Double> vectorQuery(String words[], List<Integer> count, int documentCount) {
        for (int i = 0; i < words.length; i++) {
            idfQuery.add(i, Math.log10(documentCount/count.get(i)));
        }
        return idfQuery;
    }

    /*Calcular o espaço vetorial para cada documento*/
    Map<URL, List<Double>> buildVectorSpace(List<Pair<String, List<Pair<URL, Double>>>> tfidfLists) {
        List<Pair<URL, Double>> urlsTfidf;
        List<Double> valor = new ArrayList<>();

        for (int i = 0; i < tfidfLists.size(); i++) {
            urlsTfidf = tfidfLists.get(i).getValue();
            if(i!=0){
                vectors.put(urlsTfidf.get(i-1).getKey(), valor);
                valor.clear();
            }
            for (int j = 0; j < urlsTfidf.size(); j++) {
                valor.add(j, urlsTfidf.get(i).getValue());
            }
        }
        System.out.println(vectors);
        return vectors;
    }


    /*Similarity of Cosseno*/
    Map<URL, Double> rank (Map<URL, List <Double>> document, List<Double> query) {
        Map<URL, Double> rank = new HashMap<>();
        List<URL> urls = new ArrayList<>();
        List<Double> valores = new ArrayList<>();
        double denominador = 0;
        double numeradorD = 0;
        double numeradorQ = 0;
        double numerador = 0;
        double result = 0;

        urls.addAll(document.keySet());

        for (int i = 0; i < document.size() ; i++) {
            result = denominador/numerador;
            denominador=0;
            numeradorD=0;
            numeradorQ=0;
            numerador=0;
            result=0;
            for (int j = 0; j < query.size() ; j++) {

                denominador = denominador + document.get(urls.get(i)).get(j) * query.get(j);
                numeradorD = numeradorD + document.get(urls.get(i)).get(j) * document.get(urls.get(i)).get(j);
                numeradorQ = numeradorQ + query.get(j) * query.get(j);
                numerador = Math.sqrt(numeradorD*numeradorQ);
            }
            rank.put(urls.get(i), result);

        }

        return rank;
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
