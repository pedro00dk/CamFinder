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
    public String queryG;
    public List<Double> idfQuery = new ArrayList<>();
    public InvertedIndex index;


    public Rank(InvertedIndex index, String queryG, boolean tfidf)  {
        this.index = index;
        this.queryG = queryG;

        if (tfidf){
            calculateTfIdf();
            rankVector();
        }

    }

    /*Calculo o tfidf para cada termo do meu conjunto de termos*/
    private void calculateTfIdf() {
        //Pega todas as querys da consultas, salva numa lista e calcula o tfIDF dela relacionado a todos os documentos
        for(String query : index.termFrequency.keySet()){
            tfidfLists.add(getTfidfList(query));
        }
    }

    /*Faço meu cálculo de tfIdf para o determinado termo da minha base de documentos
    ex.: zoom.10 --> 5, 6,7,8....*/
    private Pair<String, List<Pair<URL, Double>>> getTfidfList(String query) {
        Map<Integer, Integer> frequencyD = index.termDocuments.get(query);
        int generalFrequency = index.termFrequency.get(query);

        List<URL> url = new ArrayList<>();
        url.addAll(index.documentIndexes.keySet());

        if (generalFrequency == 0) {
            return null;
        }

        List<Pair<URL, Double>> tfidfList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> pageFrequency:frequencyD.entrySet()) {
            double tf = 1 + Math.log10(pageFrequency.getValue());
            double idf = Math.log10((double)index.indexedDocuments.size()/generalFrequency);
            double tfidf = tf * idf;

            tfidfList.add(new Pair<>(index.indexedDocuments.get(pageFrequency.getKey()), tfidf));
        }

        return new Pair<>(query, tfidfList);
    }

    /*Vector with TfIdf */
    private void rankVector() {
        String words[] = queryG.split("");;
        List<Integer> wordsContent = new ArrayList<>();

        wordsContent.add(0, index.termFrequency.get(queryG));

//        for (int i = 0; i < words.length ; i++) {
//            Integer freq = index.termFrequency.get(words[i]);
//            wordsContent.add(i, freq == null ? 0 :freq);
//        }
        //Integer freq = index.termFrequency.get(queryG);
        //wordsContent.add(0, freq == null ? 0 :freq);

        Map<URL, List<Double>> TfIdfVector;
        TfIdfVector = vectors.buildVectorSpace(tfidfLists);
        idfQuery = vectors.vectorQuery(1, wordsContent, index.indexedDocuments.size());

        rank = vectors.rank(TfIdfVector, idfQuery);
        rank.entrySet().stream()
                .sorted(Comparator.comparingDouble(entry -> entry.getValue()))
                .forEach(entry -> System.out.println(entry.getKey() + "  ->  " + entry.getValue()));
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
        String words[] = queryG.split(" ");;
        List<Integer> wordsContent = new ArrayList<>();

        for (int i = 0; i < words.length ; i++) {
            Integer freq = index.termFrequency.get(words[i]);
            wordsContent.add(i, freq == null ? 0 :freq);
        }

        idfQuery = vectors.vectorQuery(1, wordsContent, index.indexedDocuments.size());
        Map<URL, List<Double>> simpleVector = new HashMap<>();
        simpleVector = vectors.buildVectorSpace(tfLists);

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
