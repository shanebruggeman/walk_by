package cloud_controller.user.crud_operations;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class UserGetByUsernameAsyncTask extends AsyncTask<String, Void, WalkbyUser> {
    private static WalkbyUserApi myApiService = null;
    private UserRetrievedCallback callback;

    public UserGetByUsernameAsyncTask(UserRetrievedCallback callback) {
        this.callback = callback;
    }

    @Override
    protected WalkbyUser doInBackground(String... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        String username = params[0];

        try {
            return myApiService.userGetByUsername(username).execute();
        } catch (IOException e) {
            Log.d(LoginActivity.DEBUG_KEY, "Inserting mac address failed! " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(WalkbyUser user) {
        callback.userHasBeenRetrieved(user);
    }
}
