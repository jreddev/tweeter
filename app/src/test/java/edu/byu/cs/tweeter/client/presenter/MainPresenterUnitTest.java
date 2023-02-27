package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.client.model.service.StatusService;

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
    public void testPostStatusTask_Successful() {

    }
    @Test
    public void testPostStatusTask_Failure() {

    }
    @Test
    public void testPostStatusTask_Exception() {

    }
}
