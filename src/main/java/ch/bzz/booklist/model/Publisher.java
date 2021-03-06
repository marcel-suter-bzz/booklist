package ch.bzz.booklist.model;

import java.util.List;

/**
 * a publisher of a book
 * <p>
 * Bookshelf
 *
 * @author Marcel Suter (Ghwalin)
 */
public class Publisher {
    private String publisherUUID;
    private String publisher;
    private List<Book> bookList;

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