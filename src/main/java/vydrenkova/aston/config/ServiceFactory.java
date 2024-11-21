package vydrenkova.aston.config;

import vydrenkova.aston.dao.BookDao;
import vydrenkova.aston.dao.OrderDao;
import vydrenkova.aston.dao.ReviewDao;
import vydrenkova.aston.dao.impl.BookDaoImpl;
import vydrenkova.aston.dao.impl.OrderDaoImpl;
import vydrenkova.aston.dao.impl.ReviewDaoImpl;
import vydrenkova.aston.mappers.BookMapper;
import vydrenkova.aston.mappers.OrderMapper;
import vydrenkova.aston.mappers.ReviewMapper;
import vydrenkova.aston.services.BookService;
import vydrenkova.aston.services.OrderService;
import vydrenkova.aston.services.ReviewService;
import vydrenkova.aston.services.impl.BookServiceImpl;
import vydrenkova.aston.services.impl.OrderServiceImpl;
import vydrenkova.aston.services.impl.ReviewServiceImpl;

import javax.sql.DataSource;

/**
 * The ServiceFactory class is a utility class that provides static methods to obtain
 * instances of service classes. Each service class is configured with its corresponding
 * DAO and Mapper instances, ensuring that the services have access to the necessary
 * data access and mapping functionalities.
 */
public class ServiceFactory {

    private static final DataSource dataSource = DataSourceConfig.getDataSource();
    private static final BookDao bookDao = new BookDaoImpl(dataSource);
    private static final BookMapper bookMapper = BookMapper.INSTANCE;
    private static final OrderDao orderDao = new OrderDaoImpl(dataSource);
    private static final OrderMapper orderMapper = OrderMapper.INSTANCE;
    private static final ReviewDao reviewDao = new ReviewDaoImpl(dataSource);
    private static final ReviewMapper reviewMapper = ReviewMapper.INSTANCE;

    /**
     * Returns an instance of BookService, configured with the appropriate BookDao and BookMapper.
     *
     * @return An instance of BookService.
     */
    public static BookService getBookService() {
        return new BookServiceImpl(bookDao, bookMapper);
    }

    /**
     * Returns an instance of OrderService, configured with the appropriate OrderDao, OrderMapper,
     * and BookMapper.
     *
     * @return An instance of OrderService.
     */
    public static OrderService getOrderService(){
        return new OrderServiceImpl(orderDao, orderMapper, bookMapper);
    }

    /**
     * Returns an instance of ReviewService, configured with the appropriate ReviewDao, ReviewMapper,
     * and BookMapper.
     *
     * @return An instance of ReviewService.
     */
    public static ReviewService getReviewService(){
        return new ReviewServiceImpl(reviewDao, reviewMapper, bookMapper);
    }
}