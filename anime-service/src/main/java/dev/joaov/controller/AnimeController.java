package dev.joaov.controller;

import dev.joaov.domain.Anime;
import dev.joaov.mapper.AnimeMapper;
import dev.joaov.request.AnimePostRequest;
import dev.joaov.request.AnimePutRequest;
import dev.joaov.response.AnimeGetResponse;
import dev.joaov.response.AnimePostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {
    private static final AnimeMapper MAPPER = AnimeMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> nomeAnimes(@RequestParam(required = false) String name) {
        log.debug("Request received to list all animes, param name '{}'", name);
        var animeGetResponse = MAPPER.toAnimeGetResponse(Anime.getAnimes());
        var response = MAPPER.toAnimeGetResponse(Anime.getAnimes().stream().filter(n -> n.getName().equalsIgnoreCase(name)).toList());
        if (name == null) return ResponseEntity.ok(animeGetResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);
        var response = MAPPER.toAnimeGetResponse(Anime.getAnimes().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found")));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
        log.debug("Request to save anime : {}", animePostRequest);
        var anime = MAPPER.toAnime(animePostRequest);
        Anime.getAnimes().add(anime);
        var response = MAPPER.toAnimePostResponse(anime);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);
        var animeToDelete = Anime.getAnimes().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));

        Anime.getAnimes().remove(animeToDelete);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {
        log.debug("Request to update anime {}", request);
        var animeToRemove = Anime.getAnimes().stream()
                .filter(a -> a.getId().equals(request.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));

        var animeUpdated = MAPPER.toAnime(request);
        Anime.getAnimes().remove(animeToRemove);
        Anime.getAnimes().add(animeUpdated);

        return ResponseEntity.noContent().build();
    }
}

