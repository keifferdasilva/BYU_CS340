package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class Presenter {

    protected UserService userService;

    protected FollowService followService;

    protected StatusService statusService;

    protected View observer;

    public Presenter(UserService userService, FollowService followService, StatusService statusService, View observer){
        this.userService = userService;
        this.followService = followService;
        this.statusService = getStatusService();
        this.observer = observer;
    }

    protected StatusService getStatusService(){
        if(statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public interface View{
        void displayMessage(String message);
    }



    protected abstract class Observer implements ServiceObserver{

        protected abstract String getPrefix();
        @Override
        public void handleFailure(String message) {
            observer.displayMessage(getPrefix() + ": " + message);
        }

        @Override
        public void handleException(Exception exception) {
            observer.displayMessage(getPrefix() + " because of exception: " + exception.getMessage());
        }

        @Override
        public void handleCleanup() {

        }
    }
}
