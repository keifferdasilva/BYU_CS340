import com.google.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


public class TextDictionary implements Dict {

	private Set<String> words;

	@Inject
    public TextDictionary(@SimpleModule.TextDict String fileName) throws IOException {
        words = scanDictionary(fileName);
	}

    @Override
    public Set<String> scanDictionary(String fileName) throws IOException{
        Set<String> words = new TreeSet<>();
        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim();
                words.add(word);
            }
        }
        return words;
    }

    @Override
    public boolean isValidWord(String word) {
		return words.contains(word);
	}
}

