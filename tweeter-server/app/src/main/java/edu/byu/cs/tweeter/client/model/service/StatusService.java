package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.PagedTaskHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService extends Service{


    public static final String POST_STATUS_URL_PATH = "/poststatus";
    public static final String GET_FEED_URL_PATH = "/getfeed";
    public static final String GET_STORY_URL_PATH = "/getstory";

    public Cache getCache(){
        return Cache.getInstance();
    }

    public void getFeed(User user, Status lastStatus, PagedTaskObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new PagedTaskHandler<Status>(observer));
        executeTask(getFeedTask);
    }

    public void getStory(User user, Status lastStatus, PagedTaskObserver<Status> storyObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(getCache().getCurrUserAuthToken(),
                user, PAGE_SIZE, lastStatus, new PagedTaskHandler<Status>(storyObserver));
        executeTask(getStoryTask);
    }

    public void postNewStatus(String post, SimpleNotificationObserver mainObserver, List<String> URLs, List<String> mentions) {
        Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), System.currentTimeMillis(), URLs, mentions);
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new SimpleNotificationHandler(mainObserver));
        executeTask(statusTask);
    }


}
