package vydrenkova.aston.entities;

import java.util.Objects;

public class Review {
    private Long id;
    private Book book;
    private String reviewer;
    private Integer rating;
    private String text;

    public Review(Long id, Book book, String reviewer, Integer rating, String text) {
        this.id = id;
        this.book = book;
        this.reviewer = reviewer;
        this.rating = rating;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return Objects.equals(id, review.id) && Objects.equals(book, review.book) && Objects.equals(reviewer, review.reviewer) && Objects.equals(rating, review.rating) && Objects.equals(text, review.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, reviewer, rating, text);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", book=" + book +
                ", reviewer='" + reviewer + '\'' +
                ", rating=" + rating +
                ", text='" + text + '\'' +
                '}';
    }
}
