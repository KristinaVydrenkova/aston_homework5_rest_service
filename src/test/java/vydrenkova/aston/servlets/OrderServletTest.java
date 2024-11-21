package vydrenkova.aston.servlets;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vydrenkova.aston.dto.OrderDTO;
import vydrenkova.aston.services.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServletTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ObjectMapper objectMapper;

    private OrderServlet orderServlet;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderServlet = new OrderServlet();
        orderServlet.setOrderService(orderService);
        orderServlet.setObjectMapper(objectMapper);
    }

    @Test
    public void testGetAllOrders() throws ServletException, IOException {
        OrderDTO orderDTO1 = new OrderDTO(1L, "Customer1", new Timestamp(System.currentTimeMillis()), "Status1");
        OrderDTO orderDTO2 = new OrderDTO(2L, "Customer2", new Timestamp(System.currentTimeMillis()), "Status2");
        List<OrderDTO> orders = Arrays.asList(orderDTO1, orderDTO2);

        when(orderService.getAllOrders()).thenReturn(orders);
        when(objectMapper.writeValueAsString(orders)).thenReturn("[{}]"); // Mock the JSON serialization

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        orderServlet.doGet(request, response);

        writer.flush();
        String responseJson = stringWriter.toString();
        assertEquals("[{}]", responseJson);
    }

    @Test
    public void testGetOrderById() throws ServletException, IOException {
        OrderDTO orderDTO = new OrderDTO(1L, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(orderDTO));
        when(objectMapper.writeValueAsString(orderDTO)).thenReturn("{}"); // Mock the JSON serialization

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doGet(request, response);

        writer.flush();
        String responseJson = stringWriter.toString();
        assertEquals("{}", responseJson);
    }

    @Test
    public void testGetOrderByIdNotFound() throws ServletException, IOException {
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(mock(PrintWriter.class));

        orderServlet.doGet(request, response);

        verify(response).sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
    }

    @Test
    public void testCreateOrder() throws ServletException, IOException {
        OrderDTO orderDTO = new OrderDTO(null, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        String json = "{\"customer\":\"Customer\",\"date\":\"" + orderDTO.getDate() + "\",\"status\":\"Status\"}";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(objectMapper.readValue(any(java.io.Reader.class), eq(OrderDTO.class))).thenReturn(orderDTO); // Mock the JSON deserialization

        orderServlet.doPost(request, response);

        verify(orderService, times(1)).createOrder(orderDTO);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    public void testUpdateOrder() throws ServletException, IOException {
        OrderDTO orderDTO = new OrderDTO(1L, "Customer", new Timestamp(System.currentTimeMillis()), "Status");
        String json = "{\"id\":1,\"customer\":\"Customer\",\"date\":\"" + orderDTO.getDate() + "\",\"status\":\"Status\"}";

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getReader()).thenReturn(new java.io.BufferedReader(new java.io.StringReader(json)));
        when(objectMapper.readValue(any(java.io.Reader.class), eq(OrderDTO.class))).thenReturn(orderDTO); // Mock the JSON deserialization

        orderServlet.doPut(request, response);

        verify(orderService, times(1)).updateOrder(orderDTO);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDeleteOrder() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(request.getPathInfo()).thenReturn("/1");

        orderServlet.doDelete(request, response);

        verify(orderService, times(1)).deleteOrder(1L);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
}