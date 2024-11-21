package vydrenkova.aston.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import vydrenkova.aston.dto.ReviewDTO;
import vydrenkova.aston.entities.Review;

/**
 * The ReviewMapper interface defines the mapping between Review entities and ReviewDTO objects.
 * It uses the MapStruct framework to automatically generate the implementation for converting
 * between the two types. The mapping of the 'book' field is explicitly ignored in this mapper.
 */
@Mapper
public interface ReviewMapper {

    /**
     * The singleton instance of the ReviewMapper.
     */
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    /**
     * Converts a Review entity to a ReviewDTO. The 'book' field is ignored during the conversion.
     *
     * @param review The Review entity to be converted.
     * @return The corresponding ReviewDTO.
     */
    @Mapping(target = "book", ignore = true)
    ReviewDTO toDTO(Review review);

    /**
     * Converts a ReviewDTO to a Review entity. The 'book' field is ignored during the conversion.
     *
     * @param reviewDTO The ReviewDTO to be converted.
     * @return The corresponding Review entity.
     */
    @Mapping(target = "book", ignore = true)
    Review toEntity(ReviewDTO reviewDTO);
}