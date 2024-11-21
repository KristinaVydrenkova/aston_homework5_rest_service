package vydrenkova.aston.dto;


import java.util.Date;
import java.util.List;

public class OrderDTO {
    private Long id;
    private String customer;
    private Date date;
    private String status;
    private List<BookDTO> books;

    public OrderDTO(Long id, String customer, Date date, String status, List<BookDTO> books) {
        this.id = id;
        this.customer = customer;
        this.date = date;
        this.status = status;
        this.books = books;
    }

    public OrderDTO(Long id, String customer, Date date, String status) {
        this.id = id;
        this.customer = customer;
        this.date = date;
        this.status = status;
    }

    public OrderDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", customer='" + customer + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", books=" + books +
                '}';
    }
}
