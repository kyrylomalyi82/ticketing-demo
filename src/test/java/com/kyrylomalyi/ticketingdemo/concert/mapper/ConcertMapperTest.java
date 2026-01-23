package com.kyrylomalyi.ticketingdemo.concert.mapper;

import com.kyrylomalyi.ticketingdemo.concert.ConcertDTO;
import com.kyrylomalyi.ticketingdemo.concert.model.Concert;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ConcertMapperTest {

    private final ConcertMapper mapper =
            Mappers.getMapper(ConcertMapper.class);

    @Test
    void shouldMapEntityToDto() {
        Concert concert = new Concert(
                1L,
                10L,
                "Title",
                "Venue",
                Instant.now(),
                100,
                5,
                2L
        );

        ConcertDTO dto = mapper.toDto(concert);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.artistId()).isEqualTo(10L);
        assertThat(dto.capacity()).isEqualTo(100);
    }

    @Test
    void shouldMapDtoToEntityIgnoringReservedAndVersion() {
        ConcertDTO dto = new ConcertDTO(
                null,
                10L,
                "Title",
                "Venue",
                Instant.now(),
                100,
                null
        );

        Concert concert = mapper.toEntity(dto);

        assertThat(concert.getId()).isNull();
        assertThat(concert.getReserved()).isZero();
        assertThat(concert.getVersion()).isNull();
    }
}
