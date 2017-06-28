package rank;

import index.InvertedIndex;
import javafx.util.Pair;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Rank {

    public static List<Pair<String, List<Pair<URL, Double>>>> tfidfLists = new ArrayList<>();
    public static List<Pair<String, List<Pair<URL, Double>>>> tfLists = new ArrayList<>();
    public static Map<URL, Double> rank;
    public static Map<URL, Double> rank2;
    public Vspace vectors = new Vspace();
    public List<String> queryG;
    public List<Double> idfQuery = new ArrayList<>();
    public Map<String, Integer> attributeIndex = new HashMap<>();
    public InvertedIndex index;


    public Rank(InvertedIndex index, List<String> queryG, boolean tfidf)  {
        this.index = index;
        this.queryG = queryG;

        if (tfidf){
            calculateTfIdf();
            rankVector();
        }

    }

    /*Calculo o tfidf para cada termo do meu conjunto de termos*/
    private void calculateTfIdf() {
        List<String> attributes = new ArrayList<>();
        attributes.addAll(index.termFrequency.keySet());

        for (int i = 0; i <attributes.size() ; i++) {
            attributeIndex.put(attributes.get(i), i);
        }
        //Pega todas as querys da consultas, salva numa lista e calcula o tfIDF dela relacionado a todos os documentos
        for(String query : index.termFrequency.keySet()){
            tfidfLists.add(attributeIndex.get(query) ,getTfidfList(query));
        }
    }

    /*Faço meu cálculo de tfIdf para o determinado termo da minha base de documentos
    ex.: zoom.10 --> 5, 6,7,8....*/
    private Pair<String, List<Pair<URL, Double>>> getTfidfList(String query) {
        Map<Integer, Integer> frequencyD = index.termDocuments.get(query);
        int generalFrequency = index.termFrequency.get(query);
        double tf =0;
        if (generalFrequency == 0) {
            double tfidf=0;
            return null;
        }

        List<Pair<URL, Double>> tfidfList = new ArrayList<>();

        for (Map.Entry<Integer, Integer> pageFrequency:frequencyD.entrySet()) {
            if (pageFrequency.getValue()!=0) tf = 1 + (Math.log(pageFrequency.getValue()))/Math.log(2);
            else{
                tf=0;
            }
            double idf = ((Math.log((double)index.indexedDocuments.size())/Math.log(2))/(double)generalFrequency);
            double tfidf = tf * idf;

            tfidfList.add(new Pair<>(index.indexedDocuments.get(pageFrequency.getKey()), tfidf));
        }

        return new Pair<>(query, tfidfList);
    }

    /*Vector with TfIdf */
    private void rankVector() {
        Map<URL, List<Double>> TfIdfVector;
        TfIdfVector = vectors.buildVectorSpace(tfidfLists, attributeIndex);
        idfQuery = vectors.vectorQuery(index.termFrequency, attributeIndex, queryG, index.documentIndexes.size());

        rank = vectors.rank(TfIdfVector, idfQuery);
        rank.entrySet().stream()
                .sorted(Comparator.comparingDouble(entry -> entry.getValue()))
                .forEach(entry -> System.out.println(entry.getKey() + "  ->  " + entry.getValue()));
       // System.out.println(rank);
    }

    /*Calculo o tf para cada termo do meu conjunto de termos*/
    private void calculateTf() {
        //Pega todas as querys da consultas, salva numa lista e calcula o tfIDF dela relacionado a todos os documentos
        for(String query : index.termFrequency.keySet()){
            tfLists.add(getTfList(query));
        }
    }


    /*Vector without TfIdf*/
    private void simpleVector() throws MalformedURLException {
        List<Integer> wordsContent = new ArrayList<>();

        for (int i = 0; i < queryG.size() ; i++) {
            Integer freq = index.termFrequency.get(queryG.get(i));
            wordsContent.add(i, freq == null ? 0 :freq);
        }

        idfQuery = vectors.vectorQuery(index.termFrequency, attributeIndex, queryG, index.documentIndexes.size());
        Map<URL, List<Double>> simpleVector = new HashMap<>();
        simpleVector = vectors.buildVectorSpace(tfLists, attributeIndex);

        rank2 = vectors.rank(simpleVector, idfQuery);
        System.out.println(rank2);
    }

    /*Faço meu cálculo APENAS de TF para o determinado termo da minha base de documentos
    ex.: zoom.10 --> 5, 6,7,8....*/
    private Pair<String, List<Pair<URL, Double>>> getTfList(String query){
        Map<Integer, Integer> frequencyD = index.termDocuments.get(query);
        int generalFrequency = index.termFrequency.get(query);

        List<Integer> indices = new ArrayList<>();
        indices.addAll(index.indexedDocuments.keySet());

        List<URL> url = new ArrayList<>();
        url.addAll(index.documentIndexes.keySet());

        if (generalFrequency == 0) {
            return null;
        }

        List<Pair<URL, Double>> tfidfList = new ArrayList<>();
        for (int i = 0; i <frequencyD.size() ; i++) {
            double tf = 1 + Math.log10((double)frequencyD.get(frequencyD.get(url.get(i))));

            tfidfList.add(new Pair<>(index.indexedDocuments.get(indices.get(i)), tf));
        }

        return new Pair<>(query, tfidfList);

    }
}
