package edu.byu.cs.tweeter.client.model.service.backgroundTask;

public abstract class AuthenticateTask extends BackgroundTask{
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    protected String username;
    /**
     * The user's password.
     */
    protected String password;
}
