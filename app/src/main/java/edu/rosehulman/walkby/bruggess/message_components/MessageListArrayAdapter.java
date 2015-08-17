package edu.rosehulman.walkby.bruggess.message_components;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyMessage;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;

import java.util.List;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class MessageListArrayAdapter extends BaseAdapter {

    private List<WalkbyMessage> allMessages;
    private Context context;
    private int count = 0;
    private WalkbyUser user;

    public MessageListArrayAdapter(Context context, List<WalkbyMessage> messages, WalkbyUser user) {
        Log.d(LoginActivity.DEBUG_KEY, "Creating messages adapter");
        allMessages = messages;
        this.context = context;
        this.user = user;
    }

    @Override
    public int getCount() {
        return allMessages.size();
    }

    @Override
    public Object getItem(int position) {
        return allMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageView view;
        if (convertView == null) {
            view = new MessageView(this.context);
        } else {
            view = (MessageView) convertView;
        }

        count++;

        view.setDescription("Nonsense Description" + count);
        return view;
    }
}
