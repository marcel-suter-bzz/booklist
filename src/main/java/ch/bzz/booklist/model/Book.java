package ch.bzz.booklist.model;

import ch.bzz.booklist.data.DataHandler;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.*;
import javax.ws.rs.FormParam;
import java.math.BigDecimal;

/**
 * a book in the bookshelf
 * <p>
 * Bookshelf
 *
 * @author Marcel Suter (Ghwalin)
 */
public class Book {
    @FormParam("bookUUID")
    @Pattern(regexp = "[0-9a-fA-F]{8}-([0-9a-fA-F]{4}-){3}[0-9a-fA-F]{12}")
    private String bookUUID;

    @FormParam("title")
    @NotEmpty
    @Size(min=2, max=40)
    private String title;

    @FormParam("author")
    @NotEmpty
    @Size(min=5, max=50)
    private String author;

    @FormParam("price")
    @DecimalMax(value="299.95")
    @DecimalMin(value="0.05")
    private BigDecimal price;

    @FormParam("isbn")
    @Pattern(regexp = "(?=[0-9]{13}|[- 0-9]{17})97[89](-[0-9]{1,5}){3}-[0-9]")
    private String isbn;

    public Book() {
        setBookUUID(null);
        setTitle(null);
        setAuthor(null);
        setPrice(null);
        setIsbn(null);
    }

    /**
     * gets the uuid of the publisher from the publisherList
     * @return the publisherUUID
     */
    @JsonIgnore
    public String getPublisherUUID() {
        Publisher publisher = DataHandler.findPublisherByBook(getBookUUID());
        return publisher.getPublisherUUID();
    }
    /**
     * gets the publisher name from the publisherList
     * @return the publishername
     */
    @JsonIgnore
    public String getPublisherName() {
        Publisher publisher = DataHandler.findPublisherByBook(getBookUUID());
        return publisher.getPublisher();
    }
    /**
     * Gets the bookUUID
     *
     * @return value of bookUUID
     */
    public String getBookUUID() {
        return bookUUID;
    }

    /**
     * Sets the bookUUID
     *
     * @param bookUUID the value to set
     */

    public void setBookUUID(String bookUUID) {
        this.bookUUID = bookUUID;
    }

    /**
     * Gets the title
     *
     * @return value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     *
     * @param title the value to set
     */

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the author
     *
     * @return value of author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author
     *
     * @param author the value to set
     */

    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Gets the price
     *
     * @return value of price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets the price
     *
     * @param price the value to set
     */

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets the isbn
     *
     * @return value of isbn
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets the isbn
     *
     * @param isbn the value to set
     */

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}