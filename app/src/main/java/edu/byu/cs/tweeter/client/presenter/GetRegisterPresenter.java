package edu.byu.cs.tweeter.client.presenter;

import android.graphics.drawable.Drawable;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetRegisterPresenter {

    public interface View {

        void displayMessage(String message);

        void startIntentActivity(User registeredUser, AuthToken authToken);

        void setErrorViewText(Exception e);
    }

    private View view;
    private UserService userService;
    public GetRegisterPresenter(View view){
        this.view = view;
        userService = new UserService();
    }

    public void Register(String firstName, String lastName, String alias, String password, Drawable image) {
        userService.Register(firstName, lastName, alias, password, image, new GetRegisterObserver());
    }

    public class GetRegisterObserver implements UserService.AuthObserver {
        @Override
        public void handleFailure(String message) {
            view.displayMessage(message);
        }
        @Override
        public void handleException(Exception ex, String message) {
            view.displayMessage(message + ex.getMessage());
        }
        @Override
        public void startIntentActivity(User registeredUser, AuthToken authToken) {
            view.startIntentActivity(registeredUser, authToken);
        }

        @Override
        public void setErrorViewText(Exception e) {
            view.setErrorViewText(e);
        }
    }
}
