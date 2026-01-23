package com.kyrylomalyi.ticketingdemo.concert;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ConcertDTO (
        Long id,

        Long artistId,

        @NotNull
        String title,

        @NotBlank
        String venue,

        @NotNull
        @Future
        Instant startsAt,

        @Min(0)
        int capacity,

        Long version

)
{}
