package vydrenkova.aston.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vydrenkova.aston.dto.ReviewDTO;
import vydrenkova.aston.services.ReviewService;

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

public class ReviewServletTest {

    @Mock
    private ReviewService reviewService;

    @Mock
    private ObjectMapper objectMapper;

    private ReviewServlet reviewServlet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewServlet = new ReviewServlet();
        reviewServlet.setReviewService(reviewService);
        reviewServlet.setObjectMapper(objectMapper);
    }

    @Test
    public void testGetAllReviews() throws ServletException, IOException {
        ReviewDTO reviewDTO1 = new ReviewDTO(1L, null, "Reviewer1", 5, "Text1");
        ReviewDTO reviewDTO2 = new ReviewDTO(2L, null, "Reviewer2", 4, "Text2");
        List<ReviewDTO> reviews = Arrays.asList(reviewDTO1, reviewDTO2);

        when(reviewService.getAllReviews()).thenReturn(reviews);
        when(objectMapper.writeValueAsString(reviews)).thenReturn("[{}]"); // Mock the JSON serialization

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        reviewServlet.doGet(request, response);

        writer.flush();
        String responseJson = stringWriter.toString();
        assertEquals("[{}]", responseJson);
    }

    @Test
    public void testGetReviewById() throws ServletException, IOException {
        ReviewDTO reviewDTO = new ReviewDTO(1L, null, "Reviewer", 5, "Text");
        when(reviewService.getReviewById(1L)).thenReturn(Optional.of(reviewDTO));
        when(objectMapper.writeValueAsString(reviewDTO)).thenReturn("{}"); // Mock the JSON serialization

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(writer);

        reviewServlet.doGet(request, response);

        writer.flush();
        String responseJson = stringWriter.toString();
        assertEquals("{}", responseJson);
    }

    @Test
    public void testGetReviewByIdNotFound() throws ServletException, IOException {
        when(reviewService.getReviewById(1L)).thenReturn(Optional.empty());

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));

        reviewServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
    }

    @Test
    public void testCreateReview() throws ServletException, IOException {
        ReviewDTO reviewDTO = new ReviewDTO(null, null, "Reviewer", 5, "Text");
        String json = "{\"reviewer\":\"Reviewer\",\"rating\":5,\"text\":\"Text\"}";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(objectMapper.readValue(any(java.io.Reader.class), eq(ReviewDTO.class))).thenReturn(reviewDTO); // Mock the JSON deserialization

        reviewServlet.doPost(request, response);

        verify(reviewService, times(1)).createReview(reviewDTO);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testUpdateReview() throws ServletException, IOException {
        ReviewDTO reviewDTO = new ReviewDTO(1L, null, "Reviewer", 5, "Text");
        String json = "{\"id\":1,\"reviewer\":\"Reviewer\",\"rating\":5,\"text\":\"Text\"}";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(objectMapper.readValue(any(java.io.Reader.class), eq(ReviewDTO.class))).thenReturn(reviewDTO); // Mock the JSON deserialization

        reviewServlet.doPut(request, response);

        verify(reviewService, times(1)).updateReview(reviewDTO);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDeleteReview() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/1");

        reviewServlet.doDelete(request, response);

        verify(reviewService, times(1)).deleteReview(1L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}
