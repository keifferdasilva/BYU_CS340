package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {

    public static final String USER_KEY = "user";
    private static final String LOG_TAG = "GET_USER_TASK";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private final String alias;

    private User user;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(authToken, messageHandler);
        this.alias = alias;
    }

    @Override
    protected void runTask() {
        user = getUser();
        if(user != null) {
            // Call sendSuccessMessage if successful
            sendSuccessMessage();
        }
        // or call sendFailedMessage if not successful
        // sendFailedMessage()
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }

    private User getUser() {
        try{
            GetUserRequest request = new GetUserRequest(authToken, alias);
            GetUserResponse response = getServerFacade().getUser(request, UserService.GET_USER_URL_PATH);
            if(response.isSuccess()){
                return response.getUser();
            }
            else{
                sendFailedMessage(response.getMessage());
                return null;
            }
        }
        catch(IOException | TweeterRemoteException ex){
            Log.e(LOG_TAG, "Failed to get user", ex);
            return null;
        }
    }
}
