import java.io.IOException;

public interface DocumentFetcher {

    String getContent(String uri) throws IOException;
}
