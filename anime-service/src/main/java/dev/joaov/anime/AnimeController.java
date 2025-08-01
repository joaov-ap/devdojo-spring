package dev.joaov.anime;

import dev.joaov.domain.Anime;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
public class AnimeController {
    private final AnimeMapper mapper;
    private final AnimeService service;

    @GetMapping
    public ResponseEntity<List<AnimeGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request received to list all animes, param name '{}'", name);
        var anime = service.findAll(name);
        var animeGetResponses = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponses);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<AnimeGetResponse>> findAllPaginated(Pageable pageable) {
        log.debug("Request received to list all animes paginated");
        var animeGetResponse = service.findAllPaginated(pageable).map(mapper::toAnimeGetResponse);

        return ResponseEntity.ok(animeGetResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);
        var anime = service.findByIdOrThrowNotFound(id);
        var animeGetResponse = mapper.toAnimeGetResponse(anime);

        return ResponseEntity.ok(animeGetResponse);
    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody @Valid AnimePostRequest animePostRequest) {
        log.debug("Request to save anime : {}", animePostRequest);
        var anime = mapper.toAnime(animePostRequest);
        Anime savedAnime = service.save(anime);
        var response = mapper.toAnimePostResponse(savedAnime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);
        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Request to update anime {}", request);
        var animeToUpdate = mapper.toAnime(request);
        service.update(animeToUpdate);

        return ResponseEntity.noContent().build();
    }
}

