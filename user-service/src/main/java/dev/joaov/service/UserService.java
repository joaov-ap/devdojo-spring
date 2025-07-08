package dev.joaov.service;

import dev.joaov.domain.User;
import dev.joaov.exception.NotFoundException;
import dev.joaov.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public List<User> findAll(String firstName) {
        return firstName == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(firstName);
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
        repository.save(user);
    }

    private void assertUserExists(Long id) {
        findByIdOrThrowNotFound(id);
    }
}
