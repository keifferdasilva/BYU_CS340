package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.FollowersRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.FollowersResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class ServerFacadeTest {

    private ServerFacade serverFacade;
    private RegisterRequest registerRequest;
    private FollowersRequest followersRequest;
    private FollowersCountRequest followersCountRequest;

    private RegisterResponse expectedRegisterResponse;
    private FollowersResponse expectedFollowersResponse;
    private FollowersCountResponse expectedFollowersCountResponse;

    @BeforeEach
    public void setup(){
        serverFacade = new ServerFacade();
        registerRequest = new RegisterRequest("username", "pass", "Keiffer", "da Silva", "1234");
        followersRequest = new FollowersRequest(new AuthToken(), "@keiffer", null, 40);
        followersCountRequest = new FollowersCountRequest(new AuthToken(), "@keiffer");
    }

    @Test
    public void registerTest(){
        expectedRegisterResponse = new RegisterResponse(FakeData.getInstance().getFirstUser(), new AuthToken());
        try {
            RegisterResponse actualResponse = serverFacade.register(registerRequest, UserService.REGISTER_URL_PATH);
            Assertions.assertTrue(actualResponse.isSuccess());
            Assertions.assertEquals(expectedRegisterResponse.getMessage(), actualResponse.getMessage());
            Assertions.assertEquals(expectedRegisterResponse.getAuthToken().getToken(), actualResponse.getAuthToken().getToken());
            Assertions.assertEquals(expectedRegisterResponse.getUser(), actualResponse.getUser());
        } catch (IOException | TweeterRemoteException ex){
            throw new RuntimeException("Error registering");
        }
    }

    @Test
    public void getFollowersTest(){
        expectedFollowersResponse = new FollowersResponse(FakeData.getInstance().getFakeUsers(), false);
        try {
            FollowersResponse actualResponse = serverFacade.getFollowers(followersRequest, FollowService.GET_FOLLOWERS_URL_PATH);
            Assertions.assertTrue(actualResponse.isSuccess());
            Assertions.assertEquals(expectedFollowersResponse.getMessage(), actualResponse.getMessage());
            Assertions.assertEquals(expectedFollowersResponse.getFollowers(), actualResponse.getFollowers());
            Assertions.assertEquals(expectedFollowersResponse.getHasMorePages(), actualResponse.getHasMorePages());

            List<User> userList = FakeData.getInstance().getFakeUsers();
            List<User> newList = new ArrayList<>();
            for(int i = 0; i < 10; i++){
                newList.add(userList.get(i));
            }
            followersRequest.setLimit(10);
            FollowersResponse newResponse = serverFacade.getFollowers(followersRequest, FollowService.GET_FOLLOWERS_URL_PATH);
            Assertions.assertEquals(newList, newResponse.getFollowers());
            Assertions.assertTrue(newResponse.getHasMorePages());
        } catch (IOException | TweeterRemoteException ex){
            throw new RuntimeException("Error getting followers");
        }
    }

    @Test
    public void getFollowersCountTest(){
        expectedFollowersCountResponse = new FollowersCountResponse(20);
        try {
            FollowersCountResponse response = serverFacade.getFollowerCount(followersCountRequest, FollowService.GET_FOLLOWERS_COUNT_URL_PATH);
            Assertions.assertTrue(response.isSuccess());
            Assertions.assertEquals(expectedFollowersCountResponse.getFollowersCount(), response.getFollowersCount());
            Assertions.assertNull(response.getMessage());
        } catch (IOException | TweeterRemoteException ex){
            throw new RuntimeException("Error getting follower count");
        }
    }


}
