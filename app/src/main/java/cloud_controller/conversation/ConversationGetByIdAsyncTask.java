package cloud_controller.conversation;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversation;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class ConversationGetByIdAsyncTask extends AsyncTask<Long, Void, WalkbyConversation> {
    private static WalkbyUserApi myApiService = null;
    private ConversationRetrievedCallback callback;

    public ConversationGetByIdAsyncTask(ConversationRetrievedCallback callback) {
        this.callback = callback;
    }

    @Override
    protected WalkbyConversation doInBackground(Long... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        Log.d(LoginActivity.DEBUG_KEY, "Sent id " + params[0] + " to retrieve conversation");
        Long conversationId = params[0];

        try {
            return myApiService.getConversation(conversationId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(WalkbyConversation conversation) {
        callback.conversationRetrieved(conversation);
    }
}