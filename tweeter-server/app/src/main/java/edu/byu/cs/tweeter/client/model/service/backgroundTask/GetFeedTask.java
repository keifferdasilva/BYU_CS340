package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FeedRequest;
import edu.byu.cs.tweeter.model.net.response.FeedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {

    private static final String LOG_TAG = "GET_FEED_TASK";

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {
        try{
            FeedRequest request = new FeedRequest(authToken, getLimit(), getTargetUser().getAlias(), getLastItem());
            FeedResponse response = getServerFacade().getFeed(request, StatusService.GET_FEED_URL_PATH);
            if(response.isSuccess()){
                return new Pair<>(response.getStatuses(), response.getHasMorePages());
            }
            else{
                sendFailedMessage(response.getMessage());
                return null;
            }
        }catch (IOException | TweeterRemoteException ex){
            Log.e(LOG_TAG, "Failed to get feed", ex);
            sendExceptionMessage(ex);
            return null;
        }
        //return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }
}
