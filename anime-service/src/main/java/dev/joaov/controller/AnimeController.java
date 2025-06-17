package dev.joaov.controller;

import dev.joaov.domain.Anime;
import dev.joaov.mapper.AnimeMapper;
import dev.joaov.request.AnimePostRequest;
import dev.joaov.request.AnimePutRequest;
import dev.joaov.response.AnimeGetResponse;
import dev.joaov.response.AnimePostResponse;
import dev.joaov.service.AnimeService;
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
    private AnimeService service;

    public AnimeController() {
        this.service = new AnimeService();
    }

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all animes, param name '{}'", name);
        var anime = service.findAll(name);
        var animeGetResponses = MAPPER.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponses);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);
        var anime = service.findByIdOrThrowNotFound(id);
        var animeGetResponse = MAPPER.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest animePostRequest) {
        log.debug("Request to save anime : {}", animePostRequest);
        var anime = MAPPER.toAnime(animePostRequest);
        Anime savedAnime = service.save(anime);
        var response = MAPPER.toAnimePostResponse(savedAnime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {
        log.debug("Request to update anime {}", request);
        var animeToUpdate = MAPPER.toAnime(request);
        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }
}

