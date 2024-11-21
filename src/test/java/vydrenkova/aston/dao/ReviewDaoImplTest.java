package vydrenkova.aston.dao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vydrenkova.aston.containers.PostgresTestContainer;
import vydrenkova.aston.dao.impl.BookDaoImpl;
import vydrenkova.aston.dao.impl.ReviewDaoImpl;
import vydrenkova.aston.entities.Book;
import vydrenkova.aston.entities.Review;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PostgresTestContainer.class)
public class ReviewDaoImplTest {

    private ReviewDao reviewDao;
    private BookDao bookDao;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        dataSource = PostgresTestContainer.getDataSource();
        reviewDao = new ReviewDaoImpl(dataSource);
        bookDao = new BookDaoImpl(PostgresTestContainer.getDataSource());
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM reviews");
            stmt.execute("DELETE FROM books");
        }
    }

    @Test
    public void testGetAllReviews() {
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        bookDao.createBook(book);

        Review review1 = new Review(null, book, "Reviewer1", 5, "Text1");
        Review review2 = new Review(null, book, "Reviewer2", 4, "Text2");
        reviewDao.createReview(review1);
        reviewDao.createReview(review2);

        List<Review> reviews = reviewDao.getAllReviews();

        assertEquals(2, reviews.size());
        assertTrue(reviews.contains(review1));
        assertTrue(reviews.contains(review2));
    }

    @Test
    public void testGetReviewById() {
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        bookDao.createBook(book);

        Review review = new Review(null, book, "Reviewer", 5, "Text");
        reviewDao.createReview(review);
        Long reviewId = review.getId();

        Optional<Review> foundReview = reviewDao.getReviewById(reviewId);

        assertTrue(foundReview.isPresent());
        assertEquals(reviewId, foundReview.get().getId());
        assertEquals("Reviewer", foundReview.get().getReviewer());
        assertEquals(5, foundReview.get().getRating());
        assertEquals("Text", foundReview.get().getText());
    }

    @Test
    public void testCreateReview() {
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        bookDao.createBook(book);

        Review review = new Review(null, book, "Reviewer", 5, "Text");

        reviewDao.createReview(review);

        assertNotNull(review.getId());
        Optional<Review> foundReview = reviewDao.getReviewById(review.getId());
        assertTrue(foundReview.isPresent());
        assertEquals("Reviewer", foundReview.get().getReviewer());
        assertEquals(5, foundReview.get().getRating());
        assertEquals("Text", foundReview.get().getText());
    }

    @Test
    public void testUpdateReview() {
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        bookDao.createBook(book);

        Review review = new Review(null, book, "Reviewer", 5, "Text");
        reviewDao.createReview(review);
        Long reviewId = review.getId();

        review.setReviewer("New Reviewer");
        review.setRating(4);
        review.setText("New Text");
        reviewDao.updateReview(review);

        Optional<Review> updatedReview = reviewDao.getReviewById(reviewId);
        assertTrue(updatedReview.isPresent());
        assertEquals("New Reviewer", updatedReview.get().getReviewer());
        assertEquals(4, updatedReview.get().getRating());
        assertEquals("New Text", updatedReview.get().getText());
    }

    @Test
    public void testDeleteReview() {
        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        bookDao.createBook(book);

        Review review = new Review(null, book, "Reviewer", 5, "Text");
        reviewDao.createReview(review);
        Long reviewId = review.getId();

        reviewDao.deleteReview(reviewId);

        Optional<Review> deletedReview = reviewDao.getReviewById(reviewId);
        assertFalse(deletedReview.isPresent());
    }
}
