package dev.joaov.user;

import dev.joaov.domain.User;
import dev.joaov.exception.EmailExistsException;
import dev.joaov.exception.NotFoundException;
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

//    @Transactional(rollbackFor = Exception.class)
    public User save(User user) {
        assertEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }

    public void delete(Long id) {
        repository.delete(findByIdOrThrowNotFound(id));
    }

    public void update(User user) {
        assertUserExists(user.getId());
        assertEmailDoesNotExist(user.getEmail(), user.getId());
        repository.save(user);
    }

    private void assertUserExists(Long id) {
        findByIdOrThrowNotFound(id);
    }

    private void assertEmailDoesNotExist(String email) {
        repository.findByEmail(email).ifPresent(this::throwEmailExistsException);
    }

    private void assertEmailDoesNotExist(String email, Long id) {
        repository.findByEmailAndIdNot(email, id).ifPresent(this::throwEmailExistsException);
    }

    private void throwEmailExistsException(User user) {
        throw new EmailExistsException("E-mail %s already exists".formatted(user.getEmail()));
    }
}
