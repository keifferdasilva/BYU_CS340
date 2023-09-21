package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.observer.OpenProfileObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class OpenProfileHandler extends BackgroundTaskHandler<OpenProfileObserver> {

    public OpenProfileHandler(OpenProfileObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(OpenProfileObserver observer, Bundle data) {
        User user = (User) data.getSerializable(GetUserTask.USER_KEY);
        observer.goToUserProfile(user);
    }
}
