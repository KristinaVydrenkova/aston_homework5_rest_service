package vydrenkova.aston.dao;

import vydrenkova.aston.entities.Order;

import java.util.List;
import java.util.Optional;

/**
 * The OrderDao interface defines the contract for data access operations related to Order entities.
 * Implementing classes must provide methods to retrieve, create, update, and delete orders in the
 * underlying data store. Additionally, it includes methods to manage the relationship between
 * orders and books.
 */
public interface OrderDao {

    /**
     * Retrieves a list of all orders available in the data store.
     *
     * @return A list of Order entities, or an empty list if no orders are found.
     */
    List<Order> getAllOrders();

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id The unique identifier of the order.
     * @return An Optional containing the Order entity if found, or an empty Optional if not found.
     */
    Optional<Order> getOrderById(Long id);

    /**
     * Creates a new order in the data store.
     *
     * @param order The Order entity to be created.
     */
    void createOrder(Order order);

    /**
     * Updates an existing order in the data store.
     *
     * @param order The Order entity to be updated.
     */
    void updateOrder(Order order);

    /**
     * Deletes an order from the data store by its unique identifier.
     *
     * @param id The unique identifier of the order to be deleted.
     */
    void deleteOrder(Long id);

    /**
     * Adds a book to an existing order.
     *
     * @param orderId The unique identifier of the order.
     * @param bookId  The unique identifier of the book to be added.
     */
    void addBookToOrder(Long orderId, Long bookId);

    /**
     * Removes a book from an existing order.
     *
     * @param orderId The unique identifier of the order.
     * @param bookId  The unique identifier of the book to be removed.
     */
    void removeBookFromOrder(Long orderId, Long bookId);
}