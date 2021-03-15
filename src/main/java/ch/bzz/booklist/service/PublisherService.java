package ch.bzz.booklist.service;

/**
 * provides services for the publisher
 * <p>
 * M133: Bookshelf
 *
 * @author arcel Suter
 */

import ch.bzz.booklist.data.DataHandler;
import ch.bzz.booklist.model.Publisher;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("publisher")
public class PublisherService {
    /**
     * produces a map of all publishers
     *
     * @return Response
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)

    public Response listPublishers(
    ) {
        List<Publisher> publisherList = DataHandler.getPublisherList();
        Response response = Response
                .status(200)
                .entity(publisherList)
                .build();
        return response;

    }

    /**
     * reads a single publisher identified by the uuid
     *
     * @param publisherUUID the UUID in the URL
     * @return Response
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)

    public Response readPublisher(
            @QueryParam("uuid") String publisherUUID
    ) {
        Publisher publisher = null;
        int httpStatus;

        try {
            UUID.fromString(publisherUUID);
            publisher = DataHandler.findPublisherByUUID(publisherUUID);
            if (publisher != null && publisher.getPublisher() != null) {
                httpStatus = 200;
            } else {
                httpStatus = 404;
            }
        } catch (IllegalArgumentException argEx) {
            httpStatus = 400;
        }

        Response response = Response
                .status(httpStatus)
                .entity(publisher)
                .build();
        return response;
    }

    /**
     * creates a new publisher without books
     * @param publisherName
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createPublisher(
            @FormParam("publisher") String publisherName
    ) {
        int httpStatus = 200;
        Publisher publisher = new Publisher();
        publisher.setPublisherUUID(UUID.randomUUID().toString());
        publisher.setPublisher(publisherName);
        DataHandler.insertPublisher(publisher);

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }

    /**
     * updates the publisher in all it's books
     * @param publisherUUID  the uuid of the publisher
     * @param publisherName  the new publisherName
     * @return Response
     */
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updatePublisher(
            @FormParam("publisherUUID") String publisherUUID,
            @FormParam("publisher") String publisherName
    ) {
        int httpStatus = 200;
        Publisher publisher = new Publisher();
        try {
            UUID.fromString(publisherUUID);
            publisher.setPublisherUUID(publisherUUID);
            publisher.setPublisher(publisherName);
            if (DataHandler.updatePublisher(publisher)) {
                httpStatus = 200;
            } else {
                httpStatus = 404;
            }
        } catch (IllegalArgumentException argEx) {
            httpStatus = 400;
        }
        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }

    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deletePublisher(
            @QueryParam("uuid") String publisherUUID
    ) {
        int httpStatus;
        try {
            UUID.fromString(publisherUUID);
            int errorcode = DataHandler.deletePublisher(publisherUUID);
            if (errorcode == 0) httpStatus = 200;
            else if (errorcode == -1) httpStatus = 409;
            else httpStatus = 404;
        } catch (IllegalArgumentException argEx) {
            httpStatus = 400;
        }
        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }
    /**
     * sets the attribute values of the publisher object
     *
     * @param publisherUUID the unique key of the publisher
     * @param name          the publisherName
     */
    private void setValues(
            Publisher publisher,
            String publisherUUID,
            String name) {
        publisher.setPublisherUUID(publisherUUID);
        publisher.setPublisher(name);
    }
}
