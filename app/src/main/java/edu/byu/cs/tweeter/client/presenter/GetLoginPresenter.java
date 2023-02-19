package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetLoginPresenter extends AuthPresenter {

    public GetLoginPresenter(AuthView view, String type) {
        super(view, type);
    }

    public void login(String alias, String password) {
        userService.login(alias,password, new GetAuthObserver());
    }
}
