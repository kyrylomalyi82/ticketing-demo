package com.kyrylomalyi.ticketingdemo.artist.mapper;

import com.kyrylomalyi.ticketingdemo.artist.ArtistDTO;
import com.kyrylomalyi.ticketingdemo.artist.model.Artist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArtistMapper {

    // Entity -> DTO
    ArtistDTO toDto(Artist artist);

    // DTO -> Entity
    Artist  toEntity(ArtistDTO dto);




}

