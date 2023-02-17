package edu.byu.cs.tweeter.client.presenter;

import android.graphics.drawable.Drawable;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetRegisterPresenter extends Presenter {
    private final AuthView view;
    private final UserService userService;
    public GetRegisterPresenter(AuthView view){
        this.view = view;
        userService = new UserService();
    }

    public void Register(String firstName, String lastName, String alias, String password, Drawable image) {
        userService.Register(firstName, lastName, alias, password, image, new GetRegisterObserver());
    }

    public class GetRegisterObserver implements UserAuthObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage("Failed to register: " + message);
        }
        @Override
        public void handleException(Exception ex) {
            view.displayMessage("Failed to register due to exception: " + ex.getMessage());
        }
        @Override
        public void handleSuccess(User registeredUser, AuthToken authToken) {
            view.startIntentActivity(registeredUser, authToken);
        }

        @Override
        public void setErrorViewText(Exception e) {
            view.setErrorViewText(e);
        }
    }
}
