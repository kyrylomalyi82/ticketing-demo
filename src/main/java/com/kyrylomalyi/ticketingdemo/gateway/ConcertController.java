package com.kyrylomalyi.ticketingdemo.gateway;


import com.kyrylomalyi.ticketingdemo.concert.ConcertDTO;
import com.kyrylomalyi.ticketingdemo.concert.ConcertExternalAPI;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/concerts")
@AllArgsConstructor
public class ConcertController {

    private final ConcertExternalAPI concertApi;

    @PostMapping
    public ResponseEntity<ConcertDTO> create (@Valid @RequestBody ConcertDTO dto){
        ConcertDTO created = concertApi.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConcertDTO> update(@PathVariable Long id, @Valid @RequestBody ConcertDTO dto){
        return ResponseEntity.ok(concertApi.update(id, dto));
    }

    @GetMapping
    public ResponseEntity<Page<ConcertDTO>> list(Pageable pageable) {
        return ResponseEntity.ok(concertApi.list(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConcertDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(concertApi.getById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        concertApi.delete(id);
    }


}
