package spellcheck;

import java.io.IOException;
import java.util.Set;

public interface Dict {

    Set<String> scanDictionary(String fileName) throws IOException;

    boolean isValidWord(String word);
}
