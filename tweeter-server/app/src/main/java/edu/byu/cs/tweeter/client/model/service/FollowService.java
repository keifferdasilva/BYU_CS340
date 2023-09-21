package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.GetItemsObserver;
import edu.byu.cs.tweeter.client.model.service.observer.IsFollowerObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService extends Service{

    public static final String GET_FOLLOWING_URL_PATH = "/getfollowing";
    public static final String FOLLOW_URL_PATH = "/follow";
    public static final String IS_FOLLOWER_URL_PATH = "/isfollower";
    public static final String GET_FOLLOWERS_URL_PATH = "/getfollowers";
    public static final String GET_FOLLOWERS_COUNT_URL_PATH = "/getfollowerscount";
    public static final String GET_FOLLOWING_COUNT_URL_PATH = "/getfollowingcount";
    public static final String UNFOLLOW_URL_PATH = "/unfollow";


    public void unfollow(User selectedUser, SimpleNotificationObserver mainObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(mainObserver));
        executeTask(unfollowTask);
    }

    public void isFollower(User selectedUser, IsFollowerObserver mainObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(mainObserver));
        executeTask(isFollowerTask);
    }

    public void follow(User selectedUser, SimpleNotificationObserver mainObserver) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(mainObserver));
        executeTask(followTask);
    }

    public void getFollowingCount(User selectedUser, GetItemsObserver mainObserver) {
        // Get count of most recently selected user's followers.
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetItemsHandler(mainObserver));
        executeTask(followingCountTask);
    }

    public void getFollowersCount(User selectedUser, GetItemsObserver mainObserver) {
        // Get count of most recently selected user's followees (who they are following)
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetItemsHandler(mainObserver));
        executeTask(followersCountTask);
    }

    public void getFollowers(User user, User lastFollower, PagedTaskObserver<User> followersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollower, new PagedTaskHandler<User>(followersObserver));
        executeTask(getFollowersTask);
    }

    public void getFollowees(User user, User lastFollowee, PagedTaskObserver<User> followingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastFollowee, new PagedTaskHandler<User>(followingObserver));
        executeTask(getFollowingTask);
    }

}
