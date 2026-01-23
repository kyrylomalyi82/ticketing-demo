package com.kyrylomalyi.ticketingdemo.concert.management;

import com.kyrylomalyi.ticketingdemo.artist.ArtistInternalAPI;
import com.kyrylomalyi.ticketingdemo.concert.ConcertDTO;
import com.kyrylomalyi.ticketingdemo.concert.mapper.ConcertMapper;
import com.kyrylomalyi.ticketingdemo.concert.model.Concert;
import com.kyrylomalyi.ticketingdemo.concert.repository.ConcertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConcertManagementTest {

    private ConcertRepository repository;
    private ConcertMapper mapper;
    private ArtistInternalAPI artistApi;
    private ConcertManagement service;

    @BeforeEach
    void setUp() {
        repository = mock(ConcertRepository.class);
        mapper = mock(ConcertMapper.class);
        artistApi = mock(ArtistInternalAPI.class);
        service = new ConcertManagement(mapper, artistApi, repository);
    }

    @Test
    void shouldCreateConcertWhenArtistExists() {
        ConcertDTO dto = new ConcertDTO(
                null, 1L, "Title", "Venue", Instant.now(), 100 , 0L
        );
        Concert entity = new Concert();
        Concert saved = new Concert();
        saved.setId(10L);

        when(artistApi.exists(1L)).thenReturn(true);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(
                new ConcertDTO(10L, 1L, "Title", "Venue", dto.startsAt(), 100 , 0L)
        );

        ConcertDTO result = service.create(dto);

        assertThat(result.id()).isEqualTo(10L);
    }

    @Test
    void shouldFailCreateWhenArtistDoesNotExist() {
        when(artistApi.exists(1L)).thenReturn(false);

        assertThatThrownBy(() ->
                service.create(new ConcertDTO(
                        null, 1L, "T", "V", Instant.now(), 10 , 0L
                ))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldReserveSeats() {
        Concert concert = new Concert();
        concert.setCapacity(10);
        concert.setReserved(0);

        when(repository.findById(1L)).thenReturn(Optional.of(concert));

        service.reserveSeats(1L, 3);

        assertThat(concert.getReserved()).isEqualTo(3);
    }

    @Test
    void shouldFailWhenNotEnoughSeats() {
        Concert concert = new Concert();
        concert.setCapacity(5);
        concert.setReserved(5);

        when(repository.findById(1L)).thenReturn(Optional.of(concert));

        assertThatThrownBy(() -> service.reserveSeats(1L, 1))
                .isInstanceOf(IllegalStateException.class);
    }
}
