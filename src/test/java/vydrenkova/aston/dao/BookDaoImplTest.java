package vydrenkova.aston.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vydrenkova.aston.containers.PostgresTestContainer;
import vydrenkova.aston.dao.impl.BookDaoImpl;
import vydrenkova.aston.entities.Book;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PostgresTestContainer.class)
public class BookDaoImplTest {

    private BookDao bookDao;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        dataSource = PostgresTestContainer.getDataSource();
        bookDao = new BookDaoImpl(dataSource);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM books");
        }
    }

    @Test
    public void testGetAllBooks() {
        // Arrange
        Book book1 = new Book(null, "Title1", "Author1", "Genre1", 10.0);
        Book book2 = new Book(null, "Title2", "Author2", "Genre2", 20.0);
        bookDao.createBook(book1);
        bookDao.createBook(book2);

        // Act
        List<Book> books = bookDao.getAllBooks();

        // Assert
        assertEquals(2, books.size());
        assertTrue(books.contains(book1));
        assertTrue(books.contains(book2));
    }

    @Test
    public void testGetBookById() {
        // Arrange
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        bookDao.createBook(book);
        Long bookId = book.getId();

        // Act
        Optional<Book> foundBook = bookDao.getBookById(bookId);

        // Assert
        assertTrue(foundBook.isPresent());
        assertEquals(bookId, foundBook.get().getId());
        assertEquals("Title", foundBook.get().getTitle());
        assertEquals("Author", foundBook.get().getAuthor());
        assertEquals("Genre", foundBook.get().getGenre());
        assertEquals(15.0, foundBook.get().getPrice());
    }

    @Test
    public void testCreateBook() {
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);

        bookDao.createBook(book);

        assertNotNull(book.getId());
        Optional<Book> foundBook = bookDao.getBookById(book.getId());
        assertTrue(foundBook.isPresent());
        assertEquals("Title", foundBook.get().getTitle());
        assertEquals("Author", foundBook.get().getAuthor());
        assertEquals("Genre", foundBook.get().getGenre());
        assertEquals(15.0, foundBook.get().getPrice());
    }

    @Test
    public void testUpdateBook() {
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        bookDao.createBook(book);
        Long bookId = book.getId();

        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setGenre("New Genre");
        book.setPrice(20.0);
        bookDao.updateBook(book);

        Optional<Book> updatedBook = bookDao.getBookById(bookId);
        assertTrue(updatedBook.isPresent());
        assertEquals("New Title", updatedBook.get().getTitle());
        assertEquals("New Author", updatedBook.get().getAuthor());
        assertEquals("New Genre", updatedBook.get().getGenre());
        assertEquals(20.0, updatedBook.get().getPrice());
    }

    @Test
    public void testDeleteBook() {
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        bookDao.createBook(book);
        Long bookId = book.getId();

        bookDao.deleteBook(bookId);

        Optional<Book> deletedBook = bookDao.getBookById(bookId);
        assertFalse(deletedBook.isPresent());
    }
}