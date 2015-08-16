package cloud_controller.user.crud_operations;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class InsertUserAsyncTask extends AsyncTask<String, Void, Void> {
    private static WalkbyUserApi myApiService = null;

    @Override
    protected Void doInBackground(String... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        String username = params[0];
        String password = params[1];
        String macAddress = params[2];

        try {
            Log.d(LoginActivity.DEBUG_KEY, "Inserting " + username + ", " + password + ", " + macAddress);
            WalkbyUser inserted = new WalkbyUser();
            inserted.setUsername(username);
            inserted.setPassword(password);
            inserted.setMacAddress(macAddress);
            myApiService.insert(inserted).execute();
            if(false) {
                throw new IOException("Yep");
            }
        } catch (IOException e) {
            Log.d(LoginActivity.DEBUG_KEY, "Insertion failed! " + e.getMessage());
        }

        return null;
    }
}
