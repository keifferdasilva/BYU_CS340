package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;

public class PagedTaskHandler<T> extends BackgroundTaskHandler<PagedTaskObserver>{

    public PagedTaskHandler(PagedTaskObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(PagedTaskObserver observer, Bundle data) {
        boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);
        List<T> items = (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
        observer.addItems(items, hasMorePages);
    }
}
