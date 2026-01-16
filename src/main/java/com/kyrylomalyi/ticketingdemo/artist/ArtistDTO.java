package com.kyrylomalyi.ticketingdemo.artist;

import jakarta.validation.constraints.NotBlank;

public record ArtistDTO(
        Long id,
        @NotBlank String name,
        String bio
)
{}
