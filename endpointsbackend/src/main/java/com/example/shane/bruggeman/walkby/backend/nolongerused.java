/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.shane.bruggeman.walkby.backend;

import com.example.shane.bruggeman.walkby.backend.models.WalkbyAchievement;
import com.example.shane.bruggeman.walkby.backend.models.WalkbyConversation;
import com.example.shane.bruggeman.walkby.backend.models.WalkbyMessage;
import com.example.shane.bruggeman.walkby.backend.models.WalkbyUser;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.ObjectifyService;

import java.io.IOException;
import java.util.List;

import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

//import com.google.appengine.api.users.User;
/**
 * An endpoint class we are exposing
 */
@Api(
        name = "walkbyApi",
        version = "v1",
        namespace = @ApiNamespace(ownerDomain = "backend.walkby.bruggeman.shane.example.com",
            ownerName = "backend.walkby.bruggeman.shane.example.com", packagePath = ""),
        scopes =    {Constants.EMAIL_SCOPE},
        clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
        audiences = {Constants.ANDROID_AUDIENCE}
)

public class nolongerused {

    /**
     * All entities must be registered to be persisted
     */
    static {
        ObjectifyService.register(WalkbyUser.class);
        ObjectifyService.register(WalkbyAchievement.class);
        ObjectifyService.register(WalkbyConversation.class);
        ObjectifyService.register(WalkbyMessage.class);
    }

    //#########################################################################################################
    //######################################## User ###########################################################
    //#########################################################################################################

    @ApiMethod(name = "userInsert")
    public WalkbyUser createUser(@Named("username") String username, @Named("password") String password, @Named("macAddress") String macAddress, User user) throws OAuthRequestException, IOException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on insert walkbyuser");
//        }

        WalkbyUser walkbyUser = new WalkbyUser();
        walkbyUser.setUsername(username);
        walkbyUser.setPassword(password);
        walkbyUser.setMacAddress(macAddress);

        ofy().save().entity(walkbyUser);
        return walkbyUser;
    }

    @ApiMethod(name = "userUpdate")
    public WalkbyUser updateUser(@Named("userId") Long userId, @Named("username") String username, @Named("password") String password, @Named("macAddress") String macAddress, User user) throws OAuthRequestException, IOException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on update walkbyuser");
//        }

        WalkbyUser updatedUser = ofy().load().type(WalkbyUser.class).id(userId).now();

        if (updatedUser == null) {
            throw new IllegalArgumentException("Could not find user for given key " + userId);
        }

        if (username != null) {
            updatedUser.setUsername(username);
        }

        if (password != null) {
            updatedUser.setPassword(password);
        }

        if (macAddress != null) {
            updatedUser.setMacAddress(macAddress);
        }

        ofy().save().entity(updatedUser);

        return updatedUser;
    }

    @ApiMethod(name = "userDelete")
    public Void deleteUser(@Named("userId") Long userId, User user) throws Exception {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list walkbyuser");
//        }

        WalkbyUser deletedUser = ofy().load().type(WalkbyUser.class).id(userId).now();

        if (deletedUser == null) {
            throw new Exception("deleted user was null");
        }

        ofy().delete().entity(deletedUser).now();

        return null;
    }

    @ApiMethod(name = "usersDeleteAll")
    public Void deleteUserAll(User user) throws OAuthRequestException {
        List<WalkbyUser> users = ofy().load().type(WalkbyUser.class).list();
        ofy().delete().entities(users);

        return null;
    }

    @ApiMethod(name = "userList")
    public List listUsersAll(User user) throws OAuthRequestException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list walkbyuser");
//        }
        List<WalkbyUser> users = ofy().load().type(WalkbyUser.class).list();

        return users;
    }

    @ApiMethod(name = "userListSingle")
    public WalkbyUser listUserSingle(@Named("userId") Long userId, User user) throws OAuthRequestException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list walkbyuser");
//        }

        WalkbyUser walkbyUser = ofy().load().type(WalkbyUser.class).id(userId).now();
        return walkbyUser;
    }

    @ApiMethod(name = "userExists")
    public WalkbyUser queryUserExists(@Named("username") String username, User user) throws OAuthRequestException {
        List<WalkbyUser> allUsers = ofy().load().type(WalkbyUser.class).list();
        for(WalkbyUser walkbyUser : allUsers) {
            if(walkbyUser.getUsername().equalsIgnoreCase(username)) {
                return walkbyUser;
            }
        }
        return null;
    }

    @ApiMethod(name = "userIsAuthorized")
    public WalkbyUser queryUserAuth(@Named("username") String username,
                                    @Named("password") String password,
                                    User user) throws OAuthRequestException {
        List<WalkbyUser> allUsers = ofy().load().type(WalkbyUser.class).list();
        for(WalkbyUser walkbyUser : allUsers) {
            if(walkbyUser.getUsername().equalsIgnoreCase(username) && walkbyUser.getPassword().equalsIgnoreCase(password)) {
                return walkbyUser;
            }
        }
        return null;
    }

    //#########################################################################################################
    //######################################## Achievement ####################################################
    //#########################################################################################################

    @ApiMethod(name = "achievementList")
    public List<WalkbyAchievement> listAchievementsAll(User user) throws OAuthRequestException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list achievements");
