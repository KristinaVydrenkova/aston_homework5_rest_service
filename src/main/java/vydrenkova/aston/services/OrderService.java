package vydrenkova.aston.services;

import vydrenkova.aston.dto.OrderDTO;

import java.util.List;
import java.util.Optional;

/**
 * The OrderService interface defines the contract for services that handle operations
 * related to orders. Implementing classes must provide functionality for creating,
 * retrieving, updating, and deleting order entities.
 */
public interface OrderService {

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id The unique identifier of the order.
     * @return An Optional containing the OrderDTO if found, or an empty Optional if not found.
     */
    Optional<OrderDTO> getOrderById(Long id);

    /**
     * Retrieves a list of all orders available in the system.
     *
     * @return A list of OrderDTO objects representing all orders.
     */
    List<OrderDTO> getAllOrders();

    /**
     * Creates a new order in the system.
     *
     * @param orderDTO The OrderDTO object representing the order to be created.
     */
    void createOrder(OrderDTO orderDTO);

    /**
     * Updates an existing order in the system.
     *
     * @param orderDTO The OrderDTO object representing the order to be updated.
     */
    void updateOrder(OrderDTO orderDTO);

    /**
     * Deletes an order from the system by its unique identifier.
     *
     * @param id The unique identifier of the order to be deleted.
     */
    void deleteOrder(Long id);
}