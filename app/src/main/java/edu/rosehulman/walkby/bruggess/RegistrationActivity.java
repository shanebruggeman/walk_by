package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegistrationActivity extends Activity implements View.OnClickListener {

    private EditText mEmailField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        Button registrationButton = (Button) findViewById(R.id.registration_button);
        registrationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.registration_button:
                Intent authenticationIntent = new Intent(this, AuthenticationActivity.class);
                startActivity(authenticationIntent);
                break;
            default:
                break;
        }
    }
}
