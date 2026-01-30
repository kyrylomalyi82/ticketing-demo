package com.kyrylomalyi.ticketingdemo.reservation.repository;

import com.kyrylomalyi.ticketingdemo.reservation.model.Reservation;
import com.kyrylomalyi.ticketingdemo.reservation.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByStatusAndExpiresAtBefore(
            ReservationStatus status,
            Instant time
    );
}
