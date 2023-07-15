package com.example.rickandmortyapp.controller;

import com.example.rickandmortyapp.dto.CharacterResponseDto;
import com.example.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import com.example.rickandmortyapp.model.MovieCharacter;
import com.example.rickandmortyapp.service.MovieCharacterService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie-characters")
public class MovieCharacterController {
    private final MovieCharacterService movieCharacterService;
    private final MovieCharacterMapper movieCharacterMapper;

    public MovieCharacterController(MovieCharacterService movieCharacterService,
                                    MovieCharacterMapper movieCharacterMapper) {
        this.movieCharacterService = movieCharacterService;
        this.movieCharacterMapper = movieCharacterMapper;
    }

    @GetMapping("/synchronize")
    public String synchronizeData() {
        movieCharacterService.syncExternlaCharacters();
        return "Done!";
    }

    @GetMapping("/random")
    public CharacterResponseDto getRandom() {
        MovieCharacter randomCharacter = movieCharacterService.getRandomCharacter();
        return movieCharacterMapper.mapToDto(randomCharacter);
    }

    @GetMapping("/by-name")
    public List<CharacterResponseDto> findAllByNema(@RequestParam("name") String namePart) {
        return movieCharacterService.findAllByNameContains(namePart).stream()
                .map(movieCharacterMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
