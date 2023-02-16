package edu.byu.cs.tweeter.client.model.service;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.GetUserHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.handler.RegisterHandler;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public interface Observer {
        void handleFailure(String message);
        void handleException(Exception ex, String message);
        void startIntentActivity(User registeredUser, AuthToken authToken);

    }
    public interface MainObserver extends Observer {
        void logout();
    }

    public interface AuthObserver extends Observer{
        void setErrorViewText(Exception e);
    }

    public void getProfile(String userAlias, Observer observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void login(String alias, String password, AuthObserver observer) {
        try {
            validateLogin(alias, password);
            observer.setErrorViewText(null);

            LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(loginTask);
        } catch (Exception e) {
            observer.setErrorViewText(e);
        }
    }

    public void Register(String firstName, String lastName, String alias, String password, Drawable image, AuthObserver observer) {
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
                    alias, password, imageBytesBase64, new RegisterHandler(observer));
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(registerTask);
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

    public void onOptionsItemSelected(MainObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }
}
