package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User>{

    public FollowersPresenter(PagedView<User> observer){
        super(new UserService(), new FollowService(), null, observer);
    }


    @Override
    protected void getItems() {
        followService.getFollowers(targetUser, lastItem, new FollowersObserver());
    }

    private class FollowersObserver extends PagedObserver{
        @Override
        protected String getPrefix() {
            return "Failed to get followees";
        }
    }



}
