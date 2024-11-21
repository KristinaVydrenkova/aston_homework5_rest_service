package vydrenkova.aston.services.impl;

import vydrenkova.aston.dao.ReviewDao;
import vydrenkova.aston.dto.ReviewDTO;
import vydrenkova.aston.entities.Review;
import vydrenkova.aston.mappers.BookMapper;
import vydrenkova.aston.mappers.ReviewMapper;
import vydrenkova.aston.services.ReviewService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The ReviewServiceImpl class is an implementation of the ReviewService interface. It provides concrete
 * implementations for handling operations related to reviews. This service layer interacts with the
 * ReviewDao for data access and uses the ReviewMapper and BookMapper for converting between Review entities
 * and ReviewDTOs, as well as between Book entities and BookDTOs.
 */
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDao reviewDao;
    private final ReviewMapper reviewMapper;
    private final BookMapper bookMapper;

    /**
     * Constructs a new ReviewServiceImpl with the specified ReviewDao, ReviewMapper, and BookMapper.
     *
     * @param reviewDao    The ReviewDao to be used for data access.
     * @param reviewMapper The ReviewMapper to be used for mapping between entities and DTOs.
     * @param bookMapper   The BookMapper to be used for mapping between book entities and DTOs.
     */
    public ReviewServiceImpl(ReviewDao reviewDao, ReviewMapper reviewMapper, BookMapper bookMapper) {
        this.reviewDao = reviewDao;
        this.reviewMapper = reviewMapper;
        this.bookMapper = bookMapper;
    }

    /**
     * Retrieves a review by its unique identifier.
     *
     * @param id The unique identifier of the review.
     * @return An Optional containing the ReviewDTO if found, or an empty Optional if not found.
     */
    @Override
    public Optional<ReviewDTO> getReviewById(Long id) {
        Optional<Review> review = reviewDao.getReviewById(id);
        if (review.isPresent()) {
            ReviewDTO reviewDTO = reviewMapper.toDTO(review.get());
            reviewDTO.setBook(bookMapper.toDTO(review.get().getBook()));
            return Optional.of(reviewDTO);
        }
        return Optional.empty();
    }

    /**
     * Retrieves a list of all reviews available in the system.
     *
     * @return A list of ReviewDTO objects representing all reviews.
     */
    @Override
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewDao.getAllReviews();
        List<ReviewDTO> reviewDTOS = new ArrayList<>();
        for (Review review : reviews) {
            ReviewDTO reviewDTO = reviewMapper.toDTO(review);
            reviewDTO.setBook(bookMapper.toDTO(review.getBook()));
            reviewDTOS.add(reviewDTO);
        }
        return reviewDTOS;
    }

    /**
     * Creates a new review in the system.
     *
     * @param reviewDTO The ReviewDTO object representing the review to be created.
     */
    @Override
    public void createReview(ReviewDTO reviewDTO) {
        Review review = reviewMapper.toEntity(reviewDTO);
        reviewDao.createReview(review);
    }

    /**
     * Updates an existing review in the system.
     *
     * @param reviewDTO The ReviewDTO object representing the review to be updated.
     */
    @Override
    public void updateReview(ReviewDTO reviewDTO) {
        Review review = reviewMapper.toEntity(reviewDTO);
        reviewDao.updateReview(review);
    }

    /**
     * Deletes a review from the system by its unique identifier.
     *
     * @param id The unique identifier of the review to be deleted.
     */
    @Override
    public void deleteReview(Long id) {
        reviewDao.deleteReview(id);
    }
}