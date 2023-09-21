package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.GetItemsObserver;

public class GetItemsHandler extends BackgroundTaskHandler<GetItemsObserver> {

    public GetItemsHandler(GetItemsObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(GetItemsObserver observer, Bundle data) {
        int count = data.getInt(GetCountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
