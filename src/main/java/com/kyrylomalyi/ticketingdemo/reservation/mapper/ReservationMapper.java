package com.kyrylomalyi.ticketingdemo.reservation.mapper;

import com.kyrylomalyi.ticketingdemo.reservation.ReservationDTO;
import com.kyrylomalyi.ticketingdemo.reservation.model.Reservation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    ReservationDTO toDto(Reservation reservation);

    Reservation toEntity (ReservationDTO reservationDTO);

}
