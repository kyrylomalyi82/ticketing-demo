package com.kyrylomalyi.ticketingdemo.reservation;

public record CreateReservationRequesDTO(
        Long concertId,
        Long userId,
        int seats
)
{}
