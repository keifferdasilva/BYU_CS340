package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User>{


    public FollowingPresenter(PagedView<User> observer){
        super(new UserService(), new FollowService(), null, observer);
    }

    @Override
    protected void getItems() {
        followService.getFollowees(targetUser, lastItem, new FollowingObserver());
    }

    private class FollowingObserver extends PagedObserver {
        @Override
        protected String getPrefix() {
            return "Failed to get people you follow";
        }
    }

}
