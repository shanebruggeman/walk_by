package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

import java.util.ArrayList;

import cloud_controller.user.authentication.AuthenticateUserAsyncTask;
import cloud_controller.user.authentication.UserAuthenticationCallback;
import cloud_controller.user.crud_operations.UserListCallback;

public class LoginActivity extends Activity implements View.OnClickListener, UserAuthenticationCallback, UserListCallback {

    public static String DEBUG_KEY = "log1";
    public static String USERNAME_KEY = "USERNAME_KEY";

    private EditText mUsernameField;
    private EditText mPasswordField;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mUsernameField = (EditText) findViewById(R.id.login_username_input);
        mPasswordField = (EditText) findViewById(R.id.login_password_input);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mRegistrationButton = (Button) findViewById(R.id.login_registration_button);

        mLoginButton.setOnClickListener(this);
        mRegistrationButton.setOnClickListener(this);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            isOnline = true;
        } else {
            Toast.makeText(this, "No Internet Connection Available", Toast.LENGTH_LONG).show();
        }

    }

    private void authenticateLogin() {
        Log.d(DEBUG_KEY, "Authorization granted");
        Intent navigationIntent = new Intent(this, Activity_Navigation.class);
        navigationIntent.putExtra(USERNAME_KEY, getUsernameField());

        Log.d(DEBUG_KEY, "Passed username " + getUsernameField() + " to navigation");
        startActivity(navigationIntent);
        finish();
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
                Log.d(DEBUG_KEY, "Authenticating " + username + " with password " + password);
                (new AuthenticateUserAsyncTask(this)).execute(username,password);
                break;
            case R.id.login_registration_button:
                Log.d(DEBUG_KEY, "Starting registration");
                Intent registrationIntent = new Intent(this, RegistrationActivity.class);
                startActivity(registrationIntent);
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

    @Override
    public void userListCallback(ArrayList<WalkbyUser> walkbyUsers) {
        Log.d(LoginActivity.DEBUG_KEY, "Size of walkbyUsers is " + walkbyUsers.size());
    }

    public String getUsernameField() {
        return mUsernameField.getText().toString();
    }

    public String getPasswordField() {
        return mPasswordField.getText().toString();
    }
}
