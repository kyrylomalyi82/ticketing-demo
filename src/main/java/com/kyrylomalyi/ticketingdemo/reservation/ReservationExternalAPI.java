package com.kyrylomalyi.ticketingdemo.reservation;

public interface ReservationExternalAPI {

    ReservationDTO create(CreateReservationRequesDTO request);

    void confirm (Long reservationId);

    void cancel (Long reservationId);

}
