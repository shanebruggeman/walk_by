package cloud_controller.conversation;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversation;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class ConversationInsertAsyncTask extends AsyncTask<WalkbyConversation, Void, WalkbyConversation> {
    private static WalkbyUserApi myApiService = null;
    private ConversationInsertionCallback callback;

    public ConversationInsertAsyncTask(ConversationInsertionCallback callback) {
        this.callback = callback;
    }

    @Override
    protected WalkbyConversation doInBackground(WalkbyConversation... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }


        try {
            Log.d(LoginActivity.DEBUG_KEY, "INSERTING CONVERSATION");
            return myApiService.insertConversation(params[0]).execute();
        } catch (IOException e) {
            Log.d(LoginActivity.DEBUG_KEY,"FAILED TO INSERT CONVERSATION");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(WalkbyConversation inserted) {
        callback.onConversationInserted(inserted);
    }
}