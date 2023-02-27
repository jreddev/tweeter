package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;

public class MainPresenterUnitTest {
    private GetMainPresenter.View mockView;
    private StatusService mockStatusService;
    private GetMainPresenter mainPresenterSpy;


    @BeforeEach
    public void setup() {
        //Creating mocks
        mockView = Mockito.mock(GetMainPresenter.View.class);
        mockStatusService = Mockito.mock(StatusService.class);

        mainPresenterSpy = Mockito.spy(new GetMainPresenter(mockView));
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);
        //Mockito.doReturn(mockStatusService).when(mainPresenterSpy.getStatusService());
    }

    @Test
    public void testPostStatusTask_Success() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                Assertions.assertNotNull(observer);
                observer.handleSuccess();
                return null;
            }
        };
        verifyCommonPostStatusAnswer(answer, "Successfully Posted!");
    }
    @Test
    public void testPostStatusTask_Failure() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                Assertions.assertNotNull(observer);
                observer.handleFailure("post failure");
                return null;
            }
        };
        verifyCommonPostStatusAnswer(answer, "Failed to post status: post failure");
    }
    @Test
    public void testPostStatusTask_Exception() {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                SimpleNotificationObserver observer = invocation.getArgument(1, SimpleNotificationObserver.class);
                Assertions.assertNotNull(observer);
                observer.handleException(new Exception("post fail exception"));
                return null;
            }
        };
        verifyCommonPostStatusAnswer(answer, "Failed to post status because of exception: post fail exception");
    }

    public void verifyCommonPostStatusAnswer(Answer answer, String message) {
        Mockito.doAnswer(answer).when(mockStatusService).postStatusTask(Mockito.any(),Mockito.any());

        mainPresenterSpy.postStatusTask(Mockito.any());

        Mockito.verify(mockView).displayMessage("Posting Status...");
        Mockito.verify(mockView).cancelPostingToast();
        Mockito.verify(mockView).displayMessage(message);

    }
}
