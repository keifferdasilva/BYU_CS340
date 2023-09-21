import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class SimpleModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DocumentFetcher.class).to(URLFetcher.class);
        bind(Parser.class).to(WordExtractor.class);
        bind(Dict.class).to(TextDictionary.class);
    }

    @Qualifier
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TextDict{}

    @Provides
    @TextDict
    public String dictionaryFilePath(){return "dict.txt";}
}
