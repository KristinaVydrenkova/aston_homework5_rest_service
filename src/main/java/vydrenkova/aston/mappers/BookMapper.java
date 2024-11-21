package vydrenkova.aston.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import vydrenkova.aston.dto.BookDTO;
import vydrenkova.aston.entities.Book;

/**
 * The BookMapper interface defines the mapping between Book entities and BookDTO objects.
 * It uses the MapStruct framework to automatically generate the implementation for converting
 * between the two types.
 */
@Mapper
public interface BookMapper {

    /**
     * The singleton instance of the BookMapper.
     */
    BookMapper INSTANCE = Mappers.getMapper(BookMapper.class);

    /**
     * Converts a Book entity to a BookDTO.
     *
     * @param book The Book entity to be converted.
     * @return The corresponding BookDTO.
     */
    BookDTO toDTO(Book book);

    /**
     * Converts a BookDTO to a Book entity.
     *
     * @param bookDTO The BookDTO to be converted.
     * @return The corresponding Book entity.
     */
    Book toEntity(BookDTO bookDTO);
}
