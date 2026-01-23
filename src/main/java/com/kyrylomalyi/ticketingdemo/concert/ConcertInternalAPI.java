package com.kyrylomalyi.ticketingdemo.concert;

public interface ConcertInternalAPI {

    boolean exists (Long concertId);

    void reserveSeats(Long concertId, int seats);

}
