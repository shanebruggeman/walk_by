package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import edu.rosehulman.walkby.bruggess.conversation_components.Activity_Conversations;


public class Activity_Navigation extends Activity implements View.OnClickListener {

    private Button messagesButton;
    private Button inviteFriendsButton;
    private Button accountSettingsButton;

    private WalkbyBluetoothManager bluetoothManager;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        Log.d(LoginActivity.DEBUG_KEY, "Creating Navigation Activity");

        messagesButton = (Button) findViewById(R.id.navigation_messages_button);
        inviteFriendsButton = (Button) findViewById(R.id.navigation_invite_friends_button);
        accountSettingsButton = (Button) findViewById(R.id.navigation_account_settings_button);

        messagesButton.setOnClickListener(this);
        inviteFriendsButton.setOnClickListener(this);
        accountSettingsButton.setOnClickListener(this);

        Intent myIntent = getIntent();
        username = myIntent.getStringExtra(LoginActivity.USERNAME_KEY);

        Log.d(LoginActivity.DEBUG_KEY, "Navigation receives username " + username);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        bluetoothManager = new WalkbyBluetoothManager(adapter, username);
        bluetoothManager.connect(this);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                bluetoothManager.refresh();
            }
        };

        //continually refresh every ten seconds
        timer.schedule(timerTask, 0, 10000);
    }

    @Override
    public void onClick(View v) {
        int test = 0;
        switch(v.getId()) {
            case R.id.navigation_messages_button:
                Intent messagesIntent = new Intent(this, Activity_Conversations.class);
                messagesIntent.putExtra(LoginActivity.USERNAME_KEY, username);
                startActivity(messagesIntent);
                break;
            case R.id.navigation_invite_friends_button:
                Intent inviteFriendsIntent = new Intent(this, InviteFriendsActivity.class);
                startActivity(inviteFriendsIntent);
                break;
            case R.id.navigation_account_settings_button:
                Intent accountSettingsIntent = new Intent(this, Activity_AccountSettings.class);
                accountSettingsIntent.putExtra(LoginActivity.USERNAME_KEY, username);
                startActivity(accountSettingsIntent);
                break;
        }
    }
}
