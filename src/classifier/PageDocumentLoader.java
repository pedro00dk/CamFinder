package classifier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static util.LoggingUtils.*;

/**
 * Loads the page Jsoup documents from a link or path.
 */
public class PageDocumentLoader {

    /**
     * Prevents instantiation.
     */
    private PageDocumentLoader() {
    }

    /**
     * Downloads a list of pages from the received URLs.
     *
     * @param links    the links
     * @param parallel if should download in parallel
     * @return a map with the URLs and documents
     */
    public static Map<URL, Document> fromLinks(List<URL> links, boolean parallel) {

        //
        global().entering(className(), methodName());
        final Map<URL, Document> documents = parallel ? new Hashtable<>() : new HashMap<>();
        (parallel ? links.stream().sequential() : links.stream().parallel())
                .forEach(link -> {
                            try {
                                documents.put(link, Jsoup.parse(link, 0));
                            } catch (IOException ignored) {

                                //
                                global().log(Level.INFO, "Document " + link + " failed", link);
                            }
                        }
                );

        //
        global().exiting(className(), methodName());
        return documents;
    }

    /**
     * Loads a list of pages from the received paths.
     *
     * @param paths    the paths
     * @param parallel if should load in parallel
     * @return a map with the paths and documents
     */
    public static Map<Path, Document> fromPaths(List<Path> paths, boolean parallel) {

        //
        global().entering(className(), methodName());
        final Map<Path, Document> documents = parallel ? new Hashtable<>() : new HashMap<>();
        (parallel ? paths.stream().sequential() : paths.stream().parallel())
                .forEach(path -> {
                            try {
                                documents.put(path, Jsoup.parse(Files.newInputStream(path), "UTF-8", ""));
                            } catch (IOException ignored) {

                                //
                                global().log(Level.INFO, "Document " + path + " failed", path);
                            }

                        }
                );

        //
        global().exiting(className(), methodName());
        return documents;
    }
}
