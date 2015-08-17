package cloud_controller.conversation;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversation;

public interface ConversationRetrievedCallback {
    public void conversationRetrieved(WalkbyConversation conversation);
}
