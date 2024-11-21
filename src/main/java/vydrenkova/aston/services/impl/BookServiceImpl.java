package vydrenkova.aston.services.impl;

import vydrenkova.aston.dao.BookDao;
import vydrenkova.aston.dto.BookDTO;
import vydrenkova.aston.entities.Book;
import vydrenkova.aston.mappers.BookMapper;
import vydrenkova.aston.services.BookService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The BookServiceImpl class is an implementation of the BookService interface. It provides concrete
 * implementations for handling operations related to books. This service layer interacts with the
 * BookDao for data access and uses the BookMapper for converting between Book entities and BookDTOs.
 */
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final BookMapper bookMapper;

    /**
     * Constructs a new BookServiceImpl with the specified BookDao and BookMapper.
     *
     * @param bookDao     The BookDao to be used for data access.
     * @param bookMapper  The BookMapper to be used for mapping between entities and DTOs.
     */
    public BookServiceImpl(BookDao bookDao, BookMapper bookMapper) {
        this.bookDao = bookDao;
        this.bookMapper = bookMapper;
    }

    /**
     * Retrieves a list of all books available in the system.
     *
     * @return A list of BookDTO objects representing all books.
     */
    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookDao.getAllBooks();
        List<BookDTO> bookDTOs = books.stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());
        return bookDTOs;
    }

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id The unique identifier of the book.
     * @return An Optional containing the BookDTO if found, or an empty Optional if not found.
     */
    @Override
    public Optional<BookDTO> getBookById(Long id) {
        Optional<Book> book = bookDao.getBookById(id);
        if (book.isPresent()) {
            return Optional.of(bookMapper.toDTO(book.get()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Creates a new book in the system.
     *
     * @param bookDTO The BookDTO object representing the book to be created.
     */
    @Override
    public void createBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        bookDao.createBook(book);
    }

    /**
     * Updates an existing book in the system.
     *
     * @param bookDTO The BookDTO object representing the book to be updated.
     */
    @Override
    public void updateBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        bookDao.updateBook(book);
    }

    /**
     * Deletes a book from the system by its unique identifier.
     *
     * @param id The unique identifier of the book to be deleted.
     */
    @Override
    public void deleteBook(Long id) {
        bookDao.deleteBook(id);
    }
}