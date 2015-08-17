package cloud_controller.conversation;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversation;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversationCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class ConversationGetUserConversationsAsyncTask extends AsyncTask<String, Void, WalkbyConversationCollection> {
    private static WalkbyUserApi myApiService = null;
    private ConversationsRetrievedCallback callback;

    public ConversationGetUserConversationsAsyncTask(ConversationsRetrievedCallback callback) {
        this.callback = callback;
    }

    @Override
    protected WalkbyConversationCollection doInBackground(String... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        String username = params[0];

        try {
            return myApiService.getUserConversations(username).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(WalkbyConversationCollection collection) {
        boolean isCollectionNull = (collection == null);

        List<WalkbyConversation> conversations = new ArrayList<WalkbyConversation>();

        if(isCollectionNull) {
            Log.d(LoginActivity.DEBUG_KEY, "Collection of user conversations returned is null: " + isCollectionNull);
            callback.conversationsRetrieved(conversations);
            return;
        }

        for(int i = 0; i < collection.size(); i++) {
            WalkbyConversation nextConversation = (WalkbyConversation) collection.get(i);
            conversations.add(nextConversation);
        }

        Log.d(LoginActivity.USERNAME_KEY, "Sending conversations back to callback!");
        callback.conversationsRetrieved(conversations);
    }
}
