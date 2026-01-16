package com.kyrylomalyi.ticketingdemo.artist.repository;

import com.kyrylomalyi.ticketingdemo.artist.model.Artist;
import com.kyrylomalyi.ticketingdemo.PostgresTestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ArtistRepositoryTest extends PostgresTestContainer {

    @Autowired
    private ArtistRepository repository;

    @Test
    void shouldSaveAndLoadArtist() {
        Artist artist = new Artist(null, "Bio text", "Artist name");

        Artist saved = repository.save(artist);

        assertThat(saved.getId()).isNotNull();

        Artist found = repository.findById(saved.getId()).orElseThrow();

        assertThat(found.getName()).isEqualTo("Artist name");
        assertThat(found.getBio()).isEqualTo("Bio text");
    }

    @Test
    void shouldReturnFalseWhenArtistDoesNotExist() {
        boolean exists = repository.existsById(999L);

        assertThat(exists).isFalse();
    }
}
