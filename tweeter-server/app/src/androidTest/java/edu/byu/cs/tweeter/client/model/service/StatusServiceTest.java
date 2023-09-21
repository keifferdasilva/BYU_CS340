package edu.byu.cs.tweeter.client.model.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.PagedTaskObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusServiceTest {

    private User currentUser;
    private AuthToken currentAuthToken;

    private StatusService statusServiceSpy;
    private StatusServiceObserver storyObserver;

    private CountDownLatch countDownLatch;


    @BeforeEach
    public void setup(){
        currentUser = new User("First Name", "Last Name", null);
        currentAuthToken = new AuthToken();

        statusServiceSpy = Mockito.spy(new StatusService());

        storyObserver = new StatusServiceObserver();
        Cache cache = Mockito.mock(Cache.class);
        Mockito.when(cache.getCurrUserAuthToken()).thenReturn(currentAuthToken);
        Mockito.when(statusServiceSpy.getCache()).thenReturn(cache);

        resetCountdownLatch();
    }

    private void resetCountdownLatch(){
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountdownLatch() throws InterruptedException{
        countDownLatch.await();
        resetCountdownLatch();
    }

    private class StatusServiceObserver implements PagedTaskObserver<Status>{

        private List<Status> story;
        private boolean hasMorePages;
        private boolean success;
        private String message;
        private Exception exception;



        @Override
        public void addItems(List<Status> items, boolean hasMorePages) {
            this.success = true;
            this.message = null;
            this.story = items;
            this.hasMorePages = hasMorePages;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleFailure(String message) {
            this.success = false;
            this.message = message;
            this.story = null;
            this.hasMorePages = false;
            this.exception = null;

            countDownLatch.countDown();
        }

        @Override
        public void handleException(Exception exception) {
            this.success = false;
            this.message = null;
            this.story = null;
            this.hasMorePages = false;
            this.exception = exception;

            countDownLatch.countDown();
        }

        @Override
        public void handleCleanup() {

        }

        public List<Status> getStory() {
            return story;
        }

        public boolean getHasMorePages() {
            return hasMorePages;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Exception getException() {
            return exception;
        }
    }

    @Test
    public void testGetStory() throws InterruptedException{
        statusServiceSpy.getStory(currentUser, null, storyObserver);
        awaitCountdownLatch();
        List<Status> expectedStory = FakeData.getInstance().getFakeStatuses().subList(0, 10);
        Assertions.assertTrue(storyObserver.isSuccess());
        Assertions.assertNull(storyObserver.getMessage());
        Assertions.assertEquals(expectedStory, storyObserver.getStory());
        Assertions.assertTrue(storyObserver.getHasMorePages());
        Assertions.assertNull(storyObserver.getException());
    }
}
