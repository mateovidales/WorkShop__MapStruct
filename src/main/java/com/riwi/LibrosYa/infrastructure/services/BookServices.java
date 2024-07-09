package com.riwi.LibrosYa.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.riwi.LibrosYa.api.dto.request.BookRequest;
import com.riwi.LibrosYa.api.dto.response.BookResponse;
import com.riwi.LibrosYa.domain.entities.Book;
import com.riwi.LibrosYa.domain.repositories.BookRepository;
import com.riwi.LibrosYa.infrastructure.abstrac_services.IBookServices;
import com.riwi.LibrosYa.infrastructure.helpers.mappers.BookMapper;
import com.riwi.LibrosYa.utils.exceptions.BadRequestException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookServices implements IBookServices{

    
    @Autowired
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    @Override
    public BookResponse get(Long id) {
        return this.bookMapper.toBookResponse(this.findId(id));
    }

    @Override
    public Page<BookResponse> getAll(int page, int size) {
        if (page < 0) page = 0;
        PageRequest pagination = PageRequest.of(page, size);
        return this.bookRepository.findAll(pagination)
                .map(book -> this.bookMapper.toBookResponse(book));
    }

   
    @Override
    public BookResponse create(BookRequest request) {
        Book bookEntity = this.bookMapper.toBookEntity(request);
        return this.bookMapper.toBookResponse(this.bookRepository.save(bookEntity));
    }

    @Override
    public BookResponse update(BookRequest request, Long id) {
        Book bookEntity = this.findId(id);
        bookEntity = this.bookMapper.toBookEntity(request);
        bookEntity.setId(id);
        return this.bookMapper.toBookResponse(this.bookRepository.save(bookEntity));
    }

    @Override
    public void delete(Long id) {
        this.bookRepository.delete(this.findId(id));
    }


    private Book findId(Long id) {
        return this.bookRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No hay un libro con ese id"));
    }
}
