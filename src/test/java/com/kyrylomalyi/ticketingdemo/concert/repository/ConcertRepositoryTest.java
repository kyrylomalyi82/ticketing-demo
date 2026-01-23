package com.kyrylomalyi.ticketingdemo.concert.repository;

import com.kyrylomalyi.ticketingdemo.PostgresTestContainer;
import com.kyrylomalyi.ticketingdemo.artist.model.Artist;
import com.kyrylomalyi.ticketingdemo.concert.model.Concert;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;


import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ConcertRepositoryTest extends PostgresTestContainer {

    @Autowired
    private ConcertRepository repository;

    @Autowired
    private EntityManager em;

    @Test
    void shouldPersistConcert() {
        Artist artist = persistArtist();

        Concert concert = new Concert(
                null,
                artist.getId(),
                "Title",
                "Venue",
                Instant.now(),
                100,
                0,
                null
        );

        Concert saved = repository.save(concert);

        assertThat(saved.getId()).isNotNull();
    }



    private Artist persistArtist() {
        Artist artist = new Artist(null, "Bio", "Artist");
        em.persist(artist);
        em.flush();
        return artist;
    }
}
