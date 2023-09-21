package org.example;

import java.util.ArrayList;
import java.util.List;

public class DataPage <T>{
    private List<T> values;
    private boolean hasMorePages;

    public DataPage(){
        setValues(new ArrayList<T>());
        setHasMorePages(false);
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

    public boolean isHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
}
