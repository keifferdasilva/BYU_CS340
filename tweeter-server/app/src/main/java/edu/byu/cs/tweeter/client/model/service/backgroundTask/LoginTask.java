package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LOGIN_TASK";

    public LoginTask(String username, String password, Handler messageHandler) {
        super(messageHandler, username, password);
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {
        try{
            LoginRequest request = new LoginRequest(username, password);
            LoginResponse response = getServerFacade().login(request, UserService.LOGIN_URL_PATH);
            if(response.isSuccess()){
                User loggedInUser = response.getUser();
                AuthToken authToken = response.getAuthToken();
                return new Pair<>(loggedInUser, authToken);
            }
            else{
                sendFailedMessage(response.getMessage());
                return null;
            }
        }catch (IOException | TweeterRemoteException ex) {
            Log.e(LOG_TAG, "Failed to login", ex);
            sendExceptionMessage(ex);
            return null;
        }
        /*User loggedInUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(loggedInUser, authToken);*/
    }
}
