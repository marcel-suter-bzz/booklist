package ch.bzz.booklist.data;

import ch.bzz.booklist.model.Book;
import ch.bzz.booklist.model.Publisher;
import ch.bzz.booklist.service.Config;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * data handler for reading and writing the csv files
 * <p>
 * M133: Bookshelf
 *
 * @author Marcel Suter
 */
public class DataHandler {
    private static final DataHandler instance = new DataHandler();
    private static List<Publisher> publisherList = null;

    /**
     * default constructor: defeat instantiation
     */
    private DataHandler() {

    }

    /**
     * gets a list of all publishers with their books
     *
     * @return list of publishers
     */
    public static List<Publisher> getPublisherList() {
        if (publisherList == null) {
            publisherList = new ArrayList<>();
            readJSON();
        }
        return publisherList;
    }

    public static List<Book> getBookList() {
        List<Book> bookList = new ArrayList<>();

        for (Publisher publisher : getPublisherList()) {
            for (Book book : publisher.getBookList()) {
                bookList.add(book);
            }
        }
        return bookList;
    }

    /**
     * find the publisher for a book
     *
     * @param bookUUID
     * @return the publisher of this book
     */
    public static Publisher findPublisherByBook(String bookUUID) {
        for (Publisher publisher : getPublisherList()) {
            for (Book book : publisher.getBookList()) {
                if (book.getBookUUID().equals(bookUUID))
                    return publisher;
            }
        }
        return null;
    }

    /**
     * finds a publisher by the uuid
     * @param uuid the publisherUUID
     * @return the publisher
     */
    private static Publisher findPublisherByUUID(String uuid) {
        Publisher publisher = null;
        for (Publisher entry : getPublisherList()) {
            if (entry.getPublisherUUID().equals(uuid)) {
                publisher = entry;
            }
        }
        return publisher;
    }

    /**
     * gets a book by its uuid
     *
     * @param uuid the uuid of the book
     * @return book-object
     */
    public static Book findBookByUUID(String uuid) {
        List<Book> bookList = getBookList();
        for (Book book : bookList) {
            if (book != null && book.getBookUUID().equals(uuid))
                return book;
        }

        return null;
    }

    /**
     * reads the json-file into the publisherList
     */
    private static void readJSON() {
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get(Config.getProperty("publisherJSON")));
            ObjectMapper objectMapper = new ObjectMapper();
            Publisher[] publishers = objectMapper.readValue(jsonData, Publisher[].class);
            for (Publisher publisher : publishers) {
                getPublisherList().add(publisher);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
