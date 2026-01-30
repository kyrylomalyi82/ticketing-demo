package com.kyrylomalyi.ticketingdemo.concert.management;

import com.kyrylomalyi.ticketingdemo.artist.ArtistInternalAPI;
import com.kyrylomalyi.ticketingdemo.concert.ConcertDTO;
import com.kyrylomalyi.ticketingdemo.concert.mapper.ConcertMapper;
import com.kyrylomalyi.ticketingdemo.concert.model.Concert;
import com.kyrylomalyi.ticketingdemo.concert.repository.ConcertRepository;
import com.kyrylomalyi.ticketingdemo.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConcertManagementTest {

    private ConcertRepository repository;
    private ConcertMapper mapper;
    private ArtistInternalAPI artistApi;
    private EntityManager em;

    private ConcertManagement service;

    @BeforeEach
    void setUp() {
        repository = mock(ConcertRepository.class);
        mapper = mock(ConcertMapper.class);
        artistApi = mock(ArtistInternalAPI.class);
        em = mock(EntityManager.class);

        service = new ConcertManagement(mapper, artistApi, repository, em);
    }

    // ========= CREATE =========

    @Test
    void create_shouldCreateConcert_whenArtistExists() {
        // given
        ConcertDTO input = new ConcertDTO(
                null, 1L, "Title", "Venue", Instant.now(), 100, 0L
        );

        Concert entity = new Concert();
        Concert saved = new Concert();
        saved.setId(10L);

        when(artistApi.exists(1L)).thenReturn(true);
        when(mapper.toEntity(input)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(
                new ConcertDTO(10L, 1L, "Title", "Venue", input.startsAt(), 100, 0L)
        );

        // when
        ConcertDTO result = service.create(input);

        // then
        assertThat(result.id()).isEqualTo(10L);

        verify(artistApi).exists(1L);
        verify(mapper).toEntity(input);
        verify(repository).save(entity);
        verify(mapper).toDto(saved);
        verifyNoMoreInteractions(repository, mapper, artistApi);
    }

    @Test
    void create_shouldThrow_whenArtistDoesNotExist() {
        // given
        ConcertDTO input = new ConcertDTO(
                null, 1L, "Title", "Venue", Instant.now(), 100, 0L
        );

        when(artistApi.exists(1L)).thenReturn(false);

        // when / then
        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Artist");

        verify(artistApi).exists(1L);
        verifyNoInteractions(repository, mapper);
    }

    // ========= RESERVE =========

    @Test
    void reserveSeats_shouldReserveSeats_withPessimisticLock() {
        // given
        Concert concert = new Concert();
        concert.setCapacity(10);
        concert.setReserved(0);

        when(em.find(Concert.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(concert);

        // when
        service.reserveSeats(1L, 3);

        // then
        assertThat(concert.getReserved()).isEqualTo(3);

        verify(em).find(Concert.class, 1L, LockModeType.PESSIMISTIC_WRITE);
        verifyNoInteractions(repository);
    }

    @Test
    void reserveSeats_shouldThrow_whenConcertNotFound() {
        // given
        when(em.find(Concert.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(null);

        // when / then
        assertThatThrownBy(() -> service.reserveSeats(1L, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Concert");

        verify(em).find(Concert.class, 1L, LockModeType.PESSIMISTIC_WRITE);
    }

    @Test
    void reserveSeats_shouldThrow_whenNotEnoughSeats() {
        // given
        Concert concert = new Concert();
        concert.setCapacity(5);
        concert.setReserved(5);

        when(em.find(Concert.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(concert);

        // when / then
        assertThatThrownBy(() -> service.reserveSeats(1L, 1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Not enough available seats");
    }

    // ========= RELEASE =========

    @Test
    void releaseSeats_shouldDecreaseReservedSeats() {
        // given
        Concert concert = new Concert();
        concert.setCapacity(10);
        concert.setReserved(5);

        when(em.find(Concert.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(concert);

        // when
        service.releaseSeats(1L, 3);

        // then
        assertThat(concert.getReserved()).isEqualTo(2);
    }

    @Test
    void releaseSeats_shouldThrow_whenConcertNotFound() {
        // given
        when(em.find(Concert.class, 1L, LockModeType.PESSIMISTIC_WRITE))
                .thenReturn(null);

        // when / then
        assertThatThrownBy(() -> service.releaseSeats(1L, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Concert");
    }
}
