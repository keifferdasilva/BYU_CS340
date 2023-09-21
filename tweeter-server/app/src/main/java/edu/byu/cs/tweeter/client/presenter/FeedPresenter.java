package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;

public class FeedPresenter extends PagedPresenter<Status> {


    @Override
    protected void getItems() {
        statusService.getFeed(targetUser, lastItem, new FeedObserver());
    }

    public FeedPresenter(PagedView<Status> feedObserver){
        super(new UserService(), null, new StatusService(), feedObserver);
    }

    private class FeedObserver extends PagedObserver{
        @Override
        protected String getPrefix() {
            return "Failed to get feed";
        }
    }





}
