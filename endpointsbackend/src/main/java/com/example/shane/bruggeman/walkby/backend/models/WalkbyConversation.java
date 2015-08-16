package com.example.shane.bruggeman.walkby.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class WalkbyConversation {

    @Id
    Long id;

    //walkbyusers
    Long conversationStarterId;
    Long conversationReceiverId;

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

    public Long getId() {
        return id;
    }
}
