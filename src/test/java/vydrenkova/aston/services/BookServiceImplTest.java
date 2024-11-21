package vydrenkova.aston.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vydrenkova.aston.dao.BookDao;
import vydrenkova.aston.dto.BookDTO;
import vydrenkova.aston.entities.Book;
import vydrenkova.aston.mappers.BookMapper;
import vydrenkova.aston.services.impl.BookServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    @Mock
    private BookDao bookDao;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookDao, bookMapper);
    }

    @Test
    public void testGetAllBooks() {
        Book book1 = new Book(1L, "Title1", "Author1", "Genre1", 10.0);
        Book book2 = new Book(2L, "Title2", "Author2", "Genre2", 20.0);
        List<Book> books = Arrays.asList(book1, book2);

        BookDTO bookDTO1 = new BookDTO(1L, "Title1", "Author1", "Genre1", 10.0);
        BookDTO bookDTO2 = new BookDTO(2L, "Title2", "Author2", "Genre2", 20.0);

        when(bookDao.getAllBooks()).thenReturn(books);
        when(bookMapper.toDTO(book1)).thenReturn(bookDTO1);
        when(bookMapper.toDTO(book2)).thenReturn(bookDTO2);

        List<BookDTO> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals(bookDTO1, result.get(0));
        assertEquals(bookDTO2, result.get(1));
    }

    @Test
    public void testGetBookById() {
        Book book = new Book(1L, "Title", "Author", "Genre", 15.0);
        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);

        when(bookDao.getBookById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        Optional<BookDTO> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals(bookDTO, result.get());
    }

    @Test
    public void testGetBookByIdNotFound() {
        when(bookDao.getBookById(1L)).thenReturn(Optional.empty());
        Optional<BookDTO> result = bookService.getBookById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testCreateBook() {
        BookDTO bookDTO = new BookDTO(null, "Title", "Author", "Genre", 15.0);
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);

        when(bookMapper.toEntity(bookDTO)).thenReturn(book);
        bookService.createBook(bookDTO);
        verify(bookDao, times(1)).createBook(book);
    }

    @Test
    public void testUpdateBook() {
        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);
        Book book = new Book(1L, "Title", "Author", "Genre", 15.0);

        when(bookMapper.toEntity(bookDTO)).thenReturn(book);
        bookService.updateBook(bookDTO);
        verify(bookDao, times(1)).updateBook(book);
    }

    @Test
    public void testDeleteBook() {
        Long bookId = 1L;
        bookService.deleteBook(bookId);
        verify(bookDao, times(1)).deleteBook(bookId);
    }
}
