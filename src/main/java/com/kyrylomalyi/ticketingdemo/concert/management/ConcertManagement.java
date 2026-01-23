package com.kyrylomalyi.ticketingdemo.concert.management;


import com.kyrylomalyi.ticketingdemo.artist.ArtistInternalAPI;
import com.kyrylomalyi.ticketingdemo.concert.ConcertDTO;
import com.kyrylomalyi.ticketingdemo.concert.ConcertExternalAPI;
import com.kyrylomalyi.ticketingdemo.concert.ConcertInternalAPI;
import com.kyrylomalyi.ticketingdemo.concert.mapper.ConcertMapper;
import com.kyrylomalyi.ticketingdemo.concert.model.Concert;
import com.kyrylomalyi.ticketingdemo.concert.repository.ConcertRepository;
import com.kyrylomalyi.ticketingdemo.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Transactional
@AllArgsConstructor
public class ConcertManagement implements ConcertExternalAPI , ConcertInternalAPI {

    private final ConcertMapper mapper;
    private final ArtistInternalAPI artistApi;
    private final ConcertRepository repository;


    // =-EXTERNAL API-=
    @Override
    public ConcertDTO create(ConcertDTO dto) {
        if (!artistApi.exists(dto.artistId())){
            throw new ResourceNotFoundException("Artist" + dto.artistId());
        }

        Concert concert = mapper.toEntity(dto);
        return mapper.toDto(repository.save(concert));
    }

    @Override
    public ConcertDTO update(Long id, ConcertDTO dto) {

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Concert " ,id);
        }

        Concert concert = mapper.toEntity(dto);
        concert.setId(id);

        Concert saved = repository.save(concert);
        return mapper.toDto(saved);
    }


    @Override
    public ConcertDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDto).orElseThrow(() -> new ResourceNotFoundException("Concert " ,id ));
    }

    @Override
    public Page<ConcertDTO> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Concert", id);
        }
        repository.deleteById(id);
    }


    // =-internal API-=
    @Override
    public boolean exists(Long concertId) {
        return repository.existsById(concertId);
    }

    @Override
    public void reserveSeats(Long concertId, int seats) {
        Concert concert = repository.findById(concertId).orElseThrow(() -> new IllegalArgumentException("Concert not found"));

        concert.reserve(seats);
    }
}
