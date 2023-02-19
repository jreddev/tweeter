package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class AuthPresenter extends Presenter {
    protected AuthView view;
    protected final String type;
    protected final UserService userService;

    public AuthPresenter(AuthView view, String type) {
        this.type = type;
        this.view = view;
        this.userService = new UserService();
    }

    public class GetAuthObserver implements UserAuthObserver {

        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to " + type + ": " + message);
        }

        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to " + type + " due to exception: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(User user, AuthToken authToken) {
            view.startIntentActivity(user, authToken);
        }

        @Override
        public void setErrorViewText(Exception e) {
            view.setErrorViewText(e);
        }
    }
}
