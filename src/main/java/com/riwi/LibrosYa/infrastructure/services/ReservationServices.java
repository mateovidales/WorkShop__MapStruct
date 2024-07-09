package com.riwi.LibrosYa.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.riwi.LibrosYa.api.dto.request.ReservationRequest;
import com.riwi.LibrosYa.api.dto.response.ReservationResponse;
import com.riwi.LibrosYa.domain.entities.Book;
import com.riwi.LibrosYa.domain.entities.Reservation;
import com.riwi.LibrosYa.domain.entities.UserEntity;
import com.riwi.LibrosYa.domain.repositories.BookRepository;
import com.riwi.LibrosYa.domain.repositories.ReservationRepository;
import com.riwi.LibrosYa.domain.repositories.UserRepository;
import com.riwi.LibrosYa.infrastructure.abstrac_services.IReservationServices;
import com.riwi.LibrosYa.infrastructure.helpers.mappers.ReservationMapper;
import com.riwi.LibrosYa.utils.exceptions.BadRequestException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ReservationServices implements IReservationServices{
    
    
    @Autowired
    private final ReservationRepository reservationRepository;
    @Autowired
    private final ReservationMapper reservationMapper;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookRepository bookRepository;
    

    @Override
    public ReservationResponse get(Long id) {        
        return this.reservationMapper.entityToReservationResponse(this.findId(id));
    }

    
    @Override
    public Page<ReservationResponse> getAll(int page, int size) {
        if (page < 0) page = 0;
        PageRequest pagination = PageRequest.of(page, size);
        return this.reservationRepository.findAll(pagination)
                .map(reservation -> this.reservationMapper.entityToReservationResponse(reservation));
    }

    
    @Override
    public ReservationResponse create(ReservationRequest request) {
    UserEntity userEntity = this.userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new BadRequestException("No hay un usuario con ese id"));

        Book book = this.bookRepository.findById(request.getBookId())
                    .orElseThrow(() -> new BadRequestException("No hay un Libro con ese Id"));
        Reservation reservation = this.reservationMapper.reservationReqToEntity(request);
        reservation.setUserId(userEntity);
        reservation.setBookId(book);
        return this.reservationMapper.entityToReservationResponse(this.reservationRepository.save(reservation));
    }


    @Override
    public ReservationResponse update(ReservationRequest request, Long id) {

        Reservation reservation = this.findId(id);
        UserEntity userEntity = this.userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BadRequestException("No hay un usuario con ese id"));

        Book book = this.bookRepository.findById(request.getBookId())
            .orElseThrow(() -> new BadRequestException("No hay un Libro con ese Id"));


        reservation.setBookId(book);
        reservation.setStatus(request.isStatus());
        reservation.setUserId(userEntity);
        return this.reservationMapper.entityToReservationResponse(this.reservationRepository.save(reservation));
    }

    @Override
    public void delete(Long id) {
        this.reservationRepository.delete(this.findId(id));
    }


    private Reservation findId(Long id) {
        return this.reservationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("No hay una reservation con ese id"));
    }
    
}