//        }
        List<WalkbyAchievement> achievements = ofy().load().type(WalkbyAchievement.class).list();

        return achievements;
    }

    @ApiMethod(name = "achievementListSingle")
    public WalkbyAchievement listAchievementSingle(@Named("achievementId") Long achievementId, User user) throws OAuthRequestException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list achievement");
//        }

        WalkbyAchievement achievement = ofy().load().type(WalkbyAchievement.class).id(achievementId).now();
        return achievement;
    }

    @ApiMethod(name = "achievementInsert")
    public WalkbyAchievement createAchievement(@Named("description") String description,
                                               @Named("achievementValue") Integer achievementValue,
                                               User user) throws OAuthRequestException, IOException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on insert achievement");
//        }

        WalkbyAchievement achievement = new WalkbyAchievement();
        achievement.setDescription(description);
        achievement.setAchievementValue(achievementValue);

        ofy().save().entity(achievement);
        return achievement;
    }

    @ApiMethod(name = "achievementUpdate")
    public WalkbyAchievement updateAchievement(@Named("achievementId") Long achievementId,
                                               @Named("description") String description,
                                               @Named("achievementValue") Integer achievementValue,
                                               User user) throws OAuthRequestException, IOException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on update walkbyuser");
//        }

        WalkbyAchievement updatedAchievement = ofy().load().type(WalkbyAchievement.class).id(achievementId).now();

        if (updatedAchievement == null) {
            throw new IllegalArgumentException("Could not find achievement for given key " + achievementId);
        }

        if (description != null) {
            updatedAchievement.setDescription(description);
        }

        if (achievementValue != null) {
            updatedAchievement.setAchievementValue(achievementValue);
        }

        ofy().save().entity(updatedAchievement);

        return updatedAchievement;
    }

    @ApiMethod(name = "achievementDelete")
    public Void deleteAchievement(@Named("userId") Long achievementId,
                                  User user) throws Exception {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list walkbyuser");
//        }

        WalkbyAchievement deletedAchievement = ofy().load().type(WalkbyAchievement.class).id(achievementId).now();

        if (deletedAchievement == null) {
            throw new Exception("deleted achievement was null");
        }

        ofy().delete().entity(deletedAchievement).now();

        return null;
    }

    @ApiMethod(name = "achievementsDeleteAll")
    public Void deleteAchievmentAll(User user) throws OAuthRequestException {
        List<WalkbyAchievement> achievements = ofy().load().type(WalkbyAchievement.class).list();
        ofy().delete().entities(achievements);

        return null;
    }

    //#########################################################################################################
    //######################################## Conversation ###################################################
    //#########################################################################################################

    @ApiMethod(name = "conversationList")
    public List<WalkbyConversation> listConversationsAll(User user) throws OAuthRequestException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list achievements");
//        }
        List<WalkbyConversation> conversations = ofy().load().type(WalkbyConversation.class).list();

        return conversations;
    }

    @ApiMethod(name = "conversationListSingle")
    public WalkbyConversation listConversationSingle(@Named("conversationId") Long conversationId,
                                                     User user) throws OAuthRequestException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list achievement");
//        }

        WalkbyConversation conversation = ofy().load().type(WalkbyConversation.class).id(conversationId).now();
        return conversation;
    }

    @ApiMethod(name = "conversationInsert")
    public WalkbyConversation createConversation(@Named("conversationStarter") Long starterId,
                                                 @Named("conversationReceiver") Long receiverId,
                                                 User user) throws Exception {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on insert achievement");
//        }

        WalkbyConversation conversation = new WalkbyConversation();

        WalkbyUser starter = ofy().load().type(WalkbyUser.class).id(starterId).now();
        WalkbyUser receiver = ofy().load().type(WalkbyUser.class).id(receiverId).now();

        if(starter == null || receiver == null) {
            throw new Exception("Unknown start or receiver id used");
        }

        conversation.setConversationStarterId(starterId);
        conversation.setConversationReceiverId(receiverId);

        ofy().save().entity(conversation);
        return conversation;
    }

    @ApiMethod(name = "conversationUpdate")
    public WalkbyConversation updateConversation(@Named("conversationId") Long conversationId,
                                                 @Named("conversationStarter") Long starterId,
                                                 @Named("conversationReceiver") Long receiverId,
                                                 User user) throws OAuthRequestException, IOException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on update walkbyuser");
