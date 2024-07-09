package com.riwi.LibrosYa.infrastructure.helpers.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import com.riwi.LibrosYa.api.dto.request.BookRequest;
import com.riwi.LibrosYa.api.dto.response.BookResponse;
import com.riwi.LibrosYa.domain.entities.Book;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {

    Book toBookEntity(BookRequest bookRequest);
    BookResponse toBookResponse(Book book);
    List<BookResponse> BookListToResponseList(List<Book> bookRequests); 
}
