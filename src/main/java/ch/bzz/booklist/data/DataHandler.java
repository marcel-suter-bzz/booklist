package ch.bzz.booklist.data;

import ch.bzz.booklist.model.Book;
import ch.bzz.booklist.model.Publisher;
import ch.bzz.booklist.service.Config;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

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
            if (publisher.getBookList() != null) {
                for (Book book : publisher.getBookList()) {
                    bookList.add(book);
                }
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
     *
     * @param uuid the publisherUUID
     * @return the publisher
     */
    public static Publisher findPublisherByUUID(String uuid) {
        Publisher publisher = null;
        for (Publisher entry : getPublisherList()) {
            if (entry.getPublisherUUID().equals(uuid)) {
                publisher = entry;
            }
        }
        return publisher;
    }

    /**
     * inserts a new publisher
     *
     * @param publisher the publisher to be inserted
     */
    public static void insertPublisher(Publisher publisher) {
        getPublisherList().add(publisher);
        writeJSON();
    }

    /**
     * updates an existing publisher
     *
     * @param publisher the updated publisher
     * @return success
     */
    public static boolean updatePublisher(Publisher publisher) {
        boolean found = false;
        Publisher entry = findPublisherByUUID(publisher.getPublisherUUID());
        if (entry != null) {
            found = true;
            entry.setPublisher(publisher.getPublisher());
            writeJSON();
        }
        return found;
    }

    /**
     * deletes a publisher, if it has no books
     *
     * @param publisherUUID the uuid of the publisher to be deleted
     * @return 0=ok, -1=referential integrity, 1=not found
     */
    public static int deletePublisher(String publisherUUID) {
        int errorcode = 1;
        Publisher publisher = findPublisherByUUID(publisherUUID);
        if (publisher == null) errorcode = 1;
        else if (publisher.getBookList() == null) {
            getPublisherList().remove(publisher);
            writeJSON();
            errorcode = 0;
        } else errorcode = -1;

        return errorcode;
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
     * inserts a book to the publisher
     *
     * @param book
     * @param publisherUUID
     * @return success
     */
    public static boolean insertBook(Book book, String publisherUUID) {
        Publisher publisher = findPublisherByUUID(publisherUUID);
        if (publisher == null) {
            return false;
        } else {
            publisher.getBookList().add(book);
            writeJSON();
            return true;
        }

    }

    /**
     * updates a book by deleting and inserting it
     *
     * @param book
     * @param publisherUUID
     * @return success
     */
    public static boolean updateBook(Book book, String publisherUUID) {
        if (deleteBook(book.getBookUUID()))
            return insertBook(book, publisherUUID);
        else return false;
    }

    /**
     * deletes a book identified by its uuid
     *
     * @param bookUUID
     * @return success
     */
    public static boolean deleteBook(String bookUUID) {
        for (Publisher publisher : getPublisherList()) {
            if (publisher.getBookList() != null) {
                for (Book book : publisher.getBookList()) {
                    if (book.getBookUUID().equals(bookUUID)) {
                        publisher.getBookList().remove(book);
                        writeJSON();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * write the publishers with their books
     */
    private static void writeJSON() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer(new DefaultPrettyPrinter());
        FileOutputStream fileOutputStream = null;
        Writer fileWriter;

        String bookPath = Config.getProperty("publisherJSON");
        try {
            fileOutputStream = new FileOutputStream(bookPath);
            fileWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));
            objectWriter.writeValue(fileWriter, getPublisherList());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
