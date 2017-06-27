package rank;

import javafx.util.Pair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rank2 {

    private static List<Pair<String, List<Pair<URL, Double>>>> tfidfLists = new ArrayList<>();
    private static List<Pair<String, List<Pair<URL, Double>>>> tfLists = new ArrayList<>();
    private static Map<URL, Double> rank;
    private static Map<URL, Double> rank2;
    private Vspace vectors = new Vspace();
    private int documentCount;
    private String queryG;
    private List<Double> idfQuery = new ArrayList<>();

    public Map<URL, Integer> documentIndexes; //documentos indexados
    public Map<String, Map<Integer, Integer>> termDocuments; //pegar o termo no documento especïfico
    public Map<String, Integer> termFrequency; //pegar o termo em todos os documentos
    public List<String> attributes; //lista de attributos




    public Rank2(Map<URL, Integer> documentIndexes, Map<String, Map<Integer, Integer>> termDocuments,
                 Map<String, Integer> termFrequency, List<String> attributes, String queryG, boolean tfidf)  {

        this.documentIndexes = documentIndexes;
        this.termDocuments = termDocuments;
        this.termFrequency = termFrequency;
        this.attributes = attributes;
        this.queryG = queryG;

        if (tfidf){
            calculateTfIdf();
            rankVector();
        }



    }

    /*Calculo o tfidf para cada termo do meu conjunto de termos*/
    private void calculateTfIdf() {
        //Pega todas as querys da consultas, salva numa lista e calcula o tfIDF dela relacionado a todos os documentos
        for(String query : attributes){
            tfidfLists.add(getTfidfList(query));
        }
    }

    /*Faço meu cálculo de tfIdf para o determinado termo da minha base de documentos
    ex.: zoom.10 --> 5, 6,7,8....*/
    private Pair<String, List<Pair<URL, Double>>> getTfidfList(String query) {
        Map<Integer, Integer> frequencyD = termDocuments.get(query);
        int generalFrequency = termFrequency.get(query);

        List<URL> url = new ArrayList<>();
        url.addAll(documentIndexes.keySet());

        if (generalFrequency == 0) {
            return null;
        }
        List<Pair<URL, Double>> tfidfList = new ArrayList<>();


        for (int i = 0; i <frequencyD.size() ; i++) {
            double tf = 1 + Math.log10((double)frequencyD.get(url.get(i)));
            double idf = Math.log10((double)documentCount/generalFrequency);
            double tfidf = tf * idf;

            tfidfList.add(new Pair<>(url.get(i), tfidf));
        }

        return new Pair<>(query, tfidfList);
    }

    /*Vector with TfIdf */
    private void rankVector() {
        String words[] = queryG.split(" ");;
        List<Integer> wordsContent = new ArrayList<>();

        for (int i = 0; i < words.length ; i++) {
            wordsContent.add(i, termFrequency.get(words[i]));
        }

        Map<URL, List<Double>> TfIdfVector = new HashMap<>();
        TfIdfVector = vectors.buildVectorSpace(tfidfLists);
        idfQuery = vectors.vectorQuery(words, wordsContent, documentCount);

        rank = vectors.rank(TfIdfVector, idfQuery);
        System.out.println(rank);
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
