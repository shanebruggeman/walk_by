package edu.rosehulman.walkby.bruggess.conversation_components;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversation;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;

import java.util.List;

import edu.rosehulman.walkby.bruggess.LoginActivity;

public class ConversationListArrayAdapter extends BaseAdapter {

    private List<WalkbyConversation> allConversations;
    private Context context;
    private int count = 0;
    private WalkbyUser user;

    public ConversationListArrayAdapter(Context context, List<WalkbyConversation> conversations, WalkbyUser user) {
        Log.d(LoginActivity.DEBUG_KEY, "Creating conversation adapter");
        allConversations = conversations;
        this.context = context;
        this.user = user;
    }

    @Override
    public int getCount() {
        return allConversations.size();
    }

    @Override
    public Object getItem(int position) {
        return allConversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConversationView view;
        if (convertView == null) {
            view = new ConversationView(this.context);
        } else {
            view = (ConversationView) convertView;
        }

        count++;

        view.setLastMessageText("Nonsense" + count);
        view.setOtherUserText("Other Username" + count);
        return view;
    }
}
