package vydrenkova.aston.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vydrenkova.aston.dao.ReviewDao;
import vydrenkova.aston.dto.BookDTO;
import vydrenkova.aston.dto.ReviewDTO;
import vydrenkova.aston.entities.Book;
import vydrenkova.aston.entities.Review;
import vydrenkova.aston.mappers.BookMapper;
import vydrenkova.aston.mappers.ReviewMapper;
import vydrenkova.aston.services.impl.ReviewServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceImplTest {

    @Mock
    private ReviewDao reviewDao;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private BookMapper bookMapper;

    private ReviewServiceImpl reviewService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewService = new ReviewServiceImpl(reviewDao, reviewMapper, bookMapper);
    }

    @Test
    public void testGetReviewById() {
        Book book = new Book(1L, "Title", "Author", "Genre", 15.0);
        Review review = new Review(1L, book, "Reviewer", 5, "Text");

        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);
        ReviewDTO reviewDTO = new ReviewDTO(1L, bookDTO, "Reviewer", 5, "Text");

        when(reviewDao.getReviewById(1L)).thenReturn(Optional.of(review));
        when(reviewMapper.toDTO(review)).thenReturn(reviewDTO);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        Optional<ReviewDTO> result = reviewService.getReviewById(1L);

        assertTrue(result.isPresent());
        assertEquals(reviewDTO, result.get());
        assertEquals(bookDTO, result.get().getBook());
    }

    @Test
    public void testGetReviewByIdNotFound() {
        when(reviewDao.getReviewById(1L)).thenReturn(Optional.empty());
        Optional<ReviewDTO> result = reviewService.getReviewById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllReviews() {
        Book book1 = new Book(1L, "Title1", "Author1", "Genre1", 15.0);
        Book book2 = new Book(2L, "Title2", "Author2", "Genre2", 20.0);

        Review review1 = new Review(1L, book1, "Reviewer1", 5, "Text1");
        Review review2 = new Review(2L, book2, "Reviewer2", 4, "Text2");
        List<Review> reviews = Arrays.asList(review1, review2);

        BookDTO bookDTO1 = new BookDTO(1L, "Title1", "Author1", "Genre1", 15.0);
        BookDTO bookDTO2 = new BookDTO(2L, "Title2", "Author2", "Genre2", 20.0);

        ReviewDTO reviewDTO1 = new ReviewDTO(1L, bookDTO1, "Reviewer1", 5, "Text1");
        ReviewDTO reviewDTO2 = new ReviewDTO(2L, bookDTO2, "Reviewer2", 4, "Text2");

        when(reviewDao.getAllReviews()).thenReturn(reviews);
        when(reviewMapper.toDTO(review1)).thenReturn(reviewDTO1);
        when(reviewMapper.toDTO(review2)).thenReturn(reviewDTO2);
        when(bookMapper.toDTO(book1)).thenReturn(bookDTO1);
        when(bookMapper.toDTO(book2)).thenReturn(bookDTO2);

        List<ReviewDTO> result = reviewService.getAllReviews();

        assertEquals(2, result.size());
        assertEquals(reviewDTO1, result.get(0));
        assertEquals(reviewDTO2, result.get(1));
        assertEquals(bookDTO1, result.get(0).getBook());
        assertEquals(bookDTO2, result.get(1).getBook());
    }

    @Test
    public void testCreateReview() {
        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);
        ReviewDTO reviewDTO = new ReviewDTO(null, bookDTO, "Reviewer", 5, "Text");

        Book book = new Book(1L, "Title", "Author", "Genre", 15.0);
        Review review = new Review(null, book, "Reviewer", 5, "Text");

        when(reviewMapper.toEntity(reviewDTO)).thenReturn(review);
        reviewService.createReview(reviewDTO);
        verify(reviewDao, times(1)).createReview(review);
    }

    @Test
    public void testUpdateReview() {
        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);
        ReviewDTO reviewDTO = new ReviewDTO(1L, bookDTO, "Reviewer", 5, "Text");

        Book book = new Book(1L, "Title", "Author", "Genre", 15.0);
        Review review = new Review(1L, book, "Reviewer", 5, "Text");

        when(reviewMapper.toEntity(reviewDTO)).thenReturn(review);
        reviewService.updateReview(reviewDTO);
        verify(reviewDao, times(1)).updateReview(review);
    }

    @Test
    public void testDeleteReview() {
        Long reviewId = 1L;
        reviewService.deleteReview(reviewId);
        verify(reviewDao, times(1)).deleteReview(reviewId);
    }
}