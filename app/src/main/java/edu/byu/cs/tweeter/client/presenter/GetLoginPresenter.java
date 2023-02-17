package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetLoginPresenter extends Presenter {
    private final AuthView view;
    private final UserService userService;
    public GetLoginPresenter(AuthView view) {
        this.view = view;
        userService = new UserService();
    }

    public void login(String alias, String password) {
        userService.login(alias,password, new GetLoginObserver());
    }

    public class GetLoginObserver implements UserAuthObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to login: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to login due to exception: " + ex.getMessage());
        }
        @Override
        public void handleSuccess(User loggedInUser, AuthToken authToken) {
            view.startIntentActivity(loggedInUser,authToken);
        }

        @Override
        public void setErrorViewText(Exception e) {
            view.setErrorViewText(e);
        }
    }
}
