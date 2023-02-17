package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class Presenter {
    public interface DefaultView {
        void displayMessage(String message);
    }
    public interface ItemView<T> extends DefaultView {
        void startIntentActivity(User user);
        void addItems(List<T> items);
        void setLoadingFooter(boolean isLoading);
    }
    public interface AuthView extends DefaultView {
        void startIntentActivity(User registeredUser, AuthToken authToken);
        void setErrorViewText(Exception e);
    }
}
