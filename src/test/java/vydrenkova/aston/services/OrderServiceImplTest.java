package vydrenkova.aston.services;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vydrenkova.aston.dao.OrderDao;
import vydrenkova.aston.dto.BookDTO;
import vydrenkova.aston.dto.OrderDTO;
import vydrenkova.aston.entities.Book;
import vydrenkova.aston.entities.Order;
import vydrenkova.aston.mappers.BookMapper;
import vydrenkova.aston.mappers.OrderMapper;
import vydrenkova.aston.services.impl.OrderServiceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private BookMapper bookMapper;

    private OrderServiceImpl orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceImpl(orderDao, orderMapper, bookMapper);
    }

    @Test
    public void testGetOrderById() {
        Book book = new Book(1L, "Title", "Author", "Genre", 15.0);
        List<Book> books = Arrays.asList(book);
        Order order = new Order(1L, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        order.setBooks(books);

        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);
        List<BookDTO> bookDTOs = Arrays.asList(bookDTO);
        OrderDTO orderDTO = new OrderDTO(1L, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        orderDTO.setBooks(bookDTOs);

        when(orderDao.getOrderById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);
        when(bookMapper.toDTO(book)).thenReturn(bookDTO);

        Optional<OrderDTO> result = orderService.getOrderById(1L);

        assertTrue(result.isPresent());
        assertEquals(orderDTO, result.get());
        assertEquals(1, result.get().getBooks().size());
        assertEquals(bookDTO, result.get().getBooks().get(0));
    }

    @Test
    public void testGetOrderByIdNotFound() {
        when(orderDao.getOrderById(1L)).thenReturn(Optional.empty());
        Optional<OrderDTO> result = orderService.getOrderById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetAllOrders() {
        Book book1 = new Book(1L, "Title1", "Author1", "Genre1", 15.0);
        Book book2 = new Book(2L, "Title2", "Author2", "Genre2", 20.0);
        List<Book> books1 = Arrays.asList(book1);
        List<Book> books2 = Arrays.asList(book2);

        Order order1 = new Order(1L, "Customer1", new Timestamp(System.currentTimeMillis()), "Status1");
        order1.setBooks(books1);
        Order order2 = new Order(2L, "Customer2", new Timestamp(System.currentTimeMillis()), "Status2");
        order2.setBooks(books2);
        List<Order> orders = Arrays.asList(order1, order2);

        BookDTO bookDTO1 = new BookDTO(1L, "Title1", "Author1", "Genre1", 15.0);
        BookDTO bookDTO2 = new BookDTO(2L, "Title2", "Author2", "Genre2", 20.0);
        List<BookDTO> bookDTOs1 = Arrays.asList(bookDTO1);
        List<BookDTO> bookDTOs2 = Arrays.asList(bookDTO2);

        OrderDTO orderDTO1 = new OrderDTO(1L, "Customer1", new Timestamp(System.currentTimeMillis()), "Status1");
        orderDTO1.setBooks(bookDTOs1);
        OrderDTO orderDTO2 = new OrderDTO(2L, "Customer2", new Timestamp(System.currentTimeMillis()), "Status2");
        orderDTO2.setBooks(bookDTOs2);
        List<OrderDTO> orderDTOs = Arrays.asList(orderDTO1, orderDTO2);

        when(orderDao.getAllOrders()).thenReturn(orders);
        when(orderMapper.toDTO(order1)).thenReturn(orderDTO1);
        when(orderMapper.toDTO(order2)).thenReturn(orderDTO2);
        when(bookMapper.toDTO(book1)).thenReturn(bookDTO1);
        when(bookMapper.toDTO(book2)).thenReturn(bookDTO2);

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        assertEquals(orderDTO1, result.get(0));
        assertEquals(orderDTO2, result.get(1));
        assertEquals(1, result.get(0).getBooks().size());
        assertEquals(1, result.get(1).getBooks().size());
        assertEquals(bookDTO1, result.get(0).getBooks().get(0));
        assertEquals(bookDTO2, result.get(1).getBooks().get(0));
    }

    @Test
    public void testCreateOrder() {
        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);
        List<BookDTO> bookDTOs = Arrays.asList(bookDTO);
        OrderDTO orderDTO = new OrderDTO(null, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        orderDTO.setBooks(bookDTOs);

        Book book = new Book(1L, "Title", "Author", "Genre", 15.0);
        List<Book> books = Arrays.asList(book);
        Order order = new Order(null, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        order.setBooks(books);

        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        orderService.createOrder(orderDTO);
        verify(orderDao, times(1)).createOrder(order);
    }

    @Test
    public void testUpdateOrder() {
        BookDTO bookDTO = new BookDTO(1L, "Title", "Author", "Genre", 15.0);
        List<BookDTO> bookDTOs = Arrays.asList(bookDTO);
        OrderDTO orderDTO = new OrderDTO(1L, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        orderDTO.setBooks(bookDTOs);

        Book book = new Book(1L, "Title", "Author", "Genre", 15.0);
        List<Book> books = Arrays.asList(book);
        Order order = new Order(1L, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        order.setBooks(books);

        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        orderService.updateOrder(orderDTO);
        verify(orderDao, times(1)).updateOrder(order);
    }

    @Test
    public void testDeleteOrder() {
        Long orderId = 1L;
        orderService.deleteOrder(orderId);
        verify(orderDao, times(1)).deleteOrder(orderId);
    }
}