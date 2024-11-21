package vydrenkova.aston.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import vydrenkova.aston.config.ServiceFactory;
import vydrenkova.aston.dto.OrderDTO;
import vydrenkova.aston.services.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * The OrderServlet class is a servlet that handles HTTP requests related to orders.
 * It provides endpoints for retrieving, creating, updating, and deleting orders.
 * This servlet uses the OrderService to interact with the business logic layer and
 * the ObjectMapper to serialize and deserialize JSON data.
 */
@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {

    private ObjectMapper objectMapper;
    private OrderService orderService;

    public OrderServlet() {
        this.objectMapper = new ObjectMapper();
        this.orderService = ServiceFactory.getOrderService();
    }

    /**
     * Sets the ObjectMapper instance to be used by this servlet.
     *
     * @param objectMapper The ObjectMapper to be used.
     */
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Sets the OrderService instance to be used by this servlet.
     *
     * @param orderService The OrderService to be used.
     */
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Handles GET requests. Retrieves all orders or a specific order by ID.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the GET could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the GET request.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            getAllOrders(resp);
        } else {
            getOrderById(resp, pathInfo);
        }
    }

    /**
     * Handles POST requests. Creates a new order.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the POST could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the POST request.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderDTO orderDTO = objectMapper.readValue(req.getReader(), OrderDTO.class);
        orderService.createOrder(orderDTO);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }

    /**
     * Handles PUT requests. Updates an existing order.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the PUT could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the PUT request.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing order ID");
            return;
        }
        Long id = Long.parseLong(pathInfo.substring(1));
        OrderDTO orderDTO = objectMapper.readValue(req.getReader(), OrderDTO.class);
        orderDTO.setId(id);
        orderService.updateOrder(orderDTO);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Handles DELETE requests. Deletes an order by ID.
     *
     * @param req  The HttpServletRequest object.
     * @param resp The HttpServletResponse object.
     * @throws ServletException If the request for the DELETE could not be handled.
     * @throws IOException      If an input or output error is detected when the servlet handles the DELETE request.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing order ID");
            return;
        }
        Long id = Long.parseLong(pathInfo.substring(1));
        orderService.deleteOrder(id);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private void getOrderById(HttpServletResponse resp, String pathInfo) throws IOException {
        Long id = Long.parseLong(pathInfo.substring(1));
        Optional<OrderDTO> order = orderService.getOrderById(id);
        if (order.isPresent()) {
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(order.get()));
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
        }
    }

    private void getAllOrders(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(orderService.getAllOrders()));
    }
}