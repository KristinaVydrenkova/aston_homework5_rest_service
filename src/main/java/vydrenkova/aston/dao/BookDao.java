package vydrenkova.aston.dao;

import vydrenkova.aston.entities.Book;

import java.util.List;
import java.util.Optional;

/**
 * The BookDao interface defines the contract for data access operations related to Book entities.
 * Implementing classes must provide methods to retrieve, create, update, and delete books in the
 * underlying data store.
 */
public interface BookDao {

    /**
     * Retrieves a list of all books available in the data store.
     *
     * @return A list of Book entities, or an empty list if no books are found.
     */
    List<Book> getAllBooks();

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id The unique identifier of the book.
     * @return An Optional containing the Book entity if found, or an empty Optional if not found.
     */
    Optional<Book> getBookById(Long id);

    /**
     * Creates a new book in the data store.
     *
     * @param book The Book entity to be created.
     */
    void createBook(Book book);

    /**
     * Updates an existing book in the data store.
     *
     * @param book The Book entity to be updated.
     */
    void updateBook(Book book);

    /**
     * Deletes a book from the data store by its unique identifier.
     *
     * @param id The unique identifier of the book to be deleted.
     */
    void deleteBook(Long id);
}