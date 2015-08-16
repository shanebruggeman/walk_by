package cloud_controller.user.crud_operations;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUserCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class ListUserAsyncTask extends AsyncTask<Void, Void, WalkbyUserCollection> {
    private static WalkbyUserApi myApiService = null;
    private UserListCallback callback;

    public ListUserAsyncTask(UserListCallback callback) {
        this.callback = callback;
    }

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
        Log.d(LoginActivity.DEBUG_KEY, "Size of the collection is " + collection.size());

        Collection users = collection.values();
        Iterator userIterator = users.iterator();
        ArrayList next = (ArrayList) userIterator.next();
        ArrayList<WalkbyUser> walkbyUsers = new ArrayList<WalkbyUser>();

        for(int i = 0; i < next.size(); i++) {
            WalkbyUser nextUser = (WalkbyUser) next.get(i);
            Log.d(LoginActivity.DEBUG_KEY, nextUser.getUsername());
            walkbyUsers.add(nextUser);
        }

        callback.userListCallback(walkbyUsers);
    }
}