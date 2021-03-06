package ch.bzz.booklist.service;

import ch.bzz.booklist.data.DataHandler;
import ch.bzz.booklist.model.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

/**
 * provides services for the bookshelf
 * <p>
 * M133: Bookshelf
 *
 * @author Marcel Suter
 */
@Path("book")
public class BookService {

    /**
     * produces a list of all books
     *
     * @return Response
     */
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)

    public Response listBooks(
    ) {
        List<Book> bookList = DataHandler.getBookList();
        Response response = Response
                .status(200)
                .entity(bookList)
                .build();
        return response;

    }

    /**
     * reads a single book identified by the bookId
     *
     * @param bookUUID the bookUUID in the URL
     * @return Response
     */
    @GET
    @Path("read")
    @Produces(MediaType.APPLICATION_JSON)

    public Response readBook(
            @QueryParam("uuid") String bookUUID
    ) {
        Book book = null;
        int httpStatus;

        try {
            UUID bookKey = UUID.fromString(bookUUID);
            book = DataHandler.findBookByUUID(bookUUID);
            if (book != null) {
                httpStatus = 200;
            } else {
                httpStatus = 404;
            }
        } catch (IllegalArgumentException argEx) {
            httpStatus = 400;
        }

        Response response = Response
                .status(httpStatus)
                .entity(book)
                .build();
        return response;
    }
}