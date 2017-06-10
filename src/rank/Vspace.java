package rank;


public class Vspace {

    private double consult[] = new double[10];
    private double document[] = new double[10];
    private int total = 0;
    private int appears = 0;
    private int appearsD = 0;


    private double tfidf(int total, int appears, int appearsD) {
        double tf = 1 + Math.log(appearsD);
        double idf = Math.log(total / appears);
        double tfidf = tf * idf;

        return tfidf;
    }

    private void vetorialSpace() {
        for (int i = 0; i < document.length; i++) {
            document[i] = tfidf(total, appears, appearsD);
        }

    }

    private double similar(double document[], double query[]) {
        double denominador = 0;
        double numeradorD = 0;
        double numeradorQ = 0;
        double numerador = 0;

        for (int i = 0; i < 10 ; i++) {
                denominador = denominador + document[i] * query[i];
                numeradorD = numeradorD + document[i] * document [i];
                numeradorQ = numeradorQ + query[i] * query [i];
                numerador = Math.sqrt(numeradorD * numeradorQ);
        }

        return  denominador/numerador;
    }

}
