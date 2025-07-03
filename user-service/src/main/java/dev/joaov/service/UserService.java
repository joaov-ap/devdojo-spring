package dev.joaov.service;

import dev.joaov.domain.User;
import dev.joaov.exception.NotFoundException;
import dev.joaov.repository.UserHardCodedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserHardCodedRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll()  : repository.findByName(name);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.delete(findByIdOrThrowNotFound(id));
    }

    public void update(User user) {
        assertUserExists(user.getId());
        repository.update(user);
    }

    private void assertUserExists(Long id) {
        findByIdOrThrowNotFound(id);
    }
}
