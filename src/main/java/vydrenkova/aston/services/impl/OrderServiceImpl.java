package vydrenkova.aston.services.impl;

import vydrenkova.aston.dao.OrderDao;
import vydrenkova.aston.dto.BookDTO;
import vydrenkova.aston.dto.OrderDTO;
import vydrenkova.aston.entities.Order;
import vydrenkova.aston.mappers.BookMapper;
import vydrenkova.aston.mappers.OrderMapper;
import vydrenkova.aston.services.OrderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The OrderServiceImpl class is an implementation of the OrderService interface. It provides concrete
 * implementations for handling operations related to orders. This service layer interacts with the
 * OrderDao for data access and uses the OrderMapper and BookMapper for converting between Order entities
 * and OrderDTOs, as well as between Book entities and BookDTOs.
 */
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final OrderMapper orderMapper;
    private final BookMapper bookMapper;

    /**
     * Constructs a new OrderServiceImpl with the specified OrderDao, OrderMapper, and BookMapper.
     *
     * @param orderDao     The OrderDao to be used for data access.
     * @param orderMapper  The OrderMapper to be used for mapping between entities and DTOs.
     * @param bookMapper   The BookMapper to be used for mapping between book entities and DTOs.
     */
    public OrderServiceImpl(OrderDao orderDao, OrderMapper orderMapper, BookMapper bookMapper) {
        this.orderDao = orderDao;
        this.orderMapper = orderMapper;
        this.bookMapper = bookMapper;
    }

    /**
     * Retrieves an order by its unique identifier.
     *
     * @param id The unique identifier of the order.
     * @return An Optional containing the OrderDTO if found, or an empty Optional if not found.
     */
    @Override
    public Optional<OrderDTO> getOrderById(Long id) {
        Optional<Order> order = orderDao.getOrderById(id);
        if (order.isPresent()) {
            OrderDTO orderDTO = orderMapper.toDTO(order.get());
            List<BookDTO> books = order.get().getBooks().stream()
                    .map(bookMapper::toDTO)
                    .collect(Collectors.toList());
            orderDTO.setBooks(books);
            return Optional.of(orderDTO);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Retrieves a list of all orders available in the system.
     *
     * @return A list of OrderDTO objects representing all orders.
     */
    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderDao.getAllOrders();
        List<OrderDTO> orderDTOS = new ArrayList<>();
        for (Order order : orders) {
            OrderDTO orderDTO = orderMapper.toDTO(order);
            List<BookDTO> books = order.getBooks().stream()
                    .map(bookMapper::toDTO)
                    .collect(Collectors.toList());
            orderDTO.setBooks((ArrayList<BookDTO>) books);
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    /**
     * Creates a new order in the system.
     *
     * @param orderDTO The OrderDTO object representing the order to be created.
     */
    @Override
    public void createOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        orderDao.createOrder(order);
    }

    /**
     * Updates an existing order in the system.
     *
     * @param orderDTO The OrderDTO object representing the order to be updated.
     */
    @Override
    public void updateOrder(OrderDTO orderDTO) {
        Order order = orderMapper.toEntity(orderDTO);
        orderDao.updateOrder(order);
    }

    /**
     * Deletes an order from the system by its unique identifier.
     *
     * @param id The unique identifier of the order to be deleted.
     */
    @Override
    public void deleteOrder(Long id) {
        orderDao.deleteOrder(id);
    }
}