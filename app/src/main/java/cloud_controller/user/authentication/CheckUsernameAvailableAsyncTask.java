package cloud_controller.user.authentication;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class CheckUsernameAvailableAsyncTask extends AsyncTask<String, Void, Boolean> {
    private static WalkbyUserApi myApiService = null;
    private UserAuthenticationCallback callback = null;

    public CheckUsernameAvailableAsyncTask(UserAuthenticationCallback callback) {
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

        try {
            Log.d(LoginActivity.DEBUG_KEY, "searching for user " + username);
//            return (myApiService.userExists(username).execute() == null);
            if(false) {
                throw new IOException("Yea");
            }
            return true;
        } catch(IOException e) {
            Log.d(LoginActivity.DEBUG_KEY, "Update failed" + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Boolean response) {
        Log.d(LoginActivity.DEBUG_KEY, "Response was " + response);
        callback.finishAuth(response);
    }
}
