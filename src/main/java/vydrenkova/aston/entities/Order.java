package vydrenkova.aston.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long id;
    private String customer;
    private Date date;
    private String status;
    private List<Book> books;

    public Order(Long id, String customer, Date date, String status) {
        this.id = id;
        this.customer = customer;
        this.date = date;
        this.status = status;
        this.books = new ArrayList<>();
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

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        this.books.add(book);
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(customer, order.customer) && Objects.equals(date, order.date) && Objects.equals(status, order.status) && Objects.equals(books, order.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, date, status, books);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", customer='" + customer + '\'' +
                ", date=" + date +
                ", status='" + status + '\'' +
                ", books=" + books +
                '}';
    }
}