//        }

        WalkbyConversation updatedConversation = ofy().load().type(WalkbyConversation.class).id(conversationId).now();

        if (updatedConversation == null) {
            throw new IllegalArgumentException("Could not find conversation for given key " + conversationId);
        }

        if (starterId != null) {
            WalkbyUser starter = ofy().load().type(WalkbyUser.class).id(starterId).now();

            if(starter == null) {
                throw new Error("Invalid starter key");
            }

            updatedConversation.setConversationStarterId(starterId);
        }

        if (receiverId != null) {
            WalkbyUser receiver = ofy().load().type(WalkbyUser.class).id(receiverId).now();

            if(receiver == null) {
                throw new Error("Invalid receiver key");
            }

            updatedConversation.setConversationReceiverId(receiverId);
        }

        ofy().save().entity(updatedConversation);

        return updatedConversation;
    }

    @ApiMethod(name = "conversationDelete")
    public Void deleteConversation(@Named("conversationId") Long conversationId,
                              User user) throws Exception {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list walkbyuser");
//        }

        WalkbyConversation deletedConversation = ofy().load().type(WalkbyConversation.class).id(conversationId).now();

        if (deletedConversation == null) {
            throw new Exception("Conversation id was invalid");
        }

        ofy().delete().entity(deletedConversation).now();

        return null;
    }

    @ApiMethod(name = "conversationsDeleteAll")
    public Void deleteConversationAll(User user) throws OAuthRequestException {
        List<WalkbyConversation> conversations = ofy().load().type(WalkbyConversation.class).list();
        ofy().delete().entities(conversations);

        return null;
    }

    //#########################################################################################################
    //######################################## Message ########################################################
    //#########################################################################################################

    @ApiMethod(name = "messageList")
    public List<WalkbyMessage> listMessagesAll(User user) throws OAuthRequestException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list achievements");
//        }
        List<WalkbyMessage> messages = ofy().load().type(WalkbyMessage.class).list();

        return messages;
    }

    @ApiMethod(name = "messageListSingle")
    public WalkbyMessage listMessageSingle(@Named("messageId") Long messageId,
                                           User user) throws OAuthRequestException {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list achievement");
//        }

        WalkbyMessage message = ofy().load().type(WalkbyMessage.class).id(messageId).now();
        return message;
    }

    @ApiMethod(name = "messageInsert")
    public WalkbyMessage createMessage(@Named("conversationId") Long conversationId,
                                       @Named("messageStarterId") Long starterId,
                                       @Named("messageReceiverId") Long receiverId,
                                       @Named("messageText") String messageText,
                                       User user) throws Exception {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on insert achievement");
//        }

        WalkbyConversation conversation = ofy().load().type(WalkbyConversation.class).id(conversationId).now();

        if(conversation == null) {
            throw new Exception("Conversation has not been created yet for message insert");
        }

        WalkbyUser starter = ofy().load().type(WalkbyUser.class).id(starterId).now();
        WalkbyUser receiver = ofy().load().type(WalkbyUser.class).id(receiverId).now();

        if(starter == null || receiver == null) {
            throw new Exception("Starter or receiver key of message was invalid");
        }

        WalkbyMessage message = new WalkbyMessage();

        message.setConversationId(conversationId);
        message.setStarterId(starterId);
        message.setReceiverId(receiverId);
        message.setMessage(messageText);

        ofy().save().entity(message);
        return message;
    }

    @ApiMethod(name = "messageDelete")
    public Void deleteMessage(@Named("messageId") Long messageId,
                              User user) throws Exception {
//        if(user == null) {
//            throw new OAuthRequestException("User authentication error on list walkbyuser");
//        }

        WalkbyMessage deletedMessage = ofy().load().type(WalkbyMessage.class).id(messageId).now();

        if (deletedMessage == null) {
            throw new Exception("Message id was invalid");
        }

        ofy().delete().entity(deletedMessage).now();

        return null;
    }

    @ApiMethod(name = "messagesDeleteAll")
    public Void deleteMessageAll(User user) throws OAuthRequestException {
        List<WalkbyMessage> messages = ofy().load().type(WalkbyMessage.class).list();
        ofy().delete().entities(messages);

        return null;
    }

}
