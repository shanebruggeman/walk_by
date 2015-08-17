package cloud_controller.conversation;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyMessage;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyMessageCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class ConversationGetUserConversationMessagesAsyncTask extends AsyncTask<Long, Void, WalkbyMessageCollection> {
    private static WalkbyUserApi myApiService = null;
    private MessagesRetrievedCallback callback;

    public ConversationGetUserConversationMessagesAsyncTask(MessagesRetrievedCallback callback) {
        this.callback = callback;
    }

    @Override
    protected WalkbyMessageCollection doInBackground(Long... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        Log.d(LoginActivity.DEBUG_KEY, "Sent id " + params[0] + " to retrieve messages for conversation");
        Long conversationId = params[0];

        try {
            return myApiService.getConversationMessages(conversationId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(WalkbyMessageCollection collection) {
        boolean isCollectionNull = (collection == null);

        List<WalkbyMessage> messages = new ArrayList<WalkbyMessage>();

        if(isCollectionNull) {
            Log.d(LoginActivity.DEBUG_KEY, "Message Collection returned is null: " + isCollectionNull);
            callback.messagesRetrieved(messages);
            return;
        }

        for(int i = 0; i < collection.size(); i++) {
            WalkbyMessage nextMessage = (WalkbyMessage) collection.get(i);
            messages.add(nextMessage);
        }

        Log.d(LoginActivity.USERNAME_KEY, "Sending messages back to callback!");
        callback.messagesRetrieved(messages);
    }
}