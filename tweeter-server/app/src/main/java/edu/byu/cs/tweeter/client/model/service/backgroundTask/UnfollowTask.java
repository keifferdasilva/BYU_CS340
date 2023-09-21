package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {

    private static final String LOG_TAG = "UNFOLLOW_TASK";
    /**
     * The user that is being followed.
     */
    private final User followee;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void runTask() {
        try{
            UnfollowRequest request = new UnfollowRequest(Cache.getInstance().getCurrUser().getAlias(), followee.getAlias(), authToken);
            UnfollowResponse response = getServerFacade().unfollow(request, FollowService.UNFOLLOW_URL_PATH);
            if(response.isSuccess()){
                sendSuccessMessage();
            }
            else{
                sendFailedMessage(response.getMessage());
            }
        }
        catch(IOException | TweeterRemoteException ex){
            Log.e(LOG_TAG, "Failed to unfollow " + followee.getName(), ex);
            sendExceptionMessage(ex);
        }
        // We could do this from the presenter, without a task and handler, but we will
        // eventually access the database from here when we aren't using dummy data.

        // Call sendSuccessMessage if successful
        //sendSuccessMessage();
        // or call sendFailedMessage if not successful
        // sendFailedMessage()
    }


}
