package vydrenkova.aston.servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vydrenkova.aston.dto.BookDTO;
import vydrenkova.aston.services.BookService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServletTest {

    @Mock
    private BookService bookService;

    @Mock
    private ObjectMapper objectMapper;

    private BookServlet bookServlet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookServlet = new BookServlet();
        bookServlet.setBookService(bookService);
        bookServlet.setObjectMapper(objectMapper);
    }

    @Test
    public void testGetAllBooks() throws ServletException, IOException {
        BookDTO bookDTO1 = new BookDTO(1L, "Title1", "Author1", "Genre1", 10.0);
        BookDTO bookDTO2 = new BookDTO(2L, "Title2", "Author2", "Genre2", 20.0);
        List<BookDTO> books = Arrays.asList(bookDTO1, bookDTO2);

        when(bookService.getAllBooks()).thenReturn(books);
        when(objectMapper.writeValueAsString(books)).thenReturn("[{}]");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        bookServlet.doGet(request, response);

        writer.flush();
        String responseJson = stringWriter.toString();
        assertEquals("[{}]", responseJson);
    }


    @Test
    public void testGetBookById() throws ServletException, IOException {
        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);
        when(bookService.getBookById(1L)).thenReturn(Optional.of(bookDTO));
        when(objectMapper.writeValueAsString(bookDTO)).thenReturn("{}");

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(writer);

        bookServlet.doGet(request, response);

        writer.flush();
        String responseJson = stringWriter.toString();
        assertEquals("{}", responseJson);
    }

    @Test
    public void testGetBookByIdNotFound() throws ServletException, IOException {
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));

        bookServlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
    }

    @Test
    public void testCreateBook() throws ServletException, IOException {
        BookDTO bookDTO = new BookDTO(null, "Title", "Author", "Genre", 15.0);
        String json = "{\"title\":\"Title\",\"author\":\"Author\",\"genre\":\"Genre\",\"price\":15.0}";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(objectMapper.readValue(any(java.io.Reader.class), eq(BookDTO.class))).thenReturn(bookDTO); // Mock the JSON deserialization

        bookServlet.doPost(request, response);

        verify(bookService, times(1)).createBook(bookDTO);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testUpdateBook() throws ServletException, IOException {
        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);
        String json = "{\"id\":1,\"title\":\"Title\",\"author\":\"Author\",\"genre\":\"Genre\",\"price\":15.0}";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(objectMapper.readValue(any(java.io.Reader.class), eq(BookDTO.class))).thenReturn(bookDTO); // Mock the JSON deserialization

        bookServlet.doPut(request, response);

        verify(bookService, times(1)).updateBook(bookDTO);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDeleteBook() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/1");

        bookServlet.doDelete(request, response);

        verify(bookService, times(1)).deleteBook(1L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}