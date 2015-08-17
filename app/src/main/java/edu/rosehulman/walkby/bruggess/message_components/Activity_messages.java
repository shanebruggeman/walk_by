package edu.rosehulman.walkby.bruggess.message_components;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversation;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyMessage;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cloud_controller.conversation.ConversationGetByIdAsyncTask;
import cloud_controller.conversation.ConversationGetUserConversationMessagesAsyncTask;
import cloud_controller.conversation.ConversationRetrievedCallback;
import cloud_controller.conversation.MessageInsertAsyncTask;
import cloud_controller.conversation.MessagesRetrievedCallback;
import cloud_controller.user.crud_operations.UserGetByUsernameAsyncTask;
import cloud_controller.user.crud_operations.UserRetrievedCallback;
import edu.rosehulman.walkby.bruggess.LoginActivity;
import edu.rosehulman.walkby.bruggess.R;
import edu.rosehulman.walkby.bruggess.conversation_components.Activity_Conversations;

public class Activity_messages extends Activity implements UserRetrievedCallback,
        MessagesRetrievedCallback, ConversationRetrievedCallback {

    private Button sendButton;
    private EditText sendText;
    private ListView messagesListView;

    final Activity_messages self = this;

    String username;
    private WalkbyUser user;
    private WalkbyConversation conversation;

    private Long conversationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        username = getIntent().getStringExtra(LoginActivity.USERNAME_KEY);
        String convoId = getIntent().getStringExtra(Activity_Conversations.CONVERSATION_KEY);
        conversationId = Long.parseLong(convoId);
        Log.d(LoginActivity.DEBUG_KEY, "This is our passed long: " + conversationId);


        sendButton = (Button) findViewById(R.id.message_send);
        sendText = (EditText) findViewById(R.id.message_input);
        messagesListView = (ListView) findViewById(R.id.messages_list_view);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(self, "Creating Message!", Toast.LENGTH_LONG).show();
                WalkbyMessage message = new WalkbyMessage();

                Long conversationReceiver = conversation.getConversationReceiverId();
                Long conversationStarter = conversation.getConversationStarterId();

                Long starterId = user.getId();
                Long receiverId = null;

                if (starterId.equals(conversationReceiver)) {
                    receiverId = conversationStarter;
                } else {
                    receiverId = conversationReceiver;
                }

                message.setStarterId(starterId);
                message.setReceiverId(receiverId);
                message.setMessage(sendText.getText().toString());
                message.setConversationId(conversation.getId());

                Toast.makeText(self, "Storing Message!", Toast.LENGTH_LONG).show();
                (new MessageInsertAsyncTask()).execute(message);
            }
        });

        (new ConversationGetByIdAsyncTask(this)).execute(conversationId);
    }


    @Override
    public void userHasBeenRetrieved(WalkbyUser user) {
        this.user = user;

        //now load the user's message information
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(LoginActivity.DEBUG_KEY, "Starting messages data retrieval");
                (new ConversationGetUserConversationMessagesAsyncTask(self)).execute(conversation.getId());
            }
        };

        //continually refresh every ten seconds
        timer.schedule(timerTask, 0, 20000);
    }

    @Override
    public void messagesRetrieved(List<WalkbyMessage> messages) {
        Log.d(LoginActivity.DEBUG_KEY, "Messages retrieved and are null: " + (messages == null));
        Log.d(LoginActivity.DEBUG_KEY, "Size of messages is " + messages.size());

        for(int i = 0; i < messages.size(); i++) {
            Log.d(LoginActivity.DEBUG_KEY, "Message" + i + " is: " + messages.get(i).getMessage());
        }

        List<WalkbyMessage> fake = new ArrayList<WalkbyMessage>();

        for(int i = 0; i < messages.size(); i++) {
            fake.add(new WalkbyMessage());
        }

        final MessageListArrayAdapter adapter = new MessageListArrayAdapter(this, messages, user);

        messagesListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void conversationRetrieved(WalkbyConversation conversation) {
        this.conversation = conversation;
        (new UserGetByUsernameAsyncTask(this)).execute(username);
    }
}
