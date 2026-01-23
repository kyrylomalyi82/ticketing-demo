package com.kyrylomalyi.ticketingdemo.artist.management;


import com.kyrylomalyi.ticketingdemo.artist.ArtistDTO;
import com.kyrylomalyi.ticketingdemo.artist.ArtistExternalAPI;
import com.kyrylomalyi.ticketingdemo.artist.ArtistInternalAPI;
import com.kyrylomalyi.ticketingdemo.artist.mapper.ArtistMapper;
import com.kyrylomalyi.ticketingdemo.artist.model.Artist;
import com.kyrylomalyi.ticketingdemo.artist.repository.ArtistRepository;
import com.kyrylomalyi.ticketingdemo.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ArtistManagement implements ArtistExternalAPI , ArtistInternalAPI {

    private final ArtistRepository repository;
    private final ArtistMapper mapper;


    public ArtistManagement(ArtistRepository repository, ArtistMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    // External API

    @Override
    public ArtistDTO create (ArtistDTO dto) {
        Artist artist = mapper.toEntity(dto);
        return mapper.toDto(repository.save(artist));
    }

    @Override
    public ArtistDTO update(Long id, ArtistDTO artistDTO) {
        Artist artist = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Artist", id));
        artist.update(artistDTO.name(), artistDTO.bio());
        return mapper.toDto(artist);
    }

    @Override
    public ArtistDTO getById(Long id) {

        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Artist" , id));
    }

    @Override
    public Page<ArtistDTO> list(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Override
    public void delete(Long id) {
        if(!repository.existsById(id)){
            throw new ResourceNotFoundException("Artist" , id);
        }
        repository.deleteById(id);
    }

    // Internal API
    @Override
    public boolean exists(Long id) {
        return repository.existsById(id);
    }
}
