package vydrenkova.aston.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vydrenkova.aston.dto.OrderDTO;
import vydrenkova.aston.entities.Order;

/**
 * The OrderMapper interface defines the mapping between Order entities and OrderDTO objects.
 * It uses the MapStruct framework to automatically generate the implementation for converting
 * between the two types. The mapping of the 'books' field is explicitly ignored in this mapper.
 */
@Mapper
public interface OrderMapper {

    /**
     * The singleton instance of the OrderMapper.
     */
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    /**
     * Converts an Order entity to an OrderDTO. The 'books' field is ignored during the conversion.
     *
     * @param order The Order entity to be converted.
     * @return The corresponding OrderDTO.
     */
    @Mapping(target = "books", ignore = true)
    OrderDTO toDTO(Order order);

    /**
     * Converts an OrderDTO to an Order entity. The 'books' field is ignored during the conversion.
     *
     * @param orderDTO The OrderDTO to be converted.
     * @return The corresponding Order entity.
     */
    @Mapping(target = "books", ignore = true)
    Order toEntity(OrderDTO orderDTO);
}