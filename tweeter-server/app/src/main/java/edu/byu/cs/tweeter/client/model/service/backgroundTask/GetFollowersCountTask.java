package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    private static final String LOG_TAG = "GET_FOLLOWERS_COUNT_TASK";

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {
        try {
            FollowersCountRequest request = new FollowersCountRequest(authToken, getTargetUser().getAlias());
            FollowersCountResponse response = getServerFacade().getFollowerCount(request, FollowService.GET_FOLLOWERS_COUNT_URL_PATH);
            if(response.isSuccess()){
                return response.getFollowersCount();
            }
            else{
                sendFailedMessage(response.getMessage());
                return -1;
            }
        }
        catch(IOException | TweeterRemoteException ex){
            Log.e(LOG_TAG, "Failed to get followers count", ex);
            sendExceptionMessage(ex);
            return -1;
        }
    }
}
