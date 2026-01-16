package com.kyrylomalyi.ticketingdemo.artist.repository;

import com.kyrylomalyi.ticketingdemo.artist.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    boolean existsById(Long id);
}

