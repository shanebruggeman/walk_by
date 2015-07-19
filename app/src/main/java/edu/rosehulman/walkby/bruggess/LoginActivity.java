package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class LoginActivity extends Activity implements View.OnClickListener{

    private EditText mUsernameField;
    private EditText mPasswordField;
    private Button mGoogleSignInButton;
    private Button mLoginButton;
    private Button mRegistrationButton;

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
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.login_button:
                Intent loginIntent = new Intent(this, NavigationActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.login_registration_button:
                Intent registrationIntent = new Intent(this, RegistrationActivity.class);
                startActivity(registrationIntent);
                break;
            case R.id.google_login_button:
                Toast.makeText(this, "Gooogle Login is coming soon!", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
