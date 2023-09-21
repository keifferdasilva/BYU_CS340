import com.google.inject.Inject;

import java.io.IOException;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;


public class SpellingChecker {

    private final DocumentFetcher fetcher;
    private final Parser parser;
    private final Dict dictionary;
    @Inject
    public SpellingChecker(DocumentFetcher fetcher, Parser parser, Dict dictionary){
        this.fetcher = fetcher;
        this.parser = parser;
        this.dictionary = dictionary;
    }

	public SortedMap<String, Integer> check(String uri) throws IOException {

		// download the document content
		//
		String content = fetcher.getContent(uri);

		// extract words from the content
		//
		List<String> words = parser.parse(content);

		// find spelling mistakes
		//
		SortedMap<String, Integer> mistakes = new TreeMap<>();

        for (String word : words) {
            if (!dictionary.isValidWord(word)) {
                if (mistakes.containsKey(word)) {
                    int oldCount = mistakes.get(word);
                    mistakes.put(word, oldCount + 1);
                } else {
                    mistakes.put(word, 1);
                }
            }
        }

		return mistakes;
	}
}

