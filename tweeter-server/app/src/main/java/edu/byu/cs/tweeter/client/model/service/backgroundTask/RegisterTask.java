package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends AuthenticateTask {

    private static final String LOG_TAG = "REGISTER_TASK";
    /**
     * The user's first name.
     */
    private final String firstName;
    
    /**
     * The user's last name.
     */
    private final String lastName;

    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private final String image;

    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(messageHandler, username, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    @Override
    protected Pair<User, AuthToken> runAuthenticationTask() {

        try{
            RegisterRequest request = new RegisterRequest(username, password, firstName, lastName, image);
            RegisterResponse response = getServerFacade().register(request, UserService.REGISTER_URL_PATH);
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
            Log.e(LOG_TAG, "Failed to register", ex);
            sendExceptionMessage(ex);
            return null;
        }


        /*User registeredUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(registeredUser, authToken);*/
    }
}
