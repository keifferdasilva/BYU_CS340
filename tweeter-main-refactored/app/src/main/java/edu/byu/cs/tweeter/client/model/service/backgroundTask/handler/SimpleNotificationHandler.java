package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;

public class SimpleNotificationHandler extends BackgroundTaskHandler<SimpleNotificationObserver> {

    public SimpleNotificationHandler(SimpleNotificationObserver mainObserver) {
        super(mainObserver);
    }


    @Override
    protected void handleSuccessMessage(SimpleNotificationObserver observer, Bundle data) {
        observer.handleSuccess();
    }
}
