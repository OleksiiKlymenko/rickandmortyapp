package com.example.rickandmortyapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class RickMortyApplication {

    public static void main(String[] args) {
        SpringApplication.run(RickMortyApplication.class, args);
    }

}
