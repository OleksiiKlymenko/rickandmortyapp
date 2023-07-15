package com.example.rickandmortyapp.service;

import com.example.rickandmortyapp.dto.ApiCharacterDto;
import com.example.rickandmortyapp.dto.ApiResponseDto;
import com.example.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.repository.MovieCharacterRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class MovieCharacterServiceImpl implements MovieCharacterService{
    private final HttpClient httpClient;
    private final MovieCharacterRepository movieCharacterRepository;
    private final MovieCharacterMapper mapper;

    public MovieCharacterServiceImpl(HttpClient httpClient,
                                     MovieCharacterRepository movieCharacterRepository, MovieCharacterMapper mapper) {
        this.httpClient = httpClient;
        this.movieCharacterRepository = movieCharacterRepository;
        this.mapper = mapper;
    }

    @Override
    public void syncExternlaCharacters() {
        ApiResponseDto apiResponseDto = httpClient
                .get("https://rickandmortyapi.com/api/character", ApiResponseDto.class);
        saveDtosToDb(apiResponseDto);

        while (apiResponseDto.getInfo().getNext() != null) {
            apiResponseDto = httpClient
                    .get(apiResponseDto.getInfo().getNext(), ApiResponseDto.class);
            saveDtosToDb(apiResponseDto);
        }
    }

    private void saveDtosToDb(ApiResponseDto responseDto) {
        Map<Long, ApiCharacterDto> externalDtos = Arrays.stream(responseDto.getResults())
                .collect(Collectors.toMap(ApiCharacterDto::getId, Function.identity()));

        Set<Long> externalIds = externalDtos.keySet();

        List<MovieCharacter> existingCharacter = movieCharacterRepository
                .findAllByExternalIdIn(externalIds);

        Map<Long, MovieCharacter> existingCharactersWithIds = existingCharacter.stream()
                .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));

        Set<Long> existingIds = existingCharactersWithIds.keySet();

        externalIds.removeAll(existingIds);

        List<MovieCharacter> charactersToSave = externalIds.stream().map(i -> mapper.parseApiCharacterResponseDto(externalDtos.get(i)))
                .collect(Collectors.toList());

        movieCharacterRepository.saveAll(charactersToSave);
    }
}
