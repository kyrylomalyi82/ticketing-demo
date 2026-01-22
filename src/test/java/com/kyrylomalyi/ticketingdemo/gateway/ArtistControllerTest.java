package com.kyrylomalyi.ticketingdemo.gateway;

import com.kyrylomalyi.ticketingdemo.artist.ArtistDTO;
import com.kyrylomalyi.ticketingdemo.artist.ArtistExternalAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ArtistController.class)
class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistExternalAPI artistApi;

    @Test
    @DisplayName("POST /api/v1/artists -> 201 Created")
    void createArtist_returnsCreated() throws Exception {
        when(artistApi.create(any()))
                .thenReturn(new ArtistDTO(1L, "Hans Zimmer", "Composer"));

        mockMvc.perform(post("/api/v1/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Hans Zimmer",
                      "bio": "Composer"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hans Zimmer"));
    }

    @Test
    @DisplayName("GET /api/v1/artists/{id} -> 200 OK")
    void getById_returnsOk() throws Exception {
        when(artistApi.getById(5L))
                .thenReturn(new ArtistDTO(5L, "Artist", "Bio"));

        mockMvc.perform(get("/api/v1/artists/{id}", 5L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Artist"));
    }

    @Test
    @DisplayName("GET /api/v1/artists -> pageable list")
    void list_returnsPage() throws Exception {
        Page<ArtistDTO> page = new PageImpl<>(
                List.of(
                        new ArtistDTO(1L, "A1", "B1"),
                        new ArtistDTO(2L, "A2", "B2")
                ),
                PageRequest.of(0, 10),
                2
        );

        when(artistApi.list(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/artists?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("PUT /api/v1/artists/{id} -> 200 OK")
    void update_returnsOk() throws Exception {
        when(artistApi.update(2L, new ArtistDTO(null, "Updated", "NewBio")))
                .thenReturn(new ArtistDTO(2L, "Updated", "NewBio"));

        mockMvc.perform(put("/api/v1/artists/{id}", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "name": "Updated",
                      "bio": "NewBio"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    @DisplayName("DELETE /api/v1/artists/{id} -> 204 No Content")
    void delete_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/artists/{id}", 3L))
                .andExpect(status().isNoContent());
    }
}
