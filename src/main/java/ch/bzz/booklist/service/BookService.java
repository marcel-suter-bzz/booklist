package ch.bzz.booklist.service;

import ch.bzz.booklist.data.DataHandler;
import ch.bzz.booklist.model.Book;
import ch.bzz.booklist.model.Publisher;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
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
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String bookUUID
    ) {
        Book book = null;
        int httpStatus;


        book = DataHandler.findBookByUUID(bookUUID);
        if (book != null) {
            httpStatus = 200;
        } else {
            httpStatus = 404;
        }


        Response response = Response
                .status(httpStatus)
                .entity(book)
                .build();
        return response;
    }

    /**
     * creates a new book
     *
     * @param book          a valid book
     * @param publisherUUID the uuid of the publisher
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createBook(
            @Valid @BeanParam Book book,
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @FormParam("publisherUUID") String publisherUUID
    ) {
        int httpStatus = 200;
        book.setBookUUID(UUID.randomUUID().toString());

        DataHandler.insertBook(book, publisherUUID);

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }

    /**
     * updates an existing book
     *
     * @param book          a valid book
     * @param publisherUUID the uuid of the publisher
     * @return Response
     */
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateBook(
            @Valid @BeanParam Book book,
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @FormParam("publisherUUID") String publisherUUID
    ) {
        int httpStatus = 200;
        Book oldBook = DataHandler.findBookByUUID(book.getBookUUID());
        if (oldBook != null) {
            oldBook.setTitle(book.getTitle());
            oldBook.setAuthor(book.getAuthor());
            oldBook.setPrice(book.getPrice());
            oldBook.setIsbn(book.getIsbn());
            if (DataHandler.updateBook(book, publisherUUID)) {
                httpStatus = 200;
            } else {
                httpStatus = 404;
            }
        } else {
            httpStatus = 404;
        }

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }

    /**
     * deletes an existing book identified by its uuid
     *
     * @param bookUUID the unique key for the book
     * @return Response
     */
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBook(
            @NotEmpty
            @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
            @QueryParam("uuid") String bookUUID
    ) {
        int httpStatus;


        if (DataHandler.deleteBook(bookUUID)) {
            httpStatus = 200;

        } else {
            httpStatus = 404;
        }

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }

}