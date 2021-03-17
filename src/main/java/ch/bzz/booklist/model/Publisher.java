package ch.bzz.booklist.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;
import java.util.ArrayList;
import java.util.List;

/**
 * a publisher of a book
 * <p>
 * Bookshelf
 *
 * @author Marcel Suter (Ghwalin)
 */
public class Publisher {
    @FormParam("publisherUUID")
    @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
    private String publisherUUID;

    @FormParam("publisher")
    @NotEmpty
    @Size(min=5, max=40)
    private String publisher;

    private List<Book> bookList;

    /**
     * default constructor
     */
    public Publisher() {
        setBookList(new ArrayList<>());
    }
    /**
     * Gets the publisherUUID
     *
     * @return value of publisherUUID
     */
    public String getPublisherUUID() {
        return publisherUUID;
    }

    /**
     * Sets the publisherUUID
     *
     * @param publisherUUID the value to set
     */

    public void setPublisherUUID(String publisherUUID) {
        this.publisherUUID = publisherUUID;
    }

    /**
     * Gets the publisher
     *
     * @return value of publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Sets the publisher
     *
     * @param publisher the value to set
     */

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * gets the bookMap
     * @return map of books
     */
    public List<Book> getBookList() {
        return bookList;
    }

    /**
     * sets the bookMap
     * @param bookList
     */
    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}