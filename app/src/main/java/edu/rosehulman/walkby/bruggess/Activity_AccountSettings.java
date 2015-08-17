package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;

import cloud_controller.user.crud_operations.UserGetByUsernameAsyncTask;
import cloud_controller.user.crud_operations.UserRetrievedCallback;


public class Activity_AccountSettings extends Activity implements UserRetrievedCallback {

    final Activity self = this;
    WalkbyUser user;

    Button userChangeButtton;
    Button passwordChangeButton;
    Button logoutButton;
    Button shareButton;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);

        username = getIntent().getStringExtra(LoginActivity.USERNAME_KEY).toString();

        userChangeButtton = (Button) findViewById(R.id.change_username_button);
        passwordChangeButton = (Button) findViewById(R.id.change_password_button);
        logoutButton = (Button) findViewById(R.id.logout_button);
        shareButton = (Button) findViewById(R.id.share_button);

        userChangeButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangeUsernameDialog();
            }
        });

        passwordChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePasswordDialog();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        (new UserGetByUsernameAsyncTask(this)).execute(username);
    }

    public void openChangeUsernameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Choose your new username");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                user.setUsername(input.getText().toString());
                updateUser(user);

                Toast.makeText(self, "Changing username to " + input.getText().toString(), Toast.LENGTH_LONG).show();

                Intent loginIntent = new Intent(self, LoginActivity.class);
                startActivity(loginIntent);

                finish();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alert.show();
    }

    public void openChangePasswordDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Choose your new password");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                user.setPassword(input.getText().toString());
                updateUser(user);
                Toast.makeText(self, "Changing password to " + input.getText().toString(), Toast.LENGTH_LONG).show();

                Intent loginIntent = new Intent(self, LoginActivity.class);
                startActivity(loginIntent);

                finish();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        alert.show();
    }

    public void logout() {
        Intent accountSettingsIntent = new Intent(this, LoginActivity.class);
        startActivity(accountSettingsIntent);
        finish();
    }

    public void share() {
        Intent shareIntent = new Intent(this, InviteFriendsActivity.class);
        shareIntent.putExtra(LoginActivity.USERNAME_KEY, username);
        startActivity(shareIntent);
        finish();
    }

    public void updateUser(WalkbyUser user) {
        //TODO: Async task goes here
        Log.d(LoginActivity.DEBUG_KEY, "<insert update to user here>");
    }

    @Override
    public void userHasBeenRetrieved(WalkbyUser user) {
        this.user = user;
    }
}
