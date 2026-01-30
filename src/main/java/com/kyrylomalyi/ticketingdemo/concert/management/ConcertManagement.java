package com.kyrylomalyi.ticketingdemo.concert.management;

import com.kyrylomalyi.ticketingdemo.artist.ArtistInternalAPI;
import com.kyrylomalyi.ticketingdemo.concert.ConcertDTO;
import com.kyrylomalyi.ticketingdemo.concert.ConcertExternalAPI;
import com.kyrylomalyi.ticketingdemo.concert.ConcertInternalAPI;
import com.kyrylomalyi.ticketingdemo.concert.mapper.ConcertMapper;
import com.kyrylomalyi.ticketingdemo.concert.model.Concert;
import com.kyrylomalyi.ticketingdemo.concert.repository.ConcertRepository;
import com.kyrylomalyi.ticketingdemo.exception.ConflictException;
import com.kyrylomalyi.ticketingdemo.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PessimisticLockException;
import jakarta.persistence.LockTimeoutException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ConcertManagement implements ConcertExternalAPI, ConcertInternalAPI {

    private final ConcertMapper mapper;
    private final ArtistInternalAPI artistApi;
    private final ConcertRepository repository;
    private final EntityManager em;

    @Override
    @Transactional
    public ConcertDTO create(ConcertDTO dto) {
        if (!artistApi.exists(dto.artistId())) {
            throw new ResourceNotFoundException("Artist", dto.artistId());
        }
        Concert concert = mapper.toEntity(dto);
        return mapper.toDto(repository.save(concert));
    }

    @Override
    @Transactional
    public ConcertDTO update(Long id, ConcertDTO dto) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Concert", id);
        }
        Concert concert = mapper.toEntity(dto);
        concert.setId(id);
        Concert saved = repository.save(concert);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ConcertDTO getById(Long id) {
        return repository.findById(id).map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Concert", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ConcertDTO> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Concert", id);
        }
        repository.deleteById(id);
    }

    // ===== internal API =====

    @Override
    @Transactional(readOnly = true)
    public boolean exists(Long concertId) {
        return repository.existsById(concertId);
    }

    /**
     * Must be called from an existing transaction (Propagation.MANDATORY)
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void reserveSeats(Long concertId, int seats) {
        try {
            Concert concert = em.find(Concert.class, concertId, LockModeType.PESSIMISTIC_WRITE);
            if (concert == null) {
                throw new ResourceNotFoundException("Concert", concertId);
            }
            concert.reserve(seats);
            // no explicit flush; changes will be flushed on commit
        } catch (PessimisticLockException | LockTimeoutException ex) {
            throw new ConflictException("Could not acquire lock for concert: " + concertId);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void releaseSeats(Long concertId, int seats) {
        try {
            Concert concert = em.find(Concert.class, concertId, LockModeType.PESSIMISTIC_WRITE);
            if (concert == null) {
                throw new ResourceNotFoundException("Concert", concertId);
            }
            concert.reserve(-seats);
        } catch (PessimisticLockException | LockTimeoutException ex) {
            throw new ConflictException("Could not acquire lock for concert: " + concertId);
        }
    }
}
