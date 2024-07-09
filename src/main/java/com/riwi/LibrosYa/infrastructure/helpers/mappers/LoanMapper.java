package com.riwi.LibrosYa.infrastructure.helpers.mappers;

import java.util.List;

import com.riwi.LibrosYa.api.dto.request.LoanRequest;
import com.riwi.LibrosYa.api.dto.response.LoanResponse;
import com.riwi.LibrosYa.domain.entities.Loan;
import com.riwi.LibrosYa.domain.entities.UserEntity;
import com.riwi.LibrosYa.domain.entities.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoanMapper {

    @Mapping(target = "userId", source = "userId", qualifiedByName = "UserIdToUserEntity")
    @Mapping(target = "bookId", source = "bookId", qualifiedByName = "BookIdToBook")
    @Mapping(target = "id", ignore = true) 
    @Mapping(target = "loanDate", ignore = true) 
    @Mapping(target = "returnDate", ignore = true)
    Loan loanReqToEntity(LoanRequest loanRequest);

    @Named("UserIdToUserEntity")
    default UserEntity mapUserIdToUserEntity(Long userId) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        return userEntity;
    }

    @Named("BookIdToBook")
    default Book mapBookIdToBook(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }

    LoanResponse entityToLoanResponse(Loan loan);
    List<LoanResponse> toListLoanResp(List<Loan> loans);
}
