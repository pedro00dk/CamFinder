package classifier;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Download the pages to be used in the classifier.
 */
public class PageCollector {

    /**
     * Path to the negative page links.
     */
    static Path negativePageLinksPath = Paths.get("pages", "negative.txt");

    /**
     * Path to the positive page links.
     */
    static Path positivePageLinksPath = Paths.get("pages", "positive.txt");

    /**
     * Path to save the downloaded negative pages.
     */
    static Path negativeDownloadedPagesPath = Paths.get("pages", "downloaded", "negative");

    /**
     * Path to save the downloaded positive pages.
     */
    static Path positiveDownloadedPagesPath = Paths.get("pages", "downloaded", "positive");

    /**
     * Download the positive and negative pages.
     *
     * @param args ignored
     * @throws IOException if fails to read the links file or the directory path does not exist
     */
    public static void main(String[] args) throws IOException {

        System.out.println("Downloading negative instances");
        downloadPages(
                Files.lines(negativePageLinksPath)
                        .filter(link -> !link.startsWith("#") && link.trim().length() != 0)
                        .collect(Collectors.toList()),
                negativeDownloadedPagesPath,
                "Page "
        );

        System.out.println("Downloading positive instances");
        downloadPages(
                Files.lines(positivePageLinksPath)
                        .filter(link -> !link.startsWith("#") && link.trim().length() != 0)
                        .collect(Collectors.toList()),
                positiveDownloadedPagesPath,
                "Page "
        );
    }

    /**
     * Download the received pages and saves the html in the received directory.
     *
     * @param links     the links to download
     * @param directory the destination directory
     * @param prefix    the file name prefix
     */
    static void downloadPages(List<String> links, Path directory, String prefix) {
        AtomicInteger downloadedPages = new AtomicInteger(0);
        links.stream().parallel()
                .forEach(link -> {
                            int currentPage = downloadedPages.getAndIncrement();
                            System.out.println("Downloading link (" + currentPage + "): " + link);
                            try {
                                Document document = Jsoup.connect(link).get();
                                Files.newBufferedWriter(Paths.get(directory.toString(), prefix + currentPage + ".html"))
                                        .append(document.html())
                                        .close();
                                System.out.println("Link (" + currentPage + ") Success");
                            } catch (IOException ignored) {
                                System.out.println("Link (" + currentPage + ") Fail");
                            }
                        }
                );
    }
}
