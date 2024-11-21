package vydrenkova.aston.dao.impl;

import vydrenkova.aston.dao.BookDao;
import vydrenkova.aston.entities.Book;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The BookDaoImpl class is an implementation of the BookDao interface. It provides concrete
 * implementations for data access operations related to Book entities using a DataSource
 * to interact with the underlying database.
 */
public class BookDaoImpl implements BookDao {

    private final DataSource dataSource;

    private final static String SELECT_FROM_BOOKS = "SELECT * FROM books";
    private final static String SELECT_FROM_BOOKS_WHERE_ID = "SELECT * FROM books WHERE id = ?";
    private final static String INSERT_INTO_BOOKS = "INSERT INTO books (title, author, genre, price) VALUES (?, ?, ?, ?)";
    private final static String UPDATE_BOOK = "UPDATE books SET title = ?, author = ?, genre = ?, price = ? WHERE id = ?";
    private final static String DELETE_BOOK = "DELETE FROM books WHERE id = ?";

    private final static String BOOK_ID_COLUMN_NAME = "id";
    private final static String TITLE_COLUMN_NAME = "title";
    private final static String AUTHOR_COLUMN_NAME = "author";
    private final static String GENRE_COLUMN_NAME = "genre";
    private final static String PRICE_COLUMN_NAME = "price";

    /**
     * Constructs a new BookDaoImpl with the specified DataSource.
     *
     * @param dataSource The DataSource to be used for database connections.
     */
    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Retrieves a list of all books available in the database.
     *
     * @return A list of Book entities, or an empty list if no books are found.
     */
    @Override
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_FROM_BOOKS)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getLong(BOOK_ID_COLUMN_NAME),
                        rs.getString(TITLE_COLUMN_NAME),
                        rs.getString(AUTHOR_COLUMN_NAME),
                        rs.getString(GENRE_COLUMN_NAME),
                        rs.getDouble(PRICE_COLUMN_NAME)
                );
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return books;
    }

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id The unique identifier of the book.
     * @return An Optional containing the Book entity if found, or an empty Optional if not found.
     */
    @Override
    public Optional<Book> getBookById(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_FROM_BOOKS_WHERE_ID)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Book(
                            rs.getLong(BOOK_ID_COLUMN_NAME),
                            rs.getString(TITLE_COLUMN_NAME),
                            rs.getString(AUTHOR_COLUMN_NAME),
                            rs.getString(GENRE_COLUMN_NAME),
                            rs.getDouble(PRICE_COLUMN_NAME)
                    ));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new book in the database.
     *
     * @param book The Book entity to be created.
     */
    @Override
    public void createBook(Book book) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_INTO_BOOKS, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setDouble(4, book.getPrice());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing book in the database.
     *
     * @param book The Book entity to be updated.
     */
    @Override
    public void updateBook(Book book) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_BOOK)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setDouble(4, book.getPrice());
            pstmt.setLong(5, book.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a book from the database by its unique identifier.
     *
     * @param id The unique identifier of the book to be deleted.
     */
    @Override
    public void deleteBook(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_BOOK)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}