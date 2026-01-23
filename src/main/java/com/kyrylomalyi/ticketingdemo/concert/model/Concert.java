package com.kyrylomalyi.ticketingdemo.concert.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="artist_id" , nullable = false)
    private Long artistId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String venue;

    @Column(name= "starts_at" , nullable = false)
    private Instant startsAt;

   @Column(nullable = false)
    private int capacity;

   @Column(nullable = false)
    private int reserved = 0;

    @Version
    @Column(name = "version")
    private Long version;


    public void update(String title, String venue, Instant startsAt, int capacity) {
        this.title = title;
        this.venue = venue;
        this.startsAt = startsAt;
        this.capacity = capacity;
    }

    public boolean hasAvailableSeats(int seats) {
        return reserved + seats <= capacity;
    }

    public void reserve(int seats) {
        if (!hasAvailableSeats(seats)) {
            throw new IllegalStateException("Not enough available seats");
        }
        this.reserved += seats;
    }
}
