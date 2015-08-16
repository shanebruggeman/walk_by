package cloud_controller.user.crud_operations;

import android.os.AsyncTask;
import android.util.Log;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.WalkbyUserApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class UpdateUserAsyncTask extends AsyncTask<HashMap<Long,List<String>>, Void, Void> {
    private static WalkbyUserApi myApiService = null;
    private UserUpdateCallback callback;

    public UpdateUserAsyncTask(UserUpdateCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(HashMap<Long,List<String>>... params) {
        if(myApiService == null) {  // Only do this once
            WalkbyUserApi.Builder builder = new WalkbyUserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null)
                    .setRootUrl("https://walkby-1010.appspot.com/_ah/api/");

            myApiService = builder.build();
        }

        HashMap<Long,List<String>> updateInfo;
        for(int i = 0; i < params.length; i++) {
            updateInfo = params[i];

            Set<Long> allKeys = updateInfo.keySet();
            Iterator<Long> keyIterator = allKeys.iterator();
            Long userId = keyIterator.next();

            String username = updateInfo.get(userId).get(0);
            String password = updateInfo.get(userId).get(1);
            String macAddress = updateInfo.get(userId).get(2);

            try {
                Log.d(LoginActivity.DEBUG_KEY, "Updating username to " + username +
                        " and password to " + password + " and macAddress to " + macAddress);
//                myApiService.userUpdate(userId, username, password, macAddress).execute();
                if(false) {
                    throw new IOException("HMM");
                }
            } catch(IOException e) {
                Log.d(LoginActivity.DEBUG_KEY, "Update failed" + e.getMessage());
            }
        }

        return null;
    }
}