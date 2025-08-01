package dev.joaov.anime;

import dev.joaov.domain.Anime;
import dev.joaov.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository repository;

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Page<Anime> findAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
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
        repository.save(anime);
    }

    private void assertAnimeExists(Long id) {
        findByIdOrThrowNotFound(id);
    }
}
