package com.example.shane.bruggeman.walkby.backend;

import com.example.shane.bruggeman.walkby.backend.models.WalkbyConversation;
import com.example.shane.bruggeman.walkby.backend.models.WalkbyMessage;
import com.example.shane.bruggeman.walkby.backend.models.WalkbyUser;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "walkbyUserApi",
        version = "v1",
        resource = "walkbyUser",
        namespace = @ApiNamespace(
                ownerDomain = "backend.walkby.bruggeman.shane.example.com",
                ownerName = "backend.walkby.bruggeman.shane.example.com",
                packagePath = ""
        )
)
public class WalkbyUserEndpoint {

    private static final Logger logger = Logger.getLogger(WalkbyUserEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(WalkbyConversation.class);
        ObjectifyService.register(WalkbyUser.class);
        ObjectifyService.register(WalkbyMessage.class);
    }

    /**
     * Returns the {@link WalkbyUser} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code WalkbyUser} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "walkbyUser/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public WalkbyUser get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting WalkbyUser with ID: " + id);
        WalkbyUser walkbyUser = ofy().load().type(WalkbyUser.class).id(id).now();
        if (walkbyUser == null) {
            throw new NotFoundException("Could not find WalkbyUser with ID: " + id);
        }
        return walkbyUser;
    }

    /**
     * Inserts a new {@code WalkbyUser}.
     */
    @ApiMethod(
            name = "insert",
            path = "walkbyUser",
            httpMethod = ApiMethod.HttpMethod.POST)
    public WalkbyUser insert(WalkbyUser walkbyUser) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that walkbyUser.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(walkbyUser).now();
        logger.info("Created WalkbyUser with ID: " + walkbyUser.getId());

