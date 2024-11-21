package vydrenkova.aston.dao.impl;

import vydrenkova.aston.dao.ReviewDao;
import vydrenkova.aston.entities.Book;
import vydrenkova.aston.entities.Review;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The ReviewDaoImpl class is an implementation of the ReviewDao interface. It provides concrete
 * implementations for data access operations related to Review entities using a DataSource
 * to interact with the underlying database.
 */
public class ReviewDaoImpl implements ReviewDao {

    private final DataSource dataSource;

    private final static String SELECT_ALL_FROM_REVIEWS = "SELECT r.id, r.book_id, r.reviewer, r.rating, r.text, " +
            "b.title, b.author, b.genre, b.price " +
            "FROM reviews r " +
            "JOIN books b ON r.book_id = b.id";
    private final static String SELECT_REVIEW_BY_ID = "SELECT r.id, r.book_id, r.reviewer, r.rating, r.text, " +
            "b.title, b.author, b.genre, b.price " +
            "FROM reviews r " +
            "JOIN books b ON r.book_id = b.id " +
            "WHERE r.id = ?";
    private final static String INSERT_INTO_REVIEWS = "INSERT INTO reviews (book_id, reviewer, rating, text) " +
            "VALUES (?, ?, ?, ?)";
    private final static String UPDATE_REVIEW = "UPDATE reviews SET book_id = ?, reviewer = ?, rating = ?, text = ? " +
            "WHERE id = ?";
    private final static String DELETE_FROM_REVIEW = "DELETE FROM reviews WHERE id = ?";

    private final static String ID_COLUMN_NAME = "id";
    private final static String REVIEWER_COLUMN_NAME = "reviewer";
    private final static String RATING_COLUMN_NAME = "rating";
    private final static String TEXT_COLUMN_NAME = "text";
    private final static String BOOK_ID_COLUMN_NAME = "book_id";
    private final static String TITLE_COLUMN_NAME = "title";
    private final static String AUTHOR_COLUMN_NAME = "author";
    private final static String GENRE_COLUMN_NAME = "genre";
    private final static String PRICE_COLUMN_NAME = "price";

    /**
     * Constructs a new ReviewDaoImpl with the specified DataSource.
     *
     * @param dataSource The DataSource to be used for database connections.
     */
    public ReviewDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Retrieves a list of all reviews available in the database.
     *
     * @return A list of Review entities, or an empty list if no reviews are found.
     */
    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_FROM_REVIEWS)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getLong(BOOK_ID_COLUMN_NAME),
                        rs.getString(TITLE_COLUMN_NAME),
                        rs.getString(AUTHOR_COLUMN_NAME),
                        rs.getString(GENRE_COLUMN_NAME),
                        rs.getDouble(PRICE_COLUMN_NAME)
                );
                Review review = new Review(
                        rs.getLong(ID_COLUMN_NAME),
                        book,
                        rs.getString(REVIEWER_COLUMN_NAME),
                        rs.getInt(RATING_COLUMN_NAME),
                        rs.getString(TEXT_COLUMN_NAME)
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all reviews", e);
        }
        return reviews;
    }

    /**
     * Retrieves a review by its unique identifier.
     *
     * @param id The unique identifier of the review.
     * @return An Optional containing the Review entity if found, or an empty Optional if not found.
     */
    @Override
    public Optional<Review> getReviewById(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_REVIEW_BY_ID)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book(
                            rs.getLong(BOOK_ID_COLUMN_NAME),
                            rs.getString(TITLE_COLUMN_NAME),
                            rs.getString(AUTHOR_COLUMN_NAME),
                            rs.getString(GENRE_COLUMN_NAME),
                            rs.getDouble(PRICE_COLUMN_NAME)
                    );
                    return Optional.of(new Review(
                            rs.getLong(ID_COLUMN_NAME),
                            book,
                            rs.getString(REVIEWER_COLUMN_NAME),
                            rs.getInt(RATING_COLUMN_NAME),
                            rs.getString(TEXT_COLUMN_NAME)
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get review by id", e);
        }
        return Optional.empty();
    }

    /**
     * Creates a new review in the database.
     *
     * @param review The Review entity to be created.
     */
    @Override
    public void createReview(Review review) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_INTO_REVIEWS, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, review.getBook().getId());
            pstmt.setString(2, review.getReviewer());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getText());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    review.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create review", e);
        }
    }

    /**
     * Updates an existing review in the database.
     *
     * @param review The Review entity to be updated.
     */
    @Override
    public void updateReview(Review review) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_REVIEW)) {
            pstmt.setLong(1, review.getBook().getId());
            pstmt.setString(2, review.getReviewer());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getText());
            pstmt.setLong(5, review.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update review", e);
        }
    }

    /**
     * Deletes a review from the database by its unique identifier.
     *
     * @param id The unique identifier of the review to be deleted.
     */
    @Override
    public void deleteReview(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_FROM_REVIEW)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete review", e);
        }
    }
}