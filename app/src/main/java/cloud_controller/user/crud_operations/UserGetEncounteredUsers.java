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
import java.util.List;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class UserGetEncounteredUsers extends AsyncTask<String, Void, WalkbyUserCollection> {
    private static WalkbyUserApi myApiService = null;
    private UserEncounteredUsersCallback callback;

    public UserGetEncounteredUsers(UserEncounteredUsersCallback callback) {
        this.callback = callback;
    }

    @Override
    protected WalkbyUserCollection doInBackground(String... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        String username = params[0];

        try {
            return myApiService.getEncounteredUsers(username).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(WalkbyUserCollection collection) {
        boolean isCollectionNull = collection == null;
        List<WalkbyUser> encountered = new ArrayList<WalkbyUser>();

        if(isCollectionNull) {
            callback.userEncounteredUsersCallback(encountered);
        }

        Collection users = collection.values();
        Iterator userIterator = users.iterator();
        ArrayList next = (ArrayList) userIterator.next();

        for(int i = 0; i < next.size(); i++) {
            WalkbyUser nextUser = (WalkbyUser) next.get(i);
            Log.d(LoginActivity.DEBUG_KEY, "Encountered " + nextUser.getUsername());
            encountered.add(nextUser);
        }

        callback.userEncounteredUsersCallback(encountered);
    }
}