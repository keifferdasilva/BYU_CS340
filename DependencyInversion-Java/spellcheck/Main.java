
package spellcheck;

import java.io.IOException;
import java.util.SortedMap;


public class Main {

	public static void main(String[] args) {
	
		try {
			DocumentFetcher fetcher = new URLFetcher();
			Parser parser = new WordExtractor();
			Dict dictionary = new TextDictionary("dict.txt");
			SpellingChecker checker = new SpellingChecker(fetcher, parser, dictionary);
			SortedMap<String, Integer> mistakes = checker.check(args[0]);
			System.out.println(mistakes);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

