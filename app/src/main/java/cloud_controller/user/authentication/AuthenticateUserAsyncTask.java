package cloud_controller.user.authentication;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class AuthenticateUserAsyncTask extends AsyncTask<String, Void, Boolean> {
    private static WalkbyUserApi myApiService = null;
    UserAuthenticationCallback callback = null;

    public AuthenticateUserAsyncTask(UserAuthenticationCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        String username = params[0];
        String password = params[1];

        try {
            Log.d(LoginActivity.DEBUG_KEY, "searching for user " + username + " with password " + password);
            boolean response = (myApiService.checkUsernamePassword(username, password).execute()) != null;
            return  response;
        } catch(IOException e) {
            Log.d(LoginActivity.DEBUG_KEY, "Update failed" + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        Log.d(LoginActivity.DEBUG_KEY, "Username and password accepted: " + response);
        callback.finishAuth(response);
    }
}