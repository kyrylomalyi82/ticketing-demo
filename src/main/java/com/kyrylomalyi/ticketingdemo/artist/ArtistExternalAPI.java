package com.kyrylomalyi.ticketingdemo.artist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtistExternalAPI {
    ArtistDTO create(ArtistDTO artist);

    ArtistDTO update(Long id , ArtistDTO artistDTO);

    ArtistDTO getById(Long id);

    Page<ArtistDTO> list(Pageable pageable);

    void delete (Long id);
}
