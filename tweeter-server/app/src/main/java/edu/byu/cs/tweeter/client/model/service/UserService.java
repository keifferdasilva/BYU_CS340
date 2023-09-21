package edu.byu.cs.tweeter.client.model.service;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.AuthenticationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.OpenProfileHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.observer.OpenProfileObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;

public class UserService extends Service{

    public static final String LOGIN_URL_PATH = "/login";
    public static final String REGISTER_URL_PATH = "/register";
    public static final String GET_USER_URL_PATH = "/getuser";
    public static final String LOGOUT_URL_PATH = "/logout";


    public void login(String alias, String password, SimpleNotificationObserver observer) {
        LoginTask loginTask = new LoginTask(alias, password, new AuthenticationHandler(observer));
        executeTask(loginTask);
    }

    public void logout(SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new SimpleNotificationHandler(observer));
        executeTask(logoutTask);
    }

    public void register(String firstName, String lastName, String alias, String password, String imageBytesBase64, SimpleNotificationObserver registerObserver) {
        // Send register request.
        RegisterTask registerTask = new RegisterTask(firstName, lastName,
                alias, password, imageBytesBase64, new AuthenticationHandler(registerObserver));
        executeTask(registerTask);
    }

    public void getUser(String userAlias, OpenProfileObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new OpenProfileHandler(observer));
        executeTask(getUserTask);
    }


}
