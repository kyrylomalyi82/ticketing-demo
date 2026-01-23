package com.kyrylomalyi.ticketingdemo.artist.management;

import com.kyrylomalyi.ticketingdemo.artist.ArtistDTO;
import com.kyrylomalyi.ticketingdemo.artist.mapper.ArtistMapper;
import com.kyrylomalyi.ticketingdemo.artist.model.Artist;
import com.kyrylomalyi.ticketingdemo.artist.repository.ArtistRepository;
import com.kyrylomalyi.ticketingdemo.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

class ArtistManagementTest {

    private ArtistRepository repository;
    private ArtistMapper mapper;
    private ArtistManagement service;

    @BeforeEach
    void setUp() {
        repository = mock(ArtistRepository.class);
        mapper = mock(ArtistMapper.class);
        service = new ArtistManagement(repository, mapper);
    }

    @Test
    void shouldCreateArtist() {
        ArtistDTO dto = new ArtistDTO(null, "Name", "Bio");
        Artist entity = new Artist(null, "Bio", "Name");
        Artist saved = new Artist(1L, "Bio", "Name");

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(new ArtistDTO(1L, "Name", "Bio"));

        ArtistDTO result = service.create(dto);

        assertThat(result.id()).isEqualTo(1L);
        verify(repository).save(entity);
    }

    @Test
    void shouldUpdateArtist() {
        Artist existing = new Artist(1L, "Old Bio", "Old Name");
        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(mapper.toDto(existing))
                .thenReturn(new ArtistDTO(1L, "New Name", "New Bio"));

        ArtistDTO result = service.update(1L, new ArtistDTO(null, "New Name", "New Bio"));

        assertThat(existing.getName()).isEqualTo("New Name");
        assertThat(existing.getBio()).isEqualTo("New Bio");
        assertThat(result.name()).isEqualTo("New Name");
    }

    @Test
    void shouldThrowWhenArtistNotFoundOnUpdate() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.update(1L, new ArtistDTO(null, "Name", "Bio"))
        ).isInstanceOf(ResourceNotFoundException.class);
    }
}
