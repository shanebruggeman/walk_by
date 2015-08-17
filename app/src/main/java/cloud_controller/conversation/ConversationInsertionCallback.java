package cloud_controller.conversation;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversation;

public interface ConversationInsertionCallback {
    public void onConversationInserted(WalkbyConversation conversation);
}
