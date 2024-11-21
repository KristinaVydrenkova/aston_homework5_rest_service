package vydrenkova.aston.dao;

import vydrenkova.aston.entities.Review;

import java.util.List;
import java.util.Optional;

/**
 * The ReviewDao interface defines the contract for data access operations related to Review entities.
 * Implementing classes must provide methods to retrieve, create, update, and delete reviews in the
 * underlying data store.
 */
public interface ReviewDao {

    /**
     * Retrieves a list of all reviews available in the data store.
     *
     * @return A list of Review entities, or an empty list if no reviews are found.
     */
    List<Review> getAllReviews();

    /**
     * Retrieves a review by its unique identifier.
     *
     * @param id The unique identifier of the review.
     * @return An Optional containing the Review entity if found, or an empty Optional if not found.
     */
    Optional<Review> getReviewById(Long id);

    /**
     * Creates a new review in the data store.
     *
     * @param review The Review entity to be created.
     */
    void createReview(Review review);

    /**
     * Updates an existing review in the data store.
     *
     * @param review The Review entity to be updated.
     */
    void updateReview(Review review);

    /**
     * Deletes a review from the data store by its unique identifier.
     *
     * @param id The unique identifier of the review to be deleted.
     */
    void deleteReview(Long id);
}