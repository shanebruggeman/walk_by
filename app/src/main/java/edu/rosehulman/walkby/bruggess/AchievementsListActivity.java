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


public class AchievementsListActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements_list);

        Button tempMapsButton = (Button) findViewById(R.id.tmp_navmap_button);
        tempMapsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tmp_navmap_button:
                Intent achievementMapIntent = new Intent(this, AchievementMapActivity.class);
                startActivity(achievementMapIntent);
                break;
        }
    }
}
