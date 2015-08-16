package edu.rosehulman.walkby.bruggess;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.io.IOException;
import java.util.ArrayList;

import cloud_controller.user.authentication.AuthenticateUserAsyncTask;
import cloud_controller.user.authentication.UserAuthenticationCallback;
import cloud_controller.user.crud_operations.ListUserAsyncTask;
import cloud_controller.user.crud_operations.UserAddMacAddressAsyncTask;
import cloud_controller.user.crud_operations.UserListCallback;

public class LoginActivity extends Activity implements View.OnClickListener, UserAuthenticationCallback, UserListCallback {

    public static String DEBUG_KEY = "log1";
    public static String USERNAME_KEY = "USERNAME_KEY";

    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mGoogleSignInButton;
    private Button mLoginButton;
    private Button mRegistrationButton;

    String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    String mEmail; // Received from newChooseAccountIntent(); passed to getToken()

    GoogleCloudMessaging gcm;
    String regId;
    String PROJECT_NUMBER = "701882011679";

    GoogleAccountCredential credential;
    boolean isOnline = false;

    public void getRegistrationId() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if(gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regId = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regId;
                } catch(IOException ex) {
                    msg = "Error: " + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mUsernameField = (EditText) findViewById(R.id.login_username_input);
        mPasswordField = (EditText) findViewById(R.id.login_password_input);

        mGoogleSignInButton = (Button) findViewById(R.id.google_login_button);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mRegistrationButton = (Button) findViewById(R.id.login_registration_button);

        mGoogleSignInButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mRegistrationButton.setOnClickListener(this);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            isOnline = true;
        } else {
            Toast.makeText(this, "No Internet Connection Available", Toast.LENGTH_LONG).show();
        }

        (new ListUserAsyncTask(this)).execute();

//        getRegistrationId();
//        pickUserAccount(); //for authenticating with google
    }

    private void authenticateLogin() {
        Log.d(DEBUG_KEY, "Authorization granted");
        Intent navigationIntent = new Intent(this, NavigationActivity.class);
        navigationIntent.putExtra(USERNAME_KEY, getUsernameField());

        startActivity(navigationIntent);
    }

    private void denyLogin() {
        Log.d(DEBUG_KEY, "Authorization not granted");
        Toast.makeText(this,"Invalid Login Credentials", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_button:
                String username = getUsernameField();
                String password = getPasswordField();
                Log.d(DEBUG_KEY, "Authenticating " + username);
                (new AuthenticateUserAsyncTask(this)).execute(username,password);
                break;
            case R.id.login_registration_button:
                Log.d(DEBUG_KEY, "Starting registration");
                Intent registrationIntent = new Intent(this, RegistrationActivity.class);
                startActivity(registrationIntent);
                break;
            case R.id.google_login_button:
                String fakeLong = "5634472569470976";
                String fakeMac  = "fakeMac";

                (new UserAddMacAddressAsyncTask()).execute(fakeLong, fakeMac);
                break;
            default:
                break;
        }
    }

    @Override
    public void finishAuth(Boolean response) {
        if(response) {
            authenticateLogin();
        } else {
            denyLogin();
        }
    }


    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.d(DEBUG_KEY, "the email is " + mEmail);

                // With the account name acquired, go get the auth token
                getUsername();
            } else if (resultCode == RESULT_CANCELED) {
                // The account picker dialog closed without selecting an account.
                // Notify users that they must pick an account to proceed.
                Toast.makeText(this, "Choose Account to Proceed", Toast.LENGTH_SHORT).show();
                pickUserAccount();
            }
        }
    }

    /**
     * Attempts to retrieve the username.
     * If the account is not yet known, invoke the picker. Once the account is known,
     * start an instance of the AsyncTask to get the auth token and do work with it.
     */
    private void getUsername() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            if (isOnline) {
                Log.d(DEBUG_KEY,"You are online");
                new GetUsernameTask(LoginActivity.this, mEmail, SCOPE).execute();
            } else {
                Toast.makeText(this, "You are not online!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void userListCallback(ArrayList<WalkbyUser> walkbyUsers) {
        Log.d(LoginActivity.DEBUG_KEY, "Size of walkbyUsers is " + walkbyUsers.size());
    }

    public class GetUsernameTask extends AsyncTask {
        Activity mActivity;
        String mScope;
        String mEmail;

        GetUsernameTask(Activity activity, String name, String scope) {
            this.mActivity = activity;
            this.mScope = scope;
            this.mEmail = name;
        }

        /**
         * Executes the asynchronous job. This runs when you call execute()
         * on the AsyncTask instance.
         */
        @Override
        protected Void doInBackground(Object... params) {
            try {
                String token = fetchToken();
                if (token != null) {
                    // **Insert the good stuff here.**
                    // Use the token to access the user's Google data.
                    Log.d(DEBUG_KEY,"token is " + token);

                }
            } catch (IOException e) {
                // The fetchToken() method handles Google-specific exceptions,
                // so this indicates something went wrong at a higher level.
                // TIP: Check for network connectivity before starting the AsyncTask.
                Log.d(DEBUG_KEY,"Something on the google side went wrong");
            }
            return null;
        }

        /**
         * Gets an authentication token from Google and handles any
         * GoogleAuthException that may occur.
         */
        protected String fetchToken() throws IOException {
            try {
                return GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
            } catch (UserRecoverableAuthException userRecoverableException) {
                // GooglePlayServices.apk is either old, disabled, or not present
                // so we need to show the user some UI in the activity to recover.
                System.out.println("GooglePlayServices.apk is either old, disabled, or not present");
            } catch (GoogleAuthException fatalException) {
                // Some other type of unrecoverable exception has occurred.
                // Report and log the error as appropriate for your app.
                System.out.println("Something terrible has happened");
            }
            return null;
        }
    }

    public String getUsernameField() {
        return mUsernameField.getText().toString();
    }

    public String getPasswordField() {
        return mPasswordField.getText().toString();
    }
}
