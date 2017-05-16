package crawler;

import javafx.util.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public class Downloader {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    public Pair<URL, Document> download(URL url) throws IOException {
        Connection connection = Jsoup.connect(url.toString()).userAgent(USER_AGENT);
        Document page = connection.get();
        if (!connection.response().contentType().contains("text/html")) {
            throw new IllegalArgumentException("The link does not refers a html page.");
        } else if (connection.response().statusCode() != 200) {
            throw new IllegalStateException("The connection fails. HTML status code: " + connection.response().statusCode());
        }
        return new Pair<>(url, page);
    }
}