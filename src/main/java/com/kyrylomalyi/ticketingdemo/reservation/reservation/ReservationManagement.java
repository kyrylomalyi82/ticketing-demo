package com.kyrylomalyi.ticketingdemo.reservation.reservation;

import com.kyrylomalyi.ticketingdemo.concert.ConcertInternalAPI;
import com.kyrylomalyi.ticketingdemo.exception.ResourceNotFoundException;
import com.kyrylomalyi.ticketingdemo.reservation.CreateReservationRequesDTO;
import com.kyrylomalyi.ticketingdemo.reservation.ReservationDTO;
import com.kyrylomalyi.ticketingdemo.reservation.ReservationExternalAPI;
import com.kyrylomalyi.ticketingdemo.reservation.ReservationInternalAPI;
import com.kyrylomalyi.ticketingdemo.reservation.mapper.ReservationMapper;
import com.kyrylomalyi.ticketingdemo.reservation.model.Reservation;
import com.kyrylomalyi.ticketingdemo.reservation.model.ReservationStatus;
import com.kyrylomalyi.ticketingdemo.reservation.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ReservationManagement implements ReservationExternalAPI, ReservationInternalAPI {
    private static final Duration LOCK_TIME = Duration.ofMinutes(15);

    private final ReservationRepository reservationRepository;
    private final ConcertInternalAPI concertApi;
    private final ReservationMapper mapper;

    @Override
    public ReservationDTO create(CreateReservationRequesDTO request) {
        // this method runs in a transaction (created by Spring)
        concertApi.reserveSeats(request.concertId(), request.seats());

        Reservation reservation = new Reservation(
                request.concertId(),
                request.userId(),
                request.seats(),
                ReservationStatus.PENDING,
                Instant.now(),
                Instant.now().plus(LOCK_TIME)
        );

        reservationRepository.save(reservation);
        return mapper.toDto(reservation);
    }

    @Override
    public void confirm(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));
        reservation.confirm();
    }

    @Override
    public void cancel(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", reservationId));

        concertApi.releaseSeats(reservation.getConcertId(), reservation.getSeats());
        reservation.cancel();
    }

    @Override
    public void releaseExpiredReservations() {
        Instant now = Instant.now();
        List<Reservation> expired = reservationRepository.findByStatusAndExpiresAtBefore(ReservationStatus.PENDING, now);
        for (Reservation reservation : expired) {
            concertApi.releaseSeats(reservation.getConcertId(), reservation.getSeats());
            reservation.expire();
        }
    }
}
