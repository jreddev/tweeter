package edu.byu.cs.tweeter.client.presenter;

import android.graphics.drawable.Drawable;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class GetRegisterPresenter extends AuthPresenter {
    public GetRegisterPresenter(AuthView view, String type){
        super(view, type);
    }

    public void Register(String firstName, String lastName, String alias, String password, Drawable image) {
        userService.Register(firstName, lastName, alias, password, image, new GetAuthObserver());
    }
}
