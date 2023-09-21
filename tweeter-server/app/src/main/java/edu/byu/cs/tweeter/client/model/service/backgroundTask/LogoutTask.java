package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;

/**
 * Background task that logs out a user (i.e., ends a session).
 */
public class LogoutTask extends AuthenticatedTask {

    private static final String LOG_TAG = "LOGOUT_TASK";

    public LogoutTask(AuthToken authToken, Handler messageHandler) {
        super(authToken, messageHandler);
    }

    @Override
    protected void runTask() {
        try{
            LogoutRequest request = new LogoutRequest(authToken);
            LogoutResponse response = getServerFacade().logout(request, UserService.LOGOUT_URL_PATH);
            if(response.isSuccess()){
                sendSuccessMessage();
            }
            else{
                sendFailedMessage(response.getMessage());
            }
        }
        catch(TweeterRemoteException | IOException ex){
            Log.e(LOG_TAG, "Failed to logout", ex);
            sendExceptionMessage(ex);
        }
        // We could do this from the presenter, without a task and handler, but we will
        // eventually remove the auth token from  the DB and will need this then.

        // Call sendSuccessMessage if successful
        // or call sendFailedMessage if not successful
        // sendFailedMessage()
    }
}
