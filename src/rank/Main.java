package rank;

import index.InvertedIndex;
import javafx.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path extractedContentPath = Paths.get("pages", "extracted", "extracted.data");
        ObjectInputStream is = new ObjectInputStream(Files.newInputStream(extractedContentPath));
        //noinspection unchecked
        BlockingQueue<Pair<URL, Map<String, String>>> extractorOutput = (BlockingQueue<Pair<URL, Map<String, String>>>) is.readObject();
        InvertedIndex ii = new InvertedIndex(extractorOutput, 5);
        Rank rank = new Rank(ii);

        List<String> query1 = new ArrayList<>();
        query1.add("name.5DS");

        List<URL> rank1 = rank.rank(query1, false);

        List<String> query2 = new ArrayList<>();
        query2.add("price.2599");

        List<URL> rank2 = rank.rank(query2, false);

        System.out.println(SpearmanCorrelation.evaluate(rank1, rank2));


    }

}
