package dev.joaov.repository;

import dev.joaov.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserHardCodedRepository {
    private final UserData userData;

    public List<User> findAll() {
        return userData.getUserList();
    }

    public Optional<User> findById(Long id) {
        return userData.getUserList().stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public List<User> findByName(String name) {
        return userData.getUserList().stream()
                .filter(u -> u.getFirstName().equalsIgnoreCase(name) || u.getLastName().equalsIgnoreCase(name))
                .toList();
    }

    public User save(User user) {
        userData.getUserList().add(user);
        return user;
    }

    public void delete(User user) {
        userData.getUserList().remove(user);
    }

    public void update(User user) {
        delete(user);
        save(user);
    }
}
