package vydrenkova.aston.dao.impl;

import vydrenkova.aston.dao.OrderDao;
import vydrenkova.aston.entities.Book;
import vydrenkova.aston.entities.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The OrderDaoImpl class is an implementation of the OrderDao interface. It provides concrete
 * implementations for data access operations related to Order entities using a DataSource
 * to interact with the underlying database.
 */
public class OrderDaoImpl implements OrderDao {

    private final DataSource dataSource;

    private final static String SELECT_ALL_ORDERS = "SELECT o.id AS order_id, o.customer, o.date, o.status, " +
            "b.id AS book_id, b.title, b.author, b.genre, b.price " +
            "FROM orders o " +
            "LEFT JOIN order_books ob ON o.id = ob.order_id " +
            "LEFT JOIN books b ON ob.book_id = b.id";
    private final static String SELECT_ORDER_BY_ID = "SELECT o.id AS order_id, o.customer, o.date, o.status, " +
            "b.id AS book_id, b.title, b.author, b.genre, b.price " +
            "FROM orders o " +
            "LEFT JOIN order_books ob ON o.id = ob.order_id " +
            "LEFT JOIN books b ON ob.book_id = b.id " +
            "WHERE o.id = ?";
    private final static String INSERT_INTO_ORDERS = "INSERT INTO orders (customer, date, status) VALUES (?, ?, ?)";
    private final static String UPDATE_ORDER = "UPDATE orders SET customer = ?, date = ?, status = ? WHERE id = ?";
    private final static String DELETE_ORDER = "DELETE FROM orders WHERE id = ?";
    private final static String INSERT_INTO_ORDER_BOOKS = "INSERT INTO order_books (order_id, book_id) VALUES (?, ?)";
    private final static String DELETE_FROM_ORDER_BOOKS = "DELETE FROM order_books WHERE order_id = ? AND book_id = ?";

    private final static String ORDER_ID_COLUMN_NAME = "order_id";
    private final static String CUSTOMER_COLUMN_NAME = "customer";
    private final static String DATE_COLUMN_NAME = "date";
    private final static String STATUS_COLUMN_NAME = "status";
    private final static String BOOK_ID_COLUMN_NAME = "book_id";
    private final static String TITLE_COLUMN_NAME = "title";
    private final static String AUTHOR_COLUMN_NAME = "author";
    private final static String GENRE_COLUMN_NAME = "genre";
    private final static String PRICE_COLUMN_NAME = "price";


    /**
     * Constructs a new OrderDaoImpl with the specified DataSource.
     *
     * @param dataSource The DataSource to be used for database connections.
     */
    public OrderDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Retrieves a list of all orders available in the database.
     *
     * @return A list of Order entities, or an empty list if no orders are found.
     */
    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_ORDERS)) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getLong(ORDER_ID_COLUMN_NAME),
                        rs.getString(CUSTOMER_COLUMN_NAME),
                        rs.getTimestamp(DATE_COLUMN_NAME),
                        rs.getString(STATUS_COLUMN_NAME));
                orders.add(order);

                if (rs.getLong(BOOK_ID_COLUMN_NAME) != 0) {
                    Book book = new Book(
                            rs.getLong(BOOK_ID_COLUMN_NAME),
                            rs.getString(TITLE_COLUMN_NAME),
                            rs.getString(AUTHOR_COLUMN_NAME),
                            rs.getString(GENRE_COLUMN_NAME),
                            rs.getDouble(PRICE_COLUMN_NAME)
                    );
                    order.getBooks().add(book);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get all orders", e);
        }
        return orders;
    }

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id The unique identifier of the order.
     * @return An Optional containing the Order entity if found, or an empty Optional if not found.
     */
    @Override
    public Optional<Order> getOrderById(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ORDER_BY_ID)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order(
                            rs.getLong(ORDER_ID_COLUMN_NAME),
                            rs.getString(CUSTOMER_COLUMN_NAME),
                            rs.getTimestamp(DATE_COLUMN_NAME),
                            rs.getString(STATUS_COLUMN_NAME)
                    );
                    if (rs.getLong(BOOK_ID_COLUMN_NAME) != 0) {
                        Book book = new Book(
                                rs.getLong(BOOK_ID_COLUMN_NAME),
                                rs.getString(TITLE_COLUMN_NAME),
                                rs.getString(AUTHOR_COLUMN_NAME),
                                rs.getString(GENRE_COLUMN_NAME),
                                rs.getDouble(PRICE_COLUMN_NAME)
                        );
                        order.getBooks().add(book);
                    }
                    return Optional.of(order);
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get order by id", e);
        }
    }

    /**
     * Creates a new order in the database.
     *
     * @param order The Order entity to be created.
     */
    @Override
    public void createOrder(Order order) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_INTO_ORDERS, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, order.getCustomer());
            pstmt.setTimestamp(2, new Timestamp(order.getDate().getTime()));
            pstmt.setString(3, order.getStatus());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create order", e);
        }
    }

    /**
     * Updates an existing order in the database.
     *
     * @param order The Order entity to be updated.
     */
    @Override
    public void updateOrder(Order order) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_ORDER)) {
            pstmt.setString(1, order.getCustomer());
            pstmt.setTimestamp(2, new Timestamp(order.getDate().getTime()));
            pstmt.setString(3, order.getStatus());
            pstmt.setLong(4, order.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update order", e);
        }
    }

    /**
     * Deletes an order from the database by its unique identifier.
     *
     * @param id The unique identifier of the order to be deleted.
     */
    @Override
    public void deleteOrder(Long id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_ORDER)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete order", e);
        }
    }

    /**
     * Adds a book to an existing order.
     *
     * @param orderId The unique identifier of the order.
     * @param bookId  The unique identifier of the book to be added.
     */
    @Override
    public void addBookToOrder(Long orderId, Long bookId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_INTO_ORDER_BOOKS)) {
            pstmt.setLong(1, orderId);
            pstmt.setLong(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to add book to order", e);
        }
    }

    /**
     * Removes a book from an existing order.
     *
     * @param orderId The unique identifier of the order.
     * @param bookId  The unique identifier of the book to be removed.
     */
    @Override
    public void removeBookFromOrder(Long orderId, Long bookId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_FROM_ORDER_BOOKS)) {
            pstmt.setLong(1, orderId);
            pstmt.setLong(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to remove book from order", e);
        }
    }
}