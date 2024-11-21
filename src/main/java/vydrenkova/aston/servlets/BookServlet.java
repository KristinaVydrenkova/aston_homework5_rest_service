package vydrenkova.aston.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import vydrenkova.aston.config.ServiceFactory;
import vydrenkova.aston.dto.BookDTO;
import vydrenkova.aston.services.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * The BookServlet class is a servlet that handles HTTP requests related to books.
 * It provides endpoints for retrieving, creating, updating, and deleting books.
 * This servlet uses the BookService to interact with the business logic layer and
 * the ObjectMapper to serialize and deserialize JSON data.
 */
@WebServlet("/books/*")
public class BookServlet extends HttpServlet {

    private BookService bookService;
    private ObjectMapper objectMapper;

    public BookServlet() {
        this.bookService = ServiceFactory.getBookService();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Sets the BookService instance to be used by this servlet.
     *
     * @param bookService The BookService to be used.
     */
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Sets the ObjectMapper instance to be used by this servlet.
     *
     * @param objectMapper The ObjectMapper to be used.
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Handles GET requests. Retrieves all books or a specific book by ID.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the GET could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the GET request.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            getAllBooks(resp);
        } else {
            getBookById(resp, pathInfo);
        }
    }

    /**
     * Handles POST requests. Creates a new book.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the POST could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the POST request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookDTO bookDTO = objectMapper.readValue(req.getReader(), BookDTO.class);
        bookService.createBook(bookDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Handles PUT requests. Updates an existing book.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the PUT could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the PUT request.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing book ID");
            return;
        }
        Long id = Long.parseLong(pathInfo.substring(1));
        BookDTO bookDTO = objectMapper.readValue(req.getReader(), BookDTO.class);
        bookDTO.setId(id);
        bookService.updateBook(bookDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Handles DELETE requests. Deletes a book by ID.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the DELETE could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the DELETE request.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing book ID");
            return;
        }
        Long id = Long.parseLong(pathInfo.substring(1));
        bookService.deleteBook(id);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void getAllBooks(HttpServletResponse resp) throws IOException {
        List<BookDTO> books = bookService.getAllBooks();
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(books));
    }

    private void getBookById(HttpServletResponse resp, String pathInfo) throws IOException {
        Long id = Long.parseLong(pathInfo.substring(1));
        Optional<BookDTO> book = bookService.getBookById(id);
        if (book.isPresent()) {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(book.get()));
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
        }
    }
}