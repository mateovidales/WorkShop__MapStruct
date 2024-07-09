package com.riwi.LibrosYa.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.riwi.LibrosYa.api.dto.request.LoanRequest;
import com.riwi.LibrosYa.api.dto.response.LoanResponse;
import com.riwi.LibrosYa.domain.entities.Book;
import com.riwi.LibrosYa.domain.entities.Loan;
import com.riwi.LibrosYa.domain.entities.UserEntity;
import com.riwi.LibrosYa.domain.repositories.BookRepository;
import com.riwi.LibrosYa.domain.repositories.LoanRepository;
import com.riwi.LibrosYa.domain.repositories.UserRepository;
import com.riwi.LibrosYa.infrastructure.abstrac_services.ILoanServices;
import com.riwi.LibrosYa.infrastructure.helpers.mappers.LoanMapper;
import com.riwi.LibrosYa.utils.exceptions.BadRequestException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LoanSercices implements ILoanServices{

    @Autowired
    private final LoanRepository loanRepository;
    @Autowired
    private final LoanMapper loanMapper;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookRepository bookRepository;

    
    @Override
    public LoanResponse get(Long id) {
        return this.loanMapper.entityToLoanResponse(this.findId(id));
    }

    
    @Override
    public Page<LoanResponse> getAll(int page, int size) {
      if (page < 0) page = 0;
        PageRequest pagination = PageRequest.of(page, size);
        return this.loanRepository.findAll(pagination)
                .map(loan -> this.loanMapper.entityToLoanResponse(loan));
    }

    
    @Override
    public LoanResponse create(LoanRequest request) {
        UserEntity userEntity = this.userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new BadRequestException("No hay un usuario con ese id"));

        
        Book book = this.bookRepository.findById(request.getBookId())
                    .orElseThrow(() -> new BadRequestException("No hay un Libro con ese Id"));
        Loan loan = this.loanMapper.loanReqToEntity(request);
        loan.setUserId(userEntity);
        loan.setBookId(book);
        return this.loanMapper.entityToLoanResponse(this.loanRepository.save(loan));
    }

    
    @Override
    public LoanResponse update(LoanRequest request, Long id) {
        Loan loan = this.findId(id);
        UserEntity userEntity = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BadRequestException("No hay un usuario con ese id"));
        Book book = this.bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new BadRequestException("No hay un Libro con ese Id"));
        loan.setBookId(book);
        loan.setUserId(userEntity);
        loan.setStatus(request.isStatus());
        return this.loanMapper.entityToLoanResponse(this.loanRepository.save(loan));
    }


    @Override
    public void delete(Long id) {
        this.loanRepository.delete(this.findId(id));
    }

    
    private Loan findId(Long id) {
        return this.loanRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No hay un libro con ese id"));
    }
}
