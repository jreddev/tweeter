package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetLoginPresenter {

    public interface View {

        void displayMessage(String message);

        void startIntentActivity(User loggedInUser, AuthToken authToken);
    }

    private View view;
    private LoginService loginService;
    public GetLoginPresenter(View view) {
        this.view = view;
        loginService = new LoginService();
    }

    public void onClick(String alias, String password) {
        loginService.onClick(alias,password, new GetLoginObserver());
    }

    public class GetLoginObserver implements LoginService.Observer {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void startIntentActivity(User loggedInUser, AuthToken authToken) {
            view.startIntentActivity(loggedInUser,authToken);
        }
    }
}
