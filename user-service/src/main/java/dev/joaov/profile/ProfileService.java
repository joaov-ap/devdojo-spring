package dev.joaov.profile;

import dev.joaov.domain.Profile;
import dev.joaov.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository repository;

    public List<Profile> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Profile findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Profile not found".formatted(id)));
    }

    public Profile save(Profile profile) {
        return repository.save(profile);
    }
}
