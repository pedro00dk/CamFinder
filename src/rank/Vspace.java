package rank;


public class Vspace {

    private double tf;
    private double idf;

    /*
        Calcular o TFIDF para cada palavra
     */
    double tfidf(int total, int appears, int appearsD) {
        if (appearsD != 0) tf = 1 + Math.log10(appearsD);
        else tf = 0;
        double x = (double) total / (double) appears;
        idf = Math.log10(x);

        return tf * idf;
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

    void vetorialSpace(int words, int totalDocuments) {
        double tfidf[][] = new double[words][totalDocuments];

        for (int i = 0; i < words; i++) {
            for (int j = 0; j < totalDocuments; j++) {
                tfidf[i][j] = tfidf(totalDocuments, appear(i), appearD(i, j));
            }
        }
    }

    /*
    Consultar no Indice Invertido quantas vezes aquela palavra ocorre na base de documentos
    */
    int appear(int i) {

        return 0;
    }

    /*
    Consultar no indice invertido quantas vezes aquela palavra aparece naquele documento específico
    */
    int appearD(int i, int j) {

        return 0;
    }


}
