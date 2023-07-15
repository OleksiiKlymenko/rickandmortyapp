package com.example.rickandmortyapp.dto.mapper;

import com.example.rickandmortyapp.dto.CharacterResponseDto;
import com.example.rickandmortyapp.dto.external.ApiCharacterDto;
import com.example.rickandmortyapp.model.Gender;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.model.Status;
import org.springframework.stereotype.Component;

@Component
public class MovieCharacterMapper {
    public MovieCharacter parseApiCharacterResponseDto(ApiCharacterDto dto) {
        MovieCharacter movieCharacter = new MovieCharacter();
        movieCharacter.setExternalId(dto.getId());
        movieCharacter.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        movieCharacter.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        movieCharacter.setName(dto.getName());

        return movieCharacter;
    }

    public CharacterResponseDto mapToDto(MovieCharacter movieCharacter) {
        CharacterResponseDto characterResponseDto = new CharacterResponseDto();
        characterResponseDto.setId(movieCharacter.getId());
        characterResponseDto.setExternalId(movieCharacter.getExternalId());
        characterResponseDto.setName(movieCharacter.getName());
        characterResponseDto.setGender(movieCharacter.getGender());
        characterResponseDto.setStatus(movieCharacter.getStatus());
        return characterResponseDto;
    }
}
