package edu.rosehulman.walkby.bruggess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class InviteFriendsActivity extends Activity {

    EditText userText;
    Button sendEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friends);

        userText = (EditText) findViewById(R.id.email_friend_edit_text);
        sendEmailButton = (Button) findViewById(R.id.email_friend_send_button);

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userText.getText().toString();

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{username});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Walkby is the coolest thing in town!");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "I recently started using walkby and wanted to share it with you. Register today!");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(InviteFriendsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
