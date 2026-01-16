package com.kyrylomalyi.ticketingdemo.artist.mapper;

import com.kyrylomalyi.ticketingdemo.artist.ArtistDTO;
import com.kyrylomalyi.ticketingdemo.artist.model.Artist;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class ArtistMapperTest {

    private final ArtistMapper mapper = Mappers.getMapper(ArtistMapper.class);

    @Test
    void shouldMapEntityToDto() {
        Artist artist = new Artist(1L, "Bio", "Artist Name");

        ArtistDTO dto = mapper.toDto(artist);

        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Artist Name");
        assertThat(dto.bio()).isEqualTo("Bio");
    }

    @Test
    void shouldMapDtoToEntityWithoutId() {
        ArtistDTO dto = new ArtistDTO(null, "Artist Name", "Bio");

        Artist artist = mapper.toEntity(dto);

        assertThat(artist.getId()).isNull();
        assertThat(artist.getName()).isEqualTo("Artist Name");
        assertThat(artist.getBio()).isEqualTo("Bio");
    }
}
