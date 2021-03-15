package ch.bzz.booklist.service;

import ch.bzz.booklist.data.DataHandler;
import ch.bzz.booklist.model.Book;
import ch.bzz.booklist.model.Publisher;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
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

    /**
     * creates a new book
     * @param title the book title
     * @param author the author
     * @param publisherUUID the unique key of the publisher
     * @param price the price
     * @param isbn the isbn-13 number
     * @return Response
     */
    @POST
    @Path("create")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createBook(
            @FormParam("title") String title,
            @FormParam("author") String author,
            @FormParam("publisherUUID") String publisherUUID,
            @FormParam("price") BigDecimal price,
            @FormParam("isbn") String isbn
    ) {
        int httpStatus = 200;
        Book book = new Book();
        book.setBookUUID(UUID.randomUUID().toString());
        setValues(
                book,
                title,
                author,
                price,
                isbn
        );

        DataHandler.insertBook(book, publisherUUID);

        Response response = Response
                .status(httpStatus)
                .entity("")
                .build();
        return response;
    }

    /**
     * updates an existing book
     * @param title the book title
     * @param author the author
     * @param publisherUUID the unique key of the publisher
     * @param price the price
     * @param isbn the isbn-13 number
     * @return Response
     */
    @PUT
    @Path("update")
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateBook(
            @FormParam("bookUUID") String bookUUID,
            @FormParam("title") String title,
            @FormParam("author") String author,
            @FormParam("publisherUUID") String publisherUUID,
            @FormParam("price")BigDecimal price,
            @FormParam("isbn") String isbn
    ) {
        int httpStatus = 200;
        Book book = new Book();
        try {
            UUID.fromString(bookUUID);
            book.setBookUUID(bookUUID);
            setValues(
                    book,
                    title,
                    author,
                    price,
                    isbn
            );
            if (DataHandler.updateBook(book,publisherUUID)) {
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

    /**
     * deletes an existing book identified by its uuid
     * @param bookUUID  the unique key for the book
     * @return Response
     */
    @DELETE
    @Path("delete")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteBook(
            @QueryParam("uuid") String bookUUID
    ) {
        int httpStatus;
        try {
            UUID.fromString(bookUUID);

            if (DataHandler.deleteBook(bookUUID)) {
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

    /**
     * sets the attribute values of the book object
     * @param book  the book object
     * @param title the book title
     * @param author the author
     * @param price the price
     * @param isbn the isbn-13 number
     */
    private void setValues(
            Book book,
            String title,
            String author,
            BigDecimal price,
            String isbn) {
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setIsbn(isbn);
    }
}