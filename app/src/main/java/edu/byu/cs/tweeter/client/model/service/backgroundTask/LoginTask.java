package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    private static final String LOG_TAG = "LoginTask";

    public LoginTask(String username, String password, Handler messageHandler) {
        this.username = username;
        this.password = password;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            Pair<User, AuthToken> loginResult = doLogin();

            User loggedInUser = loginResult.getFirst();
            AuthToken authToken = loginResult.getSecond();

            sendSuccessMessage(loggedInUser, authToken);

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    private Pair<User, AuthToken> doLogin() {
        User loggedInUser = getFakeData().getFirstUser();
        AuthToken authToken = getFakeData().getAuthToken();
        return new Pair<>(loggedInUser, authToken);
    }

    private void sendSuccessMessage(User loggedInUser, AuthToken authToken) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(USER_KEY, loggedInUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);

        Message msg = Message.obtain();
        msg.setData(msgBundle);

        messageHandler.sendMessage(msg);
    }

}
