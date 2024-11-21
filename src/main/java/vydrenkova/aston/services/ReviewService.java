package vydrenkova.aston.services;

import vydrenkova.aston.dto.ReviewDTO;

import java.util.List;
import java.util.Optional;

/**
 * The ReviewService interface defines the contract for services that handle operations
 * related to reviews. Implementing classes must provide functionality for creating,
 * retrieving, updating, and deleting review entities.
 */
public interface ReviewService {

    /**
     * Retrieves a review by its unique identifier.
     *
     * @param id The unique identifier of the review.
     * @return An Optional containing the ReviewDTO if found, or an empty Optional if not found.
     */
    Optional<ReviewDTO> getReviewById(Long id);

    /**
     * Retrieves a list of all reviews available in the system.
     *
     * @return A list of ReviewDTO objects representing all reviews.
     */
    List<ReviewDTO> getAllReviews();

    /**
     * Creates a new review in the system.
     *
     * @param reviewDTO The ReviewDTO object representing the review to be created.
     */
    void createReview(ReviewDTO reviewDTO);

    /**
     * Updates an existing review in the system.
     *
     * @param reviewDTO The ReviewDTO object representing the review to be updated.
     */
    void updateReview(ReviewDTO reviewDTO);

    /**
     * Deletes a review from the system by its unique identifier.
     *
     * @param id The unique identifier of the review to be deleted.
     */
    void deleteReview(Long id);
}