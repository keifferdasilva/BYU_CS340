import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


public class URLFetcher implements DocumentFetcher{

    @Override
    public String getContent(String uri) throws IOException {
        URL url = new URL(uri);
        URLConnection connection = url.openConnection();

        StringBuilder contentBuffer = new StringBuilder();

        try (InputStream input = connection.getInputStream()) {
            int c;
            while ((c = input.read()) >= 0) {
                contentBuffer.append((char) c);
            }
        }

        return contentBuffer.toString();
    }

}

