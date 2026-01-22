package com.kyrylomalyi.ticketingdemo.gateway;

import com.kyrylomalyi.ticketingdemo.artist.ArtistDTO;
import com.kyrylomalyi.ticketingdemo.artist.ArtistExternalAPI;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/artists")
public class ArtistController {

    private final ArtistExternalAPI artistApi;

    public ArtistController(ArtistExternalAPI artistApi) {
        this.artistApi = artistApi;
    }

    @PostMapping
    public ResponseEntity<ArtistDTO> create(@Valid @RequestBody ArtistDTO dto) {
        ArtistDTO created = artistApi.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ArtistDTO dto
    ) {
        return ResponseEntity.ok(artistApi.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(artistApi.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ArtistDTO>> list(Pageable pageable) {
        return ResponseEntity.ok(artistApi.list(pageable));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        artistApi.delete(id);
    }
}

