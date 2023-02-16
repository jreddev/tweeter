package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import edu.byu.cs.tweeter.util.FakeData;

public abstract class BackgroundTask implements Runnable {
    private static final String LOG_TAG = "BackgroundTask";

    public static final String SUCCESS_KEY = "success";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";
    public static final String COUNT_KEY = "count";
    public static final String IS_FOLLOWER_KEY = "is-follower";
    public static final String MORE_PAGES_KEY = "more-pages";
    public static final String ITEMS_KEY = "items";

    /**
     * Message handler that will receive task results.
     */
    private final Handler messageHandler;

    protected BackgroundTask(Handler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            runTask();
            sendSuccessMessage();
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            sendExceptionMessage(ex);
        }
    }

    protected void runTask() throws IOException {
        //Override if used
    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }

    protected void loadSuccessBundle(Bundle msgBundle) {
        //Override if used
    }

    protected void sendSuccessMessage() {
        Bundle msgBundle = createBundle(true);
        loadSuccessBundle(msgBundle);
        sendBundle(msgBundle);
    }

    protected void sendFailedMessage(String message) {
        Bundle msgBundle = createBundle(false);
        msgBundle.putString(MESSAGE_KEY, message);
        sendBundle(msgBundle);
    }

    protected void sendExceptionMessage(Exception exception) {
        Bundle msgBundle = createBundle(false);
        msgBundle.putSerializable(EXCEPTION_KEY, exception);
        sendBundle(msgBundle);
    }

    protected void sendBundle(Bundle msgBundle) {
        Message msg = Message.obtain();
        msg.setData(msgBundle);
        messageHandler.sendMessage(msg);
    }

    @NonNull
    protected Bundle createBundle(boolean value) {
        Bundle msgBundle = new Bundle();
        msgBundle.putBoolean(SUCCESS_KEY, value);
        return msgBundle;
    }
}
