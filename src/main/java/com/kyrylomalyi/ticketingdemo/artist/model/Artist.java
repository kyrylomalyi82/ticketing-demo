package com.kyrylomalyi.ticketingdemo.artist.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table (name ="artists")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String bio;

    @Column(nullable = false)
    private String name;

    public void update(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }
}
