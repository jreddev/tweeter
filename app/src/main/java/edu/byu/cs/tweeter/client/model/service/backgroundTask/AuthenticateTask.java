package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticateTask extends BackgroundTask{
    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private final String username;
    /**
     * The user's password.
     */
    private final String password;

    private User authenticatedUser;
    private AuthToken authToken;

    protected AuthenticateTask(String username, String password, Handler messageHandler) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void runTask() throws IOException {
        Pair<User, AuthToken> authResult = runAuthenticationTask();
        authenticatedUser = authResult.getFirst();
        authToken = authResult.getSecond();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, authenticatedUser);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, authToken);
    }

    protected abstract Pair<User, AuthToken> runAuthenticationTask();
}
