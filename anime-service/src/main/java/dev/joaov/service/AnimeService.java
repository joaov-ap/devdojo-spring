package dev.joaov.service;

import dev.joaov.domain.Anime;
import dev.joaov.exception.NotFoundException;
import dev.joaov.repository.AnimeHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeHardCodedRepository repository;

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Anime not found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void delete(Long id) {
        repository.delete(findByIdOrThrowNotFound(id));
    }

    public void update(Anime anime) {
        assertAnimeExists(anime.getId());
        repository.update(anime);
    }

    private void assertAnimeExists(Long id) {
        findByIdOrThrowNotFound(id);
    }
}
