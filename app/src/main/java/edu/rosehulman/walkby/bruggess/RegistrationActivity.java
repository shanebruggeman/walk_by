package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cloud_controller.user.authentication.CheckUsernameAvailableAsyncTask;
import cloud_controller.user.authentication.UserAuthenticationCallback;
import cloud_controller.user.crud_operations.InsertUserAsyncTask;


public class RegistrationActivity extends Activity implements View.OnClickListener, UserAuthenticationCallback {

    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        Button registrationButton = (Button) findViewById(R.id.registration_button);
        mEmailField = (EditText) findViewById(R.id.register_email);
        mPasswordField = (EditText) findViewById(R.id.register_password);

        registrationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.registration_button:
//                Intent authenticationIntent = new Intent(this, AuthenticationActivity.class);
//                startActivity(authenticationIntent);
                Log.d(LoginActivity.DEBUG_KEY, "Starting registration in registration activity");
                register();
                break;
            default:
                break;
        }
    }

    public void register() {
        String username = getUsername();
        String password = getPassword();

        Log.d(LoginActivity.DEBUG_KEY, "Registering " + username + " with pass: " + password);
        (new CheckUsernameAvailableAsyncTask(this)).execute(username);
    }

    private void createUser(String username, String password) {
        String macAddress = "unregistered"; //macAddress starts empty until user uses bluetooth
        (new InsertUserAsyncTask()).execute(username, password, macAddress);

        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
    }

    public String getUsername() {
        return mEmailField.getText().toString();
    }

    public String getPassword() {
        return mPasswordField.getText().toString();
    }

    @Override
    public void finishAuth(Boolean response) {
        if(response) {
            Log.d(LoginActivity.DEBUG_KEY, "Creating User");
            createUser(getUsername(), getPassword());
        } else {
            Toast.makeText(this, "That account name is unavailable", Toast.LENGTH_LONG);
        }
    }
}
