package vydrenkova.aston.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import vydrenkova.aston.config.ServiceFactory;
import vydrenkova.aston.dto.ReviewDTO;
import vydrenkova.aston.services.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The ReviewServlet class is a servlet that handles HTTP requests related to reviews.
 * It provides endpoints for retrieving, creating, updating, and deleting reviews.
 * This servlet uses the ReviewService to interact with the business logic layer and
 * the ObjectMapper to serialize and deserialize JSON data.
 */
@WebServlet("/reviews/*")
public class ReviewServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private ReviewService reviewService;

    public ReviewServlet() {
        this.reviewService = ServiceFactory.getReviewService();
        this.objectMapper = new ObjectMapper();
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
     * Sets the ReviewService instance to be used by this servlet.
     *
     * @param reviewService The ReviewService to be used.
     */
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Handles GET requests. Retrieves all reviews or a specific review by ID.
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
            getAllReviews(resp);
        } else {
            getReviewById(resp, pathInfo);
        }
    }

    /**
     * Handles POST requests. Creates a new review.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the POST could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the POST request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ReviewDTO reviewDTO = objectMapper.readValue(req.getReader(), ReviewDTO.class);
        reviewService.createReview(reviewDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Handles PUT requests. Updates an existing review.
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
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing review ID");
            return;
        }
        Long id = Long.parseLong(pathInfo.substring(1));
        ReviewDTO reviewDTO = objectMapper.readValue(req.getReader(), ReviewDTO.class);
        reviewDTO.setId(id);
        reviewService.updateReview(reviewDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Handles DELETE requests. Deletes a review by ID.
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
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing review ID");
            return;
        }
        Long id = Long.parseLong(pathInfo.substring(1));
        reviewService.deleteReview(id);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void getReviewById(HttpServletResponse resp, String pathInfo) throws IOException {
        Long id = Long.parseLong(pathInfo.substring(1));
        Optional<ReviewDTO> review = reviewService.getReviewById(id);
        if (review.isPresent()) {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(review.get()));
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Review not found");
        }
    }

    private void getAllReviews(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(reviewService.getAllReviews()));
    }
}