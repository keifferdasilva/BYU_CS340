package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.net.ServerFacade;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.StoryRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public class StoryTest {

    private MainPresenter mainPresenter;
    private MainPresenter.MainView observer;
    private CountDownLatch countDownLatch;
    private ServerFacade facade;


    private void resetCountdownLatch(){
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountdownLatch() throws InterruptedException{
        countDownLatch.await();
        resetCountdownLatch();
    }


    @BeforeEach
    public void setup() throws IOException, TweeterRemoteException {
        resetCountdownLatch();
        facade = new ServerFacade();
        LoginRequest loginRequest = new LoginRequest("@joe", "pass");
        LoginResponse response = facade.login(loginRequest, UserService.LOGIN_URL_PATH);
        Cache.getInstance().setCurrUserAuthToken(response.getAuthToken());
        Cache.getInstance().setCurrUser(response.getUser());
        observer = Mockito.mock(MainPresenter.MainView.class);
        Mockito.doAnswer(invocation -> {
            countDownLatch.countDown();
            return null;
        }).when(observer).postMessageSuccess();
        mainPresenter = new MainPresenter(observer);

    }

    @Test
    public void postStatusTest() throws InterruptedException, IOException, TweeterRemoteException {
        String post = "Test post " + System.currentTimeMillis();
        resetCountdownLatch();
        mainPresenter.createNewStatus(post);
        awaitCountdownLatch();
        Mockito.verify(observer).displayMessage("Successfully Posted!");
        StoryRequest storyRequest = new StoryRequest("@joe", Cache.getInstance().getCurrUserAuthToken(), 20, null);
        List<Status> story = facade.getStory(storyRequest, StatusService.GET_STORY_URL_PATH).getStatuses();
        Status status = story.get(0);
        Assertions.assertEquals(post, status.getPost());
        Assertions.assertEquals(Cache.getInstance().getCurrUser(), status.getUser());
        Assertions.assertEquals(new ArrayList<String>(), status.getMentions());
        Assertions.assertEquals(new ArrayList<String>(), status.getUrls());
    }
}
