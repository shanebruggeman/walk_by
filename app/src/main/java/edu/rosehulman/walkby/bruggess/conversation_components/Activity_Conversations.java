package edu.rosehulman.walkby.bruggess.conversation_components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversation;
import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyUser;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cloud_controller.conversation.ConversationGetUserConversationsAsyncTask;
import cloud_controller.conversation.ConversationInsertAsyncTask;
import cloud_controller.conversation.ConversationInsertionCallback;
import cloud_controller.conversation.ConversationsRetrievedCallback;
import cloud_controller.user.authentication.CheckUsernameAvailableAsyncTask;
import cloud_controller.user.authentication.UserAuthenticationCallback;
import cloud_controller.user.crud_operations.UserGetByUsernameAsyncTask;
import cloud_controller.user.crud_operations.UserRetrievedCallback;
import edu.rosehulman.walkby.bruggess.LoginActivity;
import edu.rosehulman.walkby.bruggess.R;
import edu.rosehulman.walkby.bruggess.message_components.Activity_messages;


public class Activity_Conversations extends Activity implements ConversationsRetrievedCallback,
        UserAuthenticationCallback, UserRetrievedCallback, ConversationInsertionCallback{

    private WalkbyUser user;
    private String username;

    public static String CONVERSATION_KEY = "CONVERSATION_KEY";

    private List<WalkbyConversation> userConversations;
    private ListView conversationsListView;

    private WalkbyUser otherUser;
    String otherUsername;
    boolean otherUserLoading;

    final Activity_Conversations self = this;
    private Button addConversationsButton;

    private ImageButton addConversationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversations);

        otherUserLoading = false;

        //we've stored important information on the intent
        Intent myIntent = getIntent();

        //conversations holds the links to viewing all messages in any given conversation
        //add conversation allows us to start a new conversation at will
        conversationsListView = (ListView) findViewById(R.id.conversations_list_view);
        addConversationButton = (ImageButton) findViewById(R.id.add_conversation_button);

        //the viewing user's username, as established within the navigation activity
        username = myIntent.getStringExtra(LoginActivity.USERNAME_KEY);

        //get the user so that we can use them
        (new UserGetByUsernameAsyncTask(this)).execute(username);

        final Context context = Activity_Conversations.this;

        addConversationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.conversation_dialog);

                final EditText userInput = (EditText) dialog.findViewById(R.id.new_conversation_edit_text);
                Button submitNewConversationButton = (Button) dialog.findViewById(R.id.submit_new_conversation_button);

                // if button is clicked, close the custom dialog
                submitNewConversationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String username = userInput.getText().toString();
                        self.otherUsername = username;
                        (new CheckUsernameAvailableAsyncTask(self)).execute(username);
                        dialog.dismiss();
                    }
                });

                Log.d(LoginActivity.DEBUG_KEY, "Dialog trigger show");
                dialog.show();
            }
        });
    }

    //called when the user wants to start a chat with somebody
    @Override
    public void finishAuth(Boolean response) {
        //if username exists
        if(!response) {
            Toast.makeText(this, "Making conversation!", Toast.LENGTH_LONG).show();
            loadOtherUserAndStartConversation(otherUsername);
        } else {
            Toast.makeText(this, "Specified Username Does Not Exist", Toast.LENGTH_LONG).show();
        }
    }

    //handles the result of loading a user's conversation data
    @Override
    public void conversationsRetrieved(List<WalkbyConversation> conversations) {
        Log.d(LoginActivity.DEBUG_KEY, "Conversations retrieved and are null: " + (conversations == null));
        Log.d(LoginActivity.DEBUG_KEY, "Size of conversations is " + conversations.size());

        final ConversationListArrayAdapter adapter = new ConversationListArrayAdapter(this, conversations, user);

        conversationsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //handles result of retrieving user by username
    @Override
    public void userHasBeenRetrieved(WalkbyUser user) {

        //handles loading of the other user once their username has been authenticated
        if(otherUserLoading) {
            Log.d(LoginActivity.DEBUG_KEY, "Other (retrieved) user is null " + (user == null));
            otherUser = user;
            otherUserLoading = false;

            WalkbyConversation newConversation = new WalkbyConversation();
            newConversation.setConversationStarterId(this.user.getId());
            newConversation.setConversationReceiverId(this.otherUser.getId());

            (new ConversationInsertAsyncTask(this)).execute(newConversation);
            return;
        }

        //this only happens at the beginning when we load the activity
        Log.d(LoginActivity.DEBUG_KEY, "User (self) retrieved and is null: " + (user == null));
        this.user = user;

        Log.d(LoginActivity.DEBUG_KEY, "Setting up timer to retrieve user conversation data");

        //now load the user's conversation information
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(LoginActivity.DEBUG_KEY, "Starting conversation data retrieval for " + username);
                (new ConversationGetUserConversationsAsyncTask(self)).execute(username);
            }
        };

        //continually refresh every 90 seconds
        timer.schedule(timerTask, 0, 90000);
    }

    public void loadOtherUserAndStartConversation(String otherUsername) {
        Log.d(LoginActivity.DEBUG_KEY, "Loading other user " + otherUsername);
        otherUserLoading = true;
        (new UserGetByUsernameAsyncTask(this)).execute(otherUsername);
    }

    //start up the message once we've made the conversation
    @Override
    public void onConversationInserted(WalkbyConversation conversation) {
        //seems to port better this way
        String conversationId = conversation.getId().toString();

        Intent messagesIntent = new Intent(this, Activity_messages.class);
        messagesIntent.putExtra(LoginActivity.USERNAME_KEY, username);
        messagesIntent.putExtra(Activity_Conversations.CONVERSATION_KEY, conversationId);
        startActivity(messagesIntent);
    }
}
