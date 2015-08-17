package edu.rosehulman.walkby.bruggess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.countrypicker.CountryPicker;
import com.countrypicker.CountryPickerListener;

import java.util.Locale;

import cloud_controller.user.authentication.CheckUsernameAvailableAsyncTask;
import cloud_controller.user.authentication.UserAuthenticationCallback;
import cloud_controller.user.crud_operations.InsertUserAsyncTask;


public class RegistrationActivity extends FragmentActivity implements View.OnClickListener, UserAuthenticationCallback {

    private EditText mEmailField;
    private EditText mPasswordField;

    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        Button registrationButton = (Button) findViewById(R.id.registration_button);
        mEmailField = (EditText) findViewById(R.id.register_email);
        mPasswordField = (EditText) findViewById(R.id.register_password);

        registrationButton.setOnClickListener(this);

        //default to the current locale
        country = Locale.getDefault().getCountry().toString();
        Log.d(LoginActivity.DEBUG_KEY, "Set Country to " + country);

        //load picker library for nice country choice. But if they leave the dialog, they'll
        //still just get the default locale
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getSupportFragmentManager(), "CountryPicker");

        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String countryName, String countryCode) {
                country = countryName;
                Log.d(LoginActivity.DEBUG_KEY, "Set Country to " + country);
                picker.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.registration_button:
                Log.d(LoginActivity.DEBUG_KEY, "Starting registration in registration activity");
                register();
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
        String macAddress = null; //macAddress starts empty until user uses bluetooth
        String country  = this.country;
        (new InsertUserAsyncTask()).execute(username, password, macAddress, country);

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
