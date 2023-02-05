package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetLoginPresenter {

    public interface View {

        void displayMessage(String message);

        void startIntentActivity(User loggedInUser, AuthToken authToken);
    }

    private View view;
    private UserService userService;
    public GetLoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void onClick(String alias, String password) {
        userService.onClick(alias,password, new GetLoginObserver());
    }

    public class GetLoginObserver implements UserService.Observer {
        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
        @Override
        public void displayException(Exception ex, String message) {
            //DOES NOTHING HERE
        }
        @Override
        public void startActivity(User user) {
            //DOES NOTHING HERE
        }
        @Override
        public void startIntentActivity(User loggedInUser, AuthToken authToken) {
            view.startIntentActivity(loggedInUser,authToken);
        }
        @Override
        public void logout() {
        }
    }
}
