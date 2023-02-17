package edu.byu.cs.tweeter.client.model.service;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.UserAuthHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserAuthObserver;

public class UserService extends Service {
    public void getProfile(String userAlias, UserAuthObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new UserAuthHandler(observer, "get_user"));
        ExecuteTask(getUserTask);
    }

    public void login(String alias, String password, UserAuthObserver observer) {
        try {
            validateLogin(alias, password);
            observer.setErrorViewText(null);

            LoginTask loginTask = new LoginTask(alias, password, new UserAuthHandler(observer, "login"));
            ExecuteTask(loginTask);
        } catch (Exception e) {
            observer.setErrorViewText(e);
        }
    }

    public void Register(String firstName, String lastName, String alias, String password, Drawable image, UserAuthObserver observer) {
        try{
            validateRegistration(firstName, lastName, alias, password, image);
            observer.setErrorViewText(null);
            observer.handleFailure("Registering...");

            Bitmap image_map = ((BitmapDrawable) image).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image_map.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

            RegisterTask registerTask = new RegisterTask(firstName, lastName,
                    alias, password, imageBytesBase64, new UserAuthHandler(observer, "register"));
            ExecuteTask(registerTask);
        } catch (Exception e){
            observer.setErrorViewText(e);
        }

    }

    private void validateRegistration(String firstName, String lastName, String alias, String password, Drawable image) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (image == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    private void validateLogin(String alias, String password) {
        if (alias.length() > 0 && alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public void onOptionsItemSelected(SimpleNotificationObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new SimpleNotificationHandler(observer));
        ExecuteTask(logoutTask);
    }
}
