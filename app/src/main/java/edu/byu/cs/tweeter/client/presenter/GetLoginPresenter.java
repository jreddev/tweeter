package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetLoginPresenter {

    public interface View {

        void displayMessage(String message);

        void startIntentActivity(User loggedInUser, AuthToken authToken);

        void setErrorViewText(Exception e);
    }

    private View view;
    private UserService userService;
    public GetLoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void login(String alias, String password) {
        userService.login(alias,password, new GetLoginObserver());
    }

    public class GetLoginObserver implements UserService.AuthObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }
        @Override
        public void handleException(Exception ex, String message) {
            //DOES NOTHING HERE
        }
        @Override
        public void startIntentActivity(User loggedInUser, AuthToken authToken) {
            view.startIntentActivity(loggedInUser,authToken);
        }

        @Override
        public void setErrorViewText(Exception e) {
            view.setErrorViewText(e);
        }
    }
}