        return ofy().load().entity(walkbyUser).now();
    }

    /**
     * Updates an existing {@code WalkbyUser}.
     *
     * @param id         the ID of the entity to be updated
     * @param walkbyUser the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code WalkbyUser}
     */
    @ApiMethod(
            name = "update",
            path = "walkbyUser/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyUser update(@Named("id") Long id, WalkbyUser walkbyUser) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(walkbyUser).now();
        logger.info("Updated WalkbyUser: " + walkbyUser);
        return ofy().load().entity(walkbyUser).now();
    }

    @ApiMethod(
            name = "addEncounteredMacAddress",
            path = "walkbyUser/macaddressing/{username}/{addedMac}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyUser addEncounteredMacAddress(@Named("username") String username,
                                               @Named("addedMac") String addedMac) throws NotFoundException {
        checkUsernameExists(username);

        List<WalkbyUser> users = ofy().load().type(WalkbyUser.class).list();
        WalkbyUser currentUser = users.get(0);

        for(int i = 0; i < users.size(); i++) {
            currentUser = users.get(i);
            String currentUsername = currentUser.getUsername();
            if(currentUsername.equalsIgnoreCase(username)) {
                if(currentUser.getEncounteredMacAddresses() == null) {
                    currentUser.setEncounteredMacAddresses(new ArrayList<String>());
                }
                if(!currentUser.getEncounteredMacAddresses().contains(addedMac)) {
                    currentUser.addMacAddress(addedMac);
                }
                break;
            }
        }

        for(int j = 0; j < users.size(); j++) {
            WalkbyUser possibleMatch = users.get(j);
            if(possibleMatch.getMacAddress().equals(addedMac)) {
                if(possibleMatch.getEncounteredMacAddresses() == null) {
                    possibleMatch.setEncounteredMacAddresses(new ArrayList<String>());
                }
                if(!possibleMatch.getEncounteredMacAddresses().contains(addedMac)) {
                    possibleMatch.addMacAddress(addedMac);
                }
                ofy().save().entity(possibleMatch).now();
                break;
            }
        }

        ofy().save().entity(currentUser).now();
        return ofy().load().entity(currentUser).now();
    }

    @ApiMethod(
            name = "addOwnMacAddress",
            path = "walkbyUser/mac/{username}/own/{ownMac}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyUser addOwnMacAddress(@Named("username") String username,
                                       @Named("ownMac") String ownMac) throws Exception {

        List<WalkbyUser> allUsers = ofy().load().type(WalkbyUser.class).list();
        for(WalkbyUser walkbyUser : allUsers) {
            if(walkbyUser.getUsername().equalsIgnoreCase(username)) {
                walkbyUser.setMacAddress(ownMac);
                ofy().save().entity(walkbyUser).now();
                return ofy().load().entity(walkbyUser).now();
            }
        }

        throw new Exception("Could not find a user with the username " + username);
    }

    @ApiMethod(
            name = "getEncounteredUsers",
            path = "walkbyUser/mac/encountered/{username}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Collection<WalkbyUser> getEncounteredUsers(@Named("username") String username) throws Exception {

        List<WalkbyUser> encounteredUsers = new ArrayList<WalkbyUser>();
        WalkbyUser currentUser = null;

        List<WalkbyUser> allUsers = ofy().load().type(WalkbyUser.class).list();
        for(WalkbyUser walkbyUser : allUsers) {
            if(walkbyUser.getUsername().equalsIgnoreCase(username)) {
                currentUser = walkbyUser;
            }
        }

        if(currentUser == null) {
            throw new Exception("Could not find a user with username " + username);
        }

        for(WalkbyUser otherWalkbyUser : allUsers) {
            String otherMacAddress = otherWalkbyUser.getMacAddress();
            if(otherWalkbyUser.getUsername().equals(username)) {
                continue;
            }
            for(String encounteredMac : currentUser.getEncounteredMacAddresses()) {
                if(otherMacAddress.equals(encounteredMac)) {
                    encounteredUsers.add(otherWalkbyUser);
                }
            }
        }

        return encounteredUsers;
    }

    @ApiMethod(
            name = "userGetByUsername",
            path = "walkbyUser/{username}/id",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyUser userGetId(@Named("username") String username) throws Exception {
        List<WalkbyUser> users = ofy().load().type(WalkbyUser.class).list();

        for(WalkbyUser walkbyUser : users) {
            if(walkbyUser.getUsername().equalsIgnoreCase(username)) {
                return walkbyUser;
            }
        }

        return null;
    }

    @ApiMethod(
            name = "checkUsernamePassword",
            path = "walkbyUser/{username}/{password}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyUser checkUsernamePassword(@Named("username") String username,
                                            @Named("password") String password) throws Exception {
        List<WalkbyUser> users = ofy().load().type(WalkbyUser.class).list();

        for(WalkbyUser walkbyUser : users) {
            if(walkbyUser.getUsername().equalsIgnoreCase(username) && walkbyUser.getPassword().equalsIgnoreCase(password)) {
                return walkbyUser;
            }
        }

        return null;
    }

    /**
     * Deletes the specified {@code WalkbyUser}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code WalkbyUser}
     */
    @ApiMethod(
            name = "remove",
            path = "walkbyUser/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(WalkbyUser.class).id(id).now();
        logger.info("Deleted WalkbyUser with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "walkbyUser",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Collection<WalkbyUser> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<WalkbyUser> query = ofy().load().type(WalkbyUser.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<WalkbyUser> queryIterator = query.iterator();
        List<WalkbyUser> walkbyUserList = new ArrayList(limit);
        while (queryIterator.hasNext()) {
            walkbyUserList.add(queryIterator.next());
        }

        return walkbyUserList;
    }

    private void checkUsernameExists(String username) throws NotFoundException {
        List<WalkbyUser> walkbyUsers = ofy().load().type(WalkbyUser.class).list();

        for(WalkbyUser user : walkbyUsers) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return;
            }
        }
        throw new NotFoundException("Could not verify username " + username + "!");
    }

    //******************************************************************************************
    //******************************************************************************************
    //************************************** Conversation **************************************
    //******************************************************************************************
    //******************************************************************************************

    /**
     * Returns the {@link WalkbyConversation} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code WalkbyConversation} with the provided ID.
     */
    @ApiMethod(
            name = "getConversation",
            path = "walkbyConversation/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public WalkbyConversation getConversation(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting WalkbyConversation with ID: " + id);
        WalkbyConversation walkbyConversation = ofy().load().type(WalkbyConversation.class).id(id).now();
        if (walkbyConversation == null) {
            throw new NotFoundException("Could not find WalkbyConversation with ID: " + id);
        }
        return walkbyConversation;
    }

    /**
     * Inserts a new {@code WalkbyConversation}.
     */
    @ApiMethod(
            name = "insertConversation",
            path = "walkbyConversation",
            httpMethod = ApiMethod.HttpMethod.POST)
    public WalkbyConversation insertConversation(WalkbyConversation walkbyConversation) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that walkbyConversation.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(walkbyConversation).now();
        logger.info("Created WalkbyConversation with ID: " + walkbyConversation.getId());

        return ofy().load().entity(walkbyConversation).now();
    }

    /**
     * Updates an existing {@code WalkbyConversation}.
     *
     * @param id                 the ID of the entity to be updated
     * @param walkbyConversation the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code WalkbyConversation}
     */
    @ApiMethod(
            name = "updateConversation",
            path = "walkbyConversation/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyConversation updateConversation(@Named("id") Long id, WalkbyConversation walkbyConversation) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExistsConversation(id);
        ofy().save().entity(walkbyConversation).now();
        logger.info("Updated WalkbyConversation: " + walkbyConversation);
        return ofy().load().entity(walkbyConversation).now();
    }

    /**
     * Deletes the specified {@code WalkbyConversation}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code WalkbyConversation}
     */
    @ApiMethod(
            name = "removeConversation",
            path = "walkbyConversation/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void removeConversation(@Named("id") Long id) throws NotFoundException {
        checkExistsConversation(id);
        ofy().delete().type(WalkbyConversation.class).id(id).now();
        logger.info("Deleted WalkbyConversation with ID: " + id);
    }

    @ApiMethod(
            name = "listConversations",
            path = "walkbyConversation",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<WalkbyConversation> listConversations(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<WalkbyConversation> query = ofy().load().type(WalkbyConversation.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<WalkbyConversation> queryIterator = query.iterator();
        List<WalkbyConversation> walkbyConversationList = new ArrayList<WalkbyConversation>(limit);
        while (queryIterator.hasNext()) {
            walkbyConversationList.add(queryIterator.next());
        }
        return CollectionResponse.<WalkbyConversation>builder().setItems(walkbyConversationList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    @ApiMethod(
            name = "getUserConversations",
            path = "walkbyConversations/{username}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Collection<WalkbyConversation> getUserConversations(@Named("username") String username) {
        List<WalkbyConversation> conversations = ofy().load().type(WalkbyConversation.class).list();
        Long userId = null;
        List<WalkbyUser> allUsers = ofy().load().type(WalkbyUser.class).list();

        for(WalkbyUser user : allUsers) {
            String currentUsername = user.getUsername();
            if(username.equalsIgnoreCase(currentUsername)) {
                userId = user.getId();
            }
        }

        List<WalkbyConversation> matches = new ArrayList<WalkbyConversation>();

        for(WalkbyConversation conversation : conversations) {
            Long starter = conversation.getConversationStarterId();
            Long receiver = conversation.getConversationReceiverId();
            if(userId.equals(starter) || (userId.equals(receiver))) {
                matches.add(conversation);
            }
        }

        return matches;
    }

    @ApiMethod(
            name = "getConversationMessages",
            path = "walkbyMessages/conversationmessages/{conversationId}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public List<WalkbyMessage> getConversationMessages(@Named("conversationId") Long conversationId) throws Exception {
        if(conversationId == null) {
            throw new Exception("conversationId is null on messages retrieval!");
        }

        List<WalkbyMessage> messages = ofy().load().type(WalkbyMessage.class).list();
        if(messages == null) {
            throw new Exception("messages are null!");
        }

        List<WalkbyMessage> matches = new ArrayList<WalkbyMessage>();

        for(WalkbyMessage currentMessage : messages) {
            if(currentMessage == null) {
                throw new Exception("currentMessage is null!");
            }
            if(currentMessage.getConversationId().equals(conversationId)) {
                matches.add(currentMessage);
            }
        }

        return matches;
    }



    @ApiMethod(
            name = "insertMessage",
            path = "walkbyMessages",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyMessage insertMessage(WalkbyMessage message) {
        ofy().save().entity(message).now();
        return ofy().load().entity(message).now();
    }

    @ApiMethod(
            name = "listMessages",
            path = "walkbyMessage",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<WalkbyMessage> listMessages(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<WalkbyMessage> query = ofy().load().type(WalkbyMessage.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<WalkbyMessage> queryIterator = query.iterator();
        List<WalkbyMessage> walkbyMessageList = new ArrayList<WalkbyMessage>(limit);
        while (queryIterator.hasNext()) {
            walkbyMessageList.add(queryIterator.next());
        }
        return CollectionResponse.<WalkbyMessage>builder().setItems(walkbyMessageList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }


    private void checkExistsConversation(Long id) throws NotFoundException {
        try {
            ofy().load().type(WalkbyConversation.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find WalkbyConversation with ID: " + id);
        }
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(WalkbyUser.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find WalkbyUser with ID: " + id);
        }
    }
}