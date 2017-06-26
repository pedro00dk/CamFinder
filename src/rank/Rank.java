package rank;

import javafx.util.Pair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rank {

    private Map<String, Pair<Integer, List<Pair<URL, Integer>>>> indice;
    private static List<Pair<String, List<Pair<URL, Double>>>> tfidfLists = new ArrayList<>();
    private static List<Pair<String, List<Pair<URL, Double>>>> tfLists = new ArrayList<>();
    private static Map<URL, Double> rank;
    private static Map<URL, Double> rank2;
    private Vspace vectors = new Vspace();
    private int documentCount;
    private String queryG;
    private List<Double> idfQuery = new ArrayList<>();



    public Rank(Map<String, Pair<Integer, List<Pair<URL, Integer>>>> indice, String queryG, boolean tfidf, int documentCount){
        this.queryG = queryG;
        this.indice = indice;
        this.documentCount = documentCount;

        if (tfidf){
            calculateTfIdf();
            rankVector();
        }

        calculateTf();
        simpleVector();

    }

    /*Calculo o tfidf para cada termo do meu conjunto de termos*/
    private void calculateTfIdf() {
        //Pega todas as querys da consultas, salva numa lista e calcula o tfIDF dela relacionado a todos os documentos
        for(String query : indice.keySet()){
            tfidfLists.add(getTfidfList(query));
        }
    }

    /*Faço meu cálculo de tfIdf para o determinado termo da minha base de documentos
    ex.: zoom.10 --> 5, 6,7,8....*/
    private Pair<String, List<Pair<URL, Double>>> getTfidfList(String query) {
        Pair<Integer, List<Pair<URL, Integer>>> queryPages = indice.get(query);
        if (queryPages == null) {
            return null;
        }
        List<Pair<URL, Double>> tfidfList = new ArrayList<>();
        for (Pair<URL, Integer> pageInfo : queryPages.getValue()) {
            double tf = 1 + Math.log10((double)pageInfo.getValue());
            System.out.println(pageInfo.getValue());
            System.out.println(tf);
            double idf = Math.log10((double)documentCount / (double)queryPages.getKey());
            double tfidf = tf * idf;
            tfidfList.add(new Pair<>(pageInfo.getKey(), tfidf));
        }
        return new Pair<>(query, tfidfList);
    }

    /*Vector with TfIdf */
    private void rankVector(){
        String words[] = queryG.split(" ");;
        List<Integer> wordsContent = new ArrayList<>();

        for (int i = 0; i < words.length ; i++) {
            wordsContent.add(i, indice.get(words[i]).getKey());
        }

        Map<URL, List<Double>> TfIdfVector = new HashMap<>();
        TfIdfVector = vectors.buildVectorSpace(tfidfLists);
        idfQuery = vectors.vectorQuery(words, wordsContent, documentCount);

        rank = vectors.rank(TfIdfVector, idfQuery);
        System.out.println(rank.values());
    }

    /*Calculo o tf para cada termo do meu conjunto de termos*/
    private void calculateTf() {
        //Pega todas as querys da consultas, salva numa lista e calcula o tfIDF dela relacionado a todos os documentos
        for(String query : indice.keySet()){
            tfLists.add(getTfList(query));
        }
    }


    /*Vector without TfIdf*/
    private void simpleVector(){
        String words[] = queryG.split(" ");;
        List<Integer> wordsContent = new ArrayList<>();

        for (int i = 0; i < words.length ; i++) {
            wordsContent.add(i, indice.get(words[i]).getKey());
        }

        idfQuery = vectors.vectorQuery(words, wordsContent, documentCount);
        Map<URL, List<Double>> simpleVector = new HashMap<>();
        simpleVector = vectors.buildVectorSpace(tfLists);

        rank2 = vectors.rank(simpleVector, idfQuery);
    }

    /*Faço meu cálculo APENAS de TF para o determinado termo da minha base de documentos
    ex.: zoom.10 --> 5, 6,7,8....*/
    private Pair<String, List<Pair<URL, Double>>> getTfList(String query){
        Pair<Integer, List<Pair<URL, Integer>>> queryPages = indice.get(query);
        if (queryPages == null) {
            return null;
        }
        List<Pair<URL, Double>> tfList = new ArrayList<>();
        for (Pair<URL, Integer> pageInfo : queryPages.getValue()) {
            double tf = 1 + Math.log10(pageInfo.getValue());
            tfList.add(new Pair<>(pageInfo.getKey(), tf));
        }
        return new Pair<>(query, tfList);

    }

    public static void main(String[] args) throws MalformedURLException {

        URL testeURL1 = new URL("http://www.1d.com");
        URL testeURL2 = new URL("http://www.2d.com");
        String zoom = "zoom10";
        String valor = "valor20";

        String queryG = "zoom10 valor20";

        Map<String, Pair<Integer, List<Pair<URL, Integer>>>> indice = new HashMap<>();
        List<Pair<URL, Integer>> documentsF = new ArrayList<>();
        Pair<Integer, List<Pair<URL, Integer>>> teste2;

         documentsF.add(new Pair<URL, Integer>(testeURL1, 1));
         documentsF.add(new Pair<URL, Integer>(testeURL1, 2));

         documentsF.add(new Pair<>(testeURL2, 2));
         documentsF.add(new Pair<>(testeURL2, 1));

         indice.put(zoom, new Pair<Integer,List<Pair<URL, Integer>>>(3, documentsF));
         indice.put(valor, new Pair<Integer,List<Pair<URL, Integer>>>(3, documentsF));

         Rank rank = new Rank(indice, queryG, true, 2);






    }

}
