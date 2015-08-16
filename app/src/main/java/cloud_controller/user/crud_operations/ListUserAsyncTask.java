package cloud_controller.user.crud_operations;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUserCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class ListUserAsyncTask extends AsyncTask<Void, Void, WalkbyUserCollection> {
    private static WalkbyUserApi myApiService = null;

    @Override
    protected WalkbyUserCollection doInBackground(Void... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        try {
            return myApiService.list().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(WalkbyUserCollection collection) {
        Log.d(LoginActivity.DEBUG_KEY, "" + collection.size());
    }
}