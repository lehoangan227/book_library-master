package com.project.Book.mapper;

import com.project.Book.dto.request.BorrowCreateRequest;
import com.project.Book.dto.response.BorrowResponse;
import com.project.Book.entity.Borrowing;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BorrowMapper {
    Borrowing createDtoToEntity(BorrowCreateRequest borrowCreateRequest);
    BorrowResponse entityToResponseDTO(Borrowing borrow);
}
