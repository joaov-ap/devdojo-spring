package dev.joaov.service;

import dev.joaov.domain.Anime;
import dev.joaov.repository.AnimeHardCodedRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public class AnimeService {
    private AnimeHardCodedRepository repository;

    public AnimeService() {
        this.repository = new AnimeHardCodedRepository();
    }

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public Anime save(Anime anime) {
        repository.save(anime);
        return anime;
    }

    public void delete(Long id) {
        repository.delete(findByIdOrThrowNotFound(id));
    }

    public void update(Anime anime) {
        repository.update(anime);
    }
}
