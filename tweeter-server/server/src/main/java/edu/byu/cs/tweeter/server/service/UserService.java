package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.DAOFactory;
import edu.byu.cs.tweeter.server.dao.ImageDAO;
import edu.byu.cs.tweeter.server.dao.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {

    UserDAO userDAO;
    ImageDAO imageDAO;
    AuthTokenDAO authTokenDAO;

    public UserService(DAOFactory factory){
        userDAO = factory.getUserDAO();
        authTokenDAO = factory.getAuthTokenDAO();
        imageDAO = factory.getImageDAO();
    }

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        User user = userDAO.login(request.getUsername(), request.getPassword());
        AuthToken authToken = authTokenDAO.createNewAuthToken(user.getAlias());
        return new LoginResponse(user, authToken);
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        if(registerRequest.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        }
        else if(registerRequest.getPassword() == null){
            throw new RuntimeException("[Bad Request] Missing a password");
        }
        else if(registerRequest.getFirstName() == null){
            throw new RuntimeException("[Bad Request] Missing a first name");
        }
        else if(registerRequest.getLastName() == null){
            throw new RuntimeException("[Bad Request] Missing a last name");
        }

        //User user = getDummyUser();
        //AuthToken authToken = getDummyAuthToken();
        User user = userDAO.createNewUser(registerRequest.getUsername(), registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getPassword());
        user.setImageUrl(imageDAO.uploadImage(registerRequest.getImage(), registerRequest.getUsername()));

        AuthToken authToken = authTokenDAO.createNewAuthToken(user.getAlias());
        return new RegisterResponse(user, authToken);
    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }


    public GetUserResponse getUser(GetUserRequest request) {
        if(request.getUserAlias() == null){
            throw new RuntimeException("[Bad Request] Request needs a user alias");
        } else if (request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing authentication token");
        }
        //GetUserResponse response = new GetUserResponse(getFakeData().findUserByAlias(request.getUserAlias()));
        if(authTokenDAO.verifyAuthToken(request.getAuthToken())) {
            GetUserResponse response = new GetUserResponse(userDAO.getUser(request.getUserAlias()));
            if (response.getUser() == null) {
                throw new RuntimeException("[Server Error] Could not find this user");
            } else {
                return response;
            }
        }
        else{
            throw new RuntimeException("[Bad Request] Your authentication token is invalid");
        }
    }

    public LogoutResponse logout(LogoutRequest request) {
        if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing authentication token");
        }
        //eventually we will need to delete the authtoken
        LogoutResponse response = authTokenDAO.removeAuthToken(request.getAuthToken());
        return response;
    }
}
