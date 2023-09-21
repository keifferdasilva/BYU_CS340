package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    private static final String LOG_TAG = "GET_FOLLOWING_COUNT_TASK";

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {
        try {
            FollowingCountRequest request = new FollowingCountRequest(authToken, getTargetUser().getAlias());
            FollowingCountResponse response = getServerFacade().getFollowingCount(request, FollowService.GET_FOLLOWING_COUNT_URL_PATH);
            if(response.isSuccess()){
                return response.getFollowersCount();
            }
            else{
                sendFailedMessage(response.getMessage());
                return -1;
            }
        }
        catch(IOException | TweeterRemoteException ex){
            Log.e(LOG_TAG, "Failed to get following count", ex);
            sendExceptionMessage(ex);
            return -1;
        }
    }
}
