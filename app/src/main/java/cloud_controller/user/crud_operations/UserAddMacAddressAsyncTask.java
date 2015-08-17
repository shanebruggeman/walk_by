package cloud_controller.user.crud_operations;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class UserAddMacAddressAsyncTask extends AsyncTask<String, Void, Void> {
    private static WalkbyUserApi myApiService = null;

    @Override
    protected Void doInBackground(String... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        String username = params[0];
        String macAddress = params[1];

        try {
            Log.d(LoginActivity.DEBUG_KEY, "Calling endpoint to add encountered mac address for user " + username + ": " + macAddress);
            Log.d(LoginActivity.DEBUG_KEY, "myApi is null: " + (myApiService == null));
            myApiService.addEncounteredMacAddress(username, macAddress).execute();
        } catch (IOException e) {
            Log.d(LoginActivity.DEBUG_KEY, "Inserting mac address failed! " + e.getMessage());
        }

        return null;
    }
}