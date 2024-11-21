package vydrenkova.aston.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import vydrenkova.aston.containers.PostgresTestContainer;
import vydrenkova.aston.dao.impl.BookDaoImpl;
import vydrenkova.aston.dao.impl.OrderDaoImpl;
import vydrenkova.aston.entities.Book;
import vydrenkova.aston.entities.Order;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PostgresTestContainer.class)
public class OrderDaoImplTest {

    private OrderDao orderDao;
    private DataSource dataSource;

    @BeforeEach
    public void setUp() {
        dataSource = PostgresTestContainer.getDataSource();
        orderDao = new OrderDaoImpl(dataSource);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM order_books");
            stmt.execute("DELETE FROM orders");
            stmt.execute("DELETE FROM books");
        }
    }

    @Test
    public void testGetAllOrders() {
        Order order1 = new Order(null, "Customer1", new Timestamp(System.currentTimeMillis()), "Status1");
        Order order2 = new Order(null, "Customer2", new Timestamp(System.currentTimeMillis()), "Status2");
        orderDao.createOrder(order1);
        orderDao.createOrder(order2);

        List<Order> orders = orderDao.getAllOrders();

        assertEquals(2, orders.size());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order(null, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        orderDao.createOrder(order);
        Long orderId = order.getId();

        Optional<Order> foundOrder = orderDao.getOrderById(orderId);

        assertTrue(foundOrder.isPresent());
        assertEquals(orderId, foundOrder.get().getId());
        assertEquals("Customer", foundOrder.get().getCustomer());
        assertEquals("Status", foundOrder.get().getStatus());
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order(null, "Customer", new Timestamp(System.currentTimeMillis()), "Status");

        orderDao.createOrder(order);

        assertNotNull(order.getId());
        Optional<Order> foundOrder = orderDao.getOrderById(order.getId());
        assertTrue(foundOrder.isPresent());
        assertEquals("Customer", foundOrder.get().getCustomer());
        assertEquals("Status", foundOrder.get().getStatus());
    }

    @Test
    public void testUpdateOrder() {
        Order order = new Order(null, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        orderDao.createOrder(order);
        Long orderId = order.getId();

        order.setCustomer("New Customer");
        order.setStatus("New Status");
        orderDao.updateOrder(order);

        Optional<Order> updatedOrder = orderDao.getOrderById(orderId);
        assertTrue(updatedOrder.isPresent());
        assertEquals("New Customer", updatedOrder.get().getCustomer());
        assertEquals("New Status", updatedOrder.get().getStatus());
    }

    @Test
    public void testDeleteOrder() {
        Order order = new Order(null, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        orderDao.createOrder(order);
        Long orderId = order.getId();

        orderDao.deleteOrder(orderId);

        Optional<Order> deletedOrder = orderDao.getOrderById(orderId);
        assertFalse(deletedOrder.isPresent());
    }

    @Test
    public void testAddBookToOrder() {
        Order order = new Order(null, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        orderDao.createOrder(order);
        Long orderId = order.getId();

        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        BookDao bookDao = new BookDaoImpl(PostgresTestContainer.getDataSource());
        bookDao.createBook(book);
        Long bookId = book.getId();

        orderDao.addBookToOrder(orderId, bookId);

        Optional<Order> foundOrder = orderDao.getOrderById(orderId);
        assertTrue(foundOrder.isPresent());
        assertEquals(1, foundOrder.get().getBooks().size());
        assertEquals(bookId, foundOrder.get().getBooks().get(0).getId());
    }

    @Test
    public void testRemoveBookFromOrder() {
        Order order = new Order(null, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        orderDao.createOrder(order);
        Long orderId = order.getId();

        Book book = new Book(null, "Title", "Author", "Genre", 15.0);
        BookDao bookDao = new BookDaoImpl(PostgresTestContainer.getDataSource());
        bookDao.createBook(book);
        Long bookId = book.getId();

        orderDao.addBookToOrder(orderId, bookId);

        orderDao.removeBookFromOrder(orderId, bookId);

        Optional<Order> foundOrder = orderDao.getOrderById(orderId);
        assertTrue(foundOrder.isPresent());
        assertEquals(0, foundOrder.get().getBooks().size());
    }
}
