package cloud_controller.conversation;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyMessage;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class MessageInsertAsyncTask extends AsyncTask<WalkbyMessage, Void, Void> {
    private static WalkbyUserApi myApiService = null;

    @Override
    protected Void doInBackground(WalkbyMessage... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        WalkbyMessage inserted = params[0];

        try {
            Log.d(LoginActivity.DEBUG_KEY, "INSERTING MESSAGE");
            myApiService.insertMessage(inserted).execute();
        } catch (IOException e) {
            Log.d(LoginActivity.DEBUG_KEY,"FAILED TO INSERT MESSAGE");
            e.printStackTrace();
        }

        return null;
    }
}