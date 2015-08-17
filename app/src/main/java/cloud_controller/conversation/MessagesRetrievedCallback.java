package cloud_controller.conversation;

import com.example.shane.bruggeman.walkby.backend.walkbyUserApi.model.WalkbyMessage;

import java.util.List;

public interface MessagesRetrievedCallback {
    public void messagesRetrieved(List<WalkbyMessage> messages);
}