package vydrenkova.aston.dto;


public class ReviewDTO {
    private Long id;
    private BookDTO book;
    private String reviewer;
    private Integer rating;
    private String text;

    public ReviewDTO(Long id, BookDTO book, String reviewer, Integer rating, String text) {
        this.id = id;
        this.book = book;
        this.reviewer = reviewer;
        this.rating = rating;
        this.text = text;
    }

    public ReviewDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookDTO getBook() {
        return book;
    }

    public void setBook(BookDTO book) {
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
    public String toString() {
        return "ReviewDTO{" +
                "id=" + id +
                ", book=" + book +
                ", reviewer='" + reviewer + '\'' +
                ", rating=" + rating +
                ", text='" + text + '\'' +
                '}';
    }
}
