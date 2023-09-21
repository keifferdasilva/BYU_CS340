package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public  class AuthenticationPresenter extends Presenter{

    public interface AuthenticationView extends View{
        void initializeMainActivity(User user);
    }


    public AuthenticationPresenter(UserService userService, FollowService followService, StatusService statusService, View observer) {
        super(userService, followService, statusService, observer);
    }

    protected abstract class AuthenticationObserver extends Observer implements SimpleNotificationObserver {

        @Override
        public void handleSuccess() {
            User user = Cache.getInstance().getCurrUser();
            ((AuthenticationView)observer).initializeMainActivity(user);
        }
    }
}
