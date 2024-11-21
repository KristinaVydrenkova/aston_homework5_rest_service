package vydrenkova.aston.services;

import vydrenkova.aston.dto.BookDTO;

import java.util.List;
import java.util.Optional;

/**
 * The BookService interface defines the contract for services that handle operations
 * related to books. Implementing classes must provide functionality for creating,
 * retrieving, updating, and deleting book entities.
 */
public interface BookService {

    /**
     * Retrieves a list of all books available in the system.
     *
     * @return A list of BookDTO objects representing all books.
     */
    List<BookDTO> getAllBooks();

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id The unique identifier of the book.
     * @return An Optional containing the BookDTO if found, or an empty Optional if not found.
     */
    Optional<BookDTO> getBookById(Long id);

    /**
     * Creates a new book in the system.
     *
     * @param bookDTO The BookDTO object representing the book to be created.
     */
    void createBook(BookDTO bookDTO);

    /**
     * Updates an existing book in the system.
     *
     * @param bookDTO The BookDTO object representing the book to be updated.
     */
    void updateBook(BookDTO bookDTO);

    /**
     * Deletes a book from the system by its unique identifier.
     *
     * @param id The unique identifier of the book to be deleted.
     */
    void deleteBook(Long id);
}