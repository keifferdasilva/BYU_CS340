package edu.byu.cs.tweeter.client.model.service.observer;

import java.util.List;

public interface PagedTaskObserver<T> extends ServiceObserver{

    void addItems(List<T> items, boolean hasMorePages);
}
