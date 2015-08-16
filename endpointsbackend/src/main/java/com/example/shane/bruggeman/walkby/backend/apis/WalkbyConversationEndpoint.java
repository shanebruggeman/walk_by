package com.example.shane.bruggeman.walkby.backend.apis;

import com.example.shane.bruggeman.walkby.backend.models.WalkbyConversation;
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
        name = "walkbyConversationApi",
        version = "v1",
        resource = "walkbyConversation",
        namespace = @ApiNamespace(
                ownerDomain = "backend.walkby.bruggeman.shane.example.com",
                ownerName = "backend.walkby.bruggeman.shane.example.com",
                packagePath = ""
        )
)
public class WalkbyConversationEndpoint {

    private static final Logger logger = Logger.getLogger(WalkbyConversationEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(WalkbyConversation.class);
    }

    /**
     * Returns the {@link WalkbyConversation} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code WalkbyConversation} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "walkbyConversation/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public WalkbyConversation get(@Named("id") Long id) throws NotFoundException {
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
            name = "insert",
            path = "walkbyConversation",
            httpMethod = ApiMethod.HttpMethod.POST)
    public WalkbyConversation insert(WalkbyConversation walkbyConversation) {
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
            name = "update",
            path = "walkbyConversation/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyConversation update(@Named("id") Long id, WalkbyConversation walkbyConversation) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
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
            name = "remove",
            path = "walkbyConversation/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(WalkbyConversation.class).id(id).now();
        logger.info("Deleted WalkbyConversation with ID: " + id);
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
            path = "walkbyConversation",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<WalkbyConversation> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
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

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(WalkbyConversation.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find WalkbyConversation with ID: " + id);
        }
    }
}