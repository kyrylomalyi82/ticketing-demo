package com.kyrylomalyi.ticketingdemo.concert.mapper;


import com.kyrylomalyi.ticketingdemo.concert.ConcertDTO;
import com.kyrylomalyi.ticketingdemo.concert.model.Concert;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConcertMapper {

    ConcertDTO toDto(Concert concert);

    Concert toEntity(ConcertDTO dto);
}
