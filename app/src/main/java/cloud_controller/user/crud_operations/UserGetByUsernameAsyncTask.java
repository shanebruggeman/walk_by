package cloud_controller.user.crud_operations;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class UserGetByUsernameAsyncTask extends AsyncTask<String, Void, Long> {
    private static WalkbyUserApi myApiService = null;

    @Override
    protected Long doInBackground(String... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        String username = params[0];

        try {
            return myApiService.userGetByUsername(username).execute().getId();
        } catch (IOException e) {
            Log.d(LoginActivity.DEBUG_KEY, "Inserting mac address failed! " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        Log.d(LoginActivity.DEBUG_KEY, "User with given username had id of " + aLong);
    }
}
