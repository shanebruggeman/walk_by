package cloud_controller.conversation;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyConversation;

import java.util.List;

public interface ConversationsRetrievedCallback {
    public void conversationsRetrieved(List<WalkbyConversation> conversations);
}
