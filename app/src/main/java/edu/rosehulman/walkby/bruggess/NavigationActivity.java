package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;


public class NavigationActivity extends Activity implements View.OnClickListener {

    Button recentlyPassedButton;
    Button achievementsListButton;
    Button messagesButton;
    Button inviteFriendsButton;
    Button accountSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

//        MapFragment mMapFragment = MapFragment.newInstance();MapFragment
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.map_container, mMapFragment);
//        fragmentTransaction.commit();

        recentlyPassedButton = (Button) findViewById(R.id.navigation_recently_passed_button);
        achievementsListButton = (Button) findViewById(R.id.navigation_achievements_list_button);
        messagesButton = (Button) findViewById(R.id.navigation_messages_button);
        inviteFriendsButton = (Button) findViewById(R.id.navigation_invite_friends_button);
        accountSettingsButton = (Button) findViewById(R.id.navigation_account_settings_button);

        recentlyPassedButton.setOnClickListener(this);
        achievementsListButton.setOnClickListener(this);
        messagesButton.setOnClickListener(this);
        inviteFriendsButton.setOnClickListener(this);
        accountSettingsButton.setOnClickListener(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        WalkbyBluetoothManager bluetoothManager = new WalkbyBluetoothManager(adapter);
        bluetoothManager.connect(this);
    }

    @Override
    public void onClick(View v) {
        int test = 0;
        switch(v.getId()) {
            case R.id.navigation_recently_passed_button:
                Intent recentlyPassedIntent = new Intent(this, RecentlyPassedActivity.class);
                startActivity(recentlyPassedIntent);
                break;
            case R.id.navigation_achievements_list_button:
                Intent achievementsIntent = new Intent(this, AchievementsListActivity.class);
                startActivity(achievementsIntent);
                break;
            case R.id.navigation_messages_button:
                Intent messagesIntent = new Intent(this, MessagesActivity.class);
                startActivity(messagesIntent);
                break;
            case R.id.navigation_invite_friends_button:
                Intent inviteFriendsIntent = new Intent(this, InviteFriendsActivity.class);
                startActivity(inviteFriendsIntent);
                break;
            case R.id.navigation_account_settings_button:
                Intent accountSettingsIntent = new Intent(this, AccountSettingsActivity.class);
                startActivity(accountSettingsIntent);
                break;
            default:
                break;
        }
    }
}
