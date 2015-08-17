package com.example.shane.bruggeman.walkby.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;

@Entity
public class WalkbyConversation {

    @Id
    Long id;

    Long conversationStarterId;
    Long conversationReceiverId;

    ArrayList<WalkbyMessage> messages = new ArrayList<WalkbyMessage>();


    public Long getId() {
        return id;
    }

    public Long getConversationStarterId() {
        return conversationStarterId;
    }

    public void setConversationStarterId(Long conversationStarterId) {
        this.conversationStarterId = conversationStarterId;
    }

    public Long getConversationReceiverId() {
        return conversationReceiverId;
    }

    public void setConversationReceiverId(Long conversationReceiverId) {
        this.conversationReceiverId = conversationReceiverId;
    }

    public ArrayList<WalkbyMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<WalkbyMessage> messages) {
        this.messages = messages;
    }
}
