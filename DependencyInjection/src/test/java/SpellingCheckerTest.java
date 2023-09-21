import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class SpellingCheckerTest {


    private SpellingChecker spellingChecker;


    public class TestModule extends AbstractModule {

        Dict dictionary = Mockito.mock(Dict.class);
        DocumentFetcher fetcher = Mockito.mock(DocumentFetcher.class);

        @Override
        protected void configure() {

            String wordsToParse = "We doesn't be aah all the time";
            try {
                Mockito.when(fetcher.getContent(Mockito.any())).thenReturn(wordsToParse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Set<String> words = new HashSet<>();
            words.add("be");
            words.add("doesn");
            words.add("t");
            words.add("aah");
            Mockito.when(dictionary.isValidWord(Mockito.anyString())).thenAnswer(new Answer<Boolean>() {
                @Override
                public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                    return words.contains(invocationOnMock.getArgument(0));
                }
            });

            bind(Dict.class).toInstance(dictionary);
            bind(Parser.class).to(WordExtractor.class);
            bind(DocumentFetcher.class).toInstance(fetcher);
        }
    }


    @BeforeEach
    public void setup(){

        Injector injector = Guice.createInjector(new TestModule());
        spellingChecker = injector.getInstance(SpellingChecker.class);
    }

    @Test
    public void checkTest() throws IOException {
        SortedMap<String, Integer> mistakes = spellingChecker.check("test");
        SortedMap<String, Integer> expectedMistakes = new TreeMap<>();
        expectedMistakes.put("we", 1);
        expectedMistakes.put("all", 1);
        expectedMistakes.put("the", 1);
        expectedMistakes.put("time", 1);
        Assertions.assertNotNull(mistakes);
        Assertions.assertEquals(expectedMistakes, mistakes);

    }

}
