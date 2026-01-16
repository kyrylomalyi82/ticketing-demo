-- V1__init_schema.sql

CREATE TABLE artists (
                         id BIGSERIAL PRIMARY KEY,
                         name TEXT NOT NULL,
                         bio TEXT
);

CREATE TABLE concerts (
                          id BIGSERIAL PRIMARY KEY,
                          artist_id BIGINT NOT NULL,
                          title TEXT NOT NULL,
                          venue TEXT NOT NULL,
                          starts_at TIMESTAMPTZ NOT NULL,
                          capacity INT NOT NULL CHECK (capacity >= 0),
                          reserved INT NOT NULL DEFAULT 0 CHECK (reserved >= 0),
                          version BIGINT,

                          CONSTRAINT fk_concert_artist
                              FOREIGN KEY (artist_id)
                                  REFERENCES artists(id)
                                  ON DELETE RESTRICT
);

CREATE TABLE reservations (
                              id BIGSERIAL PRIMARY KEY,
                              concert_id BIGINT NOT NULL,
                              user_id BIGINT,
                              seats INT NOT NULL CHECK (seats > 0),
                              status VARCHAR(20) NOT NULL,
                              created_at TIMESTAMPTZ NOT NULL DEFAULT now(),

                              CONSTRAINT fk_reservation_concert
                                  FOREIGN KEY (concert_id)
                                      REFERENCES concerts(id)
                                      ON DELETE CASCADE
);

CREATE INDEX idx_concerts_artist_id
    ON concerts(artist_id);

CREATE INDEX idx_reservations_concert_id
    ON reservations(concert_id);
