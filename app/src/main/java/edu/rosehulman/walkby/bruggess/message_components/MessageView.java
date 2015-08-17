package edu.rosehulman.walkby.bruggess.message_components;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.rosehulman.walkby.bruggess.R;

public class MessageView extends LinearLayout {

    TextView messageTextShow;

    public MessageView(Context context) {
        super(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        inflater.inflate(R.layout.message_list_item, this);

        this.messageTextShow = (TextView) findViewById(R.id.message);
    }

    public void setDescription(String description) {
        this.messageTextShow.setText(description);
    }
}
