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

/**
 * Created by bjcc on 27/06/2017.
 */
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path extractedContentPath = Paths.get("pages", "extracted", "extracted.data");
        ObjectInputStream is = new ObjectInputStream(Files.newInputStream(extractedContentPath));
        //noinspection unchecked
        BlockingQueue<Pair<URL, Map<String, String>>> extractorOutput = (BlockingQueue<Pair<URL, Map<String, String>>>) is.readObject();
        InvertedIndex ii = new InvertedIndex(extractorOutput, 5);
        String query = "name.Nikon";
        List<String> queryG = new ArrayList<>();
        queryG.add(query);
        Rank rank = new Rank(ii, queryG, true);
    }

}
