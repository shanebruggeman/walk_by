package com.example.shane.bruggeman.walkby.backend;

import com.example.shane.bruggeman.walkby.backend.models.WalkbyUser;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
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
        ObjectifyService.register(WalkbyUser.class);
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
            name = "addMacAddress",
            path = "walkbyUser/mac/{id}/{addedMac}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public WalkbyUser addMacAddress(@Named("id") Long id,
                                    @Named("addedMac") String addedMac,
                                    WalkbyUser walkbyUser) throws NotFoundException {
        checkExists(id);
        walkbyUser.addMacAddress(addedMac);

        ofy().save().entity(walkbyUser).now();
        logger.info("Updated WalkbyUser's mac addresses: " + walkbyUser);
        return ofy().load().entity(walkbyUser).now();
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

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(WalkbyUser.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find WalkbyUser with ID: " + id);
        }
    }
}