package com.project.Book.mapper;

import com.project.Book.dto.request.BookRequest;
import com.project.Book.dto.request.BookUpdateRequest;
import com.project.Book.dto.response.BookInListResponse;
import com.project.Book.dto.response.BookResponse;
import com.project.Book.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {
    BookResponse entityToResponseDTO(Book book);
    Book requestDtoToEntity(BookRequest bookRequest);
    void updateDtoToEntity(@MappingTarget Book book, BookUpdateRequest bookUpdateRequest);
    BookInListResponse entityToBookInListDTO(Book book);
}
