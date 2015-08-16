package cloud_controller.user.crud_operations;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class DeleteUserAsyncTask extends AsyncTask<Long, Void, Void> {
    private static WalkbyUserApi myApiService = null;

    @Override
    protected Void doInBackground(Long... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        Long uid = params[0];

        try {
//            myApiService.userDelete(uid).execute();
            if(false) {
                throw new IOException("Ha");
            }
        } catch (IOException e) {
            Log.d(LoginActivity.DEBUG_KEY, "Deletion failed! " + e.getMessage());
        }

        return null;
    }
}