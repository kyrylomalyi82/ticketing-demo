package com.kyrylomalyi.ticketingdemo.gateway;

import com.kyrylomalyi.ticketingdemo.reservation.CreateReservationRequesDTO;
import com.kyrylomalyi.ticketingdemo.reservation.ReservationDTO;
import com.kyrylomalyi.ticketingdemo.reservation.ReservationExternalAPI;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationExternalAPI reservationApi;

    @PostMapping
    public ResponseEntity<ReservationDTO> create(
            @Valid @RequestBody CreateReservationRequesDTO request
    ) {
        ReservationDTO created = reservationApi.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirm(@PathVariable Long id) {
        reservationApi.confirm(id);
    }

    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        reservationApi.cancel(id);
    }
}
