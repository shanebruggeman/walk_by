package edu.rosehulman.walkby.bruggess.conversation_components;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.rosehulman.walkby.bruggess.R;

public class ConversationView extends LinearLayout {

    private TextView lastMessageText;
    private TextView otherUserText;

    public ConversationView(Context context) {
        super(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        inflater.inflate(R.layout.conversation_list_item, this);

        this.lastMessageText = (TextView) findViewById(R.id.last_message_text);
        this.otherUserText = (TextView) findViewById(R.id.other_username);
    }

    public void setLastMessageText(String text) {
        lastMessageText.setText(text);
    }

    public void setOtherUserText(String text) {
        otherUserText.setText(text);
    }
}
