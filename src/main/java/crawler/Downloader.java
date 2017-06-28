package crawler;

import javafx.util.Pair;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;

public final class Downloader {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";

    private Downloader() {
    }

    public static Pair<URL, Document> download(URL url) {
        Connection connection = Jsoup.connect(url.toString()).userAgent(USER_AGENT);
        Document page = null;
        try {
            page = connection.get();
        } catch (IOException e) {
            return null;
        }
        if (!connection.response().contentType().contains("text/html")) {
            return null;
        } else if (connection.response().statusCode() != 200) {
            return null;
        }
        return new Pair<>(url, page);
    }
}