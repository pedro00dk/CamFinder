package rank;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Evaluate {


    double spearmanCorrelation(Map<URL, Integer> rank1, Map<URL, Integer> rank2){

        List<Integer> distances = new ArrayList<>();
        int distance = 0;

        for (int i = 0; i <rank1.size() ; i++) {
             distances.set(i, rank1.get(i) - rank2.get(i));
             distances.set(i, distances.get(i)*distances.get(i));
             distance = distance + distances.get(i);
        }

        double spearmanCorrelation;

        spearmanCorrelation = 1 -
                ((6*distance)/(rank1.size()*(rank1.size()*rank1.size()-1)));

        return spearmanCorrelation;
    }
}
