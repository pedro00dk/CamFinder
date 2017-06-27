package rank;

import javafx.util.Pair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rank {

    public static List<Pair<String, List<Pair<URL, Double>>>> tfidfLists = new ArrayList<>();
    public static List<Pair<String, List<Pair<URL, Double>>>> tfLists = new ArrayList<>();
    public static Map<URL, Double> rank;
    public static Map<URL, Double> rank2;
    public Vspace vectors = new Vspace();
    public int documentCount;
    public String queryG;
    public List<Double> idfQuery = new ArrayList<>();
    public Map<Integer, URL> indexedDocuments;
    public Map<URL, Integer> documentIndexes; //documentos indexados
    public Map<String, Map<Integer, Integer>> termDocuments; //pegar o termo no documento especïfico
    public Map<String, Integer> termFrequency; //pegar o termo em todos os documentos


    public Rank(Map<URL, Integer> documentIndexes, Map<String, Map<Integer, Integer>> termDocuments,
                Map<String, Integer> termFrequency, String queryG, boolean tfidf, Map<Integer, URL> indexedDocuments)  {

        this.indexedDocuments = indexedDocuments;
        this.documentIndexes = documentIndexes;
        this.termDocuments = termDocuments;
        this.termFrequency = termFrequency;
        this.queryG = queryG;
        documentCount = documentIndexes.size();

        if (tfidf){
            calculateTfIdf();
            rankVector();
        }

    }

    /*Calculo o tfidf para cada termo do meu conjunto de termos*/
    private void calculateTfIdf() {
        //Pega todas as querys da consultas, salva numa lista e calcula o tfIDF dela relacionado a todos os documentos
        for(String query : termFrequency.keySet()){
            tfidfLists.add(getTfidfList(query));
        }
    }

    /*Faço meu cálculo de tfIdf para o determinado termo da minha base de documentos
    ex.: zoom.10 --> 5, 6,7,8....*/
    private Pair<String, List<Pair<URL, Double>>> getTfidfList(String query) {
        Map<Integer, Integer> frequencyD = termDocuments.get(query);
        int generalFrequency = termFrequency.get(query);

        List<Integer> indices = new ArrayList<>();
        indices.addAll(indexedDocuments.keySet());

        List<URL> url = new ArrayList<>();
        url.addAll(documentIndexes.keySet());

        if (generalFrequency == 0) {
            return null;
        }

        List<Pair<URL, Double>> tfidfList = new ArrayList<>();
        for (int i = 0; i <frequencyD.size() ; i++) {
            double tf = 1 + Math.log10((double)frequencyD.get(frequencyD.get(url.get(i))));
            double idf = Math.log10((double)documentCount/generalFrequency);
            double tfidf = tf * idf;

            tfidfList.add(new Pair<>(indexedDocuments.get(indices.get(i)), tfidf));
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

        Map<URL, List<Double>> TfIdfVector;
        TfIdfVector = vectors.buildVectorSpace(tfidfLists);
        idfQuery = vectors.vectorQuery(words, wordsContent, documentCount);

        rank = vectors.rank(TfIdfVector, idfQuery);
        System.out.println(rank);
    }

    /*Calculo o tf para cada termo do meu conjunto de termos*/
    private void calculateTf() {
        //Pega todas as querys da consultas, salva numa lista e calcula o tfIDF dela relacionado a todos os documentos
        for(String query : termFrequency.keySet()){
            tfLists.add(getTfList(query));
        }
    }


    /*Vector without TfIdf*/
    private void simpleVector() throws MalformedURLException {
        String words[] = queryG.split(" ");;
        List<Integer> wordsContent = new ArrayList<>();

        for (int i = 0; i < words.length ; i++) {
            wordsContent.add(i, termFrequency.get(words[i]));
        }

        idfQuery = vectors.vectorQuery(words, wordsContent, documentCount);
        Map<URL, List<Double>> simpleVector = new HashMap<>();
        simpleVector = vectors.buildVectorSpace(tfLists);

        rank2 = vectors.rank(simpleVector, idfQuery);
        System.out.println(rank2);
    }

    /*Faço meu cálculo APENAS de TF para o determinado termo da minha base de documentos
    ex.: zoom.10 --> 5, 6,7,8....*/
    private Pair<String, List<Pair<URL, Double>>> getTfList(String query){
        Map<Integer, Integer> frequencyD = termDocuments.get(query);
        int generalFrequency = termFrequency.get(query);

        List<Integer> indices = new ArrayList<>();
        indices.addAll(indexedDocuments.keySet());

        List<URL> url = new ArrayList<>();
        url.addAll(documentIndexes.keySet());

        if (generalFrequency == 0) {
            return null;
        }

        List<Pair<URL, Double>> tfidfList = new ArrayList<>();
        for (int i = 0; i <frequencyD.size() ; i++) {
            double tf = 1 + Math.log10((double)frequencyD.get(frequencyD.get(url.get(i))));

            tfidfList.add(new Pair<>(indexedDocuments.get(indices.get(i)), tf));
        }

        return new Pair<>(query, tfidfList);

    }

    public static void main(String[] args) throws MalformedURLException {




    }

}
