package com.kyrylomalyi.ticketingdemo.reservation;

import java.time.Instant;

public record ReservationDTO(
        Long id,
        Long concertId,
        Long userId,
        int seats,
        String status,
        Instant expiresAt
) {
}
