package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenterTest {

    private MainPresenter.MainView mockMainView;

    private StatusService mockStatusService;

    private Cache mockCache;

    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup(){
        //Create mocks
        mockMainView = Mockito.mock(MainPresenter.MainView.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockMainView));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);
        Cache.setInstance(mockCache);
        Mockito.when(mockCache.getCurrUser()).thenReturn(new User("John", "Doe", "@John", "test"));
    }

    @Test
    public void testCreateNewStatus_postSuccess(){
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                verifyParameters(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3));
                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                observer.handleSuccess();
                return null;
            }
        };
        addAnswerCreateStatusAndVerifyMessage(answer, "Successfully Posted!");
        Mockito.verify(mockMainView).postMessageSuccess();

    }

    private void addAnswerCreateStatusAndVerifyMessage(Answer<Void> answer, String message) {
        Mockito.doAnswer(answer).when(mockStatusService).postNewStatus(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        mainPresenterSpy.createNewStatus("");
        Mockito.verify(mockMainView).displayMessage("Posting Status...");
        Mockito.verify(mockMainView).displayMessage(message);
    }


    @Test
    public void testCreateNewStatus_postFailure(){
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                verifyParameters(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3));
                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                observer.handleFailure("Error message");
                return null;
            }
        };

        addAnswerCreateStatusAndVerifyMessage(answer, "Failed to post status: Error message");
    }

    private void verifyParameters(Object argument, Object argument1, Object argument2, Object argument3) {
        Assertions.assertEquals(argument.getClass(), String.class);
        Assertions.assertEquals("", (String)argument);
        Assertions.assertTrue(argument1 instanceof SimpleNotificationObserver);
        Assertions.assertTrue(argument2 instanceof List);
        Assertions.assertTrue(argument3 instanceof List);
    }

    @Test
    public void testCreateNewStatus_postException(){
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                verifyParameters(invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2), invocation.getArgument(3));
                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                observer.handleException(new Exception("Exception message"));
                return null;
            }
        };

        addAnswerCreateStatusAndVerifyMessage(answer, "Failed to post status because of exception: Exception message");
    }
}
