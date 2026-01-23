package com.kyrylomalyi.ticketingdemo.concert.repository;

import com.kyrylomalyi.ticketingdemo.concert.model.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert , Long> {

    List<Concert> findByArtistId(Long artistId);

}
