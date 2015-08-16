package com.example.shane.bruggeman.walkby.backend.apis;

import com.example.shane.bruggeman.walkby.backend.models.WalkbyAchievement;
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
        name = "walkbyAchievementApi",
        version = "v1",
        resource = "walkbyAchievement",
        namespace = @ApiNamespace(
                ownerDomain = "backend.walkby.bruggeman.shane.example.com",
                ownerName = "backend.walkby.bruggeman.shane.example.com",
                packagePath = ""
        )
)
public class WalkbyAchievementEndpoint {

    private static final Logger logger = Logger.getLogger(WalkbyAchievementEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(WalkbyAchievement.class);
    }

    /**
     * Returns the {@link WalkbyAchievement} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code WalkbyAchievement} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "walkbyAchievement/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public WalkbyAchievement get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting WalkbyAchievement with ID: " + id);
        WalkbyAchievement walkbyAchievement = ofy().load().type(WalkbyAchievement.class).id(id).now();
        if (walkbyAchievement == null) {
            throw new NotFoundException("Could not find WalkbyAchievement with ID: " + id);
        }
        return walkbyAchievement;
    }

    /**
     * Inserts a new {@code WalkbyAchievement}.
     */
    @ApiMethod(
            name = "insert",
            path = "walkbyAchievement",
            httpMethod = ApiMethod.HttpMethod.POST)
    public WalkbyAchievement insert(WalkbyAchievement walkbyAchievement) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that walkbyAchievement.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(walkbyAchievement).now();
        logger.info("Created WalkbyAchievement with ID: " + walkbyAchievement.getId());

        return ofy().load().entity(walkbyAchievement).now();
    }

    /**
     * Updates an existing {@code WalkbyAchievement}.
     *
     * @param id                the ID of the entity to be updated
     * @param walkbyAchievement the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code WalkbyAchievement}
     */
    @ApiMethod(
            name = "update",
            path = "walkbyAchievement/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyAchievement update(@Named("id") Long id, WalkbyAchievement walkbyAchievement) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(walkbyAchievement).now();
        logger.info("Updated WalkbyAchievement: " + walkbyAchievement);
        return ofy().load().entity(walkbyAchievement).now();
    }

    /**
     * Deletes the specified {@code WalkbyAchievement}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code WalkbyAchievement}
     */
    @ApiMethod(
            name = "remove",
            path = "walkbyAchievement/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(WalkbyAchievement.class).id(id).now();
        logger.info("Deleted WalkbyAchievement with ID: " + id);
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
            path = "walkbyAchievement",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<WalkbyAchievement> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<WalkbyAchievement> query = ofy().load().type(WalkbyAchievement.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<WalkbyAchievement> queryIterator = query.iterator();
        List<WalkbyAchievement> walkbyAchievementList = new ArrayList<WalkbyAchievement>(limit);
        while (queryIterator.hasNext()) {
            walkbyAchievementList.add(queryIterator.next());
        }
        return CollectionResponse.<WalkbyAchievement>builder().setItems(walkbyAchievementList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(WalkbyAchievement.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find WalkbyAchievement with ID: " + id);
        }
    }
}