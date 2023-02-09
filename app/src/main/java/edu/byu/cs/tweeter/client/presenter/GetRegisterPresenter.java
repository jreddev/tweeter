package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetRegisterPresenter {

    public interface View {

        void displayMessage(String message);

        void startIntentActivity(User registeredUser, AuthToken authToken);
    }

    private View view;
    private UserService userService;
    public GetRegisterPresenter(View view){
        this.view = view;
        userService = new UserService();
    }

    public void onClick(String firstName, String lastName, String alias, String password, String imageBytesBase64) {
        userService.onClick(firstName, lastName, alias, password, imageBytesBase64, new GetRegisterObserver());
    }

    public class GetRegisterObserver implements UserService.Observer {
        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
        @Override
        public void displayException(Exception ex, String message) {
            view.displayMessage(message + ex.getMessage());
        }
        @Override
        public void startIntentActivity(User registeredUser, AuthToken authToken) {
            view.startIntentActivity(registeredUser, authToken);
        }
    }
}
