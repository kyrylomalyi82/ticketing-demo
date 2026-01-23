package com.kyrylomalyi.ticketingdemo.concert;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConcertExternalAPI {

    ConcertDTO create(ConcertDTO dto);

    ConcertDTO update (Long id , ConcertDTO dto);

    ConcertDTO getById (Long id);

    Page<ConcertDTO> list(Pageable pageable);

    void delete (Long id);

}
